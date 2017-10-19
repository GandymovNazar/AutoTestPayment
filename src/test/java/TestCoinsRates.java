import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class TestCoinsRates {

    private ServerMethods server = new ServerMethods();
    private LocalMethods local = new LocalMethods();

    public TestCoinsRates() throws UnirestException {
    }

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return new Object[][]{{"sw_fufish-jp"}};
    }

    @Test(dataProvider = "allGames")
    public void isCurrencyPresentOnServer(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (!server.isCurrencyPresentForGameInServer(game, currency)) {
                if (!errors.toString().contains("List of currencies which not present on server for game")){
                    errors.append(String.format("List of currencies which not present on server for game %s:\n", game));
                }
                errors.append(String.format("%s, ", currency));
            }
        }
        if (!Objects.equals(errors.toString(), "\n")) {
            errors = errors.delete(errors.length() - 2, errors.length()).append(".");
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testCoinsRates(String game) throws IOException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies){
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                double localCoinRate = local.getCoinRate(game, currency);
                double serverCoinRate = server.getCoinRate(game, currency);
                try {
                    Assert.assertEquals(serverCoinRate, localCoinRate, "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Error in the coins rates. Game: ")){
                        errors.append(String.format("Error in the coins rates. Game: %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    private void throwError(String message, String game, String testName) {
        if (!Objects.equals(message, "\n")) {
            throw new AssertionError(message);
        } else {
            System.out.println(game + " " + testName + " PASSED");
        }
    }

}
