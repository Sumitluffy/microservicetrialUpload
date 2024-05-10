package com.training.userservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends JpaRepository<User, Integer>{
public User findByuserName(String username );


//i commented below line because right now i created address as entity class and address no longer as string data type 
//public List<User> getByaddr(String addr);
//@Query(value="select * from user where email='ravi@gmail.com'",nativeQuery =true)
//public User getUserEmail();


@Query(value="select * from user where email= :usermail",nativeQuery =true)
public User getUserEmail(String usermail);


// i commented below line because right now i created address as entity class and address no longer as string data type 
//@Query(value ="select user_name from user where addr=:address",nativeQuery =true )
//public List<String> getUserNameByAddr(String address);





}
