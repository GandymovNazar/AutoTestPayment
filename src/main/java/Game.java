import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;

class Game {


    void sendCheat(String cheat, WebDriver driver){
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




}
