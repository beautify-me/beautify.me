package controllers;

import java.util.Collection;
import java.util.List;

import models.Accessory;
import models.Pic;
import models.User;
import notifiers.Mails;
import play.Logger;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.i18n.Messages;
import play.libs.OAuth;
import play.mvc.Controller;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.IdentityProvider;
import securesocial.provider.OAuth1Provider;
import securesocial.provider.OpenIDOAuthHybridProvider;
import securesocial.provider.ProviderRegistry;
import securesocial.provider.ProviderType;
import securesocial.provider.UserId;
import securesocial.provider.UserService;

public class Application extends Controller {

	/**
	 * -------------------------------------- Attributes for secure social
	 * ----------------------------------------
	 */

	private static final String USER_COOKIE = "securesocial_user";
	private static final String NETWORK_COOKIE = "securesocial_network";
	private static final String ORIGINAL_URL = "originalUrl";
	private static final String GET = "GET";
	private static final String ROOT = "/";
	static final String USER = "user";
	private static final String ERROR = "error";
	private static final String SECURESOCIAL_AUTH_ERROR = "securesocial.authError";
	private static final String SECURESOCIAL_LOGIN_REDIRECT = "securesocial.login.redirect";
	private static final String SECURESOCIAL_LOGOUT_REDIRECT = "securesocial.logout.redirect";
	private static final String SECURESOCIAL_SECURE_SOCIAL_LOGIN = "Application.login";

	// Strings used from multiple places
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String UUID = "uuid";
	public static final String NEW_PASSWORD = "newPassword";
	public static final String CONFIRM_PASSWORD = "confirmPassword";
	public static final String CURRENT_PASSWORD = "currentPassword";

	public static void index(int type, int gender) {
		// System.out.println("type: " + type + "\ngender: "+ gender);
		List<Accessory> accessoryList = Accessory.getAccesories(type, gender);
		if (accessoryList == null)
			accessoryList = Accessory.all().fetch();
		render(accessoryList);
	}

	public static void accessories(String searchString) {
		List<Accessory> accessories = AccessoryController
				.searchAccessories(searchString);
		// Collections.shuffle(accessories); // shuffle for dummy display to be
		// suffled
		render(accessories);
	}

	public static List<Accessory> accessoryList(int type, int gender) {
		List<Accessory> accessoryList = Accessory.getAccesories(type, gender);
		return accessoryList;
	}

	public static void getAccessory(Long id) {
		Accessory a = Accessory.findById(id);
		Blob image = a.image;
		response.setContentTypeIfNotSet(image.type());
		renderBinary(image.get());
	}

	public static void getPic(Long id) {
		Pic p = Pic.findById(id);
		Blob image = p.image;
		response.setContentTypeIfNotSet(image.type());
		renderBinary(image.get());
	}

	public static void top() {

		List<Accessory> accessories = Accessory.find(
				"select a from Accessory a Order By a.likes desc").fetch(20);
		render(accessories);
	}

	public static void mystuff() {
		User user = UserService.find(getUserId());
		List<Pic> userpics = Pic.findAll();
		List<Accessory> useraccessories = user.myAccessories;
		render(userpics, useraccessories);
	}

	public static void termsofuse() {
		render();
	}

	public static void privacy() {
		render();
	}

	public static void about() {
		render();
	}

	/**
	 * Checks if there is a user logged in
	 */
	static void checkAccess() throws Throwable {
		final UserId userId = getUserId();
		if (userId == null) {
			final String originalUrl = request.method.equals(GET) ? request.url
					: ROOT;
			flash.put(ORIGINAL_URL, originalUrl);
			// login();
		} else {
			final User user = loadCurrentUser(userId);
			if (user == null) {
				// the user had the cookies but the UserService can't find it
				// ...
				// it must have been erased, redirect to login again.
				clearUserId();
				// login();
			}
		}
	}

	/**
	 * Checks if there is a user logged in and redirects to the login page if
	 * not.
	 */
	/**
	 * @Before(unless={"login", "authenticate", "authByPost", "logout", "top",
	 *                          "index", "accessories", "privacy", "termsofuse",
	 *                          "about" })
	 **/
	static void checkAccessRedirect() throws Throwable {
		final UserId userId = getUserId();
		if (userId == null) {
			final String originalUrl = request.method.equals(GET) ? request.url
					: ROOT;
			flash.put(ORIGINAL_URL, originalUrl);
			login();
		} else {
			final User user = loadCurrentUser(userId);
			if (user == null) {
				// the user had the cookies but the UserService can't find it
				// ...
				// it must have been erased, redirect to login again.
				clearUserId();
				login();
			}
		}
	}

	public static void shareOnNetwork(Long id) {
		String pic = params.get("id");
		render(pic);
	}

	public static void sendPicByEmail(
			@Required @Email(message = "securesocial.invalidEmail") String email,
			String message,
			@Required String id) {
		Long picId = Long.valueOf(id.split("=")[1]);
		if (validation.hasErrors()) {
			tryAgainSendPicByEmail(email, message, picId);
		}

		Mails mail = new Mails();
		mail.messagePic(getCurrentUser().name, email, message,
				((Pic) Pic.findById(picId)).image);

		redirect("Application.mystuff");
	}

	private static void tryAgainSendPicByEmail(String email, String message,
			Long picId) {
		flash.put("email", email);
		flash.put("message", message);
		flash.put("id", picId);
		validation.keep();
		redirect("Application.shareOnNetwork", picId);
	}

	static User loadCurrentUser() {
		UserId id = getUserId();
		final User user = id != null ? loadCurrentUser(id) : null;
		return user;
	}

	private static User loadCurrentUser(UserId userId) {
		User user = UserService.find(userId);

		if (user != null) {
			// if the user is using OAUTH1 or OPENID HYBRID OAUTH set the
			// ServiceInfo
			// so the app using this module can access it easily to invoke the
			// APIs.
			if (user.authMethod == AuthenticationMethod.OAUTH1
					|| user.authMethod == AuthenticationMethod.OPENID_OAUTH_HYBRID) {
				final OAuth.ServiceInfo sinfo;
				IdentityProvider provider = ProviderRegistry
						.get(user.idUser.provider);
				if (user.authMethod == AuthenticationMethod.OAUTH1) {
					sinfo = ((OAuth1Provider) provider).getServiceInfo();
				} else {
					sinfo = ((OpenIDOAuthHybridProvider) provider)
							.getServiceInfo();
				}
				user.serviceInfo = sinfo;
			}
			// make the user available in templates
			renderArgs.put(USER, user);
		}
		return user;
	}

	/**
	 * Returns the current user. This method can be called from secured and
	 * non-secured controllers giving you the chance to retrieve the logged in
	 * user if there is one.
	 * 
	 * @return User the current user or null if no user is logged in.
	 */
	public static User getCurrentUser() {
		// first, try to get it from the renderArgs since it should be there on
		// secured controllers.
		User currentUser = (User) renderArgs.get(USER);

		if (currentUser == null) {
			// the call is being made from an unsecured controller
			// try to provide a current user if there is one in the session
			currentUser = loadCurrentUser();
		}
		return currentUser;
	}

	/**
	 * Returns true if there is a user logged in or false otherwise.
	 * 
	 * @return a boolean
	 */
	public static boolean isUserLoggedIn() {
		return getUserId() != null;
	}

	/*
	 * Removes the SecureSocial cookies from the session.
	 */
	private static void clearUserId() {
		session.remove(USER_COOKIE);
		session.remove(NETWORK_COOKIE);
	}

	/*
	 * Sets the SecureSocial cookies in the session.
	 */
	private static void setUserId(User user) {
		session.put(USER_COOKIE, user.idUser.id);
		session.put(NETWORK_COOKIE, user.idUser.provider.toString());
	}

	/*
	 * Creates a UserId object from the values stored in the session.
	 * 
	 * @see UserId
	 * 
	 * @returns UserId the user id
	 */
	private static UserId getUserId() {
		final String userId = session.get(USER_COOKIE);
		final String networkId = session.get(NETWORK_COOKIE);
		UserId id = null;

		if (userId != null && networkId != null) {
			id = new UserId();
			id.id = userId;
			id.provider = ProviderType.valueOf(networkId);
			;
		}
		return id;
	}

	/**
	 * The action for the login page.
	 */
	public static void login() {
		final Collection providers = ProviderRegistry.all();
		flash.keep(ORIGINAL_URL);
		boolean userPassEnabled = ProviderRegistry.get(ProviderType.userpass) != null;
		render(providers, userPassEnabled);

	}

	/**
	 * The logout action.
	 */
	public static void logout() {
		clearUserId();
		final String redirectTo = Play.configuration.getProperty(
				SECURESOCIAL_LOGOUT_REDIRECT, SECURESOCIAL_SECURE_SOCIAL_LOGIN);
		redirect(redirectTo);
	}

	/**
	 * This is the entry point for all authentication requests from the login
	 * page. The type is used to invoke the right provider.
	 * 
	 * @param type
	 *            The provider type as selected by the user in the login page
	 * @see ProviderType
	 * @see IdentityProvider
	 */
	public static void authenticate(ProviderType type) {
		doAuthenticate(type);
	}

	public static void authByPost(ProviderType type) {
		doAuthenticate(type);
	}

	private static void doAuthenticate(ProviderType type) {
		if (type == null) {
			Logger.error("Provider type was missing in request");
			// just throw a 404 error
			notFound();
		}
		flash.keep(ORIGINAL_URL);

		IdentityProvider provider = ProviderRegistry.get(type);
		String originalUrl = null;

		try {
			User user = provider.authenticate();
			setUserId(user);
			originalUrl = flash.get(ORIGINAL_URL);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e, "Error authenticating user");
			if (flash.get(ERROR) == null) {
				flash.error(Messages.get(SECURESOCIAL_AUTH_ERROR));
			}
			flash.keep(ORIGINAL_URL);
			login();
		}
		final String redirectTo = Play.configuration.getProperty(
				SECURESOCIAL_LOGIN_REDIRECT, ROOT);
		redirect(originalUrl != null ? originalUrl : redirectTo);
	}

	/**
	 * A helper class to integrate SecureSocial with the Deadbolt module.
	 * <p/>
	 * Basically the integration is done by calling
	 * SecureSocial.Deadbolt.beforeRoleCheck() within the
	 * DeadboltHandler.beforeRoleCheck implementation.
	 * <p/>
	 * Eg:
	 * <p/>
	 * public class MyDeadboltHandler extends Controller implements
	 * DeadboltHandler { try { SecureSocial.DeadboltHelper.beforeRoleCheck(); }
	 * catch ( Throwable t) { // handle the exception in an application specific
	 * way } }
	 */
	public static class DeadboltHelper {
		public static void beforeRoleCheck() throws Throwable {
			checkAccessRedirect();
		}
	}

	public static void me() {
		render("@user");
	}

	public static void contact(String address, String message) {
		Mails mail = new Mails();
		mail.message(address, message);
		render();
	}

	public static void addFavorite(Long accessoryID) {
		User currentUser = getCurrentUser();
		Accessory currentAccessory = Accessory.findById(accessoryID);
		currentUser.addToMyAccessories(currentAccessory);
		currentUser.save();
	}

}
