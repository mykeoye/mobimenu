package io.mobimenu.core.service.context;

import io.mobimenu.core.port.in.setting.UpdateSettingUseCase;
import io.mobimenu.core.port.in.setting.ViewSettingUseCase;
import io.mobimenu.core.port.out.setting.SettingQueryOperationsPort;
import io.mobimenu.core.port.out.setting.SettingSaveOperationsPort;
import io.mobimenu.core.service.setting.SettingService;
import io.mobimenu.persistence.SettingPersistenceAdapter;
import io.mobimenu.persistence.SettingQueryAdapter;
import io.mobimenu.persistence.repository.SettingRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class SettingContext {

    @Produces
    @Singleton
    public SettingQueryOperationsPort settingQueryOperationsPort(SettingRepository repository) {
        return new SettingQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public SettingSaveOperationsPort settingSaveOperationsPort(SettingRepository repository) {
        return new SettingPersistenceAdapter(repository);
    }

    @Produces
    @Singleton
    public ViewSettingUseCase viewSettingUseCase(SettingSaveOperationsPort settingSaveOperationsPort,
                                                 SettingQueryOperationsPort settingQueryOperationsPort) {
        return settingService(settingSaveOperationsPort, settingQueryOperationsPort);
    }

    @Produces
    @Singleton
    public UpdateSettingUseCase updateSettingUseCase(SettingSaveOperationsPort settingSaveOperationsPort,
                                                   SettingQueryOperationsPort settingQueryOperationsPort) {
        return settingService(settingSaveOperationsPort, settingQueryOperationsPort);
    }

    SettingService settingService(SettingSaveOperationsPort settingSaveOperationsPort, SettingQueryOperationsPort settingQueryOperationsPort) {
        return new SettingService(settingSaveOperationsPort, settingQueryOperationsPort);
    }

}
