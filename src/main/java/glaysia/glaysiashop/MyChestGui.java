package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static glaysia.glaysiashop.AmountSelector.time;

public class MyChestGui extends ChestGui {
    MyChestGui(int rows, @NotNull String title){
        super(rows, title);
    }
    MyChestGui(String title){
        super(6, title);
    }
    public void show(@NotNull HumanEntity humanEntity){
        time=new Date().getTime();
        super.show(humanEntity);
    }
    public void update(){
        time=new Date().getTime();
        super.update();
    }
    public void update(Player player, Economy econ){
        player.sendMessage("잔고는 "+econ.getBalance(player));
        this.update();
    }
}
