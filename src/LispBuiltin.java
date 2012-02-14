/*
 * LispBuiltin.java 
 * Lisp's built-in functions' implementation
 * every function here corresponds to a Lisp built-in function.
 * e.g. cons() --> (cons 2 3)
 * used by SExp, Evaluator
 * 
 * Copyright (c) 2012 Dachuan Huang
 * 
 * Author: Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
class LispBuiltinException extends Exception {
	// added by eclipse, I have no idea
	private static final long serialVersionUID = 1L;

	public LispBuiltinException(String m) {
		super(m);
	}
}

// warning: this interface has no method
// so it is only used as a global variables pool
interface LispBuiltin_Names {
	// see Evaluate's apply
	public static final String CONS_name = "CONS";
	public static final String CAR_name = "CAR";
	public static final String CDR_name = "CDR";
	public static final String ATOM_name = "ATOM";
	public static final String NULL_name = "NULL";
	public static final String EQ_name = "EQ";
	public static final String INT_name = "INT";
	public static final String PLUS_name = "PLUS";
	public static final String MINUS_name = "MINUS";
	public static final String TIMES_name = "TIMES";
	public static final String QUOTIENT_name = "QUOTIENT";
	public static final String REMAINDER_name = "REMAINDER";
	public static final String LESS_name = "LESS";
	public static final String GREATER_name = "GREATER";
}

// some Lisp built-in functions
// these functions can be used by user
public class LispBuiltin implements LispBuiltin_Names {
	// for _null function, we still store "null" name, be careful
	public static final String[] BUILTINIDENTIFIERS = { CONS_name, CAR_name,
			CDR_name, ATOM_name, NULL_name, EQ_name, INT_name, PLUS_name,
			MINUS_name, TIMES_name, QUOTIENT_name, REMAINDER_name, LESS_name,
			GREATER_name };

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
			throw new LispBuiltinException("car error: shouldn't be an atom ");
		return s.getLeft();
	}

	// warning: s can't be NIL
	public static SExp cdr(SExp s) throws LispBuiltinException {
		if (true == s.isAtom()) {
			throw new LispBuiltinException("cdr error: shouldn't be an atom ");
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
		// if (SExp.isT(atom(s)) == false) {
		// throw new LispBuiltinException("null error: should be an atom");
		// }
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

	// we can't use "int" as the name
	public static SExp _int(SExp s) {
		if (SExp.isNIL(atom(s)) == true) {
			return SExp.getNIL();
		}
		return s.isNum() ? SExp.getT() : SExp.getNIL();
	}

	public static SExp plus(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				SExp s = new SExp();
				s.setAtom(true).setNum(true)
						.setValue(s1.getValue() + s2.getValue());
				return s;
			}
		}
		throw new LispBuiltinException("plus error: should both be an integer");
	}

	public static SExp minus(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				SExp s = new SExp();
				s.setAtom(true).setNum(true)
						.setValue(s1.getValue() - s2.getValue());
				return s;
			}
		}
		throw new LispBuiltinException("minus error: should both be an integer");
	}

	public static SExp times(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				SExp s = new SExp();
				s.setAtom(true).setNum(true)
						.setValue(s1.getValue() * s2.getValue());
				return s;
			}
		}
		throw new LispBuiltinException("times error: should both be an integer");
	}

	public static SExp quotient(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				SExp s = new SExp();
				s.setAtom(true).setNum(true)
						.setValue(s1.getValue() / s2.getValue());
				return s;
			}
		}
		throw new LispBuiltinException(
				"quotient error: should both be an integer");
	}

	public static SExp remainder(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				SExp s = new SExp();
				s.setAtom(true).setNum(true)
						.setValue(s1.getValue() % s2.getValue());
				return s;
			}
		}
		throw new LispBuiltinException(
				"remainder error: should both be an integer");
	}

	public static SExp less(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				if (s1.getValue() < s2.getValue()) {
					return SExp.getT();
				} else {
					return SExp.getNIL();
				}
			}
		}
		throw new LispBuiltinException("less error: should both be an integer");
	}

	public static SExp greater(SExp s1, SExp s2) throws LispBuiltinException {
		if (SExp.isT(atom(s1)) == true && SExp.isT(atom(s2)) == true) {
			if (SExp.isT(_int(s1)) == true && SExp.isT(_int(s2)) == true) {
				if (s1.getValue() > s2.getValue()) {
					return SExp.getT();
				} else {
					return SExp.getNIL();
				}
			}
		}
		throw new LispBuiltinException(
				"greater error: should both be an integer");
	}
}
