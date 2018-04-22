package com.prasad.safeboxApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prasad on 1/17/17.
 */

public class profile extends AppCompatActivity {

    private TextView username,email;
    private CircleImageView profilePicture;

    private SharedPreferences sharedPreferences;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(profile.this,menu.class));
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        try {
            username = (TextView) findViewById(R.id.profileName);
            email = (TextView) findViewById(R.id.profileEmail);
            profilePicture = (CircleImageView) findViewById(R.id.profile_image);

            sharedPreferences = getSharedPreferences("userProfile", Context.MODE_PRIVATE);
            username.setText(sharedPreferences.getString("username", ""));
            email.setText(sharedPreferences.getString("email", ""));
            Picasso.with(getApplicationContext()).load(sharedPreferences.getString("url", "")).into(profilePicture);
        } catch (Exception ex) {
        }
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}
