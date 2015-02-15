package limelight;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import limelight.JamSessionManager;

/**
 * Created by prajogotio on 15/2/15.
 */
public class JamSessionManagerTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        JamSessionManager jamSessionManager = JamSessionManager.createSessionFromFile("jams/sugar.txt");
        StackPane pane = new StackPane();
        Text text =new Text();
        text.setFont(Font.font("Courier New", 80));
        text.setFill(Color.WHITE);
        text.setText("SUGAR");

        pane.getChildren().add(jamSessionManager.getRoot());
        pane.getChildren().add(text);

        primaryStage.setScene(new Scene(pane));
        jamSessionManager.startSession();
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
