package com.hose.service;

import org.springframework.data.domain.Page;

import com.hose.model.User;

public interface UserService {

	public User getUser(String name);
	public User getUser(Long id);
	
	public void saveUser(User user);
	public void delUser(Long id);
	
	public long getTotalCount();
	public Page<User> getOneGroupUsers(int fisrtIndex, int Count, String sidx, String sord);
//	Page<User> getOneGroupUsersByName(String name, int fisrtIndex, int Count, String sidx, String sord);
//	Page<User> getOneGroupUsersByUserName(String userName, int fisrtIndex, int Count, String sidx, String sord);
	public Page<User> getOneGroupUsersByCondition(String condition, String value, int fisrtIndex, int Count, String sidx, String sord);
	public boolean isUserNameExist(String name);

}
