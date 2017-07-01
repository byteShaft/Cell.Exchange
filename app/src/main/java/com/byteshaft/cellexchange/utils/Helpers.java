package com.byteshaft.cellexchange.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.byteshaft.cellexchange.R;

/**
 * Created by fi8er1 on 01/07/2017.
 */

public class Helpers {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void loadFragment(FragmentManager fragmentManager, android.support.v4.app.Fragment fragment, String fragmentName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_transition_fragment_slide_right_enter, R.anim.anim_transition_fragment_slide_left_exit,
                R.anim.anim_transition_fragment_slide_left_enter, R.anim.anim_transition_fragment_slide_right_exit);
        transaction.add(R.id.container_main, fragment).addToBackStack(fragmentName);
        transaction.commit();
    }
}
