package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class StringMinimumTest {
	
	private StringMinimum aggregation;
	
	
	@Before
	public void setUp() throws Exception {
		aggregation = new StringMinimum();
	}
	
	@Test
	public void testProcessAddObject() {
		assertEquals("c", aggregation.processAdd("c"));
		assertEquals("b", aggregation.processAdd("b"));
		assertEquals("a", aggregation.processAdd("a"));
		assertEquals("a", aggregation.processAdd("x"));
	}
	
	@Test
	public void testProcessRemoveObject() {
		aggregation.processAdd("a");
		aggregation.processAdd("b");
		aggregation.processAdd("e");
		aggregation.processAdd("d");
		aggregation.processAdd("c");
		
		assertEquals("a", aggregation.processRemove("d")); // (a, b, c, e)
		assertEquals("a", aggregation.processRemove("b")); // (a, c, e)
		assertEquals("c", aggregation.processRemove("a")); // (c, e)
		assertEquals("c", aggregation.processRemove("e")); // (c)
	}
	
	@Test
	public void testReset() {
		aggregation.processAdd("a");
		aggregation.processAdd("e");
		
		assertEquals("", aggregation.reset());
		
		assertEquals("b", aggregation.processAdd("b"));
	}
	
}
