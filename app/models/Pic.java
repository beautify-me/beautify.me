package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Pic extends Model {
	
	@Required
	public boolean isPrivate;
	
	@Required
	public Blob image;

	@Required
	public String name;

	public Pic(boolean isPrivate, Blob image, String name) {
		this.isPrivate = isPrivate;
		this.image = image;
		this.name = name;
	}

}
