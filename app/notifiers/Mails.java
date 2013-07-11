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
 *
 */
package notifiers;

import controllers.UsernamePasswordController;
import play.Logger;
import play.Play;
import play.db.jpa.Blob;
import play.mvc.Mailer;
import play.mvc.Router;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailAttachment;

import models.User;

/**
 * A helper class to send welcome emails to users that signed up using the
 * Username Password controller
 * 
 * @see securesocial.provider.providers.UsernamePasswordProvider
 * @see controllers.UsernamePasswordController
 */
public class Mails extends Mailer {
	private static final String SECURESOCIAL_ACTIVATION_MAILER_SUBJECT = "securesocial.mailer.subject";
	private static final String SECURESOCIAL_MAILER_FROM = "securesocial.mailer.from";
	private static final String SECURESOCIAL_USERNAME_PASSWORD_CONTROLLER_ACTIVATE = "UsernamePasswordController.activate";

	private static final String SECURESOCIAL_RESET_MAILER_SUBJECT = "securesocial.mailer.reset.subject";
	private static final String SECURESOCIAL_RESET_PASSWORD_CONTROLLER_CHANGE = "PasswordResetController.changePassword";

	private static final String UUID = "uuid";
	private static final String USERNAME = "username";
	private static final String MESSAGE = "message";

	public static void sendActivationEmail(User user, String uuid) {
		setSubject(Play.configuration
				.getProperty(SECURESOCIAL_ACTIVATION_MAILER_SUBJECT));
		setFrom(Play.configuration.getProperty(SECURESOCIAL_MAILER_FROM));
		addRecipient(user.email);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(UUID, uuid);
		String activationUrl = Router.getFullUrl(
				SECURESOCIAL_USERNAME_PASSWORD_CONTROLLER_ACTIVATE, args);
		send(user, activationUrl);
	}

	public static void sendPasswordResetEmail(User user, String uuid) {
		setSubject(Play.configuration
				.getProperty(SECURESOCIAL_RESET_MAILER_SUBJECT));
		setFrom(Play.configuration.getProperty(SECURESOCIAL_MAILER_FROM));
		addRecipient(user.email);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(USERNAME, user.idUser.id);
		args.put(UUID, uuid);
		String activationUrl = Router.getFullUrl(
				SECURESOCIAL_RESET_PASSWORD_CONTROLLER_CHANGE, args);
		send(user, activationUrl);
	}

	public static void message(String address, String message) {
		setFrom(address);
		addRecipient(Play.configuration.getProperty(SECURESOCIAL_MAILER_FROM));
		setSubject("Question");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(MESSAGE, message);
		send();
	}

	public static void messagePic(String name, String address, String message,
			Blob image) {

		try {
			setFrom(Play.configuration.getProperty(SECURESOCIAL_MAILER_FROM));
			addRecipient(address);
			EmailAttachment attachment = new EmailAttachment();
			attachment.setDescription("My new hat");
			File file = new File(image.getFile().getParentFile()
					.getAbsolutePath()
					+ File.separator + "the new hat from " + name + ".jpg");

			if (file.exists())
				file.delete();
			
			FileUtils.copyFile(image.getFile(), file);
			attachment.setPath(file.getAbsolutePath());
			addAttachment(attachment);
			setSubject("Look at what I want to get!");
			Map<String, Object> args = new HashMap<String, Object>();
			if (message.length()==0)
				message = "Lool at the cool hat that I found on beautify.me! You should check it out!";
			args.put(MESSAGE, message);
			send(message);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
		}

	}

}
