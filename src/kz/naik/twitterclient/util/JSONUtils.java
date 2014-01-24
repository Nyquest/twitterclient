package kz.naik.twitterclient.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JSONUtils {

	private static SimpleDateFormat SDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy",Locale.ENGLISH);
	
	public static Date parseDate(String text) throws ParseException{
		if(text == null) return null;
		return SDF.parse(text);
	}
	
	
}
