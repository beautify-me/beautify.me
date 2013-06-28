package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import play.db.jpa.*;

import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class Accessory extends Model {
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public static final int UNISEX = 2;
	
	public static final int TYPE_HAT = 12;
	public static final int TYPE_GLASSES = 13;
	
	public String articleName;
	public int type;
	public int gender;
	//public String color;
	
	public Blob image;  //how to store images https://gist.github.com/steren/660937
	// http://blog.lunatech.com/2011/04/26/playframework-file-upload-blob
	
	@ManyToOne
	public Provider provider;
	
	@ManyToMany
	@ElementCollection
	public Map<Long, User> myUsers = new HashMap<Long, User>();;

	public Accessory(String name, int type, int gender, Blob image, Provider provider) {
		this.articleName = name;
		this.type = type;
		this.gender = gender;
		this.image = image;
		this.provider = provider;
		
	}
	
	public void addToMyUsers(User user){
		myUsers.put(user.id, user);
		
	}
	
	public void removeFromMyUsers(User user){
		myUsers.remove(user.id);
	}
}
