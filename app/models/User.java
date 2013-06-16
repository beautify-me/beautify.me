package models;

import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class User extends Model {
	
	public String name;
	public String lastName;
	public String userName;
	public String email;
	public String avatarUrl;
	public List<Accessory> myAccesories;
	public List<Pic> myPics;
	//facebook
	//gplus
	//twitter
	//openid
}
