package com.ironmind.ferrus.repositiories;

import com.ironmind.ferrus.model.template;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface templates extends CrudRepository<template, Long> {
    List<template> findAll();

    List<template> findAllByDay(int day);



}
