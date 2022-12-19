import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DigitalClock extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    
    private Thread thread = null;
    private JTextField timeField;
    private AnalogClock analogClock;
    
    public DigitalClock(AnalogClock analogClock) {
        // For synchronization
        this.analogClock = analogClock;
        
        setLayout(new GridBagLayout());
        timeField = new JTextField(10);
        timeField.setFont(new Font("sansserif", Font.PLAIN, 48));
        timeField.setPreferredSize(new Dimension(400, 100));
        add(timeField, new GridBagConstraints());
    }
    
    private void tick() {
        String sf = String.format("%02d:%02d:%02d", analogClock.getHour(), analogClock.getMinute(), analogClock.getSecond());  
        timeField.setText(sf);
        timeField.setHorizontalAlignment(JTextField.CENTER);
        timeField.getCaret().setVisible(false);
    }
    
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    public void stop() {
        if (thread != null) {
            thread = null;
        }
    }
    
    public void run() {
        while (thread != null) {
            try {
                tick();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                stop();
            }
        }
    }
}
