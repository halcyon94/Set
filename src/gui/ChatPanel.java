package gui;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import java.awt.*;
import java.io.IOException;

import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.*;

/**
 *	Set GUI in-game chat panel
 *	@author Dolen Le
 *	@version 1.0
 */
public class ChatPanel extends JPanel {
			
	private final static String defaultString = "Enter Message";
	private JEditorPane msgArea = new JEditorPane("text/html", null);
	private JButton sendButton = new JButton("SEND");
	private JFormattedTextField msgField = new JFormattedTextField(defaultString);
	private ClientConnection connection;
	private int myID;
	private int gameID = 0;

	WebToggleButton lobbyToggle = new WebToggleButton("Lobby");
	WebToggleButton gameToggle = new WebToggleButton("Game");
	WebButtonGroup toggleGroup = new WebButtonGroup(true, lobbyToggle, gameToggle);

	/**
	 *	Chat panel constructor
	 *	@param height Preferred height of the message field
	 *	@param connect	ClientConnection object
	 *	@param uid	ID of currently logged in user
	 */
	public ChatPanel(int height, ClientConnection connect, int uid) {
		super(new BorderLayout(3,3));
		myID = uid;
		connection = connect;
		setBorder(BorderFactory.createTitledBorder("Chat"));
		
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
		ActionListener sendListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = msgField.getText().replace('`', ' ');
				if(!text.isEmpty() && !text.equals(defaultString)) {
					if(gameID != 0)
						connection.sendChat(myID, text, gameID);
					else
						connection.sendChat(myID, text);
					msgField.setText("");	
				}
			}
		};
		msgField.addActionListener(sendListener);
		sendButton.addActionListener(sendListener);
        msgArea.setEditable(false);
        msgArea.setText("<html id='body'></html>");
        msgArea.setText(msgArea.getText().trim());
        JScrollPane scrollArea = new JScrollPane(msgArea);
        scrollArea.setPreferredSize(new Dimension(100, height));
        
        add(msgField, BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);
        add(scrollArea, BorderLayout.NORTH);
	}
	
	/**
	 * ChatPanel constructor with gameID
	 * @param height Preferred height of the message field
	 * @param connect ClientConnection object
	 * @param uid user ID
	 * @param gid game ID
	 */
	public ChatPanel(int height, ClientConnection connect, int uid, int gid) {
		this(height, connect, uid);
		toggleGroup.setButtonsDrawFocus(false);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
		buttonPanel.add(toggleGroup);
        buttonPanel.add(sendButton);
        add(buttonPanel, BorderLayout.EAST);
		this.gameID = gid;
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
			d.insertBeforeEnd(d.getElement("body"), "<div><b>"+message+"</b></div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		msgArea.setCaretPosition(d.getLength()); //scroll to bottom
	}
	
	public void userMessage(String username, String message, Color userColor) {
		HTMLDocument d = (HTMLDocument) msgArea.getDocument();
		try {
			d.insertBeforeEnd(d.getElement("body"), "<div><b>"+username+":</b> "+message+"</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		msgArea.setCaretPosition(d.getLength()); //scroll to bottom
	}
	
	public void setGameID(int gid) {
		this.gameID = gid;
	}
	
	public void addNelodListener(ActionListener l) { //FOR TESTING ONLY, I SWEAR!!!
		sendButton.addActionListener(l);
	}
	
	public String getMsg() { //FOR TESTING ONLY, I SWEAR!!!
		return msgField.getText();
	}
}