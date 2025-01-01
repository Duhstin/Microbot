package net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;

import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.WildernessSlayerHelperConfig;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.equipment.JewelleryLocationEnum;
import net.runelite.client.plugins.microbot.util.equipment.Rs2Equipment;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.player.Rs2Pvp;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerDetection extends Script {

    @Inject
    private Client client;

    public boolean run(WildernessSlayerHelperConfig config) {

        Microbot.enableAutoRunOn = false;
        //Rs2Antiban.resetAntibanSettings();
       // Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!super.run() || !Microbot.isLoggedIn() || !config.useTeleport()) return;

                if (!Rs2Equipment.isWearing("Amulet of glory(")) {
                    Microbot.log("NO CHARGED GLORY EQUIPPED!");
                    return;
                }

                if (!Rs2Equipment.isWearing("Ring of dueling(")) {
                    Microbot.log("NO RING OF DUELING EQUIPPED!");
                    return;
                }

                if (!Rs2Pvp.isInWilderness()) return;

                List<Player> dangerousPlayers = Rs2Player.getPlayersInCombatLevelRange().stream().filter(this::shouldTrigger).collect(Collectors.toList());

                if(!dangerousPlayers.isEmpty() && !Rs2Player.isTeleBlocked()) {

                    if (Rs2Pvp.getWildernessLevelFrom(client.getLocalPlayer().getWorldLocation()) <= 20) {
                        Microbot.log("We're in 20 or less wilderness and we're not teleblocked. We'll teleport to Ferox! ");
                        Rs2Equipment.useRingAction(JewelleryLocationEnum.FEROX_ENCLAVE);
                    }else if (Rs2Pvp.getWildernessLevelFrom(client.getLocalPlayer().getWorldLocation()) <= 30) {
                        Microbot.log("We're in 21 or higher wilderness and we're not teleblocked. We'll teleport to Edgeville! ");
                        Rs2Equipment.useAmuletAction(JewelleryLocationEnum.EDGEVILLE);
                    }
                    sleepUntil(() -> !Rs2Pvp.isInWilderness(), 5000);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        return true;
    }


    private int WildernessLevel() {
        Widget widget = Rs2Widget.getWidget(90, 50);

            if (widget != null) {
                String data = widget.getText();
                if(data.contains("--")) return 0;
                String[] Split = data.split("<");
                String[] Split2 = Split[0].split(": ");
                return Integer.parseInt(Split2[1]);
            }

        return 0;
    }


    private boolean shouldTrigger(Player player) {
        if(player.isClanMember() || player.isFriendsChatMember() || player.isFriend()) return false;
        Microbot.log("Player spotted: " + player.getName() + " - Combat Level: " + player.getCombatLevel());
        return true;
    }

}