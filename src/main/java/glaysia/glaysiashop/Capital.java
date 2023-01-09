package glaysia.glaysiashop;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static glaysia.glaysiashop.GlaysiaShop.log;

public class Capital implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = null;
        if(sender instanceof Player){
            player=(Player) sender;
        }else {
            log.info("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        DataIO dataIO = new DataIO(player);

        double money=dataIO.getMoneyFromYml();
        int diamond=dataIO.getDiamondFromYml();
        String message="지금까지 환전돼 저장된 다이아의 개수는 "+Integer.toString(diamond)+"개 입니다.\n화폐의 총액은 "+Double.toString(-money)+"$ 입니다.";
        sender.sendMessage(message);
        return true;
    }

}
