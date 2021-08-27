package ch.qarts.tattool.webplatform.core;

public class CommandExecutionFailedException extends RuntimeException {
    public CommandExecutionFailedException(String reason) {
        super(reason);
    }
}
