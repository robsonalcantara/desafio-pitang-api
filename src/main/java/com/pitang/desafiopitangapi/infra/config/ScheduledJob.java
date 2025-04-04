package com.pitang.desafiopitangapi.infra.config;

import com.pitang.desafiopitangapi.service.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Class responsible for executing a scheduled job to update statuses.
 * <p>
 * This job runs automatically at predefined intervals and is designed to
 * perform operations related to route scheduling, such as updating the
 * status of vehicles used the previous day that failed to update their status.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Runs periodically according to the {@code @Scheduled} configuration.</li>
 *     <li>Processes and updates statuses.</li>
 * </ul>
 *
 * <p><b>Configuration:</b></p>
 * <p>The execution frequency can be adjusted via Spring properties
 * or directly in the {@code @Scheduled} annotation.</p>
 *
 * <p>Example configuration:</p>
 * <pre>
 * {@code
 * @Scheduled(cron = "0 0 * * * *") // Runs every hour
 * public void executeJob() {
 *     // Job logic
 * }
 * }
 * </pre>
 *
 * @author Robson Rodrigues
 */

@AllArgsConstructor
@Slf4j
@Component
public class ScheduledJob {

    private final CarService carService;
    /**
     * Executes the job every day at 7:00 AM.
     */
    @Scheduled(cron = "0 0 7 * * *", zone = "America/Sao_Paulo")
    public void executeJob() {
        try {
            int updatedCars = carService.resetCarUsage();
            log.info("Scheduled job executed: {} cars have been reset to not be in use.", updatedCars);
        } catch (Exception e) {
            log.error("Error while resetting car usage: {}", e.getMessage(), e);
        }
    }

}
