package com.hose.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hose.model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
	User findByName(String name);
}
