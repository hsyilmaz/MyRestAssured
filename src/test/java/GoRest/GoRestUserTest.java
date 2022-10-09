package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;
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
        newUser=new User ();
        newUser.setName(getRandomName());
        newUser.setEmail(getRandomEmail());
        newUser.setGender("male");
        newUser.setStatus("active");

        userID=
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
                        .header("Authorization","Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596")

                        .when()
                        .get("users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
                        //.extract().jsonPath().getInt("id")
                ;

        // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız) //(v1 version boyle idi: data.[2].id)

        List <Integer> idUsers=response.path("id");
        List<Integer> idUsersJsonPath = response.jsonPath().getList("id");
            System.out.println("idUsers = " + idUsers);
            System.out.println("idUsersJson = " + idUsersJsonPath);

        int idUserThird = response.path("[2].id");
        int idUser3rd = response.jsonPath().getInt("[2].id");
            System.out.println("idUser3. = " + idUserThird);
            System.out.println("idUser3. = " + idUser3rd);

    }



    @Test       // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
    public void getUserByIDExtract() {
        User user=
        given()
                .header("Authorization", "Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596")
                .contentType(ContentType.JSON)
                .pathParam("userID", 3584)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .extract().as(User.class);
                //.extract().jsonPath().getObject("",User.class);
        System.out.println("user = " + user);
    }


    // TODO : Tüm gelen veriyi bir nesneye atınız
    @Test
    public void getUsersV1() {
        Response response =
                given()
                        .header("Authorization", "Bearer 502e4092439cc8b4dac5387966d8228a9a74a494639d2728ebd4b96c33fb4596")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();

        //        response.as();  // tüm gelen response uygun nesneler için tüm classların yapılması gerekiyor.

        List<User> dataUsers = response.jsonPath().getList("data", User.class);  // JSONPATH bir response içindeki bir parçayı
        // nesneye ödnüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

    /** Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
     gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
     Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
     imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
     diğer class lara gerek kalmadan

     path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
     jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir. */

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
