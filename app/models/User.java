package models;

import java.util.List;

import play.db.jpa.Model;

public class User extends Model {

		public String name;
		public String lastName;
		public String userName;
		public String email;
		public String avatarUrl;
		public List<Accesory> myAccesories;
		//facebook
		//gplus
		//twitter
		//openid
		
}
