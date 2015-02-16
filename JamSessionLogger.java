package limelight;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by prajogotio on 16/2/15.
 */
public class JamSessionLogger extends Application {


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        JamSessionManager loggedSession = JamSessionManager.createLoggedJamSession("", "res/sugar.mp3", "logged.txt");
        primaryStage.setTitle("LOGGER");
        primaryStage.setScene(new Scene(loggedSession.getRoot()));
        primaryStage.show();
        loggedSession.startSession();
    }
}
