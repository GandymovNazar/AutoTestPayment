import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class TestLimits {

    private ServerMethods server = new ServerMethods();
    private LocalMethods local = new LocalMethods();

    public TestLimits() throws UnirestException {
    }

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return LocalMethods.getAllGames("limits_slots");

    }

    @BeforeTest
    public void printDetails() {
        System.out.println("Checking games on " + Constants.ENVIRONMENT);
    }


    @Test(dataProvider = "allGames")
    public void checkIfCurrencyIsPresentOnServer(String game) throws IOException, UnirestException {
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

    @Test(dataProvider = "allGames", alwaysRun = true)
    public void testMaxWin(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                long maxWinFromFile = local.getMaxWinFromFile(game, currency);
                long maxWinFromServer = Long.parseLong(server.getLimitsFromServer(game, currency).get("winMax").toString());
                try {
                    Assert.assertEquals(maxWinFromServer, maxWinFromFile, "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Incorrect winMax value in the game")){
                        errors.append(String.format("Incorrect winMax value in the game %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testDefaultBet(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(server.getDefaultBetFromServer(game, currency), local.getDefaultBetFromFile(game, currency),
                            "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Incorrect default bet in the game")){
                        errors.append(String.format("Incorrect default bet in the game %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testMaxBet(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(server.getMaxBetFromServer(game, currency), local.getMaxBetFromFile(game, currency),
                            "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Incorrect max bet in the game")){
                        errors.append(String.format("Incorrect max bet in the game %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testMinBet(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(server.getMinBetFromServer(game, currency), local.getMinBetFromFile(game, currency),
                            "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Incorrect min bet in the game")){
                        errors.append(String.format("Incorrect min bet in the game %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }

        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testMaxTotalBet(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(server.getMaxTotalBetFromServer(game, currency), local.getMaxTotalBetFromFile(game, currency),
                            "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Incorrect max total bet in the game")){
                        errors.append(String.format("Incorrect max total bet in the game %s.\n", game));
                    }
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors.toString(), game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Test(dataProvider = "allGames")
    public void testBetValues(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = local.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (server.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    JSONArray betValuesFormServer = (JSONArray) server.getLimitsFromServer(game, currency).get("stakeAll");
                    JSONArray betValuesFromFile = local.getBetListFromFile(game, currency);
                    Assert.assertEquals(betValuesFormServer.toString(), betValuesFromFile.toString(),
                            "Currency: " + currency + ".");
                } catch (AssertionError e) {
                    if (!errors.toString().contains("Error in the bet values. Game: ")){
                        errors.append(String.format("Error in the bet values. Game: %s.\n", game));
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
