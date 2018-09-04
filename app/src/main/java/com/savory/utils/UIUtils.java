package com.savory.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.savory.R;

public class UIUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void loadActionBarIcon(Menu menu, int itemId, Icon icon, Context context) {
        loadMenuIcon(menu, itemId, icon, context, R.color.white);
    }

    private static void loadMenuIcon(Menu menu, int itemId, Icon icon, Context context, @ColorRes int colorId) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(context, icon)
                        .colorRes(colorId)
                        .actionBarSize());
    }

    public static void showShortToast(@StringRes int stringId, Context context) {
        showToast(stringId, Toast.LENGTH_SHORT, context);
    }

    public static void showLongToast(@StringRes int stringId, Context context) {
        showToast(stringId, Toast.LENGTH_LONG, context);
    }

    private static void showToast(@StringRes int stringId, int length, Context context) {
        Toast.makeText(context, stringId, length).show();
    }
}
