package ch.qarts.tattool.core.domain.command;

public interface PayloadFactory {

    Payload create(String payload);

}
