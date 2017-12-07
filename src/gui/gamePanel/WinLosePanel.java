package gui.gamePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.GameControl;
import main.GuiControl;

public class WinLosePanel extends JPanel{
	
	private GuiControl guiControl;
	private GameControl gameControl;
	
	private boolean won = false;
	private boolean draw = false;
	private Image backgroundImageWon = null;
	private Image backgroundImageLost = null;
	private JLabel nameLabel;
	private JButton returnGameBtn;
	private JButton backToMenueBtn;
	private Box contentBox;
	private String name = "";
	
	public WinLosePanel(GameControl gameControl, GuiControl guiControl) {
		this.guiControl = guiControl;
		this.gameControl = gameControl;
		
		nameLabel = new JLabel("");
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, (float)18));
		nameLabel.setForeground(Color.WHITE);
		
		returnGameBtn = new JButton("Return game");
		backToMenueBtn = new JButton("Back to menue");
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		returnGameBtn.setAlignmentX(CENTER_ALIGNMENT);
		backToMenueBtn.setAlignmentX(CENTER_ALIGNMENT);
		initiateListeners();
		
		//initiate panel
		setSize(GuiControl.SIZE);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		//intiate content box
		contentBox = new Box(BoxLayout.PAGE_AXIS);
		contentBox.setSize(GuiControl.WIN_LOOSE_PANEL_SIZE);
		contentBox.setOpaque(true);
		contentBox.setBackground(GuiControl.STANDARD_COLOR.darker());
		contentBox.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), 
						BorderFactory.createLoweredBevelBorder()), 
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		contentBox.setAlignmentX(CENTER_ALIGNMENT);
		contentBox.add(nameLabel);
		contentBox.add(Box.createRigidArea(new Dimension(0, 30)));
		contentBox.add(returnGameBtn);
		contentBox.add(Box.createRigidArea(new Dimension(0, 10)));
		contentBox.add(backToMenueBtn);
		
		
		//add content box
		add(Box.createVerticalGlue());
		add(contentBox);
		add(Box.createVerticalGlue());
		
		setOpaque(false);
		setVisible(false);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (won
				&& backgroundImageWon != null)
			g2.drawImage(backgroundImageWon, 0, 0, this);
		else if (!won
				&& backgroundImageLost != null)
			g2.drawImage(backgroundImageLost, 0, 0, this);
	}
	
	public void setBackgroundImageWon(Image backgroundImageWon){
		this.backgroundImageWon = backgroundImageWon;
	}
	
	public void setBackgroundImageLost(Image backgroundImageLost){
		this.backgroundImageLost = backgroundImageLost;
	}
	
	private void initiateListeners(){
		returnGameBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				gameControl.returnGameBtnPressed();
			}
		});
		backToMenueBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				gameControl.backToMenueBtnPressed();
			}
		});
		
	}
	
	public void setWon(boolean won, boolean draw, String name){
		this.won = won;
		this.name = name;
		this.draw = draw;
		if (!draw){
			nameLabel.setText(name + " has won!");
		}
		else{
			nameLabel.setText("DRAW!");
		}
	}
}
