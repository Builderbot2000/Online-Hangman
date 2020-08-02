package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class HangmanController {
    private Message promptMessage; //a resusable String object to display a prompt message at the screen

    //works like a constructor, but wait until dependency injection is done, so it's more like a setup
    @PostConstruct
    public void hangmanControllerInit() {
        promptMessage = new Message("Initializing...");
    }

    @GetMapping("/helloworld")
    public String showHelloworldPage(Model model) {

        promptMessage.setMessage("You are at the helloworld page!");
        model.addAttribute("promptMessage", promptMessage);

        // take the user to helloworld.html
        return "helloworld";
    }

    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {

        promptMessage.setMessage("Welcome to the Online Hangman Game");
        model.addAttribute("promptMessage", promptMessage);

        // take the user to welcome.html
        return "welcome";
    }

    @PostMapping("/game")
    public String showGamePage(Model model) {

        // take the user to game.html
        return "game";
    }
}