package com.moomen.chessrays.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moomen.chessrays.R;
import com.moomen.chessrays.utils.PreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        /*View decorViewFull = getWindow().getDecorView();
        decorViewFull.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceUtils.getName(SplashActivity.this) != null && !PreferenceUtils.getName(SplashActivity.this).equals("")) {
                    //Check type user
                    //String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //checkUserTypeToSignIn(userID);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                }
                finish();

                /*if (PreferenceUtils.getEmail(com.moomen.news.ui.Activity.SplashActivity.this) != null && !PreferenceUtils.getEmail(com.moomen.news.ui.Activity.SplashActivity.this).equals("")) {
                    //Check type user
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    checkUserTypeToSignIn(userID);
                } else {
                    startActivity(new Intent(com.moomen.news.ui.Activity.SplashActivity.this, LoginRegisterActivity.class));
                    finish();
                }*/
            }
        }, 3000);
    }

   /* private void checkUserTypeToSignIn(String userID) {
        DocumentReference df = firebaseFirestore.collection("Users").document(userID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("user"))
                        startActivity(new Intent(com.moomen.news.ui.Activity.SplashActivity.this, MainActivity.class));
                    else if (userType.equals("company"))
                        startActivity(new Intent(com.moomen.news.ui.Activity.SplashActivity.this, MainActivityCompany.class));
                    else if (userType.equals("admin"))
                        startActivity(new Intent(com.moomen.news.ui.Activity.SplashActivity.this, MainActivityAdmin.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(com.moomen.news.ui.Activity.SplashActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
