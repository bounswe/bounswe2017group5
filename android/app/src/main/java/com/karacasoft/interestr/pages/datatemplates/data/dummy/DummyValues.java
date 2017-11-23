package com.karacasoft.interestr.pages.datatemplates.data.dummy;

import com.karacasoft.interestr.pages.datatemplates.data.Template;
import com.karacasoft.interestr.pages.datatemplates.data.TemplateField;

/**
 * Created by karacasoft on 23.11.2017.
 */

public class DummyValues {

    public static Template getDummyTemplate() {
        Template t = new Template();

        TemplateField field1 = new TemplateField();
        field1.setName("Template1");
        field1.setHint("TemplateHint1");
        field1.setType(TemplateField.Type.SHORT_TEXT);
        TemplateField field2 = new TemplateField();
        field2.setName("Template2");
        field2.setHint("TemplateHint2");
        field2.setType(TemplateField.Type.SHORT_TEXT);
        TemplateField field3 = new TemplateField();
        field3.setName("BooleanField");
        field3.setType(TemplateField.Type.BOOLEAN);

        t.getFields().add(field1);
        t.getFields().add(field2);
        t.getFields().add(field3);

        return t;
    }

}
