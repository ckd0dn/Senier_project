package com.gomsinpark.makeeat_java.Pop_recipe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gomsinpark.makeeat_java.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Pop_recipe_next_Activity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("popular_recipe");
    private ScrollView sc1;
    private String uri;
    private int count;
    boolean i = true;
    SharedPreferences pref;
    SharedPreferences.Editor editor;



    StarRecipe recipe = new StarRecipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_recipe_next_);

        TextView title = findViewById(R.id.next_title);
        TextView level = findViewById(R.id.next_level);
        TextView servings = findViewById(R.id.next_servings);
        TextView cook_step = findViewById(R.id.next_cook_step);
        TextView material = findViewById(R.id.next_material);
        TextView tag = findViewById(R.id.next_tag);
        TextView tip = findViewById(R.id.next_tip);
        TextView next_cook_step = findViewById(R.id.next_cook_step);
        final ImageView img = findViewById(R.id.next_img);
        final ImageButton star_btn = findViewById(R.id.star_btn);

        sc1 = findViewById(R.id.sc1);


        final Intent intent = getIntent();


        title.setText(intent.getStringExtra("title"));
        level.setText(intent.getStringExtra("Cooking_difficulty"));
        servings.setText(intent.getStringExtra("Servings"));
        cook_step.setText(intent.getStringExtra("cook_step"));
        material.setText(intent.getStringExtra("Material"));
        tag.setText(intent.getStringExtra("Tag"));
        next_cook_step.setText(intent.getStringExtra("Cook_Step"));

        uri = intent.getStringExtra("Filename");

        //????????? ??????????????? ???????????????
        if (intent.getStringExtra("Tip") != null) {
            tip.setText("Tip!  " + intent.getStringExtra("Tip"));
        }

        //?????????,???,???????????? ????????????
        tip.setMovementMethod(new ScrollingMovementMethod());
        next_cook_step.setMovementMethod(new ScrollingMovementMethod());


        //??????????????? ???????????? ?????? ??????????????? ???????????? ?????????????????? ???????????? ????????????
        next_cook_step.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sc1.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //?????? ???????????? ?????? ??????????????? ???????????? ?????????????????? ???????????? ????????????
        tip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sc1.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //????????? ?????????????????? ???????????? ?????????

        FirebaseStorage storage = FirebaseStorage.getInstance(); //???????????? ??????????????? ????????? ??????????????? ????????? ?????????
        final StorageReference storageRef = storage.getReference(); //??????????????? ????????????


        storageRef.child("img_folder/" + uri).getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //?????????
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(img);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //?????????
            }
        });


        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        count = pref.getInt("MyInt", 0);

        star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (i == true) {
                        star_btn.setImageResource(R.drawable.ic_baseline_yellowstar_24);
                        i = false;
                    } else {
                        star_btn.setImageResource(R.drawable.ic_sharp_star_border_24);
                        i = true;
                    }


                databaseReference.child("????????????").child(Integer.toString(count)).child("title").setValue(intent.getStringExtra("title"));
                databaseReference.child("????????????").child(Integer.toString(count)).child("cooking_time").setValue(intent.getStringExtra("cooking_time"));
                databaseReference.child("????????????").child(Integer.toString(count)).child("chef").setValue(intent.getStringExtra("chef"));
                databaseReference.child("????????????").child(Integer.toString(count)).child("views").setValue(intent.getStringExtra("views"));
                databaseReference.child("????????????").child(Integer.toString(count)).child("rank").setValue(intent.getStringExtra("rank"));
                databaseReference.child("????????????").child(Integer.toString(count)).child("filename").setValue(intent.getStringExtra("Filename"));

                editor.putInt("MyInt", count + 1);
                editor.apply();
            }
        });
    }
}