package com.andre.servicio.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andre.servicio.app.oauth.models.entity.UserApi;

@FeignClient(name="service-users")
public interface UserFeignClient {
	
	@GetMapping("users/search/find-username")
    public UserApi findByUsername(@RequestParam String username);

}
