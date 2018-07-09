package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.SubSet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubSets extends CrudRepository<SubSet, Long> {
    List<SubSet> findAll();

    List<SubSet> findAllByWorkSet_Id(long id);

}