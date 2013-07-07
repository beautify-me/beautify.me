package controllers;

import models.Accessory;
import play.db.jpa.Blob;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: D Mak
 * Date: 07.07.13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
@CRUD.For(Accessory.class)
public class Accessories extends CRUD {

    public static void accessories() {
        List<Accessory> accessories = Accessory.findAll();
        Collections.shuffle(accessories); // shuffle for dummy display to be shuffled
        render(accessories);
    }

    public static void getAccessory(Long id) {
        Accessory a = Accessory.findById(id);
    	/*Pic im = a.image;
    	response.setContentTypeIfNotSet(im.image.type());
    	renderBinary(im.image.get());*/
        Blob image = a.image;
        response.setContentTypeIfNotSet(image.type());
        renderBinary(image.get());
    }
}
