package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.*;

import static java.lang.Math.*;

public class AmountSelector implements CommandExecutor {
    public static Player tradingPlayer;
    public static ItemPaletteGUI itemPaletteGUI = null;
//    public static MyChestGui askOrderBookGui    = new MyChestGui("호가§8§lO§r창, 우클릭 거래");
//    public static MyChestGui myOrderListGui     = new MyChestGui("내 거래목록 §8§lM§r창 좌 정보출력, 우 거래취소");
//    public static MyChestGui askGui             = new MyChestGui("거래요청 창§8 R");
    public static PageGUI pageGUI;
    public static long time = new Date().getTime();

    public static boolean isNotTrading(){

        if(itemPaletteGUI==null||pageGUI==null){
            return true;
        }
        return itemPaletteGUI.getViewers().size()==0&&pageGUI.getViewers().size()==0;
    }

    CommandSender sender=null;
//    private ChestGui gui = new ChestGui(6, "거래요청 창 §8 R");
//    private ItemPaletteGUI itemPaletteGUI = null;
//    private OrderBookGui askOrderBookGui = null;
//    private OrderBookGui myOrderListGui = null;

    @NotNull
    private static Material material=Material.STONE;
    @NotNull
    private Economy econ = null;
    public AmountSelector(Economy econ){
        this.econ=econ;
    }

    //거래요청 창
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label, String[] args,
            Material material
    ){
        this.sender=sender;

        if (sender instanceof Player) {
            AmountSelector.material = material;
            sender.sendMessage(String.format("당신이 고른 것 %s!", AmountSelector.material));
            pageGUI = new PageGUI(itemPaletteGUI, (Player) sender, econ);
            pageGUI.main(material);

            return true;
        } else if (sender instanceof ConsoleCommandSender) {

            return false;
        }
        return false;
    }



    @Override
    public boolean onCommand(
            CommandSender sender,
            org.bukkit.command.Command command,
            String label, String[] args
    ) {
        return onCommand(sender, command, label, args, material);
    }

    public static class MyPane extends OutlinePane{
        @NotNull
        public Trade.Order  order;
        private ItemStack item;

        private MyPane(int x, int y, int length, int height, Material material, Trade.Order order, ChestGui gui, String showname){
            super(x,y,length, height);
            item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(showname);
            item.setItemMeta(itemMeta);
//            itemMeta.setLocalizedName("test");
            this.addItem(new GuiItem(item));
            this.order=order;
            gui.addPane(this);
            this.setVisible(true);
        }

        public MyPane(int x, int y, int length, int height, Material material, Trade.Order order, ChestGui gui){
            this(x,y,length,height,material,order,gui,material.name());
        }

        public void setAmount(int amount){
            this.item.setAmount(amount);
        }


    }


}