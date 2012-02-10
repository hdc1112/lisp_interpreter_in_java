import java.util.ArrayList;
import java.util.List;

public class SExp {
	private boolean isAtom;

	// if isAtom == false
	SExp left, right;

	// if isAtom == true
	private boolean isNum;
	// if isNum == false
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
	private static List<SExp> IDPOINTERS = new ArrayList<SExp>();
	static {
		// add some id's into idPointers
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
