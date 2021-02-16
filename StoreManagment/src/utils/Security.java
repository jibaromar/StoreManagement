package utils;

import java.security.MessageDigest;
import java.util.Formatter;

public class Security {
	
	private static String bytesToHex(byte[] bytes) {
		String hex = null;
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
		    formatter.format("%02x", b);
		}
		hex = formatter.toString();
		formatter.close();
		return hex;
	}
	
	public static String encrypt(byte[] inputBytes, String algo) {
		String hash = null;
		
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algo);
			messageDigest.update(inputBytes);
			byte[] digestedMessage = messageDigest.digest();
			hash = bytesToHex(digestedMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return hash;
	}
}
