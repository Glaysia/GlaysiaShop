package glaysia.glaysiashop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Money implements CommandExecutor { //명령어 처리 클래스는 CommandExecutor 인터페이스를 상속해야 한다.
    private String userdataFileName;
    private Economy econ=null;
    public Money(Economy econ){
        this.econ=econ;
    }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) { //명령어를 사용자가 입력했으면
                OfflinePlayer player = (Player) sender; //명령어 사용자 객체를 플레이어 객체로 변환
            if (args.length != 0) {
             player = (OfflinePlayer) sender.getServer().getOfflinePlayer(args[0]);
            }else if((args.length!=2)&&(args.length!=0)){
                sender.sendMessage("§4잘못된 입력");
                return false;
            }
//            player.sendMessage(player.getName());
//            player.sendMessage(player.identity().uuid().toString());
                double money = econ.getBalance(player);
                ;
                MyPlayer myPlayer = new MyPlayer(player.getName(), player.getUniqueId().toString(), money);
                DataIO dataIO = new DataIO();


                if (dataIO.isTherePlayerInYml(myPlayer)) {
//                player.sendMessage("플레이어 정보 존재함.");
//                money=dataIO.getMoneyFromPlayer(A);
                    sender.sendMessage("§b잔액: " + String.valueOf(money) + "$");
                    dataIO.writeYmlIfNotWritten(myPlayer);
                } else {
                    sender.sendMessage("플레이어 계좌가 존재하지 않습니다. \n계좌를 만듭니다...");
                    dataIO.writeYmlIfNotWritten(myPlayer);
                    econ.createPlayerAccount(player);
                }

                return true;
            }
        else if (sender instanceof ConsoleCommandSender) {
                //콘솔창에서 사용한 경우
                sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
                return false;
            }
            return false;
        }

}