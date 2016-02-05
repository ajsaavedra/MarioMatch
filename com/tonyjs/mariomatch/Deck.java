package com.tonyjs.mariomatch;

public class Deck
{
	public static final int DECK_SIZE = 20;
	private static final String[] COLOR = {"Green", "Red"};
	private static final String[] CHARACTER = {"Mushroom", "Shell", "Shyguy", "Luma", "Flower"};
	private int myNextCardPosition;
	private CardWithImage[] myCards;

	public Deck()
	{
		myCards = new CardWithImage[DECK_SIZE];
		myNextCardPosition = 0;
		
//		for (int position = 0; position < DECK_SIZE; position++) {
//			myCards[position] = new CardWithImage(COLOR[position / 5], CHARACTER[position % 5]);
//		}
		
		int position = 0;
		for (String character : CHARACTER) {
			for (String color : COLOR) {
				myCards[position++] = new CardWithImage(color, character);
				myCards[position++] = new CardWithImage(color, character);
			}
		}
	}

	public void setNextCard(int theNextCardPosition)
	{
		myNextCardPosition = theNextCardPosition;
	}

	private void swap(int i1, int i2)
	{
		CardWithImage hold = myCards[i1];
		myCards[i1] = myCards[i2];
		myCards[i2] = hold;
	}

	public void shuffle()
	{
		for (int position = 0; position < myCards.length; position++) {
			int swapPosition = (int) (Math.random() * DECK_SIZE);
			swap(position, swapPosition);
		}
	}

	public CardWithImage addOne(boolean theFaceUp)
	{
		if (myNextCardPosition > -1 && myNextCardPosition < myCards.length) {
			myCards[myNextCardPosition].setFaceUp( theFaceUp );
			return myCards[ myNextCardPosition++ ];
		}

		System.out.println("Error: index of next card in deck is out of range.");
		return null;
	}

	public int cardsLeft()
	{
		return DECK_SIZE - myNextCardPosition;
	}
}
