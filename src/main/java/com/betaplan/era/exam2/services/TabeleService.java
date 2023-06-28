package com.betaplan.era.exam2.services;

import java.util.List;
import java.util.Optional;

import com.betaplan.era.exam2.models.Tabele;
import com.betaplan.era.exam2.models.User;
import com.betaplan.era.exam2.repositories.TableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TabeleService {
    @Autowired
    private TableRepo  TableRepo ;



    public List<Tabele> allProjects(){
        return TableRepo.findAll();
    }

    public Tabele updateProject(Tabele project) {
        return TableRepo.save(project);
    }

    public List<Tabele> getAssignedTables(User user){
        return TableRepo.findAllByUsers(user);
    }

    public List<Tabele> getUnassignedTables(User user){
        return TableRepo.findByUsersNotContains(user);
    }

    public Tabele addProject(Tabele project) {
        return TableRepo.save(project);
    }

    public void deleteProject(Tabele project) {
        TableRepo.delete(project);
    }

    public Tabele findById(Long id) {
        Optional<Tabele> optionalProject = TableRepo.findById(id);
        if(optionalProject.isPresent()) {
            return optionalProject.get();
        }else {
            return null;
        }
    }

}