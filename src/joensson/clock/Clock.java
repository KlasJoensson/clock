package joensson.clock;
import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * This class create the surroundings with the control panel for the EggTimer-object
 * 
 * @author Klas Jönsson
 *
 */
public class Clock extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	// The stuff needed to control the egg clock...
	private String[] alt = {"Äggklocka", "Tidtagning", "Klocka"};
	private Boolean started = false, alarmOn = false, paused = false;;
	private String[] pointerText = {"Ringar", "Linjer", "Gammaldags"};
	private Clockwork.function setClockType = Clockwork.function.EGGCLOCK;

	private JLabel text1 = new JLabel("Ställ in start tid:");
	private JTextField seconds = new JTextField("0",2);
	private JTextField minutes = new JTextField("0",2);
	private JTextField hours = new JTextField("0",2);
	private JButton start;
	private JButton stop;
	private JButton zero = new JButton("Rensa");
	private Clockwork ET = new Clockwork();
	private JPanel p = new JPanel();
	private JLabel c1 = new JLabel(":", JLabel.CENTER);
	private JLabel c2 = new JLabel(":", JLabel.CENTER);
	private JMenuBar mb = new JMenuBar();
	private JMenu menu = new JMenu("Inställingar");
	private JMenuItem digitalClock = new JMenuItem("Visa/dölj digital klocka");
	private JMenuItem alarmSound = new JMenuItem("Ändra Larmljudet...");
	private JMenuItem quit = new JMenuItem("Avsluta");
	private JMenuItem mcStart = new JMenuItem("Starta");
	private JMenuItem mcPause = new JMenuItem("Paus");
	private JMenuItem mcStop = new JMenuItem("Stop");
	private JMenuItem mcZero = new JMenuItem("Rensa");
	private JMenu pointerMenu = new JMenu("Visarnas utsende");
	private JMenuItem[] pointerStyle = new JMenuItem[pointerText.length];	
	private JMenu clockMenu = new JMenu("Ändra Klocktypen");
	private JMenuItem[] clockType = new JMenuItem[alt.length];
	private JMenu menu2 = new JMenu("Arkiv");
	private JMenuItem about  = new JMenuItem("Om");
	
	public Clock() {
		// Get the path to the images
		String path = System.getProperty("java.class.path");
		path = path.substring(0, path.length()-3);
		path += "images/";
		
		// Create a container to put the stuff from above in
		GridBagLayout m = new GridBagLayout();
		Container c = (Container) getContentPane();
		c.setLayout(m);
		p.setLayout(new GridLayout(1,5));
		
		// Create the menus	
		setJMenuBar(mb);
		// The first menu
		mb.add(menu2);
		menu2.add(mcStart);
		menu2.add(mcPause);
		menu2.add(mcStop);
		menu2.add(mcZero);
		menu2.addSeparator();
		menu2.add(quit);
		mb.add(menu);
		
		for (int i = 0; i < pointerText.length; i++) {
			pointerStyle[i] = new JMenuItem(pointerText[i]);
			pointerMenu.add(pointerStyle[i]);
			pointerStyle[i].addActionListener(this);
		}
		for (int i = 0; i < alt.length; i++) {
			clockType[i] = new JMenuItem(alt[i]);
			clockMenu.add(clockType[i]);
			clockType[i].addActionListener(this);
			clockType[i].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1+i, 0));
		}
		menu.add(clockMenu);
		menu.add(pointerMenu);
		menu.add(digitalClock);
		menu.add(alarmSound);	
		menu.add(about);
		
		// Adding the keyboard shortcuts...
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		mcStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
		mcPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
		mcStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		digitalClock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
		pointerStyle[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		pointerStyle[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		pointerStyle[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
                        
		// Put in the menu 
		GridBagConstraints con = new GridBagConstraints();
		
		// Put in the label
		con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 8;
		con.gridwidth = 3;
		m.setConstraints(text1, con);
		c.add(text1);
		
		// Putting in the fields for setting e.g. the alarm
		p.add(hours);
		p.add(c1);
		p.add(minutes);
		p.add(c2);
		p.add(seconds);
		con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 9;
		con.gridwidth = 5;
		m.setConstraints(p, con);
		c.add(p);
		
		// Putting in the control buttons
		start = new JButton(new ImageIcon(path+"play.gif"));
		con = new GridBagConstraints();
		con.gridx = 2;
		con.gridy = 10;
		con.gridwidth = 1;
		m.setConstraints(start, con);
		c.add(start);
		con = new GridBagConstraints();
		con.gridx = 3;
		con.gridy = 10;
		con.gridwidth = 2;
		m.setConstraints(zero, con);
		c.add(zero);
		stop = new JButton(new ImageIcon(path+"stop.gif"));
		con = new GridBagConstraints();
		con.gridx = 5;
		con.gridy = 10;
		con.gridwidth = 1;
		m.setConstraints(stop, con);
		c.add(stop);
		
		// Putting in the EggClock-object
		con = new GridBagConstraints();
		con.gridx = 0;
		con.gridy = 0;
		con.gridwidth = 7;
		con.gridheight = 7;
		con.ipadx = 180;
		con.ipady = 190;
		m.setConstraints(ET, con);
		c.add(ET);
		
		// Start the listeners
		stop.addActionListener(this);
		start.addActionListener(this);
		zero.addActionListener(this);
		mcStop.addActionListener(this);
		mcStart.addActionListener(this);
		mcZero.addActionListener(this);
		mcPause.addActionListener(this);
		pointerMenu.addActionListener(this);
		clockMenu.addActionListener(this);
		digitalClock.addActionListener(this);
		quit.addActionListener(this);
		about.addActionListener(this);
		alarmSound.addActionListener(this);
		// Set up the window
		pack();
		stop.setEnabled(false);
		mcStop.setEnabled(false);
		mcPause.setEnabled(false);
		setTitle("Ägguret");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	} // End of constructor Clock
	
	/**
	 * The method that takes care of stuff that happens while the program's running
	 */
	public void actionPerformed(ActionEvent e){
		Object s = e.getSource();
		int time=0;
		String path = System.getProperty("java.class.path");
		path = path.substring(0, path.length()-3);
		path += "images/";
		if (s == quit)
			// If "Avsluta" (i.e. quit) from the menu is chosen
			// we simply exit the program
			System.exit(1);
		if (s == start || s == mcStart || s == mcPause){	
			// If the start-button is pressed, different thing happens depending on the 
			// setting in the combobox.
			if (setClockType == Clockwork.function.EGGCLOCK || 
					setClockType == Clockwork.function.TIMER){
				stop.setEnabled(true);
				mcStop.setEnabled(true);
				hours.setEditable(false);
				minutes.setEditable(false);
				seconds.setEditable(false);
				// If its started
				if (started){
					// If it's started we may want to pause it
					if (paused){
						start.setIcon(new ImageIcon(path+"play.gif"));
						mcStart.setEnabled(false);
						mcPause.setEnabled(true);
						paused = false;
					}else {
						start.setIcon(new ImageIcon(path+"pause.gif"));
						mcStart.setEnabled(true);
						mcPause.setEnabled(false);
						paused = true;
					}
					ET.pause();
				} else {
					time = getTime();
					ET.start(time, setClockType);
					start.setIcon(new ImageIcon(path+"pause.gif"));
					mcStart.setEnabled(false);
					mcPause.setEnabled(true);
					started = true;
					paused = false;
				}
			}
			if (setClockType == Clockwork.function.CLOCK){
				if (alarmOn){
					hours.setEditable(true);
					minutes.setEditable(true);
					seconds.setEditable(true);
					start.setIcon(new ImageIcon(path+"alarmOff.gif"));
					mcStart.setEnabled(false);
					mcPause.setEnabled(true);
					ET.setAlarm(0);
					zero.setEnabled(true);
					alarmOn = false;
				}else {
					hours.setEditable(false);
					minutes.setEditable(false);
					seconds.setEditable(false);
					time = getTime();
					start.setIcon(new ImageIcon(path+"alarmOff.gif"));
					mcStart.setEnabled(false);
					mcPause.setEnabled(true);
					ET.setAlarm(time);
					zero.setEnabled(false);
					alarmOn = true;
				}
			}
			
		}
		if (s == clockType[0]){
			// If is a egg-clock...
			setClockType = Clockwork.function.EGGCLOCK;
			started = false;
			stop.setIcon(new ImageIcon(path+"stop.gif"));
			start.setIcon(new ImageIcon(path+"play.gif"));
			text1.setText("Ställ in start tid:");
			stop.setEnabled(false);
			start.setEnabled(true);
			zero.setEnabled(true);
			ET.stop();
			setTitle("Ägguret");
		}
		if (s == clockType[1]){
			//... or a timer...
			setClockType = Clockwork.function.TIMER;
			started = false;
			stop.setIcon(new ImageIcon(path+"stop.gif"));
			start.setIcon(new ImageIcon(path+"play.gif"));
			text1.setText("Börja vid tiden:");
			//stop.setEnabled(false);
			start.setEnabled(true);
			zero.setEnabled(true);
			ET.stop();
			setTitle("Tidtagaren");
		}
		if (s == clockType[2]){
			//... or just what time it is.
			setClockType = Clockwork.function.CLOCK;
			Calendar cal= Calendar.getInstance();
			time = cal.get(Calendar.SECOND) + 60 * cal.get(Calendar.MINUTE) +
			3600*cal.get(Calendar.HOUR_OF_DAY);
			stop.setEnabled(false);
			mcStart.setText("Alarm på");
			mcPause.setText("Alarm av");
			mcStop.setEnabled(false);
			if (alarmOn){
				start.setIcon(new ImageIcon(path+"alarmOff.gif"));
				mcStart.setEnabled(false);
				mcPause.setEnabled(true);
			}
			else {
				start.setIcon(new ImageIcon(path+"alarmOn.gif"));
				mcStart.setEnabled(true);
				mcPause.setEnabled(false);
			}
			text1.setText("Alarm tid:");
			ET.start(time, setClockType);
			setTitle("Uret");
			ET.stop();
			ET.start(time, setClockType);
		}
		if (s == stop || s == mcStop){
			// If we stop it...
			stop.setEnabled(false);
			hours.setEditable(true);
			minutes.setEditable(true);
			seconds.setEditable(true);
			started = false;
			start.setIcon(new ImageIcon(path+"play.gif"));
			ET.stop();
		}
		if (s == digitalClock){
			// Shows/hide the digital clock
			ET.showDigitalTime();
		}
		for (int i = 0; i < pointerText.length; i++) {
			if (s == pointerStyle[i])
				ET.setPointerStyle(i);
		}
		if (s == about){
			JOptionPane.showMessageDialog(null, " Uret \n En kombination av ett äggur, " +
					"tidtagare och klocka. \n Version 2.0 \n Av Klas Jönsson \n \u00A9 2011");			
		}
		if (s == alarmSound){
			String fileName;
			JFileChooser fc = new JFileChooser();
			int res = fc.showOpenDialog(null);
			if (res == JFileChooser.APPROVE_OPTION){
				fileName = fc.getSelectedFile().getAbsolutePath();
				ET.setAlarmSound(fileName);
			} else if (res == JFileChooser.CANCEL_OPTION) {
				return;
			}
			else
				JOptionPane.showMessageDialog(null, "Hittar inte filen... ;(");
		}
		if (s == zero || s == mcZero){
			time = 0;
			hours.setText("0");
			minutes.setText("0");
			seconds.setText("0");
		}
	}// End of method actionPerformed
	
	/**
	 * This method turns what's in the text-fields into seconds. If something in any 
	 * 	field isn't possible to make an integer of it treat it as if its zero.
	 *  
	 * @return The time in seconds as an integer  
	 */
	private int getTime(){
		int t, h, m, s;
		try {
			h = Integer.parseInt(hours.getText());
		}
		catch (NumberFormatException e){
			h = 0;
		}
		try {
			m = Integer.parseInt(minutes.getText());
		}
		catch (NumberFormatException e){
			m = 0;
		}try {
			s = Integer.parseInt(seconds.getText());
		}
		catch (NumberFormatException e){
			s = 0;
		}	
		t = s +	60 * m + 3600*h;
		return t;
	}
	
	KeyListener l = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();//, time = 0;
			// Makes enter the same as a click on active button
			if (k == KeyEvent.VK_ENTER && e.getSource() instanceof AbstractButton)
				((AbstractButton) e.getSource()).doClick();
		}
	};
	
	/**
	 * The main method that starts the hole thing...
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		Clock c = new Clock();		
	} // End of method main

} // End of class Clock
