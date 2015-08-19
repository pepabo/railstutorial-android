package com.pepabo.jodo.jodoroid.dummy;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    private static URI AVATAR = URI.create("https://www.gravatar.com/avatar/00000000000000000000000000000000");

    private static User TAKASHI = new User(0, "Takashi", AVATAR);
    private static User KAORI = new User(1, "Kaori", AVATAR);
    private static List<Micropost> TAKASHI_POSTS = new ArrayList<>();
    private static List<Micropost> KAORI_POSTS = new ArrayList<>();
    private static List<Micropost> HOME_TIMELINE = new ArrayList<>();

    static {
        List<String> messages = Arrays.asList("pyoe-", "uwaa-", "pieee", "nyan!");
        for (int i = 0; i < 10; ++i) {
            TAKASHI_POSTS.add(new Micropost(100 + i, messages.get(i % messages.size()),
                    TAKASHI, new GregorianCalendar(2015, 2, 23, 10 + i, 50, 20).getTime(), null));
        }

        for (int i = 0; i < 10; ++i) {
            KAORI_POSTS.add(new Micropost(200 + i, messages.get(i % messages.size()),
                    KAORI, new GregorianCalendar(2015, 2, 23, 10 + i, 55, 20).getTime(), null));
        }

        HOME_TIMELINE.addAll(TAKASHI_POSTS);
        HOME_TIMELINE.addAll(KAORI_POSTS);
        Collections.sort(HOME_TIMELINE, new Comparator<Micropost>() {
            @Override
            public int compare(Micropost lhs, Micropost rhs) {
                return rhs.compareTo(lhs); // newer to older
            }
        });
    }

    public static List<Micropost> getHomeTimeline() {
        return HOME_TIMELINE;
    }

    public static List<Micropost> getUserTimeline(User user) {
        if(user == TAKASHI) return TAKASHI_POSTS;
        if(user == KAORI) return KAORI_POSTS;
        throw new RuntimeException("Unknown user");
    }

    public static User getUser(long id) {
        if(id == TAKASHI.getId()) return TAKASHI;
        if(id == KAORI.getId()) return KAORI;
        throw new RuntimeException("Unknown user");
    }
}
