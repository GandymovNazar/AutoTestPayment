import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public final class Constants {


    public static final boolean STAGE = Boolean.parseBoolean(System.getProperty("stage"));

    public static final String resources = new File("src/main/resources").toString();
}
