/**
 * Represents a Forward in the AFL game.
 * Forwards primarily focus on scoring goals.
 * 
 * @author 34842489
 * @version 8.0
 */
public class Forward extends Player 
{
    /* don't need a default constructor for the Forward class, 
    as the Forward class will always be initialized with specific
     player information (such as name and team) when players are read from the file.*/

    // Non-default constructor
    public Forward(String name, int goals, Team team)
    {
        super(name, "Forward", goals, team);
    }

    /*
    * Method for handling a Forward's event.
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player startEvent(Player currentPlayer) 
    {
        if (currentPlayer.isStar()) 
        {
            return handleStarForwardEvent(currentPlayer);
        } 
        else 
        {
            return handleForwardEvent(currentPlayer);
        }
    }

    /*
    * Method for handling a normal Forward's event
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleForwardEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
          // Reset this for regular play

        if (action < 30) 
        {
            currentPlayer = handleScore(currentPlayer, 6);
            return null;
        } 
        else if (action < 70) 
        {
            currentPlayer = handleScore(currentPlayer, 1);
        } 
        else if (action < 90) 
        {
            currentPlayer = handlePass(currentPlayer, "Forward");
        }
         else 
        {
            currentPlayer = handleTurnover(currentPlayer, "Defender");
        }
        return currentPlayer;
    }

    /*
    * Method for handling a star Forward's event
    * @param currentPlayer The player currently with the ball
    * @param //startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleStarForwardEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play
        if (action < 45) 
        {
            currentPlayer = handleScore(currentPlayer, 6);
            //startFromCentre = true;
        } 
        else if (action < 85) 
        {
            currentPlayer = handleScore(currentPlayer, 1);
        } 
        else if (action < 95) 
        {
            currentPlayer = handlePass(currentPlayer, "Forward");
        } 
        else 
        {
            currentPlayer = handleTurnover(currentPlayer, "Defender");
        }
        return currentPlayer;
    }

    /**
     * Handles events specific to a Forward player.
     * 
     * @param currentPlayer The current player holding the ball.
     * @return The player who holds the ball after the event.
     */
    public Player handleEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();

        // Handle possible actions: Score, Pass, or Turnover
        if (action < 30) {
            return handleScore(currentPlayer, 6); // Score a goal
        } else if (action < 70) {
            return handleScore(currentPlayer, 1); // Score a behind
        } else if (action < 90) {
            return handlePass(currentPlayer, "Forward"); // Pass to a teammate
        } else {
            return handleTurnover(currentPlayer, "Defender"); // Turnover to the opponent
        }
    }


}
