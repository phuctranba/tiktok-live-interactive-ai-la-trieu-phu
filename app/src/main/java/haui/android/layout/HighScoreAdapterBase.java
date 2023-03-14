package haui.android.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import haui.android.R;
import haui.android.model.User;

public class HighScoreAdapterBase extends BaseAdapter {
    Context cxt;
    int layoutID, ranks[];
    ArrayList<User> topHighScore;
    LayoutInflater inflater;

    public HighScoreAdapterBase(Context cxt, int layoutID, ArrayList<User> topHighScore, int ranks[]){
        this.cxt = cxt;
        this.layoutID = layoutID;
        this.topHighScore = topHighScore;
        this.ranks = ranks;
        inflater = LayoutInflater.from(cxt);
    }

    @Override
    public int getCount() {
        return topHighScore.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        DecimalFormat df2 = new DecimalFormat( "#,###,###,###" );

        convertView = inflater.inflate(R.layout.activity_lv_high_score, null);
        ImageView rankImg = convertView.findViewById(R.id.user_rank_img);
        TextView userName = convertView.findViewById(R.id.user_name);
        TextView userScore = convertView.findViewById(R.id.user_score);

        switch (i){
            case 0:
                rankImg.setImageResource(ranks[0]);
                break;
            case 1:
                rankImg.setImageResource(ranks[1]);
                break;
            case 2:
                rankImg.setImageResource(ranks[2]);
                break;
            default:
                rankImg.setImageResource(ranks[3]);
        }

        userName.setText(topHighScore.get(i).getUsername() + "");
        userScore.setText(df2.format(topHighScore.get(i).getScore()) + " VND");
        return convertView;
    }
}
