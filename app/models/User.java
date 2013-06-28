package models;

import java.util.List;

import models.*;
import controllers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.Play;
import play.data.validation.*;
import ugot.recaptcha.Recaptcha;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	public String name;
	
	public String lastName;
	
	@Required @MinSize(6) 
	public String userName;
	
	@Required @Password
	public String passwordHash;
	
	@Required @Email 
	public String email;
	
	@Range(min=12, max=120) 
	public Integer age;
	
	public boolean isAdmin;
	
	public String avatarUrl;
	
	@ManyToMany
	@ElementCollection
	public Map<Long, Accessory> myAccesories = new HashMap<Long, Accessory>();
	
	@OneToMany
	@ElementCollection
	public Map<Long, Pic> myPics = new HashMap<Long, Pic>();
	
	//@ElementCollection
	//public Map<String, Pic> myPics;
	
	//facebook
	//gplus
	//twitter
	//openid
	
	public User(String email, String password){
		this.email = email;
		this.passwordHash = Security.getHashForPassword(password);

	}
	
	public User(String email, String password, String lastName, String name, boolean isAdmin) {
		this.email = email;
		this.passwordHash = Security.getHashForPassword(password);
		this.lastName = lastName;
		this.name = name;
		this.isAdmin = isAdmin;
		this.userName = userName;

	}
	
	public User(String email, String password, String userName, boolean isAdmin){
		this.email = email;
		this.passwordHash = Security.getHashForPassword(password);
		this.userName = userName;
		this.isAdmin = isAdmin;

	}
	 
	public static User connect (String email, String password){
		return find("byEmailAndPassword", email, password).first();
	}
		
	public String toString(){
		return userName;
	}
	
	public void addToMyAccesories(Accessory accessory){
		myAccesories.put(accessory.id, accessory);
	}
	
	public void removeFromMyAccessories(Accessory accessory){
		myAccesories.remove(accessory.id);
	}
	
	public void addPic(Pic pic){
		myPics.put(pic.id, pic);
	}
	
	public void removePic(Pic pic){
		myPics.remove(pic.id);
	}
	
	
		
}
