import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;


public class SmokeTest {
    public static void main(String[] args) throws InterruptedException, IOException {
    }

    private ChromeDriver driver;

    @BeforeClass
    public void settings() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);
        String CHROME_DRIVER;
        switch (os) {
            case "win":
                CHROME_DRIVER = "drivers\\chromedriver.exe";
                break;
            case "mac os x":
                CHROME_DRIVER = "drivers/chromedriver";
                break;
            case "linux":
                CHROME_DRIVER = "drivers/chromedriver_linux32";
                break;
            default:
                CHROME_DRIVER = "drivers\\chromedriver.exe";
                break;
        }
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return new Object[][]{
                {"sw_db", "10,10,10,10,10", "mark2"},
                {"sw_dhcf", "10,10,10,10,10", "mark2"},
                {"sw_dj", "10,10,10,10,10", "mark2"},
                {"sw_fp", "1,1,1,1,1", "mark2"},
                {"sw_gm", "1,1,1,1,1", "mark2"},
                {"sw_nyf", "1,1,1,1,1", "mark2"},
                {"sw_rf", "1,1,1,1,1", "mark2"},
                {"sw_sf", "1,1,1,1,1", "mark2"},
                {"sw_sgcf", "10,10,10,10,10", "mark2"},
                {"sw_slbs", "10,10,10,10,10", "mark2"},
                {"sw_tc", "10,10,10,10,10", "mark2"},
                {"sw_ycs", "1,1,1,1,1", "mark2"},
                {"sw_pc", "10,10,10,10,10", "mark2"},
                {"sw_omqjp", "54,57,57,58,56,4", "mark2"},
                {"sw_dd", "5,5,5,5,5", "mark2"},
                {"sw_wq", "5,5,5,5,5", "mark2"},
                {"sw_mf", "1,1,1,1,1", "mark2"},
                {"sw_mj", "10,10,10,10,10", "mark2"},

                {"sw_gol", "5,5,5,5,5", "mark3"},
                {"sw_sod", "1,3,5,7,9", "mark3"},
                {"sw_mrmnky", "5,5,5,5,5", "mark3"},
                {"sw_al", "10,10,10,10,10", "mark3"},
                {"sw_rm", "0,0,0,0,0", "mark2"},
                {"sw_qoiaf", "23,50,54,24,41", "mark2"},
                {"sw_sq", "18,73,127,65,9", "mark2"},
                {"sw_mer", "10,10,10,10,10", "mark2"},
                {"sw_888t", "1,1,1,1,1", "mark2"},
                {"sw_rs", "1,1,1,1,1", "mark2"},
                {"sw_scyd", "1,1,1,1,1", "mark2"},
                {"sw_ggdn", "1,1,1,1,1", "mark2"},
                {"sw_ld", "10,10,10,10,10", "mark2"},

                {"sw_fbb", "10,7,0,0,0", "mark4"},
                {"sw_lodk", "1,1,1,1,1", "mark4"},
                {"sw_hp", "10,10,10,10,10", "mark4"},
                {"sw_h2h", "10,10,10,10,10", "mark4"},
                {"sw_9s1k", "10,10,10,10,10", "mark4"},
                {"sw_cts", "10,10,10,10,10", "mark4"}
        };
    }

    @Test(dataProvider = "allGames")
    public void testSpin(String gameName, String loseCheat, String mark) throws IOException, InterruptedException,
            UnirestException, AWTException {

        String custId = "SergeTest";
        String currency = "USD";

        ServerMethods server = new ServerMethods();

//        LocalMethods local = new LocalMethods();

        Game game = new Game(driver, mark);

        String token = server.getToken(Constants.ENVIRONMENT);
        server.createPlayer(custId, currency, token);
        server.addBalance(custId, currency, String.valueOf(game.round(100 - Double.parseDouble(server.getUserBalance(custId)), 2)), token);
        String ticket = server.getTicket(custId);
        String linkForTheGame = server.getGameToken(gameName, token, ticket);

        driver.get(linkForTheGame);

        game.pressPlayGame();
//        Double startBalance = Double.parseDouble(server.getUserBalance(custId));
        double startBalance = game.getBalanceFromUi();

        game.sendCheat(loseCheat);
        game.pause();
        double totalBet = game.getTotalBet(mark);
        game.pressSpin();
//        Object data = driver.executeScript("return c_infoLabel.text");
//        System.out.println(data);

        game.pause(3);
        game.sendCheat(loseCheat);
        game.pause();
        game.pressSpin();
        game.pause();
        // the balance after 2 spins
//        double endBalance = Double.parseDouble(server.getUserBalance(custId));
        double endBalance = game.getBalanceFromUi();

        System.out.println("[" + gameName + "] " + "Balance in the beginning: " + startBalance);
//        for server's value
//        double defaultBet = local.getDefaultBetFromFile(game, currency);
//        double maxBet = local.getMaxBetFromFile(game, currency);
//        double maxTotalBet = local.getMaxTotalBetFromFile(game, currency);
//        double defaultTotalBet = maxTotalBet / maxBet * defaultBet;
//        System.out.println("[" + game + "] " + "The sum of bets: " + (defaultTotalBet + defaultTotalBet));
        System.out.println("[" + gameName + "] " + "The sum of bets: " + (totalBet + totalBet));
        System.out.println("[" + gameName + "] " + "Final balance: " + endBalance);

        // Validation of balance
//        Assert.assertEquals((startBalance - defaultTotalBet - defaultTotalBet), endBalance,
//                "Balance isn't correct");
        Assert.assertEquals(game.round(startBalance - totalBet * 2, 2), endBalance,
                "Balance isn't correct");
    }

    @AfterSuite
    public void closeDriver() {
        driver.close();
        System.out.println("Test report you can find there: http://10.37.18.73:8000/");
    }

}
