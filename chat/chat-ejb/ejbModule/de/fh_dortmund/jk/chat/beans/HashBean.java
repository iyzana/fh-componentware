package de.fh_dortmund.jk.chat.beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

@Startup
@Stateless
public class HashBean {
	@Resource(name = "hash-algorithm")
	private String algorithm;
	
	@PostConstruct
	public void init() {
		computeHash("");
	}

	public String computeHash(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] hash = digest.digest(data.getBytes());
			return DatatypeConverter.printHexBinary(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
