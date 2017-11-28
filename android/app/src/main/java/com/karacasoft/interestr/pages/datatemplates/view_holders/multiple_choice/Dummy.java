package com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice;

import java.util.ArrayList;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class Dummy {

    public static ArrayList<ChoiceItem> getDummyData() {
        ArrayList<ChoiceItem> choices = new ArrayList<>();

        ChoiceItem item1 = new ChoiceItem();
        item1.setName("wow choice");

        ChoiceItem item2 = new ChoiceItem();
        item2.setName("wow choice");

        ChoiceItem item3 = new ChoiceItem();
        item3.setName("wow choice");

        choices.add(item1);
        choices.add(item2);
        choices.add(item3);

        return choices;
    }

}
