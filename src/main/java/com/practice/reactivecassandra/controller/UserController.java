package com.practice.reactivecassandra.controller;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.practice.reactivecassandra.entity.User;
import com.practice.reactivecassandra.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE )
	public Flux<User> getAllUsers(){
		
		Mono<List<User>> collectList = userRepository.findAll().collectList();
		
		collectList.subscribe(x->{
			x.stream().forEach(record->{
				System.out.println(record.getCity());
			});
		});
		
		return userRepository.findAll().delayElements(Duration.ofSeconds(2)).log();
		
	}
	
	@RequestMapping(path = "/users/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE )
	public Flux<User> getAllUsersById(@PathVariable("id")Integer id){
		
		return userRepository.findByIdLessThan(id);
		
	}
	
	@RequestMapping(path = "/practice", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE )
	public Flux<User> practice(){
		
		Flux<User> users=userRepository.findAll();
		
		//convert Flux<User> to Mono<List<User>>
		
		Mono<List<User>> collectMonoList = users.collectList();
		
		//convert Mono<List<User>> to Flux<List<User>>
		
		Flux<List<User>> flatMapMany = collectMonoList.flatMapMany(usersList->Flux.just(usersList));
		
		//convert Mono<List<User>> to Flux<User>
		
		Flux<User> flatUser = collectMonoList.flatMapMany(Flux::fromIterable);
		
		//convert Flux<List<User>> to Flux<User>
		
		Flux<User> flatMap = flatMapMany.flatMap(Flux::fromIterable);
		
		
		return null;
		
	}
	
	

}
