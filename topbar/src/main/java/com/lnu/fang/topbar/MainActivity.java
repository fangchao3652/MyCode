package com.lnu.fang.topbar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lnu.fang.topbar.view.TopBar;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setLeftVisible(false);
        topBar.setOnTopBarClickListener(new TopBar.topBarClickListener() {
            @Override
            public void leftClick() {
                System.out.println("点击  back");
            }

            @Override
            public void rightClick() {
                System.out.println("点击  more");
                System.out.println("getFilesDir()==="+MainActivity.this.getFilesDir());
                System.out.println("getCacheDir()==="+MainActivity.this.getCacheDir());
                System.out.println("getExternalFilesDir()==="+MainActivity.this.getExternalFilesDir("fangchao.txt"));
                System.out.println("getExternalCacheDir()==="+MainActivity.this.getExternalCacheDir());

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
