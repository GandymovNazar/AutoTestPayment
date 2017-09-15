import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestBetValues {

    private String[] currencies = {"USD", "EUR", "GBP", "MYR", "CNY", "JPY", "KRW", "IDR", "IDS", "VND", "VNS", "THB"};
    private BetLimits betLimits = new BetLimits();

    public TestBetValues() throws UnirestException {
    }

    @DataProvider(name = "notMark4Games")
    public Object[][] notMark4Games() {
        return new Object[][]{{"sw_sgcf"}, {"sw_rs"}, {"sw_qoiaf"}, {"sw_nyf"}, {"sw_sq"}, {"sw_db"}, {"sw_slbs"}, {"sw_888t"}, {"sw_pc"}, {"sw_mer"}, {"sw_tc"}, {"sw_dhcf"}, {"sw_omqjp"}, {"sw_gm"}, {"sw_ycs"}, {"sw_al"}, {"sw_mrmnky"}, {"sw_sod"}, {"sw_gol"},
                {"sw_dd"}, {"sw_scyd"}, {"sw_dj"}, {"sw_sf"}, {"sw_mf"}, {"sw_rm"}, {"sw_fp"}, {"sw_rf"}, {"sw_hp"}, {"sw_h2h"}, {"sw_ld"}, {"sw_ggdn"}};
    }

    @DataProvider(name = "mark4Games")
    public Object[][] mark4Games() {
        return new Object[][]{{"sw_fbb"}, {"sw_lodk"}};
    }

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return new Object[][]{{"sw_sgcf"}, {"sw_rs"}, {"sw_qoiaf"}, {"sw_nyf"}, {"sw_sq"}, {"sw_db"}, {"sw_slbs"}, {"sw_888t"}, {"sw_pc"}, {"sw_mer"}, {"sw_tc"}, {"sw_dhcf"}, {"sw_omqjp"}, {"sw_gm"}, {"sw_ycs"}, {"sw_al"}, {"sw_mrmnky"}, {"sw_sod"}, {"sw_gol"},
                {"sw_dd"}, {"sw_scyd"}, {"sw_dj"}, {"sw_sf"}, {"sw_mf"}, {"sw_rm"}, {"sw_fp"}, {"sw_rf"}, {"sw_fbb"}, {"sw_lodk"}, {"sw_hp"}, {"sw_h2h"}, {"sw_ld"}, {"sw_ggdn"}};
    }

    @BeforeTest
    public void printDetails() {
        System.out.println("Checking games on " + Constants.ENVIRONMENT);
    }

    @Test(dataProvider = "notMark4Games")
    public void testWinCapping(String game) throws UnirestException, IOException {
        System.out.println("\nWin capping for game " + game);
        for (String currency : currencies) {
            System.out.println("Checking win capping for " + currency);
            Assert.assertEquals(Long.parseLong(betLimits.getLimitsFromServer(game, currency).get("winMax").toString()), betLimits.getWincappingFromFile(currency, false),
                    "Incorrect win capping value in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @Test(dataProvider = "mark4Games")
    public void testWinCappingMark4(String game) throws UnirestException, IOException {
        System.out.println("\nWin capping for game " + game);
        for (String currency : currencies) {
            System.out.println("Checking win capping for " + currency);
            Assert.assertEquals(Long.parseLong(betLimits.getLimitsFromServer(game, currency).get("winMax").toString()), betLimits.getWincappingFromFile(currency, true),
                    "Incorrect win capping value in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @Test(dataProvider = "allGames")
    public void testBetValues(String game) throws UnirestException, IOException {
        System.out.println("\nBet values for game " + game);
        for (String currency : currencies) {
            System.out.println("Checking bet values for " + currency);
            ArrayList<Double> limitDouble = new ArrayList<>();
            JSONArray limits = (JSONArray) betLimits.getLimitsFromServer(game, currency).get("stakeAll");
            for (Object limit : limits) {
                limitDouble.add(new Double(limit.toString()));
            }
                Assert.assertEquals(limitDouble, betLimits.getLimitsFromFile(game, currency),
                        "Error in the bet values. Game: " + game + ". Currency: " + currency + ".");
        }
    }


    @Test(dataProvider = "allGames")
    public void testDefaultBet(String game) throws UnirestException, IOException {
        System.out.println("\nChecking default bet for game: " + game);
        for (String currency : currencies) {
            System.out.println("Checking default bet value for " + currency);
            Assert.assertEquals(betLimits.getDefaultBetFromServer(game, currency), betLimits.getLimitsFromFile(game, currency).get(betLimits.getDefaultBetElement(game)),
                    "Incorrect default bet in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @Test(dataProvider = "allGames")
    public void testMinBet(String game) throws IOException, UnirestException {
        System.out.println("\nChecking min bet for game: " + game);
        for (String currency : currencies) {
            System.out.println("Checking min bet value for " + currency);
            Assert.assertEquals(betLimits.getMinBet(game, currency), betLimits.getLimitsFromFile(game, currency).get(0),
                    "Incorrect min bet in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @Test(dataProvider = "allGames")
    public void testMaxBet(String game) throws IOException, UnirestException {
        System.out.println("\nChecking max bet for game: " + game);
        for (String currency : currencies) {
            System.out.println("Checking max bet value for " + currency);
            ArrayList<Double> limitsFromFile = betLimits.getLimitsFromFile(game, currency);
            Assert.assertEquals(betLimits.getMaxBet(game, currency), limitsFromFile.get(limitsFromFile.size() - 1),
                    "Incorrect max bet in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @Test(dataProvider = "allGames")
    public void testMaxTotalBet(String game) throws IOException, UnirestException {
        System.out.println("\nChecking max total bet for game: " + game);
        for (String currency : currencies) {
            System.out.println("Checking max total bet value for " + currency);
            ArrayList<Double> limitsFromFile = betLimits.getLimitsFromFile(game, currency);
            Assert.assertEquals(betLimits.getMaxTotalBetFromServer(game, currency), (long) (limitsFromFile.get(limitsFromFile.size() - 1) * betLimits.getMaxTotalStakeFromFile(game)),
                    "Incorrect max total bet in the game " + game + ". Currency: " + currency + ".");
        }
    }

    @AfterSuite
    public void printDetailsAfter(){
        System.out.println("Tests have been made on " + Constants.ENVIRONMENT + ".");
    }

}
