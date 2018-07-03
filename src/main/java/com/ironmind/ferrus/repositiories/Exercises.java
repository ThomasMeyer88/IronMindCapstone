package com.ironmind.ferrus.repositiories;



import com.ironmind.ferrus.model.Exercise;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Exercises extends CrudRepository<Exercise, Long> {
    List<Exercise> findAll();

    Exercise findByName(String name);


}