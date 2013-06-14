package models;

import play.db.jpa.Model;

public class Accesory extends Model {
	
	public static final String TYPE_HAT = "Hat";
	public static final String TYPE_GLASSES = "Glasses";
	
	public String type;
	public String gender;
	public String color;
	public Producer producer;
	//popularity
	
	
	

}
