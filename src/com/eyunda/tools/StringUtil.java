package com.eyunda.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Relating to String operation
 * 
 * @author Malik Xu
 */
public class StringUtil {
	public static String rootpath = "";

	/**
	 * Filter the invalid character
	 * 
	 * @param str
	 * @return a turn string
	 */
	public static String getFilterString(String str) {
		str = str.replace('<', '_');
		str = str.replace('>', '_');
		str = str.replace('"', '_');
		str = str.replace('\'', '_');
		str = str.replace('%', '_');
		str = str.replace(';', '_');
		str = str.replace('(', '_');
		str = str.replace(')', '_');
		str = str.replace('&', '_');
		str = str.replace('+', '_');
		return str;
	}

	/**
	 * Change the initial character to Upper case
	 * 
	 * @param str
	 * @return a string of first char is upper
	 */
	public static String firstToUpper(String str) {
		str = str.trim();
		String ret = "";
		if (str.length() >= 1) {
			ret = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
		}
		return ret;
	}

	/**
	 * Change the initial character to lower case
	 * 
	 * @param str
	 * @return a string of first char is lower
	 */
	public static String firstToLower(String str) {
		str = str.trim();
		String ret = "";
		if (str.length() >= 1) {
			ret = str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
		}
		return ret;
	}

	
	/**
	 * Change time format to parrten2 pattern
	 * 
	 * @param t1
	 * @param parrten
	 * @param parrten2
	 * @return a string
	 */
	public static String getTime(String t1, String parrten, String parrten2) {
		return DateUtils.getTime(t1, parrten, parrten2);
	}

	/**
	 * Check the character string whether made of a-z
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean checkString(String s) {
		boolean tag = false;
		String regex = "\\p{Lower}+?";
		if (s != null && !"".equals(s)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(s);
			tag = m.matches();
		}
		return tag;
	}

	/**
	 * validate the input string with the given string pattern
	 * 
	 * @param s
	 *            String
	 * @param regex
	 *            pattern
	 * @return boolean true=check passed，false=check failed
	 */
	public static boolean checkString(String s, String regex) {
		boolean tag = false;
		if (s != null && !"".equals(s)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(s);
			tag = m.matches();
		}
		return tag;
	}

	/**
	 * Format the string to number
	 * 
	 * @param s
	 * 
	 * @return int，if not number ,then return -1
	 */
	public static int strToInt(String s) {
		int temp;
		if (s == null || "".equals(s)) {
			return 0;
		}
		if (StringUtil.isNumeric(s.trim())) {
			temp = Integer.parseInt(s);
		} else {
			temp = 0;
		}
		return temp;
	}

	/**
	 * format the string to lower case
	 * 
	 * @param s
	 * @return a string of lower case
	 */
	public static String toLowercase(String s) {
		String tag = null;
		if (s != null && !s.equals("")) {
			tag = s.toLowerCase();
		}
		return tag;
	}

	/**
	 * Generate SQL string
	 * 
	 * @param tablename
	 * 
	 * @param sqlprotasis
	 * 
	 * @param columnname
	 * 
	 * @return String
	 */
	public static String generationSql(String tablename, String sqlprotasis, String[] columnname) {
		StringBuffer bufsql = new StringBuffer("select ");
		for (int i = 0; i < columnname.length; i++) {
			if (i == (columnname.length - 1)) {
				bufsql.append(columnname[i]);
			} else {
				bufsql.append(columnname[i] + ",");
			}
		}
		bufsql.append(" from " + tablename + sqlprotasis);
		return bufsql.toString();
	}

	/**
	 * Generate SQL string
	 * 
	 * @param tablename
	 * 
	 * @param sqlprotasis
	 * 
	 * @param columnname
	 * 
	 * @return String
	 */
	public static String generationSql(int number, String tablename, String sqlprotasis, String[] columnname) {
		StringBuffer bufsql = new StringBuffer("select top " + number + " ");
		for (int i = 0; i < columnname.length; i++) {
			if (i == (columnname.length - 1)) {
				bufsql.append(columnname[i]);
			} else {
				bufsql.append(columnname[i] + ",");
			}
		}
		bufsql.append(" from " + tablename + sqlprotasis);
		return bufsql.toString();
	}

	/**
	 * getNumString
	 * 
	 * @param s
	 * 
	 * @param n
	 *            the start position which you want to cut off string
	 * @return a string
	 */
	public static String getNumString(String s, int n) {
		String tag;
		tag = s;
		if (s != null && !s.equals("")) {
			tag = tag.substring(0, n);
		} else {
			tag = "";
		}
		return tag;
	}

	/**
	 * Generate SQL string
	 * 
	 * @param tablename
	 * 
	 * @param sqlprotasis
	 * 
	 * @return SQL string
	 */
	public static String generationSql(String tablename, String sqlprotasis) {
		StringBuffer bufsql = new StringBuffer("select ");
		bufsql.append("* from " + tablename + sqlprotasis);
		return bufsql.toString();
	}

	/**
	 * check the string is number or not
	 * 
	 * @param s
	 * @return Yes=true;No=false
	 */
	public static boolean isNumeric(String s) {
		boolean flag = true;
		if (s != null && !s.equals("")) {
			char[] numbers = s.toCharArray();
			for (int i = 0; i < numbers.length; i++) {
				if (!Character.isDigit(numbers[i])) {
					flag = false;
					break;
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * Format the string to long type number
	 * 
	 * @param s
	 * @return a string
	 */
	public static long strToLong(String s) {
		long temp = 0;
		if (isNumeric(s)) {
			temp = Long.parseLong(s);
		}
		return temp;
	}

	/**
	 * Format input string make it same as the output string
	 * 
	 * @param input
	 * 
	 * @return buf
	 */
	public static String formatHTML(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		// Define a string buffer reference
		StringBuffer buf = new StringBuffer(input.length() + 6);
		char ch = ' ';
		// deal with the invalid character
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<')
				buf.append("&lt;");
			if (ch == '>')
				buf.append("&gt;");
			if (ch == '\n')
				buf.append("<br>");
			if (ch == '\'')
				buf.append("&acute");
			if (ch == ' ')
				buf.append("&nbsp;");
			buf.append(ch);
		}
		return buf.toString();
	}

	/**
	 * formatBr
	 * 
	 * @param input
	 * 
	 * @return a string
	 */
	public static String formatBr(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		// Define a string buffer reference
		StringBuffer bf = new StringBuffer("");
		String from = "<>";
		StringTokenizer st = new StringTokenizer(input, from, true);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (tmp != null && tmp.equals("<")) {
				String tmp2 = st.nextToken().toLowerCase();
				if (tmp2.equals("br")) {
					st.nextToken();
					bf = bf.append("");
				}
			} else {
				bf.append(tmp);
			}
		}
		return bf.toString();
	}

	/**
	 * encode
	 * 
	 * @param str
	 * @return not null return str，else return ""
	 */
	public static String encode(String str) {
		try {
			if (str != null) {
				return (new String(str.getBytes("iso-8859-1"), "gb2312"));
			} else
				return "";
		} catch (Exception e) {
			return e.toString();
		}
	}

	/**
	 * replace (eg：String a = replace("aaabbbccc","bbb","222"); ) (return
	 * value：a="aaa222ccc";)
	 * 
	 * @param str
	 * 
	 * @param substr
	 * 
	 * @param restr
	 * 
	 * @return a string
	 */
	public static String replace(String str, String substr, String restr) {
		String[] tmp = split(str, substr);
		String returnstr = null;
		if (tmp.length != 0) {
			returnstr = tmp[0];
			for (int i = 0; i < tmp.length - 1; i++)
				returnstr = dealNull(returnstr) + restr + tmp[i + 1];
		}
		return dealNull(returnstr);
	}

	/**
	 * @param source
	 * 
	 * @param strS
	 * 
	 * @param strD
	 * 
	 * @return Stringbuffer，eg： replace(new String("kkkk"),"k","e")，return
	 *         value"eeee"
	 */
	public static StringBuffer replaceAll(StringBuffer source, String strS, String strD) {
		if (strS == null || strD == null || strS.length() == 0 || strS.equals(strD))
			return source;
		int start = 0, offset = 0, tag = 0;
		while (source.indexOf(strS, tag) != -1) {
			start = source.indexOf(strS, tag);
			offset = start + strS.length();
			tag = start + strD.length();
			source.replace(start, offset, strD);
		}
		return source;
	}

	/**
	 * 
	 * Get the string tip between the given start str and end str
	 * 
	 * @param source
	 * 
	 * @param strStart
	 * 
	 * @param strEnd
	 * 
	 * @return StringBuffer
	 */

	public static StringBuffer takeString(StringBuffer source, String strStart, String strEnd) {
		int start = source.indexOf(strStart) + strStart.length();
		int end = source.indexOf(strEnd);
		if ((end > start) && end != -1 && start != -1) {
			source = new StringBuffer(source.substring(start, end));
			return source;
		}
		return new StringBuffer();
	}

	/**
	 * Function name: dealNull Description: \u8655 Input: String str Output: not
	 * null String
	 */
	public static String dealNull(Object str) {
		String returnstr = null;
		if (str == null) {
			returnstr = "";
		} else {
			returnstr = str.toString();
		}
		return returnstr;
	}

	/**
	 * Function name: split Description:(eg：String TTT[] =
	 * my_class.split("aaa:bbb:ccc:ddd",":") ; ) Output: (return
	 * value：TTT[0]="aaa"; TTT[1]="bbb"; TTT[2]="ccc"; TTT[3]="ddd"; )
	 */
	public static String[] split(String source, String div) {
		int arynum = 0, intIdx = 0, intIdex = 0, div_length = div.length();
		if (source.compareTo("") != 0) {
			if (source.indexOf(div) != -1) {
				intIdx = source.indexOf(div);
				for (int intCount = 1;; intCount++) {
					if (source.indexOf(div, intIdx + div_length) != -1) {
						intIdx = source.indexOf(div, intIdx + div_length);
						arynum = intCount;
					} else {
						arynum += 2;
						break;
					}
				}
			} else
				arynum = 1;
		} else
			arynum = 0;

		intIdx = 0;
		intIdex = 0;
		String[] returnStr = new String[arynum];

		if (source.compareTo("") != 0) {

			if (source.indexOf(div) != -1) {

				intIdx = (int) source.indexOf(div);
				returnStr[0] = (String) source.substring(0, intIdx);

				for (int intCount = 1;; intCount++) {
					if (source.indexOf(div, intIdx + div_length) != -1) {
						intIdex = (int) source.indexOf(div, intIdx + div_length);

						returnStr[intCount] = (String) source.substring(intIdx + div_length, intIdex);

						intIdx = (int) source.indexOf(div, intIdx + div_length);
					} else {
						returnStr[intCount] = (String) source.substring(intIdx + div_length, source.length());
						break;
					}
				}
			} else {
				returnStr[0] = (String) source.substring(0, source.length());
				return returnStr;
			}
		} else {
			return returnStr;
		}
		return returnStr;
	}

	/**
	 * @param name
	 * 
	 * @param c
	 * 
	 * @return String[]
	 */
	public static String[] split(String name, char c) {
		// Figure out the number of parts of the name (this becomes the size
		// of the resulting array).
		if (name == null)
			return null;
		int size = 1;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == c) {
				size++;
			}
		}
		String[] propName = new String[size];
		// Use a StringTokenizer to tokenize the property name.
		StringTokenizer tokenizer = new StringTokenizer(name, String.valueOf(c));
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			propName[i] = tokenizer.nextToken();
			i++;
		}
		return propName;
	}

	/**
	 * get file extension
	 * 
	 * @param filename
	 * @return a string of file ext name
	 */
	public static String getExt(String filename) {
		if (filename.indexOf(".") < 0) {
			return filename;
		}
		String[] strs = split(filename, '.');
		return strs[strs.length - 1];
	}

	/**
	 * judge a given string if include specified character
	 * 
	 * @param strs
	 *            String[]
	 * @param str
	 *            String
	 * @return boolean
	 */
	public static boolean isInclude(String[] strs, String str) {
		if (strs.length == 0 || str == null || str.length() == 0) {
			return false;
		}
		str = str.toLowerCase();
		for (int i = 0; i < strs.length; i++) {
			if (str.equals(strs[i]))
				return true;
		}
		return false;
	}

	// public static String htmlToText(String inputHtml) {
	// String cleanText = "";
	// Parser parser = Parser.createParser(gbToIso(inputHtml), "GBK");
	// try {
	// TextExtractingVisitor visitor = new TextExtractingVisitor();
	// parser.visitAllNodesWith(visitor);
	// cleanText = ParserUtils.removeEscapeCharacters(visitor
	// .getExtractedText());
	// } catch (ParserException e) {
	// log.error(e.toString());
	// }
	// return isoToGb(cleanText);
	// }

	public static String gbToIso(String str) {
		try {
			if (str != null && !str.equals("")) {
				byte[] byteStr = str.getBytes("gb2312");
				return new String(byteStr, "ISO-8859-1");
			} else
				return "";
		} catch (Exception e) {
			return str;
		}
	}

	public static String isoToGb(String str) {
		try {
			if (str != null && !str.equals("")) {
				byte[] byteStr = str.getBytes("ISO-8859-1");
				return new String(byteStr, "gb2312");
			} else
				return "";
		} catch (Exception e) {
			return str;
		}
	}

	//
	public static String retString(String str) {
		return str;
	}

	public static String singleParameter(String name, String value) {
		if (name != null && !"".equals(name)) {
			return "&" + name + "=" + value;
		}
		return "";
	}

	// create condition string
	public static String single1SearchContent(String searchname, String searchtype, String column) {
		if (searchname != null && !"".equals(searchname)) {
			if ("1".equals(searchtype)) {
				return " and " + column + " = '" + searchname + "'";
			} else {
				return " and " + column + " like '%" + searchname + "%'";
			}
		}
		return "";
	}

	// create condition string
	public static String single2SearchContent(String searchname, String searchtype, String column) {
		if (searchname != null && !"".equals(searchname)) {
			if ("1".equals(searchtype)) {
				return " and " + column + " = '" + searchname + "'";
			} else if ("2".equals(searchtype)) {
				return " and " + column + " < '" + searchname + "'";
			} else {
				return " and " + column + " > '" + searchname + "'";
			}
		}
		return "";
	}

	public static String GBTounicode(String s) {
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < s.length(); ++i) {
			ss.append("\\u" + Integer.toHexString(s.charAt(i)));
		}
		String temp = new String(ss);
		return temp;

	}

	public static String unicodeToGB(String s) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, "\\u");
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}

	

	/**
	 * 
	 * Make the value in the map to a string
	 * 
	 * @param map
	 * @param separator
	 * @return a string
	 */
	public static String serializeMapValue(Map<String, String> map, String separator) {
		String sResult = "";
		int i = 0;
		for (Iterator<String> iterator = map.values().iterator(); iterator.hasNext();) {
			String sTmp = iterator.next().toString();
			sResult += ((i == 0 ? "" : separator) + sTmp);
			i++;
		}
		return sResult;
	}

	/**
	 * @param strValue
	 * @return Integer[] IntegerResult
	 */
	public static Integer[] toInteger(String[] strValue) {
		Integer[] integerResult = new Integer[strValue.length];
		for (int i = 0; i < strValue.length; i++) {
			String st = strValue[i];
			Integer ii = Integer.valueOf(st);
			;
			integerResult[i] = ii;
		}
		return integerResult;
	}

	/**
	 * @param strValue
	 * @return boolean
	 */
	public static boolean isEmpty(String strValue) {
		if (null != strValue && !"".equals(strValue)) {
			return false;
		}
		return true;
	}

	public static String getTemplateStr(String format, Object... params) {
		String result = format;
		Pattern pattern = Pattern.compile("\\{\\}");
		Matcher matcher = pattern.matcher(format);
		for (Object object : params) {
			if (matcher.find()) {
				result = matcher.replaceFirst(object == null ? "" : object.toString());
				matcher = pattern.matcher(result);
			}
		}
		return result;
	}
	/**
	 * 字符串缩略显示
	 * @param str 源字符串
	 * @param lenght 需要显示的长度
	 * @return 返回的结果
	 */
	public static String shortStr(String str, int lenght){
		String shortStr = str;
		if(shortStr.length() >= lenght) {
			shortStr = str.substring(0,lenght)+"...";
		}
		return shortStr;
		
	}
	/**
	 * 银行账号缩略显示
	 * @param str 源字符串
	 * @param lenght 需要显示的长度
	 * @return 返回的结果
	 */
	public static String shortAccountStr(String str, int lenght){
		String shortStr = str;
		if(shortStr.length() >= lenght) {
			shortStr = "***"+str.substring(lenght,shortStr.length());
		}
		return shortStr;
		
	}
}
