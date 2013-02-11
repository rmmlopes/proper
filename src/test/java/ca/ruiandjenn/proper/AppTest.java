package ca.ruiandjenn.proper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	private Sheet getFirstSheet() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("DLXContent.xls");
        Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheetAt(0);
		return sheet;
	}
	
	Map<String, String> getEnglish (Sheet s) {
		return App.readXLS(s, 0, 2, 5);
	}
	
	Map<String, String> getFrench (Sheet s) {
		return App.readXLS(s, 0, 3, 5);
	}
	
	public void testreadXLS() throws Exception {
		Sheet s = getFirstSheet();
		Map<String, String> english = getEnglish(s);
		Map<String, String> french = getFrench(s);
		
		assertEquals("BOOK A FLIGHT", english.get("paymentError.button.title"));
		assertEquals("Réserver un vol", french.get("paymentError.button.title"));
	}
	
	public void testHandleWhitespace() throws Exception {
		Sheet s = getFirstSheet();
		Map<String, String> english = getEnglish(s);
		Map<String, String> french = getFrench(s);
		
		assertTrue(english.containsKey("key.with.whitespace"));
		assertTrue(french.containsKey("key.with.whitespace"));
		assertEquals("value with leading and trailing\nwhitespace", english.get("key.with.whitespace"));
		assertEquals("french leading and\ntrailing\nwhitespace", french.get("key.with.whitespace"));
	}
	
	public void testProcessConfig() throws Exception {
		HierarchicalConfiguration c = App.getConfig();
		App.processConfig(c);
	}

}
