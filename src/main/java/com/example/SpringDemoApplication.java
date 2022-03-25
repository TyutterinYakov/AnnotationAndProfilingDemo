package com.example;

import com.example.comp.Quoter;
import com.example.comp.TerminatorQuoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan
public class SpringDemoApplication implements CommandLineRunner {

	private final Quoter quoter;

	@Autowired
	public SpringDemoApplication(Quoter quoter) {
		this.quoter = quoter;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(SpringDemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		quoter.sayQuote();
//		while(true) {
//			quoter.setMessage("Message");
//			quoter.sayQuote();
//			System.out.println("-----------------");
//			Thread.sleep(5000);
//		}
	}
}
