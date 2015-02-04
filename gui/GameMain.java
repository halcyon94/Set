package gui;

import javax.swing.*;
import java.awt.event.*;

public class GameMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginFrame login = new LoginFrame();
				addLoginListeners(login);
			}
		});
	}
	
	private static void addLoginListeners(LoginFrame f) {
		//Listener for login button
		ActionListener a1 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	Gooey test = new Gooey();
				test.setVisible(true);
            }
        };
        //Listener for register button
        ActionListener a2 = new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	JOptionPane.showMessageDialog(new JFrame(), "Derp.");
            }
        };
        f.addListeners(a1, a2);
	}
}