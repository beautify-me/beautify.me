package controllers;

import models.Accessory;
import models.User;
import play.db.jpa.Blob;
import play.mvc.With;

import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import controllers.CRUD;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import controllers.deadbolt.Unrestricted;

/**
 * Created with IntelliJ IDEA. User: D Mak Date: 07.07.13 Time: 22:27 To change
 * this template use File | Settings | File Templates.
 */
@CRUD.For(Accessory.class)
@With(Deadbolt.class)
@Restrict("admin")
public class Accessories extends CRUD {
}
