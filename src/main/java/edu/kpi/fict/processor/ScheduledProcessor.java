package edu.kpi.fict.processor;

import edu.kpi.fict.service.persistence.ConfigurationService;
import edu.kpi.fict.strategy.download.ConfigurationBasedRoundLoadingStrategy;
import edu.kpi.fict.strategy.download.InitialRoundLoadingStrategy;
import edu.kpi.fict.strategy.upload.HiveUploadStrategy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class ScheduledProcessor {

    private final InitialRoundLoadingStrategy initialRoundLoadingStrategy;
    private final ConfigurationBasedRoundLoadingStrategy configurationBasedRoundLoadingStrategy;
    private final ConfigurationService configurationService;
    private final HiveUploadStrategy hiveUploadStrategy;

    public ScheduledProcessor(final InitialRoundLoadingStrategy initialRoundLoadingStrategy,
                              final ConfigurationBasedRoundLoadingStrategy configurationBasedRoundLoadingStrategy,
                              final ConfigurationService configurationService,
                              final HiveUploadStrategy hiveUploadStrategy) {

        this.initialRoundLoadingStrategy = initialRoundLoadingStrategy;
        this.configurationBasedRoundLoadingStrategy = configurationBasedRoundLoadingStrategy;
        this.configurationService = configurationService;
        this.hiveUploadStrategy = hiveUploadStrategy;
    }

//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedDelay = 1000)
    public void loadRacesData() {

        System.out.println("[START] - Data download strategy - " + new Date());

        configurationService.getConfiguration()
                .ifPresentOrElse(configurationBasedRoundLoadingStrategy::execute, initialRoundLoadingStrategy::execute);

        System.out.println("[FINISH] - Data download strategy - " + new Date() + "\n");
    }

//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedDelay = 1000)
    public void uploadDataToHive() {

        System.out.println("[START] - Hive upload strategy - " + new Date());

        hiveUploadStrategy.execute();

        System.out.println("[FINISH] - Hive upload strategy - " + new Date() + "\n");
    }
}
