package com.ironmind.ferrus.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ironmind.ferrus.repositiories.WorkSets;


@Service
public class workSetService {
    private WorkSets work;

    public workSetService(){};

    @Autowired
    public workSetService(WorkSets work) {this.work = work;}

    public WorkSets getWork() {
        return work;
    }

    public void setWork(WorkSets work) {
        this.work = work;
    }


}
