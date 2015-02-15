package limelight;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by prajogotio on 15/2/15.
 */
public class MusicBeat {
    private static final double HIT_MARGIN = 35;
    private static final Color COLOR = Color.WHITE;
    //private static final Color INVERSE_COLOR = Color.BLACK;
    private double timeOfOccurance;
    private double top;
    private double left;
    private double width;
    private double height;
    private boolean isAlive;
    private boolean isHitByPlayer;

    public MusicBeat(double width, double height, double timeOfOccurance){
        this.top = -100;
        this.width = width;
        this.height = height;
        this.timeOfOccurance = timeOfOccurance;
        isAlive = true;
        isHitByPlayer = false;
    }

    private double computeTop(double timeElapsed, double speed, double markHeight) {
        return (timeElapsed - timeOfOccurance) * speed - markHeight;
    }

    public void render(GraphicsContext g) {
        if (!isAlive || !isOnScreen()) return;
        g.save();
        g.translate(left, top);
        g.setFill(COLOR);
        g.fillRect(0, 0, width, height);
        //g.setFill(INVERSE_COLOR);
        //g.fillRect(0, -height, width, height);
        g.restore();
    }

    public void update(double left, double timeElapsed, double speed, double markHeight) {
        if(!isAlive) return;
        top = computeTop(timeElapsed, speed, markHeight);
        this.left = left;
        if(top > JamSessionManager.CANVAS_HEIGHT) isAlive = false;
    }

    public void playerHitHandler(double markHeight) {
        if(Math.abs(top-markHeight) <= HIT_MARGIN) {
            isHitByPlayer = true;
            isAlive = false;

        }
    }

    public boolean isHitByPlayer() {
        return isHitByPlayer;
    }

    public boolean isOnScreen() {
        return top > -height;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

}
