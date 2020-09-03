package com.outreach.airflowpoc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DagTask {

	private String task_id;
	private String task_name;
	private String operatorType;
	private String retries;
	private String dag;
	private String python_callable;
	private String command;
	
	public String getTaskId() {
		return task_id;
	}
	public void setTaskId(String taskId) {
		this.task_id = taskId;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	public String getRetries() {
		return retries;
	}
	public void setRetries(String retries) {
		this.retries = retries;
	}
	public String getDag() {
		return dag;
	}
	public void setDag(String dag) {
		this.dag = dag;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getPython_callable() {
		return python_callable;
	}
	public void setPython_callable(String python_callable) {
		this.python_callable = python_callable;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
