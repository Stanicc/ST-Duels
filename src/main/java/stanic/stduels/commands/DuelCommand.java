package stanic.stduels.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import stanic.stduels.Main;
import stanic.stduels.duel.DuelManager;
import stanic.stduels.duel.match.Match;
import stanic.stduels.utils.LocationUtils;
import stanic.stduels.utils.concurrent.EntryCallback;
import stanic.stduels.utils.concurrent.WeakConcurrentHashMap;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DuelCommand implements CommandExecutor {

    WeakConcurrentHashMap<Player, Player> invite = new WeakConcurrentHashMap<>(TimeUnit.SECONDS.toMillis(30), new EntryCallback<Player, Player>() {
        @Override
        public void onAdd(Player player, Player target) {
            player.sendMessage("§aYou invited §f" + target.getName() + " §ato a duel");
            target.sendMessage("§eYou're received a duel invitation from §7" + player.getName() + " \n§bUse: §f/duel accept " + target.getName() + " §bto accept \n§7You have up to 30 seconds to accept");
        }

        @Override
        public void onRemove(Player player, Player target) {
            Optional<Match> match = Main.getInstance().getDuelManager().getMatchByPlayer(player.getUniqueId());
            if (match.isPresent()) {
                player.sendMessage("§a" + target.getName() + " §cas accepted your duel invitation");
                target.sendMessage("§aYou accepted §f" + player.getName() + "'s §eduel invite");

                match.get().startMatch();
            } else {
                player.sendMessage("§7" + target.getName() + " §cas declined your duel invitation");
                target.sendMessage("§eYou declined §f" + player.getName() + "'s §eduel invite");
            }
        }
    });

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) sender.sendMessage("§cThis command is allowed only in-game");
        else {
            if (args.length == 0) {
                sender.sendMessage("&4Use: &c/duel (player)");
                return true;
            }

            Player player = (Player) sender;

            switch (args[0].toLowerCase()) {
                case "pos1":
                case "pos2":
                case "spawn":
                case "exit":
                    new LocationUtils().saveLocation(player.getLocation(), args[0].toLowerCase());
                    player.sendMessage("§aLocation §f" + args[0] + " §aset!");
                    break;
                case "accept":
                    if (!invite.containsKey(player)) {
                        sender.sendMessage("§cYou don't have pending invites");
                        return true;
                    }
                    if (args.length != 1) {
                        sender.sendMessage("&4Use: &c/duel accept (player)");
                        return true;
                    }

                    invite.removeFromMap(player, false);
                    break;
                default:
                    DuelManager duelManager = Main.getInstance().getDuelManager();
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage("§cPlayer not found");
                        return true;
                    }
                    if (duelManager.isInDuel(player.getUniqueId())) {
                        player.sendMessage("§cYou're already in a duel");
                        return true;
                    }
                    if (duelManager.isInDuel(target.getUniqueId())) {
                        player.sendMessage("§cThat player are already in a duel");
                        return true;
                    }

                    Match match = duelManager.createMatch();
                    match.addPlayer(player);
                    match.addPlayer(target);
                    invite.put(player, target);
                    break;
            }
        }
        return false;
    }

}
