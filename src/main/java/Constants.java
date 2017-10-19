import java.io.File;

final class Constants {


    static final String ENVIRONMENT = System.getProperty("env") == null ? "stage" : System.getProperty("env");

    static final String resources = new File("src/main/resources").toString();
}
