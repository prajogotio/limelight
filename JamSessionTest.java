package limelight;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by prajogotio on 16/2/15.
 */
public class JamSessionTest extends Application{
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        JamSession jamSession = new JamSession("jams/sugar.txt");
        jamSession.startJam();
    }
}
