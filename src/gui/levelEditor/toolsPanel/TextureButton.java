package gui.levelEditor.toolsPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import maps.Texture;

public class TextureButton extends JToggleButton{
	
	private Texture texture;
	private Image backgroundImage;
	
	public TextureButton(Texture texture, Image backgroundImage) {
		super();
		this.texture = texture;
		this.backgroundImage = backgroundImage;
		
		Dimension size = new Dimension(60, 60);
		setBackground(Color.WHITE);
		
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTerrain(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (backgroundImage != null){
			g.drawImage(backgroundImage, 5, 5, this);
		}
		
	}
	
	
}
