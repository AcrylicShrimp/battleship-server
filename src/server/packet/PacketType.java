package server.packet;

public class PacketType {
    public static final int SERVER_HELLO = 00001;

    public static final int NOTIFY_LOBBY_ENTER_LOBBY = 10001;
    public static final int NOTIFY_LOBBY_LEAVE_LOBBY = 10002;
    public static final int NOTIFY_LOBBY_ROOM_CREATED = 10003;
    public static final int NOTIFY_LOBBY_ROOM_RENAMED = 10004;
    public static final int NOTIFY_LOBBY_ROOM_REMOVED = 10005;
    public static final int NOTIFY_ROOM_ENTER_ROOM = 10101;
    public static final int NOTIFY_ROOM_LEAVE_ROOM = 10102;
    public static final int NOTIFY_ROOM_ROOM_RENAMED = 10103;
    public static final int NOTIFY_LOBBY = 10201;
    public static final int NOTIFY_ROOM = 10202;

    public static final int CLIENT_HELLO = 20001;
    public static final int REQUEST_CREATE_ROOM = 20101;
    public static final int REQUEST_ENTER_ROOM = 20102;
    public static final int REQUEST_LEAVE_ROOM = 20103;
    public static final int REQUEST_RENAME_ROOM = 20104;
    public static final int REQUEST_START_GAME = 20105;

    public static final int BROADCAST_CHAT_NORMAL = 30001;
    public static final int BROADCAST_CHAT_WHISPER = 30002;

    public static final int CHAT_NORMAL = 40001;
    public static final int CHAT_WHISPER = 40002;

    public static final int REJECT_ENTER_ROOM_NOTFOUND = 70001;
    public static final int REJECT_ENTER_ROOM_NOTINROOM = 70002;

    public static final int NOTIFY_GAME_INIT = 80001;
    public static final int NOTIFY_GAME_BEGIN = 80002;
    public static final int NOTIFY_GAME_TURN = 80003;
    public static final int NOTIFY_GAME_FIRE_FRIENDLY = 80004;
    public static final int NOTIFY_GAME_FIRE_ENEMY = 80005;
    public static final int NOTIFY_GAME_FIRE_REJECTED = 80006;
    public static final int NOTIFY_GAME_SET = 80007;

    public static final int GAME_INIT = 90001;
    public static final int GAME_FIRE = 90002;
}
