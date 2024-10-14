import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles the simulation of the Australian Football game.
 * This class manages the game flow, quarter play, and results.
 *
 * @author Your Name
 * @version 10.0
 */
public class AFLGame {
    //fields
    private Team teamA;
    private Team teamB;
    private Randomizer randomizer;
    private Player currentPlayer;
    private boolean startFromCentre;
    private Forward forward;
    private Midfielder midfielder;
    private Defender defender;


    //default constructor
    public AFLGame() {
        // Default initialization of fields
        this.teamA = null;
        this.teamB = null;
        this.randomizer = new Randomizer(); // Initialize the randomizer

        this.currentPlayer = null; // No player currently holding the ball

        // Initialize player roles (you can instantiate here or when necessary)
        this.forward = null;
        this.midfielder = null;
        this.defender = null;
    }

    //non-default constructor
    public AFLGame(Team teamA, Team teamB) {
        if (teamA == null || teamB == null) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        this.randomizer = new Randomizer(teamA, teamB);
        teamA.setRandomizer(this.randomizer);
        teamB.setRandomizer(this.randomizer);
        this.teamA = teamA;
        this.teamB = teamB;
        this.startFromCentre = true;
        // Initialize midfielder, forward, defender
        this.currentPlayer = null;
        this.forward = null;
        this.midfielder = null;
        this.defender = null;
    }

    public void setStartFromCentre(boolean value) {
        this.startFromCentre = value;
    }

    public boolean isStartFromCentre() {
        return startFromCentre;
    }

    // Call this method after a goal is scored
    public void handleGoalScored() {
        setStartFromCentre(true);  // Set flag to restart from the center
    }

    /*
     * method for running the whole game
     * 1. let the user choose [0-8] star players
     * 2. start the game for 4 quarters
     */
    public void startGame() {

        System.out.println("Welcome to the Australian Rules Football Simulation");
        System.out.println("====================================================");

        int starPlayerNumberTeamA = getStarPlayerNumber("team A");
        int starPlayerNumberTeamB = getStarPlayerNumber("team B");

        setStarPlayer(teamA, starPlayerNumberTeamA);
        setStarPlayer(teamB, starPlayerNumberTeamB);

        System.out.println("");

        //call the method to create star player
        for (int quarter = 1; quarter <= 4; quarter++) {
            System.out.println("*** Quarter #" + quarter + " ***");
            playQuarter();
            System.out.println();
            System.out.println("Quarter has finished");
            System.out.println();
            printQuarterSummary(quarter);
            System.out.println();
        }
    }

    /**
    * Starts the game or resets the play from the center circle.
    * 
    * Selects a random active midfielder from either team to gain possession of the ball.
    * Prints the player's name and position in the center circle.
    * 
    * @return The player who gains possession of the ball from the center circle.
    */
    public Player startFromCentre() {
        // Reset to center, select a random midfielder to start the game
        Player currentPlayer = randomizer.getRandomActivePlayer("Midfielder");

        System.out.println("Starting from the centre circle");
        System.out.println(currentPlayer.printName() + " got the ball in the centre circle.");

        return currentPlayer;
    }

    /**
    * Prompts the user to enter the number of star players for a given team.
    * The method repeatedly asks the user for input until a valid number between 0 and 8 (inclusive) is entered.
    * It handles invalid input by displaying appropriate error messages and prompting the user again.
    *
    * @param teamName The name of the team for which the number of star players is being set.
    * @return An integer representing the number of star players for the specified team.
    *         The number will be in the range of 0 to 8 (inclusive).
    * 
    * Example usage:
    *    int starPlayerNumber = getStarPlayerNumber("Team A");
    *    // Prompts the user: "Enter number of star players in Team A[0-8]"
    */
    public int getStarPlayerNumber(String teamName) {
        Scanner scanner = new Scanner(System.in);
        int number = -1;

        while (number < 0 || number > 8) {
            System.out.print("Enter number of star players in " + teamName + "[0-8]");


            if (scanner.hasNextInt()) {
                number = scanner.nextInt();

                if (number < 0 || number > 8) {
                    System.out.println("Error: number not in range [0-8]");
                }
            } else {

                System.out.println("Error: value is not a number");
                scanner.next();
            }
        }

        return number;
    }

    /**
    * Simulates one quarter of an Australian Rules Football game.
    * 
    * This method processes 80 game events for a single quarter. If the game starts from the center
    * (indicated by `startFromCentre`), a random midfielder is selected to begin the play. 
    * After each event, the `eventHandler` manages actions like scoring, passing, and turnovers.
    * 
    * If a goal is scored, the game restarts from the center circle.
    * 
    * Fields:
    * - `startFromCentre`: If true, the quarter begins from the center with a random midfielder.
    * - `currentPlayer`: The player currently holding the ball.
    * 
    * Example usage:
    *    playQuarter();  // Simulates 80 events in one quarter.
 */
    private void playQuarter() {
        this.startFromCentre = true;
        Player currentPlayer = null;
        // Loop for the Quarter (80 events per quarter)
        for (int i = 1; i <= 80; i++) {
            System.out.println("#" + i + ":");
            if (this.startFromCentre) {
                // Start the quarter from the center
                currentPlayer = startFromCentre();
                this.startFromCentre = false;
            }
            if (currentPlayer != null) {
                currentPlayer = eventHandler(currentPlayer);
            }
        }
    }

    /**
    * Handles the game event based on the current player's position.
    * 
    * Executes position-specific actions for forwards, midfielders, or defenders.
    * If no player holds the ball (e.g., after a score), it resets the game to start from the center.
    * Additionally, it checks for injuries and reports after each event.
    * 
    * @param currentPlayer The player currently holding the ball.
    * @return The player holding the ball after the event.
    */
    private Player eventHandler(Player currentPlayer) {
        switch (currentPlayer.getPosition()) {
            case "Forward":
                if (currentPlayer instanceof Forward) {
                    currentPlayer = ((Forward) currentPlayer).startEvent(currentPlayer);
                }
                break;
            case "Midfielder":
                if (currentPlayer instanceof Midfielder) {
                    currentPlayer = ((Midfielder) currentPlayer).startEvent(currentPlayer);
                }
                break;
            case "Defender":
                if (currentPlayer instanceof Defender) {
                    currentPlayer = ((Defender) currentPlayer).startEvent(currentPlayer);
                }
                break;
            default:
                System.out.println("Invalid player position.");
                break;
        }
        if (currentPlayer == null) {
            setStartFromCentre(true);
        } else {
            // Handle injuries and reporting
            currentPlayer = randomizer.handleInjuryEvent(currentPlayer);
            handleReportedEvent(teamA, teamB);  // Ensure handleReportedEvent exists
        }
        return currentPlayer;
    }


    private void printQuarterSummary(int quarter) {
        System.out.println("Team A has " + teamA.getTeamScore() + " points");
        System.out.println("Team B has " + teamB.getTeamScore() + " points");
    }


    public void printGameResult(Team teamA, Team teamB) {
        int teamAScore = teamA.getTeamScore();
        int teamBScore = teamB.getTeamScore();

        System.out.println();
        System.out.println("Game Result");
        System.out.println("===========");

        if (teamAScore > teamBScore) {
            System.out.println(teamA.getTeamName() + " has won the Game with " + teamAScore + " points.");
        } else if (teamBScore > teamAScore) {
            System.out.println(teamB.getTeamName() + " has won the Game with " + teamBScore + " points.");
        } else {
            System.out.println("The game is a draw with both teams scoring " + teamAScore + " points.");
        }

        System.out.println();

        teamA.printTeamDetails();

        System.out.println();
        teamB.printTeamDetails();
        System.out.println();
        System.out.println("Injured Players:");
        System.out.println("===========");
        teamA.printInjuredPlayers();
        teamB.printInjuredPlayers();
        System.out.println();
        System.out.println("Reported Players:");
        teamA.printReportedPlayers();
        teamB.printReportedPlayers();
    }


    // Method to set star players for a team
    public void setStarPlayer(Team team, int starPlayerNumber) {
        List<Player> players = team.getPlayers();
        Random random = new Random();

        // Ensure that the number of star players is valid
        if (starPlayerNumber < 0 || starPlayerNumber > players.size()) {
            System.out.println("Invalid number of star players.");
            return;
        }

        // Check if there are players in the team
        if (!players.isEmpty()) {
            // Randomly assign star status to the specified number of players
            for (int i = 0; i < starPlayerNumber; i++) {
                int randomIndex = random.nextInt(players.size());
                Player player = players.get(randomIndex);

                // Make sure the player is not already a star
                if (!player.isStar()) {
                    player.setStar(true);
                    
                } else {
                    i--; // Re-select if the player is already a star
                }
            }
        } else {
            System.out.println("No players in the team.");
        }
    }


    /**
     * Handles the event of randomly reporting a player for misconduct.
     * A reported player continues in the game, but the report is logged.
     *
     * @param teamA The first team in the game
     * @param teamB The second team in the game
     */
    private void handleReportedEvent(Team teamA, Team teamB) {
        // Use the randomizer to randomly select an active player from either team
        Player reportedPlayer = randomizer.getRandomActivePlayer("None");

        // Generate a random number between 0 and 100
        Random random = new Random();
        int reportChance = random.nextInt(101);

        // 5% chance of reporting the player
        if (reportChance < 5) {
            // Report the player by increasing the report count
            reportedPlayer.reportPlayer();

            // Print the reporting event
            System.out.println(reportedPlayer.getName() + " (Position: " + reportedPlayer.getPosition()
                    + ", " + reportedPlayer.getTeam().getTeamName() + ") has been reported by the umpire.");
        }
    }


    public static void main(String[] args) {
        // Create an instance of FileIO to read the team data from files
        FileIO fileIO = new FileIO();

        // Read team data from "teamA.txt" and "teamB.txt"
        Team teamA = fileIO.readTeamFromFile("teamA.txt");
        Team teamB = fileIO.readTeamFromFile("teamB.txt");

        // Check if both teams are properly initialized
        if (teamA == null || teamB == null) {
            System.out.println("Error: Could not load the teams. Exiting the game.");
            return; // Exit the game if teams are not loaded properly
        }

        teamA.setOpponentTeam(teamB);
        teamB.setOpponentTeam(teamA);

        // Create an AFLGame instance with the two teams
        AFLGame game = new AFLGame(teamA, teamB);

        // Start the game
        game.startGame();

        // Print the final results after all quarters have been played
        game.printGameResult(teamA, teamB);
        System.out.println();
        System.out.println("Writing output files");
        fileIO.writeTeamToFile(teamA);
        fileIO.writeTeamToFile(teamB);
        System.out.println("Goodbye");
    }
}

