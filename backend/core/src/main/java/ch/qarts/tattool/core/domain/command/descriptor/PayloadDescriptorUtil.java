package ch.qarts.tattool.core.domain.command.descriptor;

import ch.qarts.tattool.core.domain.command.Payload;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PayloadDescriptorUtil {

    @SneakyThrows
    public static <T extends Payload> List<CommandFieldDescriptor> payloadDescriptor(Class<T> payloadClass) {
        var fields = Arrays.stream(payloadClass.getDeclaredFields()).
                filter(field -> !field.getName().equals("verb")).collect(Collectors.toList());

        return fields.stream().map(PayloadDescriptorUtil::mapField).collect(Collectors.toList());
    }

    private static CommandFieldDescriptor mapField(Field field) {
        return CommandFieldDescriptor.builder()
                .name(field.getName())
                .annotations(getAnnotationsAsMap(field))
                .build();
    }


    private static Map<String, Map<String, String>> getAnnotationsAsMap(Field field) {
        var commandFieldAnnotations = new HashMap<String, Map<String, String>>();
        if (field.isAnnotationPresent(Renderer.class)) {
            commandFieldAnnotations.put(Renderer.class.getSimpleName(), getAnnotationAsMap(field.getAnnotation(Renderer.class)));
        }
        if (field.isAnnotationPresent(Prepend.class)) {
            commandFieldAnnotations.put(Prepend.class.getSimpleName(), getAnnotationAsMap(field.getAnnotation(Prepend.class)));
        }
        if (field.isAnnotationPresent(Metadata.class)) {
            commandFieldAnnotations.put(Metadata.class.getSimpleName(), getAnnotationAsMap(field.getAnnotation(Metadata.class)));
        }
        if (field.isAnnotationPresent(Mappable.class)) {
            commandFieldAnnotations.put(Mappable.class.getSimpleName(), getAnnotationAsMap(field.getAnnotation(Mappable.class)));
        }
        if (field.isAnnotationPresent(DataProvided.class)) {
            commandFieldAnnotations.put(DataProvided.class.getSimpleName(), getAnnotationAsMap(field.getAnnotation(DataProvided.class)));
        }
        return commandFieldAnnotations;

    }

    private static Map<String, String> getAnnotationAsMap(Renderer annotation) {
        return Map.of("value", annotation.value());
    }

    private static Map<String, String> getAnnotationAsMap(Prepend annotation) {
        return Map.of("value", annotation.value());
    }

    private static Map<String, String> getAnnotationAsMap(Metadata annotation) {
        return Map.of("name", annotation.name(), "description", annotation.description());
    }

    private static Map<String, String> getAnnotationAsMap(Mappable annotation) {
        return Map.of();
    }

    private static Map<String, String> getAnnotationAsMap(DataProvided annotation) {
        return Map.of();
    }

}
