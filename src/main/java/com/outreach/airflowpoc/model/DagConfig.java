package com.outreach.airflowpoc.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DagConfig {
	
	private String dagName;
	private String dagDesc;
	private String scheduleInterval;
	private String startDate;
	private ArrayList<String> dagPriority;
	private ArrayList<DagTask> taskLists;
	private String owner;
	private String depends_on_past; //True or False (default)
	private String start_date;
	private String email;
	private String email_on_failure="true";
	private String email_on_retry="false";
	private int retries=2;
	
	
	public String getDagName() {
		return dagName;
	}
	public void setDagName(String dagName) {
		this.dagName = dagName;
	}
	public String getDagDesc() {
		return dagDesc;
	}
	public void setDagDesc(String dagDesc) {
		this.dagDesc = dagDesc;
	}
	public String getScheduleInterval() {
		return scheduleInterval;
	}
	public void setScheduleInterval(String scheduleInterval) {
		this.scheduleInterval = scheduleInterval;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public ArrayList<String> getDagPriority() {
		return dagPriority;
	}
	public void setDagPriority(ArrayList<String> dagPriority) {
		this.dagPriority = dagPriority;
	}
	public ArrayList<DagTask> getTaskLists() {
		return taskLists;
	}
	public void setTaskLists(ArrayList<DagTask> taskLists) {
		this.taskLists = taskLists;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDepends_on_past() {
		return depends_on_past;
	}
	public void setDepends_on_past(String depends_on_past) {
		if(depends_on_past == null)
			this.depends_on_past="False";
		this.depends_on_past = depends_on_past;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail_on_failure() {
		return email_on_failure;
	}
	public void setEmail_on_failure(String email_on_failure) {
		if(email_on_failure ==null)
			email_on_failure="True";
		this.email_on_failure = email_on_failure;
	}
	public String getEmail_on_retry() {
		return email_on_retry;
	}
	public void setEmail_on_retry(String email_on_retry) {
		if(email_on_retry == null)
			email_on_retry="False";
		this.email_on_retry = email_on_retry;
	}
	public int getRetries() {
		return retries;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	
}
