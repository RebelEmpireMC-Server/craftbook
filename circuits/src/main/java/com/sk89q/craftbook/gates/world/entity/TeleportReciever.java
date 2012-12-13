package com.sk89q.craftbook.gates.world.entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.bukkit.BukkitUtil;
import com.sk89q.craftbook.ic.AbstractIC;
import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.ICFactory;
import com.sk89q.craftbook.util.Tuple2;

public class TeleportReciever extends AbstractIC {

    public TeleportReciever (Server server, ChangedSign sign, ICFactory factory) {
        super(server, sign, factory);
    }

    @Override
    public String getTitle () {
        return "Teleport Reciever";
    }

    @Override
    public String getSignTitle () {
        return "TELEPORT IN";
    }

    String band;

    @Override
    public void load() {
        band = getLine(2);
    }

    @Override
    public void trigger (ChipState chip) {

        if (chip.getInput(0)) {
            check();
        }
    }

    public void check() {
        Tuple2<Long, String> val = TeleportTransmitter.getValue(band);
        if (val == null) return;

        Player p = Bukkit.getServer().getPlayer(val.b);

        if(p == null || !p.isOnline()) {
            return;
        }

        p.teleport(BukkitUtil.toSign(getSign()).getLocation().add(0.5, 1.5, 0.5));
        p.sendMessage(ChatColor.YELLOW + "The Teleporter moves you here...");
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new TeleportReciever(getServer(), sign, this);
        }

        @Override
        public String getDescription() {

            return "Reciever for the teleportation network.";
        }

        @Override
        public String[] getLineHelp() {

            String[] lines = new String[] {
                    "frequency name",
                    null
            };
            return lines;
        }
    }
}