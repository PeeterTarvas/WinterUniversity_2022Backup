package com.nortal.platformer;

import java.util.*;

public class Game {

    public static void main(String[] args) {
        Game game = new Game("platforms.csv");
        game.run();
    }

    private Integer points = 500;
    private Platform activePlatform;
    private final String gameFile;

    public Game(String gameFile) {
        this.gameFile = gameFile;
    }

    public void run() {
        List<Platform> platforms = readPlatforms();

        // TODO: Implement your mighty algorithm and jump to oblivion.
//        activePlatform = platforms.get(0);
//        Platform nextPlatform = findNextPlatform(activePlatform);
//        jumpTo(nextPlatform);

    }

    /**
     * Reads platforms from csv file and returns the as list.
     * @return platforms - Platforms as list
     */
    private List<Platform> readPlatforms() {
        return Collections.emptyList();
    }


    /**
     * Invoke this function to jump to next platform.
     * @param platform - Platform that you are going to jump to.
     */
    public void jumpTo(Platform platform) {
        activePlatform = platform;
    }
}
