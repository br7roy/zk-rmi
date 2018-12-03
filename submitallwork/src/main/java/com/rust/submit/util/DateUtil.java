package com.rust.submit.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Rust
 */
public class DateUtil {

	public static String formatLocalDateTime(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
				.withLocale(Locale.getDefault())
				.withZone(ZoneId.systemDefault());

		return formatter.format(localDateTime);
	}

	public static String formatLocalDateTime8bit(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("yyyy-MM-dd")
				.withLocale(Locale.getDefault())
				.withZone(ZoneId.systemDefault());

		return formatter.format(localDateTime);
	}
	public static long now(){
		return System.currentTimeMillis();
	}

	public static  boolean checkDateIfNotNeedSchedule() {
		LocalDateTime ldt = LocalDateTime.now();
		// 6,7不执行
		return ldt.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)== Calendar.SATURDAY-1||	ldt.getDayOfWeek().get(ChronoField.DAY_OF_WEEK)== Calendar.MONDAY-1;
	}
}
