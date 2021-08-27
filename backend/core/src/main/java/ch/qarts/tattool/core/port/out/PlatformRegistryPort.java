package ch.qarts.tattool.core.port.out;

import ch.qarts.tattool.core.domain.platform.PlatformProxy;

import java.util.List;

public interface PlatformRegistryPort {

    PlatformProxy findById(String id);

    List<PlatformProxy> getPlatformList();

}
