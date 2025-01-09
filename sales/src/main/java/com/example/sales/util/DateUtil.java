package com.example.sales.util;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateUtil {

	public static YearMonth getPreviousMonth() {
		LocalDate today = LocalDate.now();
		return YearMonth.from(today.minusMonths(1));
	}

}
