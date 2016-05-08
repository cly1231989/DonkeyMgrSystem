package com.hose.service;

import org.springframework.data.domain.Page;

import com.hose.model.Donkey;
import com.hose.model.User;

public interface DonkeyService {

	public long getTotalCount();
	public Page<Donkey> getOneGroupDonkeys(int fisrtIndex, int Count, String sidx, String sord);
	public Page<Donkey> getOneGroupDonkeysByCondition(String condition, String value, int fisrtIndex, int Count, String sidx, String sord);
	public void delDonkey(Integer id);
	public Donkey getDonkey(Integer id);
	public Donkey saveDonkey(Donkey donkey);
	public boolean isSnExist(Integer id, Integer sn);
	public Donkey getDonkeyBySn(Integer sn);
	public Iterable<Donkey> findAll();
}
