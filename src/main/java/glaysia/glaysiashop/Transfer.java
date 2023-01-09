package glaysia.glaysiashop;
@Deprecated
public class Transfer {
    public Transfer(){}
}
//package glaysia.glaysiashop;
//
//import net.milkbowl.vault.economy.Economy;
//import org.bukkit.Material;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//
//@Deprecated
//public class Transfer implements CommandExecutor {
//    private Economy econ=null;
//    public Transfer(Economy econ){
//        this.econ=econ;
//    }
//    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
//        Player player=(Player) sender;
//        DataIO dataIO=new DataIO(player);
//        dataIO.writeAdminYmlIfNotWritten();
//        // 변수 설정
//        String mainHand= player.getInventory().getItemInMainHand().translationKey();
//        int mainHandNumber=player.getInventory().getItemInMainHand().getAmount();
//        int numberOfTransferring;
//        double money=econ.getBalance(player);
//
//        if(args.length==0)
//            numberOfTransferring=mainHandNumber;
//        else
//            numberOfTransferring=Integer.parseInt(args[0]);
//
//        // 돈->다이아 환전
////        sender.sendMessage(mainHand);
//        if(mainHand.equals("block.minecraft.air")){
//            if(args.length!=1) {
//                sender.sendMessage("§4잘못된 입력");
//                return false;
//            }
//            if(money<numberOfTransferring*10){
//                sender.sendMessage("모자란데요?");
//                return false;
//            }
//
//            econ.withdrawPlayer(player, numberOfTransferring*1000);    //출금
//            dataIO.writeAdminMoneyDiamondToYml(numberOfTransferring*1000.0, -numberOfTransferring);
//            sender.sendMessage(Double.toString(numberOfTransferring*1000.0) + "$를 다이아" + Integer.toString(numberOfTransferring ) + "개로 환전하였습니다.");
//            //채팅 출력
//            Material dia = Material.matchMaterial("diamond",false);
//            ItemStack itemStack=new ItemStack(dia);
//            itemStack.setAmount(numberOfTransferring);
//            //아이템 지정
//            player.getInventory().addItem(itemStack);
//            //아이템 주입
//            return true;
//        }
//
//        // 다이아->돈 환전
//        if(!mainHand.equals("item.minecraft.diamond")){
//            //sender.sendMessage(mainHand, Integer.toString(mainHandNumber));
//            sender.sendMessage("다이아->돈 환전: 다이아 들고 명령어 사용\n돈->다이아 환전: 빈손으로 명령어 사용");
//            return false;
//        }
//        //손에 든 것 전부 환전
//        if(args.length==0) {
//            numberOfTransferring=mainHandNumber;
//        }
//
//        //차이
//        int delta=mainHandNumber-numberOfTransferring;
//
//        if(delta>=0) {
//            player.getInventory().getItemInMainHand().setAmount(delta);
//            dataIO.writeAdminMoneyDiamondToYml(-numberOfTransferring*1000.0, numberOfTransferring);
//            sender.sendMessage("다이아몬드 " + Integer.toString(numberOfTransferring) + "개를 " + Integer.toString(numberOfTransferring * 1000) + "$로 환전하였습니다.");
//            econ.depositPlayer(player, (double)(numberOfTransferring * 1000)); //입금
//            return true;
//        }else{
//            sender.sendMessage("모자란데요?");
//            return false;
//        }
//    }
//}
