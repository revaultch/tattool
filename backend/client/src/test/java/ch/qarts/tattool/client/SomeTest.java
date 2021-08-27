package ch.qarts.tattool.client;

import ch.qarts.tattool.client.junit.runner.Tattool;
import ch.qarts.tattool.client.junit.runner.TattoolTestClientRunner;
import org.junit.jupiter.api.Disabled;

import static ch.qarts.tattool.core.domain.run.Type.RECORDING;

@Disabled
@Tattool(apiUri = "http://localhost:8888", clientUri = "http://localhost:8080", reference = "611e5da974fd525495bd09fa", type = RECORDING)
public class SomeTest extends TattoolTestClientRunner {
}
