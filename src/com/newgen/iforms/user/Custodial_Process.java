/**
 * 
 */
package com.newgen.iforms.user;

import org.apache.logging.log4j.Logger;

import com.newgen.iform.CustodialConstants.CustodialConstants;
import com.newgen.iforms.activity.Authorizer;
import com.newgen.iforms.activity.DocsPending;
import com.newgen.iforms.activity.Initiate;
import com.newgen.iforms.activity.Inputter;
import com.newgen.iforms.custom.IFormListenerFactory;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;
import com.newgen.iforms.logging.WriteToLog;

public class Custodial_Process implements IFormListenerFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6686806708712794113L;

	@Override
	public IFormServerEventHandler getClassInstance(IFormReference ifrobj) {

		new WriteToLog(ifrobj);
		Logger LogWriter = WriteToLog.getLoggerInstance();
		ifrobj.applyGroup("DisableAll");

		LogWriter.info("[Workstep Name]: "+ ifrobj.getObjGeneralData().getM_strActivityName());

		ifrobj.setValue("WorkItemId", extractWorkItemId(ifrobj.getObjGeneralData().getM_strProcessInstanceId()));
		

		ifrobj.setValue("LOGGEDINUSER", ifrobj.getUserName());

		String actName =  ifrobj.getObjGeneralData().getM_strActivityName();
		String mode = ifrobj.getObjGeneralData().getM_strMode();

		LogWriter.info("[Workitem General Data]: "+ ifrobj.getObjGeneralData().getGeneralData());
		LogWriter.info("[Workitem Mode]: "+ mode);


		if(mode.equalsIgnoreCase("R")) {
			ifrobj.applyGroup("DisableAll");
		}
		 if(CustodialConstants.INITIATION_WRKSTEPNAME.equalsIgnoreCase(actName)) {
			LogWriter.info("[calling Initation Workstep class]");
			return new Initiate();

		} else if(CustodialConstants.INPUTTER_WRKSTEPNAME.equalsIgnoreCase(actName)) {
			LogWriter.info("[calling Inputter Workstep class]");
			return new Inputter();

		} else if(CustodialConstants.DOCS_PENDING_WRKSTEPNAME.equalsIgnoreCase(actName)) {
			LogWriter.info("[calling Docs Pending Workstep class]");
			return new DocsPending();

		} else if(CustodialConstants.AUTHORIZER_WRKSTEPNAME.equalsIgnoreCase(actName)) {
			LogWriter.info("[calling Authorizer Workstep class]");
			return new Authorizer();

		}
		
		return null;


	}

    // --- Helper method to extract WorkItemId ---
    public static String extractWorkItemId(String WorkItemId) {
        if (WorkItemId == null || WorkItemId.isEmpty()) {
            throw new IllegalArgumentException("Work item ID cannot be null or empty.");
        }

        // Use regex to match and extract the numeric part between the hyphens
        String numericPart = WorkItemId.replaceAll(".*-(\\d+)-.*", "$1");

        if (numericPart.isEmpty()) {
            throw new IllegalArgumentException("No numeric part found in the work item ID.");
        }

        int number = Integer.parseInt(numericPart); // strip leading zeros
        return "CP-" + number;
    }
}
