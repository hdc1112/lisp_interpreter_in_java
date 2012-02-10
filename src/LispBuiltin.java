/*
 * LispBuiltin.java 
 * Lisp's built-in functions' implementation
 * every function here corresponds to a Lisp built-in function.
 * e.g. cons() --> (cons 2 3)
 * used by SExp, Evaluator
 * 
 * Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
class LispBuiltinException extends Exception {
	// added by eclipse, I have no idea
	private static final long serialVersionUID = 1L;

	public LispBuiltinException(String m) {
		super(m);
	}
}

// some Lisp built-in functions
// these functions can be used by user
public class LispBuiltin {
	// for _null function, we still store "null" name, be careful
	public static final String[] BUILTINIDENTIFIERS = { "cons", "car", "cdr",
			"atom", "null", "eq" };

	public static SExp cons(SExp left, SExp right) {
		// because this isn't an atom, so just initialize
		// necessary members in SExp
		SExp sexp = new SExp();
		sexp.setAtom(false).setLeft(left).setRight(right);
		return sexp;
	}

	// warning: s can't be NIL. different from clisp
	public static SExp car(SExp s) throws LispBuiltinException {
		if (true == s.isAtom())
			throw new LispBuiltinException("car error: shouldn't be an atom .");
		return s.getLeft();
	}

	// warning: s can't be NIL
	public static SExp cdr(SExp s) throws LispBuiltinException {
		if (true == s.isAtom()) {
			throw new LispBuiltinException("cdr error: shouldn't be an atom .");
		}
		return s.getRight();
	}

	public static SExp atom(SExp s) {
		// there is an assumption, that "T" is already
		// in the IDPOINTERS, so I didn't use _getIdentifier
		return s.isAtom() ? SExp.getT() : SExp.getNIL();
	}

	// we can't name this function "null"
	// because "null" is a keyword in Java
	public static SExp _null(SExp s) throws LispBuiltinException {
		// we just compare the reference,
		// because there is an assumption, that for every identifier
		// there is only one corresponding instance in IDPOINTERS
		if (SExp.isT(atom(s)) == false) {
			throw new LispBuiltinException("null error: should be an atom");
		}
		return s == SExp.getNIL() ? SExp.getT() : SExp.getNIL();
	}

	public static SExp eq(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == false || SExp.isT(atom(s2)) == false) {
			throw new LispBuiltinException("eq error: should both be an atom");
		} else {
			if (s1.isNum() == true && s2.isNum() == true) {
				return s1.getValue() == s2.getValue() ? SExp.getT() : SExp
						.getNIL();
			} else if (s1.isIdentifier() == true && s2.isIdentifier() == true) {
				return s1 == s2 ? SExp.getT() : SExp.getNIL();
			} else {
				return SExp.getNIL();
			}
		}
	}
}
