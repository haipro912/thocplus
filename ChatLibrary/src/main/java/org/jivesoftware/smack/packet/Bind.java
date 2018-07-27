/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
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

package org.jivesoftware.smack.packet;

/**
 * IQ packet used by Smack to bind a resource and to obtain the jid assigned by the server.
 * There are two ways to bind a resource. One is simply sending an empty Bind packet where the
 * server will assign a new resource for this connection. The other option is to set a desired
 * resource but the server may return a modified version of the sent resource.<p>
 * <p/>
 * For more information refer to the following
 * <a href=http://www.xmpp.org/specs/rfc3920.html#bind>link</a>.
 *
 * @author Gaston Dombiak
 */
public class Bind extends IQ {

    private String resource = null;
    private String jid = null;
    private String userName = null;
    private String status = null;
    private String lastChangeAvatar = null;

    public Bind() {
        setType(IQ.Type.SET);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastChangeAvatar() {
        return lastChangeAvatar;
    }

    public void setLastChangeAvatar(String lastChangeAvatar) {
        this.lastChangeAvatar = lastChangeAvatar;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">");
        if (resource != null) {
            buf.append("<resource>").append(resource).append("</resource>");
        }
        if (jid != null) {
            buf.append("<jid>").append(jid).append("</jid>");
        }
        buf.append("</bind>");
        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (resource != null) {
            buf.append("<resource>").append(resource).append("</resource>");
        }
        if (jid != null) {
            buf.append("<jid>").append(jid).append("</jid>");
        }
        if (userName != null) {
            buf.append("<userName>").append(userName).append("</userName>");
        }
        if (status != null) {
            buf.append("<status>").append(status).append("</status>");
        }
        if (lastChangeAvatar != null) {
            buf.append("<lastChangeAvatar>").append(lastChangeAvatar).append("</lastChangeAvatar>");
        }
        buf.append("</bind>");
        return buf.toString();
    }
}
