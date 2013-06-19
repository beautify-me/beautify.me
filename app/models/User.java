package models;

import java.util.List;
import models.*;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import play.data.validation.*;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String name;
	
	public String lastName;
	
	@Required @MinSize(6) 
	public String userName;
	
	@Required @MinSize(6) @Password
	public String password;
	
	@Required @Email 
	public String email;
	
	@Range(min=12, max=120) 
	public Integer age;
	
	public boolean isAdmin;
	
	public String avatarUrl;
	
	@ElementCollection
	public Map<String, Accessory> myAccesories;
	
	//@ElementCollection
	//public Map<String, Pic> myPics;
	
	//facebook
	//gplus
	//twitter
	//openid
	
	public User(String email, String password){
		this.email = email;
		this.password = password;
	}
	
	public User(String email, String password, String lastName, String name, boolean isAdmin) {
		this.email = email;
		this.password = password;
		this.lastName = lastName;
		this.name = name;
		this.isAdmin = isAdmin;
		this.userName = userName;
	}
	
	public User(String email, String password, String userName, boolean isAdmin){
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.isAdmin = isAdmin;
	}
	 
	public static User connect (String email, String password){
		return find("byEmailAndPassword", email, password).first();
	}
	
	public static List<User> listAdminUsers(){
		List<User> users = User.find("select u from User u where u.isAdmin = true").fetch();
		return users;
	}
	
	public static User getUserByEmail(String email){
		List <User> users = User.find("select u from User u where u.email = '" + email + "'").fetch();
		User user = users.get(0);
		return user;
	}
	
	public static List<User> listUsers(){
		List<User> users = User.find("select u from User where u.isAdmin = false").fetch();
		return users;
	}
	
	public String toString(){
		return name + " " + lastName;
	}
	
		
}
