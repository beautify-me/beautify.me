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

import com.mysql.jdbc.Constants;

import models.*;
import controllers.*;

public class Application extends Controller {
		 /*
	     * Priority lets the Framework know
	     * the order which the @Before's should
	     * be run http://seepatcode.com/2013/02/28/play-framework-1-2-5-setting-global-variables-in-controller-filters/
	     */
	    @Before(priority=10)
	    static void setConnectedUser()
	    {
	    	if(session.contains("USER_ID")) {
	    		User user = User.findById(Long.valueOf(session.get("USER_ID")));
	            renderArgs.put("user", user);
	        }
	    }
	    @Util
	    public static User getUser() {
	        return (User) renderArgs.get("user");
	    }

    public static void index() {
      	render();
    }
   
    public static void accessories() {
        List<Accessory> accessories = Accessory.findAll();
        Collections.shuffle(accessories); // shuffle for dummy display to be shuffled
         render(accessories);
    }
    
    public static void getAccessory(Long id) {
    	Accessory a = Accessory.findById(id);
    	/*Pic im = a.image;
    	response.setContentTypeIfNotSet(im.image.type());
    	renderBinary(im.image.get());*/
    	Blob image = a.image;
    	response.setContentTypeIfNotSet(image.type());
    	renderBinary(image.get());
    }
    
    public static void getPic(Long id) {
    	Pic p = Pic.findById(id);
    	Blob image = p.image;
    	response.setContentTypeIfNotSet(image.type());
    	renderBinary(image.get());
    }
       
    public static void top() {
    	render();
    }
    public static void mystuff() {
//    	User user = User.findById(params.get("id"));
//    	Map<Long, Pic> userpics = user.myPics;
//    	Map<Long, Accessory> useraccessories = user.myAccesories;
    	List<Pic> userpics = Pic.findAll();
    	List<Accessory> useraccessories = Accessory.findAll();
        render(userpics, useraccessories);
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

    public static void me() {
        render("@user");
    }
	
    public static void contact(String address, String message) {  
        Mails mail = new Mails();
        mail.message(address, message);
        render();
    }
    
    public static void addFavorite(User user, Accessory accessory){
    	User currentUser = User.findById(user.id);
    	Accessory currentAccessory = Accessory.findById(accessory.id);
    	currentUser.addToMyAccesories(currentAccessory);
    	currentAccessory.addToMyUsers(currentUser);
    }
    
   
}
