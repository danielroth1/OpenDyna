package gui.profiles;

import java.awt.Color;
import java.awt.datatransfer.StringSelection;
import java.util.Comparator;

public class Profile implements Comparable{
	
	private String name;
	private Color color;
	private Hotkeys hotkeys;
	private boolean isKi;
	
	public Profile(String name, Color color) {
		this.name = name;
		this.color = color;
		hotkeys = new Hotkeys();
		isKi = false;
	}
	
	public Profile(String name, Color color, String up, String down, String left, String right, String bomb) {
		this.name = name;
		this.color = color;
		hotkeys = new Hotkeys(up, down, left, right, bomb);
		isKi = false;
	}
	
	public Profile(String name, Color color, Hotkeys hotkeys){
		this.name = name;
		this.color = color;
		this.hotkeys = hotkeys;
		isKi = false;
	}
	
	public Profile(String name, Color color, boolean isKi){
		this(name, color);
		this.isKi = isKi;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	

//	@Override
//	public int compare(Object arg0, Object arg1) {
//		return arg0.toString().compareTo(arg1.toString());
//	}

	public Hotkeys getHotkeys() {
		return hotkeys;
	}

	public void setHotkeys(Hotkeys hotkeys) {
		this.hotkeys = hotkeys;
	}

	@Override
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	}
	
	public boolean isNameSet(){
		return name != null;
	}

	public boolean isKi() {
		return isKi;
	}

	public void setKi(boolean isKi) {
		this.isKi = isKi;
	}

	
	
	
	
	
}
