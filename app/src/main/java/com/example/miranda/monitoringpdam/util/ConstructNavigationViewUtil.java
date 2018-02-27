package com.example.miranda.monitoringpdam.util;

import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import butterknife.ButterKnife;

public class ConstructNavigationViewUtil {
    private NavigationViewListener navigationViewListener;

    public ConstructNavigationViewUtil(NavigationView view) {
        ButterKnife.bind(this, view.getHeaderView(0));

        view.setNavigationItemSelectedListener(item -> {
            navigationViewListener.onNavigationViewMenuClicked(item);
            return false;
        });
    }

    public void setNavigationViewListener(NavigationViewListener navigationViewListener) {
        this.navigationViewListener = navigationViewListener;
    }

    public interface NavigationViewListener {
        void onNavigationViewMenuClicked(MenuItem item);
    }
}
