package limelight;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by prajogotio on 15/2/15.
 */
public class JamSessionManager {
    public static final double CANVAS_WIDTH = 560;
    public static final double CANVAS_HEIGHT = 700;
    public static final Duration FPS = Duration.millis(1000/60);
    private static final double MUSIC_BAR_WIDTH = 80;
    private static final double MUSIC_BAR_HEIGHT = 700;
    private static final double MUSIC_BAR_MARK_HEIGHT = 600;
    private static final double MUSIC_BEAT_WIDTH = 80;
    private static final double MUSIC_BEAT_HEIGHT = 10;
    private static final double NUMBER_OF_MUSIC_BAR = 7;
    private static final int KEY_IS_NOT_PRESSED = 0;
    private static final int KEY_IS_ACTIVE = 1;
    private static final int KEY_IS_INACTIVE = 2;

    private static final String FIRST_BAR_PRESSED = "FIRST BAR PRESSED";
    private static final String SECOND_BAR_PRESSED = "SECOND BAR PRESSED";
    private static final String THIRD_BAR_PRESSED = "THIRD BAR PRESSED";
    private static final String FOURTH_BAR_PRESSED = "FOURTH BAR PRESSED";
    private static final String FIFTH_BAR_PRESSED = "FIFTH BAR PRESSED";
    private static final String SIXTH_BAR_PRESSED = "SIXTH BAR PRESSED";
    private static final String SEVENTH_BAR_PRESSED = "SEVENTH BAR PRESSED";

    private MediaPlayer mediaPlayer;
    private Timeline jamSessionLoop;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Group root;
    private ArrayList<MusicBar> musicBars;
    private double timeOfScroll;
    private double timeOfCreation;
    private double songDuration;
    private Map<String, Integer> commands;
    private int currentScore;
    private String songTitle;
    private int numberOfBeats;

    public static JamSessionManager createSessionFromFile(String sessionFile) {
        return new JamSessionManager(sessionFile);
    }

    public void startSession() {
        jamSessionLoop.play();
        mediaPlayer.play();
    }

    public Group getRoot() {
        return root;
    }

    private JamSessionManager() {
        currentScore = 0;
        commands = new HashMap<String, Integer>();
        timeOfCreation = System.currentTimeMillis();
    };

    private JamSessionManager(String sessionFile) {
        this();
        constructJamSessionFromFile(sessionFile);
        initializeCanvas();
        initializeJamSessionLoop();
        initializeKeyEventHandler();
    }

    private void initializeCanvas() {
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);
        graphicsContext = canvas.getGraphicsContext2D();
        root = new Group();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);
        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);
    }

    private void initializeJamSessionLoop() {
        KeyFrame sessionKeyframe = new KeyFrame(FPS, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double currentTime = System.currentTimeMillis();
                handleCommands();
                int totalScore = 0;
                for (MusicBar musicBar : musicBars) {
                    musicBar.update(currentTime);
                    musicBar.render(graphicsContext);
                    totalScore += musicBar.getScore();
                }
                currentScore = totalScore;
            }
        });

        jamSessionLoop = new Timeline();
        jamSessionLoop.setCycleCount(Animation.INDEFINITE);
        jamSessionLoop.getKeyFrames().add(sessionKeyframe);
//        jamSessionLoop.setOnFinished(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                jamSessionLoop.play();
//            }
//        });


    }

    private void handleCommands() {
        if(isCommandActive(FIRST_BAR_PRESSED)) {
            musicBars.get(0).pressedHandler();
            commands.put(FIRST_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(FIRST_BAR_PRESSED)) {
            musicBars.get(0).releasedHandler();
        }
        if(isCommandActive(SECOND_BAR_PRESSED)) {
            musicBars.get(1).pressedHandler();
            commands.put(SECOND_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(SECOND_BAR_PRESSED)) {
            musicBars.get(1).releasedHandler();
        }
        if(isCommandActive(THIRD_BAR_PRESSED)) {
            musicBars.get(2).pressedHandler();
            commands.put(THIRD_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(THIRD_BAR_PRESSED)) {
            musicBars.get(2).releasedHandler();
        }
        if(isCommandActive(FOURTH_BAR_PRESSED)) {
            musicBars.get(3).pressedHandler();
            commands.put(FOURTH_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(FOURTH_BAR_PRESSED)) {
            musicBars.get(3).releasedHandler();
        }
        if(isCommandActive(FIFTH_BAR_PRESSED)) {
            musicBars.get(4).pressedHandler();
            commands.put(FIFTH_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(FIFTH_BAR_PRESSED)) {
            musicBars.get(4).releasedHandler();
        }
        if(isCommandActive(SIXTH_BAR_PRESSED)) {
            musicBars.get(5).pressedHandler();
            commands.put(SIXTH_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(SIXTH_BAR_PRESSED)) {
            musicBars.get(5).releasedHandler();
        }
        if(isCommandActive(SEVENTH_BAR_PRESSED)) {
            musicBars.get(6).pressedHandler();
            commands.put(SEVENTH_BAR_PRESSED, KEY_IS_INACTIVE);
        }
        if(isCommandNotPressed(SEVENTH_BAR_PRESSED)) {
            musicBars.get(6).releasedHandler();
        }

    }

    private boolean isCommandActive(String command) {
        if(commands.containsKey(command)) {
            return commands.get(command).compareTo(KEY_IS_ACTIVE) == 0;
        } else {
            return false;
        }
    }

    private boolean isCommandNotPressed(String command) {
        if(commands.containsKey(command)) {
            return commands.get(command).compareTo(KEY_IS_NOT_PRESSED) == 0;
        } else {
            return true;
        }
    }

    private void constructJamSessionFromFile(String sessionFile) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(sessionFile);
            Scanner scanner = new Scanner(inputStream);
            String songFileName = scanner.nextLine();
            songTitle = scanner.nextLine();
            initializeMediaPlayer(songFileName);
            songDuration = scanner.nextDouble();
            timeOfScroll = scanner.nextDouble();
            initializeMusicBars();
            numberOfBeats = 0;
            for (int i = 0; i < NUMBER_OF_MUSIC_BAR; ++i) {
                int k = scanner.nextInt();
                numberOfBeats += k;
                for (int j = 0; j < k; ++j) {
                    long timeOfOccurance = (long) scanner.nextDouble();
                    timeOfOccurance  = ((timeOfOccurance - 100L) / 100L) * 100L;
                    musicBars.get(i).addMusicBeat(new MusicBeat(MUSIC_BEAT_WIDTH, MUSIC_BEAT_HEIGHT, timeOfOccurance));
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeMediaPlayer(String songFileName) {
        Media media = new Media(getClass().getResource(songFileName).toString());
        mediaPlayer = new MediaPlayer(media);
    }

    private void initializeMusicBars() {
        musicBars = new ArrayList<MusicBar>();
        double dLeft = (CANVAS_WIDTH - NUMBER_OF_MUSIC_BAR * MUSIC_BAR_WIDTH) / 2.0;
        double dTop = (CANVAS_HEIGHT - MUSIC_BAR_HEIGHT) / 2.0;
        for (int i = 0; i < NUMBER_OF_MUSIC_BAR; ++i) {
            musicBars.add(new MusicBar(i * MUSIC_BAR_WIDTH + dLeft, dTop, MUSIC_BAR_WIDTH, MUSIC_BAR_HEIGHT, MUSIC_BAR_MARK_HEIGHT, timeOfCreation, timeOfScroll));
        }
    }

    private void initializeKeyEventHandler() {
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                KeyCode keyCode = event.getCode();
                if(keyCode.equals(KeyCode.S)) {
                    if(isCommandNotPressed(FIRST_BAR_PRESSED)) commands.put(FIRST_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.D)) {
                    if(isCommandNotPressed(SECOND_BAR_PRESSED)) commands.put(SECOND_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.F)) {
                    if(isCommandNotPressed(THIRD_BAR_PRESSED)) commands.put(THIRD_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.SPACE)) {
                    if(isCommandNotPressed(FOURTH_BAR_PRESSED)) commands.put(FOURTH_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.J)) {
                    if(isCommandNotPressed(FIFTH_BAR_PRESSED)) commands.put(FIFTH_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.K)) {
                    if(isCommandNotPressed(SIXTH_BAR_PRESSED)) commands.put(SIXTH_BAR_PRESSED, KEY_IS_ACTIVE);
                }
                if(keyCode.equals(KeyCode.L)) {
                    if(isCommandNotPressed(SEVENTH_BAR_PRESSED)) commands.put(SEVENTH_BAR_PRESSED, KEY_IS_ACTIVE);
                }

                if(isLogging) {
                    if(keyCode.equals(KeyCode.P)) {
                        stop();
                        logSession();
                    }
                }

            }
        });

        canvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if(keyCode.equals(KeyCode.S)) {
                    commands.put(FIRST_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.D)) {
                    commands.put(SECOND_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.F)) {
                    commands.put(THIRD_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.SPACE)) {
                    commands.put(FOURTH_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.J)) {
                    commands.put(FIFTH_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.K)) {
                    commands.put(SIXTH_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
                if(keyCode.equals(KeyCode.L)) {
                    commands.put(SEVENTH_BAR_PRESSED, KEY_IS_NOT_PRESSED);
                }
            }
        });
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public boolean isTimeUp(double currentTime) {
        return timeOfCreation + songDuration < currentTime;
    }

    public void stop() {
        jamSessionLoop.stop();
        fadeOutMediaPlayer();
    }

    private void fadeOutMediaPlayer() {
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.setVolume(mediaPlayer.getVolume() * 0.99);
                if(mediaPlayer.getVolume() < 0.0001) {
                    timeline.stop();
                    mediaPlayer.stop();
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public String getSongTitle() {
        return songTitle;
    }

    public double getSongDuration() {
        return songDuration;
    }

    public double getTimeOfCreation() {
        return timeOfCreation;
    }

    public int getNumberOfBeats() { return numberOfBeats; }







    //Extension for Session Logging
    private PrintWriter printWriter;
    private boolean isLogging;

    public static JamSessionManager createLoggedJamSession(String songTitle, String songFileName, String logFile) {
        return new JamSessionManager(songTitle, songFileName, logFile);
    }

    private JamSessionManager(String songTitle, String songFileName, String logFile) {
        this();
        isLogging = true;
        songDuration = 1000000;
        timeOfScroll = 1000;
        musicBars = new ArrayList<MusicBar>();
        double dLeft = (CANVAS_WIDTH - NUMBER_OF_MUSIC_BAR * MUSIC_BAR_WIDTH) / 2.0;
        double dTop = (CANVAS_HEIGHT - MUSIC_BAR_HEIGHT) / 2.0;
        for (int i = 0; i < NUMBER_OF_MUSIC_BAR; ++i) {
            musicBars.add(new LoggedMusicBar(i * MUSIC_BAR_WIDTH + dLeft, dTop, MUSIC_BAR_WIDTH, MUSIC_BAR_HEIGHT, MUSIC_BAR_MARK_HEIGHT, timeOfCreation, timeOfScroll));
        }
        initializeMediaPlayer(songFileName);
        initializeCanvas();
        initializeJamSessionLoop();
        initializeKeyEventHandler();

        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)), true);
            printWriter.println(songFileName);
            printWriter.println(songTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logSession() {
        try {
            printWriter.format("%.2f ", (double) System.currentTimeMillis());
            printWriter.format("%.2f\n", 1000.0);
            for (MusicBar musicBar : musicBars) {
                LoggedMusicBar loggedMusicBar = (LoggedMusicBar) musicBar;
                loggedMusicBar.appendToSessionFile(printWriter);
            }
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
