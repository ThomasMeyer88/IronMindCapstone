package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.CompletedSet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompletedSets extends CrudRepository<CompletedSet,Long>{


    List<CompletedSet> findAllByExerciseIdAndClient_Id(long id, long clientid);
    List<CompletedSet> findAllByClient_Id(long id);

    List<CompletedSet> findAllByExerciseIdOrderByEstimated1RMDesc(long id);
}
