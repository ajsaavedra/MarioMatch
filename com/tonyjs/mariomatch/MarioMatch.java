package com.tonyjs.mariomatch;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.swing.*;

@SuppressWarnings("serial")
public class MarioMatch extends JFrame
{
	private static final String GAME_TITLE = "Mario Match!";
	private static final int GAME_HEIGHT = 800;
	private static final int GAME_WIDTH = 740;
	
	private JLabel playerScore, timerL;
	private JButton newGameB, quitGameB;
	private JTextField messageTF;
	private BoardGame boardGame;
	private Deck deckOfCards;
	private JPanel[][] panels;
	private int selected, totalScore;
	private static int TIMES_PLAYED = 0;
	private Timer timer;
	private static boolean GAME_START, GAME_WON, GAME_OVER = false;
	private boolean disableThirdPanel;
	private static Clip THEME;
	private Color backgroundColor;

	private Container container;
	private int timeLimit = 60;

	public MarioMatch()
	{
		
		super(GAME_TITLE);
		styleFrame();
		
		playerScore = createJLabel("Total score: " + totalScore, 100, 20, 40, 10);
		timerL = createJLabel("Time: " + timeLimit , 120, 20, 630, 10);

		newGameB = createButton("New Game", 100, 20, 40, 700);
		newGameB.addActionListener(e -> startNewGame());
		container.add(newGameB);
		
		quitGameB = createButton("Quit Game", 100, 20, 180, 700);
		quitGameB.addActionListener(e -> quitGame());
		container.add(quitGameB);

		messageTF = new JTextField();
		messageTF.setSize(240, 20);
		messageTF.setLocation(320, 700);
		messageTF.setEditable(false);
		container.add(messageTF);

		instantiateGame();

		setVisible(true);
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private JLabel createJLabel(String text, int width, int height ,int x, int y)
	{
		JLabel label = new JLabel(text);
		label.setSize(width, height);
		label.setLocation(x, y);
		container.add(label);
		return label;
	}

	private void styleFrame() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		container = getContentPane();
		backgroundColor = ColorModel.selectRandomBackgroundColor();
		container.setBackground(backgroundColor);
		container.setLayout(null);
		setLocationRelativeTo(null);
	}

	public JButton createButton(String theLabel, int theWidth, int theHeight, int theLocationX, int theLocationY)
	{
		JButton toReturn = new JButton(theLabel);
		toReturn.setSize(theWidth, theHeight);
		toReturn.setLocation(theLocationX, theLocationY);
		return toReturn;
	}

	public JLabel getLabel(String theTitle, int theWidth, int theHeight, int theLocationX, int theLocationY, Color theColor)
	{
		JLabel toReturn = new JLabel(theTitle);
		toReturn.setSize(theWidth, theHeight);
		toReturn.setLocation(theLocationX, theLocationY);
		toReturn.setForeground(theColor);
		return toReturn;
	}

	public void instantiateGame()
	{
		deckOfCards = new Deck();
		deckOfCards.shuffle();
		boardGame = new BoardGame();
		panels = boardGame.getBoardGame();
		generateBoardGame();
		instantiateGameCountdown();
		selected = 0;
		totalScore = 0;
	}

	public void startNewGame()
	{
		if (TIMES_PLAYED++ > 0) {dispose(); main(null);}
		instantiateGame();
		GameSound.setSongFileToPlay("oneUp");
		THEME = GameSound.startThemeSong();
		newGameB.setEnabled(false);
		playerScore.setText("Total score: 0");
		GAME_START = true;
		GAME_OVER = false;
		GAME_WON = false;
		disableThirdPanel = false;
		messageTF.setText("");
	}

	public void quitGame()
	{
		System.exit(0);
	}

	public void instantiateGameCountdown()
	{
		timer = new Timer(1000, new ActionListener() {
			private int count = 59;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (count <= 0 && !GAME_OVER && !GAME_WON) {
					GAME_OVER = true;
					GAME_START = false;
					timerL.setText("Time's up!");
					((Timer)e.getSource()).stop();
					count = 59;
				} else if (GAME_START && !GAME_WON && !GAME_OVER) {
					timerL.setText("Time: " + Integer.toString(count));
					count--;
				} else if (GAME_OVER || GAME_WON) {
					count = 59;
					timerL.setText("Time: 60");
					return;
				}
				getGameState();
			}
		});

		timer.start();
	}

	public void getGameState()
	{
		if (totalScore == 10) {
			GAME_WON = true;
		}

		if (!GAME_WON && GAME_OVER) {
			messageTF.setText("Game Over. Play again?");
			GameSound.setSongFileToPlay("gameOver");
			newGameB.setEnabled(true);
		} else if (GAME_WON) {
			messageTF.setText("A perfect game! Play again?");
			THEME.stop();
			GameSound.setSongFileToPlay("powerUp");
			newGameB.setEnabled(true);
		}
	}

	public void generateBoardGame()
	{
		int yPos = 40;
		int pos = 0;
		int[] positions = new int[2];
		CardWithImage[] cardsChosen = new CardWithImage[2];
		for (int i = 0; i < boardGame.getMaxCardsNumber() / 5; i++) {
			if (i > 0) {yPos += 165;} int xPos = 40;
			for (int j = 0; j < boardGame.getMaxCardsNumber() / 4; j++) {
				CardWithImage newCard = deckOfCards.addOne(false);
				boardGame.addCard(newCard);
				panels[i][j].setLocation(xPos, yPos);
				panels[i][j].setBackground(backgroundColor);
				xPos += 140;
				int tempPos = pos++;
				panels[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked (MouseEvent e) {		
						if (GAME_OVER || GAME_WON || disableThirdPanel) {
							return;
						}
						
						if (GAME_START && selected == 0 && !newCard.isFaceUp() && !disableThirdPanel) {
							newCard.setFaceUp(true);
							cardsChosen[0] = newCard;
							positions[0] = tempPos;
							boardGame.flipCardOver(cardsChosen[0], positions[0]);
							selected++;
						} else if (GAME_START && selected == 1 && !newCard.isFaceUp() && newCard != cardsChosen[0] && !disableThirdPanel) {
							newCard.setFaceUp(true);
							disableThirdPanel = true;
							positions[1] = tempPos;
							cardsChosen[1] = newCard;
							boardGame.flipCardOver(cardsChosen[1], positions[1]);
							
							if (cardsChosen[0].getCharacter() == cardsChosen[1].getCharacter() &&
									cardsChosen[0].getColor() == cardsChosen[1].getColor()) {
								playerScore.setText("Total score: " + ++totalScore);
								GameSound.setSongFileToPlay("coin");
							} else {
								int delayTime = 700;
								Timer myTimer = new Timer(delayTime, new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										cardsChosen[1].setFaceUp(false);
										cardsChosen[0].setFaceUp(false);
										boardGame.flipCardOver(cardsChosen[0], positions[0]);
										boardGame.flipCardOver(cardsChosen[1], positions[1]);
									}
								});
								myTimer.setRepeats(false);
								myTimer.start();
							}
							
							int delayTime = 700;
							Timer myTimer = new Timer(delayTime, new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									selected = 0;
									disableThirdPanel = false;
								}
							});
							myTimer.setRepeats(false);
							myTimer.start();
						}
					}
				}); 
				container.add(panels[i][j]);
			}
		}
	}

	public static void main(String[] args)
	{
		MarioMatch myAppF = new MarioMatch();
	}
}
