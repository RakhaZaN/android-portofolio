package com.belajar.newportofolio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {

    TextView dop, din1;
    EditText din;
    String bil, df;
    int op;
    double hasil, num, str;
    MediaPlayer mp;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        bil = "0";
        din = findViewById(R.id.dInput);
        dop = findViewById(R.id.operation);
        din1 = findViewById(R.id.dBil);

        din.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (din.hasFocus()) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }

    public void btn1Clicked(View v) {
        displayInput("1");
        play("beep");
    }
    public void btn2Clicked(View v) {
        displayInput("2");
        play("dart");
    }
    public void btn3Clicked(View v) {
        displayInput("3");
        play("drum");
    }
    public void btn4Clicked(View v) {
        displayInput("4");
        play("drum");
    }
    public void btn5Clicked(View v) {
        displayInput("5");
        play("beep");
    }
    public void btn6Clicked(View v) {
        displayInput("6");
        play("dart");
    }
    public void btn7Clicked(View v) {
        displayInput("7");
        play("dart");
    }
    public void btn8Clicked(View v) {
        displayInput("8");
        play("drum");
    }
    public void btn9Clicked(View v) {
        displayInput("9");
        play("beep");
    }
    public void btn0Clicked(View v) {
        displayInput("0");
        play("drum");
    }
    public void btn00Clicked(View v) {
        displayInput("00");
        play("dart");
    }
    public void btnTitikClicked(View v) {
        displayInput(".");
        play("beep");
    }

    public void btnTambahClicked(View v) {
        operation("+");
        play("oHiHat");
    }
    public void btnKurangClicked(View v) {
        operation("-");
        play("kickDrum");
    }
    public void btnKaliClicked(View v) {
        operation("x");
        play("snare");
    }
    public void btnBagiClicked(View v) {
        operation("/");
        play("cHiHat");
    }

    public void btnSamadenganClicked(View v) {
        calculate();
        play("gun");
    }
    public void btnClearClicked(View v) {
        clear();
        play("alien");
    }

    private void clear() {
        bil = "0";
        hasil = 0;
        op = 0;
        str = 0;
        din.setText(bil);
        dop.setText("");
        din1.setText("");
    }

    public void btnBackwordClicked (View v) {
        String[] arrOfbw = String.valueOf(din.getText()).split("",0);
        bil = "";
        for (int i = 0; i < arrOfbw.length -1; i++) {
            bil += arrOfbw[i];
        }
        if (bil.equals("")) {
            din.setText("0");
        } else {
            din.setText(bil);
        }
        play("spray");

    }

    public void displayInput(String in) {
        if (bil.equals("0") | bil.equals("")) {
            bil = in;
        } else {
            bil += in;
        }
        din.setText(bil);
    }

    public void operation(String operation) {
        str = Double.parseDouble(String.valueOf(din.getText()));
        din1.setText(displayFormat(str));
        bil = "";
        op = 0;

        switch (operation) {
            case "+":
                dop.setText("+");
                op = 1;
                break;
            case "-":
                dop.setText("-");
                op = 2;
                break;
            case "x":
                dop.setText("x");
                op = 3;
                break;
            case "/":
                dop.setText("/");
                op = 4;
                break;
            default:
                clear();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void calculate() {

        num = Double.parseDouble(bil);

        switch (op) {
            case 1:
                hasil = str + num;
                break;
            case 2:
                hasil = str - num;
                break;
            case 3:
                hasil = str * num;
                break;
            case 4:
                hasil = str / num;
                break;
        }

        din1.setText(displayFormat(str) + dop.getText() + din.getText());
        dop.setText("=");
        din.setText(displayFormat(hasil));
    }

    public String displayFormat(Double d) {
        df ="";
        String[] display = Double.toString(d).split("",0);
        if (display[display.length - 1].equals("0")) {
            for (int i = 1; i < display.length - 2; i++) {
                df += display[i];
            }
        } else {
            for (int i = 1; i < display.length; i++) {
                df += display[i];
            }
        }
        return df;
    }

    public void play(String song) {
        if (mp == null) {

            switch (song) {
                case "cHiHat":
                    mp = MediaPlayer.create(this, R.raw.musical_human_beatbox_closed_hi_hat);
                    break;
                case "oHiHat":
                    mp = MediaPlayer.create(this, R.raw.musical_human_beatbox_open_hi_hat);
                    break;
                case "kickDrum":
                    mp = MediaPlayer.create(this, R.raw.musical_human_beatbox_kick_drum);
                    break;
                case "snare":
                    mp = MediaPlayer.create(this, R.raw.musical_human_beatbox_snare_drum);
                    break;
                case "alien":
                    mp = MediaPlayer.create(this, R.raw.science_fiction_alien_creature_growl_002);
                    break;
                case "spray":
                    mp = MediaPlayer.create(this, R.raw.zapsplat_leisure_can_silly_string_spray_short_46149);
                    break;
                case "beep":
                    mp = MediaPlayer.create(this, R.raw.zapsplat_multimedia_notification_short_digital_futuristic_beep_generic_010_53946);
                    break;
                case "drum":
                    mp = MediaPlayer.create(this, R.raw.zapsplat_musical_drum_tom_set_down_005_54847);
                    break;
                case "dart":
                    mp = MediaPlayer.create(this, R.raw.zapsplat_sport_dart_hit_dartboard_003_13081);
                    break;
                case "gun":
                    mp = MediaPlayer.create(this, R.raw.zapsplat_warfare_bullet_whizz_hit_ground_dirt_small_stones_debris_006_43719);
                    break;
            }

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop();
                }
            });
        }
        mp.start();
    }

    private void stop() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }
}