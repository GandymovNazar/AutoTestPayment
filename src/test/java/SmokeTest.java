import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sikuli.script.FindFailed;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;


public class SmokeTest {
    public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
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
            default:
                CHROME_DRIVER = "drivers\\chromedriver.exe";
                break;
        }
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        driver = new ChromeDriver();

    }

    @DataProvider(name = "someGames")
    public Object[][] someGames() {
        return new Object[][]{
//                {"sw_ld", "10,10,10,10,10", 200},
//                {"sw_mf", "1,1,1,1,1", 300},
//                {"sw_ggdn", "1,1,1,1,1", 200},
//                {"sw_9s1k", "10,10,10,10,10", 100},
//                {"sw_888t", "1,1,1,1,1", 200},
//                {"sw_db", "10,10,10,10,10", 200},
//                {"sw_dhcf", "10,10,10,10,10", 200},
//                {"sw_dj", "10,10,10,10,10", 150},
//                {"sw_fbb", "10,7,0,0,0", 160},
//                {"sw_fp", "1,1,1,1,1", 200},
                {"sw_gm", "1,1,1,1,1"},
//                {"sw_h2h", "10,10,10,10,10", 180},
//                {"sw_hp", "10,10,10,10,10", 200},
//                {"sw_lodk", "1,1,1,1,1", 200},
//                {"sw_mer", "10,10,10,10,10", 240},
//                {"sw_nyf", "1,1,1,1,1", 200},
//                {"sw_qoiaf", "23,50,54,24,41", 200},
//                {"sw_rf", "1,1,1,1,1", 200},
//                {"sw_rs", "1,1,1,1,1", 200},
//                {"sw_scyd", "1,1,1,1,1", 200},
//                {"sw_sf", "1,1,1,1,1", 200},
//                {"sw_sgcf", "10,10,10,10,10", 200},
//                {"sw_slbs", "10,10,10,10,10", 200},
//                {"sw_sq", "18,73,127,65,9", 200},
//                {"sw_tc", "10,10,10,10,10", 180},
//                {"sw_ycs", "1,1,1,1,1", 180},
//                {"sw_sod", "1,3,5,7,9", 180},
//                {"sw_rm", "0,0,0,0,0", 200},
                {"sw_pc", "10,10,10,10,10"}
//                {"sw_omqjp", "54,57,57,58,56,4", 200},
//                {"sw_mrmnky", "5,5,5,5,5", 200},
//                {"sw_gol", "5,5,5,5,5", 240},
//                {"sw_dd", "5,5,5,5,5", 200},
//                {"sw_wq", "5,5,5,5,5", 100}


        };
    }

    @Test(dataProvider = "someGames")
    public void testSpin(String game, String loseCheat) throws IOException, InterruptedException,
            FindFailed, UnirestException, AWTException {

        String custId = "Dimon";
        String currency = "USD";
        String balance = "10";

        ServerMethods server = new ServerMethods();
        LocalMethods local = new LocalMethods();

        driver.manage().window().maximize();

        String token = server.getToken(Constants.ENVIRONMENT);
        server.createPlayer(custId, currency, token);
        server.addBalance(custId, currency, balance, token);
        String ticket = server.getTicket(custId);
        String linkForTheGame = server.getGameToken(game, token, ticket);

        driver.get(linkForTheGame);
        Game gameObject = new Game(driver);

        Thread.sleep(1000);

        gameObject.pressPlayGame();
        Double startBalance = Double.parseDouble(server.getUserBalance(custId));
        gameObject.sendCheat(loseCheat);
        Thread.sleep(1000);
        gameObject.pressSpin();
//        Object data = driver.executeScript("return c_infoLabel.text");
//        System.out.println(data);

        Thread.sleep(4000);
        gameObject.sendCheat(loseCheat);
        Thread.sleep(1000);
        gameObject.pressSpin();
        // the balance after 2 spins
        double endBalance = Double.parseDouble(server.getUserBalance(custId));

        System.out.println("[" + game + "] " + "Balance in the beginning: " + startBalance);
        double defaultBet = local.getDefaultBetFromFile(game, currency);
        double maxBet = local.getMaxBetFromFile(game, currency);
        double maxTotalBet = local.getMaxTotalBetFromFile(game, currency);
        double defaultTotalBet = maxTotalBet / maxBet * defaultBet;
        System.out.println("[" + game + "] " + "The sum of bets: " + (defaultTotalBet + defaultTotalBet));
        System.out.println("[" + game + "] " + "Final balance: " + endBalance);

        // Validation of balance
        Assert.assertEquals((startBalance - defaultTotalBet - defaultTotalBet), endBalance,
                "Balance isn't correct");

        Thread.sleep(5000);
    }

    @AfterSuite
    public void closeDriver(){
        driver.close();
    }

}
