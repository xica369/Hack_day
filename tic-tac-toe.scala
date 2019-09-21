object tic {
    object juanito {
        var score1 = 0
        var score2 = 0
        var count:Int = 0
        var bandera:Int = 0


        val computerOrP2 = readLine("\033[32mchoose between machine or player_2\n")
        var name_p1 = "Player 1"
        var name_p2 = "Player 2"
    }



    trait State[S <: State[S]] {
    def isGameOver      : Boolean
    def playerOneWin    : Boolean
    def playerTwoWin    : Boolean
    def isPlayerOneTurn : Boolean
    def generateStates  : Seq[S]
    }

    trait BestMoveFinder[S <: State[S]] {
    def move(s: S): S
    }

    class MinMaxStrategyFinder[S <: State[S]] extends BestMoveFinder[S] {
    def move(s: S): S = 
        if(s.isPlayerOneTurn) firstTurn(s).state
        else secondTurn(s).state
    
    def secondTurn(s: S): Outcome = 
        bestMoveFinder(minimize, firstTurn, PLAYER_ONE_WIN, s)
    
    def firstTurn(s: S): Outcome = 
        bestMoveFinder(maximize, secondTurn, PLAYER_ONE_LOOSE, s)
    
    def bestMoveFinder(strategy: Criteria, opponentMove: S => Outcome, 
                        worstOutcome: Int, s: S): Outcome = 
        if(s.isGameOver) Outcome(outcomeOfGame(s), s)
        else s.generateStates
            .foldLeft(Outcome(worstOutcome, s)){(acc, state) => {
                val outcome = opponentMove(state).cost
                if(strategy(outcome, acc.cost)) Outcome(outcome, state)
                else acc}}
    
    def outcomeOfGame(s: S): Int = 
        if (s.playerOneWin) PLAYER_ONE_WIN 
        else if(s.playerTwoWin) PLAYER_ONE_LOOSE 
        else DRAW
    
    case class Outcome(cost: Int, state: S)
    
    val PLAYER_ONE_WIN   =  1 
    val PLAYER_ONE_LOOSE = -1 
    val DRAW             =  0 
    
    type Criteria = (Int, Int) => Boolean
    def maximize: Criteria = (a, b) => a >= b
    def minimize: Criteria = (a, b) => a <= b
    }

    case class Position(val row: Int, val col: Int)

    class TicTacToeState(val playerOnePositions : Set[Position],
                        val playerTwoPositions : Set[Position],
                        val availablePositions : Set[Position],
                        val isPlayerOneTurn : Boolean,
                        val winLength : Int) extends State[TicTacToeState] {
    
    lazy val isGameOver  : Boolean = 
        availablePositions.isEmpty || playerOneWin || playerTwoWin
        
    lazy val playerOneWin: Boolean = checkWin(playerOnePositions)
    
    lazy val playerTwoWin: Boolean = checkWin(playerTwoPositions)
    
    def generateStates: Seq[TicTacToeState] = 
        for(pos <- availablePositions.toSeq) yield makeMove(pos)
                
    def makeMove(p: Position): TicTacToeState = {
        assert(availablePositions.contains(p))
        if(isPlayerOneTurn) 
        new TicTacToeState(
            playerOnePositions + p, 
            playerTwoPositions, 
            availablePositions - p, 
            !isPlayerOneTurn, 
            winLength)
        else new TicTacToeState(
            playerOnePositions, 
            playerTwoPositions + p, 
            availablePositions - p, 
            !isPlayerOneTurn, 
            winLength)}
        
    def checkWin(positions: Set[Position]): Boolean =
        directions.exists(winConditionsSatisified(_)(positions))
        
    final val directions = List(leftDiagonal, rightDiagonal, row, column)
    
    type StepOffsetGen = (Position, Int) => Position
    
    def leftDiagonal : StepOffsetGen = 
        (pos, offset) => Position(pos.row + offset, pos.col + offset)
        
    def rightDiagonal : StepOffsetGen = 
        (pos, offset) => Position(pos.row - offset, pos.col + offset)
        
    def row : StepOffsetGen = 
        (pos, offset) => Position(pos.row + offset, pos.col)
        
    private def column : StepOffsetGen = 
        (pos, offset) => Position(pos.row, pos.col + offset)
        
    def winConditionsSatisified(step: StepOffsetGen)
                                (positions: Set[Position]): Boolean =
        positions exists( position =>
        (0 until winLength) forall( offset =>
            positions contains step(position, offset)))
        
    def this(dimensionES: Int, winLength: Int) = this(
        Set(), Set(),
        (for{row <- (1 to dimensionES)
            col <- (1 to dimensionES)} yield Position(row, col)).toSet, 
        true, winLength)
    }

    class HumanTicTacPlayer extends BestMoveFinder[TicTacToeState] {
    def move(s: TicTacToeState): TicTacToeState = {
        println("\033[32m->[Input the row and the column, please:]<-")
        println("****choose between 1 2 or 3 (row column) Example input: 3 3****")
        println("\033[0m")
        var linea = readLine()
        var tokens = linea.split(" ")
        var arr = Array("1", "2", "3")

        while (tokens.size != 2) {
            println("\033[31m\ninsert two numbers separated by single space!")  
            println("\033[0m")
            linea = readLine()
            tokens = linea.split(" ")
        }
        juanito.bandera = 1
        while (!(arr contains (tokens(0))) || !(arr contains (tokens(1)))) {
            println("\033[31m\ninsert two [numbers] separated by single space!")  
            println("\033[0m")
            linea = readLine()
            tokens = linea.split(" ")
        }
        var (row, col) = (tokens(0).toInt, tokens(1).toInt)

        val pos = Position(row.asInstanceOf[Long].toInt, col.asInstanceOf[Long].toInt)
        try {
        s.makeMove(pos)
        } catch {
        case _: Throwable => {
            println("\033[31mSuch move is impossible!")
            move(s)}}}
    }


    class TicTacToe(playerOne: BestMoveFinder[TicTacToeState], 
                    playerTwo: BestMoveFinder[TicTacToeState]) {
    
    private var players = List(playerOne, playerTwo)

    private var game: TicTacToeState = 
        new TicTacToeState(DIMENSIONES, DIMENSIONES)
    
    def play = {
        var moveNumber = 0
        if (juanito.bandera == 0) {
            if (juanito.computerOrP2 == "player_2") {
                juanito.name_p1 = readLine("\033[32mchoose player_1 name\n")
                juanito.name_p2 = readLine("\033[32mchoose player_2 name\n")
                println("\033[0m")
            } else if(juanito.computerOrP2 == "machine") {
                juanito.name_p1 = readLine("\033[32mchoose player_1 name\n")
                println("\033[0m")
            } else {
                println("\033[31myou need to choose between machine or player_2!")
                println("\033[0m")
                System.exit(1)
            }
        }

        while(!game.isGameOver) {
        println(s"\033[32mPlayer ${moveNumber % 2 + 1} makes move:\n")
        val player = players.head
        game = player.move(game)
        println(display(game))
        println("\033[0m")
        players = players.tail :+ player
        moveNumber += 1
        }
        if(game.playerOneWin) {
            juanito.score1 += 2
            println("\033[31m----------------------!")
            println(s"\033[35m" + juanito.name_p1 +" win!")
            println(s"Score: " + juanito.name_p1 + " = " + juanito.score1)
            println(s"Score: " +juanito.name_p2 + " = " + juanito.score2)
            println("\033[31m----------------------!\n")           
        }
        else if(game.playerTwoWin) {
            juanito.score2 += 2
            println("\033[31m----------------------!") 
            println(s"\033[35m" + juanito.name_p2 + " win!")
            println(s"Score: " +juanito.name_p2+ " = " + juanito.score2)
            println(s"Score: " +juanito.name_p1+ " = "+juanito.score1)
            println("\033[31m----------------------!\n") 
        }
        else {
            juanito.score1 += 1
            juanito.score2 += 1
            println("\033[31m----------------------!") 
            println("\033[35mDraw!")
            println(s"Score " + juanito.name_p1 + ": = " + juanito.score1)
            println(s"Score " + juanito.name_p2 + ": = " + juanito.score2)
            println("\033[31m----------------------!\n") 
        }
        juanito.count += 1
    }
        
    def display(game: TicTacToeState) = 
        (1 to DIMENSIONES).map(row =>
        (1 to DIMENSIONES).map(col => {
            val p = Position(row, col)
            if(game.playerOnePositions contains p) X_PLAYER 
            else if(game.playerTwoPositions contains p) O_PLAYER 
            else EMPTY_CELL
        }).mkString).mkString(" \n\n") + "\n"
        
    final val DIMENSIONES  = 3
    final val X_PLAYER   = "\033[37m[X]"
    final val O_PLAYER   = "\033[33m[O]"
    final val EMPTY_CELL = "\033[34m[-]"
    }

    object Execution {
        def loop(){
            if (juanito.computerOrP2 == "player_2") {
                val game = new TicTacToe(
                    new HumanTicTacPlayer(),
                    new HumanTicTacPlayer())
                game.play
            } else {
                val game = new TicTacToe(
                new HumanTicTacPlayer(),
                new MinMaxStrategyFinder[TicTacToeState]())
                game.play
            } 
        }

        def init(){
            var numof_games:Int = 4

            while (numof_games > juanito.count) {
                loop()
            }

            println("\033[0m")
            println("\033[31m----------------------!") 
            println("\033[31mend game")
            println(s"\033[35mScore " + juanito.name_p1 + ": = " + juanito.score1)
            println(s"\033[35mScore " + juanito.name_p2 + ": = " + juanito.score2)
            println("\033[31m----------------------!\n") 

            println("\033[0m")
        }
    }

    def main(args: Array[String]): Unit = {
        println("Hello, world!")
        println("This project was made in The Scala Programming Language")
        println("Authors: ----------------------------------------------")
        println("->Ximena Andrade")
        println("->Yesid Gonzalez")
        println("->Alejandro Rey")
        println("->Paula Gutierrez")
        println("->Jeniffer Vanegas")
        Execution.init()
    }

}
