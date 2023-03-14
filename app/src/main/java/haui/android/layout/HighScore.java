package haui.android.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import haui.android.DatabaseHelper;
import haui.android.R;
import haui.android.model.User;

public class HighScore extends AppCompatActivity {
    ArrayList<User> topHighScore = new ArrayList();
    ListView highScoreLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        DatabaseHelper ref = new DatabaseHelper(HighScore.this);
        topHighScore = ref.getHighscoreList();
        highScoreLv = findViewById(R.id.hs_lv);

        System.out.println(topHighScore.size());

        int ranks[] = {R.drawable.rank_1, R.drawable.rank_2, R.drawable.rank_3, R.drawable.user_logo};
        HighScoreAdapterBase adapterHighScore = new HighScoreAdapterBase(HighScore.this, R.layout.activity_lv_high_score, topHighScore, ranks);
        highScoreLv.setAdapter(adapterHighScore);
    }
}