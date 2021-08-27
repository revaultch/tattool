package ch.qarts.tattool.client.junit.runner;

import org.junit.jupiter.api.DynamicContainer;

import java.util.stream.Stream;

public interface DynamicContainerStreamProvider {

    Stream<DynamicContainer> dynamicContainers();

}
