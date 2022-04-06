package com.shree.clinicalworkflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.shree.clinicalworkflow.domain.User;
import com.shree.clinicalworkflow.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	private List<User> users; 
	
	@Override
	public List<User> listAll() {
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(users::add);
	    return users;
	}

	@Override
	public void save(User user) {
		
		if(user!=null && user.getId()==null) {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			
		}
		else if(user!=null && user.getId()!=null) {
			User userOld = userRepository.getId(user.getId());
			
			if(user.getPassword()==null || user.getPassword().length()==0)
			{
				user.setPassword(userOld.getPassword())	;
			}
			else
			{
				user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			}
			
		}
			
			userRepository.save(user);
	}

	@Override
	public User get(Long id) {
		// TODO Auto-generated method stub
		return userRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		 User user =userRepository.getId(id);
		 user.remove();
		 userRepository.delete(user);

	}
	
	@Override
	public Page<User> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        users=this.listAll();
        List<User> list;
 
        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }
 
        Page<User> userPage
          = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), users.size());
 
        return userPage;
    }

}
