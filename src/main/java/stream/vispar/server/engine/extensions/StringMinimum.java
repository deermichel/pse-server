package stream.vispar.server.engine.extensions;

import java.util.TreeSet;

/**
 * An aggregation that returns the lexicographically first string.
 * 
 * @author Jonas
 */
public class StringMinimum extends StringAggregation {
	
	private TreeSet<String> strings = new TreeSet<>();
	
	
	@Override
	public Object processAdd(Object data) {
		String next = (String) data;
		
		strings.add(next);
		
		return strings.first();
	}
	
	@Override
	public Object processRemove(Object data) {
		strings.remove(data);
		return strings.first();
	}
	
	@Override
	public Object reset() {
		strings.clear();
		return "";
	}
	
	@Override
	public Object[] currentState() {
		return new Object[] {strings};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreState(Object[] state) {
		strings = (TreeSet<String>) state[0];
	}
	
}
