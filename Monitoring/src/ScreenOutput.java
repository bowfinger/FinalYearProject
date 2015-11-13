/**
 * Created by Jamie on 06/11/2015.
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenOutput {

    private static final int WINDOW_WIDTH = 680;
    private static final int WINDOW_HEIGHT = 540;

    private JFrame mainWindow;
    private JFrame filterWindow;
    private JFrame sliderWindow;

    public JPanel sliderPanel;
    public CustomPanel mainPanel;
    public CustomPanel filterPanel;

    public ScreenOutput(){
        //initialise windows
        //1. main window
        mainWindow = new JFrame("Original Image Window");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setLocation(0, 0);
        mainWindow.setVisible(true);

        mainPanel = new CustomPanel();
        mainWindow.setContentPane(mainPanel);

        //2. filtered frame image
        filterWindow = new JFrame("Filtered Image Window");
        filterWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        filterWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        filterWindow.setLocation(WINDOW_WIDTH + 10, 0);
        filterWindow.setVisible(true);

        filterPanel = new CustomPanel();
        filterWindow.setContentPane(filterPanel);

        //3. slider window
        sliderWindow = new JFrame("Threshold Sliders");
        sliderWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sliderWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        sliderWindow.setLocation(0, WINDOW_HEIGHT + 10);
        sliderWindow.setVisible(true);

        sliderPanel = new JPanel();
        //sliderPanel.add(mogThresholdSlider);
        //sliderPanel.add(mogLearnRateSlider);

        sliderWindow.setContentPane(sliderPanel);

    }
}
