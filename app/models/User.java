package models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import play.data.validation.*;

import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Required public String name;
	@Required public String lastName;
	@Required @MinSize(6) public String userName;
	@Required @Email public String email;
	@Range(min=12, max=120) public Integer age;
	
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
