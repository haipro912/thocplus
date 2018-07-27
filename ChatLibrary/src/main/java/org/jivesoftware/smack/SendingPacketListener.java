package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Packet;

public interface SendingPacketListener {
	void fireSendingPacket(Packet packet);

}
