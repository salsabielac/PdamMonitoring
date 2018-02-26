package com.example.miranda.monitoringpdam.util;

import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.example.miranda.monitoringpdam.Navdrawer;

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

//    public void setNavigationViewListener(NavigationViewListener navigationViewListener) {
//        this.navigationViewListener = navigationViewListener;
//    }

//    public void setNavigationViewListener(NavigationViewListener navigationViewListener) {
//        this.navigationViewListener = navigationViewListener;
//    }

    public void setNavigationViewListener(Navdrawer navdrawer) {
        this.navigationViewListener = navigationViewListener;
    }

    public interface NavigationViewListener {
        void onNavigationViewMenuClicked(MenuItem item);
    }
}
