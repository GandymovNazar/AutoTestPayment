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
                        {"sw_tc"}, {"sw_ycs"}};
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
                errors.append(currency + " currency is not present on server for the game " + game + "\n");
            }
        }
        throwError(errors, game, new Object() {
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
                    Assert.assertEquals(maxWinFromServer, maxWinFromFile, "Incorrect winMax value in the game " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors, game, new Object() {
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
                            "Incorrect default bet in the game " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors, game, new Object() {
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
                            "Incorrect max bet in the game " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors, game, new Object() {
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
                            "Incorrect min bet in the game " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }

        }
        throwError(errors, game, new Object() {
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
                            "Incorrect max total bet in the game " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors, game, new Object() {
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
                            "Error in the bet values. Game: " + game + ". Currency: " + currency + ".");
                } catch (AssertionError e) {
                    errors.append(e.getMessage()).append("\n");
                }
            }
        }
        throwError(errors, game, new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    private void throwError(StringBuilder message, String game, String testName) {
        if (!Objects.equals(message.toString(), "\n")) {
            throw new AssertionError(message.toString());
        } else {
            System.out.println(game + " " + testName + " PASSED");
        }
    }

}
