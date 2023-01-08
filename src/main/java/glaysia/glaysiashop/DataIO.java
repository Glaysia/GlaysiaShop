package glaysia.glaysiashop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.util.LinkedHashMap;
import java.util.Map;
public class DataIO {
    private String userdataFileName;
    private String marketFileName;

    public  java.util.logging.Logger log =  java.util.logging.Logger.getLogger("Minecraft");
    public DataIO () {
        String folderName="./plugins/glaysiashop/";
        this.userdataFileName="./plugins/glaysiashop/userdata.yml";
        this.marketFileName="./plugins/glaysiashop/market.yml";

        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        createYmlIfNotExists(this.userdataFileName);
        createYmlIfNotExists(this.marketFileName);
    }
    public void writeYmlIfNotWritten(MyPlayer player){
        YmlWriter ymlwriter= new YmlWriter();

        File file = new File(this.userdataFileName);
        if(!isTherePlayerInYml(player)){
            ymlwriter.writeYml(this.userdataFileName, player);
        }else{
            writeYmlMoneyFromVault(player);
        }
    }
    public void writeYmlMoneyFromVault(MyPlayer player){
        File file = new File(this.userdataFileName);
        YmlWriter ymlwriter= new YmlWriter();
        ymlwriter.writeYml(this.userdataFileName, player);

    }
    public boolean isTherePlayerInYml(MyPlayer player){
//            log.info("test_Start");
        YmlReader books=new YmlReader();
        Map<String, Object> data = books.readYml(this.userdataFileName);
        boolean testMap_key=data.containsKey("glaysiashop");
//            log.info(Boolean.toString(testMap_key));

        Map<String, Object> playerData = (Map<String, Object>)data.get("glaysiashop");

        if(playerData.containsKey(player.getName())){
            if(((Map<String, Object>)(playerData.get(player.getName()))).get("uuid").equals(player.getUUID()))
                return true;
        }

        return false;
    }

    public int getMoneyFromPlayer(MyPlayer player){
        YmlReader books=new YmlReader();
//        log.info("getMoneyFromPlayer_test_Start");
        Map<String, Object> data = books.readYml(this.userdataFileName);
//        log.info("getMoneyFromPlayer_book_read");
        Map<String, Object> playersData = (Map<String, Object>)data.get("glaysiashop");
//        log.info("get_from_key");
        Map<String, Object> aPlayer = (Map<String, Object>)playersData.get(player.getName());
//        log.info("get_from_name");
        int money=(int)aPlayer.get("money");
//        log.info("get_from_money");
        return money;
    }
    public void createYmlIfNotExists(String fileName) {
        YmlCreator ymlCreator =new YmlCreator();

        File file = new File(fileName);
        if (!file.exists()) {
           ymlCreator.createUserDataYml(this.userdataFileName);
        }
    }
    public void writeAdminMoneyDiamondToYml(double dMoney, int dDiamond){
        YmlWriter.writeAdminYml(this.userdataFileName, dMoney, dDiamond);
    }


    public double getMoneyFromYml(){
        return YmlReader.getMoneyFromYml(this.userdataFileName);
    }
    public int getDiamondFromYml(){
        return YmlReader.getDiamondFromYml(this.userdataFileName);
    }
    public boolean addItemToMarketList(Material material, int amount, double price, String username){
        if (-0.1<=price && price<=0.1) return false;
        boolean isBuying = price<0;


        return true;
    }
}