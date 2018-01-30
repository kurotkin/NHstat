package com.kurotkin.api.com.nicehash.api.stats.provider.ex;

import com.kurotkin.api.com.nicehash.api.stats.provider.ex.Current;

import java.util.List;

public class Result {
    public List<Current> current;
    public boolean nh_wallet;
    public int attack_written_off;
    public String attack_amount;
    public String addr;
    public String attack_repaid;
}
