package tv.accedo.colourmemory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tv.accedo.colourmemory.database.DataSource;

public class ScoreActivity extends AppCompatActivity {

    private View inputView;
    private View scoreView;

    private ListView rank;
    private EditText name;
    private Button done;
    private int score;
    private RankingAdapter adapter;
    protected DataSource dataSource;
    private ArrayList<Player> players;
    public static boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        inputView = findViewById(R.id.input_view);
        scoreView = findViewById(R.id.score_view);

        name = (EditText) findViewById(R.id.input_name);
        done = (Button) findViewById(R.id.done);
        rank = (ListView) findViewById(R.id.ranking);
        dataSource = new DataSource(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty()) {
                    Player player = new Player(name.getText().toString(), score);
                    dataSource.createTaskData(player);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        players= dataSource.findAllTasks();
                        Collections.sort(players, new Comparator<Player>(){
                            public int compare(Player m1, Player m2) {
                                return m1.getScore() - m2.getScore(); // sort order
                            }
                        });
                        Collections.reverse(players);
                        adapter= new RankingAdapter(getApplicationContext(), R.layout.row_item, players);
                        rank.setAdapter(adapter);
                        inputView.setVisibility(View.GONE);
                        scoreView.setVisibility(View.VISIBLE);
                    }
                    lock = true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Name must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();

        score = getIntent().getExtras().getInt("score");
        if (score < 1000) {
            if (!lock) {
                inputView.setVisibility(View.VISIBLE);
                scoreView.setVisibility(View.GONE);
            }
            else {
                inputView.setVisibility(View.GONE);
                scoreView.setVisibility(View.VISIBLE);
                players= dataSource.findAllTasks();
                Collections.sort(players, new Comparator<Player>(){
                    public int compare(Player m1, Player m2) {
                        return m1.getScore() - m2.getScore(); // sort order
                    }
                });
                Collections.reverse(players);
                adapter= new RankingAdapter(getApplicationContext(), R.layout.row_item, players);
                rank.setAdapter(adapter);
            }
        }
        else {
//            ArrayList<Player> Players = new ArrayList<>();
//            Players.add(new Player("Kim", 4));
//            Players.add(new Player("Harold", 0));
//            Players.add(new Player("Gabiana", -1));
//            Players.add(new Player("Kimura", -7));
//            Players.add(new Player("Kimshin", 7));
            players= dataSource.findAllTasks();
            Collections.sort(players, new Comparator<Player>(){
                public int compare(Player m1, Player m2) {
                    return m1.getScore() - m2.getScore(); // sort order
                }
            });
            Collections.reverse(players);
            adapter= new RankingAdapter(getApplicationContext(), R.layout.row_item, players);
            rank.setAdapter(adapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
    }
}
