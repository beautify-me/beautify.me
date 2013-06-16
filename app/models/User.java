package models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class User extends Model {
	
	public String name;
	public String lastName;
	public String userName;
	public String email;
	public String avatarUrl;
	@ElementCollection
	public Map<String, Accessory> myAccesories;
	@ElementCollection
	public Map<String, Pic> myPics;
	//facebook
	//gplus
	//twitter
	//openid
}
