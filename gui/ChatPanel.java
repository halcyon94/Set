/**
 *	Set GUI in-game chat panel
 *	@author Dolen Le
 *	@version 1.0
 */
package gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
			
	private final static String defaultString = "Enter Message";
	
	/**
	 *	Chat panel constructor
	 */
	public ChatPanel() {
		super(new BorderLayout(3,3));
		setBorder(BorderFactory.createTitledBorder("Chat"));
		
		final JFormattedTextField msgField = new JFormattedTextField(defaultString);
		msgField.setForeground(Color.LIGHT_GRAY);
        msgField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            	if(msgField.getText().equals(defaultString)) {
					msgField.setForeground(Color.BLACK);
					msgField.setText("");
                }
            }
            public void focusLost(FocusEvent e){
            	if(msgField.getText().isEmpty()) {
					msgField.setForeground(Color.LIGHT_GRAY);
					msgField.setText(defaultString);
				}
            }
        });
        
        JTextArea msgArea = new JTextArea();
        msgArea.setRows(5);
        
        JButton sendButton = new JButton("SEND");
        
        add(msgField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);
        add(msgArea, BorderLayout.NORTH);
	}
}