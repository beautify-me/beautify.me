package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.*;

import models.*;

import java.util.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Check("admin")
@With(Secure.class)

public class Users extends CRUD{
	

	
	
	public static void admin(){
		List<User> users = User.listAdminUsers();
		render(users);
	}
	
	public static void blankAdmin(){
		render("Users/blankAdmin.html");
	}
	
	public static void showAdmin(Long id){
		User object = User.findById(id);
		render("Users/showAdmin.html", object);
	}
	
	public static void createAdmin(){
		String email = params.get("object.email");
		String password = params.get("object.password");
		String name = params.get("object.name");
		String lastName = params.get("object.lastName");
		
		validation.required(email);
		validation.email(email);
		validation.required(password);
		
		if (validation.hasErrors()){
			params.flash();
			validation.keep();
			redirect("/admin/users/admin/new");
		}
		
		User user = new User(email, password, name, lastName, true);
		user.save();
		
		if (params.get("_save") != null) {
			System.out.println("save");
			redirect("/admin/users");
		} else if (params.get("_saveAndContinue") != null) {
			System.out.println("saveAndContinue");
			redirect("/admin/users/admin/" +user.id);
		}
		System.out.println("saveAndAdd");
		redirect("/admin/users/admin/new");
	}
	
	public static void deleteUser(User user){
		
	}
	
	public static String createNewPassword(@Required User user) {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130,random).toString(10);
	}
	
	
	
}	
	
	


