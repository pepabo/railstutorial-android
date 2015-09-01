package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

public class EmailValidatorTest extends AndroidTestCase {
    FormItemValidator mValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mValidator = new EmailValidator(getContext());
    }

    public void testValidationNormal() throws Exception {
        String[] validAddresses = {
                "user@example.com",
                "USER@foo.COM",
                "A_US-ER@foo.bar.org",
                "first.last@foo.jp",
                "alice+bob@baz.cn"
        };

        for (int i=0; i < validAddresses.length; i++) {
            mValidator.validate(validAddresses[i]);
            assertFalse(mValidator.hasError());
        }
    }

    public void testValidationAbnormal() throws Exception {
        String[] invalidAddresses = {
                "user@example,com",
                "user_at_foo.org",
                "user.name@example.",
                "foo@bar_baz.com",
                "foo@bar+baz.com",
                "foo@bar..com"
        };

        for (int i=0; i < invalidAddresses.length; i++) {
            mValidator.validate(invalidAddresses[i]);
            assertTrue(mValidator.hasError());
        }
    }
}