package ca.ruiandjenn.proper;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ca.ruiandjenn.proper.exceptions.DuplicateKeyException;

/**
 * Unit test for simple App.
 */
public class XSLToPropsTest extends TestCase {
	private Sheet getSheet(int sheetIdx) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("DLXContent.xls");
        Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheetAt(sheetIdx);
		return sheet;
	}
	
	Map<String, String> getEnglish (XSLToProps xtp, Sheet s) throws Exception {
		return xtp.readXSL(s, 0, 2, 5);
	}
	
	Map<String, String> getFrench (XSLToProps xtp, Sheet s) throws Exception {
		return xtp.readXSL(s, 0, 3, 5);
	}
	
	public void testreadXLS() throws Exception {
		XSLToProps xtp = new XSLToProps();
		Sheet s = getSheet(0);
		Map<String, String> english = getEnglish(xtp, s);
		Map<String, String> french = getFrench(xtp, s);
		
		assertEquals("BOOK A FLIGHT", english.get("paymentError.button.title"));
		assertEquals("Réserver un vol", french.get("paymentError.button.title"));
	}
	
	public void testHandleWhitespace() throws Exception {
		XSLToProps xtp = new XSLToProps();
		Sheet s = getSheet(0);
		Map<String, String> english = getEnglish(xtp, s);
		Map<String, String> french = getFrench(xtp, s);
		
		assertTrue(english.containsKey("key.with.whitespace"));
		assertTrue(french.containsKey("key.with.whitespace"));
		assertEquals("value with leading and trailing\nwhitespace", english.get("key.with.whitespace"));
		assertEquals("french leading and\ntrailing\nwhitespace", french.get("key.with.whitespace"));
	}
	
	public void testDuplicateKeysSameValue() throws Exception {
		XSLToProps xtp = new XSLToProps();
		Sheet s = getSheet(0);
		Map<String, String> english = getEnglish(xtp, s);
		Map<String, String> french = getFrench(xtp, s);
		
		assertEquals("foo", english.get("repeated.with.same.value"));
		assertEquals("foo", french.get("repeated.with.same.value"));
	}

	public void testDuplicateKeysDifferentValues() throws Exception {
		try {
			XSLToProps xtp = new XSLToProps();
			Sheet s = getSheet(2);
			xtp.readXSL(s, 0, 1, 0);
		}
		catch (DuplicateKeyException expected) {
			return;
		}
		fail("Should have got DuplicateKeyException");
	}
	
	public void testProcessConfig() throws Exception {
		XSLToProps xtp = new XSLToProps();
		HierarchicalConfiguration c = xtp.getConfig();
		xtp.processConfig(c);
	}

}
