package controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import models.*;
import controllers.Secure;

public class Security extends Secure.Security {
	
	
	
	static boolean authenticate (String username, String password){
		User user = User.find("byEmail", username).first();
		return user != null && user.passwordHash.equals(getHashForPassword(password));
	}
	
	static boolean authentify(String username, String password){
		return User.connect(username, password) != null;
	}
	
	static boolean check(String profile){
		if ("admin".equals(profile)) {
			User user = User.find("byEmail",connected()).<User>first();
			if(user != null){
				return user.isAdmin;
			}
		}
		return false;
	}
	
		
	public static String getHashForPassword(String password){
		String hashPassword = null;
		
		if(null == password) return null;
		
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes(), 0, password.length());
			hashPassword = new BigInteger(1,digest.digest()).toString(16);		
			}catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		return hashPassword;
	}


}
