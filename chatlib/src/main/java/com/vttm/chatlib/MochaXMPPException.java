package com.vttm.chatlib;

import org.jivesoftware.smack.XMPPException;

public class MochaXMPPException extends XMPPException {


    public MochaXMPPException(String message) {
        super(message);
    }
}
