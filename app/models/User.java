package models;

import play.db.jpa.Model;
import play.libs.OAuth;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.UserId;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

/**
 * A class representing a conected user and its authentication details.
 */
public class User extends Model{
    /**
     * The user id
     */
    public UserId id;

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
}
