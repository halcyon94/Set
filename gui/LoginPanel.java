package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

/**
 *	Set GUI login
 *	@author Dolen Le
 *	@version 1.0
 */
public class LoginPanel extends JPanel {

	private JButton loginButton = new JButton("Login");
	private JButton registerButton = new JButton("Register");
	private JTextField userText = new JTextField(20);
	private JPasswordField passwordText = new JPasswordField(20);

	
	public LoginPanel() {
		buildPanel();
	}
	
	private void buildPanel() {

		setLayout(null);

		JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(10, 10, 80, 25);
		add(userLabel);

		userText.setBounds(100, 10, 160, 25);
		add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		add(passwordLabel);

		passwordText.setBounds(100, 40, 160, 25);
		add(passwordText);

		loginButton.setBounds(10, 80, 80, 25);
		add(loginButton);
		
		registerButton.setBounds(180, 80, 80, 25);
		add(registerButton);
	}
	
	public String getUser() {
		return userText.getText();
	}
	
	public String getPassword() {
		return new String(passwordText.getPassword()); //BAD! NOT SECURE!!! NO NO NO NO
	}
	
	public void addListeners(ActionListener login, ActionListener reg) {
		loginButton.addActionListener(login);
		registerButton.addActionListener(reg);
	}
}