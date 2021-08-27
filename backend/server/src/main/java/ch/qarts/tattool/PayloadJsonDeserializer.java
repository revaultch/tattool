package ch.qarts.tattool;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
public class PayloadJsonDeserializer extends JsonDeserializer<Payload> {

    private List<PlatformProxy> platformList;

    @Autowired
    public PayloadJsonDeserializer(List<PlatformProxy> platformList) {
        super();
        this.platformList = platformList;
    }

    @Override
    public Payload deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var payload = jsonParser.readValueAsTree().toString();
        for (var platform : platformList) {
            try {
                return platform.payloadFactory().create(payload);
            } catch (Throwable t) {
                log.warn(t.getMessage());
            }
        }
        throw new IllegalStateException(String.format("No payload factory found for payload %s", payload));
    }
}
