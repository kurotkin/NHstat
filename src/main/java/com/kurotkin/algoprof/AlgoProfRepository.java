package com.kurotkin.algoprof;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgoProfRepository extends CrudRepository<AlgoProfModel, Integer> {
}
