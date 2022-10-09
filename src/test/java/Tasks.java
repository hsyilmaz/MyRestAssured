import POJO.Todos;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Tasks {



    /** Task 1: create a request to <a href="https://jsonplaceholder.typicode.com/todos/2"/>
     expect status 200  Converting Into POJO */
    @Test
    public void extractingJsonTask1() { //look at POJO package Todos class
        Todos todo =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .extract().as(Todos.class)
                ;
        System.out.println("todo = " + todo);
    }


    /**Task 2: create a request, to <a href="https://httpstat.us/203"/>
     expect status 203, expect content type TEXT */
    @Test
    public void task2() {

        given()

                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }

    /** Task 3: create a request to <a href="https://jsonplaceholder.typicode.com/todos/2"/>
     expect status 200  expect content type JSON
     expect title in response body to be "quis ut nam facilis et officia qui" */
    @Test
    public void task3() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .body("title",equalTo("quis ut nam facilis et officia qui"))

        ;
    }
    /** Task 4: create a request to <a href="https://jsonplaceholder.typicode.com/todos"/>
                expect status 200,  expect content type JSON, expect third item have:
                title = "fugiat veniam minus", userId = 1  */
    @Test
    public void task4() {


        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/3")
                .then()
                .statusCode(200)
                .log().body()
                .contentType(ContentType.JSON)
                .body("title",equalTo("fugiat veniam minus"))
        ;
    }

}
