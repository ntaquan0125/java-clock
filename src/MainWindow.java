import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;


public class MainWindow {
    JFrame window;
    Container cont;
    
    private AnalogClock analogClock;
    private DigitalClock digitalClock;
    private boolean isAnalogClock = true;
    
    public MainWindow() {
        window = new JFrame("Java multimedia");
        cont = window.getContentPane();

        Calendar now = Calendar.getInstance();
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);
        int s = now.get(Calendar.SECOND);
        analogClock = new AnalogClock();
        analogClock.setTime(h, m, s);
        digitalClock = new DigitalClock(analogClock);

        JButton switchClock = new JButton("CHANGE CLOCK");
        switchClock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                isAnalogClock = !isAnalogClock;
                analogClock.setVisible(isAnalogClock);
                digitalClock.setVisible(!isAnalogClock);
            }
        });
        
        JButton updateTime = new JButton("UPDATE TIME");
        updateTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame dialog = new SyncTimeDialog();
            }
        });
        
        // CardLayout class to switch between two panels
        JPanel cards = new JPanel(new CardLayout());
        cards.add(analogClock);
        cards.add(digitalClock);
        analogClock.setVisible(isAnalogClock);
        digitalClock.setVisible(!isAnalogClock);
        cont.add(cards);
        
        JPanel setting = new JPanel();
        setting.setLayout(new GridLayout(2, 1, 10, 10));
        setting.setBorder(new EmptyBorder(10, 10, 10, 10));
        setting.add(switchClock);
        setting.add(updateTime);
        cont.add(setting, "East");
        
        window.setBounds(0, 0, 600, 450);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Run the Threads in the clock object
        analogClock.start();
        digitalClock.start();
    }
    
    public class SyncTimeDialog extends JFrame {
        Container cont;
        
        public SyncTimeDialog() {           
            JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(analogClock.getHour(), 0, 23, 1));
            JSpinner minutesSpinner = new JSpinner(new SpinnerNumberModel(analogClock.getMinute(), 0, 59, 1));
            JSpinner secondsSpinner = new JSpinner(new SpinnerNumberModel(analogClock.getSecond(), 0, 59, 1));
            
            JButton syncButton = new JButton("Sync");
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
          
            syncButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Calendar now = Calendar.getInstance();
                    int h = now.get(Calendar.HOUR_OF_DAY);
                    int m = now.get(Calendar.MINUTE);
                    int s = now.get(Calendar.SECOND);
                    hourSpinner.setValue(h);
                    minutesSpinner.setValue(m);
                    secondsSpinner.setValue(s);
                }
            });
            
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int h = (Integer) hourSpinner.getValue();
                    int m = (Integer) minutesSpinner.getValue();
                    int s = (Integer) secondsSpinner.getValue();
                    analogClock.setTime(h, m, s);
                    SyncTimeDialog.this.dispose();
                }
            });
            
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SyncTimeDialog.this.dispose();
                }
            });
            
            cont = this.getContentPane();
            cont.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5); // padding
            gbc.weightx = 1;
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            cont.add(new JLabel("Hours:"), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            cont.add(new JLabel("Minutes:"), gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 0;
            cont.add(new JLabel("Seconds:"), gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            cont.add(hourSpinner, gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 1;
            cont.add(minutesSpinner, gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 1;
            cont.add(secondsSpinner, gbc);

            gbc.anchor = GridBagConstraints.PAGE_END; //bottom of space
            gbc.gridx = 0;
            gbc.gridy = 2;
            cont.add(syncButton, gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 2;
            cont.add(okButton, gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 2;
            cont.add(cancelButton, gbc);
            
            setTitle("Update Time");
            setSize(300, 150);
            setVisible(true); 
        }   
    }
    
    public static void main(String args[]) {
        MainWindow mainWindow = new MainWindow();
    }
}
