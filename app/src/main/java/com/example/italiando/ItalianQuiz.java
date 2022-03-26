package com.example.italiando;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.italiando.other.Constants;

import androidx.appcompat.app.AppCompatActivity;


public class ItalianQuiz extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private int correctQuestion = 0;
    private ImageView tvImageCard1, tvImageCard2, tvImageCard3, tvImageCard4;
    private TextView tvTextCard1, tvTextCard2, tvTextCard3, tvTextCard4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags_quiz);

        Intent intent = getIntent();
        String subject = intent.getStringExtra(Constants.SUBJECT);

        tvQuestion = findViewById(R.id.QuestionFlags);
        tvQuestionNumber = findViewById(R.id.QuestionNumberFlags);
        btnNext = findViewById(R.id.btnNextFlag);

        tvImageCard1 = findViewById(R.id.imageCard1);
        tvImageCard2 = findViewById(R.id.imageCard2);
        tvImageCard3 = findViewById(R.id.imageCard3);
        tvImageCard4 = findViewById(R.id.imageCard4);
        tvTextCard1 = findViewById(R.id.textCard1);
        tvTextCard2 = findViewById(R.id.textCard2);
        tvTextCard3 = findViewById(R.id.textCard3);
        tvTextCard4 = findViewById(R.id.textCard4);



    }
}
