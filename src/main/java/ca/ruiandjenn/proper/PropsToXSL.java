package ca.ruiandjenn.proper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PropsToXSL extends App {
	/*
	
    public static void main( String[] args )
    {
    	App.main(args);
    }
    
    @Override
    protected List<PropertiesMapLocal> processWorkbook(HierarchicalConfiguration wkbConfig) throws IOException {
    	System.out.println("Calling process");
    	return null;
    	
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
    	
    }*/
}
