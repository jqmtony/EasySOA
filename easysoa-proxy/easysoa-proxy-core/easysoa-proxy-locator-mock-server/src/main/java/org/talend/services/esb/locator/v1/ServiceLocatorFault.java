
package org.talend.services.esb.locator.v1;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.4.1
 * 2012-10-18T16:20:08.027+02:00
 * Generated source version: 2.4.1
 */

@WebFault(name = "ServiceLocatorFaultDetail", targetNamespace = "http://talend.org/schemas/esb/locator/2011/11")
public class ServiceLocatorFault extends Exception {
    
    private org.talend.schemas.esb.locator._2011._11.ServiceLocatorFaultDetail serviceLocatorFaultDetail;

    public ServiceLocatorFault() {
        super();
    }
    
    public ServiceLocatorFault(String message) {
        super(message);
    }
    
    public ServiceLocatorFault(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLocatorFault(String message, org.talend.schemas.esb.locator._2011._11.ServiceLocatorFaultDetail serviceLocatorFaultDetail) {
        super(message);
        this.serviceLocatorFaultDetail = serviceLocatorFaultDetail;
    }

    public ServiceLocatorFault(String message, org.talend.schemas.esb.locator._2011._11.ServiceLocatorFaultDetail serviceLocatorFaultDetail, Throwable cause) {
        super(message, cause);
        this.serviceLocatorFaultDetail = serviceLocatorFaultDetail;
    }

    public org.talend.schemas.esb.locator._2011._11.ServiceLocatorFaultDetail getFaultInfo() {
        return this.serviceLocatorFaultDetail;
    }
}
