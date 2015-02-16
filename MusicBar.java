package limelight;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by prajogotio on 15/2/15.
 */
public class MusicBar {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color PRESSED_COLOR = Color.color(0.9,0.9,0.8,0.5);
    private static final Color PRESSED_TARGET_COLOR = Color.color(0.9,0.9,0.6,0.9);
    private static final double BORDER_WIDTH = 2;
    private static final double ACTIVE_PRESS_MARGIN = 400;
    private double height;
    private double width;
    private double timeToScrollThrough;
    private double top;
    private double left;
    private double markHeight;
    private double speed;
    private double timeOfCreation;
    private int currentScore;

    private double lastTimePressed;
    private boolean isCurrentlyPressed;
    private boolean isActivePress;
    private ArrayList<MusicBeat> musicBeats;

    public MusicBar(double left, double top, double width, double height, double markHeight, double timeOfCreation, double timeToScrollThrough){
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.markHeight = markHeight;
        this.timeToScrollThrough = timeToScrollThrough;
        this.timeOfCreation = timeOfCreation;
        lastTimePressed = 0;
        isCurrentlyPressed = false;
        isActivePress = false;
        currentScore = 0;
        musicBeats = new ArrayList<MusicBeat>();
        speed = height/timeToScrollThrough;
    }

    public void addMusicBeat(MusicBeat musicBeat) {
        musicBeats.add(musicBeat);
    }

    public void render(GraphicsContext g) {
        g.save();
        g.translate(left, top);
        g.setFill(BACKGROUND_COLOR);
        g.setStroke((BORDER_COLOR));
        g.setLineWidth(BORDER_WIDTH);
        g.fillRect(0, 0, width, height);
        g.strokeRect(0, 0, width, height);
        g.setFill(BORDER_COLOR);
        g.fillRect(0, markHeight, width, 2);
        // Canvas Path is very expensive, as it turns out
        //g.moveTo(0, markHeight);
        //g.lineTo(width, markHeight);
        //g.stroke();
        g.restore();
        for (MusicBeat musicBeat : musicBeats) {
            musicBeat.render(g);
        }

        if(isCurrentlyPressed) {
            g.save();
            g.translate(left, top);
            g.setFill(PRESSED_COLOR);
            g.fillRect(0, 0, width, height);
            g.setFill(PRESSED_TARGET_COLOR);
            g.fillRect(0, markHeight, width, 4);
            g.restore();
        }
    }

    public void update(double currentTime) {
        updateActivePress(currentTime);
        ArrayList<MusicBeat> temp = new ArrayList<MusicBeat>();
        double timeElapsed = currentTime - timeOfCreation;
        double targetTop = markHeight + top;
        boolean OutOfScreen = false;
        for (MusicBeat musicBeat : musicBeats) {
            if(OutOfScreen) {
                temp.add(musicBeat);
                continue;
            }
            musicBeat.update(left, timeElapsed, speed, targetTop);
            if(isActivePress) {
                musicBeat.playerHitHandler(targetTop);
                if(musicBeat.isHitByPlayer()) currentScore++;
            }
            if(musicBeat.isAlive()) temp.add(musicBeat);
            if(!musicBeat.isOnScreen()) {
                OutOfScreen = true;
            }
        }
        musicBeats = temp;
    }

    private void updateActivePress(double currentTime) {
        if(!isActivePress) return;
        if(lastTimePressed + ACTIVE_PRESS_MARGIN < currentTime) isActivePress = false;
    }

    public void pressedHandler() {
        lastTimePressed = System.currentTimeMillis();
        isActivePress = true;
        isCurrentlyPressed = true;
    }

    public void releasedHandler() {
        isActivePress = false;
        isCurrentlyPressed = false;
    }

    public int getScore() {
        return currentScore;
    }

    public double getTimeOfCreation() { return timeOfCreation; }
}

class LoggedMusicBar extends MusicBar {
    private ArrayList<Double> log;

    public LoggedMusicBar(double left, double top, double width, double height, double markHeight, double timeOfCreation, double timeToScrollThrough) {
        super( left,  top,  width,  height,  markHeight,  timeOfCreation,  timeToScrollThrough);
        log = new ArrayList<Double>();
    }

    @Override
    public void pressedHandler() {
        log.add(Double.valueOf((double)(System.currentTimeMillis() - super.getTimeOfCreation()) ));
        super.pressedHandler();
    }

    public void appendToSessionFile(PrintWriter printWriter) {
        printWriter.print(log.size());
        for(Double d : log) {
            printWriter.format(" %.2f", (double) d);
        }
        printWriter.println();
    }
}