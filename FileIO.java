import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * FileIO class for reading team and player data from a file.
 * This class creates Team and Player objects based on the input file.
 * will create Forward, Midfielder, Defender
 *
 * @author 34842489
 * @version 8.0
 */
public class FileIO {
    public Team readTeamFromFile(String fileName) {
        Team team = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine(); // Read the first line (team name)
            if (line != null) {
                team = new Team(line); // Initialize the team with the team name
            }

            while ((line = br.readLine()) != null) {
                String[] playerData = line.split(",");
                if (playerData.length == 3) {
                    String playerName = playerData[0];
                    String position = playerData[1];
                    int goals = Integer.parseInt(playerData[2]);

                    Player player;
                    if (position.equals("Forward")) {
                        player = new Forward(playerName, goals, team);
                    } else if (position.equals("Midfielder")) {
                        player = new Midfielder(playerName, goals, team);
                    } else if (position.equals("Defender")) {
                        player = new Defender(playerName, goals, team);
                    } else {
                        player = new Player(playerName, position, goals, team);  // Generic Player
                    }
                    team.addPlayer(player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return team;
    }

    public void writeTeamToFile(Team team) {
        // Implement this method to write team data to a file

        try {
            String fileName = Objects.equals(team.getTeamName(), "Team A") ? "teamAUpdated.txt": "teamBUpdated.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(team.getTeamName());
            writer.newLine(); // next line
            for (Player player : team.getPlayers()) {
                writer.write(player.getName() + "," + player.getPosition() + "," + player.getGoals());
                writer.newLine(); // next line
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}


