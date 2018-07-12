package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.Exercise;
import com.ironmind.ferrus.model.WorkSet;
import com.ironmind.ferrus.model.template;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkSets extends CrudRepository<WorkSet, Long> {
    List<WorkSet> findAll();

    WorkSet findByExercise(Exercise exercise);
    WorkSet findById(long id);

    WorkSet findByTemplate_Id(long id);

    List<WorkSet> findAllById(long id);
    List<WorkSet> findAllByTemplate(template temp);

    WorkSet findByTemplate_IdAndExerciseName(long id, String name);



}
