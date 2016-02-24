/**
 * Created by Jamie on 06/11/2015.
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenOutput {

    private static final int WINDOW_WIDTH = 680;
    private static final int WINDOW_HEIGHT = 540;

    public CustomPanel mainPanel;

    public ScreenOutput(){
        //initialise windows
        //1. main window
        JFrame mainWindow = new JFrame("Original Image Window");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setLocation(0, 0);
        mainWindow.setVisible(true);

        mainPanel = new CustomPanel();
        mainWindow.setContentPane(mainPanel);
    }
}
