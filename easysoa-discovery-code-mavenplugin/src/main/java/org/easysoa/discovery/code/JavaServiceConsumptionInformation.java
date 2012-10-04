package org.easysoa.discovery.code;

import java.util.List;

import org.easysoa.registry.SoaNodeId;
import org.easysoa.registry.rest.marshalling.SoaNodeInformation;
import org.easysoa.registry.types.java.JavaServiceConsumption;

// TODO Put in a rest-client-java project
public class JavaServiceConsumptionInformation extends SoaNodeInformation implements JavaServiceConsumption {

    public JavaServiceConsumptionInformation(SoaNodeId fromDeliverable, String toInterface, String interfaceLocation) {
        super(new SoaNodeId(JavaServiceConsumption.DOCTYPE, fromDeliverable.getName() + ">" + toInterface), null, null);
        this.properties.put(XPATH_CONSUMEDINTERFACE, toInterface);
        this.properties.put(XPATH_CONSUMEDINTERFACELOCATION, interfaceLocation);
        this.parentDocuments.add(fromDeliverable);
    }
    
    @Override
    public String getConsumedInterface() throws Exception {
        return (String) properties.get(JavaServiceConsumption.XPATH_CONSUMEDINTERFACE);
    }

    @Override
    public List<SoaNodeId> getConsumableServiceImpls() throws Exception {
        throw new UnsupportedOperationException();
    }

}
