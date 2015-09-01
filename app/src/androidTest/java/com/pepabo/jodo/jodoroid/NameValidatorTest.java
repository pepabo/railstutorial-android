package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

public class NameValidatorTest extends AndroidTestCase {
    FormItemValidator mValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mValidator = new NameValidator(getContext());
    }

    public void testValidationNormal() throws Exception {
        mValidator.validate("JohnDoe");
        assertFalse(mValidator.hasError());
    }

    public void testValidationAbnormal() throws Exception {
        mValidator.validate("");
        assertTrue(mValidator.hasError());
    }
}