package com.outreach.airflowpoc.service;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.outreach.airflowpoc.controller.AirflowController;
import com.outreach.airflowpoc.dto.GenericResponse;
import com.outreach.airflowpoc.exception.AirflowException;
import com.outreach.airflowpoc.model.DagConfig;
import com.outreach.airflowpoc.util.AirflowConstants;

@Service
public class AirflowService {
	private static final Logger logger = LogManager.getLogger(AirflowController.class);
	
	@Autowired
	private ScriptGeneratorService dagScriptGenerator;
	
	
	public GenericResponse<DagConfig> createHelloWorldDag(DagConfig dagConfig) throws AirflowException {
		GenericResponse<DagConfig> response = 
				new GenericResponse<DagConfig>("Dag Configuration File Successfully created",
						dagScriptGenerator.getScriptForDag(
								dagConfig, 
								AirflowConstants.SCRIPT_FILE_PATH,
								"scripts/Hello_World_Dag.txt"));
		logger.info("Dag Configuration File has been created successfully");
		return response;
	}
	
	public GenericResponse<DagConfig> createSingerMinioFlinkDag(DagConfig dagConfig) throws AirflowException {
		GenericResponse<DagConfig> response = new GenericResponse<DagConfig>("Dag Configuration File Successfully created",
				dagScriptGenerator.getScriptForDag(
						dagConfig, 
						AirflowConstants.SCRIPT_FILE_PATH, 
						"scripts/Dag_Singer_Minio_Flink.txt"));
		logger.info("Dag Configuration File has been created successfully");
		return response;
	}
	
	public boolean uploadScripts(MultipartFile files[], String location) throws AirflowException {
		boolean status = false;
		if(files==null) {
			logger.error("Please select a valid .py file to create the DAG");			
		}else {
			for(MultipartFile file: files) {
				logger.info("files to upload: "+file.getOriginalFilename());
				if(!file.getOriginalFilename().contains(".py")) {
					logger.error(file.getOriginalFilename()+ "is an invalid input file. Please select a valid .py file to create the DAG");
				}
				
				if(file.getSize() == 0) {
					logger.error(file.getOriginalFilename()+ "is an empty input file. Please select a valid .py file to create the DAG");
				}
				
				String destination = location + file.getOriginalFilename();
				File fileToUpload = new File(destination);
				try {
					file.transferTo(fileToUpload);
					status = true;
					logger.error(file.getOriginalFilename()+ " has been uploaded to location - "+AirflowConstants.SCRIPT_FILE_PATH);
					
				} catch (Exception e) {
					logger.error("Exception occoured while saving the script file - "+file.getOriginalFilename());
				}
			}
		}
		return status;
	}
	
}
