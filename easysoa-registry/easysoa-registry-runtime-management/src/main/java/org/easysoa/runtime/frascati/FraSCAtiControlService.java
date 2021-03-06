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

package org.easysoa.runtime.frascati;

import org.easysoa.frascati.api.FraSCAtiServiceItf;
import org.easysoa.frascati.api.FraSCAtiServiceProviderItf;
import org.easysoa.runtime.api.RuntimeControlService;
import org.nuxeo.runtime.api.Framework;

/**
 * 
 * @author jguillemotte
 *
 */
public class FraSCAtiControlService implements RuntimeControlService {

	@Override
	public RuntimeState getState() {
	    // Check if the Frascati service is availble from the Nuxeo Framework
	    // Unable to return the states STARTING and STOPPING
	    RuntimeState state;
	    try {
	        FraSCAtiServiceItf frascatiService = (FraSCAtiServiceItf) Framework.getService(FraSCAtiServiceProviderItf.class).getFraSCAtiService();
	        if(frascatiService != null){
	            state = RuntimeState.STARTED;
	        } else {
	            state = RuntimeState.STOPPED;
	        }
	    }
	    catch(Exception ex){
	        // TODO : log an error or a warning
	        state = RuntimeState.STOPPED;
	    }
		return state;
	}

	@Override
	public boolean start() {
	    // TODO : How to start FraSCAti in Nuxeo by code ?
		return false;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}

}
