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
public class DonkeyServiceImpl implements DonkeyService {

//	public UserServiceImpl() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
	@Autowired
	private DonkeyRepository donkeyRepository;

//	@Autowired
//	public UserServiceImpl(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
	
	public DonkeyRepository getDonkeyRepository() {
		return donkeyRepository;
	}

	public void setDonkeyRepository(DonkeyRepository donkeyRepository) {
		this.donkeyRepository = donkeyRepository;
	}

	@Override
	public Page<Donkey> getOneGroupDonkeys(int fisrtIndex, int Count, String sidx, String sord) {
		Direction directione = getDirection(sord);
		return donkeyRepository.findAll( new PageRequest(fisrtIndex, Count, directione, sidx) );
	}

	@Override
	public long getTotalCount() {
		return donkeyRepository.count();
	}

	@Override
	public Page<Donkey> getOneGroupDonkeysByCondition(String condition, String value, int fisrtIndex, int Count,
			String sidx, String sord) {
		Direction directione = getDirection(sord);
		Specification<Donkey> spec = new Specification<Donkey>() {  
			@Override
			public Predicate toPredicate(Root<Donkey> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get(condition).as(String.class), "%" + value + "%");
			}  
        };
        
        return donkeyRepository.findAll(spec, new PageRequest(fisrtIndex, Count, directione, sidx));//.findByUsernameLike(userName, new PageRequest(fisrtIndex, Count, Direction.ASC, sidx) );
	}
	
	private Direction getDirection(String sord){
		if(sord.equalsIgnoreCase("asc")){
			return Direction.ASC;
		}else{
			return Direction.DESC;
		}
	}

	@Override
	public void delDonkey(Integer id) {
		donkeyRepository.delete(id);
	}

	@Override
	public Donkey getDonkey(Integer id) {
		return donkeyRepository.findOne(id);
	}

	@Override
	public Donkey saveDonkey(Donkey donkey) {
		return donkeyRepository.save(donkey);
		
	}

	@Override
	public boolean isSnExist(Integer id, Integer sn) {
		Donkey donkey = donkeyRepository.findOneBySn(sn);
		if(donkey == null)
			return false;
		
		if(id== 0)
			return true;
		
		return (donkey.getId() != id);
	}

	@Override
	public Donkey getDonkeyBySn(Integer sn) {
		return donkeyRepository.findOneBySn(sn);
	}

	@Override
	public Iterable<Donkey> findAll() {
		return donkeyRepository.findAll();
	}

}
