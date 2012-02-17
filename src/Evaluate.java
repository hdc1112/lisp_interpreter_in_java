/*
 * Evaluate.java 
 * 2nd component, Evaluator
 * only used by LispInterpreter.java
 * 
 * Copyright (c) 2012 Dachuan Huang
 * 
 * Author: Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
class EvaluateException extends Exception {
	// again, have no idea..
	private static final long serialVersionUID = 1L;

	public EvaluateException(String s) {
		super(s);
	}
}

// 755's slides have lisp s-exp evaluator in lisp
// that is the basis of the code of Evaluate.java
public class Evaluate implements LispBuiltin_Names {
	// !------!
	// these functions are only for convenience
	// because evaluator uses toooo many nested built-in calls

	// assisting function, only used in this class
	private static SExp atom(SExp exp) throws LispBuiltinException {
		return LispBuiltin.atom(exp);
	}

	// assisting function, only used in this class
	private static SExp eq(SExp s1, SExp s2) throws LispBuiltinException {
		return LispBuiltin.eq(s1, s2);
	}

	// assisting function, only used in this class
	private static SExp cons(SExp s1, SExp s2) throws LispBuiltinException {
		return LispBuiltin.cons(s1, s2);
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has car
	private static SExp car(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(exp);
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cdr
	private static SExp cdr(SExp exp) throws LispBuiltinException {
		return LispBuiltin.cdr(exp);
	}

	// assisting function, only used in this class
	private static SExp _int(SExp exp) throws LispBuiltinException {
		return LispBuiltin._int(exp);
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has caar
	private static SExp caar(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(LispBuiltin.car(exp));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cdar
	private static SExp cdar(SExp exp) throws LispBuiltinException {
		return LispBuiltin.cdr(LispBuiltin.car(exp));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cadr
	private static SExp cadr(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(LispBuiltin.cdr(exp));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cadar
	private static SExp cadar(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(LispBuiltin.cdr(LispBuiltin.car(exp)));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has caddr
	private static SExp caddr(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(LispBuiltin.cdr(LispBuiltin.cdr(exp)));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cadddr
	private static SExp cadddr(SExp exp) throws LispBuiltinException {
		return LispBuiltin.car(LispBuiltin.cdr(LispBuiltin.cdr(LispBuiltin
				.cdr(exp))));
	}

	// assisting function, only used in this class
	// warning: it is not this function's responsibility
	// to check whether exp has cddddr
	private static SExp cddddr(SExp exp) throws LispBuiltinException {
		return LispBuiltin.cdr(LispBuiltin.cdr(LispBuiltin.cdr(LispBuiltin
				.cdr(exp))));
	}

	private static SExp list2(SExp s1, SExp s2) throws LispBuiltinException {
		return cons(s1, cons(s2, SExp.getNIL()));
	}

	// only used by (defun a b c) to detect whether b
	// is a proper params list
	// make sure it's a list before calling this method
	private static boolean properParamsList(SExp list)
			throws LispBuiltinException {
		if (SExp.isNIL(list) == true) {
			return true;
		} else {
			if (SExp.isT(atom(car(list))) == true
					&& SExp.isNIL(_int(list)) == true
					&& SExp.isT(car(list)) == false
					&& SExp.isNIL(car(list)) == false) {
				// parameters list 's criteria
				// be careful about this choice
				// function's parameter should not be T and NIL
				// but every other identifer is acceptable, even
				// quote, cond, defun, or built-in functions' names
				// this doesn't cause too much confusion, because
				// it is just a parameter
				// I make this choice by imitating clisp.
				return properParamsList(cdr(list));
			} else {
				// we don't care how it failed, only return false
				return false;
			}
		}
	}

	// x y are list. make sure before calling this
	private static SExp append(SExp x, SExp y) throws LispBuiltinException {
		if (SExp.isList(x) == false) {
			throw new RuntimeException(
					"evaluate internal error: append: should be a list");
		}
		if (SExp.isList(y) == false) {
			throw new RuntimeException(
					"evaluate internal error: append: should be a list");
		}
		if (SExp.isNIL(x) == true) {
			return y;
		} else {
			return cons(car(x), append(cdr(x), y));
		}
	}

	// assisting function
	// islist has the form: ((a . SExp) (b . SExp))
	private static SExp in(SExp a, SExp islist) throws LispBuiltinException {
		if (SExp.isList(islist) == false) {
			throw new RuntimeException(
					"evaluate internal error: in: should be a list");
		}
		if (SExp.isNIL(islist) == true) {
			return SExp.getNIL();
		} else if (SExp.isT(eq(a, caar(islist))) == true) {
			return SExp.getT();
		} else {
			return in(a, cdr(islist));
		}
	}

	// assisting function
	// islist has the form: ((a . SExp) (b . SExp))
	private static SExp getVal(SExp a, SExp islist) throws LispBuiltinException {
		if (SExp.isList(islist) == false) {
			throw new RuntimeException(
					"evaluate internal error: getVal: should be a list");
		}
		if (SExp.isNIL(islist) == true) {
			return SExp.getNIL();
		} else if (SExp.isT(eq(a, caar(islist))) == true) {
			return cdar(islist);
		} else {
			return getVal(a, cdr(islist));
		}
	}

	// assisting function
	// make (p1 p2) (2 3) to ((p1 . 2) (p2 . 3))
	// if it's (p1 p2) (2) then too few arguments
	// if it's (p1 p2) (2 3 4) then too many arguments
	// we don't say which function
	private static SExp pair(SExp x, SExp y) throws EvaluateException,
			LispBuiltinException {
		if (SExp.isNIL(x) == true) {
			if (SExp.isNIL(y) == true) {
				return SExp.getNIL();
			} else {
				throw new EvaluateException("pair error: too many arguments");
			}
		} else if (SExp.isNIL(y) == true) {
			if (SExp.isNIL(x) == true) {
				return SExp.getNIL();
			} else {
				throw new EvaluateException("pair error: too few arguments");
			}
		} else {
			return cons(cons(car(x), car(y)), pair(cdr(x), cdr(y)));
		}
	}

	// these functions are main functions of evaluator

	// eval this list.
	// it is not this funciton's responsiblity
	// to check whether it's really a list
	private static SExp evlis(SExp list, SExp alist, SExp dlist, int depth)
			throws EvaluateException, LispBuiltinException {
		if (SExp.isNIL(list) == true) {
			return SExp.getNIL();
		} else {
			return cons(_eval(car(list), alist, dlist, depth + 1),
					evlis(cdr(list), alist, dlist, depth));
		}
	}

	// the input is like ((T NIL) (NIL T))
	private static SExp evcon(SExp be, SExp alist, SExp dlist, int depth)
			throws EvaluateException, LispBuiltinException {
		if (SExp.isNIL(be) == true) {
			throw new EvaluateException(
					"evcon error: there should be at least one condition which is true");
		} else if (SExp.isT(eval(caar(be), alist, dlist)) == true) {
			return _eval(cadar(be), alist, dlist, depth + 1);
		} else {
			return evcon(cdr(be), alist, dlist, depth);
		}
	}

	// this is only used for (defun.
	// if defun is defined in a place that has
	// a depth more than this value, it will fail.
	private static final int INITIAL_DEPTH = 1;

	// only public method
	// it throws EvaluateException & LispBuiltinException
	// if you want to understand this code, first
	// understand how lisp interpreter works
	public static SExp eval(SExp exp, SExp alist, SExp dlist)
			throws EvaluateException, LispBuiltinException {
		return _eval(exp, alist, dlist, INITIAL_DEPTH);
	}

	private static SExp _eval(SExp exp, SExp alist, SExp dlist, int depth)
			throws LispBuiltinException, EvaluateException {
		if (SExp.isT(atom(exp)) == true) {
			// there is no built-in function (int exp)
			// so I use SExp's method
			if (SExp.isT(_int(exp)) == true) {
				return exp;
			} else if (SExp.isT(exp) == true) {
				return SExp.getT();
			} else if (SExp.isNIL(exp) == true) {
				return SExp.getNIL();
			} else if (SExp.isT(in(exp, alist)) == true) {
				return getVal(exp, alist);
			} else {
				throw new EvaluateException("eval error: unbound identifer "
						+ exp);
			}
		} else if (SExp.isT(atom(car(exp))) == true) {
			// we assume quote, cond, defun has already been defined
			// in IDPOINTERS
			if (SExp.isT(eq(car(exp), SExp.getIdSExp(SExp.QUOTE_name))) == true) {
				// so we don't check what' inside cadr(exp)
				// even it has some undefined identifer
				// ? we should add arguments number checking code
				return cadr(exp);
			} else if (SExp.isT(eq(car(exp), SExp.getIdSExp(SExp.COND_name))) == true) {
				// ? we should add arguments format checking
				return evcon(cdr(exp), alist, dlist, depth);
			} else if (SExp.isT(eq(car(exp), SExp.getIdSExp(SExp.DEFUN_name))) == true) {
				// throw new RuntimeException("defun not implemented yet");
				// this trick is how we deal with embedded (defun
				if (depth > INITIAL_DEPTH) {
					throw new EvaluateException("DEFUN should not be embedded");
				}
				SExp f = cadr(exp);
				if (SExp.isT(atom(f)) == true
						&& SExp.isT(_int(f)) == false
						&& SExp.isT(eq(f, SExp.getIdSExp(SExp.QUOTE_name))) == false
						&& SExp.isT(eq(f, SExp.getIdSExp(SExp.COND_name))) == false
						&& SExp.isT(eq(f, SExp.getIdSExp(SExp.DEFUN_name))) == false) {
					// function name 's criteria
					// be extremely careful about here
					// any identifier can be used to define a new function
					// which means you can change T or NIL or built-in's
					// functions, (I can modify this code to not let this
					// happen)
					// but to imitate clisp, I don't allow quote, cond, defun
					// to be a customized function's name.
					// (actually clisp alows defun to be a new function's name
					// and I have no idea it did that).
					SExp params = caddr(exp);
					if (SExp.isList(params) == true) {
						if (true == properParamsList(params)) {
							// we do some additional checking here.
							// if this throws an exception, then it has less
							// params than expected
							// warning: we don't care what user
							// wrote in function body.
							cadddr(exp);
							// make sure it's three params
							if (SExp.isNIL(cddddr(exp)) == false) {
								throw new EvaluateException(
										"eval error: defun has too many parameters");
							}
							// important state change
							// lisp cannot do this simply because
							// it doesn't have "variable" :)
							// warning: if re-define some function
							// we don't replace this with the original one
							// we just add this one to the top of the dlist
							LispInterpreter.dlist = append(
									cons(cons(f, cons(caddr(exp), cadddr(exp))),
											SExp.getNIL()), dlist);
							return f;
						} else {
							throw new EvaluateException(
									"eval error: not a proper params list,"
											+ " re-check each param");
						}
					} else {
						throw new EvaluateException(
								"eval error: parameters should be a list");
					}
				} else {
					throw new EvaluateException(
							"eval error: this is not a proper function name "
									+ f);
				}
			} else {
				// we assume each function call is essentially a list s-exp
				if (SExp.isList(exp) == false) {
					throw new EvaluateException(
							"eval error: illegal function call, function call should be a list");
				}
				return apply(car(exp), evlis(cdr(exp), alist, dlist, depth),
						alist, dlist, depth);
			}
		} else {
			throw new EvaluateException(
					"eval error: this is not an atom, a special form or function");
		}
	}

	private static SExp apply(SExp f, SExp x, SExp alist, SExp dlist, int depth)
			throws EvaluateException, LispBuiltinException {
		if (SExp.isT(atom(f)) == true) {
			if (SExp.isT(_int(f)) == true) {
				throw new EvaluateException(
						"apply error: function name should not start with a digit");
			}
			// first check whether this is a built-in function
			// look at the requirements to know what the
			// built-in functions are.
			// see LispBuiltin.java.
			// haven't checked the arguments number in builtin-function
			if (SExp.isT(eq(f, SExp.getIdSExp(CONS_name))) == true) {
				return cons(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(CAR_name))) == true) {
				// known bug.
				// should check that x is a list
				return caar(x);
			} else if (SExp.isT(eq(f, SExp.getIdSExp(CDR_name))) == true) {
				return cdar(x);
			} else if (SExp.isT(eq(f, SExp.getIdSExp(ATOM_name))) == true) {
				return atom(car(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(NULL_name))) == true) {
				return LispBuiltin._null(car(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(EQ_name))) == true) {
				return eq(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(INT_name))) == true) {
				return _int(car(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(PLUS_name))) == true) {
				return LispBuiltin.plus(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(MINUS_name))) == true) {
				return LispBuiltin.minus(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(TIMES_name))) == true) {
				return LispBuiltin.times(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(QUOTIENT_name))) == true) {
				return LispBuiltin.quotient(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(REMAINDER_name))) == true) {
				return LispBuiltin.remainder(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(LESS_name))) == true) {
				return LispBuiltin.less(car(x), cadr(x));
			} else if (SExp.isT(eq(f, SExp.getIdSExp(GREATER_name))) == true) {
				return LispBuiltin.greater(car(x), cadr(x));
			} else if (SExp.isT(in(f, dlist)) == true) {
				// now is user-defined functions
				// builtin functions are above, they are
				// not in dlist. but user-defined functions are
				SExp f_def = getVal(f, dlist);
				return _eval(cdr(f_def), append(pair(car(f_def), x), alist),
						dlist, depth + 1);
			} else {
				throw new EvaluateException(
						"apply error: function not defined: " + f.getName());
			}
		} else {
			throw new EvaluateException("apply error: car() should be an atom");
		}
	}
}
