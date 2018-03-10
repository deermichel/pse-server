package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class StringAverageTest {
	
	private StringAverage aggregation;
	
	
	@Before
	public void setUp() throws Exception {
		aggregation = new StringAverage();
	}
	
	@Test
	public void testProcessAddObject() {
		assertEquals("a", aggregation.processAdd("a")); // (a)
		
		aggregation.processAdd("c");
		assertEquals("b", aggregation.processAdd("b")); // (a, b, c)
		
		aggregation.processAdd("y");
		assertEquals("c", aggregation.processAdd("x")); // (a, b, c, x, y)
	}
	
	@Test
	public void testProcessRemoveObject() {
		aggregation.processAdd("a");
		aggregation.processAdd("b");
		aggregation.processAdd("c");
		aggregation.processAdd("d");
		
		assertEquals("b", aggregation.processRemove("c"));
		aggregation.processRemove("a");
		assertEquals("d", aggregation.processRemove("b"));
	}
	
	
	@Test
	public void testReset() {
		aggregation.processAdd("a");
		aggregation.processAdd("b");
		aggregation.processAdd("c");
		aggregation.processAdd("d");
		
		assertEquals("", aggregation.reset());
		assertEquals("x", aggregation.processAdd("x"));
	}
	
}
