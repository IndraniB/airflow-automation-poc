package com.outreach.airflowpoc.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.outreach.airflowpoc.controller.AirflowController;
import com.outreach.airflowpoc.exception.AirflowException;
import com.outreach.airflowpoc.model.DagConfig;
import com.outreach.airflowpoc.model.DagTask;
import com.outreach.airflowpoc.model.TaskPythonOperator;
import com.outreach.airflowpoc.util.AirflowConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ScriptGeneratorService {
	
	private static final Logger logger = LogManager.getLogger(ScriptGeneratorService.class);
	Properties prop = new Properties();
	
	private void loadProperties() {
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("application.properties");
			if(is!=null) {
				prop.load(is);
			}else {
				System.out.println("File not found exception");
			}
		}catch(Exception ex) {
			System.out.println("ex.getMessage()::::: "+ex.getMessage());
		}
		
	};
	
	private void createDagScriptFile(String location, String dagFileName, String script) throws AirflowException {
		String absoluteFilePath = location + dagFileName+".py";
		File file = new File(absoluteFilePath);
        try {
			if(file.createNewFile())
				logger.info("File Created: "+absoluteFilePath);
			else 
				logger.info("File already exists: "+absoluteFilePath);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(absoluteFilePath));
	        writer.write(script);        
	        writer.close();
	        
        }catch(Exception ex) {
        	logger.error(ex.getMessage());
			throw new AirflowException(ex.getMessage());
        }
	}
	
	private StringBuffer getScriptForTask(DagTask task) throws AirflowException {
		StringBuffer script = new StringBuffer();
		
		switch(task.getOperatorType()) {
			case AirflowConstants.DummyOperator:{
				script = getScriptForDummyOperator(task, "");
				break;
			}
			case AirflowConstants.PythonOperator:{
				script = getScriptForPythonOperator(task, "");
				break;
			}
			case AirflowConstants.BashOperator:{
				script = getScriptForBashOperator(task, "scripts/Task_BashOperator.txt");
				break;
			}
		}
		return script;
	}
	
	private StringBuffer getScriptForDummyOperator(DagTask task, String templateScript) {
		StringBuffer script = new StringBuffer();
		script.append(task.getOperatorType() +" = DummyOperator(task_id='"+task.getTaskId()+"', retries="+task.getRetries()+", dag="+task.getDag()+")" );
		script.append("\n");
		
		return script;
	}
	
	private StringBuffer getScriptForPythonOperator(DagTask task, String templateScript) {
		StringBuffer script = new StringBuffer();
		script.append(task.getOperatorType()+" = PythonOperator(task_id='"+task.getTaskId()+"', python_callable="+task.getPython_callable()+", dag="+task.getDag()+")" );
		script.append("\n");
		
		return script;
	}
	
	private StringBuffer getScriptForBashOperator(DagTask task, String templateScript) throws AirflowException {
		
		StringBuffer script = new StringBuffer();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream(templateScript);
		InputStreamReader streamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(streamReader);
		

		try {
			for (String line; (line = reader.readLine()) != null;) {
				
				if(line.contains("+")) {
					String str="";
					String tempToken="";
					
					StringTokenizer st = new StringTokenizer(line, "+");
					while(st.hasMoreTokens()) {
						tempToken=st.nextToken();
						if(tempToken.contains("AirflowConstants.")) {
							tempToken =tempToken.substring(tempToken.lastIndexOf('.') + 1);
							if(prop.getProperty(tempToken) != null || !(prop.getProperty(tempToken).equalsIgnoreCase(""))) {
								str+= prop.getProperty(tempToken);	
							}
						}else if(tempToken.contains("AirflowVariable.")) {
							
							tempToken =tempToken.substring(tempToken.lastIndexOf('.') + 1);
							PropertyDescriptor pd = new PropertyDescriptor(tempToken, task.getClass());
							Method getter = pd.getReadMethod();
							Object obj = getter.invoke(task);
							if(obj != null || !(obj.toString().equalsIgnoreCase(""))) {
								str+= obj.toString();	
							}							
						}else {
							str+= tempToken;
						}
					}
					script.append(str);
				}else {
					script.append(line);
				} 
				script.append("\n");  
			}
		} catch (Exception e) {
			throw new AirflowException(e.getMessage());
		}
		
		return script;
	}
	
	public DagConfig getScriptForDag(DagConfig dagConfig, String location, String templateScript) throws AirflowException {
		StringBuffer script = new StringBuffer();
		loadProperties();
		
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream(templateScript);
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(streamReader);
			
			for (String line; (line = reader.readLine()) != null;) {
				if(line.contains("+")) {
					String str="";
					String tempToken="";
					
					StringTokenizer st = new StringTokenizer(line, "+");
					while(st.hasMoreTokens()) {
						tempToken=st.nextToken();
						if(tempToken.contains("AirflowConstants.")) {
							tempToken =tempToken.substring(tempToken.lastIndexOf('.') + 1);
							if(prop.getProperty(tempToken) != null || !(prop.getProperty(tempToken).equalsIgnoreCase(""))) {
								str+= prop.getProperty(tempToken);	
							}
						}else if(tempToken.contains("AirflowVariable.")) {
							tempToken =tempToken.substring(tempToken.lastIndexOf('.') + 1);
							PropertyDescriptor pd = new PropertyDescriptor(tempToken, dagConfig.getClass());
							Method getter = pd.getReadMethod();
							Object obj = getter.invoke(dagConfig);
							if(obj != null || !(obj.toString().equalsIgnoreCase(""))) {
								str+= obj.toString();
							}
						}else {
							str+= tempToken;
						}
					}
					script.append(str);
				}else if(line.contains("<< ADD TASKS HERE >>")) {
					
					if(dagConfig.getTaskLists() !=null) {
						for(DagTask task : dagConfig.getTaskLists()){
							System.out.println(task);
							StringBuffer scriptTask = getScriptForTask(task);
							System.out.println("scriptTask: "+scriptTask);
							script.append(scriptTask);
							script.append("\n");
						}
					}
				}else if(line.contains("<< ADD TASKS PRIORITY HERE >>")) {
					for(String priority : dagConfig.getDagPriority()){
						script.append(priority);
						script.append("\n");
					}
				}else {
					script.append(line);
				} 
				script.append("\n");   
			}
		} catch (Exception ex) {
			throw new AirflowException(ex.getMessage());
		}
		createDagScriptFile(location, dagConfig.getDagName(), script.toString() );
		return dagConfig; 
	}

}
