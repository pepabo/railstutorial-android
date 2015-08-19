package com.pepabo.jodo.jodoroid.dummy;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.net.URI;
import java.util.ArrayList;
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

    public static URI AVATAR = URI.create("http://www.gravatar.com/avatar/00000000000000000000000000000000");

    public static User TAKASHI = new User(0, "Takashi", AVATAR);
    public static User KAORI = new User(1, "Kaori", AVATAR);
    public static List<Micropost> HOME_TIMELINE = new ArrayList<Micropost>();

    static {
        HOME_TIMELINE.add(new Micropost(0, "pyoe-", TAKASHI, new GregorianCalendar(2015, 2, 23, 10, 50, 20).getTime(), null));
        HOME_TIMELINE.add(new Micropost(0, "uwaa-", KAORI, new GregorianCalendar(2015, 2, 23, 8, 40, 0).getTime(), null));
        HOME_TIMELINE.add(new Micropost(0, "pieee", TAKASHI, new GregorianCalendar(1950, 3, 10, 0, 0, 0).getTime(), null));
    }
}
