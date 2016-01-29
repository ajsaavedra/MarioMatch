package forMarioMatch;

public class Card
{
	private String cardColor;
	private String cardCharacter;
	private boolean myFaceUp; 

	public Card(String theColor, String theCharacter)
	{
		this.cardColor = theColor;
		this.cardCharacter = theCharacter;
		myFaceUp = false;
	}

	public String getCharacter()
	{
		return cardCharacter;
	}

	public String getColor()
	{
		return cardColor;
	}

	public boolean isFaceUp()
	{
		return myFaceUp;
	}

	public void setFaceUp(boolean theFaceUp)
	{
		myFaceUp = theFaceUp;
	}
	
}
