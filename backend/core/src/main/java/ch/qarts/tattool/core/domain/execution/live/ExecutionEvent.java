package ch.qarts.tattool.core.domain.execution.live;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PayloadExecutionEvent.class, name = "payloadExecutionEvent"),
        @JsonSubTypes.Type(value = RunTerminatedExecutionEvent.class, name = "runTerminatedExecutionEvent")
})
public interface ExecutionEvent {

}
