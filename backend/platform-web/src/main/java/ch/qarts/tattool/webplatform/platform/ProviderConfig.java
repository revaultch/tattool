package ch.qarts.tattool.webplatform.platform;

import lombok.Data;

import java.util.List;

@Data
public class ProviderConfig {

    private String hubUrl;

    private List<String> options;
}
