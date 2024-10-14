/**
 * Represents a Midfielder in the AFL game.
 * Midfielders can goal,behind pass or turnover.
 * 
 * @author 34842489
 * @version 8.0
 */
public class Midfielder extends Player 
{
    // Non-default constructor
    public Midfielder(String name, int goals, Team team)
    {
        super(name, "Midfielder", goals, team);
    }

    /*
    * Method for handling a Midfielder's event.
    * @param currentPlayer The player currently with the ball
    * @param startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player startEvent(Player currentPlayer) 
    {
        if (currentPlayer.isStar()) 
        {
            return handleStarMidfielderEvent(currentPlayer);
        } 
        else 
        {
            return handleMidfielderEvent(currentPlayer);
        }
    }

    /*
    * Method for handling a normal Midfielder's event
    * @param currentPlayer The player currently with the ball
    * @param startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleMidfielderEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play

        if (action < 5) 
        {
            currentPlayer = handleScore(currentPlayer, 6);
            return null;
        } 
        else if (action < 15) 
        {
            currentPlayer = handleScore(currentPlayer, 1);
        } 
        else if (action < 45) 
        {
            currentPlayer = handlePass(currentPlayer, "Forward");
        }
         else if (action < 75)
        {
            currentPlayer = handlePass(currentPlayer, "Midfielder");
        }
        else
        {
            currentPlayer = handleTurnover(currentPlayer, "Midfielder");
        }
        return currentPlayer;
    }

    /*
    * Method for handling a star Forward's event
    * @param currentPlayer The player currently with the ball
    * @param startFromCentre A flag indicating if play starts from the center
    * @return Player (the player who holds the ball)
    */
    public Player handleStarMidfielderEvent(Player currentPlayer) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play

        if (action < 10) 
        {
            currentPlayer = handleScore(currentPlayer, 6);
            //startFromCentre = true;
        } 
        else if (action < 20) 
        {
            currentPlayer = handleScore(currentPlayer, 1);
        } 
        else if (action < 55) 
        {
            currentPlayer = handlePass(currentPlayer, "Forward");
        }
         else if (action < 90)
        {
            currentPlayer = handlePass(currentPlayer, "Midfielder");
        }
        else
        {
            currentPlayer = handleTurnover(currentPlayer, "Midfielder");
        }
        return currentPlayer;
    }

    
}
