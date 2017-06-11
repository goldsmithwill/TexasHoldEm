package poker;

import java.util.ArrayList;

public class Deck {
	ArrayList<Card> deck = new ArrayList<Card>();

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}

	public Deck() {

	}

	public void print() {
		for (int i = 0; i < 14; i++) {
			System.out.println(this.getDeck().get(i).getSuit());
			System.out.println(this.getDeck().get(i).getNumber());
		}
	}

	public void shuffle() {
		Deck unShuffledDeck = new Deck();
		int m = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				unShuffledDeck.getDeck().add(new Card());
				switch (i) {
				case 0:
					unShuffledDeck.getDeck().get(m).setSuit("clubs");
					break;
				case 1:
					unShuffledDeck.getDeck().get(m).setSuit("diamonds");
					break;
				case 2:
					unShuffledDeck.getDeck().get(m).setSuit("hearts");
					break;
				case 3:
					unShuffledDeck.getDeck().get(m).setSuit("spades");
					break;
				default:
					break;
				}
				unShuffledDeck.getDeck().get(m).setNumber(j + 2);
				m++;
			}
		}
		int random;
		int k = 0;
		while (true) {
			random = (int) Math.round(Math.random() * 51);
			if (!this.getDeck().contains(unShuffledDeck.getDeck().get(random))) {
				this.getDeck().add(unShuffledDeck.getDeck().get(random));
				k++;
			}

			if (k == 52) {
				break;
			}
		}
	}

	public ArrayList<Card> dealBoard() {
		ArrayList<Card> board = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			board.add(deck.get(0));
			deck.remove(0);
		}
		return board;
	}

	public ArrayList<Card> dealHand() {
		ArrayList<Card> hand = new ArrayList<Card>();
		for (int i = 0; i < 2; i++) {
			hand.add(deck.get(0));
			deck.remove(0);
		}
		return hand;
	}
}
