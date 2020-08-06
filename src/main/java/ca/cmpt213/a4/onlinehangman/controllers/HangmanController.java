package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.Game;
import ca.cmpt213.a4.onlinehangman.model.Message;
import ca.cmpt213.a4.onlinehangman.model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

// By Kevin Tang (301357455, kta76@sfu.ca)

/**
 * The main API controller for the entire application
 */
@Controller
public class HangmanController {

    private Message promptMessage; // A reusable String object to display a prompt message at the screen

    private Game gameLogic; // The current game object that hosts the model

    private ArrayList<Game> gamesList = new ArrayList<>(); // Saved list of previous concluded or active games

    private AtomicLong ID = new AtomicLong(0); // Incremental ID counter

    private String[] hangmanStates = {
            "Hangman-0.png",
            "Hangman-1.png",
            "Hangman-2.png",
            "Hangman-3.png",
            "Hangman-4.png",
            "Hangman-5.png",
            "Hangman-6.png",
            "HangmanLogo.png"
    }; // Hard-coded addresses for the dynamic hangman image

    //works like a constructor, but wait until dependency injection is done, so it's more like a setup
    @PostConstruct
    public void hangmanControllerInit() {
        promptMessage = new Message("Initializing...");
    }

    /**
     * Example mapping provided in skeleton application
     * @param model The model
     * @return Direction to helloworld.html
     */
    @GetMapping("/helloworld")
    public String showHelloworldPage(Model model) {

        promptMessage.setMessage("You are at the helloworld page!");
        model.addAttribute("promptMessage", promptMessage);

        // take the user to helloworld.html
        return "helloworld";
    }

    /**
     * Handles direction to the welcome page
     * @param model The model
     * @return Direction to welcome.html
     */
    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {

        promptMessage.setMessage("Welcome to the Online Hangman Game");
        model.addAttribute("promptMessage", promptMessage);

        // take the user to welcome.html
        return "welcome";
    }

    /**
     * Creates a new game model and handles direction to the game page
     * @param model The model
     * @return Direction to game.html
     * @throws FileNotFoundException
     */
    @PostMapping("/game")
    public String showGamePage(Model model) throws FileNotFoundException {

        // Create new game
        Game game = new Game(ID.incrementAndGet());
        gameLogic = game;
        gamesList.add(game);
        model.addAttribute("gameLogic",gameLogic);

        // Take the user to game.html by refreshing page
        return updateView(model);
    }

    /**
     * Directs to a game of specified id
     * @param model The model
     * @param id Id of the game to be found
     * @return Direction to game.html with the specific game loaded
     */
    @GetMapping("/game/{id}")
    public String showGameById(Model model, @PathVariable long id) {
        boolean found = false;
        for (Game game : gamesList) {
            if (game.getID() == id) {
                gameLogic = game;
                found = true;
            }
        }
        if (found) return updateView(model);
        else throw new GameNotFoundException();
    }

    /**
     * Attempt to guess a letter in word
     * @param model The model
     * @param guess The input guess letter to attempt on word
     * @return Direction back to game.html
     */
    @PostMapping("/attempt")
    public String guessAttempt(Model model, @RequestParam("guess") char guess) {

        gameLogic.attempt(guess);

        // Refresh page
        return updateView(model);
    }

    /**
     * Refreshes the game.html page with updated information and graphics
     * @param model The model
     * @return Direction back to game.html
     */
    public String updateView(Model model) {

        // Update info display
        model.addAttribute("totalCounter",gameLogic.getAttempts());
        model.addAttribute("successCounter",gameLogic.getSuccessfulAttempts());
        model.addAttribute("failCounter",gameLogic.getFailedAttempts());
        model.addAttribute("gameInfo","Game " + gameLogic.getID() + " ["+ gameLogic.getStatus()+"]");
        model.addAttribute("wordState",gameLogic.getWordState());

        // Update hangman image
        if (gameLogic.getFailedAttempts() <= 7) {
            model.addAttribute("hangmanState",hangmanStates[gameLogic.getFailedAttempts()]);
        } else {
            model.addAttribute("hangmanState",hangmanStates[7]);
        }

        // Check game status and redirect accordingly
        if (gameLogic.getStatus() == Status.ACTIVE) return "game";
        else {
            model.addAttribute("endingStatus", "YOU " + gameLogic.getStatus());
            model.addAttribute("answer", "The answer is " + gameLogic.getWord());
            return "gameover";
        }
    }

    /**
     * Handles the exception where game of a certain id cannot be found in gamesList
     * @return Direction to gamenotfound.html
     */
    @ExceptionHandler({ GameNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String GameNotFoundExceptionHandler() { return "gamenotfound"; }
}