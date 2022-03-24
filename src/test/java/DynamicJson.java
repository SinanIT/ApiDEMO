import files.ReusableMethods;
import files.payload;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle){
        RestAssured.baseURI= "http://216.10.245.166";

        String response = given().log().all().header("Content-Type", "application/json")
                .body(payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
       JsonPath jsonPath = ReusableMethods.rawToJson(response);
       String id = jsonPath.get("ID");
       System.out.println(id);

       //delete book

    }
    @DataProvider(name= "BooksData")
    public Object[][] getData(){
        //array collection of elements
        // muti dimensional array: collection of arrays
        return new Object[][] {{"ktb","987824"},{"ktb", "987825"},{"ktb", "987826"}};
    }

}
