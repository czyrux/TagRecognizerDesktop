package tagrecognizerdesktop;

import de.unidue.tagrecognition.Message;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * @brief Implements runnable server thread. 
 * @author Antonio Manuel Gutierrez Martinez
 * @version 1.0
 */
public class ServerThread implements Runnable {
    /** Server port */
    private final int PORT = 8080;
    /** Server socket */
    private ServerSocket _serverSocket ;
    private TagRecognitionView _ui;

    /**
     * Constructor
     * @param ui 
     */
    public ServerThread( TagRecognitionView ui) {
        _serverSocket = null;
        _ui = ui;
    }

    /**
     * Thread process
     */
    public void run() {
        DataReceiver w;
        try {
            _serverSocket = new ServerSocket(PORT);
            _serverSocket.setReuseAddress(true);
            System.out.println("ServerThread: Server created. Listening :" + PORT);
        } catch (IOException e) {
            System.out.println("ServerThread: Unable to create server");
            _ui.updateUI(Message.ERROR_CREATING_SERVER);
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
    
    /**
     * Get Port of server
     * @return 
     */
    public int getPort() {
        return PORT;
    }
    
    /**
     * Close server
     */
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
