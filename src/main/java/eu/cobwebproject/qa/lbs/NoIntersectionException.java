package eu.cobwebproject.qa.lbs;

/**
 * Exception thrown when there is no intersection with the heightmap
 * within the maximum view distance in Line Of Sight calculation
 * 
 * @author Environment Systems - sebastian.clarke@envsys.co.uk
 *
 */
public class NoIntersectionException extends IntersectionException {
	public NoIntersectionException(String message) {
		super(message);
	}
}
