package gui.lobby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import main.GameControl;
import main.GuiControl;

public class ChatPanel extends JPanel{
	
	private GuiControl guiControl;
	private GameControl gameControl;
	private JTextPane read;
	private JScrollPane readScrollPane;
	private JTextField write;
	
	
	public ChatPanel(GuiControl guiControl, GameControl gameControl) {
		this.gameControl = gameControl;
		this.guiControl = guiControl;
		read = new JTextPane();
		write = new JTextField();
		readScrollPane = new JScrollPane(read);
//		readScrollPane.add(read);
		read.setEditable(false);
		read.setMinimumSize(new Dimension(100, 100));
		write.setMaximumSize(new Dimension(1000, 30));
		
		write.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					String s = write.getText();
					ChatPanel.this.gameControl.writeChatInLobby(s);
					write.setText("");
					System.out.println(s);
					
				}
			}
			public void keyTyped(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					write.setText("");
				}
			}
			public void keyReleased(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					write.setText("");
				}
			}
		});
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(readScrollPane);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(write);
		
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}
	
	public void showText(String s){
		try {
			Document doc = read.getStyledDocument();
			doc.insertString(doc.getLength(), s + "\n" , null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resetText(){
		read.setText("");
		write.setText("");
	}
	
	
}
