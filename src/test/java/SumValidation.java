import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {

    //6. Verify if Sum of all Course prices matches with Purchase Amount
    @Test
    public void sumValidation(){
        int expectedAmount =0;
        JsonPath jsonPath = new JsonPath(payload.CoursePrice());
        int count = jsonPath.getInt("courses.size()");

        for (int i =0;  i<count; i++){
            int price = jsonPath.get("courses["+ i +"].price");
            int copies = jsonPath.get("courses["+ i +"].copies");
            int totalAmount= price * copies;
            System.out.println(totalAmount);
            expectedAmount = expectedAmount+totalAmount;
        }
        System.out.println(expectedAmount);
        int actualAmount = jsonPath.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(expectedAmount, actualAmount);

    }
}
