/*
 * UnitTest.java 
 * called before everything
 * only used by LispInterpreter.java
 * 
 * Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
public class UnitTest {
	// I didn't find a way to automatically
	// test subroutine which needs user input
	public static void test() {
		// SExptoStringTest
		SExp s1 = new SExp();
		s1.setAtom(true).setNum(true).setValue(23);
		if (true == SExptoStringTest(s1, "23")) {
			System.out.println("SExptoStringTest passed.");
		} else {
			// if self test failed, we don't need
			// to run anymore, so RuntimeException is thrown
			throw new RuntimeException("SExptoStringTest failed");
		}

		// SExptoStringTest
		SExp s2 = new SExp();
		s2.setAtom(true).setNum(false).setName("ab");
		if (true == SExptoStringTest(s2, "ab")) {
			System.out.println("SExptoStringTest passed.");
		} else {
			throw new RuntimeException("SExptoStringTest failed");
		}

		// SExptoStringTest. have used the above two instances.
		SExp s3 = new SExp();
		s3.setAtom(false).setLeft(s1).setRight(s2);
		if (true == SExptoStringTest(s3, "(23 . ab)")) {
			System.out.println("SExptoStringTest passed.");
		} else {
			throw new RuntimeException("SExptoStringTest failed");
		}
	}

	private static boolean SExptoStringTest(SExp se, String e) {
		int r = se.toString().compareTo(e);
		return r == 0 ? true : false;
	}
}
