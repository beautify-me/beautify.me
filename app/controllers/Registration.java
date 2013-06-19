package controllers;

import models.*;
import ugot.recaptcha.*;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.IsTrue;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;

public class Registration extends Controller{

	
	public static void registration(){
		render();
	}
	
	public static void pending() {
		render();
	}
	
	public static void confirm(String code) {
		System.out.println(code);
		login();
	}
	
	public static void login() {
		render();
	}
	
	
	public static void registrationFinish(@Required @Email String email,
			@Required @MinSize(6) @Equals("confirmPassword") String password,
			@Required @MinSize (6) String userName,
			@Required @MinSize(6)  String confirmPassword,
			@Recaptcha String captcha,
			@IsTrue Boolean acceptTermsOfUse) {
		
		checkAuthenticity();
		
		if (Validation.hasErrors()) {
			flash.error("registration.error");
			Validation.keep();
			params.flash();
			registration();
		} else {
			// Valid Registration
			User user = new User(email, password, userName, false);
			try {
				user.save();
				Mails.message(user.email, "welcome to beautify.me");
				pending();
			} catch (Exception e) {
                // User already exists
                flash.error("registration.user_exists");
                registration();
			}
		}
		
		}
	

}
