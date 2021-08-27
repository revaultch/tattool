package ch.qarts.tattool.webplatform.intg;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
@Builder
public class Page {
    private String bodyContent;
    private String scriptContent;

    @SneakyThrows
    private String build() {
        var filePath = Page.class.getClassLoader().getResource("html/analysisTemplate.html");
        assert filePath != null;
        String templateContent = Files.readString(Path.of(filePath.toURI()));
        templateContent = templateContent.replace("</script>", String.format("%s</script>", scriptContent));
        return templateContent.replace("<body>", String.format("<body>%s", bodyContent));
    }

    @SneakyThrows
    public URI generate() {
        Path tempFile = Files.createTempFile("index", ".html");
        Files.writeString(tempFile, build());
        return tempFile.toUri();
    }

    public String generateAsDataUrl() {
        return String.format("data:text/html,%s", build());
    }
}
