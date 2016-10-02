package com.fs.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fs.lib.R;

/**
 * Created by milton on 25/01/16.
 */
public class ViewHelper {

    public static void makeUnClickable(View view) {
        view.setClickable(false);
        view.setFocusable(false);
    }

    public static void addHover(View view) {
        int normal = R.drawable.btn_black;
        int pressed=R.drawable.btn_blue;
        Context context=view.getContext();
        StateListDrawable slDraw = new StateListDrawable();
        slDraw.addState(new int[] {android.R.attr.state_focused},
                context.getResources().getDrawable(normal));
        slDraw.addState(new int[] {android.R.attr.state_selected},
                context.getResources().getDrawable(pressed));

        slDraw.addState(new int[] {android.R.attr.state_pressed},
                context.getResources().getDrawable(pressed));

        slDraw.addState(new int[] {},
                context.getResources().getDrawable(normal));
        view.setBackgroundDrawable(slDraw);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static boolean isTouchedOutsideEditText(AppCompatActivity activity,MotionEvent event, View view){
        boolean returnValue=false;
        if (view instanceof EditText) {
            View w = activity.getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                return true;
            }
        }
        return returnValue;
    }

    public static String toggleTextViewEditText(RelativeLayout parentView, int textViewFieldNameId, int editTextFieldNameId) {
        String s="";
        TextView textViewFieldName= (TextView) parentView.findViewById(textViewFieldNameId);
        if (textViewFieldName.getVisibility()==View.VISIBLE) {
            textViewFieldName.setVisibility(View.INVISIBLE);

            EditText editTextFieldName= (EditText) parentView.findViewById(editTextFieldNameId);
            editTextFieldName.setVisibility(View.VISIBLE);
            s=textViewFieldName.getText().toString();
            editTextFieldName.setText(s);
            editTextFieldName.requestFocus();
            editTextFieldName.setBackgroundDrawable( parentView.getContext().getResources().getDrawable(R.drawable.rounded_rectangle_bg) );
            InputMethodManager imm = (InputMethodManager) parentView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextFieldName, InputMethodManager.SHOW_IMPLICIT);
        } else {
            textViewFieldName.setVisibility(View.VISIBLE);

            EditText editTextFieldName= (EditText) parentView.findViewById(editTextFieldNameId);
            editTextFieldName.setVisibility(View.INVISIBLE);
             s = editTextFieldName.getText().toString();
            textViewFieldName.setText(s);
            editTextFieldName.clearFocus();
            editTextFieldName.setBackgroundDrawable( null );

        }
        return s;

    }

    public static void printViewHierarchy(ViewGroup $vg, String $prefix)
    {
        for (int i = 0; i < $vg.getChildCount(); i++) {
            View v = $vg.getChildAt(i);
            String desc = $prefix + " | " + "[" + i + "/" + ($vg.getChildCount()-1) + "] "+ v.getClass().getSimpleName() + " " + v.getId();
            Log.v("x", desc);

            if (v instanceof ViewGroup) {
                printViewHierarchy((ViewGroup)v, desc);
            }
        }
    }




    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


}
