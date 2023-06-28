package com.betaplan.era.exam2.repositories;

import java.util.List;

import com.betaplan.era.exam2.models.Tabele;
import com.betaplan.era.exam2.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TableRepo extends CrudRepository<Tabele, Long> {
    List<Tabele> findAll();
    Tabele findByIdIs(Long id);
    List<Tabele> findAllByUsers(User user);
    List<Tabele> findByUsersNotContains(User user);
}
