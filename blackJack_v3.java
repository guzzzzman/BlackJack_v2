//BlackJack game
//Jose Guzman
//February 2, 2024

package blackJackLearning;
import java.util.Random;
import java.util.Scanner;

public class BlackJack_v3 {
    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    private static final int[] DECK = new int[52];
    private static int currentCardIndex = 0;
    private static int playerMoney = 100; // Starting money for the player

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeDeck();
        shuffleDeck();
        
        while (playerMoney > 0) { // Continue playing as long as the player has money
            int betAmount = getBetAmount(scanner); // Get the bet amount from the player
            if (betAmount > playerMoney) {
                System.out.println("You don't have enough money to bet that amount.");
                break;
            }
            playerMoney -= betAmount; // Deduct the bet amount from player's money
            System.out.println("You bet " + betAmount + " dollars.");

            int playerTotal = dealInitialPlayerCards();
            int dealerTotal = dealInitialDealerCards();
            playerTotal = playerTurn(scanner, playerTotal);
            if (playerTotal > 21) {
                System.out.println("You busted! Dealer wins.");
            } else {
                dealerTotal = dealerTurn(dealerTotal);
                determineWinner(playerTotal, dealerTotal, betAmount);
            }

            System.out.println("Your remaining money: " + playerMoney);
            System.out.println("Do you want to continue playing? (yes/no)");
            String continuePlaying = scanner.nextLine().toLowerCase();
            if (!continuePlaying.equals("yes")) {
                break;
            }
        }
        System.out.println("Game over!");
        scanner.close();
    }

    private static void initializeDeck() {
        for (int i = 0; i < DECK.length; i++) {
            DECK[i] = i;
        }
    }

    private static void shuffleDeck() {
        Random random = new Random();
        for (int i = 0; i < DECK.length; i++) {
            int index = random.nextInt(DECK.length);
            int temp = DECK[i];
            DECK[i] = DECK[index];
            DECK[index] = temp;
        }
    }

    private static int dealInitialPlayerCards() {
        int card1 = dealCard();
        int card2 = dealCard();
        System.out.println("Your cards: " + RANKS[card1] + " of " + SUITS[card1 / 13] + " and " + RANKS[card2] + " of " + SUITS[card2 / 13]);
        return cardValue(card1) + cardValue(card2);
    }

    private static int dealInitialDealerCards() {
        int card1 = dealCard();
        System.out.println("Dealer's card: " + RANKS[card1] + " of " + SUITS[card1 / 13]);
        return cardValue(card1);
    }

    private static int playerTurn(Scanner scanner, int playerTotal) {
        while (true) {
            System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
            String action = scanner.nextLine().toLowerCase();
            if (action.equals("hit")) {
                int newCard = dealCard();
                playerTotal += cardValue(newCard);
                System.out.println("You drew a " + RANKS[newCard] + " of " + SUITS[newCard / 13]);
                if (playerTotal > 21) {
                    break;
                }
            } else if (action.equals("stand")) {
                break;
            } else {
                System.out.println("Invalid action. Please type 'hit' or 'stand'.");
            }
        }
        return playerTotal;
    }

    private static int dealerTurn(int dealerTotal) {
        while (dealerTotal < 17) {
            int newCard = dealCard();
            dealerTotal += cardValue(newCard);
        }
        System.out.println("Dealer's total is " + dealerTotal);
        return dealerTotal;
    }

    private static void determineWinner(int playerTotal, int dealerTotal, int betAmount) {
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            playerMoney += 2 * betAmount; // Player wins, get double the bet amount
            System.out.println("You win!");
        } else if (dealerTotal == playerTotal) {
            playerMoney += betAmount; // Player gets back the bet amount
            System.out.println("It's a tie!");
        } else {
            System.out.println("Dealer wins!");
        }
    }

    private static int dealCard() {
        return DECK[currentCardIndex++] % 13;
    }

    private static int cardValue(int card) {
        return card < 9 ? card + 2 : 10;
    }

    private static int getBetAmount(Scanner scanner) {
        System.out.println("Your current money: " + playerMoney);
        System.out.println("Enter your bet amount:");
        while (true) {
            try {
                int betAmount = Integer.parseInt(scanner.nextLine());
                if (betAmount <= 0) {
                    System.out.println("Please enter a positive bet amount.");
                } else {
                    return betAmount;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}