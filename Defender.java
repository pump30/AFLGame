/**
 * Represents a Defender in the AFL game.
 * Defenders primarily focus on defending against opponents and handling turnovers.
 * 
 * @author Junxiang Zeng
 * @version 10.0
 */
public class Defender extends Player 
{
    // Non-default constructor
    public Defender(String name, int goals, Team team)
    {
        super(name, "Defender", goals, team);
    }

    /*
    * Method for handling a Defender's event.
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
     public Player startEvent(Player currentPlayer) 
     {
        if (currentPlayer.isStar()) 
        {
            return handleStarDefenderEvent(currentPlayer);
        }
         else 
        {
            return handleDefenderEvent(currentPlayer);
        }
    }

    /*
    * Method for handling a normal Defender's event
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleDefenderEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play

        if (action < 80) 
        {
            currentPlayer = handlePass(currentPlayer, "Midfielder");
        } 
        else 
        {
            currentPlayer = handleTurnover(currentPlayer, "Forward");
        }
        return currentPlayer;
    }

    /*
    * Method for handling a star Defender's event
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleStarDefenderEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play
        if (action < 95) 
        {
            currentPlayer = handlePass(currentPlayer, "Midfielder");
        } 
        else 
        {
            currentPlayer = handleTurnover(currentPlayer, "Defender");
        }
        return currentPlayer;
    }
  
}
