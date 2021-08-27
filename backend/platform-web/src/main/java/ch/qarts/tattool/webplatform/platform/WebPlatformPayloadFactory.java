package ch.qarts.tattool.webplatform.platform;

import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.PayloadFactory;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static ch.qarts.tattool.webplatform.commandfactory.CommandDescriptorMapper.getDescriptor;

public class WebPlatformPayloadFactory implements PayloadFactory {

    @Override
    public Payload create(String payload) {
        CommandDescriptor commandDescriptor = getDescriptor(getVerb(payload));
        return new Gson().fromJson(payload, commandDescriptor.getPayloadType());
    }

    private String getVerb(String payload) {
        JsonObject jsonObject = new Gson().fromJson(payload, JsonObject.class);
        boolean valid = jsonObject.isJsonObject() && jsonObject.has("verb");
        if (valid) {
            return jsonObject.get("verb").getAsString();
        } else {
            throw new IllegalStateException(String.format("Invalid verb for payload %s", payload));
        }
    }


}
