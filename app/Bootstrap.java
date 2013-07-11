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

		Fixtures.delete();
		Fixtures.loadModels("initial-data.yml");

		createRoles();
		createUsers();
		createAccessories();
		createPics();
	}

	private void createRoles() {
		if (MyRole.getByName("admin") == null) {
			new MyRole("admin").save();
		}
		if (MyRole.getByName("user") == null) {
			new MyRole("user").save();
		}
	}

	private void createPics() {

		// Checks for pics, if emty it adds them
		if (Pic.count() == 0) {
			List<Pic> allPics = Pic.findAll();

			for (Pic p : allPics) {

				DB.execute("UPDATE `Pic` SET image='girl.jpg|image/jpg' WHERE id="
						+ p.getId());
			}
		}

	}

	private void createAccessories() {
		// Check if the database is empty
//		if (Accessory.count() == 0) {
			List<Accessory> allItems = Accessory.findAll();

			for (Accessory a : allItems) {
				DB.execute("UPDATE `Accessory` SET image='acce_"
						+ a.provider.name.toLowerCase()
						+ ".png|image/png' WHERE id=" + a.getId());

//			}
		}
	}

	private void createUsers() {

		// Checks for users, if emty it adds them
		if (User.count() == 0) {
			createAccount("admin", "beautify", "me", "admin@beautify-me.com",
					"asdf", true);
			createAccount("annsofi", "Annsofi", "Pettersson", "a@hej.se",
					"zxcvbn", true);
			createAccount("test", "test", "test", "test@test.test", "test",
					false);
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
	public void createAccount(String userName, String name, String lastName,
			String email, String password, boolean isAdmin) {

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
		user.role = isAdmin ? MyRole.getByName(MyRole.ADMIN_ROLE) : MyRole
				.getByName(MyRole.USER_ROLE);
		user.authMethod = AuthenticationMethod.USER_PASSWORD;

		try {
			UserService.save(user);
		} catch (Throwable e) {
			Logger.error(e, "Error while invoking UserService.save()");
		}

		// create an activation id
		UserService.activate(UserService.createActivation(user));
		user.save();
	}

}