package com.openwide.easysoa.monitoring.apidetector;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import com.openwide.easysoa.esperpoc.EsperEngineSingleton;
import com.openwide.easysoa.esperpoc.NuxeoRegistrationService;
import com.openwide.easysoa.monitoring.Message;
import com.openwide.easysoa.monitoring.Message.MessageType;
import com.openwide.easysoa.monitoring.MonitorService.MonitoringMode;
import com.openwide.easysoa.monitoring.MonitorService;
import com.openwide.easysoa.monitoring.MonitoringModel;
import com.openwide.easysoa.monitoring.soa.Api;
import com.openwide.easysoa.monitoring.soa.Appli;

/**
 * Unit test for simple App.
 */
public class ApiDetectorTest extends TestCase {
	
	public final static String SOA_MODEL_TYPE_APPLIIMPL = "appliimpl";
	public final static String SOA_MODEL_TYPE_API = "api";
	public final static String SOA_MODEL_TYPE_SERVICE = "service";
	//public final static String SOA_MODEL_TYPE_RESOURCE = "resource";
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(getInvokingClassName());
	
	//private MonitoringModel monitoringModel; // scope : global
	//private UrlTree urlTree; // scope : run
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ApiDetectorTest(String testName){
        super(testName);
    }
    
    /**
     * 
     * @return
     */
    public static String getInvokingClassName() {
    	return Thread.currentThread().getStackTrace()[1].getClassName();
    }

    /**
     * 
     */
	public void testUrlDetection(){
		MonitorService.getMonitorService(MonitoringMode.DISCOVERY);
		urlDetectionSimulate(new UrlMock().getTwitterUrlData());
		//urlDetectionSimulate(new UrlMock().getIMediaUrlData());
		urlDetectionCompute();
		urlDetectionDebugResults();
		// post run (detection mode)
		//TODO Warning, do not call this method in VALIDATED MODE Otherwise crash !!!!
		//MonitorService.getMonitorService().registerDetectedServicesToNuxeo();

		// load nuxeo model and display it
		//MonitoringModel testSoaModel = new MonitoringModel();
		//testSoaModel.fetchFromNuxeo();
		//logger.debug("allNodes:\n" + testSoaModel.getSoaNodes());
		
		//urlTree = null;
	}

	/**
	 * 
	 */
	public void testUrlValidated(){
		// NB. in validation mode, no concept or pre or post run
		// TODO LATER cache it
		MonitorService.getMonitorService(MonitoringMode.VALIDATED);
		urlDetectionSimulate(new UrlMock().getTwitterUrlData());
		// TODO LATER mixed mode : do compute and debugResults BUT ONLY on unknown messages ?!
		//urlDetectionCompute();
		//urlDetectionDebugResults();
		// load nuxeo model and display it
		//MonitoringModel testSoaModel = new MonitoringModel();
		//testSoaModel.fetchFromNuxeo();
		//logger.debug("allNodes:\n" + testSoaModel.getSoaNodes());
	}
	
	/**
	 * @param arrayList 
	 * 
	 */
	//TODO Stay here ????
	public void urlDetectionSimulate(ArrayList<String> arrayList){
		// simulate rest exchanges
		logger.debug( "Test URL detection start");
		Iterator<String> iter = arrayList.iterator();;
		String urlString;
		//for(int i=0; i<1000;i++){
		for(int i=0; i<1;i++){
			while(iter.hasNext()){
				urlString = iter.next();
				logger.debug("**** New URL :" + urlString);
				try{
					URL url = new URL(urlString);
					Message msg = new Message(url, MessageType.REST);
					MonitorService.getMonitorService().listen(msg);
				}
				catch(Exception ex){
					logger.error("**** problem spotted ! ", ex);
				}
			}
		}
		logger.debug("Test URL detection stop");
	}
		
	//TODO Stay here ?
	public void urlDetectionCompute(){
		// compute additional, non-local indicators :
		// TODO compute them ; for now, only computed on demand at the end
		MonitorService.getMonitorService().registerDetectedServicesToNuxeo();
	}

	/**
	 *
	 */
	// TODO Stay here ? Remove this method ?
	public void urlDetectionDebugResults(){
		// debug / print them only in discovery mode :
		UrlTree urlTree = MonitorService.getMonitorService().getUrlTree();
		if(urlTree != null){
			logger.debug("Printing tree node index ***");
			logger.debug("Total url count : " + urlTree.getTotalUrlCount());
			String key;
			HashMap<String, UrlTreeNode> index = urlTree.getNodeIndex();
			Iterator<String> iter2 = index.keySet().iterator();
			UrlTreeNode node;
			UrlTreeNode parentNode;
			while(iter2.hasNext()){
				key = iter2.next();
				node = index.get(key);
				if(node.getLevel() <= 3){
					parentNode = (UrlTreeNode)(node.getParent());
					logger.debug("complete = " + node.getCompleteUrlcallCount() + ",partial = " + node.getPartialUrlcallCount() + ",total = " + urlTree.getTotalUrlCount());
					logger.debug(key + " -- " + node.toString() + ", parent node => " + parentNode.getNodeName() + ", Depth => " + node.getDepth() + ", Level => " + node.getLevel()); 
					logger.debug(", direct node childs => " + node.getChildCount() + "Total childs number => " + node.getTotalChildsNumber());
					logger.debug(", ratioPartial => " + node.getRatioPartial(urlTree) + "%, ratioComplete => " + node.getRatioComplete(urlTree) + "%" + ", Ratio childs => " + node.getRatioChilds() + "%" + ", Ratio childs to ancestor => " + node.getRatioChildsToAncestor() + "%");
				}
			}
		}
	}
	
}