package poker;

import java.util.ArrayList;

import java.util.Scanner;

public class Poker {

	public static void main(String[] args) {
		playGame();
	}

	public static void playGame() {
		Scanner scan = new Scanner(System.in);
		int dealer = 0;
		int callAmount = 0;
		int potMoney = 0;
		int numOfPlayers = 2;
		int potOdds = 0;
		int round = 0;
		Guts game = new Guts();
		Deck deck = new Deck();
		ArrayList<Player> playerList = new ArrayList<Player>();
		deck.shuffle();

		
		// initializing playerList
		for (int i = 0; i < numOfPlayers; i++) {
			playerList.add(new Player());

			playerList.get(i).setHand(deck.dealHand());
			playerList.get(i).setOrigHand(deck.dealHand());
			playerList.get(i).setMoney(
					playerList.get(i).getMoney() - 1);
			potMoney++;
		}
		
		
		playerList.get(0).setIsComputer(false);

		playerList.get(1).setIsComputer(true);

		Player p = playerList.get(0);

		ArrayList<Card> board = deck.dealBoard();
		
		// playerList.get(0).getOrigHand().get(0).setNumber(12);
		// playerList.get(0).getOrigHand().get(0).setSuit("spades");
		// playerList.get(0).getOrigHand().get(1).setNumber(7);
		// playerList.get(0).getOrigHand().get(1).setSuit("diamonds");
		// playerList.get(0).getHand().get(0).setNumber(12);
		// playerList.get(0).getHand().get(0).setSuit("spades");
		// playerList.get(0).getHand().get(1).setNumber(7);
		// playerList.get(0).getHand().get(1).setSuit("diamonds");
		// // playerList.get(2).getHand().get(0).setNumber(2);
		// // playerList.get(2).getHand().get(0).setSuit("spades");
		// // playerList.get(2).getHand().get(1).setNumber(14);
		// // playerList.get(2).getHand().get(1).setSuit("diamonds");
		// // playerList.get(3).getHand().get(0).setNumber(11);
		// // playerList.get(3).getHand().get(0).setSuit("spades");
		// // playerList.get(3).getHand().get(1).setNumber(14);
		// // playerList.get(3).getHand().get(1).setSuit("diamonds");
		//
		// board.get(0).setNumber(1);
		// board.get(0).setSuit("hearts");
		// board.get(1).setNumber(3);
		// board.get(1).setSuit("diamonds");
		// board.get(2).setNumber(5);
		// board.get(2).setSuit("clubs");
		// board.get(3).setNumber(10);
		// board.get(3).setSuit("diamonds");
		// board.get(4).setNumber(11);
		// board.get(4).setSuit("clubs");
		ArrayList<Card> hand = p.getHand();
		
		for (int q = 0; q < 3; q++) {
		if(!p.getFolded() && !playerList.get(1).getFolded()){
			potMoney = game.printRound(hand, q, board, dealer, p, scan,
					callAmount, potMoney, playerList);
		}	
		}

		if (Player.isForAI()) {
			Player.setForAI(false);
		}
		for (Player player : playerList) {
			player.getHand().addAll(board);
			player.sort(player);
			player.rankHand();
		}

		game.compareHands(playerList);
		System.out.println("\nThe winning five cards are: \n");
		for (int k = 0; k < playerList.get(0).getValueArray().size(); k++) {
			System.out.println(playerList.get(0).getValueArray().get(k));
		}
		System.out.println("\nAt the end, the pot was " + potMoney);
		scan.close();		
	}
}
