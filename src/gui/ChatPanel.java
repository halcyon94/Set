package gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;

/**
 *	Set GUI in-game chat panel
 *	@author Dolen Le
 *	@version 1.0
 */
public class ChatPanel extends JPanel {
			
	private final static String defaultString = "Enter Message";
	private JEditorPane msgArea = new JEditorPane("text/html", null);
	
	/**
	 *	Chat panel constructor
	 */
	public ChatPanel(int height) {
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
        msgArea.setEditable(false);
        //msgArea.setContentType("text/html");
        JScrollPane scrollArea = new JScrollPane(msgArea);
        scrollArea.setPreferredSize(new Dimension(100, height));
        
        JButton sendButton = new JButton("SEND");
        
        add(msgField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);
        add(scrollArea, BorderLayout.NORTH);
	}
	
	/**
	 *	Display a system message
	 *	@param message The message text to be shown in bold.
	 */
	public void systemMessage(String message) {
		HTMLDocument d = (HTMLDocument) msgArea.getDocument();
		SimpleAttributeSet bold = new SimpleAttributeSet();
        StyleConstants.setBold(bold, true);
		try {
			d.insertString(d.getLength(), message+"\n", bold);
		} catch (javax.swing.text.BadLocationException e) {
			System.out.println("You smurfed up");
		}
		msgArea.setCaretPosition(d.getLength()); //scroll to bottom
	}
	
	public void userMessage(String message, String username, Color userColor) {
		
	}
}