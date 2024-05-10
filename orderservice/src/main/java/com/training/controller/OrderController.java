package com.training.controller;

import java.util.List;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.training.dao.Order;
import com.training.dao.OrderRepository;

@RestController
public class OrderController {
@Autowired
OrderRepository repo;


@GetMapping("/greet")	
public String greet() {
	return "Hello from Order Service";
}

@GetMapping("/orders")
public List<Order> getAllOrders(){
	return (List<Order>) repo.findAll();
}
@GetMapping("/order/{oid}")
public Order getOrder(@PathVariable Integer oid) {
	return repo.findById(oid).orElseThrow(()->new RuntimeException("Order Not Found"));
}
@GetMapping("/orders/{uid}")
public List<Order> getOrderByUserId(@PathVariable Integer uid){
	return repo.findByUid(uid);
}

@PostMapping("/save")
public Order saveOrder(@RequestBody Order order) {
	return repo.save(order);
}
@PutMapping("/update/{oid}")
public Order updateOrder(@RequestBody  Order order,@PathVariable Integer oid) {
	List<Order> l=new ArrayList();
	
	Order existingOrder= l.stream().filter(o->o.getOid()==oid).findAny().orElseThrow(()->new RuntimeException("user not found"));
	if(existingOrder!=null) {
		existingOrder.setUid(order.getUid());
		existingOrder.setStatus(order.getStatus());
		existingOrder.setCategery(order.getCategery());
	}
	return repo.save(existingOrder);
	
}
@DeleteMapping("/delete/{oid}")
public String orderDeleteById(@PathVariable Integer oid) {
	repo.deleteById(oid);
	return "order deleted";
}
}
