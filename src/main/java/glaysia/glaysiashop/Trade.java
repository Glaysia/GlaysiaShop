package glaysia.glaysiashop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import static glaysia.glaysiashop.GlaysiaShop.log;
import static java.lang.Math.abs;

public class Trade {
    private static int order_id=0;          //거래번호
    public static class Order{                         //주문정보를 담고있는 클래스
        public Order(int order_id, Date date, double price, int amount, String trader, Material material){
            this.order_id=order_id;
            this.date=date;
            this.price=price;
            this.amount=amount;
            this.pricePerAmount=price/amount;
            this.trader=trader;
            this.material=material;
            this.is_selling=(price>=0);
        }
        public Order(int order_id, Date date, double price, int amount, double pricePerAmount, String trader, Material material){
            this.order_id=order_id;
            this.date=date;
            this.price=price;
            this.amount=amount;
            this.pricePerAmount=pricePerAmount;
            this.trader=trader;
            this.material=material;
            this.is_selling=(price>=0);
        }

        public Order(int order_id, Date date, double price, int amount, double pricePerAmount, String trader, Material material, boolean is_selling, boolean is_canceled, boolean is_complete, boolean is_there_error){
            this.order_id=order_id;
            this.date=date;
            this.price=price;
            this.amount=amount;
            this.pricePerAmount=pricePerAmount;
            this.trader=trader;
            this.material=material;
            this.is_selling=(price>=0);
            this.is_canceled = is_canceled;
            this.is_completed = is_complete;
            this.is_there_error = is_there_error;
        }

        public final int order_id;            //거래번호 불변
        public final Date date;                 //거래시간 불변
        public final double price;               //가격 음수면 구매요청, 양수면 판매요청
        public final int amount;                   //개수
        public final double pricePerAmount;
        public final String trader;             //주문자 이름
        public final Material material;         //아이템
        public final boolean is_selling;             //true면 판매요청, false면 구매요청
        public boolean is_canceled;            //취소됐는지 여부
        public boolean is_completed;           //거래완료됐는지 여부
        public boolean is_there_error;         //각 단계에서, 플레이어에게 아이템, 금액 등이 제대로 정산됐는지 여부
        public boolean isMadeBy(String trader){
            return (this.trader).equals(trader);
        }
        public boolean isMaterial(Material material){
            return (this.material).equals(material);
        }
        //get, set 메소드 구현해야함
        public void printThisOrder(Player player){
            player.sendMessage(this.toString());
        }
        public String toString(){
            String sellOrBuy = is_selling?" 판매요청":" 구매요청";
            return "주문자 이름: "+trader+" 거래품목: "+material.toString()+" 가격: "+Double.toString(price)+" 개수: "+Integer.toString(amount) + " 개당 가격: "+Double.toString(pricePerAmount)+sellOrBuy+"입니다.";
        }
    }

    private boolean is_saved;


    public Trade(double price, int amount, String trader, Material material, boolean was_there_error){
        String fileName="./plugins/glaysiashop/market.yml";
        File dataFile = new File(fileName);
        FileConfiguration dataFileConfig = YamlConfiguration.loadConfiguration(dataFile);

        DataIO dataIO = new DataIO();
        order_id = dataIO.getLastOrder()+1;
        Order order = new Order(
                order_id,
                new Date(),
                price,
                amount,
                trader,
                material
        );

        is_saved = dataIO.writeOrderToDB(order);

    }


    private boolean saveToDB(Map<String, Object> mainDataMap, File dataFile){
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dataFile), "UTF-8")) {
            yaml.dump(mainDataMap, writer);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        //return someFunction(order); //DB에 저장에 성공했다면 true를 반환
        return true;
    }

    public boolean isSaved(){
        return is_saved;
    }
    private Order ReadFromDB(int order_id){

        DataIO dataIO= new DataIO();

        Order outOrder = null;
        outOrder=dataIO.getOrder(order_id);

        return outOrder;
    }

    Trade(int order_id){
        Order order = null;
        order=ReadFromDB(order_id);       //주문번호를 통해 DB에 접근하여 데이터를 꺼내옴
    }
    private List<Order> list=new LinkedList<>();;
    public Trade(String traderName){
        DataIO dataIO = new DataIO();
//        this.list = new ArrayList<>();

        int last_order=dataIO.getLastOrder();
//        for(int i=1;i<=last_order;i++){
//            this.list.add(dataIO.getOrder(i));
//        }


        List<Integer> list_idx_for_remove = new ArrayList<>();
        int idx=0;
        this.list.removeIf(i -> (
                ((!i.isMadeBy(traderName)) || i.is_canceled || i.is_completed || i.is_there_error)
        ));

    }
    public void setOrderListToBeDone(String traderName){
        DataIO dataIO = new DataIO();
//        int last_done_order=dataIO.getLastDoneOrder();
//        for(int i=1;i<=last_done_order;i++){
//            this.list.add(dataIO.getDoneOrder(i));
//        }
//
        List<Trade.Order> list = dataIO.getDoneOrderList();

        list.removeIf(i -> (
                (!i.isMadeBy(traderName)|| i.is_canceled || i.is_completed || i.is_there_error)
        ));

        for(Trade.Order i : this.list){
            doneMessage=doneMessage+" "+i.toString()+"\n";
        }
    }
    String doneMessage="지금까지 완료된 구매주문:\n";

    public Trade(Material material, Player playerForDebug){
        DataIO dataIO = new DataIO();
//        this.list = new ArrayList<>();

        int last_order=dataIO.getLastOrder();
        Map<String, Object> orderList = DataIO.YmlReader.readYml( "./plugins/glaysiashop/market.yml");
        orderList = (Map<String, Object>) orderList.get("glaysiashop");
        orderList = (Map<String, Object>) orderList.get("Order");
        for(int i=1;i<=last_order;i++){
            Map<String, Object> spOrder = (Map<String, Object>) orderList.get(String.valueOf(i));
            this.list.add(new Trade.Order(i, (Date)spOrder.get("date"), (Double)spOrder.get("price"), (int)spOrder.get("amount"), (Double)spOrder.get("price_per_amount"), (String)spOrder.get("trader"), (Material) spOrder.get("material"), (Boolean)spOrder.get("is_selling"),
                    (Boolean)spOrder.get("is_canceled"), (Boolean)spOrder.get("is_complete"), (Boolean)spOrder.get("is_there_error")));
        }


        List<Integer> list_idx_for_remove = new ArrayList<>();
        int idx=0;
        this.list.removeIf(i -> (
                ((!i.isMaterial(material)) || i.is_canceled || i.is_completed || i.is_there_error)
        ));
//        playerForDebug.sendMessage("orderList: "+list.toString());
    }

    public int getAvailOrderNumberPerUser(String userName){
        DataIO dataIO = new DataIO();
        int last=dataIO.getLastOrder();
        for(int i=1;i<=last;i++){
            list.add(dataIO.getOrder(i));
        }
        int res=0;
        for(Order i : list){
            if(i.isMadeBy(userName)) {
                if(i.is_canceled || i.is_completed || i.is_there_error){
                    res=res+0;
                }else{
                    res=res+1;
                }
            }

        }
        return res;
    }
    public Trade(){

    }
    public Trade(Economy econ){
        this.econ=econ;
    }
    private OfflinePlayer seller;
    private OfflinePlayer buyer;
    private CommandSender sender;
    private Order order;
    private Economy econ;

    public Trade(CommandSender player, Order order, Economy econ){
        sender=player;
        Player o = player.getServer().getPlayer(order.trader);
        this.order=order;
        this.econ=econ;

        if(order.is_selling){
            buyer=(OfflinePlayer) player;
            seller= (OfflinePlayer) o;
        }else{
            buyer=(OfflinePlayer) o;
            seller= (OfflinePlayer) player;
        }

    }
    public boolean enoughMoney(HumanEntity player){
        boolean money_is_enough_so_well_withdrawed;
        boolean money_is_well_paid;
        // 판매요청에 구매할 때 판매자가 돈을 받고 구매자가 돈을 내야함.
        boolean item_is_enough_so_well_subtracted;
        boolean item_is_well_delivered;
        DataIO dataIO = new DataIO();
        item_is_enough_so_well_subtracted = true;

        //A는 seller B는 buyer
        //판매요청 시 A는 아이템을 뺏김
        //요청수락 시 A는 돈을 받음   B는 돈을 뺏김 아이템을 받음 << 이게 중요

        //B에게 돈이 있는가? 있으면 뺏어라.  OK
        //B에게 돈이 있고 뺏었는가? 뺏엇으면 아이템을 줘라 OK
        //그러면 A에게 돈을 줘라     OK

        money_is_enough_so_well_withdrawed = setPlayersMoneyWhenTrade(buyer, order.price, order.is_selling);

        if(money_is_enough_so_well_withdrawed){
            ItemStack itemStack = new ItemStack(order.material);
            itemStack.setAmount(order.amount);
            boolean tmp;
            try {
                econ.depositPlayer(seller, abs(order.price));
                tmp=true;
            }catch (Exception e){
                log.info(e.toString());
                tmp=false;
            }
            money_is_well_paid=tmp;
            item_is_well_delivered=moveOrderToDone(dataIO);
        }else{
            money_is_well_paid=false;
            item_is_well_delivered=false;
        }

        return money_is_well_paid&&money_is_enough_so_well_withdrawed&&item_is_enough_so_well_subtracted&&item_is_well_delivered;

    }

    public boolean enoughItemSoPaid(HumanEntity player){
        //A는 buyer B는 seller
        //구매요청 시 A는 돈을 뻇김
        //요청수락 시 A는 아이템을 받음, B는 아이템을 뺏김 돈을 받음 << 이게 중요

        //B에게 아이템이 있는가? 있으면 뺏어라 OK
        //B에게서 아이템이 있고 뺏었는가? 뺏었으면 돈을 줘라 OK
        //그러면 A에게 아이템을 줘라 <= 오프라인이면 못주니까 일단 데이터베이스에 저장을 먼저 하고 O/ 동시에 알림을 보낸다.
        //알림 받은 사람이 명령어를 통해 받을 수 있는 구조.
        //플레이어가 들어오면 그 때 내역 전달, 명령어로 받을 수 있게 하기
        boolean money_is_enough_so_well_withdrawed;
        boolean money_is_well_paid;
        // 판매요청에 구매할 때 판매자가 돈을 받고 구매자가 돈을 내야함.
        boolean item_is_enough_so_well_subtracted;
        boolean item_is_well_delivered;
        DataIO dataIO = new DataIO();

        money_is_enough_so_well_withdrawed = true;
        item_is_enough_so_well_subtracted = subPlayersInventoryWhenTrade(seller, order.material, order.amount);
        boolean tmp;
        if(item_is_enough_so_well_subtracted){
            econ.depositPlayer(seller, abs(order.price));
            tmp=true;
        }else {
            tmp=false;
        }

        boolean buyer_is_online = sender.getServer().getOnlinePlayers().contains(buyer);
        if(buyer_is_online)
            buyer.getPlayer().sendMessage("거래완료!! 누군가 당신의 구매요청에 응했습니다."+order.toString());

        money_is_well_paid=tmp;
        item_is_well_delivered=moveOrderToDone(dataIO);

        return money_is_well_paid&&money_is_enough_so_well_withdrawed&&item_is_enough_so_well_subtracted&&item_is_well_delivered;
    }
    public boolean moveOrderToDone(DataIO dataIO){
        order.is_completed=true;
        dataIO.writeOrderToDB(order);
        order.is_completed=false;
        return dataIO.writeDoneOrderToDB(order);
    }

    /*
    public boolean enoughMoney_Item(HumanEntity player) {
        boolean money_is_enough_so_well_withdrawed;
        boolean money_is_well_paid;
        // 판매요청에 구매할 때 판매자가 돈을 받고 구매자가 돈을 내야함.
        boolean item_is_enough_so_well_subtracted;
        boolean item_is_well_delivered;
        DataIO dataIO = new DataIO();
        // 구매요청에 판매할 때 판매자가 돈을 받고 아이템을 내야함, 구매자는 아이템을 받아야함.
        if(order.is_selling){//A는 seller B는 buyer
            //판매요청 시 A는 아이템을 뺏김
            //요청수락 시 A는 돈을 받음   B는 돈을 뺏김 아이템을 받음 << 이게 중요

            //B에게 돈이 있는가? 있으면 뺏어라.  OK
            //B에게 돈이 있고 뺏었는가? 뺏엇으면 아이템을 줘라 OK
            //그러면 A에게 돈을 줘라     OK
            item_is_enough_so_well_subtracted = true;

            money_is_enough_so_well_withdrawed = setPlayersMoneyWhenTrade(buyer, order.price, order.is_selling);
            if(money_is_enough_so_well_withdrawed){
                Inventory inventory = buyer.getPlayer().getInventory();
                ItemStack itemStack = new ItemStack(order.material);
                itemStack.setAmount(order.amount);
                boolean tmp;
                try {
                    econ.depositPlayer(seller, abs(order.price));
                    tmp=true;
                    inventory.addItem(itemStack);
                    item_is_well_delivered=true;
                }catch (Exception e){
                    log.info(e.toString());
                    tmp=false;
//                    return false;
                }
                money_is_well_paid=tmp;
            }else{
                money_is_well_paid=false;
            }

        }else{
        }

        //공통
        order.is_completed=true;
        dataIO.writeOrderToDB(order);
        order.is_completed=false;
        item_is_well_delivered=dataIO.writeDoneOrderToDB(order);

        return (money_is_enough_so_well_withdrawed&&money_is_well_paid&&item_is_enough_so_well_subtracted&&item_is_well_delivered);
    }
    */


    public boolean setPlayersMoneyWhenTrade(CommandSender sender,  double price, boolean isSell) {
        return setPlayersMoneyWhenTrade((OfflinePlayer) sender, price, isSell);
    }

    public boolean setPlayersMoneyWhenTrade(OfflinePlayer sender,  double price, boolean isSell) {
        double maxMoney =  econ.getBalance(sender);
        boolean money_is_enough = (maxMoney>=abs(price));

        if(money_is_enough){//구매요청일 때, 그리고 돈이 충분할 때
            econ.withdrawPlayer((OfflinePlayer)sender, abs(price));
            return true;
        }else{
            return false;
        }
    }

    public boolean subPlayersInventoryWhenTrade(OfflinePlayer sender,Material material, int amount) {
        return subPlayersInventoryWhenTrade(this.sender.getServer().getPlayer(sender.getName()), material, amount);
//        return true;
    }

    public boolean subPlayersInventoryWhenTrade(Player sender,Material material,int amount){
            PlayerInventory inventory =  (sender).getInventory();
            ItemStack[] itemStacks = inventory.getStorageContents();
            List<ItemStack> temp=Arrays.asList(itemStacks);
            List<ItemStack> itemStackList =new ArrayList<>(temp);

            if(inventory.contains(material, amount)) {
                int maxAmount = 0;
//                        maxAmount=maxAmount+i.getAmount();
                for (ItemStack i : itemStacks) {
                    if ((i!=null)&&i.getType().equals(material)) {
                        maxAmount += i.getAmount();
                        itemStackList.remove(i);
                        itemStackList.add(null);
                    }
                }

                int afterAmount = maxAmount-amount;
                Integer [] arrAmount = this.divideBy64(afterAmount);

                if(arrAmount.length>this.numberOfNull(itemStackList)){
                    return false;
                }

                //null인 곳에 set하자. null인 곳의 idx를 받아오는 함수를 만들자.
                // = new ItemStack(material);
                Integer[] nullIdx=this.getNullIdx(itemStackList);
                for(int i=0;i<arrAmount.length;i++){
                    ItemStack add=new ItemStack(material);
                    add.setAmount(arrAmount[i]);
                    itemStackList.set(nullIdx[i],add);
                }

                itemStacks = itemStackList.toArray(new ItemStack[itemStackList.size()]);
                inventory.setStorageContents(itemStacks);
                sender.sendMessage(Arrays.asList(arrAmount).toString());
                sender.sendMessage(Arrays.asList(nullIdx).toString());
//                sender.sendMessage(itemStackList.toString()+Integer.toString(maxAmount));
                //debug
                return true;
            }else{
                return false;
            }
    }


    public Integer[] divideBy64(int amount){
        int quotient = (amount/64);
        Integer[] arr = new Integer[quotient+1];

        int remainder = amount%64;
        for(int i=0;i<quotient;i++){
            arr[i]=64;
        }
        arr[quotient]=remainder;

//        arr.add(remainder);
        List<Integer> list = Arrays.asList(arr);
//        sender.sendMessage("64로 나눈 배열"+list.toString());
        return arr;
    }
    public int numberOfNull(List<ItemStack> itemStackList){
        int count =0;
        for(ItemStack i:itemStackList){
            count+=((i==null)?1:0);
        }
        return count;
    }
    public Integer[] getNullIdx(List<ItemStack> itemStackList){
        List<Integer> willBeArr = new ArrayList<>();
        for(int i=0;i<itemStackList.size();i++){
            if(itemStackList.get(i)==null){
                willBeArr.add(i);
            }
        }
        return willBeArr.toArray(new Integer[itemStackList.size()]);
    }

    public void setOrderCanceled(int order_id){
        Order order = ReadFromDB(order_id);
        order.is_canceled=true;
        DataIO dataIO= new DataIO();
        dataIO.writeOrderToDB(order);
    }

    public List<Order>getList(){
        return this.list;
    }



}