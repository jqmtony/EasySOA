package org.easysoa.registry;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

/**
 * Computes...
 * 
 * still TODO (see FIXMEs) :
 * if needed soaMetamodel performances
 * special cases (see FIXMEs)
 * 
 * @author mkalam-alami
 *
 */
public class DiscoveryServiceImpl implements DiscoveryService {

    public DocumentModel runDiscovery(CoreSession documentManager, SoaNodeId identifier,
            Map<String, Object> properties, List<SoaNodeId> correlatedDocuments) throws Exception {
        DocumentService documentService = Framework.getService(DocumentService.class);
        
        // Fetch or create document
        DocumentModel documentModel = documentService.find(documentManager, identifier);
        if (documentModel == null) {
            documentModel = documentService.create(documentManager, identifier, identifier.getName());
        }
        
        // Set properties
        if (properties != null) {
            for (Entry<String, Object> property : properties.entrySet()) {
                // FIXME Non-serializable error handling
                documentModel.setPropertyValue(property.getKey(), (Serializable) property.getValue());
            }
            documentManager.saveDocument(documentModel);
        }
        
        // Link to correlated documents
        //FIXME cache / build model of soaMetamodelService responses to speed it up, handle swapped & unswapped in same call
        if (correlatedDocuments != null && !correlatedDocuments.isEmpty()) {
            String type = documentModel.getType();
            SoaMetamodelService soaMetamodelService = Framework.getService(SoaMetamodelService.class);
            for (SoaNodeId correlatedDocument : correlatedDocuments) {
                List<String> path = soaMetamodelService.getPath(type, correlatedDocument.getType());
                
                // Swap the documents if necessary
                DocumentModel parentDocument;
                if (path == null) {
                    // Create the target
                    parentDocument = documentService.find(documentManager, correlatedDocument);
                    if (parentDocument == null) {
                        parentDocument = documentService.create(documentManager, correlatedDocument, correlatedDocument.getType());
                    }
                    
                    // Find a path (either between doc & target, or between target & another correlated doc)
                    // FIXME Does not cover all special cases (ex. if no swapping, if a placeholder is found to be another existing element and therefore must be removed)
                    path = soaMetamodelService.getPath(correlatedDocument.getType(), type);
                    if (path == null) {
                        for (SoaNodeId candidateCorrelatedDocument : correlatedDocuments) {
                            if (!correlatedDocument.getType().equals(candidateCorrelatedDocument.getType())) {
                                path = soaMetamodelService.getPath(correlatedDocument.getType(), candidateCorrelatedDocument.getType());
                                if (path != null) {
                                    correlatedDocument = candidateCorrelatedDocument;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        correlatedDocument = new SoaNodeId(documentModel);
                    }
                }
                else {
                    parentDocument = documentModel;
                }
                
                // If we have unknown documents between the two, create placeholders
                // TODO More intelligent, pluggable correlation logic?
                if (path != null) {
                    for (String pathStepType : path.subList(0, path.size() - 1)) {
                        // Before creating a placeholder, check if the intermediate type
                        // is not already listed in the correlated documents
                        boolean placeholderNeeded = true;
                        for (SoaNodeId placeholderReplacementCandidate : correlatedDocuments) {
                            if (pathStepType.equals(placeholderReplacementCandidate.getType())) {
                                parentDocument = documentService.create(documentManager, placeholderReplacementCandidate,
                                        parentDocument.getPathAsString(), null);
                                placeholderNeeded = false;
                                break;
                            }
                        }
                        
                        if (placeholderNeeded) {
                            parentDocument = documentService.create(documentManager,
                                    new SoaNodeId(pathStepType, IdUtils.generateStringId()),
                                    parentDocument.getPathAsString(), "(Placeholder)");
                        }
                    }
                }
                
                // Create target document if necessary
                if (documentService.findProxy(documentManager, correlatedDocument,
                        parentDocument.getPathAsString()) == null) {
                    documentService.create(documentManager, correlatedDocument,
                            parentDocument.getPathAsString(), correlatedDocument.getName());
                }
            } 
        }
        
        return documentModel;
    }

}
