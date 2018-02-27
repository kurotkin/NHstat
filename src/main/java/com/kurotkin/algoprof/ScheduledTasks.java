package com.kurotkin.algoprof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {
    private final AlgoProfRepository algoProfRepository;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ScheduledTasks(AlgoProfRepository algoProfRepository) {
        this.algoProfRepository = algoProfRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        AlgoProfController algoProfController = new AlgoProfController();
        List<AlgoProf> algoProfList = algoProfController.getProfAlgoList();
        algoProfRepository.save(algoProfList);
    }
}
