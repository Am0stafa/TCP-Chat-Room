import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Its going to have two threads one for receive all the messages from the server and the other one is going to receive our console line input
public class Client implements Runnable{
  private Socket client;
  private BufferedReader in;
  private PrintWriter out;
  private boolean done = false;

	@Override
	public void run() {
		try {
		client = new Socket("localhost", 6000);
      		out = new PrintWriter(client.getOutputStream(), true);
      		in = new BufferedReader(new InputStreamReader(client.getInputStream()));

      InputHandler inHandler = new InputHandler();
      Thread t = new Thread(inHandler);
      t.start();

      String inMessage;
      while((inMessage = in.readLine()) != null) {
        System.out.println(inMessage);
      }

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

  public void shutdown() {
    done = true;
    try {
      in.close();
      out.close();
      if(!client.isClosed()) {
        client.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  // all we are going to do in this class is constantly ask for new console line input
  class InputHandler implements Runnable{
      @Override
      public void run() {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
        while(!done) {
          try {
            String message = inReader.readLine();
            if(message.equals("quit")) {
              inReader.close();
              shutdown();
            } else {
              out.println(message);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

      }
    
  }
  
    public static void main(String[] args) {
      Client client = new Client();
      client.run();
    }

}
