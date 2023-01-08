package glaysia.glaysiashop;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YmlReader {
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