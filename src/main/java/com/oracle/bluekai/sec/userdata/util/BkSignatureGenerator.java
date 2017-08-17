package com.oracle.bluekai.sec.userdata.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

public class BkSignatureGenerator {

	private static final Logger logger = LoggerFactory.getLogger(BkSignatureGenerator.class);

	public static String generateBkSig(String wsPrivateKey, HttpMethod httpMethod, URIBuilder uri, String postData)
			throws NoSuchAlgorithmException, InvalidKeyException {

		logger.info("Generating BK Signature");

		StringBuilder queryArgValues = extractQueryArgValues(uri);

		logger.info(Util.logValue("Http Method", httpMethod.toString()));
		logger.info(Util.logValue("URI Path", uri.getPath()));
		logger.info(Util.logValue("Query Arg Values", queryArgValues.toString()));
		logger.info(Util.logValue("Post Data", postData));

		String stringToSign = httpMethod.toString() + uri.getPath() + queryArgValues.toString() + postData;

		logger.info(Util.logValue("String To Sign", stringToSign));

		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(wsPrivateKey.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);

		String bksig = Base64.encodeBase64String(sha256_HMAC.doFinal(stringToSign.getBytes()));
		logger.info(Util.logValue("BK Signature", bksig));

		return bksig;
	}

	public static String generateBkSig(String wsPrivateKey, HttpMethod httpMethod, URIBuilder uri)
			throws NoSuchAlgorithmException, InvalidKeyException {

		return generateBkSig(wsPrivateKey, httpMethod, uri, "");
	}

	private static StringBuilder extractQueryArgValues(URIBuilder uri) {
		StringBuilder queryArgValues = new StringBuilder();

		for (NameValuePair nvp : uri.getQueryParams()) {
			String argValueEncoded;
			try {
				argValueEncoded = URLEncoder.encode(nvp.getValue(), "UTF-8");
				queryArgValues.append(argValueEncoded);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return queryArgValues;
	}
}
