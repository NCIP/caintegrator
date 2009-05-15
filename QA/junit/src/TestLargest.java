import junit.framework.*;

public class TestLargest extends TestCase{

	public TestLargest(String name) {
		super(name);
	}
	public void testOrder() {
		System.out.println("just entered testOrder");
		assertEquals(9, Largest.largest(new int[] {8,9,7}));
	}
	
}
