package com.example.demo.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

 
 
import com.example.demo.model.User;
import com.example.demo.service.TestService;

public class UserItemProcessor implements ItemProcessor<User, User> {

	@Autowired
	TestService testService;

	@Value("${output.success.file}")
	FileSystemResource outputSuccessFile;

	@Override
	public User process(final User user) throws Exception {
		final String email = user.getEmailId().toUpperCase();
		final String userName = user.getUserName().toUpperCase();
		final String id = user.getId();

		String threadName = Thread.currentThread().getName();
		final User transformedPerson = new User(id, userName, email, threadName);

		//writeToFile(user);
		System.out.println(threadName + " - UserItemProcessor.process() -> "+ userName);

		try {
			Thread.sleep(3000);
		} catch(Exception e) {
		}
		return transformedPerson;
	}

	private void writeToFile(final User user) {
		List<User> users = new ArrayList<User>();
		users.add(user);
		testService.writToFile(outputSuccessFile.getFile().getAbsolutePath(), users);
	}
}
