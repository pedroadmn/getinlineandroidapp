package com.androidapp.getinline.listener;

import android.view.View;

/**
 * Created by pedroadmn on 10/6/2016.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}