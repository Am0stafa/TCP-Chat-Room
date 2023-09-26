import java.io.*;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// we are going to have the server which is going to listen for incoming connections then its going to accept these connection requests and then open a new connection handler for each client

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
  private ServerSocket server;
  private boolean done = false;
  private ExecutorService pool;

  public Server() {
    connections = new ArrayList<>();
  }

	@Override
	public void run() {
		// code that is executed when we run or start the runnable class

	try {
	        server = new ServerSocket(6000);
	        pool = Executors.newCachedThreadPool();
	        while(!done){
	          // this server has the accept method which return a client socket
	          Socket client = server.accept();
	          // we don't want to deal with the client over here we want to open a new instance of the ConnectionHandler as we need to handle multiple client concurrently 
	          //all that handlers are going to run in a thread pool
	          ConnectionHandler handler = new ConnectionHandler(client);
	          connections.add(handler);
	          pool.execute(handler);
	        }
	        
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Server is shutting down! dut to an error");
        System.out.println("the error is "+ e.getMessage());
      }

		
		
	}
	
// In order to broad cast the message to all the different clients connected:
public void broadCast(String message) {
		for(ConnectionHandler cd: connections) {
      if(cd != null) {
        cd.sendMessage(message);
      }
    }
	}
	
  public void shutdown() {
    
    for(ConnectionHandler cd: connections) {
      if(cd != null) {
        cd.sendMessage("Server is shutting down!");
      }
    }
    done = true;
    pool.shutdown();	  
    if(!server.isClosed()) {
      try {
        server.close(); 
        for(ConnectionHandler cd: connections) {
          cd.shutdown();
        }
      
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
	
	class ConnectionHandler implements Runnable {
		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private String nickname;
		
		public ConnectionHandler(Socket client) {
			this.client = client;
		}
			
    private boolean isValidNickname(String nickname) {
        return nickname != null && nickname.length() >= 3 && nickname.length() <= 15;
    }
	    
		@Override
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				// To send a message to the client
//				out.println("Hello client");
				// to read a message from a client
//				in.readLine();
				out.println("Enter a nickname: ");
				nickname = in.readLine();
	            while (!isValidNickname(nickname)) {
	                out.println("Invalid nickname! Nickname should be between 3 and 15 characters.");
	                out.println("Enter a nickname: ");
	                nickname = in.readLine();
	            }
	            System.out.println(nickname +" Connected!");
	            broadCast(nickname +" Joined the chat!"); // broad cast this to all the other clients
	            // we want to always have a loop that asks for new messages from the client
              String message;
              while ((message = in.readLine()) != null) {
                  if(message.startsWith("/nick")) {
                    String[] messageSplit = message.split(" ",2);
                    if(messageSplit.length == 2){
                      broadCast(nickname + " changed their name to " + messageSplit[1]);
                      System.out.println(nickname + " renamed their name to " + messageSplit[1]);
                      nickname = messageSplit[1];
                      out.println("Your nickname has been changed to " + nickname);
                    } else {
                      out.println("No nickname provided!");
                    }
                  } else if(message.startsWith("/quit")) {
                    broadCast(nickname + " left the chat!");
                    shutdown();
                  } else {
                      broadCast(nickname + ": " + message);
                  }
              }
				
			} catch (IOException e) {
				e.printStackTrace();
		        shutdown();
		        System.out.println("Server is shutting down! dut to an error");
		        
			}
			
		}
		
		public void sendMessage(String message) {
			out.println(message);
		}

    public void shutdown(){
      try {
		in.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
      out.close();
      if(!client.isClosed()) {
        try {
          client.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
	
		
	}
  
  public static void main(String[] args) {
    Server server = new Server();
    server.run();
    // Thread serverThread = new Thread(server);
    // serverThread.start();
    // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    // String command;
    // try {
    //   while((command = in.readLine()) != null) {
    //     if(command.equals("/quit")) {
    //       server.shutdown();
    //       break;
    //     } else {
    //       System.out.println("Unknown command!");
    //     }
    //   }
    // } catch (IOException e) {
    //   e.printStackTrace();
    // }
  }

}
