package com.compdes.activity.repositories;

import com.compdes.activity.models.entities.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity,String> {
}
