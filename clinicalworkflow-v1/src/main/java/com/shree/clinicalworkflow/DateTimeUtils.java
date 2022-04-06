package com.shree.clinicalworkflow;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtils {
	
	public static String getTime(final Long yourmilliseconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date resultdate = new Date(yourmilliseconds);
		return sdf.format(resultdate);
		}
	
	public static String getDateTime(final Date date,final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
		}

}
