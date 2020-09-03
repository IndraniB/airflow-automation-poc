package com.outreach.airflowpoc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPythonOperator extends DagTask{

	private String python_callable;

	public String getPython_callable() {
		return python_callable;
	}

	public void setPython_callable(String python_callable) {
		this.python_callable = python_callable;
	}
	
	
	
}
