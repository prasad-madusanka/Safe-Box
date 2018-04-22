package com.prasad.safeboxApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * Created by Prasad on 1/2/17.
 */

public class vaultViewPager extends AppCompatActivity{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;
    private ImageButton vault_add,vault_view,vault_delete;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(vaultViewPager.this,menu.class));
        overridePendingTransitionExit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vault_view_pager);

        try {

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            layouts = new int[]{
                    R.layout.vault_add_view_child,
                    R.layout.vault_showpassword_view_child,
                    R.layout.vault_remove_view_child
            };

            viewPagerAdapter = new ViewPagerAdapter();
            viewPager.setAdapter(viewPagerAdapter);


        }catch (Exception ex){
            Log.d("vaultViewPager_onC",ex.toString());
        }
    }

    protected class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);


            if (position==0) {
                vault_add = (ImageButton) findViewById(R.id.vault_openAddActivity);
                vault_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(vaultViewPager.this, vaultAddNew.class));
                        overridePendingTransitionEnter();
                    }
                });
            }else if(position==1){
                vault_view = (ImageButton) findViewById(R.id.vault_view_vault);
                vault_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(vaultViewPager.this,vaultContentDisplay.class)); //vaultContentDisplay
                        overridePendingTransitionEnter();
                    }
                });
            }else if(position==2){
                vault_delete = (ImageButton) findViewById(R.id.vault_delete);
                vault_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(vaultViewPager.this, vaultDeleteEntries.class));
                        overridePendingTransitionEnter();
                    }
                });
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }




    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
