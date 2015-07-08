package com.rbs.retailtherapy.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PlainFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		return "\n|" + record.getLevel() + "|" + record.getLoggerName() + "|" + record.getMessage();
	}

}
