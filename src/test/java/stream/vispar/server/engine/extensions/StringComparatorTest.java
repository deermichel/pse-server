package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.wso2.siddhi.query.api.definition.Attribute;


public class StringComparatorTest {
	
	private static StringComparator comparator;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		comparator = new StringComparator();
	}
	
	@Test
	public void testGetReturnType() {
		assertEquals(Attribute.Type.INT, comparator.getReturnType());
	}
	
	@Test
	public void testExecuteObjectArray() {
		testStrings("a", "b");
		testStrings("b", "a");
		testStrings("aa", "a");
		testStrings("a", "A");
		testStrings("foo", "bar");
	}
	
	
	private void testStrings(String first, String second) {
		if (first.compareTo(second) < 0) {
			assertTrue((int) comparator.execute(new String[] {first, second}) < 0);
		} else if (first.compareTo(second) > 0) {
			assertTrue((int) comparator.execute(new String[] {first, second}) > 0);
		} else if (first.compareTo(second) == 0) {
			assertTrue((int) comparator.execute(new String[] {first, second}) == 0);
		}
	}
	
}
