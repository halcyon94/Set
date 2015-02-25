package gui;

import javax.swing.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import java.awt.event.*;
import java.security.MessageDigest;

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
	private WebPopOver popupMsg = new WebPopOver();
	private JLabel popupLabel = new JLabel();
	
	private FocusListener userFocus = new FocusAdapter() {
		public void focusGained(java.awt.event.FocusEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					userText.selectAll();
				}
			});
		}
	};
	
	private FocusListener passFocus = new FocusAdapter() {
		public void focusGained(java.awt.event.FocusEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					passwordText.selectAll();
				}
			});
		}
	};

	
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

		popupMsg.setCloseOnFocusLoss(true);
		popupMsg.setMovable(false);
		popupMsg.setMargin(5);
		popupMsg.add(popupLabel);
		
		userText.addFocusListener(userFocus);
		
		passwordText.addFocusListener(passFocus);
	}

	public JButton getButt() {
		return loginButton;
	}
	
	public String getUser() {
		return userText.getText();
	}
	
	/**
	 * Get the entered password hash
	 * @return hashed password text
	 */
	public String getPassword() {
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");
			return new String((new HexBinaryAdapter()).marshal(md.digest(new String(passwordText.getPassword()).getBytes())));
		} catch (Exception e) {
			showUserPopup("Unhandled exception.");
			return null;
		}
	}

	/**
	 * Show a popup notification on the username field
	 * @param text notification text
	 */
	public void showUserPopup(String text) {
		popupLabel.setText(text);
		popupMsg.show(userText);
	}
	
	/**
	 * Show a popup notification on the password field
	 * @param text notification text
	 */
	public void showPassPopup(String text) {
		popupLabel.setText(text);
		popupMsg.show(passwordText);
	}
	
	/**
	 * Adds the provided action listeners to the login and register buttons.
	 * @param login login button listener
	 * @param reg register button listener
	 */
	public void addListeners(ActionListener login, ActionListener reg) {
		loginButton.addActionListener(login);
		registerButton.addActionListener(reg);
	}

	/**
	 * Removes all access listeners from the LoginPanel elements, allowing garbage collection to clear them.
	 */
	public void detachListeners() {
		for(ActionListener l : loginButton.getActionListeners())
			loginButton.removeActionListener(l);
		for(ActionListener l : registerButton.getActionListeners())
			registerButton.removeActionListener(l);
		userText.removeFocusListener(userFocus);
		passwordText.removeFocusListener(passFocus);
	}
}