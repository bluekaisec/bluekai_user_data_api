package com.oracle.bluekai.sec.userdata.api;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.oracle.bluekai.sec.userdata.rest.RestTemplateFactory;
import com.oracle.bluekai.sec.userdata.util.BkSignatureGenerator;
import com.oracle.bluekai.sec.userdata.util.Util;

@Component
public class UserDataApiConnector {
	private static final Logger logger = LoggerFactory.getLogger(UserDataApiConnector.class);

	public void doGetDataByBkuuid(String bkuuid, String[] phints, String wsUid, String wsPrivateKey, int siteId) {

		logger.info("GetData using BKUUID: " + bkuuid);

		try {
			URIBuilder uri = baseUri(siteId);
			uri.addParameter("userid", bkuuid);
			start(uri, phints, wsUid, wsPrivateKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doGetDataByPartnerId(String pUserId, String pField, String[] phints, String wsUid, String wsPrivateKey,
			int siteId) {

		logger.info("GetData using Partner ID: " + pUserId + " / " + pField);

		try {
			URIBuilder uri = baseUri(siteId);
			logger.info("Using partner ID: " + pUserId + " / " + pField);
			uri.addParameter("puserid", pUserId);
			uri.addParameter("pfield", pField);
			start(uri, phints, wsUid, wsPrivateKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private URIBuilder baseUri(int siteId) throws URISyntaxException {
		return new URIBuilder("https://api.tags.bluekai.com/getdata/" + siteId + "/v1.2");
	}

	private void start(URIBuilder uri, String[] phints, String wsUid, String wsPrivateKey)
			throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

		processPhints(uri, phints);
		HttpMethod httpMethod = HttpMethod.GET;

		String bksig = BkSignatureGenerator.generateBkSig(wsPrivateKey, httpMethod, uri);

		uri.addParameter("bkuid", wsUid);
		uri.addParameter("bksig", bksig);

		doApiCall(uri);
	}

	private void doApiCall(URIBuilder uri) throws URISyntaxException {
		logger.info("Placing API request");
		

		Util.logUrlPretty(uri.build().toString());

		RestTemplate restTemplate = RestTemplateFactory.newInstance();

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(uri.build(), String.class);
			logger.info(Util.logValue("Response Code", response.getStatusCode().toString()));
			logger.info(Util.logValue("Response Body", "\n" + response.getBody()));
		} catch (Exception e) {
			logger.error("EXCEPTION: " + e.getMessage());
		}

	}

	private void processPhints(URIBuilder uri, String[] phints) {

		for (int i = 0; i < phints.length; i++) {
			String p = phints[i];
			logger.info("Found phint: " + p);
			uri.addParameter("phint", p);

		}

	}

}
