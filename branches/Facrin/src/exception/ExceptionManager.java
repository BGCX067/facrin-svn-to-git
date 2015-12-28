package exception;

import java.util.ArrayList;
import java.util.List;

public class ExceptionManager {
	private static List<Exception> exceptionsList = new ArrayList<Exception>();

	public static void reset() {
		exceptionsList.clear();
	}

	public static boolean add(Exception e) {
		return exceptionsList.add(e);
	}

	public static void show() {
		String out = "";
		for (Exception ex : exceptionsList) {
			out += ex.getMessage() + System.getProperty("line.separator");
		}
		System.err.println(out);
	}

	public static boolean isEmpty() {
		return exceptionsList.isEmpty();
	}

}
