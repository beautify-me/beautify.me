package controllers;

import models.User;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.IsTrue;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import ugot.recaptcha.Recaptcha;

public class Registration extends Controller{
	 public static void registration(){
			render();
		}
	    
	    public static void signin(){
	    	render();
	    }
	    
	    public static void login() {
			render();
		}
		
		public static void registrationFinish(
			@Required @MinSize(6) String username,
			@Required String firstname, 
			@Required String lastname,
	        Integer age,
	        @Required @MinSize(6) String password,
	        @Required @MinSize(6) @Equals("password") String passwordConfirm,
	        @Required @Email String email,
	        @Required @Email @Equals("email") String emailConfirm,
	        @IsTrue boolean termsOfUse,
	        @Recaptcha String captcha) {
					
			if (Validation.hasErrors()) {
				flash.error("registration.error");
				//Validation.keep();
				//params.flash();
				//registration();
				render("@registration");
			} else {
				// Valid Registration
				User user = new User(email, password, username, false);
				try {
					user.save();			
					System.out.println("USER_ID " + user.getId());
					session.put("USER_ID", user.id);
					Mails.message(user.email, "welcome to beautify.me");
					render();
				} catch (Exception e) {
	                // User already exists
	                flash.error("registration.user_exists");
	                render(user);
	                
				}
			}
			
			}
		
		public static void confirm(String code) {
			System.out.println(code);
			login();
		}
		
		public static void signinFinish(
				@Required @Email String email,
				@Required @MinSize(6) String password){
			
			if (Validation.hasErrors())
			{
				flash.error("registration.error");
				render("@signin");
			} else {
				try {
					if (Security.authenticate(email, password)) {
						//user exists -> log in
						System.out.println("user exists");
						User user = User.getUserByEmail(email);
						session.put("USER_ID", user.id);
						Application.setConnectedUser();
						Application.index();
					} else {
						//user does not exist -> error
						System.out.println("user does not exist");
						render("@signin");
					}
				} catch (Exception e) {
					flash.error("authentication.error");
					render();
				}
		    }		
		}
	    public static void signout(){
	        session.clear();
	        response.removeCookie("rememberme");     
	        flash.success("secure.logout");       
	        Application.setConnectedUser();
	        Application.index();
	    }
		public static void lostPassword(@Required @Email String email_forgot_pw){
			boolean hasErrors = true;
			hasErrors = validation.hasErrors();
			
			if (hasErrors) {
				flash.error("email.error");
				render("@signin");
			} else {
			
				if (User.getUserByEmail(email_forgot_pw) != null) {
					User user = User.getUserByEmail(email_forgot_pw);
					String newPassword = Users.createNewPassword(user);
					Mails.message(user.email, newPassword);
					user.passwordHash = Security.getHashForPassword(newPassword);
				}
			}
		}
}
