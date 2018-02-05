package com.kurotkin.dao;

import java.util.List;

public interface GenericDAO <VAL, ID> {
    void save(VAL val);
    void saveAll(List<VAL> list);
}
