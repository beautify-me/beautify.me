package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
public class Pic extends Model {
	@Required
	public boolean isPrivate;
}
