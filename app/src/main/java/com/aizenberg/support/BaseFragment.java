package com.aizenberg.support;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.support.viewinjector.ViewInjector;

/**
 * Created by Yuriy Aizenberg
 */
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjector.reflectLayout(this, inflater, container);
        afterViewsInjected(savedInstanceState);
        return view;

    }

    protected void afterViewsInjected(Bundle savedInstanceState) {

    }
}
