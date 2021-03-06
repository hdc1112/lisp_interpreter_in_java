import java.io.IOException;

/*
 * LispInterpreter.java 
 * Lisp's Interpreter class
 * also the main entry
 * it has Input & Evaluator, two components
 * 
 * Copyright (c) 2012 Dachuan Huang
 * 
 * Author: Dachuan Huang
 * huangda@cse.ohio-state.edu
 */

//global configuration class
//to control the behavior of interpreter
//I admit this class is ugly
//but in this small prj, I don't want to use
//some command line parsing library to bluff
class Gconfig {
	// default configuration: every bit is cleared
	// which means:
	// eval is on; add "evaloff" if you wanna turn this feature off
	// listprint is on; add "listprintoff" if you wanna turn this feature off
	// lowercase is on; add "lowercaseoff" if you wanna turn this feature off
	// lowercasesame is on; add "lowercasesameoff" if you wanna turn this
	// feature off
	public static int mode = 0x0;

	// input&output mode: only input and output the s-exp
	public static int EVALOFFMODE = 0x1;
	private static String EVALOFFOPT = "evaloff";

	// "don't print list notation" mode
	public static int LISTPRINTOFFMODE = 0x2;
	private static String LISTPRINTOFFOPT = "listprintoff";

	// lowercase off mode (we don't accept lower case input)
	public static int LOWERCASEOFFMODE = 0x4;
	private static String LOWERCASEOFFOPT = "lowercaseoff";

	// lowercasesame off mode (we treat lower case letters differently from
	// capital letters
	public static int LOWERCASESAMEOFFMODE = 0x8;
	private static String LOWERCASESAMEOFFOPT = "lowercasesameoff";

	// only called by LispInterpreter's init()
	public static void init_config(String[] args) {
		for (int i = 0; i < args.length; i++) {
			// I should put some help or usage printing
			// here, but let's forget it now
			if (args[i].equals(EVALOFFOPT) == true) {
				mode |= EVALOFFMODE;
			} else if (args[i].equals(LISTPRINTOFFOPT) == true) {
				mode |= LISTPRINTOFFMODE;
			} else if (args[i].equals(LOWERCASEOFFOPT) == true) {
				mode |= LOWERCASEOFFMODE;
			} else if (args[i].equals(LOWERCASESAMEOFFOPT) == true) {
				mode |= LOWERCASESAMEOFFMODE;
			} else {
				System.out.println("Ignore the unrecognizable option: "
						+ args[i]);
			}
		}
	}

	// only called by LispInterpreter's welcome()
	public static void warning() {
		if ((mode & EVALOFFMODE) != 0) {
			System.out.println("Warning: You are in Input & Output mode, "
					+ "this mode is only used for debug or test");
		}
		if ((mode & LISTPRINTOFFMODE) != 0) {
			System.out.println("Warning: list notation printing is turned off");
		}
		if ((mode & LOWERCASEOFFMODE) != 0) {
			System.out.println("Warning: lower case letters are "
					+ "now forbidden for input");
		} else if ((mode & LOWERCASESAMEOFFMODE) != 0) {
			System.out.println("Warning: now the interpreter is "
					+ "case sensitive, but built-in functions are still CAPITAL");
		}
	}
}

public class LispInterpreter {
	// the global defun-list
	// built-in functions are not here
	// each time (defun is called, some function is added into this
	public static SExp dlist;

	private static void init_IDPOINTERS() {
		// add some id's into idPointers
		// once they are added, there is no reason to
		// delete them in one run.
		// it is really a necessity to add them at the beginning
		// of everything, because many code rely on this fact.
		// it is very dangerous to directly add sth. to List
		// without first checking whether it is already in it,
		// and without checking whether it is capital letters
		// it is highly recommended that alwasy use _getIdSExp
		// if you will add sth, and use getIdSExp for only fetch sth.

		// these id's are T and NIL
		SExp._getIdSExp(SExp.NIL_name);
		SExp._getIdSExp(SExp.T_name);

		// these id's are QUOTE, COND, DEFUN
		SExp._getIdSExp(SExp.QUOTE_name);
		SExp._getIdSExp(SExp.COND_name);
		SExp._getIdSExp(SExp.DEFUN_name);

		// these id's come from lispbuiltin functions
		for (int i = 0; i < LispBuiltin.BUILTINIDENTIFIERS.length; i++) {
			SExp._getIdSExp(LispBuiltin.BUILTINIDENTIFIERS[i]);
		}
	}

	private static void welcome() {
		System.out.println("Lisp Interpreter, written in Java");
		System.out.println("Dachuan Huang");
		System.out.println("huangda@cse.ohio-state.edu");
		System.out.println("02/11/2012");
		System.out.println("Copyright (c) Dachuan Huang");
		System.out.println("Press Ctrl+C to exit");
		Gconfig.warning();
	}

	private static void init(String[] args) {
		init_IDPOINTERS();
		Gconfig.init_config(args);
		welcome();
	}

	public static void main(String[] args) {
		// Lisp Interpreter Initialization
		// vital code
		init(args);

		// self test, you can comment these following lines.
		// System.out.println("self test");
		// UnitTest.test();

		// now we roll
		int exp_num = 1;
		dlist = SExp.getNIL();
		while (true) {
			// test cases for input
			// 1) 23 passed
			// 2) (23 . 24) passed
			// 3) (23 . (24 . 25)) passed
			// 4) ( 23 . (24 . 25)) passed
			// 5) (\t 23 . (24 . 25)) passed
			// 6) (23 . 24 \n ) passed
			// 7) (2 3) passed
			// 8) (2 3 ) passed
			// 9) (2 3 4 5 6 7 8 9 ) passed
			// 10) (2 (3 . 4)) passed
			// 11) (2 (4 . 5 ) ) passed
			// 12) ((2 . 3) (4 . 5)) passed
			// 13) ((2 3) (4 5)) passed
			// 14) ((2 3 4 5) 23 ) passed
			// 15) (+24) passed
			// 16) (-24 +24 -5 +6) passed
			// 17) (2 \n\n\n . \n\n\n 3) passed
			// 18) (2 3 . 5) passed (should fail, and should restart normally)
			// 19) <23 passed (should fail, and should restart normally)
			// 20) (23 ] passed (should fail, and should restart normally)
			// 21) (2 5 6 7 1 \n 23 (2 . 4) 3 (4 5) \n ) passed
			// 22) if SExp is a List, then Print in list notation. passed
			// 23) A passed
			// 24) (A 23 (A . 3)) passed
			// 25) (A . B) passed
			//
			// test cases for evaluation
			// 26) 23 passed
			// 27) a "only capital letter is allowed"
			// 28) A "unbound identifer A"
			// 29) (2 3) "function name should start with capital letter"
			// 30) (A 3) "function not defined: A"
			// 31) (QUOTE (2 3 3 3 3)) passed
			// 32) (COND (T NIL)) passed
			// 33) (COND (NIL T) (T NIL)) passed
			// 34) (COND (NIL T ) ( T T)) passed
			// 35) (COND A) "car error: shouldn't be an atom "
			// 36) (COND (A NIL)) " unbound identifer A"
			// 37) (COND (NIL NIL))
			// "there should be at least one condition which is true"
			// 38) (2 . 3) "illegal function call format"
			// 39) (2 3) "function name should start with capital letter"
			// 40) (2 . (3 . NIL))
			// "function name should start with capital letter"
			// 41) (CAR (2 . 3)) "illegal function call format"
			// 42) (CAR (QUOTE (2 . 3))) passed
			// 43) (EQ (QUOTE A) (QUOTE A)) passed
			// 44) (PLUS 222 -90) passed
			// 45) (MINUS 222 -90) passed
			// 46) (LESS 2 (QUOTE A)) "should both be an integer"
			// 47) (LESS (QUOTE 2) (QUOTE 3)) passed
			// 48) (COND (T NIL)) passed
			// 49) (COND (NIL T)) "at least one condition should true"
			// test cases for (defun.
			// 50) (DEFUN APPEND (L1 L2) (COND ((NULL L1) L2) (T (CONS (CAR L1)
			// (APPEND (CDR L1) L2))))) passed
			// 51) (APPEND (QUOTE (2)) (QUOTE (2))) passed
			// 52) (DEFUN XMEMB (X LIST) (COND ((NULL LIST) NIL) ((EQ X (CAR
			// LIST)) T) (T (XMEMB X (CDR LIST))))) passed
			// 53) (XMEMB 2 (QUOTE (2 3))) passed
			// 54) (DEFUN XMEMB (X LIST) (COND ((NULL LIST) NIL) ((EQ X (CAR
			// LIST)) T) (T (XMEMB X (CDR LIST))))) passed
			// 55) (XUNION (QUOTE (3 4)) (QUOTE (4 5))) passed
			// 56) (DEFUN EQUAL (X Y) (COND ((ATOM X) (COND ((ATOM Y) (EQ X Y))
			// (T NIL))) ((ATOM Y) NIL) ((EQUAL (CAR X) (CAR Y)) (EQUAL (CDR X)
			// (CDR Y))) (T NIL))) passed
			// 57) (EQUAL (QUOTE (2 . 3)) (QUOTE (2 . 3))) passed
			// 58) (DEFUN ATOMSLIST (S) (COND ((NULL S) NIL) ((ATOM S) (CONS S
			// NIL)) (T (APPEND (ATOMSLIST (CAR S)) (ATOMSLIST (CDR S))))))
			// passed
			// 59) (ATOMSLIST (QUOTE ((2 . 3) . 4))) passed
			// 60) (DEFUN CHECK2 (S) (COND ((NULL S) NIL) ((XMEMB (CAR S) (CDR
			// S)) T) (T (CHECK2 (CDR S)) ) )) passed
			// 61) (DEFUN CHECK (S) (CHECK2 (ATOMSLIST S))) passed
			// 62) (CHECK (QUOTE ((2 . 3) . 2))) passed
			//
			// random test, pressure test
			// 63) (QUOTE (2 .3)) found a bug. fixed on 2/14/2012
			// 64) (.3) "we don't support floating number"
			// 65) .3 "we don't support floating number"
			// 66) (QUOTE (A .B)) "there should be a white space after '.'"
			try {
				// clisp style. even wrong exp, we +1
				System.out.printf("[%d]>", exp_num++);
				// 1st, user input
				SExp se = Input.input();
				if ((Gconfig.mode & Gconfig.EVALOFFMODE) != 0) {
					// optional, output this input, usually for debug purpose
					SExp.SExpPrintOut(se);
				} else {
					// 2nd, output the evaluation result
					SExp.SExpPrintOut(Evaluate.eval(se, SExp.getNIL(), dlist));
				}
			} catch (InputException e) {
				// we print everything to stdout
				// e.printStackTrace();
				// we have to continue after this exception
				System.out.println(e);
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println(e);
			} catch (EvaluateException e) {
				// e.printStackTrace();
				System.out.println(e);
			} catch (LispBuiltinException e) {
				// e.printStackTrace();
				System.out.println(e);
			}
		}
	}
}
