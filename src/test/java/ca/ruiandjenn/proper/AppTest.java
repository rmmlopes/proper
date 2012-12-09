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
	public void testreadXLS() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("DLXContent.xls");
        Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheetAt(0);
		Map<Integer, String> colToLang = new HashMap<Integer, String>();
		colToLang.put(2, "en");
		colToLang.put(3, "fr");
		
		Map<String, String> english = App.readXLS(sheet, 0, 2, 5);
		Map<String, String> french = App.readXLS(sheet, 0, 3, 5);
		assertEquals("BOOK A FLIGHT", english.get("paymentError.button.title"));
		assertEquals("Réserver un vol", french.get("paymentError.button.title"));
	}
	
	public void testProcessConfig() throws Exception {
		HierarchicalConfiguration c = App.getConfig();
		App.processConfig(c);
	}

}
