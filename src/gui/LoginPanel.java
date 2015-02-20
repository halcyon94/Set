package gui;

import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.event.*;

import com.alee.extended.window.WebPopOver;


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
	private ClientConnection connection;

	
	public LoginPanel(ClientConnection conn) {
		this.connection = conn;
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
	
	public JButton getButt() {
		return loginButton;
	}
	
	public String getUser() {
		return userText.getText();
	}
	
	public String getPassword() {
//		try {
//			MessageDigest m = MessageDigest.getInstance("SHA1");
//			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//			OutputStreamWriter out = new OutputStreamWriter(byteOut, "ASCII");
//			out.write(passwordText.getPassword());
//			out.flush();
//		    byte[] bytes = byteOut.toByteArray();
//			return new String(m.digest(bytes)); //BAD! NOT SECURE!!! NO NO NO NO
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "Error";
		return new String(passwordText.getPassword()); //BAD! NOT SECURE!!! NO NO NO NO
	}
	
	public void showUserPopup(String text) {
		WebPopOver popOver = new WebPopOver ();
        popOver.setCloseOnFocusLoss ( true );
        popOver.setMovable(false);
        popOver.setMargin ( 5 );
        popOver.add ( new JLabel (text) );
        popOver.show ( userText);
	}
	
	public void showPassPopup(String text) {
		WebPopOver popOver = new WebPopOver ();
        popOver.setCloseOnFocusLoss ( true );
        popOver.setMovable(false);
        popOver.setMargin ( 5 );
        popOver.add ( new JLabel (text) );
        popOver.show ( passwordText);
	}
	
	public void addListeners(ActionListener login, ActionListener reg) {
		loginButton.addActionListener(login);
		registerButton.addActionListener(reg);
	}
	
	//remove all action listeners
	public void detachListeners() {
		for(ActionListener l : loginButton.getActionListeners())
			loginButton.removeActionListener(l);
		for(ActionListener l : registerButton.getActionListeners())
			registerButton.removeActionListener(l);
	}
	
	public void wongSound() {
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("wrong.wav"));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	    }
	}
}