package stream.vispar.server.engine.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An aggregation that returns the lexicographically median string.
 * 
 * @author Jonas
 */
public class StringAverage extends StringAggregation {
	
	private List<String> strings = new ArrayList<>();
	
	@Override
	public Object processAdd(Object data) {
		strings.add((String) data);
		
		Collections.sort(strings);
		
		return getAverage();
	}
	
	@Override
	public Object processRemove(Object data) {
		strings.remove(data);
		return getAverage();
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
	
	@Override
	public void restoreState(Object[] state) {
		strings = (List<String>) state[0];
	}
	
	
	private String getAverage() {
		return strings.get(strings.size() / 2);
	}
	
}
