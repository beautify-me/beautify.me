package models;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class Accessory extends Model {
	public static final String TYPE_HAT = "Hat";
	public static final String TYPE_GLASSES = "Glasses";
	
	public String type;
	public String gender;
	public String color;
	public Provider provider;
	//popularity
}
