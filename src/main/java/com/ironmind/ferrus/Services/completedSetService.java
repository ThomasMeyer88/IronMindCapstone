package com.ironmind.ferrus.Services;

import com.ironmind.ferrus.repositiories.CompletedSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class completedSetService {
    private CompletedSets compSets;

    public completedSetService(){};

    @Autowired
    public completedSetService(CompletedSets compSets) {
        this.compSets = compSets;
    }

    public CompletedSets getCompSets() {
        return compSets;
    }

    public void setCompSets(CompletedSets compSets) {
        this.compSets = compSets;
    }
}
