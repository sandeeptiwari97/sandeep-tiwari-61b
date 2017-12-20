/** Performs some basic linked list tests. */
public class atest {

	/* Utility method for printing out empty checks. */

	public static void main(String[] args) {
		ArrayDeque<String> asdf = new ArrayDeque<>();
		asdf.addLast("a");
		System.out.println(asdf.removeFirst());
		asdf.addFirst("z");
		System.out.println(asdf.get(0));
	}
} 