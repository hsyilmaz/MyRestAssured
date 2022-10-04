import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.equalTo;

public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazirlik islemlerini yapicaz (token, send body, parametreler)
                .when()
                // linki ve methodu veriyoruz

                .then()
        // assertion ve verileri ele alma extract

        ;
    }

    @Test
    public void statusCodeTest() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() butun respons u gosterir.
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // log.all() butun respons u gosterir.
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void checkStateInResponse() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")) // body country == United States mi?
                .statusCode(200)
        ;
    }
// body.country --> body("country", equalTo("......."))
// body.'post code' --> body("post code", equalTo("......"))
// body.'country abbreviation' --> body("country abbreviation", equalTo("......"))
// body.places[0].'place name' --> body("places[0].'place name'", equalTo("......"))
// body.places[0].state ---> body("places[0].state", equalTo("....."))

    @Test
    public void bodyJsonPathTest1() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .body("places[0].state", equalTo("California")) // body country == United States mi?
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest2() {
        given()

                .when()
                .get("https://api.zippopotam.us/tr/01000")

                .then()
                //.log().body()
                .body("places.'place name'", hasItem("Çaputçu Köyü"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest4() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))  // verilen path deki Listin size kontrolu: places dizisi eleman sayisi 1 mi?
                .statusCode(200)
        ;
    }

    @Test
    public void CombiningTest5() {
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))// verilen path deki Listin size kontrolu: places dizisi eleman sayisi 1 mi?
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void PathParamTest1() {
        given()
                .pathParam("Country", "us")
                .pathParam("ZipCode", 90210)
                .log().uri()

                .when()
                .get("https://api.zippopotam.us/{Country}/{ZipCode}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void PathParamTest2() {
        //90210 dan 90250 kadar test sonuclarinda placas size in 1 geldigini test ediniz.

        for (int i = 90210; i < 90214; i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipCode", i)
                    .log().uri()

                    .when()
                    .get("https://api.zippopotam.us/{Country}/{ZipCode}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200)
            ;
        }
    }

    @Test
    public void PathParamTest3() {
        given()

                .param("page", 1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1))
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTest4() {


        for (int pageNo = 1; pageNo <= 10; pageNo++) {
            given()


                    .param("page", pageNo)
                    .log().uri() // request linki

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(pageNo))
                    .statusCode(200)
            ;
        }

    }

    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void Setup() {

        // RestAssured kendi statik değişkeni tanımlı değer atanıyor.
        baseURI = "https://gorest.co.in/public/v1";

        requestSpecs = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecs = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void requestResponseSpecification() {
        //https://gorest.co.in/public/v1/users?page=1

        given()
                .param("page", 1)
                .spec(requestSpecs)

                .when()
                .get("/users")  // url nin başında http yoksa baseUri deki değer otomatik geliyor.

                .then()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpecs)
        ;
    }

    // Json extract
    @Test
    public void extractingJsonPath() {

        String placeName =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("places[0].'place name'")
                // extract metodu ile given ile başlayan satır, bir değer döndürür hale geldi, en sonda extract olmalı
                ;

        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt() {

        int limit =

                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit, 10, "test result");
    }

    @Test
    public void extractingJsonPathInt2() {

        int id =

                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("data[2].id");
        System.out.println("id = " + id);
    }

    @Test
    public void extractingJsonPathIntList() {

        List<Integer> ids =

                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("data.id");
        System.out.println("ids = " + ids);
        Assert.assertTrue(ids.contains(3045));
    }

    @Test
    public void extractingJsonPathNameList() {

        List<String> names =

                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name");
        System.out.println("names = " + names);
        Assert.assertTrue(names.contains("Dhana Adiga"));
    }

    @Test
    public void extractingJsonPathResponseAll() {

        Response response =

                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response();  // butun body alindi

        List<Integer> ids = response.path("data.id");
        List<String> names = response.path("data.name");
        int limit = response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("names = " + names);
        System.out.println("ids = " + ids);
    }

    @Test
    public void extractingJsonPOJO() {
        Location location =
                given()

                        .when()
                        .get("https://api.zippopotam.us/us/90210")

                        .then()
                        .extract().as(Location.class);

        System.out.println("location = " + location);
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());
    }
}


//            "post code": "90210",
//            "country": "United States",
//            "country abbreviation": "US",
//
//            "places": [
//            {
//            "place name": "Beverly Hills",
//            "longitude": "-118.4065",
//            "state": "California",
//            "state abbreviation": "CA",
//            "latitude": "34.0901"
//            }
//            ]
//
//class Location{
//    String postcode;
//    String country;
//    String countryabbreviation;
//    ArrayList<Place> places
//}
//
//class Place{
//    String placename;
//    String longitude;
//    String state;
//    String stateabbreviation
//    String latitude;
//
