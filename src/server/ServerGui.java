package server;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Client;

/**
 * a server gui
 * @author Daniel Roth
 */
public class ServerGui {
	
	private Server server;
	private JFrame frame;
	private JPanel panel;
	private JLabel clientsLabel;
	
	public ServerGui(Server server) {
		this.server = server;
		initiatePanel();
		
		frame = new JFrame("Server");
		frame.add(panel);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				frame.setVisible(false);
			}
		});
		frame.setSize(new Dimension(200, 200));
		frame.setLocation(10, 80);
		frame.setVisible(true);
	}
	
	private void initiatePanel(){
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		clientsLabel = new JLabel();
		panel.add(clientsLabel);
		updateLabel();
		
	}
	
	private void updateLabel(){
		String s = "<html><head></head><body>";
		int number = 0;
		
		for (Client c : server.getGameLogic().getClients()){
			s = s + "Client" + number + ": " + c.getName() + "<br>";
		}
		
		s = s + "</body></html>";
		
		clientsLabel.setText(s);
	}
	
	public void update(){
		updateLabel();
	}
	
	public void showGui(boolean show){
		frame.setVisible(show);
	}
	
}
