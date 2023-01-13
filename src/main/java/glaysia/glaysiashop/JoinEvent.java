package glaysia.glaysiashop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
// EventHandler import needed for the event.
import org.bukkit.event.Listener;
// Listener import needed for the event.
import org.bukkit.event.player.PlayerJoinEvent;
// This is the import that holds when the player joins.

public class JoinEvent implements Listener {
    @EventHandler
    // EventHandler to recognize the event.
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player=event.getPlayer();
        Trade trade =new Trade();
        trade.setOrderListToBeDone(player.getName());
        String message=trade.doneMessage;

        if(message.length()==0){
            event.setJoinMessage("§cI made my first join plugin!");
        }else{
            player.sendMessage(message);
        }
        // This sets the join message. (Replaces the default join message)
    }
}