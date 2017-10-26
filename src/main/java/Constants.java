import java.io.File;

final class Constants {


    static final String ENVIRONMENT = System.getProperty("env") == null ? "stage" : System.getProperty("env");
    static final String GAME = System.getProperty("game"); // add parameter -Dgame=yourGame if you want to check only 1 game

    static final String resources = new File("src/main/resources").toString();
}
