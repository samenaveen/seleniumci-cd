package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	
	private static Properties props = new Properties();
	
	public static void loadProperties() throws Exception {
		logger.info("Loading properties file...");
		String env = System.getProperty("env");
		if (env == null || env.trim().isEmpty()) {
			logger.warn("System property 'env' is not set. Falling back to 'dev'.");
			env = "dev";
		} else {
			logger.info("Environment specified: {}", env);
		}
		// Try loading from workspace path first (use File.separator for cross-platform)
		String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "properties" + File.separator + env + "_application.properties";
		logger.info("Attempting to load properties file from path: {}", path);
		try (FileInputStream fis = new FileInputStream(path)) {
			props.load(fis);
			logger.info("Loaded properties from {}", path);
			return;
		} catch (Exception e) {
			logger.warn("Could not load properties file from path: {}. Will try classpath resource. Error: {}", path,
					e.getMessage());
		}
		// Fallback: try loading from classpath (src/test/resources packaged)
		String classpathResource = "/properties/" + env + "_application.properties";
		logger.info("Attempting to load properties from classpath: {}", classpathResource);
		try (java.io.InputStream is = PropertyUtil.class.getResourceAsStream(classpathResource)) {
			if (is != null) {
				props.load(is);
				logger.info("Loaded properties from classpath: {}", classpathResource);
				return;
			} else {
				logger.warn("Classpath resource {} not found.", classpathResource);
			}
		} catch (Exception e) {
			logger.error("Error loading properties from classpath: {}", e.getMessage(), e);
			throw e;
		}
		// If still not loaded, throw a clear exception
		throw new java.io.FileNotFoundException("Properties file for environment '" + env + "' not found in workspace path or classpath.");
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key, "");
	}
	
	public static void main(String[] args) {
		try {
			loadProperties();
			System.out.println("Properties loaded successfully.");
		    String application_url = props.getProperty("application_url");
		    System.out.println("Application URL: " + application_url);
		} catch (Exception e) {
			System.out.println("Failed to load properties.");
		}
	}
}
