package com.oracle.bluekai.sec.userdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oracle.bluekai.sec.userdata.api.UserDataApiConnector;
import com.oracle.bluekai.sec.userdata.util.Util;

@Component
@Configuration()
public class CommandLineAppStartupRunner implements CommandLineRunner {

	private static final int OUTPUT_WIDTH = 90;

	private static final String NOT_PROVIDED = "NOTPROVIDED";

	private static final int NOT_PROVIDED_INT = -1;

	private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

	@Autowired
	UserDataApiConnector userDataApiConnector;

	@Value("${baseUrl}")
	private String baseUrl;

	@Value("${wsUid}")
	private String wsUid;

	@Value("${wsPrivateKey}")
	private String wsPrivateKey;

	@Value("${siteId}")
	private int siteId;

	@Value("${bkuuid}")
	String bkuuid;

	@Value("${pUserId}")
	String pUserId;

	@Value("${pField}")
	String pField;

	@Value("${phints}")
	String phints;

	@Override
	public void run(String... args) throws Exception {

		logger.info("-=-=-=-=-=-=-=-=-=- Parameters -=-=-=-=-=-=-=-=-=-=-=-");
		logger.info(Util.logValue("baseUrl", baseUrl));
		logger.info(Util.logValue("wsUid", wsUid));
		logger.info(Util.logValue("wsPrivateKey", wsPrivateKey));
		logger.info(Util.logValue("siteId", siteId));
		logger.info(Util.logValue("bkuuid", bkuuid));
		logger.info(Util.logValue("pUserId", pUserId));
		logger.info(Util.logValue("pField", pField));

		logger.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

		boolean ok = false;
		String msg = null;

		String[] phintsArray = phints.split("&");

		if (baseUrl.equals(NOT_PROVIDED)) {
			msg = "baseUrl is required";
		} else if (wsUid.equals(NOT_PROVIDED)) {
			msg = "wsUid is required";
		} else if (wsPrivateKey.equals(NOT_PROVIDED)) {
			msg = "wsPrivateKey is required";
		} else if (siteId == NOT_PROVIDED_INT) {
			msg = "siteId is required";
		} else if (!pUserId.equals(NOT_PROVIDED) && !pField.equals(NOT_PROVIDED)) {
			ok = true;
			userDataApiConnector.doGetDataByPartnerId(pUserId, pField, phintsArray, wsUid, wsPrivateKey, siteId);
		} else if (!bkuuid.equals(NOT_PROVIDED)) {
			ok = true;
			userDataApiConnector.doGetDataByBkuuid(bkuuid, phintsArray, wsUid, wsPrivateKey, siteId);
		} else {
			msg = "Either (bkuuid) OR (pUserId AND pField) must be specified";

		}

		if (!ok) {
			logUsage(msg);
		}

	}

	private void logUsage(String msg) {

		StringBuilder u = new StringBuilder();
		addToOutput(u, "");
		addToOutput(u, Util.fill(OUTPUT_WIDTH, "=") + "");
		addToOutput(u, Util.fill(OUTPUT_WIDTH, "=") + "");
		addToOutput(u, "");

		addToOutput(u, " _   _               ___       _            _   ___ ___ ");
		addToOutput(u, "| | | |___ ___ _ _  |   \\ __ _| |_ __ _    /_\\ | _ \\_ _|");
		addToOutput(u, "| |_| (_-</ -_) '_| | |) / _` |  _/ _` |  / _ \\|  _/| | ");
		addToOutput(u, " \\___//__/\\___|_|   |___/\\__,_|\\__\\__,_| /_/ \\_\\_| |___|");

		if (msg != null) {
			addToOutput(u, "");
			addToOutput(u, "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

			addToOutput(u, "** ERROR **");
			addToOutput(u, msg + "");
			addToOutput(u, "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			addToOutput(u, "");
		}

		addToOutput(u, "More information: https://dstreet.datalogix.com/display/TC/User+Data+API+App");
		addToOutput(u, "");
		addToOutput(u, Util.fill(OUTPUT_WIDTH, "="));
		addToOutput(u, Util.fill(OUTPUT_WIDTH, "="));

		logger.info(u.toString());
	}

	private void addToOutput(StringBuilder u, String m) {
		if (m.length() >= OUTPUT_WIDTH) {
			u.append(m);
		} else {
			int l = m.length();
			int pre = Math.floorDiv(OUTPUT_WIDTH - l, 2);
			u.append(Util.fill(pre, " ") + m);
		}
		u.append("\n");

	}

}