package glaysia.glaysiashop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import javax.swing.*;

public class SetMoney implements CommandExecutor {
    private Economy econ=null;
    java.util.logging.Logger log =  java.util.logging.Logger.getLogger("Minecraft");

    public SetMoney(Economy econ){
        this.econ=econ;
    }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        //setmoney  iizuna  3
        //          0       1

        if(sender instanceof Player){ //명령어를 사용자가 입력했으면
            if(args.length!=2){
                sender.sendMessage("§4잘못된 입력");
                return false;
            }
            DataIO dataIO = new DataIO();

            Player player=(Player) sender; //명령어 사용자 객체를 플레이어 객체로 변환
//            player.sendMessage(player.getName());
//            player.sendMessage(player.identity().uuid().toString());
//            double money=econ.getBalance(player);;
            double money = Double.parseDouble(args[1]);

            player.sendMessage("닉네임:"+args[0]+"원:"+args[1]);
            Server server=sender.getServer();
            OfflinePlayer oPlayer=(OfflinePlayer) server.getPlayer(args[0]);
            if(oPlayer==null){
                oPlayer=server.getOfflinePlayer(args[0]);
            }

            double originalMoney=econ.getBalance(args[0]);
            double lastMoney=Double.parseDouble(args[1]);
            boolean surplus=lastMoney>=originalMoney;

            if(!econ.hasAccount(oPlayer)){
                econ.createPlayerAccount(oPlayer);
                sender.sendMessage("플레이어 계좌 존재가 존재하지 않습니다. \n계좌를 만듭니다...");
            }else{
                if(surplus) {
                    econ.depositPlayer(oPlayer, lastMoney - originalMoney);
                }
                else{
                    econ.withdrawPlayer(oPlayer,originalMoney - lastMoney);
                }
            }

            MyPlayer myPlayer=new MyPlayer(oPlayer.getName(),oPlayer.getUniqueId().toString(),lastMoney);
            dataIO.writeYmlIfNotWritten(myPlayer);

            return true;
        }else if(sender instanceof ConsoleCommandSender){
            //콘솔창에서 사용한 경우
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        return false;
    }

}
