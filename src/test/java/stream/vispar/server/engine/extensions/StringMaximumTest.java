package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class StringMaximumTest {
	
	private StringMaximum aggregation;
	
	
	@Before
	public void setUp() throws Exception {
		aggregation = new StringMaximum();
	}
	
	@Test
	public void testProcessAddObject() {
		assertEquals("a", aggregation.processAdd("a"));
		assertEquals("c", aggregation.processAdd("c"));
		assertEquals("c", aggregation.processAdd("b"));
		assertEquals("x", aggregation.processAdd("x"));
	}
	
	@Test
	public void testProcessRemoveObject() {
		aggregation.processAdd("a");
		aggregation.processAdd("b");
		aggregation.processAdd("e");
		aggregation.processAdd("d");
		aggregation.processAdd("c");
		
		assertEquals("e", aggregation.processRemove("d")); // (a, b, c, e)
		assertEquals("e", aggregation.processRemove("b")); // (a, c, e)
		assertEquals("c", aggregation.processRemove("e")); // (a, c)
		assertEquals("c", aggregation.processRemove("a")); // (c)
	}
	
	@Test
	public void testReset() {
		aggregation.processAdd("a");
		aggregation.processAdd("e");
		
		assertEquals("", aggregation.reset());
		
		assertEquals("b", aggregation.processAdd("b"));
	}
	
}
