import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * SExp.java 
 * Lisp's s-exp class
 * 
 * Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
public class SExp {
	private boolean isAtom;

	// if isAtom == false
	private SExp left, right;

	// if isAtom == true
	private boolean isNum;
	// if isNum == false, then it's an identifier
	private String name;
	// some constant sexp's value
	// we only use capital letter in this prj
	public static String NIL_name = "NIL";
	public static String T_name = "T";
	// if isNum == true
	private int value;

	// Getters & Setters
	public boolean isAtom() {
		return isAtom;
	}

	public SExp setAtom(boolean isAtom) {
		this.isAtom = isAtom;
		return this;
	}

	public SExp getLeft() {
		return left;
	}

	public SExp setLeft(SExp left) {
		this.left = left;
		return this;
	}

	public SExp getRight() {
		return right;
	}

	public SExp setRight(SExp right) {
		this.right = right;
		return this;
	}

	public boolean isNum() {
		return isNum;
	}

	public SExp setNum(boolean isNum) {
		this.isNum = isNum;
		return this;
	}

	public String getName() {
		return name;
	}

	public SExp setName(String name) {
		this.name = name;
		return this;
	}

	public int getValue() {
		return value;
	}

	public SExp setValue(int value) {
		this.value = value;
		return this;
	}

	// static idPointers
	// unlimited length
	// I should try other efficient data-structure
	private static List<SExp> IDPOINTERS = new ArrayList<SExp>();
	static {
		// add some id's into idPointers
		// once they are added, there is no reason to
		// delete them in one run.
		// it is really a necessity to add them at the beginning
		// of everything, because many code rely on this fact.

		// these id's are T and NIL
		SExp s_nil = new SExp();
		s_nil.setAtom(true).setNum(false).setName(SExp.NIL_name);
		IDPOINTERS.add(s_nil);
		SExp s_t = new SExp();
		s_t.setAtom(true).setNum(false).setName(SExp.T_name);
		IDPOINTERS.add(s_t);

		// these id's come from lispbuiltin functions
		for (int i = 0; i < LispBuiltin.BUILTINIDENTIFIERS.length; i++) {
			SExp s = new SExp();
			s.setAtom(true).setNum(false)
					.setName(LispBuiltin.BUILTINIDENTIFIERS[i]);
			IDPOINTERS.add(s);
		}
	}

	// only search the IDPOINTERS, and return it if found, return null
	// if it doesn't exisit
	public static SExp getIdentifierSExp(String name) {
		// when traverse through the IDPOINTERS, make sure
		// we use Iterator, because we may have different underlying
		// implementations for IDPOINTERS
		for (Iterator<SExp> it = IDPOINTERS.iterator(); it.hasNext();) {
			SExp s = it.next();
			if (true == s.getName().equals(name))
				return s;
		}
		return null;
	}

	// same with getIdentifierSExp, but if not found, add it
	public static SExp _getIdentifierSExp(String name) {
		SExp s = getIdentifierSExp(name);
		if (null == s) {
			s = new SExp();
			s.setAtom(true).setNum(false).setName(name);
			IDPOINTERS.add(s);
		}
		return s;
	}

	@Override
	// toString
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (isAtom == true) {
			if (isNum == true) {
				sb.append(value);
			} else {
				sb.append(name);
			}
		} else {
			// if(isList(this))
			// else
			sb.append("(" + left + " . " + right + ")");
		}
		return sb.toString();
	}

	public static void SExpPrintOut(SExp s) {
		System.out.println(s);
	}

	// isList
	// public static boolean isList(SExp s) {}
}
