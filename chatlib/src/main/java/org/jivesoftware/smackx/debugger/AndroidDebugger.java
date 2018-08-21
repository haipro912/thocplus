package org.jivesoftware.smackx.debugger;


import com.vttm.chatlib.utils.Log;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.debugger.AbstractDebugger;

/**
 * Very simple debugger that prints to the android log the sent and received stanzas. Use
 * this debugger with caution since printing to the console is an expensive operation that may
 * even block the thread since only one thread may print at a time.<p>
 * <p/>
 * It is possible to not only print the raw sent and received stanzas but also the interpreted
 * packets by Smack. By default interpreted packets won't be printed. To enable this feature
 * just change the <tt>printInterpreted</tt> static variable to <tt>true</tt>.
 *
 * @author Gaston Dombiak
 */
public class AndroidDebugger extends AbstractDebugger {

    public AndroidDebugger(XMPPConnection connection) {
        super(connection);
    }

    @Override
    protected void log(String logMessage) {
        Log.d("SMACK", logMessage);
    }

    @Override
    protected void log(String logMessage, Throwable throwable) {
        Log.d("SMACK", logMessage, throwable);
    }
}
