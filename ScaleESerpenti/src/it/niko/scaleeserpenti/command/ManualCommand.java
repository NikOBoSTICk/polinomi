package it.niko.scaleeserpenti.command;

import it.niko.scaleeserpenti.game.Game;

public class ManualCommand implements Command {

    private final Game game;

    public ManualCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        if(game.isFinish())
            return;
        game.nextRound();
    }
}
