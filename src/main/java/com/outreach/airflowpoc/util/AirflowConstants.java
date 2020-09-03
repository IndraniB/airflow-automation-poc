package com.outreach.airflowpoc.util;

public class AirflowConstants {

	
	
	public static final String DummyOperator = "DummyOperator";
	public static final String PythonOperator = "PythonOperator";
	public static final String BashOperator = "BashOperator";

	public static final String SLACK_CONN_ID = "slack";
	static String fs = System.getProperty("file.separator");
	public static final String SCRIPT_FILE_PATH = fs+"Users"+fs+"indranibandyopadhyay"+fs+"Documents"+fs+"files"+fs;
	
	//dag types
	public static final String HELLO_WORLD_DAG = "hello-world-dag";
	public static final String SINGER_MINIO_FLINK_DAG = "singer-minio-flink-dag";
}
