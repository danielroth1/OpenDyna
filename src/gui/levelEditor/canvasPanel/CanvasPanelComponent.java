package gui.levelEditor.canvasPanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;

import server.Square;
import server.components.ArrayPosition;
import gui.GamePanel;

public class CanvasPanelComponent extends GamePanel{
	
	private ArrayPosition size;
	private List<Square> buttons = new LinkedList<Square>();
	
	public CanvasPanelComponent(ArrayPosition size) {
		super(false, true);
		enableTranslationAndScaling = false;
		drawBackgroundImage = false;
//		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		SCALE = 0;
		this.size = size;
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				Point p = e.getPoint();
				
			}
		});
	}
	
	public void iniateButtons(){
		buttons = new LinkedList<Square>();
		for (int r=0; r<size.getRow(); r++){
			for (int c=0; c<size.getColumn(); c++){
				buttons.add(new Square(new ArrayPosition(r, c), null));
			}
		}
	}

}
