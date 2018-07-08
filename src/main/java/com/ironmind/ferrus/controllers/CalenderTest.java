package com.ironmind.ferrus.controllers;

import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalenderTest {
    public static void main(String[] args) {
        int duration = 90;
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = localDate.format(formatter);
        String[] testString = formattedString.split(" ");
        List<Integer> convertString = new ArrayList<>();
        for(String index: testString){
            int indexInt = Integer.parseInt(index);
            convertString.add(indexInt);
            System.out.println("Index is " + indexInt);
        }

        LocalDate end = localDate.plusDays(90);
        System.out.println(end);

    }



}