import org.junit.*;
import java.util.*;

import play.db.jpa.Blob;
import play.test.*;
import models.*;
import java.util.List;
public class BasicTest extends UnitTest {

	
    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }
   
    @Test
    public void createAccessory() {
        Provider hm = new Provider("H&M", "hm.com").save();
        Blob p = new Blob();
    	Accessory accessory = new Accessory("Round shine hat", Accessory.TYPE_HAT,Accessory.FEMALE, p, hm).save();
        
  
        
        // Test that the post has been created
        assertEquals(1, Accessory.count());
        
        // Retrieve all posts created by HM
        List<Accessory> hmAccesories = Accessory.find("byProvider", hm).fetch();
        
        // Tests
        assertEquals(1, hmAccesories.size());
        Accessory firstAcc = hmAccesories.get(0);
        assertNotNull(firstAcc);
        assertEquals(hm, firstAcc.provider);
        assertEquals(Accessory.TYPE_HAT, firstAcc.type);
        assertEquals(Accessory.FEMALE, firstAcc.gender);
    }
    
    

}
