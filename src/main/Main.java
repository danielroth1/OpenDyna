package main;

public class Main {
	
	private GuiControl guiControl;
	private GameControl gameControl;
	
	public Main() {
		gameControl = new GameControl();
		guiControl = new GuiControl(gameControl);
		
		gameControl.setComponents(guiControl);
	}
	
	
	public static void main(String[] args){
		new Main();
	}
}
