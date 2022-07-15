package sZZZ;

import hooks.AllureRestAssuredSubscription;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import s1111.model.FindUserResponse;
import sZZZ.model.Transaction;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static utils.CONSTANTS.*;

@DisplayName("История операций. ")
@Tag("history")
@ExtendWith(AllureRestAssuredSubscription.class)
public class HistoryTest extends TestBase {



    @Test
    @Tag("HistoryTest")
    @DisplayName("Проверка пагинации ")
    public void history_pagination() {

        //получить пользователя по телефону
        FindUserResponse user = steps1731.getFindUserResponse(USER1_PHONE, USER1_DEVICE_ID);

        //авторизация
        String sessionToken = steps1731.getSessionToken(user);

        //запрос истории операций
        OffsetDateTime currentDate = OffsetDateTime.now();
        String dateFrom = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(390));
        String dateTo = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(1));

        //поиск всех операций в источнике
        Response homer = stepsZZZ.getHistoryDirect(user, dateFrom, dateTo);
        List<String> all = homer.jsonPath().getList("operations.eventTime");


        List<Transaction> history = null;
        history = stepsZZZ.getCardHistory(user, sessionToken, dateFrom, dateTo, 0, 10);
        Assertions.assertEquals(10, history.size(), "Число операций не верное");
        checkOrderInHomerList(all, history, 0, 10);


        history = stepsZZZ.getCardHistory(user, sessionToken, dateFrom, dateTo, 5, 10);
        Assertions.assertEquals(10, history.size(), "Число операций не верное");
        checkOrderInHomerList(all, history, 5, 10);

        history = stepsZZZ.getCardHistory(user, sessionToken, dateFrom, dateTo, 4, 20);
        Assertions.assertEquals(20, history.size(), "Число операций не верное");
        checkOrderInHomerList(all, history, 4, 20);

        history = stepsZZZ.getCardHistory(user, sessionToken, dateFrom, dateTo, 0, 200);
        Assertions.assertEquals(100, history.size(), "Число операций не верное или недостаточное");
        checkOrderInHomerList(all, history, 0, 100);

    }

    @Step("Сравнение списка с оригиналом при отступе {offset}")
    void checkOrderInHomerList(List<String> allHomer, List<Transaction> history, int offset, int limit) {
        int i = 0;
        for (Transaction t : history) {
            Assertions.assertEquals(t.getTransactionDateTime().toString(), allHomer.get(i + offset) + "Z", "Порядок в выборке не совпадает с источникрм при offset = " + offset + "  и limit = " + limit);
            i++;
        }
    }


    @Test
    @Tag("HistoryTest")
    @DisplayName("Запрос ИО по другой сущности")
    public void history_otherCard() {

        //получить пользователя по телефону
        FindUserResponse user = steps1731.getFindUserResponse(USER1_PHONE, USER1_DEVICE_ID);

        //получить пользователя по телефону
        FindUserResponse user2 = steps1731.getFindUserResponse(USER3_PHONE, USER3_DEVICE_ID);

        //авторизация
        String sessionToken = steps1731.getSessionToken(user);

        //запрос истории операций
        OffsetDateTime currentDate = OffsetDateTime.now();
        String dateFrom = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(60));
        String dateTo = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(1));

        Response rs = stepsZZZ.getCardHistory_response(user2, sessionToken, dateFrom, dateTo, 0, 100);
        Assertions.assertEquals(rs.then().extract().statusCode(), 404, "Запрос по карте не прошел");


    }

    @Test
    @Tag("HistoryTest")
    @DisplayName("Запрос Истории операций по заблокированной карте")
    public void history_blockedCard() {

        //получить пользователя по телефону
        FindUserResponse user = steps1731.getFindUserResponse(USER3_PHONE, USER3_DEVICE_ID);

        //авторизация
        String sessionToken = steps1731.getSessionToken(user);

        //запрос истории операций
        OffsetDateTime currentDate = OffsetDateTime.now();
        String dateFrom = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(360));
        String dateTo = DateTimeFormatter.ofPattern(HISTORY_DATETIME_PATTERN, Locale.US).format(currentDate.minusDays(1));


        List<Transaction> historyBefore = stepsZZZ.getCardHistory(user, sessionToken, dateFrom, dateTo, 0, 100);
        Assertions.assertNotEquals(historyBefore.size(), 0, "Операций по заблокированной карте на найдено. Карта не подходит или операции не пришли");
    }



}
