package com.karacasoft.interestr;

import android.support.annotation.MenuRes;
import android.view.Menu;

/**
 * Created by karacasoft on 28.11.2017.
 */

public interface MenuHandler {
    public void changeMenu(@MenuRes int id);
    public Menu getMenu();
}
