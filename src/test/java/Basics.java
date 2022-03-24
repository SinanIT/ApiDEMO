import files.ReusableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class Basics {
    public static void main(String[] args) {
        //Add place==>  ->
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123")
                .header("key", "application/json")
                .body(payload.addPlace()).when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
                .extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response); // for parsing json
        String placeId = jsonPath.getString("place_id");
        System.out.println("Place ID: " + placeId);

        //Update place with new address
        String newAddress= "87 finisterra, USA";
                given().log().all().queryParam("key", "qaclick123")
                .header("key", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\""+ newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")

                .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
        System.out.println("New address is:  "+ newAddress);


        //Get place to validate if new address is present in response
        String getPlaceResponse = given().log().all().queryParams("key", "qaclick123", "place_id", placeId)
                //.queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println(getPlaceResponse);
        JsonPath js = ReusableMethods.rawToJson(getPlaceResponse);
        String actualAddress = js.getString("address");
        System.out.println(actualAddress);
        // Assertion with TestNG
        Assert.assertEquals(newAddress, actualAddress);

    }
}
