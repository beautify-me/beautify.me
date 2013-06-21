

import java.util.List;

import play.*;
import play.db.DB;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Check if the database is empty
        if(Accessory.count() == 0) {
        	Fixtures.delete();
            Fixtures.loadModels("initial-data.yml");
            User u = new User("a@hej.se", "zxcvbn", "Pettersson", "Annsofi", true);
            u.save();
            
            List<Accessory> allItems = Accessory.findAll();
            for (Accessory a: allItems){
                DB.execute("UPDATE `Accessory` SET image='acce_" + a.provider.name.toLowerCase() + ".png|image/png' WHERE id=" + a.getId());
            }
        }
    }
 
}