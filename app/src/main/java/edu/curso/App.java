package edu.curso;

import edu.curso.view.PrincipalBoundary;
import javafx.application.Application;

public class App {
    public String getGreeting() {
        return "MarketPlace ArtCRAFT";
    }

    public static void main(String[] args) {
        new App().getGreeting();
        Application.launch(PrincipalBoundary.class, args);
    }
}