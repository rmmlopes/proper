package ca.ruiandjenn.proper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ca.ruiandjenn.proper.exceptions.DuplicateKeyException;



public class XSLToProps extends Command {

	static {
		Command.commands.add(new XSLToProps());
	}
	
	@Override
	public void run() {
    	HierarchicalConfiguration config = getConfig();
    	if (config == null) {
    		System.exit(-2);
    	}
  
    	try {
			processConfig(config);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getName() {
		return "XLSToProps";
	} 
	
    protected void processConfig(HierarchicalConfiguration config) throws IOException, DuplicateKeyException {
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
	
	@Override
    public List<PropertiesMapLocal> processWorkbook(HierarchicalConfiguration wkbConfig) throws IOException, DuplicateKeyException {
    	List<PropertiesMapLocal> props = new ArrayList<PropertiesMapLocal>();
    	Workbook wkb = getWorkBook(wkbConfig.getString("filename"));
    	List<?> sheets= wkbConfig.getList("sheet.name");
    	for (int i = 0; i < sheets.size(); i++) {
    		HierarchicalConfiguration sheetConfig = wkbConfig.configurationAt("sheet(" + i + ")");
    		writeMessage("Processing: " + wkbConfig.getString("filename") + " - " + sheetConfig.getString("name"));
        	Sheet sheet = wkb.getSheet(sheetConfig.getString("name"));
        	int keysColumn = sheetConfig.getInt("keys");
        	List<?> langs = sheetConfig.getList("language.column");
        	for (int j = 0; j < langs.size(); j++) {
    			HierarchicalConfiguration langConfig = (HierarchicalConfiguration)sheetConfig.configurationAt("language(" + j + ")");
    			Map<String, String> values = readXSL(sheet, keysColumn, langConfig.getInt("column"), sheetConfig.getInt("startRow"));
    			writeMessage("Num keys: " + values.keySet().size());
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
    			writeMessage("Changed:");
    			for (String s : changed) {
    				writeMessage(s);
    			}
    			writeMessage("New:");
    			for (String s : added) {
    				writeMessage(s);
    			}
    			writeMessage("Unchanged:");
    			for (String s : unchanged) {
    				writeMessage(s);
    			}
    			props.add(propX);
        	}
    	}
    	
    	return props;
    }
    
    protected Map<String, String> readXSL(Sheet sheet, int keyColumn, int valColumn, int startRow) throws DuplicateKeyException {
    	Map<String, String> props = new HashMap<String, String>();
    	
    	writeMessage("Reading XSL: " + sheet.getSheetName());
    	for (Row r : sheet) {
    		if (r.getRowNum() < startRow) {
    			continue;
    		}
    		
    		Cell c = r.getCell(keyColumn);
    		if (c == null) {
    			continue;
    		}
    		String key = r.getCell(keyColumn).getStringCellValue().trim();
    		c = r.getCell(valColumn);
    		String val = c == null ? "" : c.getStringCellValue().trim();
    		if (key.length() > 0) {
    			if (props.containsKey(key)) {
    				if (props.get(key).equals(val)) {
    					writeMessage("*** DUPLICATE - IDENTICAL VALUE *** " + key);	
    				}
    				else {
    					// A key with duplicate values found in the Excel spreadsheet
    					throw new DuplicateKeyException(key);
    				}
    			}
    			else {
    				writeMessage(key);
    			}
    			props.put(key,  val);
    		}
    	}
    	
    	return props;
    }

}
