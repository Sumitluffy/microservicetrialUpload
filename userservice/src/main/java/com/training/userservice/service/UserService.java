package com.training.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.training.userservice.dao.User;
import com.training.userservice.dao.UserDao;
import com.training.userservice.exception.UserNotFoundException;

@Service
public class UserService {
	// List<User> userList = new ArrayList();
	@Autowired
	UserDao dao;

	/*
	 * public UserService() { userList.add(new User(1, "shreyas", "Pune",
	 * "shreyas@gmail.com","123456")); userList.add(new User(2, "vivek", "Noida",
	 * "vivek@gmail.com","123456")); userList.add(new User(3, "ravi", "Patna",
	 * "ravi@gmail.com","123456")); userList.add(new User(4, "mohit", "Mumbai",
	 * "mohit@gmail.com","123456")); userList.add(new User(5, "sandesh", "Sangli",
	 * "sandesh@gmail.com","123456")); }
	 */

	public List<User> getUsers() {
		// System.out.println(userList);
		// return userList;
		return (List<User>) dao.findAll();
	}

	public User getUserById(Integer userid) {
		/*
		 * return userList.stream().filter(n -> n.getUserId() == userid).findAny()
		 * .orElseThrow(() -> new UserNotFoundException("User Not Found"));
		 */
		return dao.findById(userid).orElseThrow(() -> new UserNotFoundException("User Not Found"));
	}
	// in place of boolean previously it is User

	public boolean isUserPresent(Integer userid) {
		// return userList.stream().filter(n -> n.getUserId() ==
		// userid).findAny().orElse(null);
		return dao.existsById(userid);
	}

	public User addUser(User user) {
		System.out.println("user::" + user);
		/*
		 * userList.add(user); return getUserById(user.getUserId());
		 */
		return dao.save(user);

	}

	public User updateUser(Integer userid, User user) {
		User existinguser = getUserById(user.getUserId());
		if (existinguser != null) {
			existinguser.setUserName(user.getUserName());
			existinguser.setAddr(user.getAddr());
			existinguser.setEmail(user.getEmail());
		}
		// return existinguser;
		return dao.save(existinguser);

	}

	public User updateUserPartial(Integer userid, User user) {
		User existingUser = getUserById(user.getUserId());
		if (existingUser != null) {
			// String
			// name=user.getUserName()!=null?user.getUserName():existingUser.getUserName();
			existingUser.setUserName(user.getUserName() != null ? user.getUserName() : existingUser.getUserName());
			existingUser.setAddr(user.getAddr() != null ? user.getAddr() : existingUser.getAddr());
			existingUser.setEmail(user.getEmail() != null ? user.getEmail() : user.getEmail());
		}

		// return existingUser;
		return dao.save(existingUser);
	}

	public User updateUserByReflectionApi(Integer userId, Map<String, String> fieldsMap) {
		User existinguser = getUserById(userId);

		Set<String> fieldSet = fieldsMap.keySet();
		System.out.println(fieldSet);

		// Tradition approach
		for (String userField : fieldSet) {
			Field field = ReflectionUtils.findField(User.class, userField);
			field.setAccessible(true);

			ReflectionUtils.setField(field, existinguser, fieldsMap.get(userField));

		}
		// return existinguser;
		return dao.save(existinguser);
	}

	public String deleteUser(Integer userid) {
		/*
		 * User existingUser = getUserById(userid); userList.remove(existingUser);
		 */
		dao.deleteById(userid);
		return "user_deleted::" + userid;
	}

	public User getUserByName(String name) {
		return dao.findByuserName(name);
	}
	// i commented below line because right now i created address as entity class and address no longer as string data type 
// public List<User> getUserByAddr(String addr) {
//		return dao.getByaddr(addr);
//	}

	public User getUserByEmail(String usermail) {
		return dao.getUserEmail(usermail);
	}
	// i commented below line because right now i created address as entity class and address no longer as string data type
//	public List<String> getUserNameByAddr(String addr) {
//		return dao.getUserNameByAddr(addr);
//	}

	// Now using Pagination
	public List<User> getUsersByPage(Integer pagenumber, Integer pageSize) {
		
		//Pageable pageble = PageRequest.of(pagenumber, pageSize);
		//we can do sorting at page level also
		Pageable pageble = PageRequest.of(pagenumber, pageSize,Sort.by("userName"));
		
		Page<User> page = dao.findAll(pageble);
		if (page.isEmpty()) {
			throw new RuntimeException("No Records in this Page");
		}

		return page.toList();
	}

	public List<User> getUserBySort(){
		//return (List<User>)dao.findAll(Sort.by("userId").descending());
		return (List<User>)dao.findAll(Sort.by("userName").descending());
		
	}
	//now i want userName and acsending or descending dynamically
	public List<User> getUserBySortDynamically(String field,String sorttype){
		if(sorttype.equals("desc")) {
			return (List<User>)dao.findAll(Sort.by(field).descending());
		}
		return (List<User>)dao.findAll(Sort.by(field));
	//or by using ternary operator
	//return sorttype.equals("desc")?(List<User>)dao.findAll(Sort.by(field).descending()):(List<User>)dao.findAll( Sort.by(field));
	}
	
	
	
	
}
