package com.ironmind.ferrus.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ironmind.ferrus.repositiories.templates;

@Service
public class templateService {
    private templates templates;


    public templateService(){};

    @Autowired
    public templateService(templates templates) {this.templates = templates;}

    public templates getTemplates() {
        return templates;
    }

    public void setTemplates(templates templates) {
        this.templates = templates;
    }
}
