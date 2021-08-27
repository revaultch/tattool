package ch.qarts.tattool.webplatform.analysis;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AnalyzerSnapshot {

    private List<ElementSuperLocator> items = new ArrayList<>();

    private List<String> log = new ArrayList<>();

    public List<ElementSuperLocator> getClickable() {
        return getFilteredItems().stream().filter(item -> item.getTags().contains("click"))
                .collect(Collectors.toList());
    }

    public List<ElementSuperLocator> getWritable() {
        return getFilteredItems().stream().filter(item -> item.getTags().contains("write"))
                .collect(Collectors.toList());
    }

    public List<ElementSuperLocator> getSelectable() {
        return getFilteredItems().stream().filter(item -> item.getTags().contains("select"))
                .collect(Collectors.toList());

    }

    public List<ElementSuperLocator> getAssertable() {
        return getFilteredItems().stream().filter(item -> item.getTags().contains("write") || item.getTags().contains("select") || item.getTags().contains("text") || item.getTags().contains("assert"))
                .collect(Collectors.toList());
    }

    public List<ElementSuperLocator> getFilteredItems() {
        return this.items.stream().filter(elementSuperLocator -> StringUtils.isNotEmpty(elementSuperLocator.getScreenshot())).collect(Collectors.toList());
    }
}
