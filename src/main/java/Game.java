import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Game {

    private ChromeDriver driver;
    private String mark;
    private int waited = 0;
//    private JavascriptExecutor executor = (JavascriptExecutor) driver;

    Game(ChromeDriver driver, String mark) {
        this.driver = driver;
        this.mark = mark;
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
        String js;
        switch (mark) {
            case "mark3":
                js = "return c_playBtn.emit('click')";
                break;
            case "mark2":
            case "mark4":
            default:
                js = "return c_button && c_button.worldVisible && c_button.emit('click')";
                break;
        }
        if (waited > maxWait){
            throw new AssertionError("Can't find button Play game. Waited " + maxWait / 1000 + " sec.");
        }
        try {
            boolean test = (boolean) driver.executeScript(js);
            if (!test) {
                Thread.sleep(timeout);
                waited += timeout;
                pressPlayGame();
            }
        } catch (WebDriverException e) {
            waited += timeout;
//            System.out.println(String.format("Waiting for \"Play game\" button for %s sec.", timeout));
            Thread.sleep(timeout);
            pressPlayGame();
        }
    }

    void pressSpin(){
        String js;
        switch (mark){
            case "mark2":
                js = "c_spinButton.emit('click')";
                break;
            case  "mark3":
                js = "c_spinButton.emit('click')";
                break;
            case  "mark4":
                js = "m_mainView.dispatcher.dispatch(\"BottomBarEvent.READY_TO_SPIN\");";
                break;
            default:
                js = "c_spinButton.emit('click')";
                break;
        }
        driver.executeScript(js);
    }


    double getBalanceFromUi() {
        try {
            String balanceFromUi = driver.findElement(By.className("footer-balance-value")).getText()
                    .replace(",", "");
            Pattern p = Pattern.compile("\\d+\\.\\d+");
            Matcher m = p.matcher(balanceFromUi);
            if (m.find()) {
                balanceFromUi = m.group(0);
            }
            return Double.parseDouble(balanceFromUi);
        } catch (NumberFormatException e) {
            System.out.println("Can't get balance. Trying again.");
            pause();
            getBalanceFromUi();
        }
        return getBalanceFromUi();
    }

    double getTotalBet(String mark) {
        String js;
        switch (mark){
            case "mark4":
                js = "return c_totalBet.valueLabel.text";
                break;
            default:
                js = "return c_totalbetLabel.text";
                break;
        }
       Pattern p = Pattern.compile("\\d+.\\d+|\\d+");
       Object data = driver.executeScript(js);
       String d = (String) data;
       Matcher m = p.matcher(d);
        if (m.find()) {
            d = m.group(0);
        }
       return Double.parseDouble(d);
    }

    double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    void pause(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void pause(int sec){
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
