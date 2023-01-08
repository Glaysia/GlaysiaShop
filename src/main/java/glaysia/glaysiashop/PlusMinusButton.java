package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.pane.component.Label;

public class PlusMinusButton{
    public Label[] button = new Label[4];
    PlusMinusButton(boolean isPlus){
        Font font=(isPlus)?(Font.RED):(Font.GREEN);
        int x=(isPlus)?(8):(7);
        button[0]=new Label(x, 0, 1, 1, font);
        button[1]=new Label(x, 1, 1, 1, font);
        button[2]=new Label(x, 2, 1, 1, font);
        button[3]=new Label(x, 3, 1, 1, font);
        button[0].setText("t");
        button[1].setText("O");
        button[2].setText("T");
        button[3].setText("H");
    }
}