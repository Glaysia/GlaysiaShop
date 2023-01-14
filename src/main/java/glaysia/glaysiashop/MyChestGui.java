package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static glaysia.glaysiashop.AmountSelector.time;

public class MyChestGui extends ChestGui {
    MyChestGui(int rows, @NotNull String title){
        super(rows, title);
    }
    public void show(@NotNull HumanEntity humanEntity){
        time=new Date().getTime();
        super.show(humanEntity);
    }

    public void update(){
        time=new Date().getTime();
        super.update();
    }
}
