package com.outreach.airflowpoc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirflowpocApplication {
	
	public static void main(String[] args) {
		//AirflowpocApplication app = new AirflowpocApplication();
		//app.createFile();
		SpringApplication.run(AirflowpocApplication.class, args);
		
	}

}
