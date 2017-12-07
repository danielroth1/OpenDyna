package gui.gamePanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;

import gui.GamePanel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import main.GameControl;
import main.GuiControl;

public class GamePanelBackground extends JPanel{
	
	private GamePanel gamePanel;
	private Image background;
	
	public GamePanelBackground(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		
		setLayout(new GridLayout(0, 1));
		add(gamePanel);
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		setSize(GuiControl.SIZE);
	}
	
	public void setBackground(Image background){
		this.background = background;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (background != null)
			g2.drawImage(background, 0, 0, this);
		
	}
}
