/**
 * EasySOA Proxy
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
package org.easysoa.proxy.core.api.exchangehandler;

import org.apache.log4j.Logger;
import org.easysoa.message.InMessage;
import org.easysoa.message.OutMessage;
import org.easysoa.proxy.core.api.configuration.ProxyConfiguration;
import org.easysoa.proxy.core.api.run.RunManager;
import org.easysoa.records.ExchangeRecord;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;


/**
 * Handler to record messages using the run manager
 * @author jguillemotte
 *
 */
@Scope("composite")
public class MessageRecordHandler extends MessageHandlerBase {

    public final static String HANDLER_ID = "messageRecordHandler";

    /**
     * Logger
     */
    private Logger logger = Logger.getLogger(MessageRecordHandler.class.getName());

    private boolean enabled = true;

    @Reference
    protected RunManager runManager;

    /**
     * @param runManager
     */
    public MessageRecordHandler(){
    }

    /**
     * @param runManager
     */
    // TODO : Find an other way to init runManager (from nuxeo). When done, remove this setter
    public void setRunManager(RunManager runManager){
        this.runManager = runManager;
    }

    @Override
    public void handleMessage(InMessage inMessage, OutMessage outMessage) throws Exception {
        if(enabled){
            logger.debug("Message received, calling registered handlers");
            // Builds a new Exchange record with data contained in request and response
            ExchangeRecord record = new ExchangeRecord();
            record.setInMessage(inMessage);
            record.setOutMessage(outMessage);
            // Call runManager to register the exchange record
            runManager.record(record);
        } else {
            logger.info("Message record handler is disabled");
        }
    }

    @Override
    public void setHandlerConfiguration(ProxyConfiguration configuration) {
        setConfiguration(configuration);
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public String getID() {
        return HANDLER_ID;
    }

}
