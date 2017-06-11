package poker;

public class Card {
	String suit;

	int number;

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Card() {

	}

	public Card(String suit, int number) {
		this.suit = suit;
		this.number = number;
	}
}
