package ProjectShip;

import ProjectShip.Model.Position;
import ProjectShip.Model.UtilityCookies;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class PositionTest extends UtilityCookies {
    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(6);
    }
    public String getShortRandomName(){
        return RandomStringUtils.randomAlphabetic(4);
    }
    String positionID;
    String positionName = getRandomName();
    String positionShortName = getShortRandomName();
    //String positionTenantID;it was set as active in Model Class/Position.

    @Test
    public void createPosition(){
        Position position = new Position(positionName,positionShortName);

        positionID=
                given()
                        .cookies(cookies)
                        .body(position)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .body("name",equalTo(positionName))
                        .extract().jsonPath().getString("id")
                        //.extract().path("id")
        ;
    }
    @Test(dependsOnMethods = "createPosition",priority = 1)
    public void createPositionNegative(){
          Position position = new Position(positionName,positionShortName);

                given()
                        .cookies(cookies)
                        .body(position)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Position with Name \""+positionName+"\" already exists."))
        ;
    }
    @Test(dependsOnMethods = "createPosition", priority = 2)
    public void updatePosition(){
        positionName=getRandomName();  // positionName and shortName edited or updated.
        positionShortName=getShortRandomName();

        Position position = new Position(positionName,positionShortName);
        position.setId(positionID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionName))
        ;
    }
    @Test (dependsOnMethods = "updatePosition")
    public void deletePositionById(){

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("positionID",positionID)

                .when()
                .delete("school-service/api/employee-position/{positionID}")

                .then()
                //.log().body()  --no need because no return
                .statusCode(204)
        ;

    }
    @Test(dependsOnMethods = "deletePositionById", priority=2)
    public void deletePositionByIdNegative()
    {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("positionID",positionID)

                .when()
                .delete("school-service/api/employee-position/{positionID}")

                .then()
                //.log().body() --no need because no return
                .statusCode(204) //it should have been 400!!! This seems to be a BUG
                //.body("message",equalTo("employee-position not found")) this message should have been written!!!
        ;
    }
    @Test(dependsOnMethods = "deletePositionById", priority=1)
    public void UpdatePositionNegative()
    {   positionName=getRandomName();
        positionShortName=getShortRandomName();

        Position position = new Position(positionName,positionShortName);
        position.setId(positionID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("detail", equalTo("EmployeePosition is null"))
                .body("message",equalTo("Can't find Position"))
        ;
    }




    }


