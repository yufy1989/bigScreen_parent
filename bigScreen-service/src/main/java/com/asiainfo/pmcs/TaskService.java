package com.asiainfo.pmcs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
	private static Logger logger = LoggerFactory.getLogger(TaskService.class);
	public void run() {
		logger.info(" test1......................................" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

	}

	public void run1() {
		logger.info(" test2.........................." +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
}
