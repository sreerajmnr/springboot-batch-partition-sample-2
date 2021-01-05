package com.example.demo.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

 
import com.example.demo.model.User;
import com.example.demo.service.TestService;

public class ConsoleItemWriter<T> implements ItemWriter<T> {

	@Autowired
	TestService testService;

	@Value("${output.success.file}")
	FileSystemResource outputSuccessFile;

	@Override
	public void write(List<? extends T> items) throws Exception {
		//System.out.println("ConsoleItemWriter.write() -> Writer");
		String threadName = Thread.currentThread().getName();
		
		System.out.println(threadName + " - ConsoleItemWriter.write() -> Chunck size "+ items.size());
		testService.writToFile(outputSuccessFile.getFile().getAbsolutePath(), (List<User>) items);
	}
}
