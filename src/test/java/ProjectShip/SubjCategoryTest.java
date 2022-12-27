package ProjectShip;

import ProjectShip.Model.SbjCategory;
import ProjectShip.Model.UtilityCookies;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class SubjCategoryTest extends UtilityCookies {

    String sbjCatId;
    String sbjCatName = getRandomName();
    String sbjCatCode = getRandomCode();
    //Boolean active=true; it was set as active in Model/SbjCategory class.

    @Test
    public void createCategory(){
        SbjCategory category = new SbjCategory(sbjCatName, sbjCatCode);
        sbjCatId =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(category)

                        .when()
                        .post("school-service/api/subject-categories")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
                        //.extract().path("id")
                ;
    }
    @Test(dependsOnMethods = "createCategory")
    public void createCategoryNegative(){
        SbjCategory category = new SbjCategory(sbjCatName, sbjCatCode);
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(category)

                        .when()
                        .post("school-service/api/subject-categories")
                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Subject Category with Name \""+ sbjCatName +"\" already exists."))
                ;
    }
    @Test(dependsOnMethods = "createCategory")
    public void updateCategory(){
        sbjCatName = getRandomName();
        sbjCatCode = getRandomCode();

        SbjCategory category = new SbjCategory(sbjCatName,sbjCatCode);
        category.setId(sbjCatId);

                         given()
                                 .cookies(cookies)
                                 .contentType(ContentType.JSON)
                                 .body(category)

                                 .when()
                                 .put("school-service/api/subject-categories")

                                 .then()
                                 .log().body()
                                 .statusCode(200)
                                 .body("name",equalTo(sbjCatName))
                         ;
    }
    @Test(dependsOnMethods = "updateCategory" )
    public void deleteCategoryById(){

                        given()
                                .cookies(cookies)
                                .contentType(ContentType.JSON)
                                .pathParam("sbjCatId",sbjCatId)

                                .when()
                                .delete("school-service/api/subject-categories/{sbjCatId}")

                                .then()
                                .log().body()
                                .statusCode(200)
                        ;
    }
    @Test(dependsOnMethods = "deleteCategoryById",priority=1)
    public void updateCategoryNegative(){
        sbjCatName = getRandomName();
        sbjCatCode = getRandomCode();

        SbjCategory category = new SbjCategory(sbjCatName,sbjCatCode);
        category.setId(sbjCatId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(category)

                .when()
                .put("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Can't find Subject Category"))
                .body("detail",equalTo("SubjectCategory is null"))
        ;
    }
    @Test(dependsOnMethods = "deleteCategoryById", priority=2)
    public void deleteCategoryByIdNegative(){

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("sbjCatId",sbjCatId)

                .when()
                .delete("school-service/api/subject-categories/{sbjCatId}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("SubjectCategory not  found"))
        ;
    }

}
