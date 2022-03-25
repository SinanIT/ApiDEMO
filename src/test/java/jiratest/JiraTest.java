package jiratest;

import io.restassured.filter.session.SessionFilter;
import static io.restassured.RestAssured.*;

public class JiraTest {
    public static void main(String[] args) {

        baseURI= "http://localhost:8080";

        //SesssionFileter Class
        SessionFilter sessionFilter = new SessionFilter();


        //Login Scenario
        String Response = given().header("Content-Type", "application/json")
                .body("{ \"username\": \"Easy\", \"password\": \"Degerli1977\" }").log().all().filter(sessionFilter)
                .when()
                .post("/rest/auth/1/session")
                .then().log().all().extract().response().asString();


        given().pathParam("key", 10005).log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                "    \"body\": \"This is my first comment.\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}")
                .filter(sessionFilter).when()
                .post("/rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201);

    }
}
