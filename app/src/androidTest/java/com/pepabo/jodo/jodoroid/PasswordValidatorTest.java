package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

public class PasswordValidatorTest extends AndroidTestCase {
    FormItemValidator mValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mValidator = new PasswordValidator(getContext());
    }

    public void testValidationNormal() throws Exception {
        mValidator.validate("abcdef");
        assertFalse(mValidator.hasError());
    }

    public void testValidationAbnormal() throws Exception {
        mValidator.validate("abcde");
        assertTrue(mValidator.hasError());
    }
}