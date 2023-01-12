package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static glaysia.glaysiashop.GlaysiaShop.log;

public class DoneOrder implements CommandExecutor {
    private Player sender;
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            this.sender= (Player)sender;

//            Trade trade =new Trade();
//            trade.setOrderListToBeDone(sender.getName());
//            List<Trade.Order> myList = trade.getList();
//            sender.sendMessage(trade.doneMessage);
////            for(Trade.Order i : trade.getList()){
////                trade.addPlayersInventoryWhenTrade((Player) sender, i.material, i.amount);
////            }
//
//            AmountSelector.OrderBookGui gui = new AmountSelector.OrderBookGui("구매완료 목록");
//            List<AmountSelector.MyPane> myPanes = new ArrayList<>();
//
//
////            GuiItem guiItem = new GuiItem();
//            AmountSelector.MyPane tmp;
//            int idx=0;
//            int max_idx=trade.getList().size();
//            for(int row=0;row<5;row++){
//                for(int col=0;col<9;col++){
//                    if(idx==max_idx)
//                        break;
//
//                    tmp=new AmountSelector.MyPane(col,row,1,1,(myList.get(idx).material), myList.get(idx), gui);
//                    tmp.setAmount(myList.get(idx).amount);
//                    myPanes.add(tmp);
//                    idx++;
//                }
//                if(idx==max_idx)
//                    break;
//            }
//
//            for(AmountSelector.MyPane i : myPanes){
//                i.setOnClick(
//                        inventoryClickEvent -> {
//                            String info = i.order.toString();
//                            String click = inventoryClickEvent.getClick().toString();
//                            Trade itrade = new Trade();
//                            String message="";
//
//                            if(!click.equals("SHIFT_LEFT")){
//                                message = "시프트 눌러서 한 번에 가져가셈\n"+info;
//                            }else{
//                                i.setVisible(false);
//                                i.clear();
//                                gui.update();
//                            }
//
//
//                            sender.sendMessage(message+click);
//                        }
//                );
//            }
//            gui.show((HumanEntity) sender);
            show();
            return true;
        }
        else if (sender instanceof ConsoleCommandSender) {
            //콘솔창에서 사용한 경우
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        return false;
    }
    private void show(){

        Trade trade =new Trade();
        trade.setOrderListToBeDone(sender.getName());
        List<Trade.Order> myList = trade.getList();
        sender.sendMessage(trade.doneMessage);

        AmountSelector.OrderBookGui gui = new AmountSelector.OrderBookGui("구매완료 목록");
        List<AmountSelector.MyPane> myPanes = new ArrayList<>();

        AmountSelector.MyPane tmp;
        int idx=0;
        int max_idx=trade.getList().size();
        for(int row=0;row<5;row++){
            for(int col=0;col<9;col++){
                if(idx==max_idx)
                    break;

                tmp=new AmountSelector.MyPane(col,row,1,1,(myList.get(idx).material), myList.get(idx), gui);
                tmp.setAmount(myList.get(idx).amount);
                myPanes.add(tmp);
                idx++;
            }
            if(idx==max_idx)
                break;
        }

        for(AmountSelector.MyPane i : myPanes){
            i.setOnClick(
                    inventoryClickEvent -> {
                        String info = i.order.toString();
                        String click = inventoryClickEvent.getClick().toString();
                        Trade itrade = new Trade();
                        String message="";

                        if(!click.equals("SHIFT_LEFT")){
                            message = "시프트 눌러서 한 번에 가져가셈\n"+info;
                            inventoryClickEvent.getInventory().close();
                            gui.show((HumanEntity) sender);
                        }else{
                            if(isThereSpace(inventoryClickEvent.getInventory().getContents())){
                                inventoryClickEvent.getInventory().addItem(new ItemStack(i.order.material,i.order.amount));
                            }
                            i.clear();
                            gui.update();
                        }
                        sender.sendMessage(message+click);
                    }
            );
        }

        gui.show((HumanEntity) sender);
    }

    private boolean isThereSpace(ItemStack[] itemStacks){
        for(ItemStack i : itemStacks){
            if(i.getType()==Material.AIR){
                return true;
            }
        }
        return false;
    }
}
