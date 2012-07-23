/*
 * TagRecognizer.java
 */

package tagrecognizerdesktop;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class TagRecognizer extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        this.getMainFrame().setTitle("TagRecognizer Desktop");
        _view = new TagRecognitionView();
        show(_view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
        root.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            if (_view!=null)
                _view.closeApp();
        }

    });
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of TagRecognizer
     */
    public static TagRecognizer getApplication() {
        return Application.getInstance(TagRecognizer.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(TagRecognizer.class, args);
    }
    
    private TagRecognitionView _view;
}
