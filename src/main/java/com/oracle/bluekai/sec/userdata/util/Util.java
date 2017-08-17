package com.oracle.bluekai.sec.userdata.util;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	private static final int NAME_VALUE_LOG_WIDTH = 20;

	public static String logValue(String n, String v) {

		String padding = "";
		if (n.length() <= NAME_VALUE_LOG_WIDTH) {
			padding = fill(NAME_VALUE_LOG_WIDTH - n.length(), " ");

		}

		return " > " + n + ":" + padding + v;

	}

	public static String logValue(String n, int v) {
		return logValue(n, Integer.toString(v));

	}

	public static String fill(int length, String s) {

		StringBuffer outputBuffer = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			outputBuffer.append(s);
		}
		return outputBuffer.toString();
	}

	public static void logUrlPretty(String uri) throws URISyntaxException {
		String[] uriSplit = uri.toString().split("\\?");

		logger.info("    " + uriSplit[0]);
		String[] args = uriSplit[1].split("&");

		int i = 0;
		for (String a : args) {
			if (i == 0) {
				logger.info("      ?" + a);
			} else {
				logger.info("      &" + a);
			}

			i++;
		}
	}

}
