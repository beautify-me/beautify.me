package controllers;

import play.mvc.With;
import controllers.CRUD;
import controllers.CRUD.For;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import models.Pic;

/**
 * Created with IntelliJ IDEA.
 * User: D Mak
 * Date: 07.07.13
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
@CRUD.For(Pic.class)
@With(Deadbolt.class)
@Restrict("admin")
public class Pics extends CRUD {
	
	
	
}
