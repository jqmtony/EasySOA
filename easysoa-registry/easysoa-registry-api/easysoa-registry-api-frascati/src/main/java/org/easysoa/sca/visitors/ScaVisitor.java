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

package org.easysoa.sca.visitors;

import org.easysoa.sca.BindingInfoProvider;

public interface ScaVisitor {

    /**
     * 
     * @param bindingInfoProvider 
     * @throws Exception when local, not fatal error
     */
    void visit(BindingInfoProvider bindingInfoProvider) throws Exception;

    /**
     * To resolve linking when visit is finished
     * @throws Exception when local, not fatal error
     */
    void postCheck() throws Exception;

    /**
     * Describes the scaVisitor instance for debugging purpose
     * @return
     */
    String getDescription();

    /**
     * Set the document manager
     */
    public void setDocumentManager(Object documentManager);
    
}
