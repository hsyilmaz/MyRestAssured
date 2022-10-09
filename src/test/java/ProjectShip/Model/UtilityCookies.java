package ProjectShip.Model;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class UtilityCookies {
    protected Cookies cookies;

    @BeforeClass
    public void loginCampus()  //{"username": "richfield.edu","password": "Richfield2020!","rememberMe": "true"}
    {
        baseURI = "https://demo.mersys.io/";
        Map<String,String> credentials = new HashMap<>();
        credentials.put("username","richfield.edu");
        credentials.put("password","Richfield2020!");
        credentials.put("rememberMe","true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credentials)

                        .when()
                        .post("auth/login")

                        .then()
                        .log().cookies() //log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }
}
