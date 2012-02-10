// some Lisp built-in functions
public class LispBuiltin {
	public static SExp cons(SExp left, SExp right) {
		// because this isn't an atom, so just initialize
		// necessary members in SExp
		SExp sexp = new SExp();
		sexp.setAtom(false).setLeft(left).setRight(right);
		return sexp;
	}
}
