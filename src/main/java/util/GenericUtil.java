package util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class GenericUtil {

	public static String getRandomAlphabet(int length) {
		return RandomStringUtils.random(length);
	}
	
	public static String getRandomNumeric(int length) {
		return RandomStringUtils.randomNumeric(length);
	}
	
	public static String getRandomAlphaNumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	//email
	public static String getRandomEmail(int length) {
		String randomString = RandomStringUtils.randomAlphabetic(length);
		return randomString.toLowerCase() + "@gmail.com";
	}
	
	//generate date in yyyy-mm-dd hh-mm-ss format
	public static String getCurrentDateTime() {
		Date date = new Date();
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(date);
	}
}
