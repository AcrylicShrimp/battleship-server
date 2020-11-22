package server.packet;

public class Packet {
	public class ClientHello {
		public String name;
	}

	public class RequestEnterRoom {
		public int roomId;
	}

	public class RequestLeaveRoom {
	}

	public class ChatNormal {
		public String message;
	}

	public class ChatWhisper {
		public String message;
	}
}
