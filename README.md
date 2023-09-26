# TCP Chat Room

A simple Java-based TCP chat room application. This project establishes a basic chatroom where one central server manages connected clients and relays messages between them. Clients can connect, choose a nickname, send messages to all other connected clients, change their nicknames, and leave the chat. The server utilizes a thread pool to handle multiple clients concurrently.

## Features

- Central server and multiple client architecture.
- Nickname selection for clients.
- Broadcast messages to all connected clients.
- Ability to change nicknames with the `/nick` command.
- Leave the chat using the `/quit` command.
- Thread pool in the server for scalability.
- Client utilizes two threads: 
  - One for receiving all the messages from the server.
  - One for accepting user console input.

## Prerequisites

Ensure you have the following installed on your machine:

- Java JDK 8 or higher

## How to Compile and Run

1. Clone this repository:

```bash
git clone https://github.com/your-username/tcp-chat-room.git
cd tcp-chat-room
```

Replace `your-username` with your GitHub username.

2. Compile the source files:

```bash
javac Server.java Client.java
```

3. To run the server:

```bash
java Server
```

4. To run a client:

```bash
java Client
```

## Usage

1. When running the `Client`, you'll be prompted to choose a nickname. Nicknames should be between 3 and 15 characters.

2. Send messages simply by typing them into the client console and pressing `Enter`.

3. To change your nickname:

```bash
/nick NewNickname
```

Replace `NewNickname` with your desired nickname.

4. To leave the chat:

```bash
/quit
```

## Global Usage

To run the project globally, you can package the application into a `.jar` file and then execute it anywhere Java is installed.

1. To create a `.jar` file:

```bash
# Assuming you are in the tcp-chat-room directory
jar cfe Server.jar Server *.class
jar cfe Client.jar Client *.class
```

2. Transfer these `.jar` files to any location.

3. To run the server globally:

```bash
java -jar Server.jar
```

4. To run a client globally:

```bash
java -jar Client.jar
```

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
