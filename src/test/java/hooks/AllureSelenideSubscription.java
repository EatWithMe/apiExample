package hooks;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;

public class AllureSelenideSubscription {

    /**
     * подписка на селенидовские события,
     * что бы в аллуре появлялись все действия в виде технических шагов
     */
    @BeforeAll
    public static void allureSubscribeThreadParalled() {
        String listenerName = "AllureSelenide";

        // todo ПЕРЕДЕЛАТЬ весь метод для текущего фрейм форка
        if (!(SelenideLogger.hasListener(listenerName)))
            SelenideLogger.addListener(listenerName,
                    new AllureSelenide().
                            screenshots(true).
                            savePageSource(false)
            );
    }
}
