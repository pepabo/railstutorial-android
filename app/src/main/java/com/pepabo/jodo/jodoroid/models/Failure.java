package com.pepabo.jodo.jodoroid.models;

import java.util.List;

public class Failure {
    List<String> errors;

    Failure() {
    }

    Failure(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
