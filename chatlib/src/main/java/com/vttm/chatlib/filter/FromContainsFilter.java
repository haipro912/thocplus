/**
 * $RCSfile$
 * $Revision: 7071 $
 * $Date: 2007-02-12 01:59:05 +0100 (lun. 12 févr. 2007) $
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vttm.chatlib.filter;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Filters for packets where the "from" field contains a specified value.
 *
 * @author Matt Tucker
 */
public class FromContainsFilter implements StanzaFilter {

    private String from;

    /**
     * Creates a "from" contains filter using the "from" field part.
     *
     * @param from the from field value the packet must contain.
     */
    public FromContainsFilter(String from) {
        if (from == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.from = from.toLowerCase();
    }

    @Override
    public boolean accept(Stanza stanza) {
        if (stanza.getFrom() == null) {
            return false;
        }
        else {
            return stanza.getFrom().toString().toLowerCase().indexOf(from) != -1;
        }
    }
}