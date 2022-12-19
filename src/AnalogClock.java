import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class AnalogClock extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private static final float radPerTick = (float)(Math.PI / 30.0);
    private static final float radPerNum = (float)(Math.PI / 6.0);
    private static final int MARGIN = 50;
    private static final int SIZE = 300;
    
    private Thread thread = null;
    
    private int hour, minute, second;
    private int centerX, centerY;

    public AnalogClock() {
        centerX = MARGIN + SIZE / 2;
        centerY = MARGIN + SIZE / 2;
    }
    
    public void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
    
    private void tick() {
        if (second < 59) {
            second++;
        } else {
            second = 0;
            if (minute < 59) {
                minute++;
            } else {
                minute = 0;
                if (hour < 11) {
                    hour++;
                } else {
                    hour = 0;
                }
            }
        }
    }

    private void drawFace(Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.drawOval(MARGIN - 2, MARGIN - 2, SIZE + 4, SIZE + 4);
        g.setColor(Color.WHITE);
        g.fillOval(MARGIN, MARGIN, SIZE, SIZE);
        
        try {                
            Image img = ImageIO.read(new File("./background.png"));
            Graphics2D g2d = (Graphics2D)g.create();
            img = img.getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.drawImage(img, MARGIN, MARGIN, this);
         } catch (IOException e) {}
    }

    private void drawNumbers(Graphics g) {
        // Hour marks
        for (int s = 0; s < 60; s++) {
            double angle = radPerTick * s;
            int tickStart = (s % 5 == 0) ? SIZE / 2 - 12 : SIZE / 2 - 6;
            int tickEnd = SIZE/2 - 3;
            
            g.setColor(Color.BLACK);
            int startX = (int)(tickStart * Math.sin(angle));
            int startY = (int)(tickStart * -Math.cos(angle));
            int endX = (int)(tickEnd * Math.sin(angle));
            int endY = (int)(tickEnd * -Math.cos(angle));
            g.drawLine(centerX + startX, centerX + startY, centerX + endX, centerX + endY);
        }
        
        for (int n = 1; n <= 12; n++) {
            double angle = radPerNum * n;
            
            g.setFont(g.getFont().deriveFont(Font.BOLD, 20));
            g.setColor(Color.BLACK);
            int endX = (int)((SIZE / 2 - 25) * Math.sin(angle));
            int endY = (int)((SIZE / 2 - 25) * -Math.cos(angle));
            g.drawString("" + n, centerX + endX - 6, centerY + endY + 6);
        }
    }

    private void drawHands(Graphics2D g) {
        double secondAngle = second * radPerTick;
        double minuteAngle = (minute + (second / 60.0)) * radPerTick;
        double hourAngle = (hour + (minute / 60.0)) * radPerNum;
        
        int secondLength = (int)(SIZE * 0.36);
        int minuteLength = (int)(SIZE * 0.28);
        int hourLength = (int)(SIZE * 0.2);
        
        g.setStroke(new BasicStroke(3));
        int endX, endY;
        g.setColor(Color.RED);
        endX = (int)(secondLength * Math.sin(secondAngle));
        endY = (int)(secondLength * -Math.cos(secondAngle));
        g.drawLine(centerX, centerY, centerX + endX, centerY + endY);
        g.setColor(Color.BLACK);
        endX = (int)(minuteLength * Math.sin(minuteAngle));
        endY = (int)(minuteLength * -Math.cos(minuteAngle));
        g.drawLine(centerX, centerY, centerX + endX, centerY + endY);        
        endX = (int)(hourLength * Math.sin(hourAngle));
        endY = (int)(hourLength * -Math.cos(hourAngle));
        g.drawLine(centerX, centerY, centerX + endX, centerY + endY);
        
        // Center point of the Clock
        g.setColor(Color.BLACK);
        g.drawOval(centerX - 3, centerY - 3, 6, 6);
        g.setColor(Color.RED);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
    }

    public void paint(Graphics g) {
        super.paint(g);    
        
        drawFace((Graphics2D)g);
        drawNumbers(g);  
        drawHands((Graphics2D)g);
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
                repaint();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                stop();
            }
        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
