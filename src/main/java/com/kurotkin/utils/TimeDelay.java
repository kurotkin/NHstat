package com.kurotkin.utils;

public class TimeDelay {

    private Long t1;
    private Long ms;

    public TimeDelay(Long ms) {
        this.ms = ms;
        t1 = System.currentTimeMillis();
    }

    public Long getTime() {
        Long t2 = System.currentTimeMillis();
        Long dt = t2 - t1;
        System.out.println("Время записи " + dt + "ms");

        try {
            Thread.sleep(ms - dt);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dt;
    }
}
