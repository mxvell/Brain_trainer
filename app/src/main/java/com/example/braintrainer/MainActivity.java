package com.example.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private  TextView textViewQuestion;
    private TextView textViewScore;
    private  TextView textViewOption0;
    private  TextView textViewOption1;
    private  TextView textViewOption2;
    private  TextView textViewOption3;
    private ArrayList <TextView> options = new ArrayList<>();


   private String questions;
   private int rightAnswers;
   private int rightAnswerPosition;
   private boolean isPositive;
private int min = 5;
private int max = 30;
private int countOfQuestion = 0;
private int countOfRightAnswer = 0;
   private boolean gameOver = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewScore = findViewById(R.id.textViewScore);
        textViewOption0 = findViewById(R.id.textViewOpinion0);
        textViewOption1 = findViewById(R.id.textViewOpinion1);
        textViewOption2 = findViewById(R.id.textViewOpinion2);
        textViewOption3 = findViewById(R.id.textViewOpinion3);
        options.add(textViewOption0);
        options.add(textViewOption1);
        options.add(textViewOption2);
        options.add(textViewOption3);
        CountDownTimer timer  = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(getTime(l));
                if (l<10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                }
            }

            @Override
            public void onFinish() {
          gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max",0);
                if (countOfRightAnswer >= max){
                    preferences.edit().putInt("max",countOfRightAnswer).apply();
                }
                Intent intent = new Intent( MainActivity.this, MainActivity2.class);
                intent.putExtra("result",countOfRightAnswer);
                startActivity(intent);

            }
        };
        timer.start();
            generateQuestion();
            for (int i = 0; i <options.size(); i++){
                if (i == rightAnswerPosition){
                    options.get(i).setText(Integer.toString(rightAnswers));
                }else {
                    options.get(i).setText(Integer.toString(generateWrongAnswer()));
                }
            }
        }
        private void playNext(){
        generateQuestion();
            for (int i = 0; i <options.size(); i++){
                if (i == rightAnswerPosition){
                    options.get(i).setText(Integer.toString(rightAnswers));
                }else {
                    options.get(i).setText(Integer.toString(generateWrongAnswer()));
                }
            }
            String score = String.format("%s / %s", countOfRightAnswer , countOfQuestion);
            textViewScore.setText(score);

        }


    private  void generateQuestion() {
        int a = (int)(Math.random()*(max-min+1)+min);
        int b = (int)(Math.random()*(max-min+1)+min);
        int mark = (int)(Math.random()*2);
        isPositive = mark==1;
        if (isPositive){
            rightAnswers = a+b;
            questions = String.format("%s + %s",a,b);
        }else {
            rightAnswers = a-b;
            questions = String.format("%s - %s",a,b);
        }
        textViewQuestion.setText(questions);
        rightAnswerPosition = (int)(Math.random()*4);
    }
    private int generateWrongAnswer(){
        int result;
        do {
            result = (int)(Math.random()*max*2+1)-(max-min);
        }while (result ==rightAnswers);
         return result;
    }
    private String getTime (long mills){
        int seconds = (int) (mills / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds );
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {

        TextView textView = (TextView) view;
        String answer = textView.getText().toString();
        int choseAnswer = Integer.parseInt(answer);
        if (choseAnswer == rightAnswers) {
            countOfRightAnswer++;
            Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не правильно", Toast.LENGTH_SHORT).show();
        }
        countOfQuestion++;
        playNext();
        }
    }
}
