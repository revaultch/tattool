package ch.qarts.tattool.webplatform.command;

public interface ExecutableCommandFactory {

    ExecutableCommand<?, ?> create(String payload);
}
