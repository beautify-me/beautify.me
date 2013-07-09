package controllers;

import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import models.User;

/**
 * Created with IntelliJ IDEA.
 * User: D Mak
 * Date: 07.07.13
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
@CRUD.For(User.class)
@With(Deadbolt.class)
@Restrictions({@Restrict("admin")})
public class Users extends CRUD {
}
