package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Nav implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) { //명령어를 사용자가 입력했으면

            ChestGui gui = new ChestGui(6, "Shop");
            PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
            pages.populateWithItemStacks(Arrays.asList(
                    new ItemStack(Material.GOLDEN_SWORD),
                    new ItemStack(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 16),
                    new ItemStack(Material.COOKED_COD, 64)
            ));
            pages.setOnClick(event -> {
                //buy item
            });
            gui.addPane(pages);

            OutlinePane background = new OutlinePane(0, 5, 9, 1);
            background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
            background.setRepeat(true);
            background.setPriority(Pane.Priority.LOWEST);

            gui.addPane(background);

            StaticPane navigation = new StaticPane(0, 5, 9, 1);
            navigation.addItem(new GuiItem(new ItemStack(Material.RED_WOOL), event -> {
                if (pages.getPage() > 0) {
                    pages.setPage(pages.getPage() - 1);

                    gui.update();
                }
            }), 0, 0);

            navigation.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL), event -> {
                if (pages.getPage() < pages.getPages() - 1) {
                    pages.setPage(pages.getPage() + 1);

                    gui.update();
                }
            }), 8, 0);

            navigation.addItem(new GuiItem(new ItemStack(Material.BARRIER), event ->
                    event.getWhoClicked().closeInventory()), 4, 0);

            gui.addPane(navigation);

            gui.show((HumanEntity)sender);
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
