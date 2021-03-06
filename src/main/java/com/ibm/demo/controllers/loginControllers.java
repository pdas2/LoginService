package com.ibm.demo.controllers;

import com.carrotsearch.ant.tasks.junit4.dependencies.org.simpleframework.xml.Order;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.ibm.demo.model.Account;
import com.ibm.demo.model.offerService;
import com.ibm.demo.producer.Producer;
import com.ibm.demo.repository.AccountSearchRepository;
import com.ibm.demo.repository.offerMongoRepository;
import com.ibm.demo.repository.orderCheckService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ibm.demo.repository.offerMongoRepository;
import com.ibm.demo.repository.AccountRepository;
import com.ibm.demo.properties.*;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller

public class loginControllers {
	
	public static final Logger logger = LoggerFactory.getLogger(loginControllers.class);
	public String userName;
	public String Error;
	public String userAccId;
	public String userPhone;
	
	@Value("${OrderService.URL}")
	private String OrderUrl;
	
	@Value("${OrderService.PORT}")
	private String OrderPort;
	
	
	@Autowired
	AccountSearchRepository accountSearchRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	offerMongoRepository offerRepository;
	
	@Autowired
	orderCheckService orderservice;
	
	@Autowired
	Producer producer;
	
    @Autowired
    private Environment environment;
    
    /*
    @Autowired
    private OrderProperties orderProperties;
	*/
    
   
	@RequestMapping("/home")
	public String home1(Model model) {
		
		//System.out.println("URL="+"test");
		 model.addAttribute("userName", userName);
		 model.addAttribute("userAccId", userAccId);
		 model.addAttribute("userPhone", userPhone);
		 
		 //System.out.println("URL="+url);
		 
		// Fetching Order Service URL : PORT from application.properties
		 //model.addAttribute("orderServiceURL", environment.getProperty("OrderService.URL"));
		 //model.addAttribute("orderServicePORT", environment.getProperty("OrderService.PORT"));
		 
		 model.addAttribute("orderServiceURL", OrderUrl);
		 model.addAttribute("orderServicePORT", OrderPort);
		 
		return "home";
	}
	
	
	@RequestMapping("/login" )
	public String home(Model model) {
		
		model.addAttribute("userList", Error);
		return "login";
	}
	
	
	@RequestMapping("/success" )
	public String home2(Model model) {
		
		model.addAttribute("userList", "Thank you "+userName+" for choosing the offer." );
		model.addAttribute("userList1", "The offer have been successfully added to your account" );
		model.addAttribute("userList2", "Account would be updated shortly" );
		return "success";
	}
	

	@RequestMapping("/failOrder" )
	public String home3(Model model) {
		
		model.addAttribute("userList", "No Response From Order Service at this moment." );
		model.addAttribute("userList1", "Service will be back shortly." );
		
		return "failorder";
	}
	
	@NewSpan
	@RequestMapping(value = "/addOffer", method = RequestMethod.POST)
	
	public String addOffer(@ModelAttribute offerService offer) throws JSONException 
	{
		String Url = "http://"+OrderUrl+":"+OrderPort+"/offer/addOffer";
         
		String data=orderservice.callOrder(offer,Url);
		/*
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		json.put("userPhone",offer.getuserPhone());
		json.put("userName",offer.getuserName());
		json.put("userAccId",offer.getuserAccId());
		json.put("offerid",offer.getofferid());

		HttpEntity <String> httpEntity = new HttpEntity <String> (json.toString(), httpHeaders);

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(Url, httpEntity, String.class);

		JSONObject jsonObj = new JSONObject(response);
		String data = jsonObj.get("data").toString(); 
		System.out.println(data);
		*/
		//return "redirect:success";
		return data;
	}
	/*
	@HystrixCommand(fallbackMethod = "failOrderService")
	public String callOrder(offerService offer, String Url) throws JSONException
	{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		json.put("userPhone",offer.getuserPhone());
		json.put("userName",offer.getuserName());
		json.put("userAccId",offer.getuserAccId());
		json.put("offerid",offer.getofferid());

		HttpEntity <String> httpEntity = new HttpEntity <String> (json.toString(), httpHeaders);

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(Url, httpEntity, String.class);

		JSONObject jsonObj = new JSONObject(response);
		String data = jsonObj.get("data").toString(); 
		System.out.println(data);
		
		return data;
	}
	
	private String failOrderService()
	{
        return "redirect:failOrder";
    }
    */
	/*
	@RequestMapping(value = "/addOffer", method = RequestMethod.POST)
	public String addOffer(@ModelAttribute offerService offer) {
		
		String msg="Offer successfully ordered to Account No."+offer.getuserAccId();
		
		offerRepository.save(offer);
		
		 Gson gson = new Gson();
	     String json = gson.toJson(offer);
		producer.produceMsg(json);
		logger.info(json);
		
		userName=offer.getuserName();
		
		logger.info("Offer will be activated within 24 hrs for User: "+offer.getuserName());
		return "redirect:success";
		//returns "Offer will be activated within 24 hrs for User: "+offer.getuserName();
	}
	*/
	
	@NewSpan
	 @RequestMapping(value = "/login" , method = RequestMethod.POST )
	    public String loginSubmit(Model model, @RequestParam String userid,@RequestParam String password) {
		 // model.addAttribute("userList", accountSearchRepository.searchuid(userid,password));
		  
		 Account acc =accountSearchRepository.searchuid(userid,password);
			//model.addAttribute("search", userid);
		 ModelMap model1 = new ModelMap();
		 
		
		// model1.put("userList", "prosenjit");
		 
			if(acc==null)
			{
				
				Error="No User details found";
				
				logger.info(Error);
				
			return "redirect:login";
			}
			else
			{
				userName=acc.getname();
				userAccId=acc.getaccId();
				userPhone=acc.getPhone();
				
				 model.addAttribute("userList", acc.getname());
				 
				 
				 
				System.out.println("Name="+acc.getname());
				
				logger.info(userName+" logged in successfully");
			return "redirect:home";
			}
	    }

}
