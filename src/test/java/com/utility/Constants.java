package com.utility;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {
	public static final String USER_DIR=System.getProperty("user.dir");
	public static final String APPLICATION_PROPERTIES=USER_DIR+"/src/test/resources/testData.properties";
	public static final String SPARKS_HTML_REPORT_PATH=USER_DIR+"/reports/testreport.html";



	    private static Properties prop;

	    static {
	        prop = new Properties();
	        try (FileInputStream fis = new FileInputStream(APPLICATION_PROPERTIES)) {
	            prop.load(fis);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static String getUri() {
	        return prop.getProperty("uri");
	    }

	    public static String getUsername() {
	        return prop.getProperty("username");
	    }

	    public static String getPassword() {
	        return prop.getProperty("password");

	    }
	    
}
	
	

