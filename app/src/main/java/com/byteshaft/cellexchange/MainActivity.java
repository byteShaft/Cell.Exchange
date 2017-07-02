package com.byteshaft.cellexchange;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.byteshaft.cellexchange.fragments.WelcomeFragment;
import com.byteshaft.cellexchange.utils.Helpers;

public class MainActivity extends FragmentActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        Helpers.loadFragment(fragmentManager, new WelcomeFragment(), "WelcomeFragment");
    }

}
