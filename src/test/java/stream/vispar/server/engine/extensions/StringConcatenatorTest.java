package stream.vispar.server.engine.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.query.api.definition.Attribute;


public class StringConcatenatorTest {
	
	private StringConcatenator concatenator;
	
	
	@Before
	public void setUp() throws Exception {
		concatenator = new StringConcatenator();
	}
	
	@Test
	public void testToString() {
		concatenator.processAdd(new String[] {"Hello", "World"});
		assertEquals("HelloWorld", concatenator.toString());
	}
	
	@Test
	public void testGetReturnType() {
		assertEquals(Attribute.Type.STRING, concatenator.getReturnType());
	}
	
	@Test
	public void testProcessAddObject() {
		assertEquals("Hello", concatenator.processAdd("Hello"));
		assertEquals("HelloWorld", concatenator.processAdd("World"));
	}
	
	@Test
	public void testProcessAddObjectArray() {
		assertEquals("HelloWorld", concatenator.processAdd(new String[] {"Hello", "World"}));
	}
	
	@Test
	public void testProcessRemoveObject() {
		concatenator.processAdd(new String[] {"Hello", "World"});
		assertEquals("Hello", concatenator.processRemove("World"));
		
		concatenator.processAdd("World");
		assertEquals("World", concatenator.processRemove("Hello"));
	}
	
	@Test
	public void testProcessRemoveObjectArray() {
		concatenator.processAdd(new String[] {"Hello", "World", "foo", "bar"});
		assertEquals("Hellofoo", concatenator.processRemove(new String[] {"World", "bar"}));
	}
	
	@Test
	public void testReset() {
		concatenator.processAdd(new String[] {"Hello", "World"});
		assertEquals("", concatenator.reset());
		
		assertEquals("foo", concatenator.processAdd("foo"));
	}
	
	@Test
	public void testState() {
		concatenator.processAdd(new String[] {"Hello", "World"});
		
		Object[] state = concatenator.currentState();
		
		concatenator.reset();
		
		concatenator.restoreState(state);
		assertEquals("HelloWorld", concatenator.toString());
	}
	
}
