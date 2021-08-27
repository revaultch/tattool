package ch.qarts.tattool.core.domain.recording;

import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionService;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.transpilation.ExecutableRecordingInstance;
import ch.qarts.tattool.core.domain.transpilation.TranspilationService;
import ch.qarts.tattool.core.port.in.RecordingService;
import ch.qarts.tattool.core.port.in.RecordingServiceImpl;
import ch.qarts.tattool.core.port.out.RecordingRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecordingServiceTest {

    private RecordingService testedRecordingService;
    private RecordingRepositoryPort recordingRepositoryPort;
    @Mock
    private TranspilationService transpilationService;
    @Mock
    private ExecutableTestExecutionService executableTestExecutionService;


    @BeforeEach
    public void setup() {
        this.transpilationService = mock(TranspilationService.class);
        this.executableTestExecutionService = mock(ExecutableTestExecutionService.class);
        when(executableTestExecutionService.execute(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null));
        this.recordingRepositoryPort = Mockito.mock(RecordingRepositoryPort.class);
        this.testedRecordingService = new RecordingServiceImpl(recordingRepositoryPort, transpilationService, executableTestExecutionService);
    }

    @Test
    public void shouldSaveRecording() {
        Recording recording = new Recording();
        this.testedRecordingService.save(recording);
        Mockito.verify(this.recordingRepositoryPort).save(recording);
        Mockito.reset(this.recordingRepositoryPort);
    }

    @Test
    public void shouldGetRecording() {
        Recording expected = new Recording();
        Mockito.when(this.recordingRepositoryPort.getOne("someid")).thenReturn(expected);
        Recording actual = this.testedRecordingService.getRecording("someid");
        assertEquals(expected, actual);
        Mockito.reset(this.recordingRepositoryPort);
    }

    @Test
    public void shouldMatchFutureCountWhenNoTranspilation() throws Exception {
        Recording expected = new Recording();
        Mockito.when(this.recordingRepositoryPort.getOne("someid")).thenReturn(expected);
        when(transpilationService.transpile((Recording) any())).thenReturn(Collections.singletonList(ExecutableRecordingInstance.builder().build()));
        assertEquals(1, testedRecordingService.buildExecutable("someid", new SessionConfiguration(), null).getExecutionList().get().size());
        reset(transpilationService);
    }

    @Test
    public void shouldMatchFutureCountWhenTranspilation() throws Exception {
        Recording expected = new Recording();
        Mockito.when(this.recordingRepositoryPort.getOne("someid")).thenReturn(expected);
        when(transpilationService.transpile((Recording) any())).thenReturn(Arrays.asList(ExecutableRecordingInstance.builder().build(), ExecutableRecordingInstance.builder().build()));
        assertEquals(2, testedRecordingService.buildExecutable("someid", new SessionConfiguration(), null).getExecutionList().get().size());
        reset(transpilationService);
    }

}
