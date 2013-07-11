package controllers;

import models.Accessory;
import models.User;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Header;
import play.mvc.With;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import controllers.deadbolt.Unrestricted;

/**
 * Created with IntelliJ IDEA. User: D Mak Date: 07.07.13 Time: 22:27 To change
 * this template use File | Settings | File Templates.
 */
public class AccessoryController extends Controller{

	
	private static final String ORIGINAL_URL = "originalUrl";
	
	public static void accessories() {
		List<Accessory> accessories = Accessory.findAll();
		Collections.shuffle(accessories); // shuffle for dummy display to be
											// shuffled
		render(accessories);
	}

	
	public static void getAccessory(Long id) {
		Accessory a = Accessory.findById(id);
		/*
		 * Pic im = a.image; response.setContentTypeIfNotSet(im.image.type());
		 * renderBinary(im.image.get());
		 */
		Blob image = a.image;
		response.setContentTypeIfNotSet(image.type());
		renderBinary(image.get());
	}

	
	public static void getPic(long id) {
		Accessory a = Accessory.findById(id);
		Blob image = a.image;
		response.setContentTypeIfNotSet(image.type());
		renderBinary(image.get());
	}

	// add a accessory to userprofile
	
	public static void likeAccessory(Long accessoryID) {
		User currentUser = Application.loadCurrentUser();
		Accessory currentAccessory = Accessory.findById(accessoryID);
		currentAccessory.likes++;
		currentAccessory.save();
		System.out.println("test " + currentAccessory.articleName + "\n");
		System.out.println("user " + currentUser.password + "\n");
		System.out.println("vorher: " + currentUser.myAccessories.size() + "\n");
		currentUser.addToMyAccessories(currentAccessory);
		currentUser.save();
		System.out
				.println("nachher: " + currentUser.myAccessories.size() + "\n");
		
		System.out.println(Application.loadCurrentUser().myAccessories.contains(currentAccessory));
		System.out.println(Application.loadCurrentUser().myAccessories.size());
		System.out.println(Application.loadCurrentUser().myAccessories.isEmpty());

		
		try{
		      Header refererHeader =
		Http.Request.current().headers.get("referer");
		      if(refererHeader != null){
		        List<String> refererList = refererHeader.values;
		        if(refererList != null){
		          String callingPageURL = refererList.get(0);
		          if(callingPageURL != null && callingPageURL.length()>0){
		            redirect(new URL(callingPageURL).getFile());
		          }
		        }
		      }
		    } catch (Exception e) { // MalformedURLException and any other
		      e.printStackTrace();
		    }
	}

	// remove a accessory from userprofile
	
	public static void dislikeAccessory(Long accessoryID) {
		User currentUser = Application.loadCurrentUser();
		Accessory currentAccessory = Accessory.findById(accessoryID);
		if (currentAccessory.likes >= 1) {
			currentAccessory.likes--;
			currentAccessory.save();
		}
		System.out.println("test " + currentAccessory.articleName + "\n");
		System.out.println("user " + currentUser.password + "\n");

		currentUser.removeFromMyAccessories(currentAccessory);
		currentUser.save();
		
		System.out.println(Application.loadCurrentUser().myAccessories.contains(currentAccessory));
		System.out.println(Application.loadCurrentUser().myAccessories.size());
		System.out.println(Application.loadCurrentUser().myAccessories.isEmpty());

		
		try{
		      Header refererHeader =
		Http.Request.current().headers.get("referer");
		      if(refererHeader != null){
		        List<String> refererList = refererHeader.values;
		        if(refererList != null){
		          String callingPageURL = refererList.get(0);
		          if(callingPageURL != null && callingPageURL.length()>0){
		            redirect(new URL(callingPageURL).getFile());
		          }
		        }
		      }
		    } catch (Exception e) { // MalformedURLException and any other
		      e.printStackTrace();
		    } 
	}

	// search function for accessory view
	
	public static List<Accessory> searchAccessories(String searchString) {
		if (searchString == null) {
			return null;
		} else if (searchString.trim().equals("*")) {
			return Accessory.findAll();
		} else {
			// Query query = JPA.em().createQuery("SELECT a FROM Accessory a " +
			// "WHERE a.articleName = " + searchString.trim() + " OR a.type = "
			// + searchString.trim());
			// List<Accessory> resultList = (List<Accessory>)
			// query.getResultList();
			// List <Accessory> resultList =
			// Accessory.find("SELECT a FROM Accessory a " +
			// "WHERE a.articleName = '" + searchString.trim() +
			// "' OR a.type = '" + searchString.trim() + "'").fetch();
			List<Accessory> resultList = Accessory.find(
					"upper(articleName) like ?",
					"%" + searchString.toUpperCase() + "%").fetch();
			return resultList;

		}
	}

	
	public static boolean doesContain(Accessory accessory) {
		if (Application.loadCurrentUser().myAccessories.contains(accessory)) {
			return true;
		} else {
			return false;
		}

	}
}
