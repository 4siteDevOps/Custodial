package com.newgen.iforms.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.logging.log4j.Logger;

import com.newgen.iform.CustodialConstants.CustodialConstants;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.logging.WriteToLog;

public class Commons {
	private Logger writeToLog = WriteToLog.getLoggerInstance();
	private boolean calcClickFlag;		


	public final void disableField(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Disabling Combos]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "disable", "true");
	}

	public final void enableField(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Enabling Fields]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "disable", "false");
	}

	public final String getValue(IFormReference ifrmObj, String controlName) {
		String v = ifrmObj.getValue(controlName).toString().trim();
		writeToLog.info("[Control]: "+ controlName+" Value: "+v);
		return v;
	}

	public final void setValue(IFormReference ifrmObj, String controlName, String value) {
		writeToLog.info("[Control]: "+ controlName+" Value: "+value);
		ifrmObj.setValue(controlName, value);
	}

	public final void clearField(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Clearing Fields]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setValue(c, "");
	}

	public final void clearFieldsCombos(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Clearing Fields]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setValue(c, "");
	}	

	public final void clearTable(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Clearing Tables]: "+controls.toString());
		for(String c : controls)
			ifrmObj.clearTable(c);	
	}

	public final void addIteminCombo(IFormReference ifrmObj, String controlName, String value) {
		writeToLog.info("[Adding "+value+" in the Combo]: "+controlName);

		ifrmObj.addItemInCombo(controlName, value);
	}

	public final void nonMandatoryFields(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Making Fields Non-Mandatory Combos]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "mandatory", "false");
	}

	public final void mandatoryFields(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Making Fields Mandatory Combos]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "mandatory", "true");
	}

	public final void setTabsVisible(IFormReference ifrmObj, List<String> tabName) {
		writeToLog.info("[Setting Tabs Visibility]: " + tabName.toString());
		for (String tabId : tabName) {
			ifrmObj.setTabStyleByName(CustodialConstants.PARENT_TAB_NAME, tabId, "visible", "true");
		}
	}

	public final void setTabsInVisible(IFormReference ifrmObj, List<String> tabName) {
		writeToLog.info("[Setting Tabs Visibility]: " + tabName.toString());
		for (String tabId : tabName) {
			ifrmObj.setTabStyleByName(CustodialConstants.PARENT_TAB_NAME, tabId, "visible", "false");
		}
	}

	public final void fieldsInvisible(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Making Fields Invisible Combos]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "visible", "false");
	}

	public final void fieldsVisible(IFormReference ifrmObj, List<String> controls) {
		writeToLog.info("[Making Fields Invisible Combos]: "+controls.toString());
		for(String c : controls)
			ifrmObj.setStyle(c, "visible", "true");
	}

	public final void setHeadersValues(IFormReference ifrmObj) {
		for(int i=0;i<CustodialConstants.HEADERS.length;i++) {
			writeToLog.info(String.format("[%s : %s]", CustodialConstants.HEADERS[i], CustodialConstants.TAB_APPLICATIONDETAILS[i]));
			setValue(ifrmObj, CustodialConstants.HEADERS[i] , getValue(ifrmObj, CustodialConstants.TAB_APPLICATIONDETAILS[i]));
		}
	}


	public boolean checkMandatoryTextBox(IFormReference ifrmObj, String ControlName, String CaptionName)
	{
		if (ifrmObj.getValue(ControlName).toString().trim().equalsIgnoreCase("")) {
			//ifrmObj.setNGFocus(ControlName);
			throw new ValidatorException(new FacesMessage("Please " + CaptionName + "."));
		}
		return true;
	}
	
	private boolean isInputValid(String input) {
	    return input != null && !input.trim().isEmpty();
	}
	
	public int saveDatainHistoryGrid(IFormReference ifrmObj) {
		writeToLog.info("[Inside  saveDatainHistoryGrid]:]");
		int isInserted = 0;
		try {
			String username = ifrmObj.getObjGeneralData().getM_strUserName();
			String workstepName = ifrmObj.getObjGeneralData().getM_strActivityName();
			String pid = ifrmObj.getObjGeneralData().getM_strProcessInstanceId();

			writeToLog.info("[username: "+username +" workstepName: "+workstepName);
			ifrmObj.setValue("ComingFrom", username);
			ifrmObj.setValue("PreviousWstep", workstepName);
			
			if(workstepName.equalsIgnoreCase(CustodialConstants.INITIATION_WRKSTEPNAME)) {
				String entryDateTime =null;
				String exitDateTIme, tat;

				if (workstepName.equalsIgnoreCase(CustodialConstants.INITIATION_WRKSTEPNAME)) {
					entryDateTime = ifrmObj.getObjGeneralData().getM_strCreatedDateTime();  }
				else {
					entryDateTime = getValue(ifrmObj, "EntryDateTime");
				}

				LocalDateTime dateTime = LocalDateTime.now();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime startTime = LocalDateTime.parse(entryDateTime.split("\\.")[0], formatter);
				writeToLog.info("[startTime: "+startTime );
				exitDateTIme = dateTime.format(formatter);
				//ifrmObj.setValue("ENTDATETIME", exitDateTIme);
				LocalDateTime endTime = LocalDateTime.parse(exitDateTIme, formatter);

				// Format the date and time
				Duration duration = Duration.between(startTime, endTime);

				// Convert the duration to minutes
				long minutes = duration.toMinutes();
				String fetchDec = "Select Decision from NIC_CP_DECISIONMASTER where workstepname ='"+workstepName+"'";
				writeToLog.info("[query to fetch decision field: "+fetchDec);
				List<List<String>> decs = ifrmObj.getDataFromDB(fetchDec);
				writeToLog.info("[DECS "+decs);
				String decision = getValue(ifrmObj, decs.get(0).get(0));
				writeToLog.info("[decision "+decision);
				String comments = getValue(ifrmObj, "Comments");
				
				// Escape single quotes in comments to prevent SQL issues
		        comments = comments.replace("'", "''");
		        
				String sendBackReason = "";
				if(decision.toLowerCase().contains("send back")) {
					sendBackReason = getValue(ifrmObj, "SendBackReason");
				}
				tat = String.valueOf(minutes);
				writeToLog.info("[tat: "+tat);

				 if (!isInputValid(comments)) {
			            writeToLog.warn("StatusEnterComments is empty. Skipping insertion.");
			            return isInserted;
			        }
				 
				String query = "INSERT INTO NIC_CUSTODIAL_HISTORY_DECISION (UserName,WorkStepName,EntryDT,DT,Decision,Comments,TAT,SendBackReason,WrkItemId) VALUES('"+username+"','"+workstepName+"','"+entryDateTime+"','"+exitDateTIme+"','"+decision+"','"+comments+"','"+tat+"','"+sendBackReason+"','"+pid+"')";

				writeToLog.info("[Query For Histroy Grid]:"+query);

				isInserted= ifrmObj.saveDataInDB(query);
				writeToLog.info("[Values Inserted into Histroy Grid]"+isInserted);
			}	
		}
		catch(Exception e) {
			writeToLog.info("[Exception: ]"+e.getLocalizedMessage());
		}
		return isInserted;
	}



}
