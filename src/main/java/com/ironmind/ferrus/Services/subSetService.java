package com.ironmind.ferrus.Services;

import com.ironmind.ferrus.repositiories.SubSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class subSetService {
    private SubSets sets;

    @Autowired
    public subSetService(SubSets sets){this.sets = sets;}

    public SubSets getSets() {
        return sets;
    }


}
