import java.io.IOException;

public class LispInterpreter {

	public static void main(String[] args) {
		// self test, you can comment these following lines.
		System.out.println("self test.");
		UnitTest.test();

		while (true) {
			// test case
			// 1)  23 passed
			// 2)  (23 . 24) passed
			// 3)  (23 . (24 . 25)) passed
			// 4)  (   23 . (24 . 25)) passed
			// 5)  (\t 23 . (24 . 25)) passed
			// 6)  (23 . 24     \n   ) passed
			// 7)  (2 3) passed
			// 8)  (2 3   ) passed
			// 9)  (2 3 4 5 6 7 8 9    ) passed
			// 10) (2 (3 . 4)) passed
			// 11) (2 (4 . 5 )  ) passed
			// 12) ((2 . 3) (4 . 5)) passed
			// 13) ((2 3) (4 5)) passed
			// 14) ((2 3 4 5) 23 ) passed
			// 15) (+24) passed
			// 16) (-24 +24 -5 +6) passed
			// 17) (2 \n\n\n . \n\n\n 3) passed
			// 18) (2 3 . 5) passed (should fail)
			// 19) <23 passed (should fail, and should restart normally)
			// 20) (23 ] passed (should fail, and should restart normally)
			try {
				SExp se = Input.input();
				SExp.SExpPrintOut(se);
			} catch (InputException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// we print everything to stdout
				// we have to continue after this exception
				System.out.println(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
