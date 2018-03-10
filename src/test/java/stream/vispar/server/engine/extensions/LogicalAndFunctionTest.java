package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class LogicalAndFunctionTest {
	
	private LogicalAndFunction function;
	
	
	@Before
	public void setUp() throws Exception {
		function = new LogicalAndFunction();
	}
	
	@Test
	public void testExecuteObjectArray() {
		assertFalse((boolean) function.execute(new Object[] {0l, 0l}));
		assertFalse((boolean) function.execute(new Object[] {0l, 1l}));
		
		assertTrue((boolean) function.execute(new Object[] {1l, 1l}));
		
		assertFalse((boolean) function.execute(new Object[] {0l, 0l}));
		assertFalse((boolean) function.execute(new Object[] {2l, 1l}));
		
		assertTrue((boolean) function.execute(new Object[] {2l, 2l}));
	}
	
}
