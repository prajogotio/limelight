package limelight;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by prajogotio on 15/2/15.
 */
public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Limelight - by Prajogo Tio");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("limelight.png")));
        BorderPane mainLayout = new BorderPane();
        VBox musicChoicePane = new VBox();
        musicChoicePane.setAlignment(Pos.CENTER);
        musicChoicePane.setPrefWidth(360.0);
        ScrollPane scroller = new ScrollPane(musicChoicePane);
        scroller.setPrefHeight(600.0);
        scroller.setPrefWidth(380.0);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scanner scanner = new Scanner(getClass().getResourceAsStream("list.txt"));
        int numberOfChoices = scanner.nextInt();
        scanner.nextLine();
        for(int i = 0; i < numberOfChoices; ++i) {
            final String fileName = scanner.nextLine();
            String imageSource = scanner.nextLine();
            Image img = new Image(getClass().getResourceAsStream(imageSource), 320, 320, false, false);
            Button button = new Button();
            button.setStyle("-fx-effect: null; -fx-background-color: transparent");
            button.setCursor(Cursor.HAND);
            button.setGraphic(new ImageView(img));
            button.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    JamSession jamSession = new JamSession(fileName);
                    jamSession.startJam();
                }
            });
            musicChoicePane.getChildren().add(button);
        }
        mainLayout.setCenter(scroller);
        Text titleLabel = new Text("Limelight");
        titleLabel.setFont(Font.font("Helvetica", 50));
        HBox topPanel = new HBox();
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(30, 20, 20, 30));
        topPanel.getChildren().add(titleLabel);
        mainLayout.setTop(topPanel);
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
    }
}
