package com.kurotkin.hashbtc;

import com.kurotkin.rate.Rate;
import com.kurotkin.rate.RateController;
import com.kurotkin.rate.RateDAO;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledTasks {
    HashBTC_DAO hashBTC_dao = new HashBTC_DAO();

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        HashBTC_Controller hashBTC_controller = new HashBTC_Controller();
        HashBTC hashBTC = hashBTC_controller.getHashBTC();
        hashBTC_dao.save(hashBTC);
    }
}
