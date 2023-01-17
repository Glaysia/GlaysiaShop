package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

import static glaysia.glaysiashop.GlaysiaShop.getEconomy;
import static glaysia.glaysiashop.GlaysiaShop.log;
import static java.lang.Math.abs;

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

        MyChestGui gui = new MyChestGui("구매완료 목록");
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
        DataIO dataIO=new DataIO();

        for(AmountSelector.MyPane i : myPanes){

            i.setOnClick(
                    inventoryClickEvent -> {
                        String info = i.order.toString();
                        String click = inventoryClickEvent.getClick().toString();
                        Trade itrade = new Trade();
                        String message="";

                        if(!click.equals("SHIFT_LEFT")){
                            message = "시프트 눌러서 한 번에 가져가셈\n"+info;
                            preventTakeItem(inventoryClickEvent, gui);
//                            addItemToInventoryWhenCancelTrade(i.order, getEconomy());
                            inventoryClickEvent.getInventory().close();
                            gui.show((HumanEntity) sender);
                        }else{
                            if(isThereSpace(inventoryClickEvent.getInventory().getContents(), inventoryClickEvent.getWhoClicked())){
                                addItemToInventoryWhenCancelTrade(i.order, getEconomy());
                                dataIO.setDoneOrderCompletedToDB(i.order);
                                i.clear();
                                gui.update();
                            }
                        }
                        sender.sendMessage(message+click);
                    }
            );
        }

        gui.show((HumanEntity) sender);
    }

    private boolean addItemToInventoryWhenCancelTrade(Trade.Order order, Economy econ){
        try{
            if(!order.is_selling) {
                PlayerInventory inventory = ((Player) sender).getInventory();
                ItemStack itemStack = new ItemStack(order.material);
                itemStack.setAmount(order.amount);
                inventory.addItem(itemStack);
            }
            return true;
        }
        catch (Exception e){
            sender.sendMessage(e.toString());
            return false;
        }
    }
    private void preventTakeItem(InventoryClickEvent inventoryClickEvent, MyChestGui gui) {
        inventoryClickEvent.getWhoClicked().closeInventory();
        gui.show(inventoryClickEvent.getWhoClicked());
    }
    private boolean isThereSpace(ItemStack[] itemStacks, HumanEntity playerForDebug){
        List<ItemStack> t = Arrays.asList(itemStacks);
        playerForDebug.sendMessage(t.toString());

        return t.contains(null);

//        return false;
    }
//    public boolean isThereSpace(Inventory inventory){
//        inventory.
//    }
}
