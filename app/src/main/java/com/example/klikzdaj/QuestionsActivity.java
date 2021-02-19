package com.example.klikzdaj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class QuestionsActivity extends AppCompatActivity {

    public static final String FILE_NAME = "KLIKZDAJ";
    public static final String KEY_NAME = "QUESTIONS";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView question,noIndicator;
    private ImageView question_photo;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button previousBtn;
    private Button nextBtn;
    private int count = 0;
    private List<QuestionModel> list;
    private int position = 0;
    private int score = 0;
    private String category;
    private int setNo;
    private Dialog loadingDialog;
    private int prvs = 1;

    private List<QuestionModel> bookmarksList;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionPosition;
    private TextView tekst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Toolbar toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        question_photo = findViewById(R.id.question_photo);
        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        bookmarkBtn = findViewById(R.id.bookmark_btn);
        optionsContainer = findViewById(R.id.options_container);
        //previousBtn = findViewById(R.id.previous_btn);
        nextBtn = findViewById(R.id.next_btn);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelMatch()){
                    bookmarksList.remove(matchedQuestionPosition);
                    bookmarkBtn.setImageDrawable((getDrawable(R.drawable.bookmark_border)));
                }else{
                    bookmarksList.add(list.get(position));
                    bookmarkBtn.setImageDrawable((getDrawable(R.drawable.bookmark)));
                }
            }
        });




        list = new ArrayList<>();

        //previousBtn.setEnabled(true);

        /**previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position > 0) {
                    position--;
                    playAnim(question,0,list.get(position).getQuestion());
                }
                else{
                    Toast.makeText(QuestionsActivity.this, category, Toast.LENGTH_LONG).show();
                    //Toast.makeText(QuestionsActivity.this,"To Twoje pierwsze pytanie", Toast.LENGTH_SHORT).show();
                }

                count = 0;

            }
        });**/

        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo",1);

        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(QuestionModel.class));
                }

                if(list.size() > 0){


                    for(int i = 0; i <4; i++){
                        optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            };
                        });
                        loadingDialog.dismiss();
                    }

                    playAnim(question,0,list.get(position).getQuestion());


                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.4f);
                            position++;
                            enableOption(true);
                            if (position == list.size()){
                                Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();

                                return;
                            }
                            count = 0;
                            playAnim(question,0,list.get(position).getQuestion());

                        }
                    });

                }else{
                    finish();
                    Toast.makeText(QuestionsActivity.this,"No questions", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }

        });






    }

    @Override
    protected void onPause(){
        super.onPause();
        storeBookmarks();
    }

    private void playAnim(View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4){
                    String option = "";
                    String source = "";
                    if (count == 0) {
                        source = list.get(position).getSrc();
                        Picasso.get().load(source).into(question_photo);
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();
                    }else if (count == 2) {
                        option = list.get(position).getOptionC();
                    }else if (count == 3) {
                        option = list.get(position).getOptionD();
                    }

                    playAnim(optionsContainer.getChildAt(count),0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                // data change
                if(value == 0){
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText(position+1+"/"+list.size());
                        if(modelMatch()){
                            bookmarkBtn.setImageDrawable((getDrawable(R.drawable.bookmark)));
                        }else{
                            bookmarkBtn.setImageDrawable((getDrawable(R.drawable.bookmark_border)));
                        }
                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



    }


    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectANS())){
            //poprawna
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4E9E31")));
        }else{
            //niepoprawna
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F64848")));
            Button correctoption = (Button)optionsContainer.findViewWithTag(list.get(position).getCorrectANS());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4E9E31")));
        }
    }

    private void enableOption(boolean enable){
        for(int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00ADFE")));
            }
        }
    }

    private void getBookmarks(){
       String json = preferences.getString(KEY_NAME,"");

       Type type = new TypeToken<List<QuestionModel>>(){}.getType();

       bookmarksList = gson.fromJson(json,type);

       if(bookmarksList == null){
           bookmarksList = new ArrayList<>();
       }
    }

    private boolean modelMatch(){
        boolean matched = false;
        int i = 0;
        for(QuestionModel model : bookmarksList){
            if (model.getQuestion().equals(list.get(position).getQuestion())
            && model.getCorrectANS().equals(list.get(position).getCorrectANS())
            && model.getSetNo() == list.get(position).getSetNo()){
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storeBookmarks(){

        String json = gson.toJson(bookmarksList);

        editor.putString(KEY_NAME,json);
        editor.commit();

    }


}