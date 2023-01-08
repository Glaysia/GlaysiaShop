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

public class YmlCreator {
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