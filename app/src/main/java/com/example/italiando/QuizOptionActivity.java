package com.example.italiando;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.italiando.other.Constants;


public class QuizOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_option);


        CardView cvNumbers = findViewById(R.id.cvNumbers);
        CardView cvFoods = findViewById(R.id.cvFoods);
        CardView cvIntro = findViewById(R.id.cvIntro);
        CardView cvAnimals = findViewById(R.id.cvAnimals);

        findViewById(R.id.imageViewQuizOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cvNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizOptionActivity.this, GeographyOrLiteratureQuizActivity.class);
                intent.putExtra(Constants.SUBJECT, getString(R.string.numbers));
                startActivity(intent);
            }
        });

        cvFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizOptionActivity.this, GeographyOrLiteratureQuizActivity.class);
                intent.putExtra(Constants.SUBJECT,getString(R.string.foods));
                startActivity(intent);
            }
        });

        cvIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizOptionActivity.this, GeographyOrLiteratureQuizActivity.class);
                intent.putExtra(Constants.SUBJECT,getString(R.string.Intro));
                startActivity(intent);
            }
        });

        cvAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizOptionActivity.this, GeographyOrLiteratureQuizActivity.class);
                intent.putExtra(Constants.SUBJECT,getString(R.string.animals));
                startActivity(intent);
            }
        });

    }
}