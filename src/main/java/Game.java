import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

class Game {

    private ChromeDriver driver;
//    private JavascriptExecutor executor = (JavascriptExecutor) driver;

    Game (ChromeDriver driver){
        this.driver = driver;
    }


    void sendCheat(String cheat){
        try {
            driver.findElement(By.className("combinations-toggle")).click();
            driver.findElement(By.className("combinations-input-field")).sendKeys(cheat);
            Thread.sleep(1000);
            driver.findElement(By.className("combinations-input-ok")).click();
            driver.findElement(By.className("combinations-toggle")).click();
        }

        catch (ElementNotVisibleException ee){
            driver.findElement(By.xpath("/html/body/div[3]/div/div[17]")).click();
            driver.findElement(By.xpath("/html/body/div[3]/div/div[17]/div/div[1]")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void pressPlayGame() throws InterruptedException {
        int maxWait = 60 * 1000;
        int timeout = 2000;
        int waited = 0;
        try {
            String js = "return c_button.emit('click')";
            boolean test = (boolean) driver.executeScript(js);
            if (!test) {
                Thread.sleep(1000);
                waited +=1000;
                pressPlayGame();
            }
            if (waited > maxWait){
                throw new AssertionError("Can't find button Play game");
            }
        } catch (WebDriverException e) {
//            System.out.println(String.format("Waiting for \"Play game\" button for %s sec.", timeout));
            Thread.sleep(timeout);
            waited+=timeout;
            pressPlayGame();
        }
    }

    void pressSpin(){
        driver.executeScript("c_spinButton.emit('click')");
    }




}
