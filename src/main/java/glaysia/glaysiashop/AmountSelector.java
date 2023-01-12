package glaysia.glaysiashop;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.*;
import java.util.logging.Logger;

import static glaysia.glaysiashop.GlaysiaShop.log;
import static java.lang.Math.*;

public class AmountSelector implements CommandExecutor {

    CommandSender sender=null;
    private ChestGui gui = new ChestGui(6, "거래요청 창 §8 R");
    private ItemPaletteGUI itemPaletteGUI=null;
    private OrderBookGui orderBookGui = null;
    private OrderBookGui myOrderListGui = null;
    @NotNull
    private Material material=Material.STONE;
    @NotNull
    private Economy econ = null;
    public AmountSelector(Economy econ){
        this.econ=econ;
    }

    //거래요청 창
    public boolean onCommand(
            CommandSender sender,
            org.bukkit.command.Command command,
            String label, String[] args, ChestGui gui,
            ItemPaletteGUI itemPaletteGUI, Material material
    ){
        this.sender=sender;
        this.itemPaletteGUI=itemPaletteGUI;
        orderBookGui = new OrderBookGui("호가§8§lO§r창, 우클릭 거래", material);
        myOrderListGui = new OrderBookGui("내 거래목록 §8§lM§r창 우 거래취소, 좌 정보출력");

        if (sender instanceof Player) {

            this.gui=gui;
            this.material=material;
//            ChestGui gui =
            gui.show((HumanEntity) sender);

            //아이템
            ItemStack item = new ItemStack(material);
            OutlinePane itemPane = new OutlinePane(3, 2, 1, 1);

//            item.setItemMeta()

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
            MyLabel goToMyOrderList = new MyLabel(0,5,1,1,Font.JUNGLE_PLANKS, "M", gui);


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

            //자리수별 증가버튼
            PlusMinusButton increment=new PlusMinusButton(true);
            PlusMinusButton decrement=new PlusMinusButton(false);
            double[] delta={0.1, 1, 10, 100, 1000};//자리수별 증감
            for(int i=0;i<5;i++){//자리수별 증가버튼
                int finalI = i;
                increment.button[i].setOnClick(event -> {

                    int[] num_arr =new int[5];

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

                    int[] num_arr =new int[5];

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
            goToMyOrderList.setOnClick(
                    inventoryClickEvent -> {
                        ((Player)sender).closeInventory();
                        sender.sendMessage("내 거래목록 §8§lM§r창으로 이동합니다.");
                        setOrderList();;
                        myOrderListGui.show((HumanEntity) sender);
                    }
            );
            //거래요청
            confirm1.setOnClick(
                    inventoryClickEvent -> {
                        boolean isPlus=sign.getText().equals("+");
                        String sellOrBuy=(isPlus)?" 판매":" 구매";
                        double price = combinePlaceValues(num1000, num100, num10, num1, nump10, isPlus);
                        int amount = Integer.parseInt(amount10.getText());
//                        int amount = 64;
                        double pricePerAmount = price/amount;
                        double maxMoney = econ.getBalance((OfflinePlayer) sender);
                        boolean money_is_not_enough = (maxMoney<price);


                        String message="거래요청 확정하시겠습니까?\n거래품목은"+
                                        material.toString()+
                                        " 거래수량은 "+
                                        Integer.toString(amount)+
                                        "개, 가격은 "+Double.toString(price) +
                                        "$, (개당 "+ Double.toString(pricePerAmount) +
                                        "$), 거래 유형은"+sellOrBuy+"입니다.\n";



                        sender.sendMessage(message);
//
                        confirm2_gray.setVisible(false);
                        confirm2_gray.setPriority(Pane.Priority.LOW);

                        confirm2_blue.setVisible(true);
                        confirm2_blue.setPriority(Pane.Priority.HIGH);
                        gui.update();
                    }
            );
            //거래확정
            confirm2_blue.setOnClick(
                    inventoryClickEvent -> {
                        Trade trade_s = new Trade(econ);
                        boolean isPlus=sign.getText().equals("+");
                        String sellOrBuy=(isPlus)?" 판매":" 구매";
                        double price = combinePlaceValues(num1000, num100, num10, num1, nump10, isPlus);
                        int amount = Integer.parseInt(amount10.getText());

                        boolean money_is_enough_so_well_withdrawed;// = setPlayersMoneyWhenTrade(price, isPlus);;
                        boolean item_is_enough_so_well_subtracted;// = setPlayersInventoryWhenTrade(amount);
                        if(isPlus){
                            money_is_enough_so_well_withdrawed=false;
                            item_is_enough_so_well_subtracted=trade_s.subPlayersInventoryWhenTrade((Player) sender, material, amount);
                        }else{
                            money_is_enough_so_well_withdrawed=trade_s.setPlayersMoneyWhenTrade(sender,  price, isPlus);
                            item_is_enough_so_well_subtracted=false;
                        }

                        double pricePerAmount = price/amount;
                        Trade nullTrade = new Trade();
                        String message = isPlus?"아이템이 부족합니다":"돈이 부족합니다.";

                        if(nullTrade.getAvailOrderNumberPerUser(sender.getName())>44){
                            sender.sendMessage("현재 거래 개수 제한이 44개입니다.추후에 늘릴게요");
                        }
                        else {
                            if(isPlus){//판매
                                if(item_is_enough_so_well_subtracted) {
                                    Trade trade = new Trade(price, amount, sender.getName(), material, false);
                                    sender.sendMessage("거래요청 확정됐습니다.");
                                }
                                else {
                                    sender.sendMessage(message);
                                }
                            }else{  //구매
                                if(money_is_enough_so_well_withdrawed){
                                    Trade trade = new Trade(price, amount, sender.getName(), material, false);
                                    sender.sendMessage("거래요청 확정됐습니다.");
                                }else{
                                    sender.sendMessage(message);
                                }
                            }

                        }

                        confirm2_gray.setVisible(true);
                        confirm2_gray.setPriority(Pane.Priority.HIGH);
                        confirm2_blue.setVisible(false);
                        confirm2_blue.setPriority(Pane.Priority.LOW);

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



    private boolean addItemToInventoryWhenCancelTrade(Trade.Order order){
        try{
            if(order.is_selling){
                PlayerInventory inventory = ((Player)sender).getInventory();
                ItemStack itemStack = new ItemStack(order.material);
                itemStack.setAmount(order.amount);
                inventory.addItem(itemStack);
            }else{
                double price=abs(order.price);
                econ.depositPlayer((OfflinePlayer) sender, price);
            }
            return true;
        }
        catch (Exception e){
            sender.sendMessage(e.toString());
            return false;
        }
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            org.bukkit.command.Command command,
            String label, String[] args
    ) {
        return onCommand(sender, command, label, args, gui, (ItemPaletteGUI) gui,  material);
    }


    //호가창
    private void setGuiToOrderBook(){
        ItemStack item = new ItemStack(material);
        //입력받은 아이템 출력 Pane
        OutlinePane itemPane = new OutlinePane(4, 5, 1, 1);
        itemPane.addItem(new GuiItem(item));
        itemPane.setOnClick(inventoryClickEvent -> {
            ((Player)sender).closeInventory();
            orderBookGui.show((HumanEntity) sender);
        });

        AskPrice askPrice = new AskPrice();


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
    //내 거래요청 리스트
    private void setOrderList(){
//        ItemStack item = new ItemStack(material);
//        OutlinePane itemPane = new OutlinePane(4, 5, 1, 1);
//        itemPane.addItem(new GuiItem(item));
//        itemPane.setOnClick(inventoryClickEvent -> {
//            ((Player)sender).closeInventory();
//            orderBookGui.show((HumanEntity) sender);
//        });

        Trade trade = new Trade(sender.getName());
        List<Trade.Order> myList = trade.getList();
        sender.sendMessage("mylist: "+myList.toString());
        MyLabel backToPallete = new MyLabel(2,5,1,1,Font.RED, "Q", myOrderListGui);
        MyLabel goToAmountselector = new MyLabel(3,5,1,1,Font.STONE, "R", myOrderListGui);
        List<MyPane> myPanes = new ArrayList<>();
//        sender.sendMessage("myPanes: "+);
        int idx=0;
        int max_idx=myList.toArray().length;

        for(int row=0;row<5;row++){
            for(int col=0;col<9;col++){
                if(idx==max_idx)
                    break;
                myPanes.add(new MyPane(col,row,1,1,(myList.get(idx).material), myList.get(idx), myOrderListGui));
                idx++;
            }
            if(idx==max_idx)
                break;
        }

        //거래취소
        for(MyPane pane : myPanes){
            pane.setOnClick(
                    inventoryClickEvent -> {
                        String inf=pane.order.toString();
                        String c = inventoryClickEvent.getClick().toString();
                        Trade itrade= new Trade();
                        String message;

                        if(c.equals("LEFT")){
                            message="우클릭을 누르면 거래가 취소됩니다.\n"+inf;
//                            sender.sendMessage(message);
                        }else if(c.equals("RIGHT")){
                            message="§d§l거래취소됐습니다.§r 좌클릭을 누르면 거래정보만 출력합니다.\n"+""+inf;
//                            sender.sendMessage(message);
                            itrade.setOrderCanceled(pane.order.order_id);
                            pane.setVisible(false);
                            pane.clear();
                            myOrderListGui.update();
                            addItemToInventoryWhenCancelTrade(pane.order);
                        }else{
                            message="우클릭은 거래취소, 좌클릭은 정보출력,\n";
                        }

                        sender.sendMessage(message);
                    }
            );
        }
//        backToPallete.setText("Q");
//        goToAmountselector.setText("R");

        backToPallete.setOnClick(
                inventoryClickEvent -> {
                    ((Player)this.sender).closeInventory();
                    sender.sendMessage("아이템선택§8§lQ§r 창으로 돌아갑니다.\n");
                    itemPaletteGUI.show((HumanEntity) sender);
                }
        );

        //거래요청창
        goToAmountselector.setOnClick(
                inventoryClickEvent -> {
                    ((Player)sender).closeInventory();
                    sender.sendMessage("거래요청§8§lR§r 창 으로 이동합니다.\n");
                    gui.show((HumanEntity) sender);
                }
        );

//        myOrderList.addPane(backToPallete);
//        myOrderList.addPane(goToAmountselector);
//        orderBookGui.addPane(itemPane);
    }

    //호가창, 거래요청창 내가 만든 클래스
    public static class OrderBookGui extends ChestGui{
        private Material material=null;

        OrderBookGui(String title, Material material){
            super(6, title);
            this.material=material;
        }
        OrderBookGui(String title){
            super(6, title);
        }
    }

    //라벨 내가 만든 클래스
    private class MyLabel extends MyLabel_v2{
        private MyLabel(int x, int y, int length, int height,Font font, String text, ChestGui gui, String showName){
            super(x,y,length,height,font);
            super.setText(text, showName);
            gui.addPane(this);
        }
        private MyLabel(int x, int y, int length, int height,Font font, String text, ChestGui gui){
            super(x,y,length,height,font);
            super.setText(text);
            gui.addPane(this);
        }
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
        public MyPane(int x, int y, int length, int height, Material material,  ChestGui gui){
            this(x,y,length,height,material,null,gui,material.name());
        }
        public void setAmount(int amount){
            this.item.setAmount(amount);
        }

        private void setOrder(Trade.@NotNull Order order){
            this.order=order;
        }

        private void setShowname(String showname){
//            this.clear();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(showname);
            item.setItemMeta(itemMeta);
//            this.addItem(new GuiItem(item));
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
    //    호가창에서 아이템 출력
    private class AskPrice{
        List<Trade.Order> orderList;
        List<Trade.Order> orderList_sellOnly;
        List<Trade.Order> orderList_buyOnly;
        List<MyPane> orderPanes_sellOnly = new LinkedList<>();
        List<MyPane> orderPanes_buyOnly = new LinkedList<>();
        private final Integer[] sellXArray = {3,2,1, 0,0,0,0, 1,2,3, 3,2,1,0};
        private final Integer[] sellYArray = {0,0,0, 0,1,2,2, 2,2,3, 4,4,4,4};
        private final Integer[] buyXArray = {5,6,7, 8,8,8,7, 6,5,5, 5,6,7,8};
        private final Integer[] buyYArray = {0,0,0, 0,1,2,2, 2,2,3, 4,4,4,4};
        AskPrice(){
            Trade trade = new Trade(material, (Player) sender);
            orderList = trade.getList();

//            material
//            orderBookGui
////            int lastOrder_id = dataIO.getLastOrder();

            this.setSellBuyOrder();
            this.sortByPrice();
            sender.sendMessage("orderList_sellOnly: "+orderList_sellOnly.toString());
            sender.sendMessage("orderList_buyOnly: "+orderList_buyOnly.toString());
            
            for(int i=0;i<14;i++){
                orderPanes_sellOnly.add(new MyPane(sellXArray[i], sellYArray[i], 1,1, material, null, orderBookGui, "test"));
                orderPanes_buyOnly.add(new MyPane(buyXArray[i], buyYArray[i], 1,1, material, null, orderBookGui));
            }

            for(int i=0;i<14;i++){
                if(i<orderList_sellOnly.size()){
                    orderPanes_sellOnly.get(i).setOrder(orderList_sellOnly.get(i));
                    orderPanes_sellOnly.get(i).setShowname(orderList_sellOnly.get(i).toString());
                }else{
                    orderPanes_sellOnly.get(i).setVisible(false);
                }

                if(i<orderList_buyOnly.size()){
                    orderPanes_buyOnly.get(i).setOrder(orderList_buyOnly.get(i));
                    orderPanes_buyOnly.get(i).setShowname(orderList_buyOnly.get(i).toString());
                }else{
                    orderPanes_buyOnly.get(i).setVisible(false);
                }
            }

            orderBookGui.update();
            for(MyPane i : orderPanes_sellOnly){
                i.setOnClick(
                        inventoryClickEvent -> {
                            String info = i.order.toString();
                            String click = inventoryClickEvent.getClick().toString();
                            String message;
                            Trade i_trade = new Trade(sender, i.order, econ);

                            if(click.equals("LEFT")){
                                message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
                            }else if(click.equals("RIGHT")){

                                if(i_trade.enoughMoney((Player)sender)) {
                                    message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                                }else{
                                    message = "돈이 모자란데요?";
                                }

                                i.setVisible(false);
                                i.clear();
                                orderBookGui.update();
                            }else{
                                message="우클릭은 거래승낙, 좌클릭은 정보출력";
                            }
                            sender.sendMessage(message);
                        }
                );
            }

            for(MyPane i : orderPanes_buyOnly){
                i.setOnClick(
                        inventoryClickEvent -> {
                            String info = i.order.toString();
                            String click = inventoryClickEvent.getClick().toString();
                            String message;
                            Trade i_trade = new Trade(sender, i.order, econ);

                            if(click.equals("LEFT")){
                                message="거래요청에 응하시겠습니까? 우클릭을 누르면 거래완료됩니다."+info;
                            }else if(click.equals("RIGHT")){

                                if(i_trade.enoughItem((Player)sender)) {
                                    message = "거래가 완료됐습니다. 환불은 안됨 " + info;
                                }else{
                                    message = "아이템이 모자란데요?";
                                }

                                i.setVisible(false);
                                i.clear();
                                orderBookGui.update();
                            }else{
                                message="우클릭은 거래승낙, 좌클릭은 정보출력";
                            }
                            sender.sendMessage(message);
                        }
                );
            }

        }

        public void filteringByMaterial(){
            orderList.removeIf(i -> !(i.material.equals(material)));
        }

        private void setSellBuyOrder(){
            orderList_sellOnly=new ArrayList<>(orderList);
            orderList_sellOnly.removeIf(i -> !(i.is_selling));
            orderList_buyOnly=new ArrayList<>(orderList);
            orderList_buyOnly.removeIf(i -> (i.is_selling));
        }

        private void sortByPrice(){
            sortOrdersByPrice();
        }

        public void sortOrdersByPrice() {

            if(orderList_sellOnly.size()!=0)
                QuickSort.quickSort(orderList_sellOnly, 0, orderList_sellOnly.size() - 1);

            if(orderList_buyOnly.size()!=0)
                QuickSort.quickSort(orderList_buyOnly, 0, orderList_buyOnly.size() - 1);
        }
        private class QuickSort {
            public static void quickSort(List<Trade.Order> orders, int left, int right) {
                int i = left, j = right;
                double pivot = orders.get((left + right) / 2).pricePerAmount;
                while (i <= j) {
                    while (orders.get(i).pricePerAmount < pivot) {
                        i++;
                    }
                    while (orders.get(j).pricePerAmount > pivot) {
                        j--;
                    }
                    if (i <= j) {
                        swap(orders, i, j);
                        i++;
                        j--;
                    }
                }
                if (left < j) {
                    quickSort(orders, left, j);
                }
                if (i < right) {
                    quickSort(orders, i, right);
                }
            }

            private static void swap(List<Trade.Order> orders, int i, int j) {
                Trade.Order temp = orders.get(i);
                orders.set(i, orders.get(j));
                orders.set(j, temp);
            }
        }
    }
}