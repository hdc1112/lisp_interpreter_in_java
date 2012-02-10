import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * SExp.java 
 * Lisp's s-exp class
 * important basis, used by everyone
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
	// if this assumption doesn't hold, then change
	// corresponding code
	private String name;
	// some constant sexp's value
	// we only use capital letter in this prj
	public static String NIL_name = "NIL";
	public static String T_name = "T";
	// if isNum == true
	private int value;

	// Getters & Setters
	// if there is similar function defined
	// in LispBuiltin, use that. don't use this.
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

	public boolean isIdentifier() {
		return !isNum();
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
	// warning: any identifier should be added into this
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

	// short name
	public static SExp getIdSExp(String name) {
		return getIdentifierSExp(name);
	}

	public static SExp _getIdSExp(String name) {
		return _getIdentifierSExp(name);
	}

	public static SExp getT() {
		return getIdSExp(SExp.T_name);
	}

	public static SExp getNIL() {
		return getIdSExp(SExp.NIL_name);
	}

	public static boolean isT(SExp s) {
		if (s.isAtom() == true && s.isNum() == false
				&& s.getName().equals(SExp.T_name)) {
			return true;
		}
		return false;
	}

	// there are two ways to compare two identifiers
	// 1st, compare names; 2nd, compare reference of SExp.
	// under the assumption that one name has only one reference
	// then these two methods are the same
	public static boolean isNIL(SExp s) {
		if (s.isAtom() == true && s.isNum() == false
				&& s.getName().equals(SExp.NIL_name)) {
			return true;
		}
		return false;
	}

	// short name. >>>end

	@Override
	// toString
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (isAtom() == true) {
			if (isNum() == true) {
				sb.append(value);
			} else {
				// if it's not a number, then'
				// it's an identifer
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
