package controllers;

import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import models.MyRole;
import models.MyRoleHolder;
import models.User;
import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample handler to show SecureSocial and Deadbolt integration
 */
public class MyDeadboltHandler extends Controller implements DeadboltHandler {

    public void beforeRoleCheck() {
        try {
            Application.DeadboltHelper.beforeRoleCheck();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
    }

    public RoleHolder getRoleHolder() {
        // get the current user
        User user = Application.getCurrentUser();

        // create a role based on the network the user belongs to.
		List<Role> roles = new ArrayList();
		roles.add(user.role);

        // we're done
        return new MyRoleHolder(roles);
    }

    public void onAccessFailure(String controllerClassName) {
        forbidden();
    }

    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor() {
        return null;
    }

    public RestrictedResourcesHandler getRestrictedResourcesHandler() {
        return null;  
    }
}
