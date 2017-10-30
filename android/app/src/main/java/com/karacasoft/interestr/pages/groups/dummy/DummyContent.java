package com.karacasoft.interestr.pages.groups.dummy;

import com.karacasoft.interestr.network.models.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Group> ITEMS = new ArrayList<Group>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Group> ITEM_MAP = new HashMap<String, Group>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Group item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId()), item);
    }

    private static Group createDummyItem(int position) {
        Group g = new Group();
        g.setId(position);
        g.setName("Group " + position);
        g.setDescription(makeDetails(position));
        g.setMemberCount(10);
        g.setPictureUrl(null);

        return g;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
