package com.hose.service;

import java.util.ArrayList;
import java.util.List;

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
import com.hose.model.FilterItem;
import com.hose.model.Filters;
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
	public Page<Donkey> getOneGroupDonkeys(int pageth, int Count, String sidx, String sord) {
		Direction directione = getDirection(sord);
		return donkeyRepository.findAll( new PageRequest(pageth, Count, directione, sidx) );
	}

	@Override
	public long getTotalCount() {
		return donkeyRepository.count();
	}

	@Override
	public Page<Donkey> getOneGroupDonkeysByCondition(Filters filters, int pageth, int Count,
			String sidx, String sord) {
		Direction directione = getDirection(sord);
		Specification<Donkey> spec = new Specification<Donkey>() {  
			@Override
			public Predicate toPredicate(Root<Donkey> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();  
				for(FilterItem item:filters.getRules()){
					if( item.getOp().equalsIgnoreCase("cn") )
						list.add(cb.like(root.get(item.getField()).as(String.class), "%"+item.getData()+"%"));
					else if( item.getOp().equalsIgnoreCase("eq") )
						list.add(cb.equal(root.get(item.getField()).as(String.class), item.getData()));
				}

				Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));  
			}  
        };
        
        return donkeyRepository.findAll(spec, new PageRequest(pageth, Count, directione, sidx));//.findByUsernameLike(userName, new PageRequest(fisrtIndex, Count, Direction.ASC, sidx) );
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
		
		return !id.equals( donkey.getId() );
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
