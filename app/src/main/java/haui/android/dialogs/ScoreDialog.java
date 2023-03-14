package haui.android.dialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import haui.android.App;
import haui.android.DatabaseHelper;
import haui.android.R;
import haui.android.manager.DatabaseManager;


public class ScoreDialog extends Dialog implements View.OnClickListener {
    private int score;
    private EditText edtName;
    private TextView tvScore;

    public ScoreDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.score_dialog);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        edtName = (EditText) findViewById(R.id.edt_name);
        tvScore = (TextView) findViewById(R.id.tv_score);
        findViewById(R.id.btn_score_cancel).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

    }

    public void setScore(String score) {
        tvScore.setText(score + " VNĐ");
        this.score = Integer.parseInt(score.replaceAll(",", ""));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_score_cancel) {
            dismiss();
        }
        if(v.getId() == R.id.btn_save){
            if (edtName.getText().toString().isEmpty()) {
                return;
            }
            DatabaseManager databaseManager = new DatabaseManager(App.getContext());
            ContentValues values = new ContentValues();
            values.put("username", edtName.getText().toString().trim());
            values.put("score", score);
            databaseManager.insert("USER", values);
            dismiss();

        }
    }
}
