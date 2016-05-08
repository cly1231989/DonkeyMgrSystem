package com.hose.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hose.model.Donkey;
import com.hose.model.User;

@Service
public class UserServiceImpl implements UserService {

//	public UserServiceImpl() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	public UserServiceImpl(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User getUser(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public User getUser(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public long getTotalCount() {		
		return userRepository.count()-1;
	}

	@Override
	public Page<User> getOneGroupUsers(int fisrtIndex, int Count, String sidx, String sord) {
		Direction directione = getDirection(sord);
		return userRepository.findAll( new PageRequest(fisrtIndex, Count, directione, sidx) );
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public Page<User> getOneGroupUsersByCondition(String condition, String value, int fisrtIndex, int Count, String sidx, String sord) {
		
		Direction directione = getDirection(sord);
		Specification<User> spec = new Specification<User>() {  
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(condition == null || condition.length() == 0){
					return cb.notEqual(root.get("typeid").as(Long.class), 1);
				}else{
				return cb.and(cb.notEqual(root.get("typeid").as(Long.class), 1),  
						cb.like(root.get(condition).as(String.class), "%" + value + "%") );
				}
			}  
        };
        
        return userRepository.findAll(spec, new PageRequest(fisrtIndex, Count, directione, sidx));//.findByUsernameLike(userName, new PageRequest(fisrtIndex, Count, Direction.ASC, sidx) );
	}
	
	private Direction getDirection(String sord){
		if(sord.equalsIgnoreCase("asc")){
			return Direction.ASC;
		}else{
			return Direction.DESC;
		}
	}

	@Override
	public void delUser(Long id) {
		userRepository.delete(id);		
	}

	@Override
	public boolean isUserNameExist(String name) {
		User user = userRepository.findByName(name);
		if(user == null)
			return false;
		
		return true;
		
	}
}
