package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

public class NameValidatorTest extends AndroidTestCase {

    public void testValidationNormal() throws Exception {

        NameValidator ev = new NameValidator("JohnDoe");
        ev.validate(getContext());

        assertFalse(ev.hasError());
    }

    public void testValidationAbnormal() throws Exception {
        NameValidator ev = new NameValidator("");
        ev.validate(getContext());

        assertTrue(ev.hasError());
    }
}