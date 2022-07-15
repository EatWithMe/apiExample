package sZZZ.steps;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.response.Response;
import s1111.model.FindUserResponse;
import sZZZ.DefaultApi;
import sZZZ.model.*;
import sZZZ.pack.ApiClient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static sZZZ.pack.GsonObjectMapper.gson;
import static utils.CONSTANTS.*;

public class TestSteps {

    private DefaultApi api;

    public TestSteps() {
        api = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder()
                        .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                        .addFilter(new ErrorLoggingFilter())
                        .setBaseUri(BASE_URL_2)
                        .setUrlEncodingEnabled(false)
                        .setRelaxedHTTPSValidation())).defaultApi();
    }

    public List<Transaction> getCardHistory(FindUserResponse user, String sessionToken, String dateFrom, String dateTo, Integer offset, Integer limit) {
        //2022-02-02T15:30:00.383Z
        //2022-04-22T04:40:08.259561
        Response rs = getCardHistory_response(user, sessionToken, dateFrom, dateTo, offset, limit);

        GetCardTransactionsResponse ctr = rs.then().statusCode(200).extract().as(GetCardTransactionsResponse.class);
        return ctr.getData().getTransactions();
    }

    @Step("Получить историю операций")
    public Response getCardHistory_response(FindUserResponse user, String sessionToken, String dateFrom, String dateTo, Integer offset, Integer limit) {
        //2022-02-02T15:30:00.383Z
        //2022-04-22T04:40:08.259561
        Response rs = api.transactionsCardsCardIdGet()
                .cardIdPath(user.getUser().getCardPublicId())
                .xCallIDHeader(UUID.randomUUID().toString())
                .xClientAuthenticationHeader(sessionToken)
                .xPartnerIDHeader("YYYY")
                .initiatorHostHeader("YYYY")
                .initiatorServiceHeader("YYYY")
                .dateFromQuery(dateFrom)
                .dateToQuery(dateTo)
                .limitQuery(limit)
                .offsetQuery(offset)
                .execute(r -> r.prettyPeek());

        return rs;
    }





    @Step("Получить данные Напрямую")
    public Response getHistoryDirect(FindUserResponse user, String dateFrom, String dateTo) {

        Response rs = given()
                .baseUri(BASE_URL_P)
                .relaxedHTTPSValidation()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .param("card", user.getUser().getCardPublicId())
                .param("dateFrom", dateFrom)
                .param("dateTo", dateTo)
                .param("count", 100)
                .log().all()
                .get("history/byCard")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        return rs;
    }

}
