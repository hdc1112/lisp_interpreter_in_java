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
			throw new RuntimeException("SExptoStringTest failed.");
		}

		// SExptoStringTest
		SExp s2 = new SExp();
		s2.setAtom(true).setNum(false).setName("ab");
		if (true == SExptoStringTest(s2, "ab")) {
			System.out.println("SExptoStringTest passed.");
		} else {
			throw new RuntimeException("SExptoStringTest failed.");
		}

		// SExptoStringTest. have used the above two instances.
		SExp s3 = new SExp();
		s3.setAtom(false).setLeft(s1).setRight(s2);
		if (true == SExptoStringTest(s3, "(23 . ab)")) {
			System.out.println("SExptoStringTest passed.");
		} else {
			throw new RuntimeException("SExptoStringTest failed.");
		}

		// SExpisListTest
		if (true == SExpisListTest(SExp.getNIL(), true)) {
			System.out.println("SExpisListTest passed.");
		} else {
			throw new RuntimeException("SExpisListTest failed.");
		}

		// SExpisListTest
		SExp s4 = new SExp();
		s4.setAtom(false).setLeft(s3).setRight(SExp.getNIL());
		if (true == SExpisListTest(s4, true)) {
			System.out.println("SExpisListTest passed.");
		} else {
			throw new RuntimeException("SExpisListTest failed.");
		}

		// SExpisListTest
		SExp s5 = new SExp();
		s5.setAtom(false).setLeft(s3).setRight(s4);
		if (true == SExpisListTest(s5, true)) {
			System.out.println("SExpisListTest passed.");
		} else {
			throw new RuntimeException("SExpisListTest failed.");
		}

		// SExpisListTest
		if (true == SExpisListTest(s1, false)) {
			System.out.println("SExpisListTest passed.");
		} else {
			throw new RuntimeException("SExpisListTest failed.");
		}

		// List toString Test
		if (true == List_excluding_NIL_PrintTest(s4, "((23 . ab))")) {
			System.out.println("List_excluding_NIL_printTest passed.");
		} else {
			throw new RuntimeException("List_excluding_NIL_printTest failed.");
		}
	}

	private static boolean SExptoStringTest(SExp se, String e) {
		int r = se.toString().compareTo(e);
		return r == 0 ? true : false;
	}

	private static boolean SExpisListTest(SExp s, boolean islist) {
		return (SExp.isList(s) == islist) ? true : false;
	}

	private static boolean List_excluding_NIL_PrintTest(SExp s, String expect) {
		if (SExp.isList(s) == false || s == SExp.getNIL()) {
			throw new RuntimeException("ListPrintTest failed. param is wrong.");
		}
		return s.toString().equals(expect) ? true : false;
	}
}
