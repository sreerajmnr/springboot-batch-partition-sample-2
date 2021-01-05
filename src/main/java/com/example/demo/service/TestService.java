package com.example.demo.service;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;

 

@Service
public class TestService {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public void triggerJob() {
		System.out.println("Job triggered...");
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		try {
			jobLauncher.run(job, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void writToFile(String fileName, List<User> users) {
		File file = new File(fileName);// "D:\\TestFile.csv"
		try {
			FileWriter myWriter = new FileWriter(file, true);
			for (User user : users) {
				String data = user.getId() + "," + user.getUserName() + "," + user.getEmailId() + "," + user.getThreadName() + "\n";
				myWriter.write(data);
				//System.out.println("TestService.writToFile() -> " + data);
			}
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
