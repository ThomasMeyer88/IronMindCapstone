package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepositories extends CrudRepository<Client, Long>{
    Client findByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM users LIMIT 1")
    Client first();
}
