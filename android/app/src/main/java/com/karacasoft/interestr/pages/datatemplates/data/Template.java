package com.karacasoft.interestr.pages.datatemplates.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by karacasoft on 22.11.2017.
 */

public class Template implements Serializable {

    private static final String FIELD_BOOLEAN = "checkbox";
    private static final String FIELD_MULTIPLE_SELECT = "multisel";
    private static final String FIELD_LONG_TEXT = "textarea";
    private static final String FIELD_SHORT_TEXT = "text";
    private static final String FIELD_NUMERIC = "number";
    private static final String FIELD_EMAIL = "email";


    private String name;
    private ArrayList<TemplateField> fields = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TemplateField> getFields() {
        return fields;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        try {
            for (TemplateField field : getFields()) {
                JSONObject fieldObj = new JSONObject();
                switch (field.getType()) {
                    case SHORT_TEXT:

                        fieldObj.put("type", FIELD_SHORT_TEXT);
                        fieldObj.put("legend", field.getName());
                        fieldObj.put("inputs", new JSONArray());

                        break;
                    case LONG_TEXT:

                        fieldObj.put("type", FIELD_LONG_TEXT);
                        fieldObj.put("legend", field.getName());
                        fieldObj.put("inputs", new JSONArray());

                        break;
                    case EMAIL:

                        fieldObj.put("type", FIELD_EMAIL);
                        fieldObj.put("legend", field.getName());
                        fieldObj.put("inputs", new JSONArray());

                        break;
                    case NUMERIC:

                        fieldObj.put("type", FIELD_NUMERIC);
                        fieldObj.put("legend", field.getName());
                        fieldObj.put("inputs", new JSONArray());

                        break;
                    case BOOLEAN:

                        fieldObj.put("type", FIELD_BOOLEAN);
                        fieldObj.put("legend", field.getName());
                        fieldObj.put("inputs", new JSONArray());
                        break;
                    case MULTIPLE_CHOICE:

                        fieldObj.put("type", FIELD_MULTIPLE_SELECT);
                        fieldObj.put("legend", field.getName());

                        JSONArray choices = new JSONArray();

                        for (String choice :
                                ((MultipleChoiceTemplateField) field).getChoices()) {
                            JSONObject choiceObj = new JSONObject();
                            choiceObj.put("type", "checkbox");
                            choiceObj.put("label", choice);
                            choiceObj.put("side", "R");

                            choices.put(choiceObj);
                        }

                        fieldObj.put("inputs", choices);

                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return object;
    }

    public static Template fromJSON(JSONArray objects) {
        Template template = new Template();

        for (int i = 0; i < objects.length(); i++) {
            try {
                JSONObject field = objects.getJSONObject(i);

                TemplateField templateField = null;

                if(field.getString("type").equals(FIELD_BOOLEAN)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.BOOLEAN);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(FIELD_SHORT_TEXT)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.SHORT_TEXT);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(FIELD_LONG_TEXT)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.LONG_TEXT);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(FIELD_MULTIPLE_SELECT)) {
                    templateField = new MultipleChoiceTemplateField();
                    templateField.setType(TemplateField.Type.MULTIPLE_CHOICE);
                    templateField.setName(field.getString("legend"));
                    ArrayList<String> choices = ((MultipleChoiceTemplateField) templateField).getChoices();

                    JSONArray choicesArr = field.getJSONArray("inputs");

                    for (int j = 0; j < choicesArr.length(); j++) {
                        JSONObject choiceObj = choicesArr.getJSONObject(j);
                        choices.add(choiceObj.getString("label"));
                    }

                } else if(field.getString("type").equals(FIELD_NUMERIC)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.NUMERIC);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(FIELD_EMAIL)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.EMAIL);
                    templateField.setName(field.getString("legend"));
                } else {
                    Log.e("Template", "Field type not supported in Android yet.");
                    continue;
                }

                template.getFields().add(templateField);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return template;
    }


}
