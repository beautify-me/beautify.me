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

import play.db.jpa.JPA;
import play.libs.Codec;
import securesocial.provider.UserId;
import securesocial.provider.UserServiceDelegate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import models.Accessory;
import models.User;

/**
 * The default user service provided with SecureSocial.
 * If users need to find/save users in a custom backing store they only
 * need to provide an implementation of the UserService.Service interface in their app. It will be picked up automatically.
 * <p/>
 * This class it not suitable for a production environment.  It is only meant to be used in development.  For production use
 * you need to provide your own implementation.
 *
 * @see UserServiceDelegate
 * @see securesocial.plugin.SecureSocialPlugin
 */
public class SecureSocialDBUserService implements UserServiceDelegate {


    public User find(UserId id) {
    	List users = (User.find("select u from User u")).fetch();
    	for (Object object : users) {
			User user = (User)object;
			if (user.idUser.equals(id))
				return user;
		}
        return null ;
    }

    public User find(String email) {
    	List users = ((List) User.find("email=?", "%" + email+ "%").fetch());
    	return users.size()>0 ? (User) users.get(0) : null ;
    }

    public void save(User user) {
        user.save();
    }

    public String createActivation(User user) {
        final String uuid = Codec.UUID();
        user.uuid = uuid;
        return uuid;
    }

    public boolean activate(String uuid) {
    	List users = ((List) User.find("uuid=?", "%" + uuid+ "%").fetch());
        
        boolean result = false;

        if (users.size() > 0) {
        	User user = (User) users.get(0);
            user.isEmailVerified = true;
            user.uuid = "verified";
            result = true;
            save(user);
        }
        return result;
    }

    @Override
    public String createPasswordReset(User user) {
        final String uuid = Codec.UUID();
        user.uuid = uuid; 
        user.save();
        return uuid;
    }

    @Override
	public User fetchForPasswordReset(String username, String uuid) {
        
    	User user = (User)((List)User.find("uuid=?", "%" + uuid+ "%").fetch()).get(0);
    	if (!user.isEmailVerified) {
            return null;
        }

        if (user.idUser.id.equals(username)) {
            return user;
        }

        return null;
    }

    @Override
    public void disableResetCode(String username, String uuid) {
        User socialUser = fetchForPasswordReset(username, uuid);
        if (socialUser != null) {
        	User.delete("uuid=? and isEmailVerified=?", uuid, true);
        }
    }

    public void deletePendingActivations() {
        User.delete("uuid!=?", "verified");
    }
}
