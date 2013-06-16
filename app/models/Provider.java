package models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class Provider extends Model {
	public String name;
	public String url;
	@ElementCollection
	public Map<String, Accessory> promoted;
}
