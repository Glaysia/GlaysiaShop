package glaysia.glaysiashop;
import java.io.*;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class DataIO {
    private String USERDATAFILENAME= "./plugins/glaysiashop/userdata.yml";
    private String MARKETFILENAME= "./plugins/glaysiashop/market.yml";
    private static String header = "glaysiashop";
    private static String dummy = "DIAMOND";
    private Player player = null;
    public static java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");

    private List<Trade.Order> orderList = null;

    public DataIO(Player player) {
        this.player = player;

        String folderName = "./plugins/glaysiashop/";
//        this.USERDATAFILENAME = "./plugins/glaysiashop/userdata.yml";
//        this.MARKETFILENAME = "./plugins/glaysiashop/market.yml";

        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        createYmlIfNotExists(this.USERDATAFILENAME);
        createYmlIfNotExists(this.MARKETFILENAME);
    }

    public DataIO() {

        String folderName = "./plugins/glaysiashop/";

        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        createYmlIfNotExists(this.USERDATAFILENAME);
        createYmlIfNotExists(this.MARKETFILENAME);
    }

    public void createYmlIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            YmlCreator.createUserDataYml(fileName);
            log.info("데이터 파일 없어서 새로 만듦. debuging_DataIO");
        }
    }

    private static class YmlCreator {
        public static void createUserDataYml(String fileName) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);

            Map<String, Object> data = new LinkedHashMap<>();
            Map<String, Object> templateOfPlayerData = new LinkedHashMap<>();
            templateOfPlayerData.put("uuid", "ADMIN-UUID");
            templateOfPlayerData.put("money", 0.0);
            templateOfPlayerData.put("amount", 0);
            Map<String, Object> templateOfPlayer = new LinkedHashMap<>();
            templateOfPlayer.put(dummy, templateOfPlayerData);
            data.put(header, templateOfPlayer);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isTherePlayerInYml() {
        Map<String, Object> playerData = null;
        playerData = YmlReader.readYml(this.USERDATAFILENAME);
        playerData = (Map<String, Object>) playerData.get(header);
        player.sendMessage("test" + playerData.toString());
        return playerData.containsKey(player.getName());
//        return true;
    }

    /*
    @Deprecated
    public boolean isTherePlayerInYml(Player oPlayer){
        Map<String, Object> playerData=null;
        playerData = YmlReader.readYml(this.USERDATAFILENAME);
        playerData=(Map<String, Object>)playerData.get(header);
        oPlayer.sendMessage("test"+playerData.toString());
        return playerData.containsKey(oPlayer.getName());
    }
    */

    private static class YmlReader {
        public static Map<String, Object> readYml(String fileName) {
            try (Reader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8")) {
                Yaml yaml = new Yaml();
                Map<String, Object> data = yaml.load(reader);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                Map<String, Object> ermap = new LinkedHashMap<>();
                ermap.put("error", "error");
                return ermap;
            }
        }

        @Deprecated
        public static double getMoneyFromYml(String filename) {
            Map<String, Object> ldata = new LinkedHashMap<>();
            ldata = readYml(filename);
            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop = (Map<String, Object>) ldata.get(header);
            Map<String, Object> key_is_username = new LinkedHashMap<>();
            key_is_username = (Map<String, Object>) key_is_glaysiashop.get(dummy);

            return (double) key_is_username.get("money");
        }

        @Deprecated
        public static int getDiamondFromYml(String filename) {
            Map<String, Object> ldata = new LinkedHashMap<>();
            ldata = readYml(filename);
            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop = (Map<String, Object>) ldata.get(header);
            Map<String, Object> key_is_username = new LinkedHashMap<>();
            key_is_username = (Map<String, Object>) key_is_glaysiashop.get(dummy);
            if (key_is_username == null)
//                writeAdminYmlIfNotWritten();
                return 1;

            return (int) key_is_username.get("amount");
        }
    }


    public int getLastOrder() {

        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
        key_is_glaysiashop = YmlReader.readYml(MARKETFILENAME);
        Map<String, Object> key_is_DIAMOND_Order = new LinkedHashMap<>();
        key_is_DIAMOND_Order = (Map<String, Object>) (key_is_glaysiashop.get(header));

        int lastOrder = (Integer) (((Map<String, Object>) (key_is_DIAMOND_Order.get(dummy))).get("last_order"));
        return lastOrder;
    }
//    public int getLastDoneOrder() {
//
//        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
//        key_is_glaysiashop = YmlReader.readYml(MARKETFILENAME);
//        Map<String, Object> key_is_DIAMOND_Order = new LinkedHashMap<>();
//        key_is_DIAMOND_Order = (Map<String, Object>) (key_is_glaysiashop.get(header));
//
//        int lastOrder = (Integer) (((Map<String, Object>) (key_is_DIAMOND_Order.get(dummy))).size());
//        return lastOrder;
//    }
    List<Trade.Order> getDoneOrderList(){
        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
        key_is_glaysiashop = YmlReader.readYml(MARKETFILENAME);

        Map<String, Object> key_is_DIAMOND_Order = new LinkedHashMap<>();
        key_is_DIAMOND_Order = (Map<String, Object>) (key_is_glaysiashop.get(header));

        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id = (Map<String, Object>) (key_is_DIAMOND_Order.get("DoneOrder"));

        LinkedList<Trade.Order> list = new LinkedList<>();
        for(String key : key_is_id.keySet()){
            list.add(getDoneOrder(Integer.parseInt(key)));
        }
        return list;
    }

    public Trade.Order getOrder(int order_id) {
        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
        key_is_glaysiashop = YmlReader.readYml(MARKETFILENAME);

        Map<String, Object> key_is_DIAMOND_Order = new LinkedHashMap<>();
        key_is_DIAMOND_Order = (Map<String, Object>) (key_is_glaysiashop.get(header));

        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id = (Map<String, Object>) (key_is_DIAMOND_Order.get("Order"));

        Map<String, Object> key_is_date_price_amount__ = new LinkedHashMap<>();

        key_is_date_price_amount__ = (Map<String, Object>) (key_is_id.get(Integer.toString(order_id)));
        Material m = Material.getMaterial((String) key_is_date_price_amount__.get("material"));
        m = (m == null) ? Material.RED_MUSHROOM : m;
//        var tmp =
//        double ppa = 3.141592;
//        if (tmp instanceof Integer){
//            ppa = ((Integer)tmp).doubleValue();
//        }
        Trade.Order order = new Trade.Order(
                order_id,
                (Date) key_is_date_price_amount__.get("date"),
                (Double) key_is_date_price_amount__.get("price"),
                (Integer) key_is_date_price_amount__.get("amount"),
                (Double) key_is_date_price_amount__.get("price_per_amount"),
                (String) key_is_date_price_amount__.get("trader"),
                m
        );
//        order.is_selling= (boolean) key_is_date_price_amount__.get("is_selling");
        order.is_canceled = (Boolean) key_is_date_price_amount__.get("is_canceled");
        order.is_completed = (Boolean) key_is_date_price_amount__.get("is_complete");
        order.is_there_error = (Boolean) key_is_date_price_amount__.get("is_there_error");

        return order;
    }

    public Trade.Order getDoneOrder(int order_id) {
        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
        key_is_glaysiashop = YmlReader.readYml(MARKETFILENAME);

        Map<String, Object> key_is_DIAMOND_Order = new LinkedHashMap<>();
        key_is_DIAMOND_Order = (Map<String, Object>) (key_is_glaysiashop.get(header));

        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id = (Map<String, Object>) (key_is_DIAMOND_Order.get("DoneOrder"));

        Map<String, Object> key_is_date_price_amount__ = new LinkedHashMap<>();

        key_is_date_price_amount__ = (Map<String, Object>) (key_is_id.get(Integer.toString(order_id)));

        Material m = Material.getMaterial((String) key_is_date_price_amount__.get("material"));
        m = (m == null) ? Material.RED_MUSHROOM : m;
//        var tmp =
//        double ppa = 3.141592;
//        if (tmp instanceof Integer){
//            ppa = ((Integer)tmp).doubleValue();
//        }
        Trade.Order order = new Trade.Order(
                order_id,
                (Date) key_is_date_price_amount__.get("date"),
                (Double) key_is_date_price_amount__.get("price"),
                (Integer) key_is_date_price_amount__.get("amount"),
                (Double) key_is_date_price_amount__.get("price_per_amount"),
                (String) key_is_date_price_amount__.get("trader"),
                m
        );
//        order.is_selling= (boolean) key_is_date_price_amount__.get("is_selling");
        order.is_canceled = (Boolean) key_is_date_price_amount__.get("is_canceled");
        order.is_completed = (Boolean) key_is_date_price_amount__.get("is_complete");
        order.is_there_error = (Boolean) key_is_date_price_amount__.get("is_there_error");

        return order;
    }


    private static class YmlWriter {
        private static Map<String, Object> nameUUIDMoneyToValue(String fileName, String name, String UUID, double money) {
            Map<String, Object> key_is_glaysiashop = (Map<String, Object>) (YmlReader.readYml(fileName).get(header));
            Map<String, Object> key_is_username = new LinkedHashMap<>();
            Map<String, Object> ldata = new LinkedHashMap<>();

            key_is_username.put("uuid", UUID);
            key_is_username.put("money", money);
            key_is_glaysiashop.put(name, key_is_username);
            ldata.put(header, key_is_glaysiashop);
            return ldata;
        }

        public static boolean writeYml(String fileName, Player player, double iMoney) {
            //glaysiashop:
            //  Glaysia:
            //      uuid:ssss
            //      money:1234
            //  iizuna:
            //      uuid:asdf
            //      money:1230
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);

            String name = player.getName();
            String UUID = player.getUniqueId().toString();
            double money = iMoney;

            Map<String, Object> ldata = (Map<String, Object>) nameUUIDMoneyToValue(fileName, name, UUID, money);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(ldata, writer);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Deprecated
        public static boolean writeYml(String fileName, String adminName, double iMoney) {
            //glaysiashop:
            //  Glaysia:
            //      uuid:ssss
            //      money:1234
            //  iizuna:
            //      uuid:asdf
            //      money:1230
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);

            String name = adminName;
            String UUID = "adminUUID";

            double money = iMoney;

            Map<String, Object> ldata = (Map<String, Object>) nameUUIDMoneyToValue(fileName, name, UUID, money);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(ldata, writer);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Deprecated
        public void writeAdminYml(String fileName, double dMoney, int dDiamond) {
//        DIAMOND:
//          uuid: for-admin
//          money: 0.0
//          diamond: 0
            log.info("writeAdmin1");

            int adminDiamond = YmlReader.getDiamondFromYml(fileName);
            double adminMoney = YmlReader.getMoneyFromYml(fileName);

            Map<String, Object> key_is_username = new LinkedHashMap<>();
//

            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop = YmlReader.readYml(fileName);

//            playerForDebugging.sendMessage(key_is_glaysiashop.toString());

            key_is_username = (Map<String, Object>) key_is_glaysiashop.get(header);

            key_is_username.put("uuid", "for-admin");
            key_is_username.put("money", adminMoney + dMoney);
            key_is_username.put("diamond", adminDiamond + dDiamond);

            key_is_glaysiashop.put(dummy, key_is_username);

            Map<String, Object> ldata = new LinkedHashMap<>();
            ldata.put(header, key_is_glaysiashop);


//        YmlReader ymlReader=new YmlReader();


            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(ldata, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static boolean writeBankToYml(String fileName, double money, int amount) {
//            DIAMOND:
//              uuid: ADMIN-UUID
//              money: 0.0
//              amount: 0
//            Glaysia:
//              uuid: blabla
//              money: 0.0
//            iizuna:
//              uuid: blabla
//              money: 0.0
//            ...
            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop = YmlReader.readYml(fileName);

            Map<String, Object> key_is_DIAMOND = new LinkedHashMap<>();
            key_is_DIAMOND = (Map<String, Object>) (key_is_glaysiashop.get(header));

            Map<String, Object> key_is_uidmoneyamount = new LinkedHashMap<>();
            key_is_uidmoneyamount = (Map<String, Object>) (key_is_DIAMOND.get(dummy));

            key_is_uidmoneyamount.put("money", money);//test
            key_is_uidmoneyamount.put("amount", amount);//test

            key_is_DIAMOND.put(dummy, key_is_uidmoneyamount);
            key_is_glaysiashop.put(header, key_is_DIAMOND);

//            double aMoney=0.0;
//            int aAmount=0;


            boolean success;

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(key_is_glaysiashop, writer);
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            }
//            String debugInfo="저장된 돈: "+Double.toString(aMoney)+"저장된 다이아: "+Double.toString(aAmount);
//            log.info(debugInfo);

            return success;
        }

        public static boolean writeOrderToYml(Map<String, Object> key_is_id_Onevalue, int order_id, String fileName) {
            return writeOrderToYml(key_is_id_Onevalue, order_id, fileName, false);
        }

        public static boolean writeOrderToYml(Map<String, Object> key_is_id_Onevalue, int order_id, String fileName, boolean isDone){
            DataIO tmp = new DataIO();
            int last = tmp.getLastOrder();

            if (last < order_id) {
                last = order_id;
            }
            String key_for_key_is_DIAMOND_ORDER=isDone?"DoneOrder":"Order";
            String key_for_key_is_dummy=isDone?"last_done_order":"last_order";

            Map<String, Object> key_is_glaysiashop = YmlReader.readYml(fileName);
            Map<String, Object> key_is_DIAMOND_ORDER = new LinkedHashMap<>();
            key_is_DIAMOND_ORDER = (Map<String, Object>) (key_is_glaysiashop.get(header));

            Map<String, Object> key_is_dummy = new LinkedHashMap<>();
            key_is_dummy = (Map<String, Object>) (key_is_DIAMOND_ORDER.get(dummy));
            key_is_dummy.put(key_for_key_is_dummy, last);

            Map<String, Object> key_is_id = new LinkedHashMap<>();
            key_is_id = (Map<String, Object>) (key_is_DIAMOND_ORDER.get(key_for_key_is_DIAMOND_ORDER));
            key_is_id.put(Integer.toString(order_id), key_is_id_Onevalue);

            key_is_DIAMOND_ORDER.put(dummy, key_is_dummy);
            key_is_DIAMOND_ORDER.put(key_for_key_is_DIAMOND_ORDER, key_is_id);

            key_is_glaysiashop.put(header, key_is_DIAMOND_ORDER);

            boolean success;

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(key_is_glaysiashop, writer);
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            }
//            String debugInfo="저장된 돈: "+Double.toString(aMoney)+"저장된 다이아: "+Double.toString(aAmount);
//            log.info(debugInfo);

            return success;
        }
        }



    public boolean writeOrderToDB(Trade.@NotNull Order order) {
        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id.put("date", order.date);
        key_is_id.put("price", order.price);
        key_is_id.put("amount", order.amount);
        key_is_id.put("price_per_amount", order.pricePerAmount);
        key_is_id.put("trader", order.trader);
        key_is_id.put("material", order.material.toString());
        key_is_id.put("is_selling", order.is_selling);
        key_is_id.put("is_canceled", order.is_canceled);
        key_is_id.put("is_complete", order.is_completed);
        key_is_id.put("is_there_error", order.is_there_error);

        return YmlWriter.writeOrderToYml(key_is_id, order.order_id, MARKETFILENAME);
    }
    public boolean writeDoneOrderToDB(Trade.@NotNull Order order) {
        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id.put("date", order.date);
        key_is_id.put("price", order.price);
        key_is_id.put("amount", order.amount);
        key_is_id.put("price_per_amount", order.pricePerAmount);
        key_is_id.put("trader", order.trader);
        key_is_id.put("material", order.material.toString());
        key_is_id.put("is_selling", order.is_selling);
        key_is_id.put("is_canceled", order.is_canceled);
        key_is_id.put("is_complete", order.is_completed);
        key_is_id.put("is_there_error", order.is_there_error);

        return YmlWriter.writeOrderToYml(key_is_id, order.order_id, MARKETFILENAME, true);
    }
    public boolean setDoneOrderCompletedToDB(Trade.Order order){
        Map<String, Object> key_is_id = new LinkedHashMap<>();
        key_is_id.put("date", order.date);
        key_is_id.put("price", order.price);
        key_is_id.put("amount", order.amount);
        key_is_id.put("price_per_amount", order.pricePerAmount);
        key_is_id.put("trader", order.trader);
        key_is_id.put("material", order.material.toString());
        key_is_id.put("is_selling", order.is_selling);
        key_is_id.put("is_canceled", order.is_canceled);
        key_is_id.put("is_complete", true);
        key_is_id.put("is_there_error", order.is_there_error);

        return YmlWriter.writeOrderToYml(key_is_id, order.order_id, MARKETFILENAME, true);

    }

    public boolean writePlayerToDB(double money) {
        return YmlWriter.writeYml(USERDATAFILENAME, player, money);
    }

    public boolean writePlayerToDB(Player player, Economy econ, double money) {
        return YmlWriter.writeYml(this.USERDATAFILENAME, player, money);
    }

    /*
        @Deprecated
        public static boolean writePlayerToDB(String adminName,double money) {
            return YmlWriter.writeYml(USERDATAFILENAME, adminName, money);
        }
    */

    public boolean setMoneyOfDBFromPlayer(double money) {
        boolean success = writePlayerToDB(money);
        if (!isTherePlayerInYml()) {//파일에 없을 때
            String message = success ? "계좌가 없습니다. 계좌를 만듭니다." : "파일생성 실패";
            player.sendMessage(message);
        } else {
            String message = success ? "계좌에 금액을 저장하였습니다" : "파일저장 실패";
            player.sendMessage(message);
        }
        return success;
    }

    public double getMoneyFromYml() {
        return YmlReader.getMoneyFromYml(USERDATAFILENAME);
    }

    public int getDiamondFromYml() {
        return YmlReader.getDiamondFromYml(USERDATAFILENAME);
    }

//    public static double getMoneyFromYml(String fileName) {
//        return YmlReader.getMoneyFromYml(fileName);
//    }
//    public static int getDiamondFromYml(String fileName) {
//        return YmlReader.getDiamondFromYml(fileName);
//    }

    public boolean writeYmlIfNotWritten(Player player, Economy econ) {
        double money = econ.getBalance(player);
        File file = new File(USERDATAFILENAME);
        boolean success = writePlayerToDB(player, econ, money);
        if (!success)
            player.sendMessage("파일 저장 실패");
        return success;
    }

    public boolean writeBankMoneyToYml(double money, int amount) {
        return YmlWriter.writeBankToYml(USERDATAFILENAME, money, amount);
    }
}







