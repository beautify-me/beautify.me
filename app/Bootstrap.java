
import java.util.List;

import controllers.UsernamePasswordController;
import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.db.DB;
import play.i18n.Messages;
import play.jobs.*;
import play.test.*;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.ProviderType;
import securesocial.provider.UserId;
import securesocial.provider.UserService;
import securesocial.utils.SecureSocialPasswordHasher;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		// Check if the database is empty
		if (Accessory.count() == 0) {
			Fixtures.delete();
			Fixtures.loadModels("initial-data.yml");

			createAdminAccount("admin", "beautify", "me",
					"admin@beautify-me.com", "asdf");

			createAdminAccount("annsofi", "Annsofi", "Pettersson",
					"a@hej.se", "zxcvbn");
			
			List<Accessory> allItems = Accessory.findAll();

			for (Accessory a : allItems) {
				DB.execute("UPDATE `Accessory` SET image='acce_"
						+ a.provider.name.toLowerCase()
						+ ".png|image/png' WHERE id=" + a.getId());

			}

			List<Pic> allPics = Pic.findAll();

			for (Pic p : allPics) {

				DB.execute("UPDATE `Pic` SET image='girl.jpg|image/jpg' WHERE id="
						+ p.getId());
			}
		}
	}

	/**
	 * Creates an admin account
	 * 
	 * @param userName
	 *            The admin username
	 * @param name
	 *            The admin's first name
	 * @param lastName
	 *            The admin's last name
	 * @param email
	 *            The admin's email
	 * @param password
	 *            The password
	 */
	public void createAdminAccount(
			@Required(message = "securesocial.required") String userName,
			@Required String name,
			@Required String lastName,
			@Required @Email(message = "securesocial.invalidEmail") String email,
			@Required String password) {

		UserId id = new UserId();
		id.id = userName;
		id.provider = ProviderType.userpass;

		User user = new User();
		user.idUser = id;
		user.name = name;
		user.lastName = lastName;
		user.email = email;
		user.password = SecureSocialPasswordHasher.passwordHash(password);
		// the user is automatically active
		user.isEmailVerified = true;
		user.isAdmin = true;
		user.authMethod = AuthenticationMethod.USER_PASSWORD;

		try {
			UserService.save(user);
		} catch (Throwable e) {
			Logger.error(e, "Error while invoking UserService.save()");
		}

		// create an activation id
		UserService.activate(UserService.createActivation(user));
	}

}