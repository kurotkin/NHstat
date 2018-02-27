package com.kurotkin.algoprof;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
//    final AlgoProfRepository algoProfRepository;
//
//    public ScheduledTasks(AlgoProfRepository algoProfRepository) {
//        this.algoProfRepository = algoProfRepository;
//    }
    AlgoProfDAO algoProfDAO = new AlgoProfDAO();

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        AlgoProfController algoProfController = new AlgoProfController();
        List<AlgoProf> algoProfList = algoProfController.getProfAlgoList();
        algoProfDAO.save(algoProfList);
    }
}
