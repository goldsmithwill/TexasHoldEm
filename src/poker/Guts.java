package poker;

import java.util.ArrayList;
import java.util.Scanner;

public class Guts {
	public Guts() {

	}

	public void compareHands(ArrayList<Player> playerListInput) {
		ArrayList<Player> playerList = playerListInput;
		for (Player player : playerList) {
			if (player.getFolded()) {
				player.foldHand();
			}
		}
		for (int i = 0; i < playerList.size(); i++) {
			System.out.println("\nPlayer " + i + ":\n");
			for (int m = 0; m < playerList.get(i).getValueArray().size(); m++) {
				System.out.println(playerList.get(i).getValueArray().get(m));
			}
		}
		for (int n = 0; n < playerList.size() - 1; n++) {
			for (int j = n + 1; j < playerList.size(); j++) {
				if (playerList.get(n).getRanking() < playerList.get(j)
						.getRanking()) {
					playerList.add(playerList.get(n));
					playerList.add(playerList.get(j));
					playerList.set(n, playerList.get(playerList.size() - 1));
					playerList.set(j, playerList.get(playerList.size() - 2));
					for (int q = 0; q < 2; q++) {
						playerList.remove(playerList.size() - 1);
					}
				} else if (playerList.get(n).getRanking() == playerList.get(j)
						.getRanking()) {
					for (int k = 0; k < 5; k++) {
						if (playerList.get(n).getValueArray().get(k) < playerList
								.get(j).getValueArray().get(k)) {
							playerList.add(playerList.get(n));
							playerList.add(playerList.get(j));
							playerList.set(n,
									playerList.get(playerList.size() - 1));
							playerList.set(j,
									playerList.get(playerList.size() - 2));
							for (int m3 = 0; m3 < 2; m3++) {
								playerList.remove(playerList.size() - 1);
							}
							break;
						}

					}
				}
			}
		}
	}

	public int printRound(ArrayList<Card> handInput, int roundInput,
			ArrayList<Card> boardInput, int dealerInput, Player pInput,
			Scanner scanInput, int callAmountInput, int potMoneyInput,
			ArrayList<Player> playerListInput) {
		int dealer = dealerInput;
		Player p = pInput;
		Scanner scan = scanInput;
		int callAmount = 0;
		int potMoney = potMoneyInput;
		int round = roundInput;
		ArrayList<Card> hand = handInput;
		ArrayList<Card> board = boardInput;
		ArrayList<Player> playerList = playerListInput;
		System.out.println("\nHere is your hand: \n");
		for (int i = 0; i < 2; i++) {
			System.out.println(p.getOrigHand().get(i).getNumber() + " of "
					+ p.getOrigHand().get(i).getSuit());
		}
		for (int i = 0; i < 2; i++) {
			System.out.println(playerList.get(1).getOrigHand().get(i).getNumber() + " of "
					+ playerList.get(1).getOrigHand().get(i).getSuit());
		}
		System.out.println("\nHere is the board: \n");
		for (int j = 0; j < round + 3; j++) {
			System.out.println(board.get(j).getNumber() + " of "
					+ board.get(j).getSuit());
		}

		int[] potOddsArray = null;
		while (true) {
			if (!p.getFolded()) {
				potOddsArray = Player.humanAction(dealer, p, scan, callAmount,
						potMoney);
				callAmount = potOddsArray[0];
				potMoney = potOddsArray[1];
			}
			if (callAmount == -1) {
				break;
			}

			if (!p.getFolded()) {
				p = playerList.get(1);
				potOddsArray = Player.computerAction(p, potMoney, callAmount,
						round, board, playerList);
				callAmount = potOddsArray[0];
				potMoney = potOddsArray[1];
			}
			if (callAmount == -1) {
				break;
			}
		}

		return potOddsArray[1];

	}
}