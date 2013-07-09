package controllers;

import models.Accessory;
import play.db.jpa.Blob;

import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.MatchMode;

/**
 * Created with IntelliJ IDEA.
 * User: D Mak
 * Date: 07.07.13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
@CRUD.For(Accessory.class)
public class Accessories extends CRUD {

    public static void accessories() {
        List<Accessory> accessories = Accessory.findAll();
        Collections.shuffle(accessories); // shuffle for dummy display to be shuffled
        render(accessories);
    }

    public static void getAccessory(Long id) {
        Accessory a = Accessory.findById(id);
    	/*Pic im = a.image;
    	response.setContentTypeIfNotSet(im.image.type());
    	renderBinary(im.image.get());*/
        Blob image = a.image;
        response.setContentTypeIfNotSet(image.type());
        renderBinary(image.get());
    }

    public static List<Accessory> getAccesories(int type, int gender) {

        //building query string for searching items
        String query = (gender <0 ) ? "? < 0" : "gender = ?";
        query += " and " + ((type < 0) ? "? < 0" : "type = ?");

        return Accessory.find(query, gender, type).fetch();

    }
    
    public static void getPic(long id) {
        Accessory a = Accessory.findById(id);
        Blob image = a.image;
        response.setContentTypeIfNotSet(image.type());
        renderBinary(image.get());
    }
    
    //add a accessory to userprofile
    public static void likeAccessory(Accessory a){
    	a.likes++;
    	a.save();
    	Application.loadCurrentUser().addToMyAccesories(a);
    }
    
    //remove  a accessory from userprofile
    public static void dislikeAccessory(Accessory a){
    	if (a.likes >= 1) {
    		a.likes--;
    		a.save();
    	}
    	Application.loadCurrentUser().removeFromMyAccessories(a);
    }
    
    //search function for accessory view
	public static List<Accessory> searchAccessories(String searchString){
		if (searchString == null) {
			return null;
		} else if (searchString.trim().equals("*")){
			return Accessory.findAll();
		} else {
			//Query query = JPA.em().createQuery("SELECT a FROM Accessory a " + 
			//		"WHERE a.articleName = " +  searchString.trim() + " OR a.type = " + searchString.trim());
			//List<Accessory> resultList = (List<Accessory>) query.getResultList();
			//List <Accessory> resultList =  Accessory.find("SELECT a FROM Accessory a " + 
			//"WHERE a.articleName = '" +  searchString.trim() + "' OR a.type = '" + searchString.trim() + "'").fetch();
			List <Accessory> resultList = Accessory.find("upper(articleName) like ?", "%" + searchString.toUpperCase() + "%").fetch();
			return  resultList;
			
		}
	}
	
	public static boolean doesContain(Accessory accessory){
		if(Application.loadCurrentUser().myAccesories.contains(accessory)){
	
			return true;
			
		}
		else {
		
		return false;
		}

	}
}
