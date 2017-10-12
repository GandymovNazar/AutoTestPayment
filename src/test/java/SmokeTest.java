import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;



public class SmokeTest {
    public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
    }

    @BeforeClass
    public void settings() {
        String CHROME_DRIVER = "src\\main\\java\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        Settings.AutoWaitTimeout = 80f;
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
//                {"sw_gm", "1,1,1,1,1", 200},
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
                {"sw_pc", "10,10,10,10,10", 200}
//                {"sw_omqjp", "54,57,57,58,56,4", 200},
//                {"sw_mrmnky", "5,5,5,5,5", 200},
//                {"sw_gol", "5,5,5,5,5", 240},
//                {"sw_dd", "5,5,5,5,5", 200},
//                {"sw_wq", "5,5,5,5,5", 100}


        };
    }

    @Test(dataProvider = "someGames")
    public void testSpin(String game, String loseCheat, int defaultTotalBet) throws IOException, InterruptedException,
            FindFailed, UnirestException, AWTException {

        ChromeDriver driver = new ChromeDriver();

        String custId = "Dimon";
        String currency = "USD";
        String balance = "10";

        Methods gameName = new Methods();

        driver.manage().window().maximize();

        String token = gameName.getAdminToken();
        gameName.createPlayer(custId,currency, token);
        gameName.addBalance(custId, currency, balance, token);
        String ticket = gameName.getTicket(custId);
        String linkForTheGame = gameName.getGameToken(game, token, ticket);

        driver.get(linkForTheGame);

        Thread.sleep(10000);
//        Object data = driver.executeScript("return c_button.visible");
//        System.out.println(data);

//        WebDriverWait wait = new WebDriverWait(driver, 20);
//        System.out.println(driver.executeScript("c_button.visible"));
//        wait.until(ExpectedConditions.jsReturnsValue(""));


//        wait.until(ExpectedConditions.jsReturnsValue("c_button.visible"));


        // press button "Play game"
        driver.executeScript("c_button.emit('click')");



//        // screenshot
//        Screen screen = new Screen();
//
//        // Open the game. Press button 'Play'
//        Pattern pattern = new Pattern(resources_folder + "button_play.png").similar(0.8f);
//
//        screen.find(pattern).click();

        //driver.executeScript("(function(){alert(1)})();");

//        Mouse.move(150, 150);
//        Thread.sleep(1000);

        // Balance in the beginning
        String startBalance = driver.findElement(By.className("footer-balance-value")).getText()
                .replace("$", "")
                .replace(".", "")
                .replace(",", "");
        int startBalanceInt = Integer.parseInt(startBalance);


        // set Cheat
        Game cheatsForGame = new Game();
        cheatsForGame.sendCheat(loseCheat, driver);


        Thread.sleep(1000);
        driver.executeScript("c_spinButton.emit('click')");
//        Object data = driver.executeScript("return c_infoLabel.text");
//        System.out.println(data);

        Thread.sleep(4000);
        // set Cheat
        cheatsForGame.sendCheat(loseCheat, driver);

        // do the 2-nd spin
        Thread.sleep(1000);
        driver.executeScript("c_spinButton.emit('click')");

        // the balance after 2 spins
        String endBalance = driver.findElement(By.className("footer-balance-value")).getText()
                .replace("$", "")
                .replace(".", "")
                .replace(",", "");
        int endBalanceInt = Integer.parseInt(endBalance);

        System.out.println("[" + game + "] " + "Balance in the beginning: " + startBalanceInt);
        System.out.println("[" + game + "] " + "The sum of bets: " + (defaultTotalBet + defaultTotalBet));
        System.out.println("[" + game + "] " + "Final balance: " + endBalance);

        // Validation of balance
        Assert.assertEquals((startBalanceInt - defaultTotalBet - defaultTotalBet), endBalanceInt,
                "Balance isn't correct");

        // Добавить дефолтную ставку. Сверять ставку в игре с заданой

        Thread.sleep(5000);
        driver.close();
    }

}
