
public class Largest {

	public static int largest(int[] list) {
		int index, max=Integer.MAX_VALUE;
		for (index = 0; index < list.length-1; index++) {
			System.out.println("index = " + index );
			if (list[index] > max) {
				max = list[index];
			}
		}
		return max;
	}
}
