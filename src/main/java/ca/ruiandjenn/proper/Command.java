package ca.ruiandjenn.proper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ca.ruiandjenn.proper.exceptions.DuplicateKeyException;

public abstract class Command {
	protected static List<Command> commands = new ArrayList<Command>();
	protected boolean verbose = false;
	
    protected HierarchicalConfiguration getConfig() {
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
    
    protected Workbook getWorkBook(String filename) {
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
    
    public void writeMessage(String message) {
    	if (verbose) {
    		System.out.println(message);
    	}
    }
    
    public void setVerbose(boolean v) {
    	verbose = v;
    }
    
    public abstract List<PropertiesMapLocal> processWorkbook(HierarchicalConfiguration wkbConfig) throws IOException, DuplicateKeyException;
    
    public abstract String getName();
    
    public abstract void run();
}
