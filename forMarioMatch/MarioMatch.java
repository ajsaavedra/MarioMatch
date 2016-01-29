package forMarioMatch;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class MarioMatch extends JFrame
{
	private JLabel playerScore, timerL;
	private JButton newGameB, quitGameB;
	private JTextField messageTF;
	private BoardGame myBoardGame;
	private Deck myDeckOfCards;
	private JPanel[][] panels;
	private int selected, totalScore;
	private static int timesPlayed = 0;
	private Timer timer;
	private static boolean gameStart, gameIsWon, gameOver;
	private boolean disableThirdPanel;
	private static Clip theme;
	private Color backgroundColor;

	private Container myCP;

	public MarioMatch()
	{
		super("Mario Match!");
		setSize(740, 800);
		myCP = getContentPane();
		backgroundColor = ColorModel.selectRandomBackgroundColor();
		myCP.setBackground(backgroundColor);
		myCP.setLayout(null);
		setLocationRelativeTo(null);

		playerScore = new JLabel("Total score: 0");
		playerScore.setSize(100, 20);
		playerScore.setLocation(40,10);
		myCP.add(playerScore);

		timerL = new JLabel("Time: 60");
		timerL.setSize(120, 20);
		timerL.setLocation(630, 10);
		myCP.add(timerL);

		newGameB = createButton("New Game", 100, 20, 40, 700);
		newGameB.addActionListener(e -> startNewGame());
		myCP.add(newGameB);

		gameStart = false;

		quitGameB = createButton("Quit Game", 100, 20, 180, 700);
		quitGameB.addActionListener(e -> quitGame());
		myCP.add(quitGameB);

		messageTF = new JTextField();
		messageTF.setSize(240, 20);
		messageTF.setLocation(320, 700);
		messageTF.setEditable(false);
		myCP.add(messageTF);

		instantiateGame();

		setVisible(true);
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
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
		myDeckOfCards = new Deck();
		myDeckOfCards.shuffle();
		myBoardGame = new BoardGame();
		panels = myBoardGame.getBoardGame();
		generateBoardGame();
		instantiateGameCountdown();
		selected = 0;
		totalScore = 0;
	}

	public void startNewGame()
	{
		if (timesPlayed++ > 0) {dispose(); main(null);}
		instantiateGame();
		GameSound.setSongFileToPlay("oneUp");
		theme = GameSound.startThemeSong();
		newGameB.setEnabled(false);
		playerScore.setText("Total score: 0");
		gameStart = true;
		gameOver = false;
		gameIsWon = false;
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
				if (count <= 0 && !gameOver && !gameIsWon) {
					gameOver = true;
					gameStart = false;
					timerL.setText("Time's up!");
					((Timer)e.getSource()).stop();
					count = 59;
				} else if (gameStart && !gameIsWon && !gameOver) {
					timerL.setText("Time: " + Integer.toString(count));
					count--;
				} else if (gameOver || gameIsWon) {
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
			gameIsWon = true;
		}

		if (!gameIsWon && gameOver) {
			messageTF.setText("Game Over. Play again?");
			GameSound.setSongFileToPlay("gameOver");
			newGameB.setEnabled(true);
		} else if (gameIsWon) {
			messageTF.setText("A perfect game! Play again?");
			theme.stop();
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
		for (int i = 0; i < myBoardGame.getMaxCardsNumber() / 5; i++) {
			if (i > 0) {yPos += 165;} int xPos = 40;
			for (int j = 0; j < myBoardGame.getMaxCardsNumber() / 4; j++) {
				CardWithImage newCard = myDeckOfCards.addOne(false);
				myBoardGame.addCard(newCard);
				panels[i][j].setLocation(xPos, yPos);
				panels[i][j].setBackground(backgroundColor);
				xPos += 140;
				int tempPos = pos++;
				panels[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked (MouseEvent e) {		
						if (gameOver || gameIsWon || disableThirdPanel) {
							return;
						}
						
						if (gameStart && selected == 0 && !newCard.isFaceUp() && !disableThirdPanel) {
							newCard.setFaceUp(true);
							cardsChosen[0] = newCard;
							positions[0] = tempPos;
							myBoardGame.flipCardOver(cardsChosen[0], positions[0]);
							selected++;
						} else if (gameStart && selected == 1 && !newCard.isFaceUp() && newCard != cardsChosen[0] && !disableThirdPanel) {
							newCard.setFaceUp(true);
							disableThirdPanel = true;
							positions[1] = tempPos;
							cardsChosen[1] = newCard;
							myBoardGame.flipCardOver(cardsChosen[1], positions[1]);
							
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
										myBoardGame.flipCardOver(cardsChosen[0], positions[0]);
										myBoardGame.flipCardOver(cardsChosen[1], positions[1]);
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
				myCP.add(panels[i][j]);
			}
		}
	}

	public static void main(String[] args)
	{
		MarioMatch myAppF = new MarioMatch();
	}
}
