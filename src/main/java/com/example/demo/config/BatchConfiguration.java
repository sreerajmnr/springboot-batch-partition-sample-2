package com.example.demo.config;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.example.demo.model.User;
import com.example.demo.processor.UserItemProcessor;
import com.example.demo.writer.ConsoleItemWriter;
 

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;
 
    @Bean
    public Job partitioningJob() throws Exception {
        return jobBuilderFactory.get("parallelJob")
                .incrementer(new RunIdIncrementer())
                .flow(masterStep())
                .end()
                .build();
    }

    @Bean
    public Step masterStep() throws Exception {
        return stepBuilderFactory.get("masterStep")
                .partitioner(slaveStep())
                .partitioner("partition", partitioner())
                .gridSize(25)
                .taskExecutor(masterStepTaskExecutor())
                .build();
    }
    
    @Bean
    public TaskExecutor masterStepTaskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(16);
        asyncTaskExecutor.setThreadNamePrefix("MasterThread-");
        return asyncTaskExecutor;
    }
    
    @Bean
    public TaskExecutor slaveStepTaskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(100);
        asyncTaskExecutor.setThreadNamePrefix("SlaveThread-");
        return asyncTaskExecutor;
    }

    @Bean
    public Partitioner partitioner() throws Exception {
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        partitioner.setResources(resolver.getResources("file:D:/test/PartInputFile*"));
        return partitioner;

    }

    @Bean
    public Step slaveStep() throws Exception {
        return stepBuilderFactory.get("slaveStep")
                .<User, User>chunk(100)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .taskExecutor(slaveStepTaskExecutor())
                .throttleLimit(100)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<User> reader(@Value("#{stepExecutionContext['fileName']}") String file) throws MalformedURLException {
        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
		// reader.setResource(new FileSystemResource("D:\\TestFile.csv"));
		reader.setResource(new UrlResource(file));
		reader.setLineMapper(new DefaultLineMapper<User>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "id", "userName", "emailId" });
						setDelimiter(DELIMITER_COMMA);
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
					{
						setTargetType(User.class);
					}
				});
			}
		});
		return reader;
    }

    @Bean
	public ConsoleItemWriter<User> writer() {
		return new ConsoleItemWriter<User>();
	}

	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}

}
