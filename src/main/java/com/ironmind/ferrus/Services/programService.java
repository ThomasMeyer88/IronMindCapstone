package com.ironmind.ferrus.Services;

import com.ironmind.ferrus.repositiories.Programs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ironmind.ferrus.repositiories.templates;

@Service
public class programService {
    private Programs programs;


    public programService(){};

    @Autowired
    public programService(Programs programs) {
        this.programs = programs;
    }

    public Programs getPrograms() {
        return programs;
    }

    public void setPrograms(Programs programs) {
        this.programs = programs;
    }
}
