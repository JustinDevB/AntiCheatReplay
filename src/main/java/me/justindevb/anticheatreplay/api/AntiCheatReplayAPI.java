package me.justindevb.anticheatreplay.api;

import me.justindevb.anticheatreplay.AntiCheat;
import me.justindevb.anticheatreplay.AntiCheatReplay;

public class AntiCheatReplayAPI {
    private AntiCheatReplay acReplay;
    public static AntiCheatReplayAPI instance = null;

    public AntiCheatReplayAPI(AntiCheatReplay acReplay) {
        this.acReplay = acReplay;
        instance = this;
    }

    /**
     * Return active AntiCheat running on the server
     * @return
     */
    public AntiCheat getActiveAntiCheat() {
        return acReplay.getAntiCheat();
    }


    /**
     * Get instance of AntiCheatReplayAPI
     * @return instance
     */
    public AntiCheatReplayAPI getInstance() {
        return instance;
    }
}
