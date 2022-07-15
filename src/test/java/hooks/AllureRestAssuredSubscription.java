package hooks;


import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


public class AllureRestAssuredSubscription implements BeforeAllCallback {

    private static boolean isSubscripted = false;

    @Override
    public void beforeAll(ExtensionContext var1) {
        if (!isSubscripted) {
            isSubscripted = true;
            RestAssured.filters(new AllureResponseRename());
        }

    }
}
