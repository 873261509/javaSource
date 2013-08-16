package pharos.eht.autoClaim.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class PropertiesReader {
	
	private static ResourceBundle bundle = null;
	
	static{
		bundle =  ResourceBundle.getBundle("config",Locale.SIMPLIFIED_CHINESE);
	}
	
	/**
	 * 返回指定 key 的 value
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return bundle.getString(key);
	}


	public static String getConnURL() {
		return bundle.getString("ConnURL");
	}
	
	public static String getConnUserName() {
		return bundle.getString("ConnUserName");
	}
	
	public static String getConnPassword() {
		return bundle.getString("ConnPassword");
	}
	
	
	public static String getConnDriver() {
		return bundle.getString("ConnDriver");
	}
	
	public static List<String> getAirProductCodes() {
		List<String> ret = new ArrayList<String>();
		String validate = bundle.getString("isValidate");
		if(validate.equals("false")) {
			return ret;
		}
		String ps = bundle.getString(ConstConfigName.ProperitesNames.AIR_INCLUDE_PRODUCTCODE);
		String[] ss = ps.split(",");
		ret = Arrays.asList(ss);
		return ret;
	}
	
	
	public static void main(String[] args) {
		String d =	PropertiesReader.getConnDriver();
		System.out.println(d);
		List<String> s = PropertiesReader.getAirProductCodes();
		System.out.println(s.size());
		for (String string : s) {
			System.out.println(string);
		}
		
	}
}
