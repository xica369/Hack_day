___
# Hack_day - TicTacToe Challenge

Project from Holberton School Hack-day, the present project has been built in scala multi-paradigm (Functional programming and Object Oriented Programming) programming languaje. Also, as a Scala project you need to ensure that the package Scala and Java is on your machine.

![N|Solid](https://i.redd.it/v6aucg2ju9k21.jpg)

___
# Very basics of Scala and Play

A first example, we use the standard “Hello, world!” program to demonstrate the use of the Scala tools without knowing too much about the language.

```sh
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
  }
}
```

To make this project executable as a webapp, the main framework is called Play, a small tutorial would be able using the next commands:

```sh
$ sudo apt-get update
$ sudo apt-get install sbt
$ sbt new playframework/play-scala-seed.g8
$ sbt run
```
___

## How to execute it in the console?

For this point maybe you ask how to execute our wonderful game, to do this you have to follow the next steps:

 - Clone the GitHub repo
 - Enter the folder where you clone the repo, make sure that your scala version is 2.11.6 (link) and make  sure that you have java install. ```$ cd Hack_day/```
 - Now to excecute the file with .scala extension type the following command: ```$ scala tic-tac-toe.scala ```.
 - This process make the file compiled and executable, but if for reasons unknow you need to compile then try with the following command: ```$ scalac tic-tac-toe.scala ```, otherwise just omitted it.
 - Then, the game is going to ask you to choose if you want to play with the machine or with another player.

  ![N|Solid](https://i.imgur.com/dK1d7j6.png)

 - Now your name will be required and the second player if you choose (the name is what ever you want).
 ![N|Solid](https://i.imgur.com/p2s263W.png)
 - The way to play is: write a number followed by a space, the number should be between 1 and 3, where the first name choose the columns and the second number are the rows
```sh
[-]
[-] -> columns         [-][O][X] -> rows    
[O]
```
![N|Solid](https://i.imgur.com/ThY4nEi.png)

 - The game choose a winner when the rows and columns are filled.

![N|Solid](https://i.imgur.com/8GDIC2D.png)

 - You have 5 opportunities to win, each draw will give you one point, and if you win, you will receive 2 points.

 - The game finish when you try your 5 opportunities and retrives the final score

![N|Solid](https://i.imgur.com/jBVF2se.png)

___

# Authors

 - Alejandro Rey - [GITHUB](https://github.com/alejo-rey) / [TWITTER](https://twitter.com/AlejoReyRios)
 - Jeniffer Vanegas - [GITHUB](https://github.com/jeniffervp) / [TWITTER](https://twitter.com/jeniffervaneg)
 - Paula Gutierrez - [GITHUB](https://github.com/andzapata) / [TWITTER](https://twitter.com/AndZapata1)
 - Ximena Andrade - [GITHUB](https://github.com/xica369) / [TWITTER](https://twitter.com/xica369)
 - Yesid Gonzalez - [GITHUB](https://github.com/yawzyag) / [TWITTER](https://twitter.com/yesid_dev)
 ___
