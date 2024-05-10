package com.training.userservice.controllers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.training.userservice.dao.User;
import com.training.userservice.dto.Orders;
import com.training.userservice.dto.UserDto;
import com.training.userservice.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class UserController {
	@Autowired
	UserService service;
	//this is used to connect with orderservice
	@Autowired
RestTemplate restTemplate;
	 
	@Value("${orderservice.url}")
	String orderserviceurl;
	
	// List<User>userList=new ArrayList();
	/*
	 * public UserController() { userList.add(new User(1, "shreyas", "Pune",
	 * "shreyas@gmail.com")); userList.add(new User(2, "vivek", "Noida",
	 * "vivek@gmail.com")); userList.add(new User(3, "ravi", "Patna",
	 * "ravi@gmail.com")); userList.add(new User(4, "mohit", "Mumbai",
	 * "mohit@gmail.com")); userList.add(new User(5, "sandesh", "Sangli",
	 * "sandesh@gmail.com")); }
	 */
	//this is used to connect with OrderService
	//in this method we use fallback mechanism
	@Retry(name ="ordergreetfallback",fallbackMethod = "fallBackOrderGreets" )
	//@CircuitBreaker(name ="ordergreetfallback",fallbackMethod = "fallBackOrderGreet" ) 
	@GetMapping("/ordergreet")
	public String greetFromOrderService() {
		System.out.println("===================OrderGreet=================");
		//return restTemplate.getForObject("http://localhost:7070/greet", String.class);
		return restTemplate.getForObject(orderserviceurl+"/greet", String.class);
	}
	
	//fallback method for ordergreet
	
	public String fallBackOrderGreet(Throwable t) {
		return "<h1>Currently orderservice is not working.....come after some time because our DEV are sleeping</h1>";
	}
	
	
	//this is used to connect with OrderService
	//this is use for fallback mechanism so I change its return ype as ResponseEntity
	
	@CircuitBreaker(name ="UserOrdersfromOrderServicetfallback",fallbackMethod = "fallBackforUserOrdersfromOrderService" ) 
    @GetMapping("/orders/{uid}")
	//public List<Orders> getUserOrdersfromOrderService(@PathVariable Integer uid){
	public ResponseEntity<List<Orders>>  getUserOrdersfromOrderService(@PathVariable Integer uid){
	//	return restTemplate.getForObject("http://localhost:7070/orders/"+uid, List.class);
	//return restTemplate.getForObject(orderserviceurl+"/orders/"+uid, List.class);
		return new ResponseEntity<List<Orders>>(restTemplate.getForObject(orderserviceurl+"/orders/"+uid, List.class),HttpStatus.OK  );
	}
	
	public ResponseEntity<Object> fallBackforUserOrdersfromOrderService(Integer uid,Throwable t){
		System.out.println("fallback happened");
		return new ResponseEntity<Object>("<h1>Current order service is not working</h1>",HttpStatus.GATEWAY_TIMEOUT);
	}
	
	
	
	@GetMapping("/user/order/{uid}")
	public UserDto getOrdersInUserResponse(@PathVariable Integer uid){
		UserDto userDto=new UserDto();
		User user=service.getUserById(uid);
		userDto.setUserId(uid);
		userDto.setUserName(user.getUserName());
		userDto.setEmail(user.getEmail());
	//	userDto.setPassword(user.getPassword());
		userDto.setAddr(user.getAddr());
		userDto.setPayments(user.getPayments());
		
	//List<Orders>orders=	restTemplate.getForObject("http://localhost:7070/orders/"+uid,List.class);
		List<Orders>orders=	restTemplate.getForObject(orderserviceurl+"/orders/"+uid,List.class);
		userDto.setOrders(orders);
	return userDto;
	}
	
	
	
	@GetMapping(value = "/greet", produces = { "text/plain" })
	public ResponseEntity<String> greet() {
		return new ResponseEntity<String>("<h1> Hello response is created </h1>", HttpStatus.OK);
	}

//@RequestMapping("/users")
//@GetMapping(value="/users",produces = {"application/xml"})
	@GetMapping(value = "/users")
	public ResponseEntity<List<User>> getUsers(@RequestParam(defaultValue = "application/json") String mediatype) {
		// return userList;
		HttpHeaders headers = new HttpHeaders();
		headers.set("my-msg", "its response header");
		headers.set("Content-Type", mediatype);
		return new ResponseEntity<List<User>>(service.getUsers(), headers, HttpStatus.OK);
		/*
		 * or return new ResponseEntity().ok(HttpStatus.OK).header("my-msg",
		 * "its response header").body(service.getUsers());
		 */
	}

//@RequestMapping("/user/{uid}")
	@GetMapping("/user/{uid}")
	public ResponseEntity<User> getUserById(@PathVariable(name = "uid") int uid) {
		// User u=null;
		/*
		 * User u=userList.stream().filter(n->n.getUserId()==uid).findAny().get();
		 * return u;
		 */
		/*
		 * for(User user:userList) { if(user.getUserId()==uid) { u=user; } } return u;
		 */
		// u=userList.stream().filter(n->n.getUserId()==uid).findAny().orElse(null);
		// return u;
		return new ResponseEntity<User>(service.getUserById(uid), HttpStatus.OK);

	}
	/*
	 * @RequestMapping("/user") public User getUserByIdRequestParam(@RequestParam
	 * int uid,@RequestParam String username) { User u=null;
	 * u=userList.stream().filter(n->n.getUserId()==uid).findAny().orElse(null);
	 * return u; }
	 */

//@RequestMapping(value="/save",method = RequestMethod.POST)
	@PostMapping(value = "/save", consumes = { "application/json" })
	public ResponseEntity<User> saveUser(@RequestBody User u) {
//	userList.add(u);
//return	userList.stream().filter(user->user.getUserId()==u.getUserId()).findAny().orElse(null);
		/*
		 * if(service.isUserPresent(u.getUserId())!=null) { return new
		 * ResponseEntity<User>(service.addUser(u), HttpStatus.CONFLICT); }
		 */
		if (service.isUserPresent(u.getUserId())) {
			return new ResponseEntity<User>(service.addUser(u), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<User>(service.addUser(u), HttpStatus.CREATED);

	}

//Durgesh and my Logic
	/*
	 * @RequestMapping(value="/update/{userId}",method=RequestMethod.PUT) public
	 * User updateUser(@RequestBody User user,@PathVariable("userId") Integer
	 * userId) { return userList.stream().map(b->{ if(b.getUserId()==userId) {
	 * b.setAddr(user.getAddr()); b.setEmail(user.getEmail()); } return b;
	 * }).findAny().orElse(null);
	 * 
	 * 
	 * 
	 * }
	 */
//@RequestMapping(value="/update/{userid}",method = RequestMethod.PUT)
	@PutMapping("/update/{userid}")
	public ResponseEntity<User> updateEntireUser(@PathVariable Integer userid, @RequestBody User user) {
		/*
		 * User
		 * existinguser=userList.stream().filter(usr->usr.getUserId()==userid).findAny()
		 * .orElse(null); if(existinguser!=null) {
		 * existinguser.setUserName(user.getUserName());
		 * existinguser.setAddr(user.getAddr()); existinguser.setEmail(user.getEmail());
		 * } return existinguser;
		 */
		return new ResponseEntity<User>(service.updateUser(userid, user), HttpStatus.CREATED);

		// service.updateUser(userid, user);

	}

//@RequestMapping(value = "/partial/{userid}",method = RequestMethod.PATCH)
	@PatchMapping("/partial/{userid}")
	public ResponseEntity<User> updatePartialUser(@RequestBody User user, @PathVariable("userid") Integer userid) {
		/*
		 * User
		 * existingUser=userList.stream().filter(usr->usr.getUserId()==userid).findAny()
		 * .orElse(null); if(existingUser!=null) { //String
		 * name=user.getUserName()!=null?user.getUserName():existingUser.getUserName();
		 * existingUser.setUserName(user.getUserName()!=null?user.getUserName():
		 * existingUser.getUserName());
		 * existingUser.setAddr(user.getAddr()!=null?user.getAddr():existingUser.getAddr
		 * ());
		 * existingUser.setEmail(user.getEmail()!=null?user.getEmail():user.getEmail());
		 * }
		 * 
		 * 
		 * return existingUser;
		 */

		return new ResponseEntity<User>(service.updateUserPartial(userid, user), HttpStatus.CREATED);

	}

//Use of Reflection Api if we number of Field
//@RequestMapping(value = "/updatefield/{userId}",method = RequestMethod.PATCH)
	@PatchMapping("/updatefield/{userId}")
	public ResponseEntity<User> updateUserField(@PathVariable Integer userId,
			@RequestBody Map<String, String> fieldsMap) {
		// User
		// existinguser=userList.stream().filter(usr->usr.getUserId()==userId).findAny().orElse(null);

//Set<String>fieldSet=fieldsMap.keySet();
//System.out.println(fieldSet);
//Tradition approach
		/*
		 * for(String userField:fieldSet) { Field field
		 * =ReflectionUtils.findField(User.class, userField); field.setAccessible(true);
		 * ReflectionUtils.setField(field, existinguser, fieldsMap.get(userField)); }
		 */
//Here are using Modern approch
		/*
		 * fieldsMap.forEach((k,v)->{ Field field =ReflectionUtils.findField(User.class,
		 * k); field.setAccessible(true); //sql querry ReflectionUtils.setField(field,
		 * existinguser, v); }); return existinguser;
		 */
		return new ResponseEntity<User>(service.updateUserByReflectionApi(userId, fieldsMap), HttpStatus.CREATED);

	}

//@RequestMapping(value="/delete/{userid}",method = RequestMethod.DELETE)
	@DeleteMapping("/delete/{userid}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userid) {
		/*
		 * User
		 * existingUser=userList.stream().filter(usr->usr.getUserId()==userid).findAny()
		 * .orElse(null); if(userList.remove(existingUser)) {
		 * 
		 * return "user_deleted"; } return "not_deleted";
		 */
		return new ResponseEntity<String>(service.deleteUser(userid), HttpStatus.ACCEPTED);

	}

//produces = {"application/json"}
	@GetMapping("/userbyname/{name}")
	public ResponseEntity<User> getUserByName(@PathVariable String name) {
		return new ResponseEntity<User>(service.getUserByName(name), HttpStatus.OK);
	}
	// i commented below line because right now i created address as entity class and address no longer as string data type 
//   @GetMapping("/userbyaddr/{addr}")
//	public ResponseEntity<List<User>> getByAddr(@PathVariable String addr) {
//		return new ResponseEntity<List<User>>(service.getUserByAddr(addr), HttpStatus.OK);
//		}

	@GetMapping("/userbyemail/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<User>(service.getUserByEmail(email), HttpStatus.OK);
	}
	// i commented below line because right now i created address as entity class and address no longer as string data type
//	@GetMapping("/usernameaddr/{address}")
//	public ResponseEntity<List<String>> getUserNameByAddr(@PathVariable String address) {
//		return new ResponseEntity<List<String>>(service.getUserNameByAddr(address), HttpStatus.OK);
//	}

	@GetMapping("/userbypage/{pageSize}/{pagenumber}")
	public ResponseEntity<List<User>> getUserPage(@PathVariable Integer pagenumber, @PathVariable Integer pageSize) {
		return new ResponseEntity<List<User>>(service.getUsersByPage(pagenumber, pageSize), HttpStatus.OK);
	}
	@GetMapping("/userbysort")
	public ResponseEntity<List<User>> getUserBySort(){
		return new ResponseEntity<List<User>>(service.getUserBySort(), HttpStatus.OK);
	}
	@GetMapping("userbysortdynamically/{field}/{sorttype}")
	public ResponseEntity<List<User>> getUserBySort(@PathVariable  String field,@PathVariable  String sorttype){
		return new ResponseEntity<List<User>>(service.getUserBySortDynamically( field,sorttype), HttpStatus.OK);
}
}