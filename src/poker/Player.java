package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {
	public Player() {

	}

	private static boolean isForAI = false;

	public static boolean isForAI() {
		return isForAI;
	}

	public static void setForAI(boolean isForAI) {
		Player.isForAI = isForAI;
	}

	private boolean isComputer = false;
	private boolean folded = false;
	private static boolean[] booleanRankArray = null;

	private ArrayList<Integer> valueArray = new ArrayList<Integer>();
	private ArrayList<Card> hand = new ArrayList<Card>();

	private String playerAction;

	private double drawOdds;

	private int money = 1000;
	private int ranking = 0;
	private static int[] drawOddsArray = new int[4];
	private int[] numberArray = new int[5];
	private ArrayList<Card> origHand;

	public void setOrigHand(ArrayList<Card> origHand) {
		this.origHand = origHand;
	}

	public ArrayList<Card> getOrigHand() {
		return origHand;
	}

	public boolean isComputer() {
		return isComputer;
	}

	public void setIsComputer(boolean isComputer) {
		this.isComputer = isComputer;
	}

	public boolean getFolded() {
		return folded;
	}

	public void setFolded(boolean folded) {
		this.folded = folded;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public ArrayList<Integer> getValueArray() {
		return valueArray;
	}

	public void addValueArray(Integer i) {
		valueArray.add(i);
	}

	public void setValueArray(int index, Integer i) {
		valueArray.set(index, i);
	}

	public void printAction() {
		if (isComputer) {
			System.out.println("\nThe other player has " + playerAction + ".");

		} else {
			System.out.println("\nYou have " + playerAction + ".");
		}
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public ArrayList<Card> getHand() {
		return this.hand;
	}

	public String getPlayerAction() {
		return playerAction;
	}

	public void setPlayerAction(String playerAction) {
		this.playerAction = playerAction;
	}

	public double getDrawOdds() {
		return drawOdds;
	}

	public void setDrawOdds(double drawOdds) {
		this.drawOdds = drawOdds;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void foldHand() {
		setRanking(0);
		for (int i = 0; i < 5; i++) {
			getValueArray().set(i, 0);
		}
	}

	public static int[] humanAction(int dealerInput, Player pInput,
			Scanner scanInput, int callAmountInput, int potMoneyInput) {
		int dealer = dealerInput;
		Player p = pInput;
		Scanner scan = scanInput;
		int callAmount = callAmountInput;
		int potMoney = potMoneyInput;

		System.out.print("\nWould you like to ");
		if (callAmount == 0) {
			System.out.print("bet, check");
		} else {
			System.out.print("call, raise,");
		}
		System.out.println(" or fold?");
		scan = new Scanner(System.in);

		String playerAction;
		while (true) {
			playerAction = scan.nextLine();
			if (callAmount == 0) {
				if (!playerAction.equals("bet")
						&& !playerAction.equals("check")
						&& !playerAction.equals("fold")) {
					System.out.println("\"" + playerAction
							+ "\" is not an option. Please try again:");
				} else {
					break;
				}
			} else {
				if (!playerAction.equals("call")
						&& !playerAction.equals("raise")
						&& !playerAction.equals("fold")) {
					System.out.println("\"" + playerAction
							+ "\" is not an option. Please try again:");
				} else {
					break;
				}
			}
		}
		p.setPlayerAction(playerAction);
		return p.doAction(callAmount, potMoney);
	}

	public int[] doAction(int callAmountInput, int potMoneyInput) {
		int callAmount = callAmountInput;
		int potMoney = potMoneyInput;
		if (playerAction.equals("bet")) {
			if (isComputer()) {
				callAmount = callAmountInput;
			} else {
				System.out
						.println("How much would you like to bet?");
				Scanner scan = new Scanner(System.in);
				callAmount = scan.nextInt();
			}
			playerAction = "bet " + callAmount + " dollars";
			money -= callAmount;
			potMoney += callAmount;
			printAction();

		} else if (playerAction.equals("raise")) {
			if (isComputer) {
				calcComputerRaiseAmount(callAmount, potMoney);
			} else {
				System.out
						.println("How much would you like to raise? The minimum raise amount is "
								+ callAmount + ".");
				Scanner scan = new Scanner(System.in);
				int raiseAmount = scan.nextInt();
				money -= raiseAmount + callAmount;
				potMoney += raiseAmount + callAmount;
				callAmount = raiseAmount;
			}
			playerAction = "raised " + callAmount + " dollars";
			printAction();
		} else if (playerAction.equals("fold")) {
			playerAction = "folded";
			printAction();
			setFolded(true);

		} else if (playerAction.equals("check")) {
			playerAction = "checked";
			printAction();
		} else if (playerAction.equals("call")) {
			playerAction = "called";
			printAction();
			setMoney(money - callAmount);
			potMoney += callAmount;
		} else {
			System.out.println("That is not an option.\n");
		}

		if ((!playerAction.contains("raise") && !playerAction.contains("bet"))
				&& isComputer) {
			callAmount = -1;
		} else if ((playerAction.contains("call") || playerAction
				.contains("fold")) && !isComputer) {
			callAmount = -1;
		}

		int[] potOddsArray = { callAmount, potMoney };
		return potOddsArray;
	}

	public static int[] computerAction(Player pInput, int potMoneyInput,
			int callAmountInput, int roundInput, ArrayList<Card> boardInput,
			ArrayList<Player> playerListInput) {
		ArrayList<Card> board = boardInput;
		Player p = pInput;
		int potMoney = potMoneyInput;
		int callAmount = callAmountInput;
		int round = roundInput;
		ArrayList<Player> playerList = playerListInput;

		double potOdds = calcPotOdds(potMoney, callAmount);
		double drawOdds = calcDrawOdds(p, round, board);
		p.drawOdds = drawOdds;
		int betAmount = (int) ((drawOdds * potMoney) / (1 - drawOdds ));
		if (drawOdds >= potOdds && betAmount >= callAmount) {
			if (callAmount == 0) {
				callAmount = betAmount;
				p.playerAction = "bet";
			} else {
				if (betAmount >= callAmount) {
					p.playerAction = "raise";
				} else {
					p.playerAction = "call";
				}
			}
		} else if (callAmount == 0) {
			p.playerAction = "check";
		} else {
			p.playerAction = "fold";
		}
		return p.doAction(callAmount, potMoney);
	}

	public static double calcDrawOdds(Player pInput, int roundInput,
			ArrayList<Card> boardInput) {
		isForAI = true;

		ArrayList<Card> board = boardInput;
		Player p = pInput;
		int round = roundInput;
		for (int c = 0; c < round + 3; c++) {
			p.hand.add(board.get(c));
		}

		p.rankHand();

		int NOK = drawOddsArray[0];
		int NOG = drawOddsArray[1];
		int NIR = drawOddsArray[2];
		int NOS = drawOddsArray[3];

		int numOfOuts = 0;

		boolean isStraightFlush = booleanRankArray[0];
		boolean isFourOAK = booleanRankArray[1];
		boolean isFlush = booleanRankArray[2];
		boolean isStraight = booleanRankArray[3];
		boolean isThreeOAK = booleanRankArray[4];
		boolean isTwoPair = booleanRankArray[5];
		boolean isOnePair = booleanRankArray[6];

		// OAKS

		if (isOnePair && isThreeOAK) {
			numOfOuts += 3;
		} else if (isTwoPair) {
			numOfOuts += 4;
		} else if (isOnePair) {
			numOfOuts += 2;
		} else if (isThreeOAK) {
			numOfOuts++;
		}

		// straights
		for (int m = 0; m < round + 5; m++) {
			for (int n = m + 1; n < round + 5; n++) {
				if (p.hand.get(n).getNumber() - p.hand.get(m).getNumber() <= 4) {
					if (n - m == 2) {
						numOfOuts += 8;
					}
				}
			}
		}

		// flushes
		if (NOS < 5 && NOS >= round + 3) {
			numOfOuts += (13 - NOS);
		}
		for (int a = 0; a < round + 5; a++) {
			p.hand.remove(0);
		}
		for (int b = 0; b < 2; b++) {
			p.hand.add(p.origHand.get(b));
		}

		double test = (double) (52 - (round + 5));
		System.out.println(test);
		
		double test2 = numOfOuts/test;
		System.out.println(test2);
		
		return (double) numOfOuts / (double) (52 - (round + 5));
	}

	public static double calcPotOdds(int potMoneyInput, int callAmountInput) {
		int potMoney = potMoneyInput;
		int callAmount = callAmountInput;
		return ((double) callAmount / (double) (potMoney + callAmount));
	}

	// rankHand
	public void rankHand() {
		int numberArrayEnding = 0;
		int suitNOK = 0;
		int numberInARow = 0;
		int suitInARow = 0;

		boolean hasBeenRun = false;
		boolean isStraightFlush = false;
		boolean isFourOAK = false;
		boolean isFlush = false;
		boolean isStraight = false;
		boolean isThreeOAK = false;
		boolean isTwoPair = false;
		boolean isOnePair = false;

		int[] straightArray = new int[2];

		sort(this);

		rankOAKs();

		numberArrayEnding = numberArray[4];

		if (hand.get(0).getNumber() == 1) {
			hand.remove(0);
		}

		straightArray = rankStraights(straightArray);
		numberInARow = straightArray[0];
		suitInARow = straightArray[1];

		if (isForAI) {
			drawOddsArray[2] = numberInARow;
		}

		suitNOK = rankFlushes(suitNOK);

		if (valueArray.size() > 5) {
			while (valueArray.size() > 5) {
				valueArray.remove(0);
			}
		}

		numberArray[numberArrayEnding] = 0;

		for (int j = 0; j < 3; j++) {
			if (numberInARow >= 4 && suitInARow >= 4) {
				isStraightFlush = true;
			} else if (numberArray[j] == 3) {
				isFourOAK = true;
			} else if (suitNOK >= 4) {
				isFlush = true;
			} else if (numberInARow >= 4) {
				isStraight = true;
			} else if (numberArray[j] >= 1 && isThreeOAK == true) {
				isOnePair = true;
			} else if (numberArray[j] == 2) {
				isThreeOAK = true;
			}
			// if there are two three OAKs, it sets one to a pair and the
			// other
			// to a three OAK for the full house Ex: 333 444 5
			else if (numberArray[j] == 1 && isOnePair == true) {
				isTwoPair = true;
			} else if (numberArray[j] == 1) {
				isOnePair = true;
			}
		}

		boolean[] booleanRankArray = { isStraightFlush, isFourOAK, isFlush,
				isStraight, isThreeOAK, isTwoPair, isOnePair };
		Player.booleanRankArray = booleanRankArray;

		if (!isForAI) {
			if (isStraightFlush) {
				System.out.println("straight flush\n");
				ranking = 9;
			} else if (isFourOAK) {
				System.out.println("4 of a kind\n");
				ranking = 8;
			} else if (isThreeOAK && isOnePair) {
				System.out.println("full house\n");
				ranking = 7;
			} else if (isFlush) {
				System.out.println("flush\n");
				ranking = 6;
			} else if (isStraight) {
				System.out.println("straight\n");
				ranking = 5;
			} else if (isThreeOAK) {
				System.out.println("3 of a kind\n");
				ranking = 4;
			} else if (isTwoPair) {
				System.out.println("2 pair\n");
				ranking = 3;
			} else if (isOnePair) {
				System.out.println("1 pair\n");
				ranking = 2;
			} else {
				System.out.println("high card\n");
				ranking = 1;
			}
		}
	}

	// sort
	public ArrayList<Card> sort(Player pInput) {
		Player p = pInput;
		Card temp = new Card();
		int tempIndex;
		for (int i = 0; i < hand.size(); i++) {
			temp = hand.get(i);
			tempIndex = i;
			for (int j = i; j < hand.size(); j++) {
				if (temp.getNumber() > hand.get(j).getNumber()) {
					temp = hand.get(j);
					tempIndex = j;
				}
			}
			hand.set(tempIndex, hand.get(i));
			hand.set(i, temp);
		}
		return hand;
	}

	public int rankFlushes(int suitNOKInput) {
		int suitNOK = suitNOKInput;
		int suitNOKTemp = 0;
		String flushSuit = null;

		// flush
		for (int r = 0; r < hand.size(); r++) {
			for (int s = r + 1; s < hand.size(); s++) {
				if (hand.get(r).getSuit().equals(hand.get(s).getSuit())) {
					flushSuit = hand.get(r).getSuit();
					suitNOK++;
				}
			}
			if (suitNOK < 4) {
				if (suitNOK > suitNOKTemp) {
					suitNOKTemp = suitNOK;
				}
				suitNOK = 0;
			}
		}
		if (isForAI) {
			suitNOK = suitNOKTemp + 1;
			drawOddsArray[3] = suitNOK;
		} else {
			flushValueArray(suitNOK, flushSuit);
		}
		if (isForAI) {
			valueArray = new ArrayList<Integer>();
		}
		return suitNOK;
	}

	public void flushValueArray(int suitNOKInput, String flushSuitInput) {
		int suitNOK = suitNOKInput;
		String flushSuit = flushSuitInput;
		boolean hasBeenRun = false;
		if (suitNOK >= 4) {
			for (int f = 0; f < 5; f++) {
				valueArray.remove(0);
			}
			for (int i = 6; i > 0; i--) {
				if (valueArray.size() < 5) {
					if ((hand.get(0).getNumber() == 1 && hand.get(0).getSuit()
							.equals(flushSuit))
							&& hasBeenRun == false) {
						hand.set(0, new Card(hand.get(0).getSuit(), 14));
						i++;
						hasBeenRun = true;
					} else if (hand.get(i).getSuit().equals(flushSuit)) {
						valueArray.add(new Integer(hand.get(i).getNumber()));
					}
				}
			}
		}

	}

	public int[] rankStraights(int[] straightArrayInput) {

		int[] straightArray = straightArrayInput;
		ArrayList<Integer> straightNumbers = new ArrayList<Integer>();

		if (isForAI) {
			valueArray = new ArrayList<Integer>();
		}

		// straight or straight flush
		if (hand.get(hand.size() - 1).getNumber() == 14) {
			hand.add(0, new Card(hand.get(0).getSuit(), 1));
		}
		// checks how many numbers and suits are in a row
		for (int m2 = 0; m2 < hand.size(); m2++) {
			for (int q = m2 + 1; q < hand.size(); q++) {
				if (hand.get(m2).getNumber() == hand.get(q).getNumber() - 1) {
					if (straightArray[0] == 0) {
						straightNumbers.add(hand.get(m2).getNumber());
					}
					straightNumbers.add(hand.get(q).getNumber());
					straightArray[0]++;
				} else if (straightArray[0] < 4
						&& hand.get(m2).getNumber() != hand.get(q).getNumber()) {
					for (int f = 0; f < straightArray[0]; f++) {
						straightNumbers.remove(0);
					}
					straightArray[0] = 0;
				}
				if (hand.get(m2).getSuit().equals(hand.get(q).getSuit())) {
					straightArray[1]++;
				} else if (straightArray[1] < 4) {
					straightArray[1] = 0;
				}
				if (m2 == hand.size() - 1) {
					for (int f = 0; f < 5; f++) {
						valueArray.remove(0);
					}
					for (int i = straightNumbers.size() - 1; i >= 0; i--) {
						valueArray.add(0, straightNumbers.get(i));
					}
				}
				break;
			}
			if (valueArray.size() == 5 && straightArray[0] >= 4) {
				break;
			}
		}
		if (hand.get(0).getNumber() == 1) {
			hand.remove(0);
		}
		return straightArray;
	}

	public void rankOAKs() {
		boolean hasBeenRun = false;
		int numberOfAKind = 0;
		int numberOfGroups = 0;

		for (int m = 0; m < hand.size(); m++) {
			for (int n = m + 1; n < hand.size(); n++) {
				if (hand.get(m).getNumber() == hand.get(n).getNumber()) {

					numberOfAKind++;
					if (numberOfAKind == 1) {
						valueArray.add(new Integer(hand.get(m).getNumber()));
					}
					valueArray.add(new Integer(hand.get(n).getNumber()));
					if (n == 6) {
						numberArray[numberOfGroups] = numberOfAKind;
					}
					break;
				}

				if (numberOfAKind >= 1) {
					numberArray[numberOfGroups] = numberOfAKind;
					numberOfGroups++;
					numberOfAKind = 0;
				}

			}
		}
		for (int z = 0; z < 4; z++) {
			if (numberArray[z] == 0) {
				numberArray[z] = numberArray[0];
				numberArray[4] = z;
				break;
			}
		}

		if (isForAI) {
			drawOddsArray[0] = numberOfAKind;
			drawOddsArray[1] = numberOfGroups;
		} else {
			this.OAKValueArray();
		}
		if (isForAI) {
			valueArray = new ArrayList<Integer>();
		}
	}

	public void OAKValueArray() {

		boolean hasBeenRun = false;

		// four of a kind, three of a kind, full house
		for (int c = 0; c < numberArray.length - 1; c++) {
			for (int i2 = c + 1; i2 < numberArray.length; i2++) {
				if (valueArray.size() > 5 && numberArray[c] <= numberArray[i2]) {
					if (numberArray[c] == numberArray[c + 1]
							&& numberArray[c] == 2) {
						valueArray.remove(0);
					} else if (numberArray[c] == numberArray[i2]
							&& (numberArray[c] == 1 && !hasBeenRun)) {
						hasBeenRun = true;
						for (int n2 = 0; n2 < 2; n2++) {
							if (numberArray[0] == 2) {
								valueArray.remove(3);
							} else {
								valueArray.remove(0);
							}
						}
					}
				}
			}
		}

		// three pair
		if (valueArray.size() > 5) {
			if (numberArray[0] == numberArray[1]
					&& numberArray[1] == numberArray[2]) {
				for (int m2 = 0; m2 < 2; m2++) {
					valueArray.remove(0);
				}
			}
		}
		hasBeenRun = false;
		int b = 7;
		while (valueArray.size() < 5) {
			b--;

			if (!valueArray.contains(new Integer(hand.get(b).getNumber()))) {
				if (hand.get(0).getNumber() == 1 && !hasBeenRun) {
					hand.set(0, new Card(hand.get(0).getSuit(), 14));
					hasBeenRun = true;
					b++;
				} else {
					valueArray.add((new Integer(hand.get(b).getNumber())));
				}
			}
		}
	}

	public int calcComputerRaiseAmount(int callAmountInput, int potMoneyInput) {
		int callAmount = callAmountInput;
		int potMoney = potMoneyInput;
		int raiseAmount = (int) ((drawOdds * potMoney) / (1 - drawOdds));
		money -= raiseAmount + callAmount;
		potMoney += raiseAmount + callAmount;
		callAmount = raiseAmount;
		return callAmount;
	}
}
