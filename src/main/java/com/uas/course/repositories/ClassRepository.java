package com.uas.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uas.course.models.StudyClass;

@Repository
public interface ClassRepository extends JpaRepository<StudyClass, Integer> {

}
