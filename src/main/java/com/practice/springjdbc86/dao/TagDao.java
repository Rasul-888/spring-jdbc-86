package com.practice.springjdbc86.dao;

import com.practice.springjdbc86.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> findAll();
    Tag findById(int id);
}
