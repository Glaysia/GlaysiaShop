package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.swing.event.HyperlinkEvent;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

public class AmountSelector implements CommandExecutor {
    private ChestGui gui =new ChestGui(4, "Select amount");
    private Material material=Material.STONE;
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, ChestGui gui, Material material){
        if (sender instanceof Player) {


            this.gui=gui;
            this.material=material;
//            ChestGui gui =
            gui.show((HumanEntity) sender);
            ItemStack item = new ItemStack(material);



//            Label decrement = new Label(2, 1, 1, 1, Font.OAK_PLANKS);
//            decrement.setText("-");
//            decrement.setVisible(false);


            PlusMinusButton increment=new PlusMinusButton(true);
            PlusMinusButton decrement=new PlusMinusButton(false);

            //라벨
            Label nump10        = new Label(6, 0, 1, 1, Font.BIRCH_PLANKS);
            Label dot           = new Label(5, 0, 1, 1, Font.BIRCH_PLANKS);
            Label num1          = new Label(4, 0, 1, 1, Font.BIRCH_PLANKS);
            Label num10         = new Label(3, 0, 1, 1, Font.BIRCH_PLANKS);
            Label num100        = new Label(2, 0, 1, 1, Font.BIRCH_PLANKS);
            Label num1000       = new Label(1, 0, 1, 1, Font.BIRCH_PLANKS);
            Label sign          = new Label(0, 0, 1, 1, Font.BIRCH_PLANKS);
            Label sell          = new Label(0,1,1,1,Font.BIRCH_PLANKS);
            Label backToPallete = new Label(0,1,1,1,Font.BIRCH_PLANKS);


            OutlinePane itemPane = new OutlinePane(3, 2, 1, 1);
            itemPane.addItem(new GuiItem(item));
            itemPane.setOnClick(inventoryClickEvent -> {
                ((Player)sender).closeInventory();
                gui.show((HumanEntity) sender);
            });

            nump10.setText("0");
            dot.setText(".");
            num1.setText("1");
            num10.setText("0");
            num100.setText("0");
            num1000.setText("0");
            sign.setText("+");
            sell.setText("S");

            double[] delta={0.1, 1, 10, 100};

//            if (item.getMaxStackSize() == 1) {
//                increment.setVisible(false);
//            }

//            decrement.setOnClick(event -> {
////                item.setAmount(item.getAmount() - 1);
//
//                int num_arr[]=new int[5];
//
//                num_arr[0]=Integer.parseInt(num1000.getText());
//                num_arr[1]=Integer.parseInt(num100.getText());
//                num_arr[2]=Integer.parseInt(num10.getText());
//                num_arr[3]=Integer.parseInt(num1.getText());
//                num_arr[4]=Integer.parseInt(nump10.getText());
//                double num = combinePlaceValues(num_arr[0],num_arr[1],num_arr[2],num_arr[3], num_arr[4]);
//
//                num--;
//                num_arr=doubleTo5DigitArray(num);
//
//                num1000.setText(Integer.toString(num_arr[0]));
//                num100.setText(Integer.toString(num_arr[1]));
//                num10.setText(Integer.toString(num_arr[2]));
//                num1.setText(Integer.toString(num_arr[3]));
//                nump10.setText(Integer.toString(num_arr[4]));
//
//
//                num1000.setVisible(true);
//                num100.setVisible(true);
//                num10.setVisible(true);
//                num1.setVisible(true);
//                nump10.setVisible(true);
//
////                if (item.getAmount() == 1) {
////                    decrement.setVisible(false);
////                }
//
//                incrementP1.setVisible(true);
//
//                gui.update();
//            });


            //1의 자리 증가
            //자리수별 증가버튼

            for(int i=0;i<4;i++){
                int finalI = i;
                increment.button[i].setOnClick(event -> {

                    int num_arr[]=new int[5];

                    num_arr[0]=Integer.parseInt(num1000.getText());
                    num_arr[1]=Integer.parseInt(num100.getText());
                    num_arr[2]=Integer.parseInt(num10.getText());
                    num_arr[3]=Integer.parseInt(num1.getText());
                    num_arr[4]=Integer.parseInt(nump10.getText());
                    boolean isPlus=sign.getText().equals("+");

                    double num =combinePlaceValues(num_arr[0],num_arr[1],num_arr[2],num_arr[3],num_arr[4], isPlus);
                    num = num + delta[finalI];
                    boolean isGreaterThanZero=(num>=0);

                    if(!isGreaterThanZero) {
                        sign.setText("-");
                        sell.setText("B");
                    }
                    else {
                        sign.setText("+");
                        sell.setText("S");
                    }

                    double numWillBeString=(isGreaterThanZero)?(num):(-num);

                    num_arr=doubleTo5DigitArray(numWillBeString);

                    num1000.setText(Integer.toString(num_arr[0]));
                    num100.setText(Integer.toString(num_arr[1]));
                    num10.setText(Integer.toString(num_arr[2]));
                    num1.setText(Integer.toString(num_arr[3]));
                    nump10.setText(Integer.toString(num_arr[4]));

                    num1000.setVisible(true);
                    num100.setVisible(true);
                    num10.setVisible(true);
                    num1.setVisible(true);
                    nump10.setVisible(true);

                    gui.update();


                });
                decrement.button[i].setOnClick(event -> {

                    int num_arr[]=new int[5];

                    num_arr[0]=Integer.parseInt(num1000.getText());
                    num_arr[1]=Integer.parseInt(num100.getText());
                    num_arr[2]=Integer.parseInt(num10.getText());
                    num_arr[3]=Integer.parseInt(num1.getText());
                    num_arr[4]=Integer.parseInt(nump10.getText());
                    boolean isPlus=sign.getText().equals("+");

                    double num =combinePlaceValues(num_arr[0],num_arr[1],num_arr[2],num_arr[3],num_arr[4], isPlus);
                    num = num - delta[finalI];
                    boolean isGreaterThanZero=(num>=0);

                    if(!isGreaterThanZero) {
                        sign.setText("-");
                        sell.setText("B");
                    }
                    else {
                        sign.setText("+");
                        sell.setText("S");
                    }

                    double numWillBeString=(isGreaterThanZero)?(num):(-num);

                    num_arr=doubleTo5DigitArray(numWillBeString);

                    num1000.setText(Integer.toString(num_arr[0]));
                    num100.setText(Integer.toString(num_arr[1]));
                    num10.setText(Integer.toString(num_arr[2]));
                    num1.setText(Integer.toString(num_arr[3]));
                    nump10.setText(Integer.toString(num_arr[4]));

                    num1000.setVisible(true);
                    num100.setVisible(true);
                    num10.setVisible(true);
                    num1.setVisible(true);
                    nump10.setVisible(true);

                    gui.update();


                });
            }



//            gui.addPane(decrement);
            gui.addPane(itemPane);
            gui.addPane(increment.button[0]);
            gui.addPane(increment.button[1]);
            gui.addPane(increment.button[2]);
            gui.addPane(increment.button[3]);

            gui.addPane(decrement.button[0]);
            gui.addPane(decrement.button[1]);
            gui.addPane(decrement.button[2]);
            gui.addPane(decrement.button[3]);

            gui.addPane(num1000);
            gui.addPane(num100);
            gui.addPane(num10);
            gui.addPane(num1);
            gui.addPane(dot);
            gui.addPane(nump10);
            gui.addPane(sign);
            gui.addPane(sell);


            gui.show((HumanEntity)sender);
            sender.sendMessage(material.toString());
            return true;
        } else if (sender instanceof ConsoleCommandSender) {

            return false;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return onCommand(sender, command, label, args, gui, material);
    }

    private double combinePlaceValues(int thousands, int hundreds, int tens, int ones, int t, boolean isPlus) {
        double result=thousands*1000 + hundreds*100 + tens*10 + ones + t*0.1;
        return (isPlus)?(result):(-result);
    }

    public static int[] doubleTo5DigitArray(double value) {
        int[] placeValues = new int[5];
        placeValues[0] = (int) value / 1000;
        placeValues[1] = (int) (value - placeValues[0] * 1000) / 100;
        placeValues[2] = (int) (value - placeValues[0] * 1000 - placeValues[1] * 100) / 10;
        placeValues[3] = (int) (value - placeValues[0] * 1000 - placeValues[1] * 100 - placeValues[2] * 10);
        double temp=value;
        temp=Math.round(temp*10);   //1.2 to 12
        placeValues[4] = (int) temp%10;

//        placeValues[4] = (int) ((value - (int) value) * 10);
        return placeValues;
    }
}