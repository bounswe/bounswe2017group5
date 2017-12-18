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

    private static final String JSON_FIELD_CHECKBOX_KEY = "checkbox";
    private static final String JSON_FIELD_MULTISEL_KEY = "multisel";
    private static final String JSON_FIELD_TEXTAREA_KEY = "textarea";
    private static final String JSON_FIELD_TEXT_KEY = "text";
    private static final String JSON_FIELD_NUMBER_KEY = "number";
    private static final String JSON_FIELD_DATE_KEY = "date";
    private static final String JSON_FIELD_EMAIL_KEY = "email";
    private static final String JSON_FIELD_URL_KEY = "url";
    private static final String JSON_FIELD_TEL_KEY = "tel";


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

        JSONArray completeWasteOfFuckingMemoryAndNetworkBandwidth = new JSONArray();
        JSONObject wasteOfTime = new JSONObject();

        try {
            for (TemplateField field : getFields()) {
                JSONObject fieldObj = new JSONObject();
                switch (field.getType()) {
                    case TEXT:

                        fieldObj.put("type", JSON_FIELD_TEXT_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "text");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        fieldObj.put("inputs", completeWasteOfFuckingMemoryAndNetworkBandwidth);

                        break;
                    case TEXTAREA:

                        fieldObj.put("type", JSON_FIELD_TEXTAREA_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "textarea");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        fieldObj.put("inputs", completeWasteOfFuckingMemoryAndNetworkBandwidth);


                        break;
                    case EMAIL:

                        fieldObj.put("type", JSON_FIELD_EMAIL_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "email");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case NUMBER:

                        fieldObj.put("type", JSON_FIELD_NUMBER_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "number");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case CHECKBOX:

                        fieldObj.put("type", JSON_FIELD_CHECKBOX_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "checkbox");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case DATE:

                        fieldObj.put("type", JSON_FIELD_CHECKBOX_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "date");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case TEL:

                        fieldObj.put("type", JSON_FIELD_CHECKBOX_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "tel");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case URL:

                        fieldObj.put("type", JSON_FIELD_CHECKBOX_KEY);
                        fieldObj.put("legend", field.getName());

                        wasteOfTime.put("type", "url");

                        completeWasteOfFuckingMemoryAndNetworkBandwidth.put(wasteOfTime);

                        break;
                    case MULTISEL:

                        fieldObj.put("type", JSON_FIELD_MULTISEL_KEY);
                        fieldObj.put("legend", field.getName());

                        // in this case (ONLY) the inputs array is actually useful
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

                if(field.getString("type").equals(JSON_FIELD_CHECKBOX_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.CHECKBOX);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_TEXT_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.TEXT);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_TEXTAREA_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.TEXTAREA);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_MULTISEL_KEY)) {
                    templateField = new MultipleChoiceTemplateField();
                    templateField.setType(TemplateField.Type.MULTISEL);
                    templateField.setName(field.getString("legend"));
                    ArrayList<String> choices = ((MultipleChoiceTemplateField) templateField).getChoices();

                    JSONArray choicesArr = field.getJSONArray("inputs");

                    for (int j = 0; j < choicesArr.length(); j++) {
                        JSONObject choiceObj = choicesArr.getJSONObject(j);
                        choices.add(choiceObj.getString("label"));
                    }

                } else if(field.getString("type").equals(JSON_FIELD_NUMBER_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.NUMBER);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_EMAIL_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.EMAIL);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_DATE_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.DATE);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_URL_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.URL);
                    templateField.setName(field.getString("legend"));
                } else if(field.getString("type").equals(JSON_FIELD_TEL_KEY)) {
                    templateField = new TemplateField();
                    templateField.setType(TemplateField.Type.TEL);
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
