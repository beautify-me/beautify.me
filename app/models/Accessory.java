package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class Accessory extends Model {
	public static final int FEMALE = 1;
	public static final int MALE = 2;
	public static final int UNISEX = 3;
	
	public static final int TYPE_HAT = 12;
	public static final int TYPE_GLASSES = 13;
	
	@Required
	public String articleName;
	
	@Required
	public int type;
	
	@Required
	public int gender;
	
	@Required
	public Blob image;  //how to store images https://gist.github.com/steren/660937
	// http://blog.lunatech.com/2011/04/26/playframework-file-upload-blob
	
	@ManyToOne
	public Provider provider;
	
	@Column(name="likes")
	public int likes;

	public Accessory(String name, int type, int gender, Blob image, Provider provider, int likes) {
		this.articleName = name;
		this.type = type;
		this.gender = gender;
		this.image = image;
		this.provider = provider;
		this.likes = likes;
	}

/*
 * 
 * Rewritten by Annsofi so you can query without either type or gender, no selections..
 * 
 * 	
*/
    public static List<Accessory> getAccesories(int type, int gender) {
    	String query="";
        //building query string for searching items
    	if(gender > 0 && type > 0){
    		query = "gender = ? and type = ?";
    		 return find(query, gender, type).fetch();
    	}
    		
    	else if (gender > 0)
    	{
    		query = "gender = ?";	
    		return find(query, gender).fetch();
    	}
    			
    	else if (type > 0){
			query = "type = ?";
			return find(query, type).fetch();
    	}

        return null;
    }
}
