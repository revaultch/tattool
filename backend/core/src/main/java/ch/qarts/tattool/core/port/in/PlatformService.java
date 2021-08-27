package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.platform.PlatformProxy;

import java.util.Collection;

public interface PlatformService {

    PlatformProxy getById(String id);

    Collection<PlatformProxy> getList();
}
