package ch.qarts.tattool.core.domain.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public abstract class Payload implements Serializable {

    private String verb;

    public abstract String humanReadable();

}
