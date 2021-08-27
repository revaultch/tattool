package ch.qarts.tattool.adapter;

import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.port.out.PlatformRegistryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformRegistryPortAdapter implements PlatformRegistryPort {

    private final List<PlatformProxy> platformList;

    @Autowired
    public PlatformRegistryPortAdapter(List<PlatformProxy> platformList) {
        this.platformList = platformList;
    }

    @Override
    public PlatformProxy findById(String id) {
        return platformList.stream().filter(t -> t.getId().equals(id)).findFirst().orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public List<PlatformProxy> getPlatformList() {
        return this.platformList;
    }
}
