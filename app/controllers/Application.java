package controllers;

import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.IsTrue;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.mvc.*;
import ugot.recaptcha.Recaptcha;

import java.util.*;

import models.*;
import controllers.*;

public class Application extends Controller {

    public static void index() {
      	render();
    }
    
    
    public static void accessories() {
        List<Accessory> accessories = Accessory.findAll();
        Collections.shuffle(accessories); // shuffle for dummy display to be suffled
         render(accessories);
    }
    
    public static void getPic(long id) {
    	Accessory a = Accessory.findById(id);
    	/*Pic im = a.image;
    	response.setContentTypeIfNotSet(im.image.type());
    	renderBinary(im.image.get());*/
    	Blob image = a.image;
    	response.setContentTypeIfNotSet(image.type());
    	renderBinary(image.get());
    }
    
    
    public static void top() {
    	render();
    }
    public static void mystuff() {
        render();
    }
    public static void termsofuse() {
        render();
    }
    public static void privacy() {
        render();
    }
    public static void about() {
        render();
    }

    public static void registration(){
		render();
	}
    
    public static void signin(){
    	render();
    }
	
	public static void pending() {
		render();
	}
    public static void contact(String address, String message) {  
        Mails mail = new Mails();
        mail.message(address, message);
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
				Mails.message(user.email, "welcome to beautify.me");
				System.out.println("USER_ID " + user.getId());
				session.put("userID", user.getId());
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
					session.put("userID", user.getId());
					render();
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
}
