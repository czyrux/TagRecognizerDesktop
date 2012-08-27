package tagrecognizerdesktop;

import de.unidue.tagrecognition.Message;
import de.unidue.tagrecognition.Tag;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @brief Manage client connection
 * @author Antonio Manuel Gutierrez Martinez
 * @version 1.0
 */
public class DataReceiver implements Runnable {
    /** Communication socket */
    private Socket _client;
    /** Input stream */
    private ObjectInputStream _in;
    /** Output stream */
    private ObjectOutputStream _out;
    private TagRecognitionView _ui;
    
    /**
     * Constructor
     * @param client
     * @param ui 
     */
    public DataReceiver ( Socket client , TagRecognitionView ui ) {
        _client = client;
        _in = null;
        _out = null;
        _ui = ui;
    }
    
    public void run() {
        System.out.println("DataReceiver: Client ip: " + _client.getInetAddress());
        try{  
            _in = new ObjectInputStream(_client.getInputStream());  
            _out = new ObjectOutputStream(_client.getOutputStream());
            System.out.println("DataReceiver: streams created");
        } catch (IOException e) {     
            System.out.println("DataReceiver: in or out failed");
            return;
        }
            
        ArrayList<Tag> tags = new ArrayList<Tag>();
        tags.clear();
        Tag tag;
        Object  o;
        boolean end = false;
        while(!end){ 
            try{
                System.out.println("DataReceiver: Waiting data...");
                //Read object from client
                 o = _in.readObject();
                //Process data
                if ( o instanceof Tag ) {
                    //Read tag from client
                    tag = (Tag)o;
                    tags.add(tag);
                    System.out.println("DataReceiver: Received " + tag.toString());
                }else if ( o instanceof String ) {
                    //Read command from client
                    if ( o.equals(Message.QUIT.toString())) {
                        end = true;
                        System.out.println("DataReceiver: Communication ended.");
                    }
                    else  {
                        System.out.println("DataReceiver: Received cmd: " + o.toString());
                        proccessCommand(o.toString());
                    }
                }else if (o instanceof byte[] ) {
                    System.out.println("DataReceiver: Reading a image from client.");
                    proccessImage((byte[])o);
                }
                
                //Send ACK to client    
                _out.writeObject(Message.ACK.toString()); 
            }
            catch (IOException e) {      
                //The client is no longer connected
                end = true;
            }  
            catch (ClassNotFoundException ex) {
                    System.err.println("DataReceiver: Not class found"); 
                    System.err.print(ex);
            }
        }//while
            
        //Proccess data received
        if (!tags.isEmpty())
            _ui.updateUI(tags);
                    
        //Close our socket and streams
        try {
            if (_client != null )
                _client.close();
            if ( _in != null )
                _in.close();
            if (_out != null)
                _out.close();
        } catch (IOException ex) {
            System.err.println("DataReceiver: Error closing");
            System.err.print(ex);    
        }
    }
    
    /**
     * Action related to receive a command
     * @param cmd 
     */
    private void proccessCommand ( String cmd ) {
        if ( cmd.equals(Message.CALIBRATION_FAIL.toString()) ){
                _ui.updateUI(Message.CALIBRATION_FAIL);
        }
        else if ( cmd.equals(Message.CALIBRATION_OK.toString()) ){
            _ui.updateUI(Message.CALIBRATION_OK);
        }
    }
    
    /**
     * Action related to receive a image
     * @param cmd 
     */
    private void proccessImage ( byte[] img ) {
        _ui.updateUI(img);
    }
}
