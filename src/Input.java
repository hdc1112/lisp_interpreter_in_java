import java.io.IOException;

/*
 * Input.java 
 * 1st component, Input
 * only used by LispInterpreter.java
 * 
 * Copyright (c) 2012 Dachuan Huang
 * 
 * Author: Dachuan Huang
 * huangda@cse.ohio-state.edu
 */
class InputException extends Exception {
	// generated by eclipse, I know nothing ..
	private static final long serialVersionUID = 1L;

	public InputException(String m) {
		super(m);
	}
}

public class Input {
	public static SExp input() throws InputException, IOException {
		Token t = ckNextToken();
		if (t.type == Token.LEFT_PARENTHESIS) {
			skipToken();
			t = ckNextToken();
			if (t.type == Token.RIGHT_PARENTHESIS) {
				skipToken();
				// every atom with a string's name
				// must have an entry in idPointers
				// we must search it first and if no, create it and
				// add it
				return SExp.getNIL();
			} else {
				// so here we found out that token is not
				// ), so we re-read it to a s-exp
				SExp left = input();
				t = ckNextToken();
				if (t.type == Token.DOT) {
					skipToken(); // skip .
					t = ckNextToken();
					SExp s = LispBuiltin.cons(left, input());
					// if t.right_parenthesis_prefetched is true
					// we are going to use this token again
					// so we don't skip
					if (t.right_parenthesis_prefetched == false) {
						t = ckNextToken();
						if (t.type == Token.RIGHT_PARENTHESIS)
							skipToken();
						else {
							_flush();
							throw new InputException("input error: missing ) ");
						}
					} else {
						// we don't skip anything, just
						// make token's type to ). and unset the right_
						// parenthesis_prefetched flag
						// 2nd solution. if we meet a 23)
						current.type = Token.RIGHT_PARENTHESIS;
						current.right_parenthesis_prefetched = false;
					}
					return s;
				} else {
					return LispBuiltin.cons(left, input2());
				}
			}
		} else if (t.type == Token.IDENTIFIER) {
			// throw new RuntimeException("not implemented 2.");
			// now I implement Identifier, it's very similar with number
			// unlike number, I don't create new identifier object onece
			// there is such one.
			SExp s = SExp._getIdSExp(t.value);
			if (t.right_parenthesis_prefetched == false) {
				skipToken();
			} else {
				current.type = Token.RIGHT_PARENTHESIS;
				current.right_parenthesis_prefetched = false;
			}
			return s;
		} else if (t.type == Token.INTEGER) {
			// skipToken(); // we can't skip here, because we want to use it.
			// when we encounter a number, we just create a new SExp.
			// unlike identifier
			SExp s = new SExp();
			s.setAtom(true).setNum(true).setValue(current.integer);
			// skipToken();
			// if t.right_parenthesis_prefetched is true
			// we are going to use this token again
			// so we don't skip
			// whoever meets this ugly flag will process it immediately
			if (t.right_parenthesis_prefetched == false) {
				skipToken();
			} else {
				// 2nd solution
				current.type = Token.RIGHT_PARENTHESIS;
				current.right_parenthesis_prefetched = false;
			}
			return s;
		} else {
			_flush();
			throw new InputException("input error: illegal token");
		}
	}

	private static SExp input2() throws InputException, IOException {
		// Token t = new Token(); // wrong here
		Token t = ckNextToken();
		// if t.right_parenthesis_prefetched == true
		// it means we have already read a ) before

		// we met a problem here. (2) needs this t.ri** == true
		// but (2 3) can't skip, because 3) can't be skipped
		// so I need to rethink the right_parenthesis_prefetched
		// maybe I can change token.type to ) if it processed
		// the number or identifier, but don't skip it.
		if (t.type == Token.RIGHT_PARENTHESIS) {
			// || t.right_parenthesis_prefetched == true) {
			skipToken();
			return SExp.getNIL();
		} else if (t.type == Token.DOT) {
			_flush();
			throw new InputException("input2 error: meets a unwelcome dot");
		} else {
			return LispBuiltin.cons(input(), input2());
		}
	}

	private static Token current = null;

	private static Token ckNextToken() throws InputException {
		// ckNextToken() will always return the same Token
		// unless skipToken() has been called.
		if (current != null)
			return current;

		int c;
		try {
			// we skip space, tab, newline characters
			// watch out, in java, we read a newline, it's 13
			while ((c = System.in.read()) == ' ' || c == '\t' || c == '\n'
					|| c == 13)
				;

			if (c == '(') {
				Token t = new Token();
				t.type = Token.LEFT_PARENTHESIS;
				t.value = "("; // maybe not necessary
				current = t;
				return t;
			} else if (c == ')') {
				Token t = new Token();
				t.type = Token.RIGHT_PARENTHESIS;
				t.value = ")";
				current = t;
				return t;
			} else if (c == '.') {
				// it's a little different here
				// what follows a '.' must be a white space
				// otherwise (QUOTE (2 .3)) will be seen as a
				// legal expression, in fact it's not
				// so read a character, this action won't
				// interfere the ckNextToken() next.
				// code purely for fault-detection
				c = System.in.read();
				if (c != ' ') {
					_flush();
					// again, I try to be nice to user
					if (isDigit(c) == true) {
						throw new InputException(
								"ckNextToken error: we don't support floating number,"
										+ " please add a white space before number");
					}
					throw new InputException(
							"ckNextToken error: there should be a white space after '.'");
				}
				// and also be careful, that if in the future
				// I want to support floating number, then '.'
				// has two meanings, 1st for floating number and 2nd for
				// (A . B) so some changes must be made when
				// reading a '.', we should continue reading, if it's really
				// a floating number, then return a number Token; if it's
				// not a white space then wrong; if it's a white space
				// then return a dot Token.
				// and when reading a number, we also need to
				// make some changes, because '.' can be met legally.
				// just for future maintenance.
				Token t = new Token();
				t.type = Token.DOT;
				t.value = ".";
				current = t;
				return t;
			} else if (isDigit(c) || c == '+' || c == '-') {
				// above: watch out, +25! and 10 characters at most
				int value = 0;
				boolean positive = true;
				// if it's + or -, then we must have a digit
				if (c == '+' || c == '-') {
					if (c == '-') {
						positive = false;
					}
					c = System.in.read();
					if (false == isDigit(c)) {
						_flush();
						throw new InputException(
								"ckNextToken error: illegal digit after "
										+ (char) c);
					} else {
						value = c - '0';
					}
				} else {
					value = c - '0';
				}

				// we believe that every integer has a
				// space/tab/newline character following it
				// otherwise, it's wrong. the same rule applies
				// to identifier. the principle is that we
				// never need to rewind to get the previous character.
				// if this is a false assumption, then this code
				// needs rewriting
				while (true) {
					c = System.in.read();
					// these characters will terminate number legally
					if (c == ' ' || c == '\t' || c == '\n' || c == 13
							|| c == ')') {
						// we have finished an integer
						// now create a Token
						Token t = new Token();
						t.type = Token.INTEGER;
						t.integer = value;
						if (positive == false) {
							t.integer = -t.integer;
						}
						current = t;
						// ) has prefetched, so we need write this down
						// although it looks ugly, I think this is inevitable
						// because we have to find something to signify
						// that number or identifier has reached to an end.
						if (c == ')')
							t.right_parenthesis_prefetched = true;
						return t;
					} else if (isDigit(c)) {
						value = value * 10 + c - '0';
					} else {
						_flush();
						throw new InputException(
								"ckNextToken error: illegal digit after "
										+ value);
					}
				}
			} else if (isCapitalLetter(c)) {
				// throw new RuntimeException("not implemented yet.");
				// once you have a correct integer input code,
				// it's very easy to write a identifer input code.
				StringBuffer sb = new StringBuffer();
				sb.append((char) c);
				while (true) {
					c = System.in.read();
					if (c == ' ' || c == '\t' || c == '\n' || c == 13
							|| c == ')') {
						// we have finished a identifer
						// now create a Toekn
						Token t = new Token();
						t.type = Token.IDENTIFIER;
						t.value = sb.toString();
						current = t;
						if (c == ')') {
							t.right_parenthesis_prefetched = true;
						}
						return t;
					} else if (isCapitalLetter(c) || isDigit(c)) {
						sb.append((char) c);
					} else {
						_flush();
						if (isLowercaseLetter(c)) {
							throw new InputException(
									"only capital letter is allowed ");
						} else {
							throw new InputException(
									"ckNextToken error: illegal character "
											+ (char) c);
						}
					}
				}
			} else {
				_flush();
				// I try to be nice to user
				if (isLowercaseLetter(c)) {
					throw new InputException("only capital letter is allowed");
				} else {
					throw new InputException(
							"ckNextToken error: illegal character " + (char) c);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// if read error, we can do nothing
			throw new RuntimeException("System.in.read() error");
		}
	}

	private static void skipToken() {
		current = null;
	}

	private static boolean isDigit(int c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}

	private static boolean isCapitalLetter(int c) {
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		return false;
	}

	private static boolean isLowercaseLetter(int c) {
		if (c >= 'a' && c <= 'z') {
			return true;
		}
		return false;
	}

	private static void flushStdin() throws IOException {
		int rem;
		while ((rem = System.in.available()) > 0) {
			for (int i = 0; i < rem; i++)
				System.in.read();
		}
	}

	private static void _flush() throws IOException {
		flushStdin();
		current = null;
	}

	private static class Token {
		static final int LEFT_PARENTHESIS = 1;
		static final int RIGHT_PARENTHESIS = 2;
		static final int DOT = 3;
		static final int IDENTIFIER = 4;
		static final int INTEGER = 5;

		// init to nothing
		int type = -1;

		// if type is 1, 2, 3, 4
		String value;
		// if type is 5
		int integer;
		// if type is 4, 5
		// because number and identifier can have a immediate )
		// we must have ) to signify that number or identifier has
		// reached to an end. so if it's like 23) ab), then
		// must make this flag true. then after input has read
		// the data, we make it a ) token.
		// default is false, so we can ignore this if nothing special
		// happened.
		boolean right_parenthesis_prefetched = false;

	}
}
