package pharos.eht.autoClaim.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesReader4prop {


	private static PropertiesReader4prop impl = null;
	private static Properties prop = null;
	private static String filePath = "config.properties";
//	private static String filePath = "./claimWebConfig.properties";

	private static synchronized void init(String filePath) throws Exception {
		if (prop != null)
			return;
		impl = new PropertiesReader4prop();
		prop = new Properties();
//		FileReader reader = new FileReader(filePath);
		
		String path = PropertiesReader4prop.class.getClassLoader().getResource(filePath).getPath();
		InputStream fis =  new FileInputStream(new File( path));
		prop.load(fis);
//		reader.close();
	}

	public static PropertiesReader4prop getInstance() {
		if (prop == null) {
			synchronized (filePath) {
				try {
					if (prop == null)
						init(filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return impl;
	}
	
	public String readValueByName(String name) {
		return prop.getProperty(name);
	}
	
	public static PropertiesReader4prop reBoot() {
		try {
			prop = null;
			impl = null;
			init(filePath);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return impl;
	}
	
	public static void main(String[] args) {
		String t = PropertiesReader4prop.reBoot().readValueByName("CLMErrorInfo");
		String ret = "";
		try {
			ret = new String(t.getBytes("ISO-8859-1"),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(ret);
	}

}
