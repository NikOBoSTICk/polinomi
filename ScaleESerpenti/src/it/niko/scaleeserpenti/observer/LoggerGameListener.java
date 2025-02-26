package it.niko.scaleeserpenti.observer;

import it.niko.scaleeserpenti.game.GameEvent;

import javax.swing.*;

/*
  Visualizza gli eventi avvenuti nei vari round del gioco
 */
public class LoggerGameListener extends JPanel implements GameListener {
    private final JTextArea textArea;

    public LoggerGameListener() {
        textArea = new JTextArea(20, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
    }

    @Override
    public void update(GameEvent e) {
        switch(e.getEventType()) {
            case CONFIG -> textArea.setText("");
            case ROUND -> {
                textArea.append(e.getSrc().getRoundLog());
                repaint();
                revalidate();
            }
        }
    }
}
