import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonPars {
    public static void main(String[] args) {

        JsonPath jsonPath = new JsonPath(payload.CoursePrice());

        //1. Print No of courses returned by API
        int count = jsonPath.getInt("courses.size()");
        System.out.println(count);

        //2.Print Purchase Amount

        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println(purchaseAmount);

       // 3. Print Title of the first course
        String course = jsonPath.get("courses[0].title");
        System.out.println(course);

       // 4. Print All course titles and their respective Prices
        for(int i= 0; i<count; i++){
            String courseTitles = jsonPath.get("courses["+ i + "].title");
            int prices = jsonPath.getInt("courses["+ i +"].price");
            System.out.println("The " + courseTitles + " course price is " + prices);
        }

       // 5. Print no of copies sold by RPA Course
        for(int i= 0; i<count; i++){
            String courseTitles = jsonPath.get("courses["+ i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")){
                int copies = jsonPath.get("courses["+ i + "].copies");
                System.out.println("RPA sold cpoy is " + copies);
                break;
            }
        }
    }


}
