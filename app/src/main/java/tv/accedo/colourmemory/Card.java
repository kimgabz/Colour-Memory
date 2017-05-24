package tv.accedo.colourmemory;

import android.widget.Button;

/**
 * Created by kimharold on 5/22/17.
 */

public class Card {

    private Button button;
    private int x;
    private int y;

    public Card(Button button, int x,int y) {
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
