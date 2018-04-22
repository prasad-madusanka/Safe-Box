package com.prasad.safeboxApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Prasad on 1/16/17.
 */

public class registerDone extends AppCompatActivity {

    private ImageView imageView;

    public void onBackPressed() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_complete);

            imageView = (ImageView) findViewById(R.id.fingerPrint);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(registerDone.this, requestPassword.class));
                    overridePendingTransitionEnter();

                }
            });

    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

}
