package com.ssh.core.utilities.results;

public class ErrorResult extends Result {
    public ErrorResult() {
        super(false);
    }

    public ErrorResult(String Message) {
        super(false, Message);
    }
}
