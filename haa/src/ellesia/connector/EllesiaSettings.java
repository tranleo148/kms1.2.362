package ellesia.connector;

import java.util.HashMap;
import java.util.Map;
/* 접속기 파일 체크, 버전체크, CRC 셋팅*/
public class EllesiaSettings {

    public static String CRC = "07483446C7583BC64B5464EE68B17DDFC0818A82";
    public static Map<String, String> Files = new HashMap<String, String>() {
        {
            put("skill.wz", "7AE42516C3A88BE7183883A6319A07539D9F4E8B");
            put("Skill001.wz", "E427CB2A0C89497A2B2D02463DB6918FDB6E41A9");
            put("Skill002.wz", "1C61D653564014F1F75D6B5527C78DF4192C3720");
            put("Skill003.wz", "27B15F407BBA1020B5FEA205E59CFFEB3CA54889");
            put("Character.wz", "57C85E05C335B067BAE100C3FDFC1B9DC509BD94");
            put("item.wz", "55960D6B318D937D8C7E55032B2D8A5ECC894616");
            put("Localhost.dll", "BD46D07F53C96F792493F9F1F504BFDABAA07E54");
        }
    };
    public static int ConnectorVersion = 18012;

}
