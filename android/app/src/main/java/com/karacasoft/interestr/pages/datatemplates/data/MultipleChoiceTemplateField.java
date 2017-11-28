package com.karacasoft.interestr.pages.datatemplates.data;

import java.util.ArrayList;

/**
 * Created by karacasoft on 22.11.2017.
 */

public class MultipleChoiceTemplateField extends TemplateField {

    private ArrayList<String> choices = new ArrayList<>();

    public final ArrayList<String> getChoices() {
        return choices;
    }

}
