package ch.qarts.tattool.core.domain.execution.live;


public interface ProgressListener {

    void onProgress(PayloadExecutionEvent payloadExecutionEvent);

    void onDone(RunTerminatedExecutionEvent runTerminatedExecutionEvent);

}
