package cantro.util;

public class StringUtil {
	public static String CombineWithColons(Object ... strings)
	{
		String ret = "";
		for(Object s : strings)
		{
			ret+=s.toString();
			ret+=":";
		}
		return ret.substring(0,ret.length()-1);
	}
}