/**
 * Class Main.java
 * This class is the main class of the project.
 */
package Main;

import Features.Database;
import commands.CMD;

import data.LoginMenu;
import exception.custom.IllegalAdminAccess;
import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;

import static Features.Database.student_loginChecker;

public class Main extends Application {

    /**
     * The start method is called when the application is launched.
     * It initializes the LoginMenu and executes the first command in the command list in CMD class.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        LoginMenu loginMenuObj = new LoginMenu();


            try{
                CMD.runCommands(CMD.listCommands[0]);


                loginMenuObj.menu();

            }catch (IOException e){
                System.out.println("Error: Main.java, Method: start\n"+"Details information:\n"+e);
            }




    }



    public static void main(String[] args) {
        launch(args);
    }

}
