package com.compdes.activity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compdes.activity.models.entities.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {
}
