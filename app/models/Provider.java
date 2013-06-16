package models;

import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class Provider extends Model {
	public String name;
	public String url;
	public List<Accessory> promoted;
}
