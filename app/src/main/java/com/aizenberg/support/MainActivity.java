package com.aizenberg.support;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aizenberg.support.viewinjector.ViewInjector;
import com.aizenberg.support.viewinjector.annotation.Id;

/**
 * Created by Yuriy Aizenberg
 */
public class MainActivity extends Activity {

    @Id(R.id.textView)
    private TextView textView;
    @Id(value = R.id.button, clickMethod = "btnClick")
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.reflect(this, this);
    }

    public void btnClick(View view) {
        Log.d(MainActivity.class.getSimpleName(), "Clicked");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, MainFragment.instantiate(this, MainFragment.class.getName()));
        fragmentTransaction.commit();
    }
}
