/**
* Copyright 2011 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/
package securesocial.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.User;

/**
 * Base class for all itendity providers
 */
public abstract class IdentityProvider {
    /**
     * The provider ID.
     */
    public ProviderType type;

    /**
     * The authentication method used by this provider
     */
    public AuthenticationMethod authMethod;

    private static final String TYPE = "type";
    private static final String SECURESOCIAL_SECURE_SOCIAL_AUTHENTICATE = "Application.authenticate";
    private static final String SECURESOCIAL = "securesocial.";
    private static final String DOT = ".";

    /**
     * Creates a new IdentityProvider
     *
     * @param type The type for this provider
     * @param authMethod The authentication method used by this provider
     */
    protected IdentityProvider(ProviderType type, AuthenticationMethod authMethod) {
        this.type = type;
        this.authMethod = authMethod;
    }
    
    @Override
    public String toString() {
        return type.toString();
    }

    /**
     * The authentication flow starts here.  This method is called from the
     * SecureSocial controller
     *
     * @return A SocialUser if the user was authenticated properly
     */
    public User authenticate() {
        // authenticate against the 3rd party service (facebook, twitter, etc)
        Map<String, Object> authContext = new HashMap<String, Object>();
        User user = doAuth(authContext);

        // if user authenticated correctly, retrieve some profile information
        fillProfile(user, authContext);

        // save the user
        user.lastAccess = new Date();
        UserService.save(user);
        
        // we're done.
        return user;
    }

    /**
     * A helper method to create a user with some authentication details.
     *
     * @return A SocialUser object
     */
    protected User createUser() {
        User user = new User();
        user.idUser = new UserId();
        user.idUser.provider = type;
        user.authMethod = authMethod;                
        return user;
    }

    /**
     * Returns the full url for the authenticate action.
     *
     * @return A url
     */
    public String getFullUrl() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(TYPE, type);
        return play.mvc.Router.getFullUrl(SECURESOCIAL_SECURE_SOCIAL_AUTHENTICATE,  args);
    }

    /**
     * Subclasses must implement the authentication logic in this method
     *
     * @param authContext This map can be used to store information that fillProfile will need to complete the operation
     * @return SocialUser the authenticated user
     */
    protected abstract User doAuth(Map<String, Object> authContext);

    /**
     * Once the user is authenticated this method is called to retrieve profile information from the provider.
     *
     * @param user A SocialUser
     * @param authContext This map can contain information collected during the doAuth call.
     */
    protected abstract void fillProfile(User user, Map<String, Object> authContext);

    /**
     * A helper method to return the keys for the properties required by the provider.
     *
     * @param type The provider type
     * @return A String
     */
    public static String getPropertiesKey(ProviderType type) {
        return new StringBuilder(SECURESOCIAL).append(type).append(DOT).toString();
    }    
}
