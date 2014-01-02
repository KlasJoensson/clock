/*******************************************************************************
 * Copyright (c) 2014  Klas Jönsson <klas.joensson@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package joensson.clock;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class implements the graphics that's shows the analogue clock.
 * 
 * @author Klas Jönsson
 *
 */
public class Clockwork extends JPanel implements ActionListener {

	private static final long serialVersionUID = 2L;
	// The stuff that are needed
	private Timer tim = new Timer(1000, this);
	private int s=0,m=0,h=0, t=0;
	private Boolean paused = false, digitalTime = false;
	private int pointerStyle;
	private final int ROUND = 0;
	private final int LINE = 1;
	private final int OLDSTYLE = 2;
	private String alarmSound;//"/home/klas/workspace/Clock/bin/CHARGE.WAV";
	private Boolean alarmOn = false;
	private int alarmTime = 0;
	private function type;
	private PlaySound PS;
	
	/**
	 * The different functions of the clock
	 * 
	 * @author Klas Jönsson
	 *
	 */
	public enum function {
		EGGCLOCK,
		TIMER,
		CLOCK
	}
	
	/**
	 * The constructor of the class 
	 */
	public Clockwork(){
		setSize(160,160);
		setBackground(Color.GRAY);
		repaint();
		pointerStyle = ROUND;
		String path = System.getProperty("java.class.path");
		path = path.substring(0, path.length()-3);
		path += "sounds/";
		alarmSound = path + "CHARGE.WAV";
	} // End of constructor EggTimer
	
	/**
	 * This method paints the graphics 
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		// Declaring the variables that are needed 	
		g.setColor(Color.DARK_GRAY);
		int l = 3; // length of the lines in the clock face 
		int sd = 15; // From where to draw the circle of the clock face (both x and y)
		int dd = l*3; // Diameter of the circles in the clock face
		int dr= dd/2; 
		int d = 160; // Diameter of the clock
		int r=d/2; 
		int center = d/2+sd; // Center of the clock
		int noStep = 60; // Steps in the clock
		int x1,y1,x2,y2;
		double step;
		
		// Draws the clock face
		g.drawOval(sd, sd, d, d);	
		step = (Math.PI * 2)/noStep; 		
		for (int i=1; i <= noStep; i++){
			if (i%5 == 0){
				y1 = (int) (center + (r)*Math.sin((step*i)));
				x1 = (int) (center + (r)*Math.cos((step*i)));
				g.fillOval(x1-dr, y1-dr, dd, dd);
			}
			else{
				y1 = (int) (center + (r-l)*Math.sin((step*i)));
				x1 = (int) (center + (r-l)*Math.cos((step*i)));
				y2 = (int) (center + (r+l)*Math.sin((step*i)));
				x2 = (int) (center + (r+l)*Math.cos((step*i)));
				g.drawLine(x1, y1, x2 ,y2);
			}			
		}
		
		// Draws the second pointers
		g.setColor(Color.RED);
		y2 = (int) (center + (r)*Math.sin((step*(s-15))));
		x2 = (int) (center + (r)*Math.cos((step*(s-15))));
		if (pointerStyle == ROUND){
			g.drawOval(x2-8, y2-8, 16, 16);
		}
		if (pointerStyle == LINE){
			g.drawLine(center, center, x2 ,y2);
		}
		if (pointerStyle == OLDSTYLE){
			g.drawOval(x2-8, y2-8, 16, 16);
			y2 = (int) (center + (r-8)*Math.sin((step*(s-15))));
			x2 = (int) (center + (r-8)*Math.cos((step*(s-15))));
			g.drawLine(center, center, x2,y2);
			y1 = (int) (center + (r+8)*Math.sin((step*(s-15))));
			x1 = (int) (center + (r+8)*Math.cos((step*(s-15))));
			y2 = (int) (center + (r+16)*Math.sin((step*(s-15))));
			x2 = (int) (center + (r+16)*Math.cos((step*(s-15))));
			g.drawLine(x1 ,y1, x2 ,y2);
		}
		
		// Draws the minutes pointers 
		g.setColor(Color.GREEN);
		y2 = (int) (center + (r)*Math.sin((step*(m-15))));
		x2 = (int) (center + (r)*Math.cos((step*(m-15))));
		if (pointerStyle == ROUND){
			g.drawOval(x2-5, y2-5, 10, 10);
		}
		if (pointerStyle == LINE){
			g.drawLine(center, center, x2 ,y2);
		}
		if (pointerStyle == OLDSTYLE){
			g.drawOval(x2-5, y2-5, 10, 10);
			y2 = (int) (center + (r-5)*Math.sin((step*(m-15))));
			x2 = (int) (center + (r-5)*Math.cos((step*(m-15))));
			g.drawLine(center, center, x2 ,y2);
			y1 = (int) (center + (r+5)*Math.sin((step*(m-15))));
			x1 = (int) (center + (r+5)*Math.cos((step*(m-15))));
			y2 = (int) (center + (r+10)*Math.sin((step*(m-15))));
			x2 = (int) (center + (r+10)*Math.cos((step*(m-15))));
			g.drawLine(x1 ,y1, x2 ,y2);
		}
		
		// Draws the hour pointers
		g.setColor(Color.CYAN);
		y2 = (int) (center + (r)*Math.sin((step*(h-15))));
		x2 = (int) (center + (r)*Math.cos((step*(h-15))));
		if (pointerStyle == ROUND){
			g.drawOval(x2-3, y2-3, 6, 6);
		}
		if (pointerStyle == LINE) {
			g.drawLine(center, center, x2 ,y2);
		}
		if (pointerStyle == OLDSTYLE){
			g.drawOval(x2-3, y2-3, 6, 6);
			y2 = (int) (center + (r-3)*Math.sin((step*(h-15))));
			x2 = (int) (center + (r-3)*Math.cos((step*(h-15))));
			g.drawLine(center, center, x2 ,y2);
			y1 = (int) (center + (r+3)*Math.sin((step*(h-15))));
			x1 = (int) (center + (r+3)*Math.cos((step*(h-15))));
			y2 = (int) (center + (r+6)*Math.sin((step*(h-15))));
			x2 = (int) (center + (r+6)*Math.cos((step*(h-15))));
			g.drawLine(x1 ,y1, x2 ,y2);
		}
		
		// To see the time in digits
		if (digitalTime){
			String text = "";
			int realH = h/5;
			if (realH<10)
				text = text + "0" + realH + ":";
			else
				text = text + realH + ":";
			if (m<10)
				text = text + "0" + m + ":";
			else
				text = text + m + ":";
			if (s<10)
				text = text + "0" + s;
			else
				text = text + s;
			g.drawString(text, center - 27, (center*2+sd/2));
		}
	} //End of method paint
	
	/**
	 * The method that takes care of stuff that happens while the program's running
	 * 
	 * @param ActionEvent
	 */
	public void actionPerformed(ActionEvent e){
		if (t >= alarmTime){
			if (alarmOn)
				PS = new PlaySound(alarmSound);	
		}
		if (type == function.CLOCK || type == function.TIMER){
			if (m%12 == 0 && s == 60){	
				h++;
				if (h == 300 && type == function.CLOCK)
					h = 0;
			}
			if (m == 60)
				m = 0;
			if (s == 60){
				m++;
				s = 0;
			}
			t++;
			s++;
			repaint();
		}
		if (type == function.EGGCLOCK){
		//else {// it's an egg-clock...
			if (t == 0){
				// The egg-clock-alarm 
				stop();
				PS = new PlaySound(alarmSound); 
			}
			else if (m%12 == 0 && s == 0)
				h--;
			else if (m == 0)
				m = 60;
			else if (s == 0){
				m--;
				s = 60;
			}
			t--;
			s--;
			repaint();
		}		
		
	}// End of method actionPerformed
	
	/**
	 * This method starts the clock
	 * 
	 * @param startTime
	 * @param cu
	 */
	public void start(int startTime, function f){
		t = startTime;
		if (t != 0){
			h = t/3600*5;
			m = (t-h*3600/5)/60;
			s = t - h*3600/5 - m*60;
		}
		tim.start();
		paused=false;
		type = f;
	} // End of method start

	/**
	 * This method pauses the clock
	 */
	public void pause() {
		if (paused){
			tim.start();
			paused=false;
		}else {
			tim.stop();
			paused=true;
		}
	} // End of method pause
	
	/**
	 * This method stops the clock
	 */
	public void stop(){
		tim.stop();
		h=m=s=0;
		repaint();
	} // End of method stop
	
	/**
	 * A method to set the style of the pointer
	 */
	public void setPointerStyle(int use){
		if (use == 0)
			pointerStyle = ROUND;
		if (use == 1)
			pointerStyle = LINE;
		if (use == 2)
			pointerStyle = OLDSTYLE;
		repaint();
	} // End of method setPointerStyle
	
	/**
	 * A method to show/hide the digital clock
	 */
	public void showDigitalTime(){
		digitalTime = !digitalTime;
		repaint();
	} // End of method showDigitalTime
	
	/**
	 * A method that sets a new alarm sound
	 * 
	 * @param fileName
	 */
	public void setAlarmSound(String fileName){
		alarmSound = fileName;
	}
	
	/**
	 * This method starts and stops the alarm for the clock
	 * 
	 * @param time
	 */
	public void setAlarm(int time){
		alarmTime = time;
		alarmOn = !alarmOn;
	}
}// End of class EggTimer
