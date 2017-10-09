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

    private BetLimits betLimits = new BetLimits();

    public TestLimits() throws UnirestException {
    }

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return new Object[][]
                {{"sw_9s1k"}, {"sw_888t"}, {"sw_al"}, {"sw_db"}, {"sw_dd"}, {"sw_dhcf"}, {"sw_dj"}, {"sw_fbb"},
                        {"sw_fp"}, {"sw_ggdn"}, {"sw_gm"}, {"sw_gol"}, {"sw_h2h"}, {"sw_hp"}, {"sw_ld"}, {"sw_lodk"},
                        {"sw_mer"}, {"sw_mf"}, {"sw_mrmnky"}, {"sw_nyf"}, {"sw_omqjp"}, {"sw_pc"}, {"sw_qoiaf"}, {"sw_rf"},
                        {"sw_rm"}, {"sw_rs"}, {"sw_scyd"}, {"sw_sf"}, {"sw_sgcf"}, {"sw_slbs"}, {"sw_sod"}, {"sw_sq"},
                        {"sw_tc"},{"sw_wq"}, {"sw_ycs"}};
    }

    @BeforeTest
    public void printDetails() {
        System.out.println("Checking games on " + Constants.ENVIRONMENT);
    }


    @Test(dataProvider = "allGames")
    public void isCurrencyPresentOnServer(String game) throws IOException, UnirestException {
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (!betLimits.isCurrencyPresentForGameInServer(game, currency)) {
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                long maxWinFromFile = betLimits.getMaxWinFromFile(game, currency);
                long maxWinFromServer = Long.parseLong(betLimits.getLimitsFromServer(game, currency).get("winMax").toString());
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(betLimits.getDefaultBetFromServer(game, currency), betLimits.getDefaultBetFromFile(game, currency),
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(betLimits.getMaxBetFromServer(game, currency), betLimits.getMaxBetFromFile(game, currency),
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(betLimits.getMinBetFromServer(game, currency), betLimits.getMinBetFromFile(game, currency),
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    Assert.assertEquals(betLimits.getMaxTotalBetFromServer(game, currency), betLimits.getMaxTotalBetFromFile(game, currency),
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
        Set<String> allCurrencies = betLimits.getAllCurrencies(game);
        StringBuilder errors = new StringBuilder("\n");
        for (String currency : allCurrencies) {
            if (betLimits.isCurrencyPresentForGameInServer(game, currency)) {
                try {
                    JSONArray betValuesFormServer = (JSONArray) betLimits.getLimitsFromServer(game, currency).get("stakeAll");
                    JSONArray betValuesFromFile = betLimits.getBetListFromFile(game, currency);
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
