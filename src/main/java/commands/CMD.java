package commands;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.io.IOException;


public class CMD {
    private static final String get_directory = System.getProperty("user.dir");
    private static final String get_driveLetter = get_directory.substring(0, 2);
    public static String[][] listCommands = {
            {"cmd.exe", "/c", get_driveLetter + " && cd " + get_directory + " && qres x=1366 y=768"},
            {"cmd.exe", "/c", get_driveLetter + " && cd " + get_directory + " && "+revertResolution()}
};

    private static String revertResolution(){
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();

        return "qres "+"x=" + (int)screenWidth +" "+ "y=" + (int)screenHeight;
    }


    public static void runCommands(String[] commands) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);

        processBuilder.start();
    }
}
