package glaysia.glaysiashop;
import java.io.*;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataIO {
    private String USERDATAFILENAME;
    private String MARKETFILENAME;
    private static String header="glaysiashop";
    private Player player = null;
    public static java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");

    public DataIO(Player player) {
        this.player = player;

        String folderName = "./plugins/glaysiashop/";
        this.USERDATAFILENAME = "./plugins/glaysiashop/userdata.yml";
        this.MARKETFILENAME = "./plugins/glaysiashop/market.yml";

        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        createYmlIfNotExists(this.USERDATAFILENAME);
        createYmlIfNotExists(this.MARKETFILENAME);
    }
    public DataIO() {

        String folderName = "./plugins/glaysiashop/";
        this.USERDATAFILENAME = "./plugins/glaysiashop/userdata.yml";
        this.MARKETFILENAME = "./plugins/glaysiashop/market.yml";

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
            log.info("유저데이터 파일 없어서 새로 만듦. debuging_DataIO");
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
            templateOfPlayer.put("DIAMOND",templateOfPlayerData);
            data.put(header, templateOfPlayer);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isTherePlayerInYml() {
        Map<String, Object> playerData=null;
        playerData = YmlReader.readYml(this.USERDATAFILENAME);
        playerData=(Map<String, Object>)playerData.get(header);
        player.sendMessage("test"+playerData.toString());
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
        public static double getMoneyFromYml(String filename){
            Map<String, Object> ldata = new LinkedHashMap<>();
            ldata=readYml(filename);
            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop=(Map<String, Object>)ldata.get("glaysiashop");
            Map<String, Object> key_is_username= new LinkedHashMap<>();
            key_is_username=(Map<String, Object>)key_is_glaysiashop.get("DIAMOND");

            return (double)key_is_username.get("money");
        }
        @Deprecated
        public static int getDiamondFromYml(String filename){
            Map<String, Object> ldata = new LinkedHashMap<>();
            ldata=readYml(filename);
            Map<String, Object> key_is_glaysiashop= new LinkedHashMap<>();
            key_is_glaysiashop=(Map<String, Object>)ldata.get("glaysiashop");
            Map<String, Object> key_is_username= new LinkedHashMap<>();
            key_is_username=(Map<String, Object>)key_is_glaysiashop.get("DIAMOND");
            if (key_is_username==null)
//                writeAdminYmlIfNotWritten();
                return  1;

            return (int)key_is_username.get("amount");
        }
    }

    private static class YmlWriter{
        private static Map<String, Object> nameUUIDMoneyToValue(String fileName, String name, String UUID, double money){
            Map<String, Object> key_is_glaysiashop = (Map<String, Object>)(YmlReader.readYml(fileName).get("glaysiashop"));
            Map<String, Object> key_is_username =new LinkedHashMap<>();
            Map<String, Object> ldata =new LinkedHashMap<>();

            key_is_username.put("uuid",UUID);
            key_is_username.put("money",money);
            key_is_glaysiashop.put(name, key_is_username);
            ldata.put("glaysiashop", key_is_glaysiashop);
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

            String name=player.getName();
            String UUID=player.getUniqueId().toString();
            double money=iMoney;

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

            String name=adminName;
            String UUID="adminUUID";

            double money=iMoney;

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
        public void writeAdminYml(String fileName, double dMoney, int dDiamond){
//        DIAMOND:
//          uuid: for-admin
//          money: 0.0
//          diamond: 0
            log.info("writeAdmin1");

            int adminDiamond=YmlReader.getDiamondFromYml(fileName);
            double adminMoney=YmlReader.getMoneyFromYml(fileName);

            Map<String, Object> key_is_username = new LinkedHashMap<>();
//

            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop=YmlReader.readYml(fileName);

//            playerForDebugging.sendMessage(key_is_glaysiashop.toString());

            key_is_username=(Map<String, Object>)key_is_glaysiashop.get("glaysiashop");

            key_is_username.put("uuid","for-admin");
            key_is_username.put("money",adminMoney+dMoney);
            key_is_username.put("diamond",adminDiamond+dDiamond);

            key_is_glaysiashop.put("DIAMOND",key_is_username);

            Map<String, Object> ldata =new LinkedHashMap<>();
            ldata.put("glaysiashop",key_is_glaysiashop);


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

        public static boolean writeBankToYml(String fileName, double money, int amount){
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
            key_is_glaysiashop=YmlReader.readYml(fileName);

            Map<String, Object> key_is_DIAMOND = new LinkedHashMap<>();
            key_is_DIAMOND=(Map<String, Object>)(key_is_glaysiashop.get("glaysiashop"));

            Map<String, Object> key_is_uidmoneyamount = new LinkedHashMap<>();
            key_is_uidmoneyamount=(Map<String, Object>)(key_is_DIAMOND.get("DIAMOND"));

            key_is_uidmoneyamount.put("money",money);//test
            key_is_uidmoneyamount.put("amount",amount);//test

            key_is_DIAMOND.put("DIAMOND",key_is_uidmoneyamount);
            key_is_glaysiashop.put("glaysiashop",key_is_DIAMOND);

//            double aMoney=0.0;
//            int aAmount=0;


            boolean success ;

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(key_is_glaysiashop, writer);
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
                success=false;
            }
//            String debugInfo="저장된 돈: "+Double.toString(aMoney)+"저장된 다이아: "+Double.toString(aAmount);
//            log.info(debugInfo);

            return success;
        }

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
        if(!success)
            player.sendMessage("파일 저장 실패");
        return success;
    }

    public boolean writeBankMoneyToYml(double money, int amount){
        return YmlWriter.writeBankToYml(USERDATAFILENAME, money, amount);
    }

    /*
    @Deprecated
    public static void writeAdminYmlIfNotWritten() {
        double money = 0.0;

        File file = new File(DataIO.USERDATAFILENAME);
        boolean success = writePlayerToDB("DIAMOND", money);
        if(!success)
            log.info("어드민 다이아 파일 저장 실패");
//            player.sendMessage("파일 저장 실패");
    }
    */


    /*
    @Deprecated
    public void writeAdminMoneyDiamondToYml(double dMoney, int dDiamond) {
        YmlWriter ymlWriter = new YmlWriter();
        ymlWriter.writeAdminYml(this.USERDATAFILENAME, dMoney, dDiamond);
    }
    */


    /*
    public static void writeAdminYml(String fileName, double dMoney, int dDiamond){
//        DIAMOND:
//          uuid: for-admin
//          money: 0.0
//          diamond: 0
        int adminDiamond=YmlReader.getDiamondFromYml(fileName);
        double adminMoney=YmlReader.getMoneyFromYml(fileName);

        Map<String, Object> key_is_username = new LinkedHashMap<>();
        key_is_username.put("uuid","for-admin");
        key_is_username.put("money",adminMoney+dMoney);
        key_is_username.put("diamond",adminDiamond+dDiamond);
        Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
        key_is_glaysiashop=YmlReader.readYml(fileName);
        key_is_glaysiashop=(Map<String, Object>)key_is_glaysiashop.get("glaysiashop");
        key_is_glaysiashop.put("DIAMOND",key_is_username);
        Map<String, Object> ldata =new LinkedHashMap<>();
        ldata.put("glaysiashop",key_is_glaysiashop);


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
    */

}

/*
//    public int getMoneyFromPlayer(MyPlayer player) {
//        YmlReader books = new YmlReader();
////        log.info("getMoneyFromPlayer_test_Start");
//        Map<String, Object> data = books.readYml(this.userdataFileName);
////        log.info("getMoneyFromPlayer_book_read");
//        Map<String, Object> playersData = (Map<String, Object>) data.get("glaysiashop");
////        log.info("get_from_key");
//        Map<String, Object> aPlayer = (Map<String, Object>) playersData.get(player.getName());
////        log.info("get_from_name");
//        int money = (int) aPlayer.get("money");
////        log.info("get_from_money");
//        return money;
//    }
//
//    public void writeYmlIfNotWritten(MyPlayer player) {
//        YmlWriter ymlwriter = new YmlWriter();
//
//        File file = new File(this.userdataFileName);
//        if (!isTherePlayerInYml(player)) {
//            ymlwriter.writeYml(this.userdataFileName, player);
//        } else {
//            writeYmlMoneyFromVault(player);
//        }
//    }
//
//    public void writeYmlMoneyFromVault(MyPlayer player) {
//        File file = new File(this.userdataFileName);
//        YmlWriter ymlwriter = new YmlWriter();
//        ymlwriter.writeYml(this.userdataFileName, player);
//
//    }
//
//    public boolean isTherePlayerInYml(MyPlayer player) {
////            log.info("test_Start");
//        YmlReader books = new YmlReader();
//        Map<String, Object> data = books.readYml(this.userdataFileName);
//
////        boolean testMap_key=data.containsKey("glaysiashop");
////            log.info(Boolean.toString(testMap_key));
//
//        Map<String, Object> playerData = (Map<String, Object>) data.get("glaysiashop");
//        if (playerData.containsKey(player.getName())) {
//            if (((Map<String, Object>) (playerData.get(player.getName()))).get("uuid").equals(player.getUUID()))
//                return true;
//        }
//
//        return false;
//    }
*/

/*
public void createYmlIfNotExists(String fileName) {
    YmlCreator ymlCreator = new YmlCreator();

    File file = new File(fileName);
    if (!file.exists()) {
        ymlCreator.createUserDataYml(this.userdataFileName);
    }
}

public void writeAdminMoneyDiamondToYml(double dMoney, int dDiamond) {
    YmlWriter.writeAdminYml(this.userdataFileName, dMoney, dDiamond);
}


//    public double getMoneyFromYml() {
//        return YmlReader.getMoneyFromYml(this.userdataFileName);
//    }
//
//    public int getDiamondFromYml() {
//        return YmlReader.getDiamondFromYml(this.userdataFileName);
//    }

public boolean addItemToMarketList(Material material, int amount, double price, String username) {
    if (-0.1 <= price && price <= 0.1) return false;
    boolean isBuying = price < 0;


    return true;
}*/

/*
private class YmlCreator {
    public static void createUserDataYml(String fileName) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        Map<String, Object> data = new LinkedHashMap<>();
        Map<String, Object> templateOfPlayerData = new LinkedHashMap<>();
        templateOfPlayerData.put("uuid", "asdf");
        templateOfPlayerData.put("money", 100);
        Map<String, Object> templateOfPlayer = new LinkedHashMap<>();
        templateOfPlayer.put("user_test김불",templateOfPlayerData);
        data.put("glaysiashop", templateOfPlayer);

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
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
    public static double getMoneyFromYml(String filename){
        Map<String, Object> ldata = new LinkedHashMap<>();
        ldata=readYml(filename);
        Map<String, Object> key_is_glaysiashop= new LinkedHashMap<>();
        key_is_glaysiashop=(Map<String, Object>)ldata.get("glaysiashop");
        Map<String, Object> key_is_username= new LinkedHashMap<>();
        key_is_username=(Map<String, Object>)key_is_glaysiashop.get("DIAMOND");
        return (double)key_is_username.get("money");
    }
    public static int getDiamondFromYml(String filename){
        Map<String, Object> ldata = new LinkedHashMap<>();
        ldata=readYml(filename);
        Map<String, Object> key_is_glaysiashop= new LinkedHashMap<>();
        key_is_glaysiashop=(Map<String, Object>)ldata.get("glaysiashop");
        Map<String, Object> key_is_username= new LinkedHashMap<>();
        key_is_username=(Map<String, Object>)key_is_glaysiashop.get("DIAMOND");
        return (int)key_is_username.get("diamond");
    }
}
*/

/*
    private class YmlWriter {
        public static void writeYml(String fileName, MyPlayer player) {
            YmlReader ymlReader=new YmlReader();
            //glaysiashop:
            //  Glaysia:
            //      uuid:asdf
            //      money:1234
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);

            Map<String, Object> key_is_glaysiashop = (Map<String, Object>)(YmlReader.readYml(fileName).get("glaysiashop"));
            //Gdsfia:
            //  uuid : "b2815e3f-e84c-437f-8f53-b510c548bc79",
            //  money: 1234
            //Gsdfa:
            //  uuid : "b2815e3f-e84c-437f-8f53-b510c548bc79",
            //  money: 1234
            //Gsdf:
            //  uuid : "b2815e3f-e84c-437f-8f53-b510c548bc79",
            //  money: 1234
            //sdf:
            //  uuid : "b2815e3f-e84c-437f-8f53-b510c548bc79",
            //  money: 1234

            Map<String, Object> key_is_username =new LinkedHashMap<>();
            key_is_username.put("uuid",player.getUUID());
            key_is_username.put("money",player.getMoney());
            key_is_glaysiashop.put(player.getName(), key_is_username);
            Map<String, Object> ldata =new LinkedHashMap<>();

            ldata.put("glaysiashop",key_is_glaysiashop);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8")) {
                yaml.dump(ldata, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void writeAdminYml(String fileName, double dMoney, int dDiamond){
//        DIAMOND:
//          uuid: for-admin
//          money: 0.0
//          diamond: 0
            int adminDiamond=YmlReader.getDiamondFromYml(fileName);
            double adminMoney=YmlReader.getMoneyFromYml(fileName);

            Map<String, Object> key_is_username = new LinkedHashMap<>();
            key_is_username.put("uuid","for-admin");
            key_is_username.put("money",adminMoney+dMoney);
            key_is_username.put("diamond",adminDiamond+dDiamond);
            Map<String, Object> key_is_glaysiashop = new LinkedHashMap<>();
            key_is_glaysiashop=YmlReader.readYml(fileName);
            key_is_glaysiashop=(Map<String, Object>)key_is_glaysiashop.get("glaysiashop");
            key_is_glaysiashop.put("DIAMOND",key_is_username);
            Map<String, Object> ldata =new LinkedHashMap<>();
            ldata.put("glaysiashop",key_is_glaysiashop);


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
    }
    */

/*
public static class MyPlayer {
    private String name;
    private String uuid;
    private double money;

    public MyPlayer(String name, String uuid, double money) {
        this.name = name;
        this.uuid = uuid;
        this.money = money;
    }
    public String getName() {
        return name;
    }
    public String getUUID() {
        return uuid;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void addMoney(int pmoney) {
        this.money = this.money+pmoney;
    }
    public void subMoney(int mmoney) {
        this.money = this.money-mmoney;
    }

}
*/


