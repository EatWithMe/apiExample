package s1111.steps;

import s1111.model.FindUserResponse;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static utils.CONSTANTS.BASE_URL_1;

public class testSteps {

    public static FindUserResponse getFindUserResponse(String phone, String deviceId) {
        return given().baseUri(BASE_URL_1)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"phone\": \"" + phone + "\",\n" +
                        "  \"deviceId\": \"" + deviceId + "\",\n" +
                        "}")
                .log().all()
                .post("/by-phone")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .extract().response().as(FindUserResponse.class);
    }

    public String getSessionToken(FindUserResponse user) {
        return given().baseUri(BASE_URL_1).
                header("Authorization", UUID.randomUUID().toString())
                .header("X-Request-ID", UUID.randomUUID().toString())
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"userUuid\": \"" + user.getUser().getUuid() + "\",\n" +
                        "  \"deviceId\": \"" + user.getDevice().getDeviceId() + "\"\n" +
                        "}")
                .log().all()
                .post("/signin")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().jsonPath().getString("token");


}
