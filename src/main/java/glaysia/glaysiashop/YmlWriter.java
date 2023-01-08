package glaysia.glaysiashop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YmlWriter {
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