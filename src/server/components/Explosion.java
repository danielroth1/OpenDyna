package server.components;

import maps.Texture;
import server.Square;

public class Explosion extends Square{
	
	public static Texture STANDARD_TEXTURE = Texture.EXPLOSION;
	int duration;
	
	public Explosion(ArrayPosition a, int duration) {
		super(a, STANDARD_TEXTURE);
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
