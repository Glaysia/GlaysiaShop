package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.concurrent.atomic.AtomicReference;

import static glaysia.glaysiashop.GlaysiaShop.log;

public class AmountSelector implements CommandExecutor {
    CommandSender sender=null;
    private ChestGui gui = new ChestGui(6, "거래요청 창 §8 R");
    private ItemPaletteGUI itemPaletteGUI=null;
    private OrderBookGui orderBookGui = null;
    private Material material=Material.STONE;
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, ChestGui gui, ItemPaletteGUI itemPaletteGUI, Material material){
        this.sender=sender;
        this.itemPaletteGUI=itemPaletteGUI;
        orderBookGui = new OrderBookGui("호가§8§lO§r창", material);
        if (sender instanceof Player) {

            this.gui=gui;
            this.material=material;
//            ChestGui gui =
            gui.show((HumanEntity) sender);

            //아이템
            ItemStack item = new ItemStack(material);
            OutlinePane itemPane = new OutlinePane(3, 2, 1, 1);
            itemPane.addItem(new GuiItem(item));
                gui.addPane(itemPane);

            itemPane.setOnClick(inventoryClickEvent -> {
                ((Player)sender).closeInventory();
                gui.show((HumanEntity) sender);
            });



            //라벨
            Label nump10        = new Label(6, 0, 1, 1, Font.BIRCH_PLANKS);
                nump10.setText("0");
                gui.addPane(nump10);
            Label dot           = new Label(5, 0, 1, 1, Font.BIRCH_PLANKS);
                dot.setText(".");
                gui.addPane(dot);
            Label num1          = new Label(4, 0, 1, 1, Font.BIRCH_PLANKS);
                num1.setText("0");
                gui.addPane(num1);
            Label num10         = new Label(3, 0, 1, 1, Font.BIRCH_PLANKS);
                num10.setText("0");
                gui.addPane(num10);
            Label num100        = new Label(2, 0, 1, 1, Font.BIRCH_PLANKS);
                num100.setText("0");
                gui.addPane(num100);
            Label num1000       = new Label(1, 0, 1, 1, Font.BIRCH_PLANKS);
                num1000.setText("1");
                gui.addPane(num1000);


            Label sign          = new Label(0, 0, 1, 1, Font.BIRCH_PLANKS);
                sign.setText("+");
                gui.addPane(sign);
            Label sell          = new Label(0,1,1,1,Font.BIRCH_PLANKS);
                sell.setText("S");
                gui.addPane(sell);



            Label backToPallete = new Label(2,5,1,1,Font.RED);
                backToPallete.setText("Q");
                gui.addPane(backToPallete);
            Label goToOrderBook = new Label(3,5,1,1,Font.STONE);
                goToOrderBook.setText("O");
                gui.addPane(goToOrderBook);


            Label confirm1      = new Label(4,5,1,1,Font.LIGHT_BLUE);
                confirm1.setText("C");
                gui.addPane(confirm1);
            Label confirm2_blue = new Label(5,5,1,1,Font.BLUE);
                confirm2_blue.setText("C");
                confirm2_blue.setVisible(false);
                gui.addPane(confirm2_blue);
            Label confirm2_gray = new Label(5,5,1,1,Font.LIGHT_GRAY);
                confirm2_gray.setText("C");
                gui.addPane(confirm2_gray);


            Label amount10      = new Label(4,2, 2,1, Font.BIRCH_PLANKS);
                amount10.setText("64");
                gui.addPane(amount10);
            MyLabel incrementItem16   = new MyLabel(4,3,1,1,Font.RED,"+", gui);
            MyLabel incrementItem1    = new MyLabel(5,3,1,1,Font.PINK,"+", gui);
            MyLabel decrementItem16   = new MyLabel(4,4,1,1,Font.BLUE,"-", gui);
            MyLabel decrementItem1    = new MyLabel(5,4,1,1,Font.LIGHT_BLUE,"-", gui);



            //클릭 이벤트
            backToPallete.setOnClick(
                    inventoryClickEvent -> {
                    ((Player)sender).closeInventory();
                    sender.sendMessage("아이템선택§8§lQ§r 창으로 돌아갑니다.");
                    itemPaletteGUI.show((HumanEntity) sender);
                }
            );

            goToOrderBook.setOnClick(
                    inventoryClickEvent -> {
                        ((Player)sender).closeInventory();
                        sender.sendMessage("호가§8§lO§r창으로 이동합니다.");
                        setGuiToOrderBook();
                        orderBookGui.show((HumanEntity) sender);
                    }
            );



            confirm2_blue.setOnClick(
                    inventoryClickEvent -> {
                        boolean isPlus=sign.getText().equals("+");
                        String sellOrBuy=(isPlus)?" 판매":" 구매";
                        double price = combinePlaceValues(num1000, num100, num10, num1, nump10, true);
                        int amount = Integer.parseInt(amount10.getText().toString());
//                        int amount = 64;
                        double pricePerAmount = price/amount;

                        Trade trade = new Trade(price, amount, sender.getName(), material, false);

                        sender.sendMessage("거래요청 확정됐습니다.");
                        confirm2_gray.setVisible(true);
                        confirm2_gray.setPriority(Pane.Priority.HIGH);
                        confirm2_blue.setVisible(false);
                        confirm2_blue.setPriority(Pane.Priority.LOW);

                        gui.update();
                    }
            );


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
            PlusMinusButton increment=new PlusMinusButton(true);
            PlusMinusButton decrement=new PlusMinusButton(false);
            double[] delta={0.1, 1, 10, 100, 1000};//자리수별 증감
            for(int i=0;i<5;i++){//자리수별 증가버튼
                int finalI = i;
                increment.button[i].setOnClick(event -> {

                    int num_arr[]=new int[5];

                    num_arr[0]=Integer.parseInt(num1000.getText());
                    num_arr[1]=Integer.parseInt(num100.getText());
                    num_arr[2]=Integer.parseInt(num10.getText());
                    num_arr[3]=Integer.parseInt(num1.getText());
                    num_arr[4]=Integer.parseInt(nump10.getText());
                    boolean isPlus=sign.getText().equals("+");

                    double num = combinePlaceValues(num_arr[0],num_arr[1],num_arr[2],num_arr[3],num_arr[4], isPlus);
//                    double num = combinePlaceValues(num1000, num100, num10, num1, nump10, isPlus);
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
                gui.addPane(increment.button[i]);
                gui.addPane(decrement.button[i]);
            }


            confirm1.setOnClick(
                    inventoryClickEvent -> {
                        boolean isPlus=sign.getText().equals("+");
                        String sellOrBuy=(isPlus)?" 판매":" 구매";
                        double price = combinePlaceValues(num1000, num100, num10, num1, nump10, true);
                        int amount = Integer.parseInt(amount10.getText().toString());
//                        int amount = 64;
                        double pricePerAmount = price/amount;

                        sender.sendMessage(
                                "거래요청 확정하시겠습니까?\n거래품목은"
                                        +material.toString()+
                                        " 거래수량은 "+
                                        Integer.toString(amount)+
                                        "개, 가격은 "+Double.toString(price) +
                                        "$, (개당 "+ Double.toString(pricePerAmount) +
                                        "$), 거래 유형은"+sellOrBuy+"입니다."
                        );
//

                        confirm2_gray.setVisible(false);
                        confirm2_gray.setPriority(Pane.Priority.LOW);
                        confirm2_blue.setVisible(true);
                        confirm2_blue.setPriority(Pane.Priority.HIGH);
                        gui.update();
                    }
            );


            incrementItem16.setOnClick(
                    inventoryClickEvent ->{
                        int amount=Integer.parseInt(amount10.getText().toString());
                        amount = (amount<=48)?(amount+16):64;
                        amount10.setText(Integer.toString(amount));
                        gui.update();
                    }
            );
            incrementItem1.setOnClick(
                    inventoryClickEvent ->{
                        int amount=Integer.parseInt(amount10.getText().toString());
                        amount = (amount<=63)?(amount+1):64;
                        amount10.setText(Integer.toString(amount));
                        gui.update();
                    }
            );
            decrementItem16.setOnClick(
                    inventoryClickEvent ->{
                        int amount=Integer.parseInt(amount10.getText().toString());
                        amount = (amount>=16)?(amount-16):0;
                        amount10.setText(Integer.toString(amount));
                        gui.update();
                    }
            );
            decrementItem1.setOnClick(
                    inventoryClickEvent ->{
                        int amount=Integer.parseInt(amount10.getText().toString());
                        amount = (amount>=1)?(amount-1):0;
                        amount10.setText(Integer.toString(amount));
                        gui.update();
                    }
            );


            gui.show((HumanEntity)sender);
//            sender.sendMessage(material.toString());

            return true;
        } else if (sender instanceof ConsoleCommandSender) {

            return false;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return onCommand(sender, command, label, args, gui, (ItemPaletteGUI) gui,  material);
    }

    private double combinePlaceValues(int thousands, int hundreds, int tens, int ones, int t, boolean isPlus) {
        double result=thousands*1000 + hundreds*100 + tens*10 + ones + t*0.1;
        return (isPlus)?(result):(-result);
    }
    private double combinePlaceValues(
            Label num1000,
            Label num100,
            Label num10,
            Label num1,
            Label nump10,
            boolean isPlus
    ) {
        int thousands=Integer.parseInt(num1000.getText());
        int hundreds=Integer.parseInt(num100.getText());
        int tens=Integer.parseInt(num10.getText());
        int ones=Integer.parseInt(num1.getText());
        int t=Integer.parseInt(nump10.getText());
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
    private void setGuiToOrderBook(){
        ItemStack item = new ItemStack(material);
        OutlinePane itemPane = new OutlinePane(4, 5, 1, 1);
        itemPane.addItem(new GuiItem(item));
        itemPane.setOnClick(inventoryClickEvent -> {
            ((Player)sender).closeInventory();
            orderBookGui.show((HumanEntity) sender);
        });


        Label backToPallete = new Label(2,5,1,1,Font.RED);
        Label goToAmountselector = new Label(3,5,1,1,Font.STONE);
        backToPallete.setText("Q");
        goToAmountselector.setText("R");

        backToPallete.setOnClick(
                inventoryClickEvent -> {
                    ((Player)this.sender).closeInventory();
                    sender.sendMessage("아이템선택§8§lQ§r 창으로 돌아갑니다.");
                    itemPaletteGUI.show((HumanEntity) sender);
                }
        );

        goToAmountselector.setOnClick(
                inventoryClickEvent -> {
                    ((Player)sender).closeInventory();
                    sender.sendMessage("거래요청§8§lR§r 창 으로 이동합니다.");
                    gui.show((HumanEntity) sender);
                }
        );

        orderBookGui.addPane(backToPallete);
        orderBookGui.addPane(goToAmountselector);
        orderBookGui.addPane(itemPane);

    }


    public class OrderBookGui extends ChestGui{
        private Material material=null;

        private OrderBookGui(String title, Material material){
            super(6, title);
            this.material=material;
        }
    }

    private class MyLabel extends Label{
        private MyLabel(int x, int y, int length, int height,Font font, String text, ChestGui gui){
            super(x,y,length,height,font);
            super.setText(text);
            gui.addPane(this);
        }
    }
    public class PlusMinusButton{
        public Label[] button = new Label[5];
        PlusMinusButton(boolean isPlus){
            Font font=(isPlus)?(Font.RED):(Font.GREEN);
            int x=(isPlus)?(8):(7);
            button[0]=new Label(x, 0, 1, 1, font);
            button[1]=new Label(x, 1, 1, 1, font);
            button[2]=new Label(x, 2, 1, 1, font);
            button[3]=new Label(x, 3, 1, 1, font);
            button[4]=new Label(x, 4, 1, 1, font);
            button[0].setText("t");
            button[1].setText("O");
            button[2].setText("T");
            button[3].setText("H");
            button[4].setText("M");

        }
    }
}