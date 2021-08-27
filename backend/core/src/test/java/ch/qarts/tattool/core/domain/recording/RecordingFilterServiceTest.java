package ch.qarts.tattool.core.domain.recording;

import ch.qarts.tattool.core.domain.execution.ExecutableTestExecutionService;
import ch.qarts.tattool.core.domain.recordingfilter.RecordingFilter;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.transpilation.ExecutableRecordingInstance;
import ch.qarts.tattool.core.domain.transpilation.TranspilationService;
import ch.qarts.tattool.core.port.in.RecordingFilterService;
import ch.qarts.tattool.core.port.in.RecordingFilterServiceImpl;
import ch.qarts.tattool.core.port.out.RecordingFilterRepositoryPort;
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

public class RecordingFilterServiceTest {

    private RecordingFilterService testedRecordingFilterService;
    private RecordingFilterRepositoryPort recordingFilterRepositoryPort;
    @Mock
    private TranspilationService transpilationService;
    @Mock
    private ExecutableTestExecutionService executableTestExecutionService;


    @BeforeEach
    public void setup() {
        this.transpilationService = mock(TranspilationService.class);
        this.executableTestExecutionService = mock(ExecutableTestExecutionService.class);
        when(executableTestExecutionService.execute(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null));
        this.recordingFilterRepositoryPort = mock(RecordingFilterRepositoryPort.class);
        this.testedRecordingFilterService = new RecordingFilterServiceImpl(recordingFilterRepositoryPort, transpilationService, executableTestExecutionService);
    }

    @Test
    public void shouldSaveRecordingFilter() {
        RecordingFilter recordingFilter = new RecordingFilter();
        this.testedRecordingFilterService.save(recordingFilter);
        Mockito.verify(this.recordingFilterRepositoryPort).save(recordingFilter);
        Mockito.reset(this.recordingFilterRepositoryPort);
    }

    @Test
    public void shouldGetRecordingFilter() {
        RecordingFilter expected = new RecordingFilter();
        when(this.recordingFilterRepositoryPort.getOne("someid")).thenReturn(expected);
        RecordingFilter actual = this.testedRecordingFilterService.getRecordingFilter("someid");
        assertEquals(expected, actual);
        reset(this.recordingFilterRepositoryPort);
    }

    @Test
    public void shouldMatchFutureCountWhenNoTranspilation() throws Exception {
        RecordingFilter expected = new RecordingFilter();
        when(recordingFilterRepositoryPort.getOne("someid")).thenReturn(expected);
        when(transpilationService.transpile((RecordingFilter) any())).thenReturn(Collections.singletonList(ExecutableRecordingInstance.builder().build()));
        assertEquals(1, testedRecordingFilterService.buildExecutable("someid", new SessionConfiguration(), null).getExecutionList().get().size());
        reset(transpilationService);
        reset(recordingFilterRepositoryPort);
    }

    @Test
    public void shouldMatchFutureCountWhenTranspilation() throws Exception {
        RecordingFilter expected = new RecordingFilter();
        when(recordingFilterRepositoryPort.getOne("someid")).thenReturn(expected);
        when(transpilationService.transpile((RecordingFilter) any())).thenReturn(Arrays.asList(ExecutableRecordingInstance.builder().build(), ExecutableRecordingInstance.builder().build()));
        assertEquals(2, testedRecordingFilterService.buildExecutable("someid", new SessionConfiguration(), null).getExecutionList().get().size());
        reset(transpilationService);
        reset(recordingFilterRepositoryPort);
    }

}
