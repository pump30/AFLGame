/*
 * The Randomizer class is responsible for generating random events in the AFL game.
 * It provides utility methods for selecting random active players from any team,
 * determining probabilities for actions, and handling random events such as injuries and reports.
 * 
 * Main functionalities include:
 * - Selecting a random active player based on their position (or any position if none is specified).
 * - Generating probabilities for player actions (such as passing, scoring, or turnovers).
 * - Handling random injury events where a player might become injured.
 * - Handling random report events where a player might be reported without affecting game flow.
 *
 * @ author 34842489
 * @ version 9.0
 */

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Randomizer {
    private Random rand;
    private Team teamA;
    private Team teamB;
    private List<Player> availablePlayers = new ArrayList<>();

    //default constructor
    public Randomizer() {
        this.rand = new Random();
    }

    //non-default constructor
    public Randomizer(Team teamA, Team teamB) {
        if (teamA == null || teamB == null) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        this.rand = new Random();
        this.teamA = teamA;
        this.teamB = teamB;
        availablePlayers.addAll(teamA.getActivePlayers());
        availablePlayers.addAll(teamB.getActivePlayers());
    }


    /**
     * Returns a random active player of a specific position.
     * If position is "None", it returns a random active player of any position.
     *
     * @param position The position of the player to be returned (e.g., "Midfielder", "Forward", "Defender").
     * @return A random active player matching the given position.
     */
    public Player getRandomActivePlayer(String position) {
        availablePlayers = new ArrayList<>();

        // Add active players from both teams that match the position or any position if "None"
        for (Player player : teamA.getActivePlayers()) {
            if (player.isActive() && (player.getPosition().equals(position) || position.equals("None"))) {
                availablePlayers.add(player);
            }
        }

        for (Player player : teamB.getActivePlayers()) {
            if (player.isActive() && (player.getPosition().equals(position) || position.equals("None"))) {
                availablePlayers.add(player);
            }
        }

        // Return a random player if available players exist
        if (!availablePlayers.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(availablePlayers.size());
            return availablePlayers.get(randomIndex);
        } else {
            System.out.println("No active players available for the given position: " + position);
            return null;
        }
    }

    public double generateActionProbability() {
        return rand.nextDouble() * 100;
    }

    /**
     * Get a random active player from both teams based on a specified position.
     *
     * @param teamA    Team A's active players.
     * @param teamB    Team B's active players.
     * @param position The position to filter the players by.
     * @return A random active player from the specified position.
     */

    public Player getRandomPlayer(Team teamA, Team teamB, String position) {
        List<Player> availablePlayers = new ArrayList<>();

        // Assuming Team class has a method getActivePlayers()
        availablePlayers.addAll(teamA.getActivePlayers());
        availablePlayers.addAll(teamB.getActivePlayers());

        // Filter players based on the position
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : availablePlayers) {
            if (player.getPosition().equals(position) && player.isActive() && !player.isInjured()) {
                filteredPlayers.add(player);
            }
        }

        if (filteredPlayers.isEmpty()) {
            return null; // No available players in that position
        }

        return filteredPlayers.get(rand.nextInt(filteredPlayers.size())); // Return a random player from filtered list
    }


    public Player handleInjuryEvent(Player currentPlayer) {
        Random random = new Random();
        List<Player> allActivePlayers = new ArrayList<>();

        // Gather all active players from both teams
        allActivePlayers.addAll(currentPlayer.getTeam().getActivePlayers());
        allActivePlayers.addAll(currentPlayer.getTeam().getOpponentTeam().getActivePlayers());

        // Filter out the injured players
        List<Player> uninjuredPlayers = new ArrayList<>();
        for (Player player : allActivePlayers) {
            if (!player.isInjured() && !player.getPosition().equals("Reserve")) {
                uninjuredPlayers.add(player);
            }
        }

        // 5% chance of injuring a player
        if (random.nextInt(100) < 5 && !uninjuredPlayers.isEmpty()) {
            // Select a random player to be injured
            Player injuredPlayer = uninjuredPlayers.get(random.nextInt(uninjuredPlayers.size()));
            injuredPlayer.setInjured(true);
            injuredPlayer.setActive(false);
            System.out.println(injuredPlayer.printName() + " is injured!");

            Player replacePlayer = injuredPlayer.getTeam().getReserve();
            if(replacePlayer == null) {
                System.out.println("No available substitute for " + injuredPlayer.getName() + " in position " + injuredPlayer.getPosition() + ".");
            } else {
                replacePlayer.setPosition(injuredPlayer.getPosition());
            }

            // If the injured player is the current ball holder, get a teammate in the same position
            if (injuredPlayer.equals(currentPlayer)) {
                Player newBallHolder = getRandomActiveTeammate(currentPlayer, currentPlayer.getPosition());
                System.out.println("Found a replacement player: " + newBallHolder.getName() + " who is playing the " + newBallHolder.getPosition() + " position" );
                if (newBallHolder != null) {
                    System.out.println(newBallHolder.getName() + " (Position: " + newBallHolder.getPosition() + ", Team: " + newBallHolder.getTeam().getTeamName() + ") takes the ball as a replacement.");
                    return newBallHolder;  // Return the new ball holder
                } else {
                    System.out.println("No available substitute for " + currentPlayer.getName() + " in position " + currentPlayer.getPosition() + ".");
                    return currentPlayer;  // If no replacement, the same player holds the ball
                }
            }
        }

        return currentPlayer;  // If the current player is not injured, return them as the ball holder
    }


    /*
     *
     * @ return Palyer a random active teammate(in the court) that fits the target position
     */
    public Player getRandomActiveTeammate(Player currentPlayer, String targetPosition) {
        List<Player> filteredPlayers = new ArrayList<>();

        for (Player player : currentPlayer.getTeam().getPlayers()) {
            if (player.isActive() && !player.isInjured() && player.getPosition().equals(targetPosition) && player != currentPlayer) {
                filteredPlayers.add(player);
            }
        }

        if (filteredPlayers.isEmpty()) {
            System.out.println("No available teammates in position " + targetPosition + " for " + currentPlayer.getName() + " to pass to.");
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(filteredPlayers.size());

        Player ballHolder = filteredPlayers.get(randomIndex);

        return ballHolder;
    }

    public Player getRandomActiveOpponent(Player currentPlayer, String targetPosition) {
        List<Player> filteredPlayers = new ArrayList<>();
        List<Player> availableOpponentPlayers = currentPlayer.getTeam().getOpponentTeam().getPlayers();

        for (Player player : availableOpponentPlayers) {
            if (player.isActive() && !player.isInjured() && player.getPosition().equals(targetPosition)) {
                filteredPlayers.add(player);
            }
        }

        if (filteredPlayers.isEmpty()) {
            System.out.println("No available opponents in position " + targetPosition + ".");
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(filteredPlayers.size());
        return filteredPlayers.get(randomIndex);
    }

    // Helper method to randomly choose a player from a list
    private Player getRandomPlayerFromList(List<Player> players) {
        if (players.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(players.size());
        return players.get(randomIndex);
    }

    public void handleReportedEvent(Team teamA, Team teamB) {
        double reportProbability = generateActionProbability(); // Use random probability generator
        if (reportProbability < 1.0) { // 1% chance of reporting a player
            // Select a random active player from either teamA or teamB
            Team team = generateActionProbability() < 50.0 ? teamA : teamB; // 50-50 chance between teams
            Player reportedPlayer = getAnyRandomActivePlayer();
            reportedPlayer.reportPlayer(); // Mark the player as reported
            System.out.println(reportedPlayer.toString() + " has been reported by the umpire!");
        }
    }

    // Method to get any random active player regardless of position
    public Player getAnyRandomActivePlayer() {
        List<Player> activePlayers = new ArrayList<>();

        // Add active players from both teams
        activePlayers.addAll(teamA.getActivePlayers());
        activePlayers.addAll(teamB.getActivePlayers());

        // Return null if there are no active players
        if (activePlayers.isEmpty()) {
            return null;
        }

        // Return a random player from the list of active players
        Random random = new Random();
        return activePlayers.get(random.nextInt(activePlayers.size()));
    }


}

