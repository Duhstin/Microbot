package net.runelite.client.plugins.microbot.dustin.KarambwanFisher;

import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.accountselector.AutoLoginScript;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.bank.enums.BankLocation;
import net.runelite.client.plugins.microbot.util.camera.Rs2Camera;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.runelite.client.plugins.microbot.dustin.KarambwanFisher.currentState.FISH;


enum currentState {
    FISH,
    RING_TO_BANK,
    WALK_AND_BANK,
    GO_BACK_TO_RING,
    MISSING_ITEMS,
}


public class KarambwanFisherScript extends Script {

    Zone Zanaris = new Zone(new WorldPoint(2379, 4464, 0), new WorldPoint(2415, 4432, 0));
    Zone FishingArea = new Zone(new WorldPoint(2906, 3110, 0), new WorldPoint(2893, 3122, 0));

    private currentState state = FISH;
    public boolean run(KarambwanFisherConfig config) {
        Microbot.enableAutoRunOn = false;
        Rs2Walker.disableTeleports = true;
        Rs2Antiban.antibanSetupTemplates.applyFishingSetup();
        Rs2AntibanSettings.microBreakActive = true;
        Rs2AntibanSettings.takeMicroBreaks = true;
        Rs2AntibanSettings.microBreakDurationLow = 1;
        Rs2AntibanSettings.microBreakDurationHigh = 3;
        Rs2AntibanSettings.microBreakChance = .2;

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

            updateState();

            if(state == null){
                Microbot.log("ERROR?");
            }

                switch (state) {
                    case MISSING_ITEMS:
                        disableAutoLogin("Missing items. Logging out.");
                        break;
                    case FISH:
                        //Microbot.log("WE CAN FISH");
                        NPC fishingSpot = Rs2Npc.getNpc(4712);
                        if (fishingSpot != null && !Rs2Player.isAnimating()){
                            Rs2Camera.turnTo(fishingSpot);
                            Rs2Npc.interact(fishingSpot, "Fish");
                            Rs2Player.waitForAnimation();
                            Rs2Player.waitForWalking();
                            sleepUntil(() -> Rs2Player.isAnimating(), 15000);
                        }
                        break;

                    case RING_TO_BANK:
                        //Microbot.log("We should use the ring and go bank.");
                        var FishingRing = Rs2GameObject.findObjectById(29495);
                        if(FishingRing != null) {
                            Rs2GameObject.interact(FishingRing, "Zanaris");
                            sleepUntil(() -> Zanaris.contains(Rs2Player.getWorldLocation()), 5000);
                        }

                        break;

                    case WALK_AND_BANK:
                            if (Rs2Bank.isOpen()) {
                                Rs2Bank.depositAllExcept("Raw karambwanji", "Karambwan vessel");
                                Rs2Bank.closeBank();
                            } else {
                                Rs2Walker.disableTeleports = true;
                                Rs2Bank.walkToBankAndUseBank(BankLocation.ZANARIS);
                            }
                        break;

                    case GO_BACK_TO_RING:
                        Rs2Walker.disableTeleports = true;
                        Rs2Walker.walkTo(new WorldPoint(2411, 4436, 0));

                        var ZanarisRing = Rs2GameObject.findObjectById(29560);
                        if(ZanarisRing != null) {
                            Rs2GameObject.interact(ZanarisRing, "Last-destination (DKP)");
                        }
                        sleepUntil(() -> FishingArea.contains(Rs2Player.getWorldLocation()), 5000);

                        break;

                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private void updateState() {
        if (!Rs2Inventory.contains("Raw karambwanji", "Karambwan vessel")) {
            state = currentState.MISSING_ITEMS;
        } else if (Rs2Inventory.isFull() && FishingArea.contains(Rs2Player.getWorldLocation())){
            state = currentState.RING_TO_BANK;
        } else if (Rs2Inventory.isFull() && Zanaris.contains(Rs2Player.getWorldLocation())){
            state = currentState.WALK_AND_BANK;
        }else if(!Rs2Inventory.isFull() && Zanaris.contains(Rs2Player.getWorldLocation())) {
            state = currentState.GO_BACK_TO_RING;
        }else if(!Rs2Inventory.isFull() && FishingArea.contains(Rs2Player.getWorldLocation())){
            state = currentState.FISH;
        }else{
            state = null;
        }
    }

    private void disableAutoLogin(String message) {
        List<Script> runningScripts = Microbot.getActiveScripts();

        for (Script script : runningScripts) {
            if (script instanceof AutoLoginScript) {
                script.shutdown();
            }
        }
        Rs2Player.logout();
        Microbot.showMessage(message);
        shutdown();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        // Any cleanup code here
    }
}