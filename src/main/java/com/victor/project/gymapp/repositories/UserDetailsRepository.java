package com.victor.project.gymapp.repositories;

import org.springframework.data.repository.CrudRepository;
import com.victor.project.gymapp.models.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails,Long>{

}
