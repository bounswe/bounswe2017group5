package com.karacasoft.interestr;

import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by karacasoft on 28.11.2017.
 */

public interface FloatingActionsMenuHandler {
    public FloatingActionMenu getFloatingActionsMenu();
    public void clearFloatingActionsMenu();
    public void hideFloatingActionsMenu();
    public void showFloatingActionsMenu();
}
