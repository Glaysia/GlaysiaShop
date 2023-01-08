package glaysia.glaysiashop;




import com.github.stefvanschie.inventoryframework.gui.GuiItem;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


import java.util.Arrays;


public class GlaysiaGui implements CommandExecutor {
    private CommandSender sender;
    private org.bukkit.command.Command command;
    private String label;
    private String[] args;
    ItemPaletteGUI itemPalette=null;
    private Material material;
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.sender=sender;
        this.command=command;
        this.label=label;
        this.args=args;

        if (sender instanceof Player) { //명령어를 사용자가 입력했으면
            itemPalette = new ItemPaletteGUI.Builder("Choose an Item:")
                    .show(Material::isFlammable) //decide what items are displayed(e.g. flammable only)
                    .as(this::getDisplayItem) //how should the displayed materials look? Pass a Function<Material, GuiItem>
                    .build();

            itemPalette.show((Player)sender);

            return true;
        }
        else if (sender instanceof ConsoleCommandSender) {
            //콘솔창에서 사용한 경우
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return false;
        }
        return false;
    }

    private GuiItem getDisplayItem(Object o) {
        return getDisplayItem((Material)o);
    }

    private GuiItem getDisplayItem(Material material)
    {


        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + material.name());
        item.setItemMeta(meta);

        return new GuiItem(item, event ->
        {
            Player player = (Player) event.getWhoClicked();
//            player.getInventory().addItem(item);
            player.sendMessage(String.format(ChatColor.BLUE + "당신이 고른 것 %s!", material));
            AmountSelector amountSelector=new AmountSelector();
            player.closeInventory();

            ((Player)sender).closeInventory();

            ChestGui gui = new ChestGui(4, "Select amount");
            amountSelector.onCommand(sender, command, label, args, gui, material);
//            gui.show((Player)sender);

//            itemPalette.update();
        });
    }

}
