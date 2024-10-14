
/**
 * Represents a Team in the AFL Game.
 * Each team has a name and a list of active players.
 * 
 * @author 34842489
 * @version 7.0
 */
 import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Team {
    private String teamName;
    private List<Player> players;
    private Team opponentTeam;
    private Randomizer randomizer;

     // Default constructor
    public Team() 
    {
        this.teamName = "Unknown";
        this.players = new ArrayList<>(); // Initialize the players field
        this.randomizer = new Randomizer();
    }

    // non-default constructor
    public Team(String teamName)
    {
        this.teamName = teamName;
        this.players = new ArrayList<>(); // Initialize the players field
    }

    public void setRandomizer(Randomizer randomizer)
    {
        this.randomizer = randomizer;
    }

    public Randomizer getRandomizer()
    {
        return this.randomizer;
    }

    // Setter for opponent team
    public void setOpponentTeam(Team opponentTeam) 
    {
        this.opponentTeam = opponentTeam;
    }

    // Getter for opponent team
    public Team getOpponentTeam() 
    {
        return this.opponentTeam;
    }

    /*
    * Adds a player to the team
    * @ param player The player to be added to the team.
    */
    public void addPlayer(Player player) 
    {
        this.players.add(player);
    }

    /**
     * Returns the list of all players in the team.
     * @return List of players.
     */
    public List<Player> getPlayers() 
    {
        return this.players;
    }

    /**
     * Calculates the total score for the team based on goals and behinds.
     * @return The total score for the team.
     */
    public int getTeamScore() 
    {
        int totalScore = 0;
        for (Player player : players) {
            totalScore += player.getPlayerScore();
        }
        return totalScore;
    }

    public int getTotalGoals() {
        int totalGoals = 0;
        for (Player player : players) {
            totalGoals += player.getGoals();
        }
        return totalGoals;
    }

    public int getTotalBehinds() {
        int totalBehinds = 0;
        for (Player player : players) {
            totalBehinds += player.getBehinds();
        }
        return totalBehinds;
    }

    public Player getGreatestNumberOfKicksPlayer()
    {
        Player playerWithMostKicks = null;
        int maxKicks = 0;
        for (Player player : players) {
            if (player.getKicks() > maxKicks) {
                maxKicks = player.getKicks();
                playerWithMostKicks = player;
            }
        }
        return playerWithMostKicks;
    }

    /**
     * Retrieves the team name.
     * @return The team name.
     */
    public String getTeamName() 
    {
        return this.teamName;
    }


    // This method will return a list of active players
    public List<Player> getActivePlayers() 
    {
        if (this.players == null) {
            return Collections.emptyList();  // Return an empty list if no players
        }
        return players.stream()
                    .filter(Player::isActive)
                    .collect(Collectors.toList());
    }


    public void setStarPlayer(Team team, int starPlayerNumber) {
        // Ensure that the number of star players does not exceed the active players
        if (starPlayerNumber < 0 || starPlayerNumber > team.getPlayers().size()) {
            System.out.println("Invalid number of star players. Setting to maximum available players.");
            starPlayerNumber = Math.min(starPlayerNumber, team.getPlayers().size());
        }

        Random random = new Random();
        List<Player> availablePlayers = new ArrayList<>(team.getPlayers()); // Copy of the players to randomize

        // Randomly assign the star players
        for (int i = 0; i < starPlayerNumber; i++) {
            int randomIndex = random.nextInt(availablePlayers.size());
            Player starPlayer = availablePlayers.remove(randomIndex); // Avoid selecting the same player twice
            starPlayer.setStar(true); // Mark the player as a star
            System.out.println(starPlayer.getName() + " is now a star player for " + team.getTeamName());
        }
    }
    /**
     * Retrieves a random active player from the team.
     * Used for gameplay purposes.
     * @param position The position of the player (Forward, Midfielder, Defender).
     * @return A player object matching the given position.
     */
     /*
    public Player getRandomPlayer(String position)
    {
        List<Player> availabPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                availablePlayers.add(player);
            }
        }
        if (!availablePlayers.isEmpty()) {
            return availablePlayers.get((int) (Math.random() * availablePlayers.size()));
        }
        return null;
    }*/

    /**
     * Prints all the players and their details in the team.
     */
    public void printTeamDetails()
    {
        System.out.println(this.teamName);
        System.out.println("Total goals: " + this.getTotalGoals());
        System.out.println("Total behinds: " + this.getTotalBehinds());
        System.out.println("Total score: " + this.getTeamScore());
        System.out.println("Who has the greatest number of kicks?");
        Player playerWithMostKicks = this.getGreatestNumberOfKicksPlayer();
        System.out.println("  " + playerWithMostKicks.getName() + " kicked the ball " + playerWithMostKicks.getKicks() + " times");
        System.out.println("How kick the most goals?");
        for(Player player : players) {
            if(player.getKicks() != 0) {
                System.out.println("  " + player.getName() + " kicked " + player.getKicks() + " goals");
            }
        }
        System.out.println("Individual statistics:");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s%n", "Name", "Kicks", "Goals", "Behinds", "Pass Percent", "Position");
        for(Player player : players) {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-1s%n", player.getName(), player.getKicks(), player.getGoals(), player.getBehinds(), player.getPassPercent(), player.getPosition(), player.printPlayerCondition());
            //System.out.print(player.printPlayerCondition());
        }
    }



    public void printInjuredPlayers()
    {
        System.out.println("Team: "+teamName);
        for(Player player : players) {
            if(player.isInjured()) {
                System.out.println("  " + player.getName() + "(" + player.getPosition() + ")");
            }
        }
    }

    public void printReportedPlayers()
    {
        System.out.println("Team: "+teamName);
        for(Player player : players) {
            if(player.getReported() > 0) {
                System.out.println("  " + player.getName() + "(" + player.getPosition() + ")");
            }
        }
    }
}

