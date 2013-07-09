import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;


public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    @Test
    public void testTheHomePage() {
        Response response = GET("/");
        assertStatus(200, response);
    }
    @Test
    public void testAccesories() {
        Response response = GET("/accessories");
        assertStatus(200, response);
    }
    @Test
    public void testSignup() {
        Response response = GET("/signup");
        assertStatus(200, response);
    }
    
    
}