# Protocol

This document describes about protocols the `Battleship` will use.

## Packet structure

## Packets

Table for all packets are below.

### Server to client

| ID | Name | Description | Data |
| --- | --- | --- | --- |
| 00001 | server-hello | First packet of the server to notify its id after successful client-hello | clientId: int, clientName: string |
| 10001 | notify-lobby-enter-lobby | Notifies the clients in the lobby that a new client has entered the lobby | clientId: int, clientName: string |
| 10002 | notify-lobby-leave-lobby | Notifies the clients in the lobby that a client has left the lobby | clientId: int |
| 10003 | notify-lobby-room-created | Notifies the clients in the lobby that a new room has been created. | roomId: int, roomName: string |
| 10004 | notify-lobby-room-renamed | Notifies the clients in the lobby that a room name has been changed. | roomId: int, roomName: string |
| 10005 | notify-lobby-room-removed | Notifies the clients in the lobby that a room has been removed. | roomId: int |
| 10101 | notify-room-enter-room | Notifies the clients in the room that a new client has entered the room | clientId: int, clientName: string |
| 10102 | notify-room-leave-room | Notifies the clients in the room that a client has left the room | clientId: int |
| 10103 | notify-room-room-renamed | Notifies the clients in the room that the room name has been changed. | roomName: string |
| 10201 | notify-lobby | Notifies a client that it is now in the lobby | clientNum: int, clients: {clientId: int, clientName: string}[], roomNum: int, rooms: {roomId: int, roomName: string}[] |
| 10202 | notify-room | Notifies a client that it is now in the room | roomId: int, roomName: string, clientNum: int, {clientId: int, clientName: string}[] |
| 80001 | notify-game-init | Notifies the clients in to room that a new game has been initialized | isBlue: int |
| 80002 | notify-game-begin | Notifies the clients in the game that the game has been began | N/A |
| 80003 | notify-game-turn | Notifies the clients in the game that the turn has been changed | isMyTurn: int |
| 80004 | notify-game-fire-friendly | Notifies the client in the game that the friendly fired | x: int, y: int, hit: int |
| 80005 | notify-game-fire-enemy | Notifies the client in the game that the enemy fired | x: int, y: int, hit: int |
| 80006 | notify-game-fire-rejected | Notifies the client in the game that the fire has been rejected | N/A |
| 80007 | notify-game-set | Notifies the clients in the game that the game has been set | won: int |
| 30001 | broadcast-chat-normal | Broadcasts a chat | clientId: int, clientName: string, message: string |
| 30002 | broadcast-chat-whisper | Broadcasts a whisper chat | clientId: int, clientName: string, message: string |
| 70001 | reject-enter-room-notfound | Notifies a client that the requested room has been removed | N/A |
| 70002 | reject-leave-room-notinroom | Notifies a client that the request failed because the client is not in a room | N/A |

### Client to server

| ID | Name | Description | Data |
| --- | --- | --- | --- |
| 20001 | client-hello | First packet of clients to set its name | name: string |
| 20101 | request-create-room | Requests to create a new room | roomName: string |
| 20102 | request-enter-room | Requests to enter the room | roomId: int |
| 20103 | request-leave-room | Requests to leave the room | N/A |
| 20104 | request-rename-room | Requests to rename the room | roomName: string |
| 20105 | request-start-game | Requests to start a new game | N/A |
| 90001 | game-init | Send the game board | {x: int, y: int, rotate: int}[4] |
| 90002 | game-fire | Fire to the specified location | x: int, y: int |
| 40001 | chat-normal | Sends a chat | message: string |
| 40002 | chat-whisper | Sends a whisper chat to the specific client | clientId: int, message: string |
