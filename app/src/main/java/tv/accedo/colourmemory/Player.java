package tv.accedo.colourmemory;

/**
 * Created by kimharold on 5/23/17.
 */

public class Player {

    private String name;
    private int score;

    public Player() {

    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
