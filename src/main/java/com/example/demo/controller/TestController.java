package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TestService;

import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

	@Autowired
	TestService testService;
	
	@GetMapping("/job/trigger")
	public String triggerJob() {
		long time = System.currentTimeMillis();
		testService.triggerJob();
		time = System.currentTimeMillis() - time;
		
		// long minutes = (milliseconds / 1000) / 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);

        // long seconds = (milliseconds / 1000);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		
		return "Job triggered... Seconds - " + seconds + ", Minutes - " + minutes;
	}
}
