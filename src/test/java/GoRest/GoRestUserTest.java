package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTest {

    @BeforeClass
    void Setup() {
        // RestAssured kendi statik degiskeni tanimli deger ataniyor

        baseURI = "https://gorest.co.in/public/v2";
    }

    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomEmail()
    {
        return RandomStringUtils.randomAlphabetic(8)+"@gmail.com";
    }

    int userID=0;
    User newUser;


    @Test
    public void createUserObject()
    {
        newUser = new User();                  //// Class Yontemi
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userID=
        given()
                .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                .contentType(ContentType.JSON)
                .body(newUser)
                //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                .log().body()
                .when()
                .post("users")

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                //.extract().path("id")
                .extract().jsonPath().getInt("id")
        ;
        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
        System.out.println("userID = " + userID);
    }

    @Test(dependsOnMethods = "createUserObject", priority=1)
    public void updateUserObject()
    {
//       Map<String,String> updateUser = new HashMap<>();   ///MAP Yontemi calismadi!
//        updateUser.put("name", "Huseyin YILMAZ");

        newUser.setName("Huseyin YILMAZ");
                given()
                        .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().body()
                        .pathParam("userID", userID)

                        .when()
                        .put("users/{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name", equalTo("Huseyin YILMAZ"))
                ;
    }

    @Test(dependsOnMethods = "createUserObject", priority=2)
    public void getUserById()
    {
        given()
                .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "createUserObject", priority = 3)
    public void deleteUserById()
    {

        given()
                .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }
    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative()
    {

        given()
                .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;
    }
    @Test
    public void getUsers()
    {
        Response response=
                given()
                        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")

                        .when()
                        .get("users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
                ;

        // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
        // TODO : Tüm gelen veriyi bir nesneye atınız
        // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
    }













    @Test(enabled = false)
    public void createUserObjectII()
    {
       int userID=
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token, gidecek body, parametreleri
                        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+ getRandomEmail()+"\", \"status\":\"active\"}")

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

        System.out.println("userID = " + userID);
    }


    @Test(enabled=false)
    public void createUserObjectIII()
    {
        Map<String,String> newUser = new HashMap<>();   ///MAP Yontemi
        newUser.put("name",getRandomName());
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");

        int userID=
                given()
                        .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596" )
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().body()

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

        System.out.println("userID = " + userID);
    }


}
class User{
    private String name;
    private String gender;
    private String email;
    private String status;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

