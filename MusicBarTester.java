package limelight;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by prajogotio on 15/2/15.
 */
public class MusicBarTester extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Canvas canvas = new Canvas(500, 700);
        final MusicBar musicBar = new MusicBar(30, 30,100, 650, 600, System.currentTimeMillis(), 1300);
        musicBar.addMusicBeat(new MusicBeat(100, 10, 1000));
        musicBar.addMusicBeat(new MusicBeat(100, 10, 1500));
        musicBar.addMusicBeat(new MusicBeat(100, 10, 2300));
        final MusicBar secondMusicBar = new MusicBar(230, 30, 100, 650, 600, System.currentTimeMillis(), 1300);
        secondMusicBar.addMusicBeat(new MusicBeat(100, 10, 800));
        secondMusicBar.addMusicBeat(new MusicBeat(100, 10, 1400));
        KeyFrame keyframe = new KeyFrame(Duration.millis(1000 / 60), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                musicBar.update(System.currentTimeMillis());
                secondMusicBar.update(System.currentTimeMillis());
                musicBar.render(canvas.getGraphicsContext2D());
                secondMusicBar.render(canvas.getGraphicsContext2D());
            }
        });
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Group root = new Group();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
