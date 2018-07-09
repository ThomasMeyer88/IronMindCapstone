package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.Program;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Programs extends CrudRepository<Program, Long> {
    Program findByClient_Id(long id);
    Program findById(long id);
    Program findByClient_IdAndId(long clientId, long id);
    List<Program> findAllByClient_Id(long id);
    Program findByClient_IdAndName(long id, String name);
}
