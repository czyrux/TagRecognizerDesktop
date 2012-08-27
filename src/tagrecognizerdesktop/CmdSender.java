/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tagrecognizerdesktop;

import de.unidue.tagrecognition.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * @brief Implements runnable client.
 * @author Antonio Manuel Gutierrez Martinez
 * @version 1.0
 */
public class CmdSender implements Runnable {
    /** Server IP */
    private final String HOST = "192.168.137.5";
    /** Server Port */
    private final int PORT = 8000;
    /** Client socket */
    private Socket _socket = null;
    /** Output stream */
    private ObjectOutputStream _out = null;
    /** Input stream */
    private ObjectInputStream _in = null;
    /** Message to send */
    private Message _cmd;
    private TagRecognitionView _ui;
    
    /**
     * Constructor
     * @param ui
     * @param cmd 
     */
    public CmdSender ( TagRecognitionView ui , Message cmd ) {
        _cmd = cmd;
        _ui = ui;
    }
    
    /**
     * Client work
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void run() {	
        Object o = null;	
        try {		
            // Create socket	
            SocketAddress sockaddr = new InetSocketAddress(HOST, PORT);		
            _socket = new Socket();	
            _socket.connect(sockaddr, 2000); // 2 second connection timeout
		
            if (_socket.isConnected()) {		
                System.out.println("SenderCmd: Client connected");		
                
                // Open streams		
                _out = new ObjectOutputStream(_socket.getOutputStream());		     
                _in = new ObjectInputStream(_socket.getInputStream());		         
                System.out.println("SenderCmd: stream created");
		
               
                // Send CMD					    
                System.out.println("SenderCmd: Sending tag: " + _cmd.toString());             
                _out.writeObject(_cmd.toString());       
                _out.flush();
     
                // Wait confirmation            
                try {                
                    System.out.println("SenderCmd: Waiting ACK");               
                    o = _in.readObject();                       
                    if (o.equals(Message.ACK.toString())) {	                    
                        System.out.println("SenderCmd: ACK received");               
                    }            
                } catch (ClassNotFoundException e) {        
                    System.out.println("SenderCmd: ClassNotFoundException");          
                    //e.printStackTrace();      
                }    
            }

        } catch (UnknownHostException e) {
            _ui.updateUI(Message.ERROR_CONNECTING);
            System.out.println("SenderCmd: UnknownHostException");	
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SenderCmd: IOException");
            _ui.updateUI(Message.ERROR_CONNECTING);
            e.printStackTrace();
        } finally {
            //Close sockets and streams
            try {
                if (_in != null)	
                    _in.close();
                if (_out != null)
                    _out.close();
                if (_socket != null)
                    _socket.close();
            } catch (IOException e) {
                System.out.println("SenderCmd: IOException");
                //e.printStackTrace();
            } catch (Exception e) {
                System.out.println("SenderCmd: Exception");
                //e.printStackTrace();
            }
            System.out.println("SenderCmd: Finished");
        }
    }
}
