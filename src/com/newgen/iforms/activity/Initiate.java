package com.newgen.iforms.activity;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;

import com.newgen.iform.CustodialConstants.CustodialConstants;
import com.newgen.iforms.EControl;
import com.newgen.iforms.FormDef;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.iforms.logging.WriteToLog;
import com.newgen.mvcbeans.model.WorkdeskModel;

public class Initiate extends Commons implements IFormServerEventHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1417361051617187776L;
	private Logger writeToLog = WriteToLog.getLoggerInstance();

	@Override
	public void beforeFormLoad(FormDef arg0, IFormReference arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public String executeCustomService(FormDef arg0, IFormReference arg1, String arg2, String arg3, String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray executeEvent(FormDef arg0, IFormReference arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeServerEvent(IFormReference ifrmObj, String sControlName, String pEventType, String sData) {
		writeToLog.info("[Executing Server Event]: " + sControlName + " [pEventType]: " + pEventType);

		switch (pEventType) {

		case CustodialConstants.ON_LOAD: {
			
			
			if (getValue(ifrmObj, "Loggedindate").isEmpty()) {
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String formattedDate = currentDate.format(formatter);
				writeToLog.info("[Current date in DD/MM/YYYY format]:" + formattedDate);
				setValue(ifrmObj, "Loggedindate", formattedDate);
			}

			String username = ifrmObj.getObjGeneralData().getM_strUserName();
			writeToLog.info("[Logged in user]:" + username);
			ifrmObj.setValue("Loggedinuser", username);

			writeToLog.info("[Enabling Initiation Fields]");
			ifrmObj.applyGroup(CustodialConstants.INITATION_ENABLE);

			break;
		}

		case CustodialConstants.ADD_HISTORY: {
			if (CustodialConstants.ADD_HISTORY_Q_DECISIONHISTORY.equalsIgnoreCase(sControlName)) {
				String comments = getValue(ifrmObj, "Comments");

				int i = saveDatainHistoryGrid(ifrmObj);
				if (i <= 0) {
					writeToLog.info("[Failed to insert into comments history. Comment: " + comments + "]");
				} else {
					writeToLog.info("[Inserted successfully in comments history. Comment: " + comments + "]");
				}
			}

			break;
		}

		default:
			writeToLog.info("[Unhandled server event]: " + pEventType);
		}

		return null;
	}


	@Override
	public String generateHTML(EControl arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomFilterXML(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean introduceWorkItemInSpecificProcess(IFormReference arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String introduceWorkItemInWorkFlow(IFormReference arg0, HttpServletRequest arg1, HttpServletResponse arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String introduceWorkItemInWorkFlow(IFormReference arg0, HttpServletRequest arg1, HttpServletResponse arg2,
			WorkdeskModel arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String onChangeEventServerSide(IFormReference arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postHookExportToPDF(IFormReference arg0, File arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postHookOnDocumentOperations(IFormReference arg0, String arg1, String arg2, int arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHookOnDocumentUpload(IFormReference arg0, String arg1, String arg2, File arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setMaskedValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public void updateDataInWidget(IFormReference arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public String validateDocumentConfiguration(String arg0, String arg1, File arg2, Locale arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray validateSubmittedForm(FormDef arg0, IFormReference arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
