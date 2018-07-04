package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.CompletedSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompletedSets extends CrudRepository<CompletedSet,Long>{

    List<CompletedSet> findAllByClient(Client client);

    List<CompletedSet> findAllByExerciseId(long id);

    CompletedSet findByExerciseIdOrderByEstimated1RMDesc(long id);
}
