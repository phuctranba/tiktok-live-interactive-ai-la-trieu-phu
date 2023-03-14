package haui.android.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import haui.android.App;
import haui.android.R;


public class CallDialog extends Dialog implements View.OnClickListener {
    private TextView tvAnswer;
    private LinearLayout answerLayout;
    private RelativeLayout callsLayout;


    private String trueAnswer;

    public CallDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(R.layout.call_dialog);

        answerLayout = (LinearLayout) findViewById(R.id.ln_answer);
        callsLayout = (RelativeLayout) findViewById(R.id.rl_calls);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);

        findViewById(R.id.btn_close).setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getAnswer();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;

        }
    }

    private void getAnswer() {
        App.getMusicPlayer().play(R.raw.call, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                callsLayout.removeAllViews();
                callsLayout.addView(answerLayout);
                answerLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setTrueAnswer(int trueAnswer) {
        if (trueAnswer == 1) {
            this.trueAnswer = "A";
        } else if (trueAnswer == 2) {
            this.trueAnswer = "B";
        } else if (trueAnswer == 3) {
            this.trueAnswer = "C";
        } else {
            this.trueAnswer = "D";
        }
        tvAnswer.setText("Theo tôi đáp án đúng là " + this.trueAnswer);
    }
}


