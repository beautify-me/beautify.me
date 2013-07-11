package models;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.OAuth;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.UserId;

import java.io.Serializable;
import java.util.Date;

import models.*;
import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import controllers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

/**
 * A class representing a conected user and its authentication details.
 */
@Entity
public class User extends Model  implements RoleHolder {
	
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
     * The role that the user has. Can be admin or user
     */
    @Required
    @ManyToOne
    public MyRole role;
	
	@Required
	@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable
	public List<Accessory> myAccessories = new ArrayList<Accessory>();
	
	@Required
	@OneToMany
	public List<Pic> myPics = new ArrayList<Pic>();

	/**
	 * Activation uuid
	 */
	public String uuid;
	

	public String toString(){
		return idUser.id;
	}
	
	public void addToMyAccessories(Accessory accessory){
		myAccessories.add(accessory);
	}
	
	public void removeFromMyAccessories(Accessory accessory){
		myAccessories.remove(accessory);
	}
	
	public void addPic(Pic pic){
		myPics.add(pic);
	}
	
	public void removePic(Pic pic){
		myPics.remove(pic);
	}

	@Override
	public List<Role> getRoles() {
		return Arrays.asList((Role)role);
	}

}
