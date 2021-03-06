/**
 * EasySOA Samples - Smart Travel
 * Copyright 2011 Open Wide
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact : easysoa-dev@googlegroups.com
 */


/*
 * 
 */

package fr.inria.galaxy.j1.scenario1;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.1
 * Thu May 21 20:15:22 CEST 2009
 * Generated source version: 2.2.1
 * 
 */


@WebServiceClient(name = "ISummaryService", 
                  wsdlLocation = "wsdl/CreateSummary.wsdl",
                  targetNamespace = "http://scenario1.j1.galaxy.inria.fr/") 
public class ISummaryServiceFactory extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://scenario1.j1.galaxy.inria.fr/", "ISummaryService");
    public final static QName ISummaryServicePort = new QName("http://scenario1.j1.galaxy.inria.fr/", "ISummaryServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Users/veleno/workspace/galaxy-svn/demoj1-scenario1/trunk/summary-model/src/main/resources/wsdl/CreateSummary.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/Users/veleno/workspace/galaxy-svn/demoj1-scenario1/trunk/summary-model/src/main/resources/wsdl/CreateSummary.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public ISummaryServiceFactory(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ISummaryServiceFactory(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ISummaryServiceFactory() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns ISummaryServicePortType
     */
    @WebEndpoint(name = "ISummaryServicePort")
    public ISummaryServicePortType getISummaryServicePort() {
        return super.getPort(ISummaryServicePort, ISummaryServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISummaryServicePortType
     */
    @WebEndpoint(name = "ISummaryServicePort")
    public ISummaryServicePortType getISummaryServicePort(WebServiceFeature... features) {
        return super.getPort(ISummaryServicePort, ISummaryServicePortType.class, features);
    }

}
