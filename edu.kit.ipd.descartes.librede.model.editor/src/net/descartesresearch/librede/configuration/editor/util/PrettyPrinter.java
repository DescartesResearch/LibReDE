package net.descartesresearch.librede.configuration.editor.util;

public class PrettyPrinter {
	
	public static String toCamelCase(String s) {
		String[] parts = s.split("\\s");
		StringBuilder res = new StringBuilder(s.length());
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) {
				res.append(" ");
			}
			res.append(Character.toUpperCase(parts[i].charAt(0)));
			res.append(parts[i].substring(1).toLowerCase());
		}
		return res.toString();
	}

}
