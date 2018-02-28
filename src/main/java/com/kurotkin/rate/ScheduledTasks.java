package com.kurotkin.rate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    RateDAO rateDAO = new RateDAO();

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        RateController rateController = new RateController();
        Rate rate = rateController.getRate();
        rateDAO.save(rate);
    }
}
