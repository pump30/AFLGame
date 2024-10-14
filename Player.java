
/**
 * Represents a player in the AFL game.
 * Each player has a name, position, goals, behinds, kicks, and other attributes.
 * The player can be either active or injured, and can be reported.
 * 
 * @author 34842489
 * @version 8.0
 */
public class Player 
{
    //fields
    private String name;
    private String position;
    private int goals;
    private int behinds;
    private int kicks;
    private int reported;
    private boolean isStar;
    private boolean isActive;
    private boolean isInjured;
    private Team team;
    private int passCount;
    private int playerScore;
    private Randomizer randomizer;
    private boolean isReserve;

    //non-default constructor
    public Player(String name, String position, int goals, Team team)
    {
        this.name = name;
        this.position = position;
        this.goals = goals;
        this.team = team;
        this.kicks = 0;
        this.behinds = 0;
        this.passCount = 0;
        this.isStar = false;
        this.isInjured = false;
        this.reported = 0;
        this.isActive = true;
        this.playerScore = 0;
        if(this.position.equals("Reserve"))
        {
            this.isReserve = true;
        }
        else
        {
            this.isReserve = false;
        }
    }
    public Player startEvent(Player currentPlayer)
    {
        switch (currentPlayer.getPosition()) {
            case "Forward":
                if (currentPlayer.isStar())
                {
                    return handleStarForwardEvent(currentPlayer);
                }
                else
                {
                    return handleForwardEvent(currentPlayer);
                }
            case "Midfielder":
                if (currentPlayer.isStar())
                {
                    return handleStarMidfielderEvent(currentPlayer);
                }
                else
                {
                    return handleMidfielderEvent(currentPlayer);
                }
            case "Defender":
                if (currentPlayer.isStar())
                {
                    return handleStarDefenderEvent(currentPlayer);
                }
                else
                {
                    return handleDefenderEvent(currentPlayer);
                }
            default:
                return currentPlayer;
        }
    }

    public Player handleForwardEvent(Player currentPlayer)
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        // Reset this for regular play

        if (action < 30)
        {
            currentPlayer = handleScore(currentPlayer, 6);
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

    public Player handleMidfielderEvent(Player currentPlayer)
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        double action = randomizer.generateActionProbability();
        //startFromCentre = false;  // Reset this for regular play

        if (action < 5)
        {
            currentPlayer = handleScore(currentPlayer, 6);
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
    //Getters and Setters
    public void addPassCount()
    {
        this.passCount++;
    }
    
    public String getName()
    {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getGoals() {
        return goals;
    }

    public int getBehinds() {
        return behinds;
    }

    public int getPlayerScore()
    {
        return playerScore;
    }

    public int getKicks() {
        return kicks;
    }

    public String getPassPercent()
    {
        if (kicks == 0) return "0.00%";
        int positiveOut = this.getGoals()+ this.getBehinds() + this.passCount;

        double result = ((double)positiveOut / kicks) * 100;
        result = Math.round(result * 100.0) / 100.0;
        return String.format("%.2f", result) + "%";
    }

    public void addKick() {
        this.kicks++;
    }

    public int getReported() {
        return reported;
    }

    public void reportPlayer() {
        this.reported++;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean isInjured)
    {
        this.isInjured = isInjured;
    }

    public Team getTeam()
    {
        return team;
    }

    // Methods to increase goals or behinds
    public void scoreGoal() {
        this.goals++;
    }

    public void scoreBehind() {
        this.behinds++;
    }

    public void addPlayerScore(int newScore)
    {
        this.playerScore += newScore;
    }


    /*
    * Method for handling a Forward's scoring
    * @param currentPlayer The player currently with the ball
    * @param score The score value (either 1 for behind, or 6 for goal)
    * @return Player (the player who holds the ball)
    */
    public Player handleScore(Player currentPlayer, int score)
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        if (score == 6)
        {
            System.out.println(currentPlayer.printName() + " scored a goal, scored 6 points");
            currentPlayer.addPlayerScore(score);
            currentPlayer.scoreGoal();
            currentPlayer.addKick();
            return null;  // After scoring, the play restarts from the center
        }
        else
        {
            System.out.println(currentPlayer.printName() + " scored a behind, scored 1 point");
            currentPlayer.addPlayerScore(score);
            currentPlayer.scoreBehind();
            currentPlayer.addKick();
            Player nextPlayer = randomizer.getRandomActiveOpponent(currentPlayer, "Defender");
            System.out.println(nextPlayer.printName() + "Now has the ball");
            return nextPlayer;  // After a behind, the ball goes to the opponent defender
            
        }
    }
    

    /*
    * Method for handling a pass event
    * @param currentPlayer The player currently with the ball
    * @param targetPosition The position to pass to (e.g., "Forward", "Midfielder")
    * @return Player (the player who receives the pass)
    */
    public Player handlePass(Player currentPlayer, String targetPosition) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        Player teammate = randomizer.getRandomActiveTeammate(currentPlayer, targetPosition);
        currentPlayer.addPassCount();
        currentPlayer.addKick();
        System.out.println(currentPlayer.printName() + " passed the ball to " + teammate.printName());
        System.out.println(teammate.printName() + " has the ball.");
        if(teammate.isReserve) {
            System.out.println();
        }
        return teammate;
    }

     /*
    * Method for handling a turnover event
    * @param currentPlayer The player currently with the ball
    * @param targetPosition The position of the opponent to pass to (e.g., "Defender")
    * @return Player (the opponent player who receives the ball)
    */
    public Player handleTurnover(Player currentPlayer, String targetPosition) 
    {
        final Randomizer randomizer = currentPlayer.getTeam().getRandomizer();
        Player opponent = randomizer.getRandomActiveOpponent(currentPlayer, targetPosition);
        currentPlayer.addKick();
        System.out.println(currentPlayer.printName() + " lost the ball to " + opponent.printName());
        System.out.println(opponent.printName() + " has the ball.");
        return opponent;
    }
    
    // Override toString method
    @Override
    public String toString() 
    {
    return String.format("%s (Position: %s, Team: %s) - Goals: %d, Behinds: %d, Kicks: %d, Passes: %d, Star: %b, Injured: %b",
            this.name, this.position, this.team.getTeamName(), this.goals, this.behinds, this.kicks, this.passCount, this.isStar, this.isInjured);
    }

    /* return the player's condition in String
    * @ return (Reserve) if player is active but not injured
    * @ return (injured ) if player is inactive and injured
    * @ return none if player is active and not injured
    */
    public String printPlayerCondition()
    {
        if (this.isInjured && !this.isReserve)
        {
            return position + "(Injured)";
        }
        else if (this.isReserve && this.isInjured)
        {
            return position + "(Reserve)(Injured)";
        } else if (this.isReserve && !this.isInjured)
        {
            return position + "(Reserve)";
        } else
        {
            return position;

        }
    }

    public String printName()
    {
        return this.name + "(" + this.position + ", "+this.team.getTeamName() + ")";
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

