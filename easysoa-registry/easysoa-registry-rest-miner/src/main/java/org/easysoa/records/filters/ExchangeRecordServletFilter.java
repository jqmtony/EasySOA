/**
 * EasySOA Registry Rest Miner
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
package org.easysoa.records.filters;

import org.easysoa.proxy.core.api.exchangehandler.HttpExchangeHandler;
import org.easysoa.proxy.core.api.exchangehandler.MessageHandler;

/**
 *
 * @author mkalam-alami
 *
 */
public interface ExchangeRecordServletFilter {

    /**
     * Start the filter
     * @param httpExchangeHandler The http exchange handler to use for exchange recording
     * @throws Exception If a problem occurs
     */
	//void start(HttpExchangeHandler httpExchangeHandler) throws Exception;
    void start(MessageHandler httpExchangeHandler) throws Exception;

	/**
	 * Stop the filter
	 */
	void stop();

}
