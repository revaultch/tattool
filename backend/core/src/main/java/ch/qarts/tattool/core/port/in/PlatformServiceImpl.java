package ch.qarts.tattool.core.port.in;

import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.port.out.PlatformRegistryPort;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private PlatformRegistryPort platformRegistry;

    @Override
    public PlatformProxy getById(String platformId) {
        return this.platformRegistry.findById(platformId);
    }

    @Override
    public Collection<PlatformProxy> getList() {
        return platformRegistry.getPlatformList();
    }


}
