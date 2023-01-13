package glaysia.glaysiashop;




import com.github.stefvanschie.inventoryframework.gui.GuiItem;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import static glaysia.glaysiashop.GlaysiaShop.itemPalette;
import static glaysia.glaysiashop.GlaysiaShop.tradingPlayer;


public class GlaysiaGui implements CommandExecutor {
    private CommandSender sender;
    private org.bukkit.command.Command command;
    private String label;
    private String[] args;
//    ItemPaletteGUI itemPalette=null;
    private Material material;
    private Economy econ;
    GlaysiaGui(Economy econ){

        this.econ=econ;
    }
    @Override
    /** 명령어 실행시 작동부 */
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.sender=sender;
        this.command=command;
        this.label=label;
        this.args=args;

        if (sender instanceof Player) { //명령어를 사용자가 입력했으면
            if(itemPalette==null){
                command();
            }
            else if(itemPalette.getViewers().size()!=0){
                try {
                    sender.sendMessage(tradingPlayer.getName()+"님이 거래중입니다. 잠시만 기다려주세요.");
                }catch (Exception e){
                    sender.sendMessage(e+"ㅎㅎ");
                }
            }else{
                command();
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

    void command(){
        tradingPlayer=(Player)sender;
        itemPalette = new ItemPaletteGUI.Builder("아이템선택§8§lQ§r 창")
                .show(Material::isBlock) //decide what items are displayed(e.g. flammable only)
                .show(Material::isItem) //decide what items are displayed(e.g. flammable only)
                .as(this::getDisplayItem) //how should the displayed materials look? Pass a Function<Material, GuiItem>
                .build();
        itemPalette.show((Player)sender);

    }


    /** 위 함수의 구현을 위해 필요함 */
    private GuiItem getDisplayItem(Object o) {
        return getDisplayItem((Material)o);
    }

    /** 거래요청 창으로 이동 */
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
            AmountSelector amountSelector=new AmountSelector(econ);
            player.closeInventory();

//            ((Player)sender).closeInventory();

            ChestGui gui = new ChestGui(6, "거래요청§8§lR§r 창");

            amountSelector.onCommand(sender, command, label, args, gui, itemPalette, material);

        });
    }


}
