package gui.profiles;

public class Hotkeys {
	
	public static int UP = 0;
	public static int DOWN = 1;
	public static int LEFT = 2;
	public static int RIGHT = 3;
	public static int BOMB = 4;
	
	private String up;
	private String down;
	private String left;
	private String right;
	private String bomb;
	private boolean isSet;
	
	public Hotkeys(String up, String down, String left, String right, String bomb) {
		this.up = up.toUpperCase();
		this.left = left.toUpperCase();
		this.down = down.toUpperCase();
		this.right = right.toUpperCase();
		this.bomb = bomb.toUpperCase();
		isSet = true;
	}
	
	public Hotkeys(){
		up = "";
		down = "";
		left = "";
		right = "";
		bomb = "";
		isSet = false;
	}
	
	public String getHotkey(int hotkey){
		if (hotkey == UP)
			return up;
		if (hotkey == DOWN)
			return down;
		if (hotkey == LEFT)
			return left;
		if (hotkey == RIGHT)
			return right;
		if (hotkey == BOMB)
			return bomb;
		else
			return null;
	}

	public String getUp() {
		return up;
	}

	public String getDown() {
		return down;
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setRight(String right) {
		this.right = right;
	}
	
	public String getBomb() {
		return bomb;
	}

	public void setBomb(String bomb) {
		this.bomb = bomb;
	}

	
	public boolean isSet(){
		return isSet;
	}
	
	public void setSet(boolean isSet){
		this.isSet = isSet;
	}
	
}
