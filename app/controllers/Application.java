package controllers;

import play.*;
import play.db.jpa.Blob;
import play.mvc.*;

import java.util.*;

import models.*;
import controllers.*;

public class Application extends Controller {

    public static void index() {
       render(); 
    }
    
    public static void home(){
    	String user = Security.connected();
    	render(user);
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
    
    public static void contact(String address, String message) {  
        Mails mail = new Mails();
        mail.message(address, message);
        render();
    }
}
