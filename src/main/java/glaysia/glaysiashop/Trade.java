package glaysia.glaysiashop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import static glaysia.glaysiashop.GlaysiaShop.log;

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
            String sellOrBuy = is_selling?"판매":"구매";
            return "주문자 이름: "+trader+" 거래품목: "+material.toString()+" 가격: "+Double.toString(price)+" 개수: "+Integer.toString(amount) + " 개당 가격: "+Double.toString(pricePerAmount)+" 거래유형은 "+sellOrBuy+"입니다.";
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
//        log.info(mainDataMap.toString());

//        order_id++; //거래할 때마다 증가. static변수를 사용.
//
//        Map<String, Object> sheet = new LinkedHashMap<>();
//
//        Order order = new Order(
//                order_id,
//                (new Date()),
//                price,
//                amount,
//                trader,
//                material
//        );
//
//        sheet.put(String.valueOf(order_id), order);
//
//        order.is_canceled=false;
//        order.is_completed=false;
//        order.is_there_error=was_there_error;  //거래 시작했을 때 아이템, 금액이 정산됐는지 여부
//
//        if (order.is_there_error){
//            failure.put(String.valueOf(order_id), sheet);
//        }
//        else{
//            success.put(String.valueOf(order_id), sheet);
//        }
//        is_saved=saveToDB(mainDataMap, dataFile);
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
//        return someFunction(order_id);
//        Order exampleOrder = new Order(
//                1,
//                (new Date(1673122855911L)),
//                -300.0,
//                64,
//                "Glaysia",
//                Material.ACACIA_LOG
//        );
//        exampleOrder.is_canceled=false;
//        exampleOrder.is_completed=false;
//        exampleOrder.is_there_error=false;//예시
        DataIO dataIO= new DataIO();

        Order outOrder = null;
        outOrder=dataIO.getOrder(order_id);

//        File dataFile = new File( "./plugins/market.yml");
//        FileConfiguration dataFileConfig = YamlConfiguration.loadConfiguration(dataFile);
//        Map<String, Object> mainDataMap;
//
//        Yaml yaml = new Yaml();
//        Map<String, Object> success = new HashMap<>();
//        try {
//            Map<String, Object> dataMap = yaml.load(new FileInputStream(dataFile));
//            mainDataMap = dataMap;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        success = (Map<String, Object>) mainDataMap.get("success");
//        if (success.containsKey(String.valueOf(order_id))){
//            exampleOrder = (Order) success.get(String.valueOf(order_id));
//            exampleOrder.is_completed = true;
//        }
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
        for(int i=1;i<=last_order;i++){
            this.list.add(dataIO.getOrder(i));
        }

        List<Integer> list_idx_for_remove = new ArrayList<>();
        int idx=0;
        this.list.removeIf(i -> (
                ((!i.isMadeBy(traderName)) || i.is_canceled || i.is_completed || i.is_there_error)
                ));
    }

    public Trade(Material material, Player playerForDebug){
        DataIO dataIO = new DataIO();
//        this.list = new ArrayList<>();

        int last_order=dataIO.getLastOrder();
        for(int i=1;i<=last_order;i++){
            this.list.add(dataIO.getOrder(i));
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