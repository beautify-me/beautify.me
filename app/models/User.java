package models;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.OAuth;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.UserId;

import java.io.Serializable;
import java.util.Date;

import models.*;
import controllers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

/**
 * A class representing a conected user and its authentication details.
 */
@Entity
public class User extends Model{
	
    /**
     * The user id
     */
    public UserId idUser;

    /**
     * The user first name.
     */
    public String name;
    
    /**
     * The user last name
     */
    public String lastName;

    /**
     * The user gender
     */
    public String gender;
    
    /**
     * The user's email
     */
    public String email;

    /**
     * A URL pointing to an avatar
     */
    public String avatarUrl;

    /**
     * The time of the last login.  This is set by the SecureSocial controller.
     */
    public Date lastAccess;

    /**
     * The method that was used to authenticate the user.
     */
    public AuthenticationMethod authMethod;

    /**
     * The service info required to make calls to the API for OAUTH1 users
     * (available when authMethod is OAUTH1 or OPENID_OAUTH_HYBRID)
     *
     * Note: this value does not need to be persisted by UserService since it is set automatically
     * in the SecureSocial Controller for each request that needs it.
     */
    @Transient
    public OAuth.ServiceInfo serviceInfo;

    /**
     * The OAuth1 token (available when authMethod is OAUTH1 or OPENID_OAUTH_HYBRID)
     */
    public String token;

    /**
     * The OAuth1 secret (available when authMethod is OAUTH1 or OPENID_OAUTH_HYBRID)
     */
    public String secret;

    /**
     * The OAuth2 access token (available when authMethod is OAUTH2)
     */
    public String accessToken;

    /**
     * The user password (available when authMethod is USER_PASSWORD)
     */
    public String password;

    /**
     * A boolean indicating if the user has validated his email adddress (available when authMethod is USER_PASSWORD)
     */
    public boolean isEmailVerified;
    
	/**
	 * A boolean indicating if the user is an administrator
	 */
	public boolean isAdmin = false;
	
	@Required
	@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable
	public List<Accessory> myAccesories = new ArrayList<Accessory>();
	
	@Required
	@OneToMany
	public List<Pic> myPics = new ArrayList<Pic>();
	

	public String toString(){
		return idUser.id;
	}
	
	public void addToMyAccesories(Accessory accessory){
		myAccesories.add(accessory);
	}
	
	public void removeFromMyAccessories(Accessory accessory){
		myAccesories.remove(accessory);
	}
	
	public void addPic(Pic pic){
		myPics.add(pic);
	}
	
	public void removePic(Pic pic){
		myPics.remove(pic);
	}
}
