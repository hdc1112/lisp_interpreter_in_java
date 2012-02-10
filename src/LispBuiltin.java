/*
 * LispBuiltin.java 
 * Lisp's built-in functions' implementation
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
			"atom", "null" };

	public static SExp cons(SExp left, SExp right) {
		// because this isn't an atom, so just initialize
		// necessary members in SExp
		SExp sexp = new SExp();
		sexp.setAtom(false).setLeft(left).setRight(right);
		return sexp;
	}

	public static SExp car(SExp s) throws LispBuiltinException {
		if (true == s.isAtom())
			throw new LispBuiltinException("car error: shouldn't be an atom .");
		return s.getLeft();
	}

	public static SExp cdr(SExp s) throws LispBuiltinException {
		if (true == s.isAtom()) {
			throw new LispBuiltinException("cdr error: shouldn't be an atom .");
		}
		return s.getRight();
	}

	public static SExp atom(SExp s) {
		if (true == s.isAtom()) {
			// there is an assumption, that "T" is already
			// in the IDPOINTERS, so I didn't use _getIdentifier
			return SExp.getIdentifierSExp(SExp.T_name);
		} else {
			return SExp.getIdentifierSExp(SExp.NIL_name);
		}
	}

	// we can't name this function "null"
	// because "null" is a keyword in Java
	public static SExp _null(SExp s) {
		// we just compare the reference,
		// because there is an assumption, that for every identifier
		// there is only one corresponding instance in IDPOINTERS
		if (s == SExp.getIdentifierSExp(SExp.NIL_name)) {
			return SExp.getIdentifierSExp(SExp.T_name);
		} else {
			return SExp.getIdentifierSExp(SExp.NIL_name);
		}
	}
}
