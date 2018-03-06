package com.kurotkin;

import com.kurotkin.algoprof.AlgoProf;
import com.kurotkin.algoprof.AlgoProfController;
import com.kurotkin.algoprof.AlgoProfDAO;
import com.kurotkin.balance.Balance;
import com.kurotkin.balance.BalanceController;
import com.kurotkin.balance.BalanceDAO;
import com.kurotkin.hashbtc.HashBTC;
import com.kurotkin.hashbtc.HashBTC_Controller;
import com.kurotkin.hashbtc.HashBTC_DAO;
import com.kurotkin.rate.Rate;
import com.kurotkin.rate.RateController;
import com.kurotkin.rate.RateDAO;
import com.kurotkin.rate.RateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    final RateRepository rateRepository;

    public ScheduledTasks(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    private AlgoProfDAO algoProfDAO = new AlgoProfDAO();
    private RateDAO rateDAO = new RateDAO();
    private BalanceDAO balanceDAO = new BalanceDAO();
    private HashBTC_DAO hashBTC_dao = new HashBTC_DAO();

    @Scheduled(fixedRate = 100_000)
    public void algoProfEx() {
        AlgoProfController algoProfController = new AlgoProfController();
        List<AlgoProf> algoProfList = algoProfController.getProfAlgoList();
        algoProfDAO.save(algoProfList);
    }

    @Scheduled(fixedRate = 999_000)
    public void rateEx() {
        RateController rateController = new RateController();
        Rate rate = rateController.getRate();
        rateDAO.save(rate);
        rate.setId(1L);
        rateRepository.save(rate);
        System.out.println("Find " + rateRepository.findOne(1L).toString());
    }

    @Scheduled(fixedRate = 930_000)
    public void balanceEx() {
        BalanceController balanceController = new BalanceController();
        Balance balance = balanceController.getBalance();
        balanceDAO.save(balance);
    }

    @Scheduled(fixedRate = 240_000)
    public void hashBTCEx() {
        HashBTC_Controller hashBTC_controller = new HashBTC_Controller(rateRepository);
        HashBTC hashBTC = hashBTC_controller.getHashBTC();
        hashBTC_dao.save(hashBTC);
    }
}
