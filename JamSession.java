package limelight;


import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by prajogotio on 16/2/15.
 */
public class JamSession{

    private Stage jamStage;
    private JamSessionManager jamSessionManager;
    private StackPane root;
    private Text scoreField;
    private Text songTitle;
    private Text timePassed;
    private Text announcement;
    private Timeline statusLoop;

    public JamSession(String sessionFileName) {
        jamSessionManager = JamSessionManager.createSessionFromFile(sessionFileName);
        initializeJamStage();
        initializeStatusLoop();
        jamStage.show();
    }

    private void initializeJamStage() {
        jamStage = new Stage();
        jamStage.setTitle("Jam Session");
        root = new StackPane();
        root.getChildren().add(jamSessionManager.getRoot());

        announcement = new Text();
        songTitle = new Text();
        timePassed = new Text();
        scoreField = new Text();
        songTitle.setFont(Font.font("Courier New", 20));
        songTitle.setFill(Color.WHITE);
        songTitle.setText(jamSessionManager.getSongTitle());
        timePassed.setFont(Font.font("Courier New", 20));
        timePassed.setFill(Color.WHITE);
        timePassed.setText("NOTHING");
        scoreField.setFont(Font.font("Courier New", 60));
        scoreField.setFill(Color.WHITE);
        scoreField.setText("" + jamSessionManager.getCurrentScore());
        announcement.setFont(Font.font("Courier New", 50));
        announcement.setFill(Color.WHITE);
        BorderPane statusPane = new BorderPane();
        HBox songStatusPane = new HBox();
        VBox topPane = new VBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.getChildren().addAll(songStatusPane, scoreField);
        songStatusPane.setAlignment(Pos.CENTER);
        songStatusPane.setSpacing(78);
        songStatusPane.getChildren().addAll(timePassed, songTitle);
        statusPane.setTop(topPane);
        statusPane.setCenter(announcement);
        root.getChildren().add(statusPane);

        jamStage.setScene(new Scene(root));
    }

    public void startJam() {
        setAnnouncement(jamSessionManager.getSongTitle());
        jamSessionManager.startSession();
        statusLoop.play();
    }

    public void jamSessionIsOverHandler() {
        statusLoop.stop();
        jamSessionManager.stop();
        setAnnouncement("Jam is Over\nYou scored: "+ jamSessionManager.getCurrentScore());
    }

    private String getTimeElapsedInformation() {
        double songDuration = jamSessionManager.getSongDuration() / 1000.0;
        double timeElapsed = System.currentTimeMillis() / 1000.0 - jamSessionManager.getTimeOfCreation() / 1000.0;
        return String.format("%02d:%02d/%02d:%02d", (int) Math.floor(timeElapsed/60.0), (int) Math.floor(timeElapsed)%60, (int) Math.floor(songDuration/60.0), (int) Math.floor(songDuration)%60);
    }

    private void initializeStatusLoop() {
        KeyFrame statusFrame = new KeyFrame(JamSessionManager.FPS, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scoreField.setText(""+jamSessionManager.getCurrentScore());
                timePassed.setText(getTimeElapsedInformation());
                if(jamSessionManager.isTimeUp(System.currentTimeMillis())) {
                    jamSessionIsOverHandler();
                };
            }
        });
        statusLoop = new Timeline();
        statusLoop.getKeyFrames().add(statusFrame);
        statusLoop.setCycleCount(Animation.INDEFINITE);
    }

    private void setAnnouncement(String announcement) {
        this.announcement.setText(announcement);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), this.announcement);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(0.9);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(2);
        fadeTransition.play();
    }

}
