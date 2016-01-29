package forMarioMatch;

import javax.swing.*;

public class CardWithImage extends Card
{
	final static String sep = java.io.File.separator;
	static final ImageIcon cardBackImage = new ImageIcon("cards" + sep + "back.png");
	private ImageIcon myImage;
	
	public CardWithImage(String theColor, String theCharacter)
	{
		super(theColor, theCharacter);
		myImage = new ImageIcon("cards" + sep + theColor + theCharacter + ".png");
	}
	
	public ImageIcon getImage()
	{
		return isFaceUp() ? myImage : cardBackImage;
	}
}
