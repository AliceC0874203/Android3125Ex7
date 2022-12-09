package com.example.android_3125_ex7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android_3125_ex7.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    private Map<String, String> questionDict = new HashMap<String, String>();
    private ArrayList<Button> buttonArrayList = new ArrayList<Button>();

    String answer = "???";
    int points = 0;
    int lives = 5;
    CountDownTimer timer;
    int seconds = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questionDict.put("Pumpkin", "🎃");
        questionDict.put("Panda", "🐼");
        questionDict.put("Fox", "🦊");
        questionDict.put("Mice", "🐭");
        questionDict.put("Dog", "🐶");
        questionDict.put("Cat", "🐱");
        questionDict.put("Ghost", "👻");
        questionDict.put("Maple", "🍁");
        questionDict.put("Robot", "🤖");
        questionDict.put("Brain", "🧠");

        buttonArrayList.add(binding.btn1);
        buttonArrayList.add(binding.btn2);
        buttonArrayList.add(binding.btn3);
        buttonArrayList.add(binding.btn4);

        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.btn3.setOnClickListener(this);
        binding.btn4.setOnClickListener(this);
        binding.btnStart.setOnClickListener(this);
    }

    private void runTimer() {
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (seconds > 0) {
                    seconds -= 1;
                } else {
                    timeout();
                }

                binding.textTiming.setText("Timing: "+seconds);
            }

            public void onFinish() {
                timer.start();
            }
        }.start();
    }

    private void resetTimer() {
        seconds = 10;
        binding.textTiming.setText("");
    }

    private void timeout() {
        resetTimer();
        answerAction(null);
    }

    private void updateQuestion() {
        //SET TEXT QS GET ANSWER
        List keys = new ArrayList(questionDict.keySet());
        List values = new ArrayList(questionDict.values());
        int randomInt = randomInt(0,keys.size()-1);
        answer = (String) keys.get(randomInt);
        String question = (String) values.get(randomInt);
        binding.textQS.setText(question);


        //SET QS BUTTONS
        int randomNumber = randomInt(0,3);
        Map<String, String> tempArray = new HashMap<>(questionDict);

        for (int i = 0; i <= 3; i++) {
            if (randomNumber == i) { //SET CORRECT ANSWER BUTTON
                buttonArrayList.get(i).setText(answer);
                tempArray.remove(answer);
            } else { // SET DUMMY ANSWER
                List keys2 = new ArrayList(tempArray.values());
                String randomQuestionKey = (String) keys.get(randomInt(0,keys2.size()-1));
                buttonArrayList.get(i).setText(randomQuestionKey);
                tempArray.remove(randomQuestionKey);
            }
        }

        resetTimer();
    }

    void restart() {
        lives = 5;
        points = 0;
        binding.textPoint.setText("Points: " + points);
        binding.textLives.setText("Lives: " + lives) ;
        updateQuestion();
        runTimer();
    }

    private int randomInt(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnStart) {
            restart();
        } else {
            answerAction(v);
        }
    }

    void answerAction(View v) {
        if (v == null) {
            lives -= 1;
            binding.textLives.setText("Lives: " + lives) ;
            checkLives();
            return;
        }

        Button b = (Button) v;
        if (b.getText().toString() == answer) {
            points += 1;
            binding.textPoint.setText("Points: " + points);
        } else {
            lives -= 1;
            binding.textLives.setText("Lives: " + lives) ;
        }

        checkLives();
    }

    void checkLives() {
        if (lives <= 0) {
            timer.cancel();

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Game Over");
            builder.setMessage("Do you wanna play again?");

            // add the buttons
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restart();
                }
            });
            builder.setNegativeButton("NO", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            updateQuestion();
        }
    }
}