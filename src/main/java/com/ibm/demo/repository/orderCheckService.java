package com.ibm.demo.repository;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.demo.model.offerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class orderCheckService 
{

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
		
		return "redirect:success";
	}
	
	private String failOrderService(offerService offer, String Url)
	{
        return "redirect:failOrder";
    }
}
