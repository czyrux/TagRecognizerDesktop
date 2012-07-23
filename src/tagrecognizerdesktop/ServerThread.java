/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tagrecognizerdesktop;

/**
 *
 * @author czyrux
 */
import java.io.IOException;
import java.net.ServerSocket;


public class ServerThread implements Runnable {
    private final int PORT = 8080;
    private ServerSocket _serverSocket ;
    private TagRecognitionView _ui;

    public ServerThread( TagRecognitionView ui) {
        _serverSocket = null;
        _ui = ui;
    }

    public void run() {
        DataReceiver w;
        try {
            _serverSocket = new ServerSocket(PORT);
            System.out.println("ServerThread: Server created. Listening :" + PORT);
        } catch (IOException e) {
            System.out.println("ServerThread: Unable to create server");
            return;   
        }

        boolean end = false;
        while(!end){
            try {
                System.out.println("ServerThread: Waiting a client...");
                //server.accept returns a client connection
                w = new DataReceiver(_serverSocket.accept(), _ui );
                Thread t = new Thread(w);
                t.start();
            } catch (IOException e) {
                //e.printStackTrace();
                end = true;
            }
        }
    }
    
    public void closeServer() {
        if (_serverSocket!=null) {
            try{
                _serverSocket.close();
                System.out.println("ServerThread: Server closed.");
            } catch (IOException e) {
                System.out.println("ServerThread: Could not close socket");
            }
        }
    }
}
