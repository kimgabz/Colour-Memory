package tv.accedo.colourmemory;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static int ROW = 4;
    private static int COL = 4;
    private Context context;
    private Drawable backImage;
    private int[][] cards;
    private int paired;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondCard;

    private static final Object lock = new Object();

//    private int turns;
    private boolean firstTurn = true;
    private int score1 = 0;
//    private int score2 = 0;
    private TextView player1;
//    private TextView player2;
    private UpdateCardsHandler handler;
    private ButtonListener buttonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        TableLayout mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        TableRow tr = (TableRow) findViewById(R.id.TableRow03);
        Button playerScore = (Button) findViewById(R.id.player_score);
        playerScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("score", 1000);
                startActivity(intent);
            }
        });

        player1 = (TextView) findViewById(R.id.player_1);
//        player2 = (TextView) findViewById(R.id.player_2);

        context  = mainTable.getContext();

        images = new ArrayList<>();

        images.add(getDrawableRes(R.drawable.colour1));
        images.add(getDrawableRes(R.drawable.colour2));
        images.add(getDrawableRes(R.drawable.colour3));
        images.add(getDrawableRes(R.drawable.colour4));
        images.add(getDrawableRes(R.drawable.colour5));
        images.add(getDrawableRes(R.drawable.colour6));
        images.add(getDrawableRes(R.drawable.colour7));
        images.add(getDrawableRes(R.drawable.colour8));

        backImage = getDrawableRes(R.drawable.card_bg);

        handler = new UpdateCardsHandler();

        buttonListener = new ButtonListener();

        cards = new int[COL][ROW];

        mainTable = new TableLayout(context);
        tr.addView(mainTable);

        for (int y = 0; y < ROW; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard=null;
        loadCards();

        player1.setText("Player 1: " + score1);
//        player2.setText("Player 2: " + score2);
//        player2.setVisibility(View.GONE);
    }

    private Drawable getDrawableRes(int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    private void loadCards() {
        try {
            int size = ROW * COL;
            paired = size;
            ArrayList<Integer> list = new ArrayList<>();

            for(int i = 0; i < size; i++){
                list.add(i);
            }

            Random r = new Random();

            for (int i = size-1; i >= 0; i--) {
                int t = 0;

                if (i > 0) {
                    t = r.nextInt(i);
                }

                t = list.remove(t);
                cards[i%COL][i/COL] = t%(size/2);
            }
        }
        catch (Exception e) {
            Log.e("loadCards()", e+"");
        }
    }

    private TableRow createRow(int y){
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL; x++) {
            row.addView(createImageButton(x,y));
        }
        return row;
    }

    private View createImageButton(int x, int y){
        Button button = new Button(context);
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        button.setBackgroundDrawable(backImage);
        return button;
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            synchronized (lock) {
                if (firstCard  != null &&
                    secondCard != null) {
                    return;
                }
                int id = v.getId();
                int x = id/100;
                int y = id%100;
                turnCard((Button)v,x,y);
            }

        }

        private void turnCard(Button button, int x, int y) {
            button.setBackgroundDrawable(images.get(cards[x][y]));

            if (firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{

                if(firstCard.getX() == x &&
                   firstCard.getY() == y){
                    return; //the user pressed the same card
                }

                secondCard = new Card(button,x,y);

                TimerTask timerTask = new TimerTask() {

                    @Override
                    public void run() {
                        try{
                            synchronized (lock) {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e) {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer timer = new Timer(false);
                timer.schedule(timerTask, 1000);
            }

        }

    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
//        if (firstTurn) {
//            Toast.makeText(getApplicationContext(), "Player 1", Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "Player 2", Toast.LENGTH_LONG).show();
//        }
    }

    private class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }

        private void checkCards(){
            if (cards[secondCard.getX()][secondCard.getY()] ==
                cards[firstCard.getX()][firstCard.getY()]){
                firstCard.getButton().setVisibility(View.INVISIBLE);
                secondCard.getButton().setVisibility(View.INVISIBLE);
//                int finalScore;
//                if (isFirstTurn()) {
                    score1 = score1 + 2;
////                    setFirstTurn(false);
//                }
//                else {
//                    score2 = score2 + 2;
////                    setFirstTurn(true);
//                }
//                finalScore = score1;
//                if (score2 > score1) {
//                    finalScore = score2;
//                }
                paired = paired - 2;
                if (paired == 0) {
//                    Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_LONG).show();
                    ScoreActivity.lock = false;
                    Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                    intent.putExtra("score", score1);
                    startActivity(intent);
                }
            }
            else {
                secondCard.getButton().setBackgroundDrawable(backImage);
                firstCard.getButton().setBackgroundDrawable(backImage);
//                if (isFirstTurn()) {
                    score1 = score1 - 1;
//                    setFirstTurn(false);
//                }
//                else {
//                    score2 = score2 - 1;
//                    setFirstTurn(true);
//                }
            }

            player1.setText("Player 1: " + score1);
//            player2.setText("Player 2: " + score2);

            firstCard  = null;
            secondCard = null;
        }
    }
}
