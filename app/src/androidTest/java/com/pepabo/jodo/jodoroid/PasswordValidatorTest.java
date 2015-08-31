package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

public class PasswordValidatorTest extends AndroidTestCase {

    public void testValidationNormal() throws Exception {

        PasswordValidator ev = new PasswordValidator("abcdef");
        ev.validate(getContext());

        assertFalse(ev.hasError());
    }

    public void testValidationAbnormal() throws Exception {
        PasswordValidator ev = new PasswordValidator("abcde");
        ev.validate(getContext());

        assertTrue(ev.hasError());
    }
}