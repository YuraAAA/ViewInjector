package com.aizenberg.support;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.aizenberg.support.viewinjector.annotation.Id;
import com.aizenberg.support.viewinjector.annotation.Layout;

/**
 * Created by Yuriy Aizenberg
 */
@Layout(R.layout.main_fragment)
public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    @Id(value = R.id.progressBar, clickMethod = "progressClick")
    private ProgressBar progressBar;

    public void progressClick(View view) {
        Log.d(TAG, "progressClick: ");
    }
}
