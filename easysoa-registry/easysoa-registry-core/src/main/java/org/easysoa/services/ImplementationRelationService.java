/**
 * EasySOA Registry
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

package org.easysoa.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.relations.api.QNameResource;
import org.nuxeo.ecm.platform.relations.api.RelationManager;
import org.nuxeo.ecm.platform.relations.api.Resource;
import org.nuxeo.ecm.platform.relations.api.Statement;
import org.nuxeo.ecm.platform.relations.api.impl.ResourceImpl;
import org.nuxeo.ecm.platform.relations.api.impl.StatementImpl;
import org.nuxeo.ecm.platform.relations.api.util.RelationHelper;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Helpers for managing relations between documents.
 * Only considers the relations of type RelationService.DEFAULT_PREDICATE
 * 
 * @author mkalam-alami
 *
 */
public class ImplementationRelationService extends DefaultComponent {
    
    public static final String DEFAULT_PREDICATE = "Est rattaché à";
    public static final String DEFAULT_PREDICATE_INVERSE = "Est implémenté par";
    
    private static Log log = LogFactory.getLog(ImplementationRelationService.class);

    /**
    * Returns all document related to specified document.
    * @param session
    * @param doc
    * @return
    */
    public final DocumentModelList getRelations(CoreSession session,
            DocumentModel doc) {
        DocumentModelList relations = null;
        try {
            
            // Uses content automations
            AutomationService automationService = (AutomationService) Framework
                    .getService(AutomationService.class);
            OperationContext opCtxt = new OperationContext(session);
            opCtxt.setInput(doc);

            OperationChain chain = new OperationChain(null);
            chain.add("Context.FetchDocument");
            chain.add("Relations.GetRelations").set("predicate",
                    DEFAULT_PREDICATE);

            relations = (DocumentModelList) automationService
                    .run(opCtxt, chain);
        } catch (Exception e) {
            log.error("Failed to get document relations", e);
        }

        return relations;
    }

    /**
    * Creates a relation between two documents.
    * @param from
    * @param to
    */
    public final void createRelation(CoreSession session,
            DocumentModel from, DocumentModel to) {
        
        try {
            // Relation creation
            // Could also have used automations (see Relations.CreateRelation)
            RelationManager service = RelationHelper.getRelationManager();
            QNameResource fromResource = RelationHelper
                    .getDocumentResource(from);
            QNameResource toResource = RelationHelper.getDocumentResource(to);
            Resource predicate = new ResourceImpl(DEFAULT_PREDICATE);

            List<Statement> stmts = new ArrayList<Statement>();
            Statement stmt = new StatementImpl(fromResource, predicate,
                    toResource);
            stmts.add(stmt);
            if (!service.hasStatement("default", stmt)) {
                service.add("default", stmts);
                log.info("Relation creation : " + from.getTitle()
                        + " -> " + to.getTitle());
            } else {
                log.info("Relation " + from.getTitle() + " -> " + to.getTitle()
                        + " already exists.");
            }
            
        } catch (Exception e) {
            log.error("Relation creation failed", e);
        }
    }

    /**
    * Clears all relations from specified document.
    * @param doc
    */
    public final void clearRelations(CoreSession session, DocumentModel doc) {
        try {            
            Resource predicate = new ResourceImpl(DEFAULT_PREDICATE);
            List<Statement> stmts = RelationHelper.getStatements(doc, predicate);
            log.info("Deleting all " + stmts.size() + " relations of document " + doc.getTitle());
            RelationHelper.getRelationManager().remove("default", stmts);
            
        } catch (Exception e) {
            log.error("Relations reset failed", e);
        }
    }

}
