package com.tonyjs.mariomatch;

import javax.swing.*;

public class BoardGame
{
	private static final int maxCardsToMatch = 20;
	protected JLabel myLabels[] = new JLabel[20];
	protected CardWithImage myCards[], placeholder[];
	protected int myNumOfCards, myNumOfIcons;
	
	private JPanel[][] panelsArray = new JPanel[maxCardsToMatch/5][maxCardsToMatch/4];
	
	public BoardGame()
	{
		myNumOfCards = 0;
		myNumOfIcons = 0;
		placeholder = new CardWithImage[maxCardsToMatch];
		myCards = new CardWithImage[maxCardsToMatch];
		for (int i = 0; i < maxCardsToMatch / 5; i++) {
			for (int j = 0; j < maxCardsToMatch / 4; j++) {
				JPanel cardPanel = new JPanel();
				cardPanel.setSize(98, 150);
				myLabels[myNumOfCards] = new JLabel();
				cardPanel.add(myLabels[myNumOfCards++]);
				panelsArray[i][j] = cardPanel;
			}
		}
	}
	
	public int getMaxCardsNumber()
	{
		return maxCardsToMatch;
	}
	
	public JPanel[][] getBoardGame()
	{
		return panelsArray;
	}
	
	public void addCard(CardWithImage theCard)
	{
		if (myNumOfIcons > maxCardsToMatch - 1) {
			System.out.println("Error, cannot accept more cards!");
		} else {
			myLabels[myNumOfIcons].setIcon(theCard.getImage());
			myLabels[myNumOfIcons].repaint();
			myCards[myNumOfIcons++] = theCard;
		}
	}
	
	public void flipCardOver(CardWithImage theCard, int position)
	{
		myLabels[position].setIcon(theCard.getImage());
		myLabels[position].repaint();
		myCards[position] = theCard;
	}
}
