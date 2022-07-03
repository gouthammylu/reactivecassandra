package com.practice.reactivecassandra.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.practice.reactivecassandra.entity.User;

import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCassandraRepository<User, Integer> {
	
	@AllowFiltering
	Flux<User> findByIdLessThan(int id);

}
