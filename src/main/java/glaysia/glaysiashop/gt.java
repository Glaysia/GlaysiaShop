package glaysia.glaysiashop;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class gt implements CommandExecutor { //명령어 처리 클래스는 CommandExecutor 인터페이스를 상속해야 한다.

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof Player){ //명령어를 사용자가 입력했으면
            Player player=(Player) sender; //명령어 사용자 객체를 플레이어 객체로 변환
            player.sendMessage("§6아이템을 지급하였습니다.");
            ItemStack item=new ItemStack(Material.DIRT);

            item.setAmount(1);

            player.getInventory().addItem(item);
            return true;
        }else if(sender instanceof ConsoleCommandSender){
            //콘솔창에서 사용한 경우
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        return false;
    }

}