package jiratest;

import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTest {
    public static void main(String[] args) {

        baseURI = "http://localhost:8080";

        //SesssionFileter Class
        SessionFilter sessionFilter = new SessionFilter();


        //Login Scenario
        String Response = given().header("Content-Type", "application/json")
                .body("{ \"username\": \"Easy\", \"password\": \"Degerli1977\" }").log().all().filter(sessionFilter)
                .when()
                .post("/rest/auth/1/session")
                .then().log().all().extract().response().asString();

        //add comment
        String expectedMessage = " Last comment for this bug";
        String addCommentResponse = given().pathParam("key", 10005).log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"" + expectedMessage + "\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                .filter(sessionFilter).when()
                .post("/rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath jsonPath = new JsonPath(addCommentResponse);
        String commentId = jsonPath.getString("id");

        //add attachment
        given().header("X-Atlassian-Token", "no-check").log().all().filter(sessionFilter).pathParam("key", 10005)
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", new File("jira.txt"))
                .when()
                .post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

        //Get the issue
        String response = given().filter(sessionFilter).pathParam("key", 10005)
                .queryParam("fields", "comment")
                .log().all().when().get("rest/api/2/issue/{key}")
                .then().log().all().extract().response().asString();
        System.out.println(response);


        JsonPath js = new JsonPath(response);
        int commentCount = js.getInt("fields.comment.comments.size()");
        for (int i = 0; i < commentCount; i++) {
            String commentIdIsuue = js.get("fields.comment.comments[" + i + "].id").toString();
            if (commentIdIsuue.equalsIgnoreCase(commentId)) {
                String message = js.get("fields.comment.comments[" + i + "].body").toString();
                System.out.println(message);
                Assert.assertEquals(message, expectedMessage);
            }
        }
    }
}
