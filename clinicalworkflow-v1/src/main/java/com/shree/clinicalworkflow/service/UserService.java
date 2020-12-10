package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.User;

public interface UserService {
	public List<User> listAll(); 
	public void save(User user);
	public User get(Long id) ;
	public void delete(Long id) ;
	public Page<User> findPaginated(Pageable pageable) ;

}
