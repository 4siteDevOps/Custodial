package com.newgen.iform.CustodialConstants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class CustodialConstants {
	//Log Configuration Location
	public static final String LOG_CONFIGFILELOCATION = System.getProperty("user.dir") + File.separator +  "ProcessLogConfig" + File.separator + "CustodialLog4j2.xml";
	public static final String T24_CONFIGFILELOCATION = System.getProperty("user.dir") + File.separator +  "OnlineAO.properties";
	public static final String BRMS_CONFIGFILELOCATION = System.getProperty("user.dir") + File.separator + "BRMSSocket.properties";
	public static final String IPRS_CONFIGURATION = System.getProperty("user.dir") + File.separator +  "OnlineAO.properties";


	//Process Name
	public static final String REQUESTTYPE = "CustodialProcess";

	//Events
	public static final String ON_LOAD = "ON_LOAD";
	public static final String ON_CHANGE = "ON_CHANGE";
	public static final String ON_CLICK = "ON_CLICK";
	public static final String ON_FOCUS = "ON_FOCUS";
	public static final String ON_LOAD_LISTVIEW = "ON_LOAD_LISTVIEW";
	public static final String ADD_HISTORY = "ADD_HISTORY";


	//Work Step
	public static final String INITIATION_WRKSTEPNAME = "Initiate";
	public static final String INPUTTER_WRKSTEPNAME = "Custodial Inputter";
	public static final String DOCS_PENDING_WRKSTEPNAME = "Docs Pending";
	public static final String AUTHORIZER_WRKSTEPNAME = "Custodial Authorizer";

	//Control Sets
	public static final String DISABLE_ALL = "DisableAll";//Disable All the Fields.
	public static final String INITATION_ENABLE = "InitiationEnable";
	public static final String INPUTTER_ENABLE = "InputterEnable";
	public static final String DOCSPENDING_ENABLE = "DocsPendingEnable";
	public static final String AUTHORIZER_ENABLE = "AuthorizerEnable";

	//add History        

	public static final String ADD_HISTORY_Q_DECISIONHISTORY = "Q_DecisionHistory";


}
