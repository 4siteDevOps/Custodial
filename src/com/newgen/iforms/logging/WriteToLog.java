package com.newgen.iforms.logging;


import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import com.newgen.iform.CustodialConstants.CustodialConstants;
import com.newgen.iforms.custom.IFormReference;
import org.apache.logging.log4j.Logger;

public class WriteToLog {
	
	private static String workitemNumber; 
	private static String workstepName;
	
	public WriteToLog() {
	}
	
	public WriteToLog(IFormReference refObj) {
		WriteToLog.workitemNumber = refObj.getObjGeneralData().getM_strProcessInstanceId();
		WriteToLog.workstepName = refObj.getObjGeneralData().getM_strActivityName();
	}
	
	public static Logger getLoggerInstance() {
		System.setProperty("logFolder", getWorkitemNumber());
		System.setProperty("logFileName", getWorkstepName());
		// Get the logger context and start it with the specified configuration file
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(Paths.get(CustodialConstants.LOG_CONFIGFILELOCATION).toUri());

        // Get the logger
        Logger logger = LogManager.getLogger();
		return logger;
	}
	
	
	public static String getWorkitemNumber() {
		return workitemNumber;
	}
	
	public static String getWorkstepName() {
		return workstepName;
	}
}
