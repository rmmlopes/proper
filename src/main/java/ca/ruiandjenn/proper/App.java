package ca.ruiandjenn.proper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class App 
{
    public static void main( String[] args )
    {
    	HierarchicalConfiguration config = getConfig();
    	if (config == null) {
    		System.exit(-2);
    	}
  
    	try {
			processConfig(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    protected static HierarchicalConfiguration getConfig() {
    	try
    	{
    	    XMLConfiguration config = new XMLConfiguration("proper-config.xml");
    	    return config;
    	}
    	catch(ConfigurationException cex)
    	{
    		cex.printStackTrace();
    	}
    	
    	return null;
    }
    
    protected static void processConfig(HierarchicalConfiguration config) throws IOException {
    	List<PropertiesMapLocal> props = new ArrayList<PropertiesMapLocal>();
    	List<?> wkbs = config.getList("workbook.filename");
		for (int i = 0; i < wkbs.size(); i++) {
			HierarchicalConfiguration wkbConfig = config.configurationAt("workbook(" + i + ")");
			props.addAll(processWorkbook(wkbConfig));
		}
		for (PropertiesMapLocal pm : props) {
			pm.store();
		}
    }
    
    protected static List<PropertiesMapLocal> processWorkbook(HierarchicalConfiguration wkbConfig) throws IOException {
    	List<PropertiesMapLocal> props = new ArrayList<PropertiesMapLocal>();
    	Workbook wkb = getWorkBook(wkbConfig.getString("filename"));
    	List<?> sheets= wkbConfig.getList("sheet.name");
    	for (int i = 0; i < sheets.size(); i++) {
    		HierarchicalConfiguration sheetConfig = wkbConfig.configurationAt("sheet(" + i + ")");
    		System.out.println("Processing: " + wkbConfig.getString("filename") + " - " + sheetConfig.getString("name"));
        	Sheet sheet = wkb.getSheet(sheetConfig.getString("name"));
        	int keysColumn = sheetConfig.getInt("keys");
        	List<?> langs = sheetConfig.getList("language.column");
        	for (int j = 0; j < langs.size(); j++) {
    			HierarchicalConfiguration langConfig = (HierarchicalConfiguration)sheetConfig.configurationAt("language(" + j + ")");
    			Map<String, String> values = readXLS(sheet, keysColumn, langConfig.getInt("column"), sheetConfig.getInt("startRow"));
    			System.out.println("Num keys: " + values.keySet().size());
    			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    			PropertiesMapLocal propX = new PropertiesMapLocal(map);             
    			propX.load(langConfig.getString("propFile"));
    			List<String> unchanged = new ArrayList<String>();
    			List<String> changed = new ArrayList<String>();
    			List<String> added = new ArrayList<String>();
    			for (String valKey : values.keySet()) {
    				if (map.containsKey(valKey)) {
    					if (values.get(valKey).equals(map.get(valKey))) {
    						unchanged.add(valKey);
    					}
    					else {
    						changed.add(valKey);
    						map.put(valKey, values.get(valKey));
    					}
    				}
    				else {
    					added.add(valKey);
    					map.put(valKey, values.get(valKey));
    				}
    				
    			}
    			System.out.println("Changed:");
    			for (String s : changed) {
    				System.out.println(s);
    			}
    			System.out.println("New:");
    			for (String s : added) {
    				System.out.println(s);
    			}
    			System.out.println("Unchanged:");
    			for (String s : unchanged) {
    				System.out.println(s);
    			}
    			props.add(propX);
        	}
    	}
    	
    	return props;
    }
    
    protected static Workbook getWorkBook(String filename) {
    	InputStream is = null;
    	
        try {
			is = new FileInputStream(filename);
		} 
        catch (FileNotFoundException e) {
        	is = ClassLoader.getSystemResourceAsStream(filename);
		}
        
		try {
			Workbook wb = WorkbookFactory.create(is);
			return wb;
		} 
		catch (InvalidFormatException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
    }
    
    protected static Map<String, String> readXLS(Sheet sheet, int keyColumn, int valColumn, int startRow) {
    	Map<String, String> props = new HashMap<String, String>();
    	
    	System.out.println("Reading XLS:");
    	for (Row r : sheet) {
    		if (r.getRowNum() < startRow) {
    			continue;
    		}
    		
    		String key = r.getCell(keyColumn).getStringCellValue();
    		Cell c = r.getCell(valColumn);
    		String val = c == null ? "" : c.getStringCellValue();
    		if (key.length() > 0) {
    			if (props.containsKey(key)) {
    				if (props.get(key).equals(val)) {
    					System.out.println("*** DUPLICATE - IDENTICAL VALUE *** " + key);	
    				}
    				else {
    					System.out.println("*** DUPLICATE - CHANGED VALUE *** " + key);
    					System.out.println("*** DUPLICATE - PREVIOUS VALUE = " + props.get(key));
    					System.out.println("*** DUPLICATE - NEW VALUE = " + val);
    				}
    			}
    			else {
    				System.out.println(key);
    			}
    			props.put(key,  val);
    		}
    	}
    	
    	return props;
    }    
}

