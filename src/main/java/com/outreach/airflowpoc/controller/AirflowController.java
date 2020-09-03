package com.outreach.airflowpoc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.outreach.airflowpoc.dto.GenericResponse;
import com.outreach.airflowpoc.exception.AirflowException;
import com.outreach.airflowpoc.model.DagConfig;
import com.outreach.airflowpoc.service.AirflowService;
import com.outreach.airflowpoc.util.AirflowConstants;


@RestController
@RequestMapping("airflow")
@CrossOrigin(origins="", allowedHeaders="*")
public class AirflowController {

	private static final Logger logger = LogManager.getLogger(AirflowController.class);
	
	@Autowired
	private AirflowService service;
	
	@RequestMapping(method=RequestMethod.POST, value="/hollo-world-dag")
	public ResponseEntity<Object> createHelloWorldDag(
			@RequestPart(value="files", required=true) MultipartFile files[],
			@RequestPart(value="dagConfig", required=true) DagConfig dagConfig) 
			throws AirflowException{
		
		GenericResponse<DagConfig> response=null;
		if(files!=null) {
			service.uploadScripts(files, AirflowConstants.SCRIPT_FILE_PATH);
		}
		response = service.createHelloWorldDag(dagConfig);
		return new ResponseEntity(response, HttpStatus.OK);
		
	}

	//https://medium.com/@pankajsingla_24995/multipart-request-with-request-body-using-spring-boot-and-test-using-postman-6ea46b71b75d
	@SuppressWarnings("unchecked")
	@PostMapping(value="/singer-minio-flink-dag")
	public ResponseEntity createDag1(
			@RequestPart(value="files", required=true) MultipartFile files[],
			@RequestPart(value="dagConfig", required=true) DagConfig dagConfig)
		throws AirflowException{
		
		GenericResponse<DagConfig> response=null;
		if(files!=null) {
			service.uploadScripts(files, AirflowConstants.SCRIPT_FILE_PATH);
		}
		response = service.createSingerMinioFlinkDag(dagConfig);
		return new ResponseEntity(response, HttpStatus.OK);
	}
}
