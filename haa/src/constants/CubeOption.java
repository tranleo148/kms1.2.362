/*
 * Decompiled with CFR 0.150.
 */
package constants;

import constants.GameConstants;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.Randomizer;

public class CubeOption {
    /*
     * Opcode count of 19376 triggered aggressive code reduction.  Override with --aggressivesizethreshold.
     */
    public static int getRedCubePotentialId(int itemid, int grade, int num, int ... args) {
        int potentialid;
        block154: {
            LinkedHashMap<Integer, Integer> list = new LinkedHashMap<Integer, Integer>();
            potentialid = 0;
            int type = itemid / 1000;
            type = GameConstants.isWeapon(itemid) || GameConstants.isSecondaryWeapon(itemid) ? 1302 : (GameConstants.isCap(itemid) ? 1004 : (GameConstants.isCape(itemid) ? 1102 : (type == 1132 || type == 1152 ? 1132 : (GameConstants.isLongcoat(itemid) || type >= 1040 && type <= 1042 ? 1042 : (type >= 1070 && type <= 1073 ? 1073 : (type >= 1080 && type <= 1082 ? 1082 : (type >= 1061 && type <= 1063 ? 1062 : (type == 1672 ? 1672 : (type == 1190 || type == 1191 ? 1190 : 1122)))))))));
            if (grade == 1) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(163, 61224);
                            list.put(164, 61224);
                            list.put(165, 61224);
                            list.put(166, 61224);
                            list.put(10005, 61224);
                            list.put(10006, 61224);
                            list.put(10011, 40816);
                            list.put(10012, 40816);
                            list.put(10041, 61224);
                            list.put(10042, 61224);
                            list.put(10043, 61224);
                            list.put(10044, 61224);
                            list.put(10051, 20408);
                            list.put(10052, 20408);
                            list.put(10055, 20408);
                            list.put(10070, 20408);
                            list.put(203, 40816);
                            list.put(10202, 20408);
                            list.put(10207, 20408);
                            list.put(10222, 20408);
                            list.put(10227, 20408);
                            list.put(10232, 20408);
                            list.put(10236, 20408);
                            list.put(10242, 20408);
                            list.put(10247, 20408);
                            list.put(10291, 20408);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 122727);
                            list.put(2, 122727);
                            list.put(3, 122727);
                            list.put(4, 122727);
                            list.put(5, 122727);
                            list.put(6, 122727);
                            list.put(11, 81818);
                            list.put(12, 81818);
                            list.put(163, 6122);
                            list.put(164, 6122);
                            list.put(165, 6122);
                            list.put(166, 6122);
                            list.put(10005, 6122);
                            list.put(10006, 6122);
                            list.put(10011, 4082);
                            list.put(10012, 4082);
                            list.put(10041, 6122);
                            list.put(10042, 6122);
                            list.put(10043, 6122);
                            list.put(10044, 6122);
                            list.put(10051, 2041);
                            list.put(10052, 2041);
                            list.put(10055, 2041);
                            list.put(10070, 2041);
                            list.put(203, 4082);
                            list.put(10202, 2041);
                            list.put(10207, 2041);
                            list.put(10222, 2041);
                            list.put(10227, 2041);
                            list.put(10232, 2041);
                            list.put(10236, 2041);
                            list.put(10242, 2041);
                            list.put(10247, 2041);
                            list.put(10291, 2041);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 135000);
                        list.put(2, 135000);
                        list.put(3, 135000);
                        list.put(4, 135000);
                        list.put(5, 135000);
                        list.put(6, 135000);
                        list.put(11, 90000);
                        list.put(12, 90000);
                        list.put(163, 612);
                        list.put(164, 612);
                        list.put(165, 612);
                        list.put(166, 612);
                        list.put(10005, 612);
                        list.put(10006, 612);
                        list.put(10011, 408);
                        list.put(10012, 408);
                        list.put(10041, 612);
                        list.put(10042, 612);
                        list.put(10043, 612);
                        list.put(10044, 612);
                        list.put(10051, 204);
                        list.put(10052, 204);
                        list.put(10055, 204);
                        list.put(10070, 204);
                        list.put(203, 408);
                        list.put(10202, 204);
                        list.put(10207, 204);
                        list.put(10222, 204);
                        list.put(10227, 204);
                        list.put(10232, 204);
                        list.put(10236, 204);
                        list.put(10242, 204);
                        list.put(10247, 204);
                        list.put(10291, 204);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(163, 61224);
                            list.put(164, 61224);
                            list.put(165, 61224);
                            list.put(166, 61224);
                            list.put(10005, 61224);
                            list.put(10006, 61224);
                            list.put(10011, 40816);
                            list.put(10012, 40816);
                            list.put(10041, 61224);
                            list.put(10042, 61224);
                            list.put(10043, 61224);
                            list.put(10044, 61224);
                            list.put(10051, 20408);
                            list.put(10052, 20408);
                            list.put(10055, 20408);
                            list.put(10070, 20408);
                            list.put(203, 40816);
                            list.put(10202, 20408);
                            list.put(10207, 20408);
                            list.put(10222, 20408);
                            list.put(10227, 20408);
                            list.put(10232, 20408);
                            list.put(10236, 20408);
                            list.put(10242, 20408);
                            list.put(10247, 20408);
                            list.put(10291, 20408);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 122727);
                            list.put(2, 122727);
                            list.put(3, 122727);
                            list.put(4, 122727);
                            list.put(5, 122727);
                            list.put(6, 122727);
                            list.put(11, 81818);
                            list.put(12, 81818);
                            list.put(163, 6122);
                            list.put(164, 6122);
                            list.put(165, 6122);
                            list.put(166, 6122);
                            list.put(10005, 6122);
                            list.put(10006, 6122);
                            list.put(10011, 4082);
                            list.put(10012, 4082);
                            list.put(10041, 6122);
                            list.put(10042, 6122);
                            list.put(10043, 6122);
                            list.put(10044, 6122);
                            list.put(10051, 2041);
                            list.put(10052, 2041);
                            list.put(10055, 2041);
                            list.put(10070, 2041);
                            list.put(203, 4082);
                            list.put(10202, 2041);
                            list.put(10207, 2041);
                            list.put(10222, 2041);
                            list.put(10227, 2041);
                            list.put(10232, 2041);
                            list.put(10236, 2041);
                            list.put(10242, 2041);
                            list.put(10247, 2041);
                            list.put(10291, 2041);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 135000);
                        list.put(2, 135000);
                        list.put(3, 135000);
                        list.put(4, 135000);
                        list.put(5, 135000);
                        list.put(6, 135000);
                        list.put(11, 90000);
                        list.put(12, 90000);
                        list.put(163, 612);
                        list.put(164, 612);
                        list.put(165, 612);
                        list.put(166, 612);
                        list.put(10005, 612);
                        list.put(10006, 612);
                        list.put(10011, 408);
                        list.put(10012, 408);
                        list.put(10041, 612);
                        list.put(10042, 612);
                        list.put(10043, 612);
                        list.put(10044, 612);
                        list.put(10051, 204);
                        list.put(10052, 204);
                        list.put(10055, 204);
                        list.put(10070, 204);
                        list.put(203, 408);
                        list.put(10202, 204);
                        list.put(10207, 204);
                        list.put(10222, 204);
                        list.put(10227, 204);
                        list.put(10232, 204);
                        list.put(10236, 204);
                        list.put(10242, 204);
                        list.put(10247, 204);
                        list.put(10291, 204);
                        break;
                    }
                }
            } else if (grade == 2) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(20041, 108696);
                            list.put(20042, 108696);
                            list.put(20043, 108696);
                            list.put(20044, 108696);
                            list.put(20045, 108696);
                            list.put(20046, 108696);
                            list.put(20051, 43478);
                            list.put(20052, 43478);
                            list.put(20055, 43478);
                            list.put(20070, 43478);
                            list.put(20086, 43478);
                            list.put(20202, 43478);
                            list.put(20207, 43478);
                            list.put(10291, 43478);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 55102);
                            list.put(164, 55102);
                            list.put(165, 55102);
                            list.put(166, 55102);
                            list.put(10005, 55102);
                            list.put(10006, 55102);
                            list.put(10011, 36735);
                            list.put(10012, 36735);
                            list.put(10041, 55102);
                            list.put(10042, 55102);
                            list.put(10043, 55102);
                            list.put(10044, 55102);
                            list.put(10051, 18367);
                            list.put(10052, 18367);
                            list.put(10055, 18367);
                            list.put(10070, 18367);
                            list.put(203, 36735);
                            list.put(10202, 18367);
                            list.put(10207, 18367);
                            list.put(10222, 18367);
                            list.put(10227, 18367);
                            list.put(10232, 18367);
                            list.put(10236, 18367);
                            list.put(10242, 18367);
                            list.put(10247, 18367);
                            list.put(10291, 18367);
                            list.put(20041, 10870);
                            list.put(20042, 10870);
                            list.put(20043, 10870);
                            list.put(20044, 10870);
                            list.put(20045, 10870);
                            list.put(20046, 10870);
                            list.put(20051, 4348);
                            list.put(20052, 4348);
                            list.put(20055, 4348);
                            list.put(20070, 4348);
                            list.put(20086, 4348);
                            list.put(20202, 4348);
                            list.put(20207, 4348);
                            list.put(10291, 4348);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 60612);
                        list.put(164, 60612);
                        list.put(165, 60612);
                        list.put(166, 60612);
                        list.put(10005, 60612);
                        list.put(10006, 60612);
                        list.put(10011, 40408);
                        list.put(10012, 40408);
                        list.put(10041, 60612);
                        list.put(10042, 60612);
                        list.put(10043, 60612);
                        list.put(10044, 60612);
                        list.put(10051, 20204);
                        list.put(10052, 20204);
                        list.put(10055, 20204);
                        list.put(10070, 20204);
                        list.put(203, 40408);
                        list.put(10202, 20204);
                        list.put(10207, 20204);
                        list.put(10222, 20204);
                        list.put(10227, 20204);
                        list.put(10232, 20204);
                        list.put(10236, 20204);
                        list.put(10242, 20204);
                        list.put(10247, 20204);
                        list.put(10291, 20204);
                        list.put(20041, 1087);
                        list.put(20042, 1087);
                        list.put(20043, 1087);
                        list.put(20044, 1087);
                        list.put(20045, 1087);
                        list.put(20046, 1087);
                        list.put(20051, 435);
                        list.put(20052, 435);
                        list.put(20055, 435);
                        list.put(20070, 435);
                        list.put(20086, 435);
                        list.put(20202, 435);
                        list.put(20207, 435);
                        list.put(10291, 435);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(20041, 108696);
                            list.put(20042, 108696);
                            list.put(20043, 108696);
                            list.put(20044, 108696);
                            list.put(20045, 108696);
                            list.put(20046, 108696);
                            list.put(20051, 43478);
                            list.put(20052, 43478);
                            list.put(20055, 43478);
                            list.put(20070, 43478);
                            list.put(20086, 43478);
                            list.put(20202, 43478);
                            list.put(20207, 43478);
                            list.put(10291, 43478);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 55102);
                            list.put(164, 55102);
                            list.put(165, 55102);
                            list.put(166, 55102);
                            list.put(10005, 55102);
                            list.put(10006, 55102);
                            list.put(10011, 36735);
                            list.put(10012, 36735);
                            list.put(10041, 55102);
                            list.put(10042, 55102);
                            list.put(10043, 55102);
                            list.put(10044, 55102);
                            list.put(10051, 18367);
                            list.put(10052, 18367);
                            list.put(10055, 18367);
                            list.put(10070, 18367);
                            list.put(203, 36735);
                            list.put(10202, 18367);
                            list.put(10207, 18367);
                            list.put(10222, 18367);
                            list.put(10227, 18367);
                            list.put(10232, 18367);
                            list.put(10236, 18367);
                            list.put(10242, 18367);
                            list.put(10247, 18367);
                            list.put(10291, 18367);
                            list.put(20041, 10870);
                            list.put(20042, 10870);
                            list.put(20043, 10870);
                            list.put(20044, 10870);
                            list.put(20045, 10870);
                            list.put(20046, 10870);
                            list.put(20051, 4348);
                            list.put(20052, 4348);
                            list.put(20055, 4348);
                            list.put(20070, 4348);
                            list.put(20086, 4348);
                            list.put(20202, 4348);
                            list.put(20207, 4348);
                            list.put(10291, 4348);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 60612);
                        list.put(164, 60612);
                        list.put(165, 60612);
                        list.put(166, 60612);
                        list.put(10005, 60612);
                        list.put(10006, 60612);
                        list.put(10011, 40408);
                        list.put(10012, 40408);
                        list.put(10041, 60612);
                        list.put(10042, 60612);
                        list.put(10043, 60612);
                        list.put(10044, 60612);
                        list.put(10051, 20204);
                        list.put(10052, 20204);
                        list.put(10055, 20204);
                        list.put(10070, 20204);
                        list.put(203, 40408);
                        list.put(10202, 20204);
                        list.put(10207, 20204);
                        list.put(10222, 20204);
                        list.put(10227, 20204);
                        list.put(10232, 20204);
                        list.put(10236, 20204);
                        list.put(10242, 20204);
                        list.put(10247, 20204);
                        list.put(10291, 20204);
                        list.put(20041, 1087);
                        list.put(20042, 1087);
                        list.put(20043, 1087);
                        list.put(20044, 1087);
                        list.put(20045, 1087);
                        list.put(20046, 1087);
                        list.put(20051, 435);
                        list.put(20052, 435);
                        list.put(20055, 435);
                        list.put(20070, 435);
                        list.put(20086, 435);
                        list.put(20202, 435);
                        list.put(20207, 435);
                        list.put(10291, 435);
                        break;
                    }
                }
            } else if (grade == 3) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(30041, 111111);
                            list.put(30042, 111111);
                            list.put(30043, 111111);
                            list.put(30044, 111111);
                            list.put(30051, 66667);
                            list.put(30052, 66667);
                            list.put(30055, 88889);
                            list.put(30070, 66667);
                            list.put(30086, 88889);
                            list.put(30291, 66667);
                            list.put(30601, 66667);
                            list.put(40601, 44444);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 97826);
                            list.put(20042, 97826);
                            list.put(20043, 97826);
                            list.put(20044, 97826);
                            list.put(20045, 97826);
                            list.put(20046, 97826);
                            list.put(20051, 39130);
                            list.put(20052, 39130);
                            list.put(20055, 39130);
                            list.put(20070, 39130);
                            list.put(20086, 39130);
                            list.put(20202, 39130);
                            list.put(20207, 39130);
                            list.put(10291, 39130);
                            list.put(30041, 11111);
                            list.put(30042, 11111);
                            list.put(30043, 11111);
                            list.put(30044, 11111);
                            list.put(30051, 6667);
                            list.put(30052, 6667);
                            list.put(30055, 8889);
                            list.put(30070, 6667);
                            list.put(30086, 8889);
                            list.put(30291, 6667);
                            list.put(30601, 6667);
                            list.put(40601, 4444);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 107609);
                        list.put(20042, 107609);
                        list.put(20043, 107609);
                        list.put(20044, 107609);
                        list.put(20045, 107609);
                        list.put(20046, 107609);
                        list.put(20051, 43043);
                        list.put(20052, 43043);
                        list.put(20055, 43043);
                        list.put(20070, 43043);
                        list.put(20086, 43043);
                        list.put(20202, 43043);
                        list.put(20207, 43043);
                        list.put(10291, 43043);
                        list.put(30041, 1111);
                        list.put(30042, 1111);
                        list.put(30043, 1111);
                        list.put(30044, 1111);
                        list.put(30051, 667);
                        list.put(30052, 667);
                        list.put(30055, 889);
                        list.put(30070, 667);
                        list.put(30086, 889);
                        list.put(30291, 667);
                        list.put(30601, 667);
                        list.put(40601, 444);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(30041, 80645);
                            list.put(30042, 80645);
                            list.put(30043, 80645);
                            list.put(30044, 80645);
                            list.put(30054, 64516);
                            list.put(30045, 96774);
                            list.put(30046, 96774);
                            list.put(30086, 64516);
                            list.put(30106, 64516);
                            list.put(30107, 32258);
                            list.put(30357, 64516);
                            list.put(30356, 64516);
                            list.put(30551, 64516);
                            list.put(31002, 64516);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 8065);
                            list.put(30042, 8065);
                            list.put(30043, 8065);
                            list.put(30044, 8065);
                            list.put(30054, 9677);
                            list.put(30045, 9677);
                            list.put(30046, 9677);
                            list.put(30086, 6452);
                            list.put(30106, 6452);
                            list.put(30107, 3226);
                            list.put(30357, 6452);
                            list.put(30356, 6452);
                            list.put(30551, 6452);
                            list.put(31002, 6452);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 806);
                        list.put(30042, 806);
                        list.put(30043, 806);
                        list.put(30044, 806);
                        list.put(30054, 968);
                        list.put(30045, 968);
                        list.put(30046, 968);
                        list.put(30086, 645);
                        list.put(30106, 645);
                        list.put(30107, 323);
                        list.put(30357, 645);
                        list.put(30356, 645);
                        list.put(30551, 645);
                        list.put(31002, 645);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(30041, 96154);
                            list.put(30042, 96154);
                            list.put(30043, 96154);
                            list.put(30044, 96154);
                            list.put(30054, 76923);
                            list.put(30045, 115385);
                            list.put(30046, 115385);
                            list.put(30086, 76923);
                            list.put(30357, 76923);
                            list.put(30356, 76923);
                            list.put(30551, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 9615);
                            list.put(30042, 9615);
                            list.put(30043, 9615);
                            list.put(30044, 9615);
                            list.put(30054, 7692);
                            list.put(30045, 11538);
                            list.put(30046, 11538);
                            list.put(30086, 7692);
                            list.put(30357, 7692);
                            list.put(30356, 7692);
                            list.put(30551, 7692);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 962);
                        list.put(30042, 962);
                        list.put(30043, 962);
                        list.put(30044, 962);
                        list.put(30054, 769);
                        list.put(30045, 1154);
                        list.put(30046, 1154);
                        list.put(30086, 769);
                        list.put(30357, 769);
                        list.put(30356, 769);
                        list.put(30551, 769);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(30041, 96154);
                            list.put(30042, 96154);
                            list.put(30043, 96154);
                            list.put(30044, 96154);
                            list.put(30054, 76923);
                            list.put(30045, 115385);
                            list.put(30046, 115385);
                            list.put(30086, 76923);
                            list.put(30357, 76923);
                            list.put(30356, 76923);
                            list.put(30551, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 9615);
                            list.put(30042, 9615);
                            list.put(30043, 9615);
                            list.put(30044, 9615);
                            list.put(30054, 7692);
                            list.put(30045, 11538);
                            list.put(30046, 11538);
                            list.put(30086, 7692);
                            list.put(30357, 7692);
                            list.put(30356, 7692);
                            list.put(30551, 7692);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 962);
                        list.put(30042, 962);
                        list.put(30043, 962);
                        list.put(30044, 962);
                        list.put(30054, 769);
                        list.put(30045, 1154);
                        list.put(30046, 1154);
                        list.put(30086, 769);
                        list.put(30357, 769);
                        list.put(30356, 769);
                        list.put(30551, 769);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(30041, 75758);
                            list.put(30042, 75758);
                            list.put(30043, 75758);
                            list.put(30044, 75758);
                            list.put(30054, 60606);
                            list.put(30045, 90909);
                            list.put(30046, 90909);
                            list.put(30086, 60606);
                            list.put(30371, 60606);
                            list.put(30366, 60606);
                            list.put(30357, 60606);
                            list.put(30356, 60606);
                            list.put(30551, 60606);
                            list.put(30376, 60606);
                            list.put(30377, 30303);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 118421);
                            list.put(20042, 118421);
                            list.put(20043, 118421);
                            list.put(20044, 118421);
                            list.put(20045, 118421);
                            list.put(20046, 118421);
                            list.put(20054, 71053);
                            list.put(20086, 47368);
                            list.put(20366, 71053);
                            list.put(30041, 7576);
                            list.put(30042, 7576);
                            list.put(30043, 7576);
                            list.put(30044, 7576);
                            list.put(30054, 6061);
                            list.put(30045, 9091);
                            list.put(30046, 9091);
                            list.put(30086, 6061);
                            list.put(30371, 6061);
                            list.put(30366, 6061);
                            list.put(30357, 6061);
                            list.put(30356, 6061);
                            list.put(30551, 6061);
                            list.put(30376, 6061);
                            list.put(30377, 3030);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 130263);
                        list.put(20042, 130263);
                        list.put(20043, 130263);
                        list.put(20044, 130263);
                        list.put(20045, 130263);
                        list.put(20046, 130263);
                        list.put(20054, 78158);
                        list.put(20086, 52105);
                        list.put(20366, 78158);
                        list.put(30041, 758);
                        list.put(30042, 758);
                        list.put(30043, 758);
                        list.put(30044, 758);
                        list.put(30054, 606);
                        list.put(30045, 909);
                        list.put(30046, 909);
                        list.put(30086, 606);
                        list.put(30371, 606);
                        list.put(30366, 606);
                        list.put(30357, 606);
                        list.put(30356, 606);
                        list.put(30551, 606);
                        list.put(30376, 606);
                        list.put(30377, 303);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(30041, 89286);
                            list.put(30042, 89286);
                            list.put(30043, 89286);
                            list.put(30044, 89286);
                            list.put(30054, 71429);
                            list.put(30045, 107143);
                            list.put(30046, 107143);
                            list.put(30086, 71429);
                            list.put(30357, 71429);
                            list.put(30356, 71429);
                            list.put(30551, 71429);
                            list.put(31001, 71429);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 8929);
                            list.put(30042, 8929);
                            list.put(30043, 8929);
                            list.put(30044, 8929);
                            list.put(30054, 7143);
                            list.put(30045, 10714);
                            list.put(30046, 10714);
                            list.put(30086, 7143);
                            list.put(30357, 7143);
                            list.put(30356, 7143);
                            list.put(30551, 7143);
                            list.put(31001, 7143);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 893);
                        list.put(30042, 893);
                        list.put(30043, 893);
                        list.put(30044, 893);
                        list.put(30054, 714);
                        list.put(30045, 1071);
                        list.put(30046, 1071);
                        list.put(30086, 714);
                        list.put(30357, 714);
                        list.put(30356, 714);
                        list.put(30551, 714);
                        list.put(31001, 714);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(30041, 83333);
                            list.put(30042, 83333);
                            list.put(30043, 83333);
                            list.put(30044, 83333);
                            list.put(30054, 66667);
                            list.put(30045, 100000);
                            list.put(30046, 100000);
                            list.put(30086, 66667);
                            list.put(32091, 16667);
                            list.put(32092, 16667);
                            list.put(32093, 16667);
                            list.put(32094, 16667);
                            list.put(30357, 66667);
                            list.put(30356, 66667);
                            list.put(30551, 66667);
                            list.put(31003, 66667);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 109756);
                            list.put(20042, 109756);
                            list.put(20043, 109756);
                            list.put(20044, 109756);
                            list.put(20045, 109756);
                            list.put(20046, 109756);
                            list.put(20054, 65854);
                            list.put(20086, 43902);
                            list.put(30041, 8333);
                            list.put(30042, 8333);
                            list.put(30043, 8333);
                            list.put(30044, 8333);
                            list.put(30054, 6667);
                            list.put(30045, 10000);
                            list.put(30046, 10000);
                            list.put(30086, 6667);
                            list.put(32091, 1667);
                            list.put(32092, 1667);
                            list.put(32093, 1667);
                            list.put(32094, 1667);
                            list.put(30357, 6667);
                            list.put(30356, 6667);
                            list.put(30551, 6667);
                            list.put(31003, 6667);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 120732);
                        list.put(20042, 120732);
                        list.put(20043, 120732);
                        list.put(20044, 120732);
                        list.put(20045, 120732);
                        list.put(20046, 120732);
                        list.put(20054, 72439);
                        list.put(20086, 48293);
                        list.put(30041, 833);
                        list.put(30042, 833);
                        list.put(30043, 833);
                        list.put(30044, 833);
                        list.put(30054, 667);
                        list.put(30045, 1000);
                        list.put(30046, 1000);
                        list.put(30086, 667);
                        list.put(32091, 167);
                        list.put(32092, 167);
                        list.put(32093, 167);
                        list.put(32094, 167);
                        list.put(30357, 667);
                        list.put(30356, 667);
                        list.put(30551, 667);
                        list.put(31003, 667);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(30041, 89286);
                            list.put(30042, 89286);
                            list.put(30043, 89286);
                            list.put(30044, 89286);
                            list.put(30054, 71429);
                            list.put(30045, 107143);
                            list.put(30046, 107143);
                            list.put(30086, 71429);
                            list.put(30357, 71429);
                            list.put(30356, 71429);
                            list.put(30551, 71429);
                            list.put(31004, 71429);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 8929);
                            list.put(30042, 8929);
                            list.put(30043, 8929);
                            list.put(30044, 8929);
                            list.put(30054, 7143);
                            list.put(30045, 10714);
                            list.put(30046, 10714);
                            list.put(30086, 7143);
                            list.put(30357, 7143);
                            list.put(30356, 7143);
                            list.put(30551, 7143);
                            list.put(31004, 7143);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 893);
                        list.put(30042, 893);
                        list.put(30043, 893);
                        list.put(30044, 893);
                        list.put(30054, 714);
                        list.put(30045, 1071);
                        list.put(30046, 1071);
                        list.put(30086, 714);
                        list.put(30357, 714);
                        list.put(30356, 714);
                        list.put(30551, 714);
                        list.put(31004, 714);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(30041, 113636);
                            list.put(30042, 113636);
                            list.put(30043, 113636);
                            list.put(30044, 113636);
                            list.put(30054, 90909);
                            list.put(30045, 136364);
                            list.put(30046, 136364);
                            list.put(30086, 90909);
                            list.put(30551, 90909);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 11364);
                            list.put(30042, 11364);
                            list.put(30043, 11364);
                            list.put(30044, 11364);
                            list.put(30054, 9091);
                            list.put(30045, 13636);
                            list.put(30046, 13636);
                            list.put(30086, 9091);
                            list.put(30551, 9091);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 1136);
                        list.put(30042, 1136);
                        list.put(30043, 1136);
                        list.put(30044, 1136);
                        list.put(30054, 909);
                        list.put(30045, 1364);
                        list.put(30046, 1364);
                        list.put(30086, 909);
                        list.put(30551, 909);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(30041, 113636);
                            list.put(30042, 113636);
                            list.put(30043, 113636);
                            list.put(30044, 113636);
                            list.put(30054, 90909);
                            list.put(30045, 136364);
                            list.put(30046, 136364);
                            list.put(30086, 90909);
                            list.put(30551, 90909);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 128571);
                            list.put(20042, 128571);
                            list.put(20043, 128571);
                            list.put(20044, 128571);
                            list.put(20045, 128571);
                            list.put(20046, 128571);
                            list.put(20054, 77143);
                            list.put(20086, 51429);
                            list.put(30041, 11364);
                            list.put(30042, 11364);
                            list.put(30043, 11364);
                            list.put(30044, 11364);
                            list.put(30054, 9091);
                            list.put(30045, 13636);
                            list.put(30046, 13636);
                            list.put(30086, 9091);
                            list.put(30551, 9091);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 141429);
                        list.put(20042, 141429);
                        list.put(20043, 141429);
                        list.put(20044, 141429);
                        list.put(20045, 141429);
                        list.put(20046, 141429);
                        list.put(20054, 84857);
                        list.put(20086, 56571);
                        list.put(30041, 1136);
                        list.put(30042, 1136);
                        list.put(30043, 1136);
                        list.put(30044, 1136);
                        list.put(30054, 909);
                        list.put(30045, 1364);
                        list.put(30046, 1364);
                        list.put(30086, 909);
                        list.put(30551, 909);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(30041, 125000);
                            list.put(30042, 125000);
                            list.put(30043, 125000);
                            list.put(30044, 125000);
                            list.put(30051, 75000);
                            list.put(30052, 75000);
                            list.put(30055, 100000);
                            list.put(30070, 75000);
                            list.put(30086, 100000);
                            list.put(30291, 75000);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 97826);
                            list.put(20042, 97826);
                            list.put(20043, 97826);
                            list.put(20044, 97826);
                            list.put(20045, 97826);
                            list.put(20046, 97826);
                            list.put(20051, 39130);
                            list.put(20052, 39130);
                            list.put(20055, 39130);
                            list.put(20070, 39130);
                            list.put(20086, 39130);
                            list.put(20202, 39130);
                            list.put(20207, 39130);
                            list.put(10291, 39130);
                            list.put(30041, 12500);
                            list.put(30042, 12500);
                            list.put(30043, 12500);
                            list.put(30044, 12500);
                            list.put(30051, 7500);
                            list.put(30052, 7500);
                            list.put(30055, 10000);
                            list.put(30070, 7500);
                            list.put(30086, 10000);
                            list.put(30291, 7500);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 107609);
                        list.put(20042, 107609);
                        list.put(20043, 107609);
                        list.put(20044, 107609);
                        list.put(20045, 107609);
                        list.put(20046, 107609);
                        list.put(20051, 43043);
                        list.put(20052, 43043);
                        list.put(20055, 43043);
                        list.put(20070, 43043);
                        list.put(20086, 43043);
                        list.put(20202, 43043);
                        list.put(20207, 43043);
                        list.put(10291, 43043);
                        list.put(30041, 1250);
                        list.put(30042, 1250);
                        list.put(30043, 1250);
                        list.put(30044, 1250);
                        list.put(30051, 750);
                        list.put(30052, 750);
                        list.put(30055, 1000);
                        list.put(30070, 750);
                        list.put(30086, 1000);
                        list.put(30291, 750);
                        break;
                    }
                }
            } else if (grade == 4) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(40041, 97561);
                            list.put(40042, 97561);
                            list.put(40043, 97561);
                            list.put(40044, 97561);
                            list.put(40051, 48780);
                            list.put(40052, 48780);
                            list.put(40055, 48780);
                            list.put(40070, 48780);
                            list.put(40086, 73171);
                            list.put(42095, 48780);
                            list.put(42096, 48780);
                            list.put(40291, 48780);
                            list.put(40292, 48780);
                            list.put(40601, 48780);
                            list.put(40602, 48780);
                            list.put(40603, 48780);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 100000);
                            list.put(30042, 100000);
                            list.put(30043, 100000);
                            list.put(30044, 100000);
                            list.put(30051, 60000);
                            list.put(30052, 60000);
                            list.put(30055, 80000);
                            list.put(30070, 60000);
                            list.put(30086, 80000);
                            list.put(30291, 60000);
                            list.put(30601, 60000);
                            list.put(40601, 40000);
                            list.put(40041, 9756);
                            list.put(40042, 9756);
                            list.put(40043, 9756);
                            list.put(40044, 9756);
                            list.put(40051, 4878);
                            list.put(40052, 4878);
                            list.put(40055, 4878);
                            list.put(40070, 4878);
                            list.put(40086, 7317);
                            list.put(42095, 4878);
                            list.put(42096, 4878);
                            list.put(40291, 4878);
                            list.put(40292, 4878);
                            list.put(40601, 4878);
                            list.put(40602, 4878);
                            list.put(40603, 4878);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 110000);
                        list.put(30042, 110000);
                        list.put(30043, 110000);
                        list.put(30044, 110000);
                        list.put(30051, 66000);
                        list.put(30052, 66000);
                        list.put(30055, 88000);
                        list.put(30070, 66000);
                        list.put(30086, 88000);
                        list.put(30291, 66000);
                        list.put(30601, 66000);
                        list.put(40601, 44000);
                        list.put(40041, 976);
                        list.put(40042, 976);
                        list.put(40043, 976);
                        list.put(40044, 976);
                        list.put(40051, 488);
                        list.put(40052, 488);
                        list.put(40055, 488);
                        list.put(40070, 488);
                        list.put(40086, 732);
                        list.put(42095, 488);
                        list.put(42096, 488);
                        list.put(40291, 488);
                        list.put(40292, 488);
                        list.put(40601, 488);
                        list.put(40602, 488);
                        list.put(40603, 488);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(40041, 80000);
                            list.put(40042, 80000);
                            list.put(40043, 80000);
                            list.put(40044, 80000);
                            list.put(40054, 80000);
                            list.put(40045, 80000);
                            list.put(40046, 80000);
                            list.put(40086, 60000);
                            list.put(40106, 60000);
                            list.put(40107, 40000);
                            list.put(40357, 60000);
                            list.put(40356, 60000);
                            list.put(40557, 40000);
                            list.put(40556, 60000);
                            list.put(41006, 60000);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 72581);
                            list.put(30042, 72581);
                            list.put(30043, 72581);
                            list.put(30044, 72581);
                            list.put(30054, 58065);
                            list.put(30045, 87097);
                            list.put(30046, 87097);
                            list.put(30086, 58065);
                            list.put(30106, 58065);
                            list.put(30107, 29032);
                            list.put(30357, 58065);
                            list.put(30356, 58065);
                            list.put(30551, 58065);
                            list.put(31002, 58065);
                            list.put(40041, 8000);
                            list.put(40042, 8000);
                            list.put(40043, 8000);
                            list.put(40044, 8000);
                            list.put(40054, 8000);
                            list.put(40045, 8000);
                            list.put(40046, 8000);
                            list.put(40086, 6000);
                            list.put(40106, 6000);
                            list.put(40107, 4000);
                            list.put(40357, 6000);
                            list.put(40356, 6000);
                            list.put(40557, 4000);
                            list.put(40556, 6000);
                            list.put(41006, 6000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 79839);
                        list.put(30042, 79839);
                        list.put(30043, 79839);
                        list.put(30044, 79839);
                        list.put(30045, 95806);
                        list.put(30046, 95806);
                        list.put(30054, 63871);
                        list.put(30086, 63871);
                        list.put(30106, 63871);
                        list.put(30107, 31935);
                        list.put(30357, 63871);
                        list.put(30356, 63871);
                        list.put(30551, 63871);
                        list.put(31002, 63871);
                        list.put(40041, 800);
                        list.put(40042, 800);
                        list.put(40043, 800);
                        list.put(40044, 800);
                        list.put(40054, 800);
                        list.put(40045, 800);
                        list.put(40046, 800);
                        list.put(40086, 600);
                        list.put(40106, 600);
                        list.put(40107, 400);
                        list.put(40357, 600);
                        list.put(40356, 600);
                        list.put(40557, 400);
                        list.put(40556, 600);
                        list.put(41006, 600);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(40041, 108108);
                            list.put(40042, 108108);
                            list.put(40043, 108108);
                            list.put(40044, 108108);
                            list.put(40054, 108108);
                            list.put(40045, 108108);
                            list.put(40046, 108108);
                            list.put(40086, 81081);
                            list.put(40357, 81081);
                            list.put(40356, 81081);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 86538);
                            list.put(30042, 86538);
                            list.put(30043, 86538);
                            list.put(30044, 86538);
                            list.put(30054, 69231);
                            list.put(30045, 103846);
                            list.put(30046, 103846);
                            list.put(30086, 69231);
                            list.put(30357, 69231);
                            list.put(30356, 69231);
                            list.put(30551, 69231);
                            list.put(40041, 10811);
                            list.put(40042, 10811);
                            list.put(40043, 10811);
                            list.put(40044, 10811);
                            list.put(40054, 10811);
                            list.put(40045, 10811);
                            list.put(40046, 10811);
                            list.put(40086, 8108);
                            list.put(40357, 8108);
                            list.put(40356, 8108);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 95192);
                        list.put(30042, 95192);
                        list.put(30043, 95192);
                        list.put(30044, 95192);
                        list.put(30054, 76154);
                        list.put(30045, 114231);
                        list.put(30046, 114231);
                        list.put(30086, 76154);
                        list.put(30357, 76154);
                        list.put(30356, 76154);
                        list.put(30551, 76154);
                        list.put(40041, 1081);
                        list.put(40042, 1081);
                        list.put(40043, 1081);
                        list.put(40044, 1081);
                        list.put(40054, 1081);
                        list.put(40045, 1081);
                        list.put(40046, 1081);
                        list.put(40086, 811);
                        list.put(40357, 811);
                        list.put(40356, 811);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(40041, 108108);
                            list.put(40042, 108108);
                            list.put(40043, 108108);
                            list.put(40044, 108108);
                            list.put(40054, 108108);
                            list.put(40045, 108108);
                            list.put(40046, 108108);
                            list.put(40086, 81081);
                            list.put(40357, 81081);
                            list.put(40356, 81081);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 86538);
                            list.put(30042, 86538);
                            list.put(30043, 86538);
                            list.put(30044, 86538);
                            list.put(30054, 69231);
                            list.put(30045, 103846);
                            list.put(30046, 103846);
                            list.put(30086, 69231);
                            list.put(30357, 69231);
                            list.put(30356, 69231);
                            list.put(30551, 69231);
                            list.put(40041, 10811);
                            list.put(40042, 10811);
                            list.put(40043, 10811);
                            list.put(40044, 10811);
                            list.put(40054, 10811);
                            list.put(40045, 10811);
                            list.put(40046, 10811);
                            list.put(40086, 8108);
                            list.put(40357, 8108);
                            list.put(40356, 8108);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 95192);
                        list.put(30042, 95192);
                        list.put(30043, 95192);
                        list.put(30044, 95192);
                        list.put(30054, 76154);
                        list.put(30045, 114231);
                        list.put(30046, 114231);
                        list.put(30086, 76154);
                        list.put(30357, 76154);
                        list.put(30356, 76154);
                        list.put(30551, 76154);
                        list.put(40041, 1081);
                        list.put(40042, 1081);
                        list.put(40043, 1081);
                        list.put(40044, 1081);
                        list.put(40054, 1081);
                        list.put(40045, 1081);
                        list.put(40046, 1081);
                        list.put(40086, 811);
                        list.put(40357, 811);
                        list.put(40356, 811);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(40041, 88889);
                            list.put(40042, 88889);
                            list.put(40043, 88889);
                            list.put(40044, 88889);
                            list.put(40054, 88889);
                            list.put(40045, 88889);
                            list.put(40046, 88889);
                            list.put(40086, 66667);
                            list.put(40111, 44444);
                            list.put(40357, 66667);
                            list.put(40356, 66667);
                            list.put(40366, 66667);
                            list.put(40371, 66667);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 68182);
                            list.put(30042, 68182);
                            list.put(30043, 68182);
                            list.put(30044, 68182);
                            list.put(30054, 54545);
                            list.put(30045, 81818);
                            list.put(30046, 81818);
                            list.put(30086, 54545);
                            list.put(30371, 54545);
                            list.put(30366, 54545);
                            list.put(30357, 54545);
                            list.put(30356, 54545);
                            list.put(30551, 54545);
                            list.put(30376, 54545);
                            list.put(30377, 27273);
                            list.put(40041, 8889);
                            list.put(40042, 8889);
                            list.put(40043, 8889);
                            list.put(40044, 8889);
                            list.put(40054, 8889);
                            list.put(40045, 8889);
                            list.put(40046, 8889);
                            list.put(40086, 6667);
                            list.put(40111, 4444);
                            list.put(40357, 6667);
                            list.put(40356, 6667);
                            list.put(40366, 6667);
                            list.put(40371, 6667);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 75000);
                        list.put(30042, 75000);
                        list.put(30043, 75000);
                        list.put(30044, 75000);
                        list.put(30054, 60000);
                        list.put(30045, 90000);
                        list.put(30046, 90000);
                        list.put(30086, 60000);
                        list.put(30371, 60000);
                        list.put(30366, 60000);
                        list.put(30357, 60000);
                        list.put(30356, 60000);
                        list.put(30551, 60000);
                        list.put(30376, 60000);
                        list.put(30377, 30000);
                        list.put(40041, 889);
                        list.put(40042, 889);
                        list.put(40043, 889);
                        list.put(40044, 889);
                        list.put(40054, 889);
                        list.put(40045, 889);
                        list.put(40046, 889);
                        list.put(40086, 667);
                        list.put(40111, 444);
                        list.put(40357, 667);
                        list.put(40356, 667);
                        list.put(40366, 667);
                        list.put(40371, 667);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(40041, 100000);
                            list.put(40042, 100000);
                            list.put(40043, 100000);
                            list.put(40044, 100000);
                            list.put(40054, 100000);
                            list.put(40045, 100000);
                            list.put(40046, 100000);
                            list.put(40086, 75000);
                            list.put(40357, 75000);
                            list.put(40356, 75000);
                            list.put(41005, 75000);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 80357);
                            list.put(30042, 80357);
                            list.put(30043, 80357);
                            list.put(30044, 80357);
                            list.put(30054, 64286);
                            list.put(30045, 96429);
                            list.put(30046, 96429);
                            list.put(30086, 64286);
                            list.put(30357, 64286);
                            list.put(30356, 64286);
                            list.put(30551, 64286);
                            list.put(31001, 64286);
                            list.put(40041, 10000);
                            list.put(40042, 10000);
                            list.put(40043, 10000);
                            list.put(40044, 10000);
                            list.put(40054, 10000);
                            list.put(40045, 10000);
                            list.put(40046, 10000);
                            list.put(40086, 7500);
                            list.put(40357, 7500);
                            list.put(40356, 7500);
                            list.put(41005, 7500);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 88393);
                        list.put(30042, 88393);
                        list.put(30043, 88393);
                        list.put(30044, 88393);
                        list.put(30054, 70714);
                        list.put(30045, 106071);
                        list.put(30046, 106071);
                        list.put(30086, 70714);
                        list.put(30357, 70714);
                        list.put(30356, 70714);
                        list.put(30551, 70714);
                        list.put(31001, 70714);
                        list.put(40041, 1000);
                        list.put(40042, 1000);
                        list.put(40043, 1000);
                        list.put(40044, 1000);
                        list.put(40054, 1000);
                        list.put(40045, 1000);
                        list.put(40046, 1000);
                        list.put(40086, 750);
                        list.put(40357, 750);
                        list.put(40356, 750);
                        list.put(41005, 750);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(40041, 90909);
                            list.put(40042, 90909);
                            list.put(40043, 90909);
                            list.put(40044, 90909);
                            list.put(40054, 90909);
                            list.put(40045, 90909);
                            list.put(40046, 90909);
                            list.put(40086, 68182);
                            list.put(40056, 45455);
                            list.put(40357, 68182);
                            list.put(40356, 68182);
                            list.put(41007, 68182);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 75000);
                            list.put(30042, 75000);
                            list.put(30043, 75000);
                            list.put(30044, 75000);
                            list.put(30054, 60000);
                            list.put(30045, 90000);
                            list.put(30046, 90000);
                            list.put(30086, 60000);
                            list.put(32091, 15000);
                            list.put(32092, 15000);
                            list.put(32093, 15000);
                            list.put(32094, 15000);
                            list.put(30357, 60000);
                            list.put(30356, 60000);
                            list.put(30551, 60000);
                            list.put(31003, 60000);
                            list.put(40041, 9091);
                            list.put(40042, 9091);
                            list.put(40043, 9091);
                            list.put(40044, 9091);
                            list.put(40054, 9091);
                            list.put(40045, 9091);
                            list.put(40046, 9091);
                            list.put(40086, 6818);
                            list.put(40056, 4545);
                            list.put(40357, 6818);
                            list.put(40356, 6818);
                            list.put(41007, 6818);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 82500);
                        list.put(30042, 82500);
                        list.put(30043, 82500);
                        list.put(30044, 82500);
                        list.put(30054, 66000);
                        list.put(30045, 99000);
                        list.put(30046, 99000);
                        list.put(30086, 66000);
                        list.put(32091, 16500);
                        list.put(32092, 16500);
                        list.put(32093, 16500);
                        list.put(32094, 16500);
                        list.put(30357, 66000);
                        list.put(30356, 66000);
                        list.put(30551, 66000);
                        list.put(31003, 66000);
                        list.put(40041, 909);
                        list.put(40042, 909);
                        list.put(40043, 909);
                        list.put(40044, 909);
                        list.put(40054, 909);
                        list.put(40045, 909);
                        list.put(40046, 909);
                        list.put(40086, 682);
                        list.put(40056, 455);
                        list.put(40357, 682);
                        list.put(40356, 682);
                        list.put(41007, 682);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(40041, 102564);
                            list.put(40042, 102564);
                            list.put(40043, 102564);
                            list.put(40044, 102564);
                            list.put(40054, 102564);
                            list.put(40045, 102564);
                            list.put(40046, 102564);
                            list.put(40086, 76923);
                            list.put(40086, 51282);
                            list.put(40357, 76923);
                            list.put(40356, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 80357);
                            list.put(30042, 80357);
                            list.put(30043, 80357);
                            list.put(30044, 80357);
                            list.put(30054, 64286);
                            list.put(30045, 96429);
                            list.put(30046, 96429);
                            list.put(30086, 64286);
                            list.put(30357, 64286);
                            list.put(30356, 64286);
                            list.put(30551, 64286);
                            list.put(31004, 64286);
                            list.put(40041, 10256);
                            list.put(40042, 10256);
                            list.put(40043, 10256);
                            list.put(40044, 10256);
                            list.put(40054, 10256);
                            list.put(40045, 10256);
                            list.put(40046, 10256);
                            list.put(40086, 7692);
                            list.put(40086, 5128);
                            list.put(40357, 7692);
                            list.put(40356, 7692);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 88393);
                        list.put(30042, 88393);
                        list.put(30043, 88393);
                        list.put(30044, 88393);
                        list.put(30054, 70714);
                        list.put(30045, 106071);
                        list.put(30046, 106071);
                        list.put(30086, 70714);
                        list.put(30357, 70714);
                        list.put(30356, 70714);
                        list.put(30551, 70714);
                        list.put(31004, 70714);
                        list.put(40041, 1026);
                        list.put(40042, 1026);
                        list.put(40043, 1026);
                        list.put(40044, 1026);
                        list.put(40054, 1026);
                        list.put(40045, 1026);
                        list.put(40046, 1026);
                        list.put(40086, 769);
                        list.put(40086, 513);
                        list.put(40357, 769);
                        list.put(40356, 769);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(40041, 129032);
                            list.put(40042, 129032);
                            list.put(40043, 129032);
                            list.put(40044, 129032);
                            list.put(40054, 129032);
                            list.put(40045, 129032);
                            list.put(40046, 129032);
                            list.put(40086, 96774);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 102273);
                            list.put(30042, 102273);
                            list.put(30043, 102273);
                            list.put(30044, 102273);
                            list.put(30054, 81818);
                            list.put(30045, 122727);
                            list.put(30046, 122727);
                            list.put(30086, 81818);
                            list.put(30551, 81818);
                            list.put(40041, 12903);
                            list.put(40042, 12903);
                            list.put(40043, 12903);
                            list.put(40044, 12903);
                            list.put(40054, 12903);
                            list.put(40045, 12903);
                            list.put(40046, 12903);
                            list.put(40086, 9677);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 112500);
                        list.put(30042, 112500);
                        list.put(30043, 112500);
                        list.put(30044, 112500);
                        list.put(30054, 90000);
                        list.put(30045, 135000);
                        list.put(30046, 135000);
                        list.put(30086, 90000);
                        list.put(30551, 90000);
                        list.put(40041, 1290);
                        list.put(40042, 1290);
                        list.put(40043, 1290);
                        list.put(40044, 1290);
                        list.put(40054, 1290);
                        list.put(40045, 1290);
                        list.put(40046, 1290);
                        list.put(40086, 968);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(40041, 93023);
                            list.put(40042, 93023);
                            list.put(40043, 93023);
                            list.put(40044, 93023);
                            list.put(40054, 93023);
                            list.put(40045, 93023);
                            list.put(40046, 93023);
                            list.put(40086, 69767);
                            list.put(40650, 69767);
                            list.put(40656, 69767);
                            list.put(40501, 69767);
                            list.put(40502, 69767);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 102273);
                            list.put(30042, 102273);
                            list.put(30043, 102273);
                            list.put(30044, 102273);
                            list.put(30054, 81818);
                            list.put(30045, 122727);
                            list.put(30046, 122727);
                            list.put(30086, 81818);
                            list.put(30551, 81818);
                            list.put(40041, 9302);
                            list.put(40042, 9302);
                            list.put(40043, 9302);
                            list.put(40044, 9302);
                            list.put(40054, 9303);
                            list.put(40045, 9302);
                            list.put(40046, 9302);
                            list.put(40086, 6977);
                            list.put(40650, 6977);
                            list.put(40656, 6977);
                            list.put(40501, 6977);
                            list.put(40502, 6977);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 112500);
                        list.put(30042, 112500);
                        list.put(30043, 112500);
                        list.put(30044, 112500);
                        list.put(30054, 90000);
                        list.put(30045, 135000);
                        list.put(30046, 135000);
                        list.put(30086, 90000);
                        list.put(30551, 90000);
                        list.put(40041, 930);
                        list.put(40042, 930);
                        list.put(40043, 930);
                        list.put(40044, 930);
                        list.put(40054, 930);
                        list.put(40045, 930);
                        list.put(40046, 930);
                        list.put(40086, 698);
                        list.put(40650, 698);
                        list.put(40656, 698);
                        list.put(40501, 698);
                        list.put(40502, 698);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(40041, 114286);
                            list.put(40042, 114286);
                            list.put(40043, 114286);
                            list.put(40044, 114286);
                            list.put(40051, 57143);
                            list.put(40052, 57143);
                            list.put(40055, 57143);
                            list.put(40070, 57143);
                            list.put(40086, 85714);
                            list.put(42095, 57143);
                            list.put(42096, 57143);
                            list.put(40291, 57143);
                            list.put(40292, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 112500);
                            list.put(30042, 112500);
                            list.put(30043, 112500);
                            list.put(30044, 112500);
                            list.put(30051, 67500);
                            list.put(30052, 67500);
                            list.put(30055, 90000);
                            list.put(30070, 67500);
                            list.put(30086, 90000);
                            list.put(30291, 67500);
                            list.put(40041, 11429);
                            list.put(40042, 11429);
                            list.put(40043, 11429);
                            list.put(40044, 11429);
                            list.put(40051, 5714);
                            list.put(40052, 5714);
                            list.put(40055, 5714);
                            list.put(40070, 5714);
                            list.put(40086, 8571);
                            list.put(42095, 5714);
                            list.put(42096, 5714);
                            list.put(40291, 5714);
                            list.put(40292, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 123750);
                        list.put(30042, 123750);
                        list.put(30043, 123750);
                        list.put(30044, 123750);
                        list.put(30051, 74250);
                        list.put(30052, 74250);
                        list.put(30055, 99000);
                        list.put(30070, 74250);
                        list.put(30086, 99000);
                        list.put(30291, 74250);
                        list.put(40041, 1143);
                        list.put(40042, 1143);
                        list.put(40043, 1143);
                        list.put(40044, 1143);
                        list.put(40051, 571);
                        list.put(40052, 571);
                        list.put(40055, 571);
                        list.put(40070, 571);
                        list.put(40086, 851);
                        list.put(42095, 571);
                        list.put(42096, 571);
                        list.put(40291, 571);
                        list.put(40292, 571);
                        break;
                    }
                }
            }
            if (!list.isEmpty()) {
                while (potentialid == 0) {
                    int potentialidt = CubeOption.getWeightedRandom(list);
                    if (potentialidt == 0) continue;
                    potentialid = potentialidt;
                    if (num == 3 && !GameConstants.getPotentialCheck(potentialid, args[0], args[1])) {
                        potentialid = 0;
                        continue;
                    }
                    break;
                }
            } else {
                potentialid = 1;
            }
            if (MapleItemInformationProvider.getInstance().getReqLevel(itemid) >= 80) break block154;
            if (potentialid >= 163 && potentialid <= 166) {
                potentialid -= 162;
            }
            if (potentialid == 203) {
                potentialid = MapleItemInformationProvider.getInstance().getReqLevel(itemid) == 10 ? 1 : 201;
            }
        }
        return potentialid;
    }

    /*
     * Opcode count of 19376 triggered aggressive code reduction.  Override with --aggressivesizethreshold.
     */
    public static int getBlackCubePotentialId(int itemid, int grade, int num, int ... args) {
        int potentialid;
        block154: {
            LinkedHashMap<Integer, Integer> list = new LinkedHashMap<Integer, Integer>();
            potentialid = 0;
            int type = itemid / 1000;
            type = GameConstants.isWeapon(itemid) || GameConstants.isSecondaryWeapon(itemid) ? 1302 : (GameConstants.isCap(itemid) ? 1004 : (GameConstants.isCape(itemid) ? 1102 : (type == 1132 || type == 1152 ? 1132 : (GameConstants.isLongcoat(itemid) || type >= 1040 && type <= 1042 ? 1042 : (type >= 1070 && type <= 1073 ? 1073 : (type >= 1080 && type <= 1082 ? 1082 : (type >= 1061 && type <= 1063 ? 1062 : (type == 1672 ? 1672 : (type == 1190 || type == 1191 ? 1190 : 1122)))))))));
            if (grade == 1) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(163, 61224);
                            list.put(164, 61224);
                            list.put(165, 61224);
                            list.put(166, 61224);
                            list.put(10005, 61224);
                            list.put(10006, 61224);
                            list.put(10011, 40816);
                            list.put(10012, 40816);
                            list.put(10041, 61224);
                            list.put(10042, 61224);
                            list.put(10043, 61224);
                            list.put(10044, 61224);
                            list.put(10051, 20408);
                            list.put(10052, 20408);
                            list.put(10055, 20408);
                            list.put(10070, 20408);
                            list.put(203, 40816);
                            list.put(10202, 20408);
                            list.put(10207, 20408);
                            list.put(10222, 20408);
                            list.put(10227, 20408);
                            list.put(10232, 20408);
                            list.put(10236, 20408);
                            list.put(10242, 20408);
                            list.put(10247, 20408);
                            list.put(10291, 20408);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 122727);
                            list.put(2, 122727);
                            list.put(3, 122727);
                            list.put(4, 122727);
                            list.put(5, 122727);
                            list.put(6, 122727);
                            list.put(11, 81818);
                            list.put(12, 81818);
                            list.put(163, 6122);
                            list.put(164, 6122);
                            list.put(165, 6122);
                            list.put(166, 6122);
                            list.put(10005, 6122);
                            list.put(10006, 6122);
                            list.put(10011, 4082);
                            list.put(10012, 4082);
                            list.put(10041, 6122);
                            list.put(10042, 6122);
                            list.put(10043, 6122);
                            list.put(10044, 6122);
                            list.put(10051, 2041);
                            list.put(10052, 2041);
                            list.put(10055, 2041);
                            list.put(10070, 2041);
                            list.put(203, 4082);
                            list.put(10202, 2041);
                            list.put(10207, 2041);
                            list.put(10222, 2041);
                            list.put(10227, 2041);
                            list.put(10232, 2041);
                            list.put(10236, 2041);
                            list.put(10242, 2041);
                            list.put(10247, 2041);
                            list.put(10291, 2041);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 135000);
                        list.put(2, 135000);
                        list.put(3, 135000);
                        list.put(4, 135000);
                        list.put(5, 135000);
                        list.put(6, 135000);
                        list.put(11, 90000);
                        list.put(12, 90000);
                        list.put(163, 612);
                        list.put(164, 612);
                        list.put(165, 612);
                        list.put(166, 612);
                        list.put(10005, 612);
                        list.put(10006, 612);
                        list.put(10011, 408);
                        list.put(10012, 408);
                        list.put(10041, 612);
                        list.put(10042, 612);
                        list.put(10043, 612);
                        list.put(10044, 612);
                        list.put(10051, 204);
                        list.put(10052, 204);
                        list.put(10055, 204);
                        list.put(10070, 204);
                        list.put(203, 408);
                        list.put(10202, 204);
                        list.put(10207, 204);
                        list.put(10222, 204);
                        list.put(10227, 204);
                        list.put(10232, 204);
                        list.put(10236, 204);
                        list.put(10242, 204);
                        list.put(10247, 204);
                        list.put(10291, 204);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(163, 75000);
                            list.put(164, 75000);
                            list.put(165, 75000);
                            list.put(166, 75000);
                            list.put(10005, 75000);
                            list.put(10006, 75000);
                            list.put(10011, 50000);
                            list.put(10041, 75000);
                            list.put(10042, 75000);
                            list.put(10043, 75000);
                            list.put(10044, 75000);
                            list.put(10045, 50000);
                            list.put(10046, 50000);
                            list.put(10054, 50000);
                            list.put(203, 50000);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 128571);
                            list.put(2, 128571);
                            list.put(3, 128571);
                            list.put(4, 128571);
                            list.put(5, 128571);
                            list.put(6, 128571);
                            list.put(13, 128571);
                            list.put(163, 7500);
                            list.put(164, 7500);
                            list.put(165, 7500);
                            list.put(166, 7500);
                            list.put(10005, 7500);
                            list.put(10006, 7500);
                            list.put(10011, 5000);
                            list.put(10041, 7500);
                            list.put(10042, 7500);
                            list.put(10043, 7500);
                            list.put(10044, 7500);
                            list.put(10045, 5000);
                            list.put(10046, 5000);
                            list.put(10054, 5000);
                            list.put(203, 5000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 141429);
                        list.put(2, 141429);
                        list.put(3, 141429);
                        list.put(4, 141429);
                        list.put(5, 141429);
                        list.put(6, 141429);
                        list.put(13, 141429);
                        list.put(163, 750);
                        list.put(164, 750);
                        list.put(165, 750);
                        list.put(166, 750);
                        list.put(10005, 750);
                        list.put(10006, 750);
                        list.put(10011, 500);
                        list.put(10041, 750);
                        list.put(10042, 750);
                        list.put(10043, 750);
                        list.put(10044, 750);
                        list.put(10045, 500);
                        list.put(10046, 500);
                        list.put(10054, 500);
                        list.put(203, 5000);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(163, 61224);
                            list.put(164, 61224);
                            list.put(165, 61224);
                            list.put(166, 61224);
                            list.put(10005, 61224);
                            list.put(10006, 61224);
                            list.put(10011, 40816);
                            list.put(10012, 40816);
                            list.put(10041, 61224);
                            list.put(10042, 61224);
                            list.put(10043, 61224);
                            list.put(10044, 61224);
                            list.put(10051, 20408);
                            list.put(10052, 20408);
                            list.put(10055, 20408);
                            list.put(10070, 20408);
                            list.put(203, 40816);
                            list.put(10202, 20408);
                            list.put(10207, 20408);
                            list.put(10222, 20408);
                            list.put(10227, 20408);
                            list.put(10232, 20408);
                            list.put(10236, 20408);
                            list.put(10242, 20408);
                            list.put(10247, 20408);
                            list.put(10291, 20408);
                            break;
                        }
                        if (num == 2) {
                            list.put(1, 122727);
                            list.put(2, 122727);
                            list.put(3, 122727);
                            list.put(4, 122727);
                            list.put(5, 122727);
                            list.put(6, 122727);
                            list.put(11, 81818);
                            list.put(12, 81818);
                            list.put(163, 6122);
                            list.put(164, 6122);
                            list.put(165, 6122);
                            list.put(166, 6122);
                            list.put(10005, 6122);
                            list.put(10006, 6122);
                            list.put(10011, 4082);
                            list.put(10012, 4082);
                            list.put(10041, 6122);
                            list.put(10042, 6122);
                            list.put(10043, 6122);
                            list.put(10044, 6122);
                            list.put(10051, 2041);
                            list.put(10052, 2041);
                            list.put(10055, 2041);
                            list.put(10070, 2041);
                            list.put(203, 4082);
                            list.put(10202, 2041);
                            list.put(10207, 2041);
                            list.put(10222, 2041);
                            list.put(10227, 2041);
                            list.put(10232, 2041);
                            list.put(10236, 2041);
                            list.put(10242, 2041);
                            list.put(10247, 2041);
                            list.put(10291, 2041);
                            break;
                        }
                        if (num != 3) break;
                        list.put(1, 135000);
                        list.put(2, 135000);
                        list.put(3, 135000);
                        list.put(4, 135000);
                        list.put(5, 135000);
                        list.put(6, 135000);
                        list.put(11, 90000);
                        list.put(12, 90000);
                        list.put(163, 612);
                        list.put(164, 612);
                        list.put(165, 612);
                        list.put(166, 612);
                        list.put(10005, 612);
                        list.put(10006, 612);
                        list.put(10011, 408);
                        list.put(10012, 408);
                        list.put(10041, 612);
                        list.put(10042, 612);
                        list.put(10043, 612);
                        list.put(10044, 612);
                        list.put(10051, 204);
                        list.put(10052, 204);
                        list.put(10055, 204);
                        list.put(10070, 204);
                        list.put(203, 408);
                        list.put(10202, 204);
                        list.put(10207, 204);
                        list.put(10222, 204);
                        list.put(10227, 204);
                        list.put(10232, 204);
                        list.put(10236, 204);
                        list.put(10242, 204);
                        list.put(10247, 204);
                        list.put(10291, 204);
                        break;
                    }
                }
            } else if (grade == 2) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(20041, 108696);
                            list.put(20042, 108696);
                            list.put(20043, 108696);
                            list.put(20044, 108696);
                            list.put(20045, 108696);
                            list.put(20046, 108696);
                            list.put(20051, 43478);
                            list.put(20052, 43478);
                            list.put(20055, 43478);
                            list.put(20070, 43478);
                            list.put(20086, 43478);
                            list.put(20202, 43478);
                            list.put(20207, 43478);
                            list.put(10291, 43478);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 55102);
                            list.put(164, 55102);
                            list.put(165, 55102);
                            list.put(166, 55102);
                            list.put(10005, 55102);
                            list.put(10006, 55102);
                            list.put(10011, 36735);
                            list.put(10012, 36735);
                            list.put(10041, 55102);
                            list.put(10042, 55102);
                            list.put(10043, 55102);
                            list.put(10044, 55102);
                            list.put(10051, 18367);
                            list.put(10052, 18367);
                            list.put(10055, 18367);
                            list.put(10070, 18367);
                            list.put(203, 36735);
                            list.put(10202, 18367);
                            list.put(10207, 18367);
                            list.put(10222, 18367);
                            list.put(10227, 18367);
                            list.put(10232, 18367);
                            list.put(10236, 18367);
                            list.put(10242, 18367);
                            list.put(10247, 18367);
                            list.put(10291, 18367);
                            list.put(20041, 10870);
                            list.put(20042, 10870);
                            list.put(20043, 10870);
                            list.put(20044, 10870);
                            list.put(20045, 10870);
                            list.put(20046, 10870);
                            list.put(20051, 4348);
                            list.put(20052, 4348);
                            list.put(20055, 4348);
                            list.put(20070, 4348);
                            list.put(20086, 4348);
                            list.put(20202, 4348);
                            list.put(20207, 4348);
                            list.put(10291, 4348);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 60612);
                        list.put(164, 60612);
                        list.put(165, 60612);
                        list.put(166, 60612);
                        list.put(10005, 60612);
                        list.put(10006, 60612);
                        list.put(10011, 40408);
                        list.put(10012, 40408);
                        list.put(10041, 60612);
                        list.put(10042, 60612);
                        list.put(10043, 60612);
                        list.put(10044, 60612);
                        list.put(10051, 20204);
                        list.put(10052, 20204);
                        list.put(10055, 20204);
                        list.put(10070, 20204);
                        list.put(203, 40408);
                        list.put(10202, 20204);
                        list.put(10207, 20204);
                        list.put(10222, 20204);
                        list.put(10227, 20204);
                        list.put(10232, 20204);
                        list.put(10236, 20204);
                        list.put(10242, 20204);
                        list.put(10247, 20204);
                        list.put(10291, 20204);
                        list.put(20041, 1087);
                        list.put(20042, 1087);
                        list.put(20043, 1087);
                        list.put(20044, 1087);
                        list.put(20045, 1087);
                        list.put(20046, 1087);
                        list.put(20051, 435);
                        list.put(20052, 435);
                        list.put(20055, 435);
                        list.put(20070, 435);
                        list.put(20086, 435);
                        list.put(20202, 435);
                        list.put(20207, 435);
                        list.put(10291, 435);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(20041, 142857);
                            list.put(20042, 142857);
                            list.put(20043, 142857);
                            list.put(20044, 142857);
                            list.put(20045, 142857);
                            list.put(20046, 142857);
                            list.put(20054, 85714);
                            list.put(20086, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 67500);
                            list.put(164, 67500);
                            list.put(165, 67500);
                            list.put(166, 67500);
                            list.put(10005, 67500);
                            list.put(10006, 67500);
                            list.put(10011, 45000);
                            list.put(10041, 67500);
                            list.put(10042, 67500);
                            list.put(10043, 67500);
                            list.put(10044, 67500);
                            list.put(10045, 45000);
                            list.put(10046, 45000);
                            list.put(10054, 45000);
                            list.put(203, 45000);
                            list.put(20041, 14286);
                            list.put(20042, 14286);
                            list.put(20043, 14286);
                            list.put(20044, 14286);
                            list.put(20045, 14286);
                            list.put(20046, 14286);
                            list.put(20054, 8571);
                            list.put(20086, 5714);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 74250);
                        list.put(164, 74250);
                        list.put(165, 74250);
                        list.put(166, 74250);
                        list.put(10005, 74250);
                        list.put(10006, 74250);
                        list.put(10011, 49500);
                        list.put(10041, 74250);
                        list.put(10042, 74250);
                        list.put(10043, 74250);
                        list.put(10044, 74250);
                        list.put(10045, 49500);
                        list.put(10046, 49500);
                        list.put(10054, 49500);
                        list.put(203, 49500);
                        list.put(20041, 1429);
                        list.put(20042, 1429);
                        list.put(20043, 1429);
                        list.put(20044, 1429);
                        list.put(20045, 1429);
                        list.put(20046, 1429);
                        list.put(20054, 857);
                        list.put(20086, 571);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(20041, 108696);
                            list.put(20042, 108696);
                            list.put(20043, 108696);
                            list.put(20044, 108696);
                            list.put(20045, 108696);
                            list.put(20046, 108696);
                            list.put(20051, 43478);
                            list.put(20052, 43478);
                            list.put(20055, 43478);
                            list.put(20070, 43478);
                            list.put(20086, 43478);
                            list.put(20202, 43478);
                            list.put(20207, 43478);
                            list.put(10291, 43478);
                            break;
                        }
                        if (num == 2) {
                            list.put(163, 55102);
                            list.put(164, 55102);
                            list.put(165, 55102);
                            list.put(166, 55102);
                            list.put(10005, 55102);
                            list.put(10006, 55102);
                            list.put(10011, 36735);
                            list.put(10012, 36735);
                            list.put(10041, 55102);
                            list.put(10042, 55102);
                            list.put(10043, 55102);
                            list.put(10044, 55102);
                            list.put(10051, 18367);
                            list.put(10052, 18367);
                            list.put(10055, 18367);
                            list.put(10070, 18367);
                            list.put(203, 36735);
                            list.put(10202, 18367);
                            list.put(10207, 18367);
                            list.put(10222, 18367);
                            list.put(10227, 18367);
                            list.put(10232, 18367);
                            list.put(10236, 18367);
                            list.put(10242, 18367);
                            list.put(10247, 18367);
                            list.put(10291, 18367);
                            list.put(20041, 10870);
                            list.put(20042, 10870);
                            list.put(20043, 10870);
                            list.put(20044, 10870);
                            list.put(20045, 10870);
                            list.put(20046, 10870);
                            list.put(20051, 4348);
                            list.put(20052, 4348);
                            list.put(20055, 4348);
                            list.put(20070, 4348);
                            list.put(20086, 4348);
                            list.put(20202, 4348);
                            list.put(20207, 4348);
                            list.put(10291, 4348);
                            break;
                        }
                        if (num != 3) break;
                        list.put(163, 60612);
                        list.put(164, 60612);
                        list.put(165, 60612);
                        list.put(166, 60612);
                        list.put(10005, 60612);
                        list.put(10006, 60612);
                        list.put(10011, 40408);
                        list.put(10012, 40408);
                        list.put(10041, 60612);
                        list.put(10042, 60612);
                        list.put(10043, 60612);
                        list.put(10044, 60612);
                        list.put(10051, 20204);
                        list.put(10052, 20204);
                        list.put(10055, 20204);
                        list.put(10070, 20204);
                        list.put(203, 40408);
                        list.put(10202, 20204);
                        list.put(10207, 20204);
                        list.put(10222, 20204);
                        list.put(10227, 20204);
                        list.put(10232, 20204);
                        list.put(10236, 20204);
                        list.put(10242, 20204);
                        list.put(10247, 20204);
                        list.put(10291, 20204);
                        list.put(20041, 1087);
                        list.put(20042, 1087);
                        list.put(20043, 1087);
                        list.put(20044, 1087);
                        list.put(20045, 1087);
                        list.put(20046, 1087);
                        list.put(20051, 435);
                        list.put(20052, 435);
                        list.put(20055, 435);
                        list.put(20070, 435);
                        list.put(20086, 435);
                        list.put(20202, 435);
                        list.put(20207, 435);
                        list.put(10291, 435);
                        break;
                    }
                }
            } else if (grade == 3) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(30041, 111111);
                            list.put(30042, 111111);
                            list.put(30043, 111111);
                            list.put(30044, 111111);
                            list.put(30051, 66667);
                            list.put(30052, 66667);
                            list.put(30055, 88889);
                            list.put(30070, 66667);
                            list.put(30086, 88889);
                            list.put(30291, 66667);
                            list.put(30601, 66667);
                            list.put(40601, 44444);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 86957);
                            list.put(20042, 86957);
                            list.put(20043, 86957);
                            list.put(20044, 86957);
                            list.put(20045, 86957);
                            list.put(20046, 86957);
                            list.put(20051, 34783);
                            list.put(20052, 34783);
                            list.put(20055, 34783);
                            list.put(20070, 34783);
                            list.put(20086, 34783);
                            list.put(20202, 34783);
                            list.put(20207, 34783);
                            list.put(10291, 34783);
                            list.put(30041, 22222);
                            list.put(30042, 22222);
                            list.put(30043, 22222);
                            list.put(30044, 22222);
                            list.put(30051, 13333);
                            list.put(30052, 13333);
                            list.put(30055, 17778);
                            list.put(30070, 13333);
                            list.put(30086, 17778);
                            list.put(30291, 13333);
                            list.put(30601, 13333);
                            list.put(40601, 8889);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 103261);
                        list.put(20042, 103261);
                        list.put(20043, 103261);
                        list.put(20044, 103261);
                        list.put(20045, 103261);
                        list.put(20046, 103261);
                        list.put(20051, 41304);
                        list.put(20052, 41304);
                        list.put(20055, 41304);
                        list.put(20070, 41304);
                        list.put(20086, 41304);
                        list.put(20202, 41304);
                        list.put(20207, 41304);
                        list.put(10291, 41304);
                        list.put(30041, 5556);
                        list.put(30042, 5556);
                        list.put(30043, 5556);
                        list.put(30044, 5556);
                        list.put(30051, 3333);
                        list.put(30052, 3333);
                        list.put(30055, 4444);
                        list.put(30070, 3333);
                        list.put(30086, 4444);
                        list.put(30291, 3333);
                        list.put(30601, 3333);
                        list.put(40601, 2222);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(30041, 96154);
                            list.put(30042, 96154);
                            list.put(30043, 96154);
                            list.put(30044, 96154);
                            list.put(30054, 76923);
                            list.put(30045, 115385);
                            list.put(30046, 115385);
                            list.put(30086, 76923);
                            list.put(30357, 76923);
                            list.put(30356, 76923);
                            list.put(30551, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 19231);
                            list.put(30042, 19231);
                            list.put(30043, 19231);
                            list.put(30044, 19231);
                            list.put(30054, 15385);
                            list.put(30045, 23077);
                            list.put(30046, 23077);
                            list.put(30086, 15385);
                            list.put(30357, 15385);
                            list.put(30356, 15385);
                            list.put(30551, 15385);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 4808);
                        list.put(30042, 4808);
                        list.put(30043, 4808);
                        list.put(30044, 4808);
                        list.put(30054, 3846);
                        list.put(30045, 5769);
                        list.put(30046, 5769);
                        list.put(30086, 3846);
                        list.put(30357, 3846);
                        list.put(30356, 3846);
                        list.put(30551, 3846);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(30041, 80645);
                            list.put(30042, 80645);
                            list.put(30043, 80645);
                            list.put(30044, 80645);
                            list.put(30054, 64516);
                            list.put(30045, 96774);
                            list.put(30046, 96774);
                            list.put(30086, 64516);
                            list.put(30106, 64516);
                            list.put(30107, 32258);
                            list.put(30357, 64516);
                            list.put(30356, 64516);
                            list.put(30551, 64516);
                            list.put(31002, 64516);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 16129);
                            list.put(30042, 16129);
                            list.put(30043, 16129);
                            list.put(30044, 16129);
                            list.put(30054, 12903);
                            list.put(30045, 9677);
                            list.put(30046, 9677);
                            list.put(30086, 12903);
                            list.put(30106, 12903);
                            list.put(30107, 3226);
                            list.put(30357, 12903);
                            list.put(30356, 12903);
                            list.put(30551, 12903);
                            list.put(31002, 12903);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 4032);
                        list.put(30042, 4032);
                        list.put(30043, 4032);
                        list.put(30044, 4032);
                        list.put(30054, 3226);
                        list.put(30045, 4839);
                        list.put(30046, 4839);
                        list.put(30086, 3226);
                        list.put(30106, 3226);
                        list.put(30107, 1613);
                        list.put(30357, 3226);
                        list.put(30356, 3226);
                        list.put(30551, 3226);
                        list.put(31002, 3226);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(30041, 96154);
                            list.put(30042, 96154);
                            list.put(30043, 96154);
                            list.put(30044, 96154);
                            list.put(30054, 76923);
                            list.put(30045, 115385);
                            list.put(30046, 115385);
                            list.put(30086, 76923);
                            list.put(30357, 76923);
                            list.put(30356, 76923);
                            list.put(30551, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 19231);
                            list.put(30042, 19231);
                            list.put(30043, 19231);
                            list.put(30044, 19231);
                            list.put(30054, 15385);
                            list.put(30045, 23077);
                            list.put(30046, 23077);
                            list.put(30086, 15385);
                            list.put(30357, 15385);
                            list.put(30356, 15385);
                            list.put(30551, 15385);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 4808);
                        list.put(30042, 4808);
                        list.put(30043, 4808);
                        list.put(30044, 4808);
                        list.put(30054, 3846);
                        list.put(30045, 5769);
                        list.put(30046, 5769);
                        list.put(30086, 3846);
                        list.put(30357, 3846);
                        list.put(30356, 3846);
                        list.put(30551, 3846);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(30041, 75758);
                            list.put(30042, 75758);
                            list.put(30043, 75758);
                            list.put(30044, 75758);
                            list.put(30054, 60606);
                            list.put(30045, 90909);
                            list.put(30046, 90909);
                            list.put(30086, 60606);
                            list.put(30371, 60606);
                            list.put(30366, 60606);
                            list.put(30357, 60606);
                            list.put(30356, 60606);
                            list.put(30551, 60606);
                            list.put(30376, 60606);
                            list.put(30377, 30303);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 105263);
                            list.put(20042, 105263);
                            list.put(20043, 105263);
                            list.put(20044, 105263);
                            list.put(20045, 105263);
                            list.put(20046, 105263);
                            list.put(20054, 63158);
                            list.put(20086, 42105);
                            list.put(20366, 63158);
                            list.put(30041, 15152);
                            list.put(30042, 15152);
                            list.put(30043, 15152);
                            list.put(30044, 15152);
                            list.put(30054, 12121);
                            list.put(30045, 18182);
                            list.put(30046, 18182);
                            list.put(30086, 12121);
                            list.put(30371, 12121);
                            list.put(30366, 12121);
                            list.put(30357, 12121);
                            list.put(30356, 12121);
                            list.put(30551, 12121);
                            list.put(30376, 6061);
                            list.put(30377, 3030);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 125000);
                        list.put(20042, 125000);
                        list.put(20043, 125000);
                        list.put(20044, 125000);
                        list.put(20045, 125000);
                        list.put(20046, 125000);
                        list.put(20054, 75000);
                        list.put(20086, 50000);
                        list.put(20366, 750000);
                        list.put(30041, 3788);
                        list.put(30042, 3788);
                        list.put(30043, 3788);
                        list.put(30044, 3788);
                        list.put(30054, 3030);
                        list.put(30045, 4545);
                        list.put(30046, 4545);
                        list.put(30086, 3030);
                        list.put(30371, 3030);
                        list.put(30366, 3030);
                        list.put(30357, 3030);
                        list.put(30356, 3030);
                        list.put(30551, 3030);
                        list.put(30376, 3030);
                        list.put(30377, 1515);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(30041, 89286);
                            list.put(30042, 89286);
                            list.put(30043, 89286);
                            list.put(30044, 89286);
                            list.put(30054, 71429);
                            list.put(30045, 107143);
                            list.put(30046, 107143);
                            list.put(30086, 71429);
                            list.put(30357, 71429);
                            list.put(30356, 71429);
                            list.put(30551, 71429);
                            list.put(31001, 71429);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 17857);
                            list.put(30042, 17857);
                            list.put(30043, 17857);
                            list.put(30044, 17857);
                            list.put(30054, 14286);
                            list.put(30045, 21429);
                            list.put(30046, 21429);
                            list.put(30086, 14286);
                            list.put(30357, 14286);
                            list.put(30356, 14286);
                            list.put(30551, 14286);
                            list.put(31001, 14286);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 4464);
                        list.put(30042, 4464);
                        list.put(30043, 4464);
                        list.put(30044, 4464);
                        list.put(30054, 3571);
                        list.put(30045, 5357);
                        list.put(30046, 5357);
                        list.put(30086, 3571);
                        list.put(30357, 3571);
                        list.put(30356, 3571);
                        list.put(30551, 3571);
                        list.put(31001, 3571);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(30041, 83333);
                            list.put(30042, 83333);
                            list.put(30043, 83333);
                            list.put(30044, 83333);
                            list.put(30054, 66667);
                            list.put(30045, 100000);
                            list.put(30046, 100000);
                            list.put(30086, 66667);
                            list.put(32091, 16667);
                            list.put(32092, 16667);
                            list.put(32093, 16667);
                            list.put(32094, 16667);
                            list.put(30357, 66667);
                            list.put(30356, 66667);
                            list.put(30551, 66667);
                            list.put(31003, 66667);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 97561);
                            list.put(20042, 97561);
                            list.put(20043, 97561);
                            list.put(20044, 97561);
                            list.put(20045, 97561);
                            list.put(20046, 97561);
                            list.put(20054, 58537);
                            list.put(20086, 39024);
                            list.put(30041, 16667);
                            list.put(30042, 16667);
                            list.put(30043, 16667);
                            list.put(30044, 16667);
                            list.put(30054, 13333);
                            list.put(30045, 20000);
                            list.put(30046, 20000);
                            list.put(30086, 13333);
                            list.put(32091, 3333);
                            list.put(32092, 3333);
                            list.put(32093, 3333);
                            list.put(32094, 3333);
                            list.put(30357, 13333);
                            list.put(30356, 13333);
                            list.put(30551, 13333);
                            list.put(31003, 13333);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 115854);
                        list.put(20042, 115854);
                        list.put(20043, 115854);
                        list.put(20044, 115854);
                        list.put(20045, 115854);
                        list.put(20046, 115854);
                        list.put(20054, 69512);
                        list.put(20086, 46341);
                        list.put(30041, 4167);
                        list.put(30042, 4167);
                        list.put(30043, 4167);
                        list.put(30044, 4167);
                        list.put(30054, 3333);
                        list.put(30045, 5000);
                        list.put(30046, 5000);
                        list.put(30086, 3333);
                        list.put(32091, 833);
                        list.put(32092, 833);
                        list.put(32093, 833);
                        list.put(32094, 833);
                        list.put(30357, 3333);
                        list.put(30356, 3333);
                        list.put(30551, 3333);
                        list.put(31003, 3333);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(30041, 89286);
                            list.put(30042, 89286);
                            list.put(30043, 89286);
                            list.put(30044, 89286);
                            list.put(30054, 71429);
                            list.put(30045, 107143);
                            list.put(30046, 107143);
                            list.put(30086, 71429);
                            list.put(30357, 71429);
                            list.put(30356, 71429);
                            list.put(30551, 71429);
                            list.put(31004, 71429);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 17857);
                            list.put(30042, 17857);
                            list.put(30043, 17857);
                            list.put(30044, 17857);
                            list.put(30054, 14286);
                            list.put(30045, 21429);
                            list.put(30046, 21429);
                            list.put(30086, 14286);
                            list.put(30357, 14286);
                            list.put(30356, 14286);
                            list.put(30551, 14286);
                            list.put(31004, 14286);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 4464);
                        list.put(30042, 4464);
                        list.put(30043, 4464);
                        list.put(30044, 4464);
                        list.put(30054, 3571);
                        list.put(30045, 5357);
                        list.put(30046, 5357);
                        list.put(30086, 3571);
                        list.put(30357, 3571);
                        list.put(30356, 3571);
                        list.put(30551, 3571);
                        list.put(31004, 3571);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(30041, 113636);
                            list.put(30042, 113636);
                            list.put(30043, 113636);
                            list.put(30044, 113636);
                            list.put(30054, 90909);
                            list.put(30045, 136364);
                            list.put(30046, 136364);
                            list.put(30086, 90909);
                            list.put(30551, 90909);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 22727);
                            list.put(30042, 22727);
                            list.put(30043, 22727);
                            list.put(30044, 22727);
                            list.put(30054, 18182);
                            list.put(30045, 27273);
                            list.put(30046, 27273);
                            list.put(30086, 18182);
                            list.put(30551, 18182);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 5682);
                        list.put(30042, 5682);
                        list.put(30043, 5682);
                        list.put(30044, 5682);
                        list.put(30054, 4545);
                        list.put(30045, 6818);
                        list.put(30046, 6818);
                        list.put(30086, 4545);
                        list.put(30551, 4545);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(30041, 113636);
                            list.put(30042, 113636);
                            list.put(30043, 113636);
                            list.put(30044, 113636);
                            list.put(30054, 90909);
                            list.put(30045, 136364);
                            list.put(30046, 136364);
                            list.put(30086, 90909);
                            list.put(30551, 90909);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 114286);
                            list.put(20042, 114286);
                            list.put(20043, 114286);
                            list.put(20044, 114286);
                            list.put(20045, 114286);
                            list.put(20046, 114286);
                            list.put(20054, 68571);
                            list.put(20086, 45714);
                            list.put(30041, 22727);
                            list.put(30042, 22727);
                            list.put(30043, 22727);
                            list.put(30044, 22727);
                            list.put(30054, 18182);
                            list.put(30045, 27273);
                            list.put(30046, 27273);
                            list.put(30086, 18182);
                            list.put(30551, 18182);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 135714);
                        list.put(20042, 135714);
                        list.put(20043, 135714);
                        list.put(20044, 135714);
                        list.put(20045, 135714);
                        list.put(20046, 135714);
                        list.put(20054, 81429);
                        list.put(20086, 54286);
                        list.put(30041, 5682);
                        list.put(30042, 5682);
                        list.put(30043, 5682);
                        list.put(30044, 5682);
                        list.put(30054, 4545);
                        list.put(30045, 6818);
                        list.put(30046, 6818);
                        list.put(30086, 4545);
                        list.put(30551, 4545);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(30041, 125000);
                            list.put(30042, 125000);
                            list.put(30043, 125000);
                            list.put(30044, 125000);
                            list.put(30051, 75000);
                            list.put(30052, 75000);
                            list.put(30055, 100000);
                            list.put(30070, 75000);
                            list.put(30086, 100000);
                            list.put(30291, 75000);
                            break;
                        }
                        if (num == 2) {
                            list.put(20041, 97826);
                            list.put(20042, 97826);
                            list.put(20043, 97826);
                            list.put(20044, 97826);
                            list.put(20045, 97826);
                            list.put(20046, 97826);
                            list.put(20051, 39130);
                            list.put(20052, 39130);
                            list.put(20055, 39130);
                            list.put(20070, 39130);
                            list.put(20086, 39130);
                            list.put(20202, 39130);
                            list.put(20207, 39130);
                            list.put(10291, 39130);
                            list.put(30041, 12500);
                            list.put(30042, 12500);
                            list.put(30043, 12500);
                            list.put(30044, 12500);
                            list.put(30051, 7500);
                            list.put(30052, 7500);
                            list.put(30055, 10000);
                            list.put(30070, 7500);
                            list.put(30086, 10000);
                            list.put(30291, 7500);
                            break;
                        }
                        if (num != 3) break;
                        list.put(20041, 107609);
                        list.put(20042, 107609);
                        list.put(20043, 107609);
                        list.put(20044, 107609);
                        list.put(20045, 107609);
                        list.put(20046, 107609);
                        list.put(20051, 43043);
                        list.put(20052, 43043);
                        list.put(20055, 43043);
                        list.put(20070, 43043);
                        list.put(20086, 43043);
                        list.put(20202, 43043);
                        list.put(20207, 43043);
                        list.put(10291, 43043);
                        list.put(30041, 1250);
                        list.put(30042, 1250);
                        list.put(30043, 1250);
                        list.put(30044, 1250);
                        list.put(30051, 750);
                        list.put(30052, 750);
                        list.put(30055, 1000);
                        list.put(30070, 750);
                        list.put(30086, 1000);
                        list.put(30291, 750);
                        break;
                    }
                }
            } else if (grade == 4) {
                switch (type) {
                    case 1302: {
                        if (num == 1) {
                            list.put(40041, 97561);
                            list.put(40042, 97561);
                            list.put(40043, 97561);
                            list.put(40044, 97561);
                            list.put(40051, 48780);
                            list.put(40052, 48780);
                            list.put(40055, 48780);
                            list.put(40070, 48780);
                            list.put(40086, 73171);
                            list.put(42095, 48780);
                            list.put(42096, 48780);
                            list.put(40291, 48780);
                            list.put(40292, 48780);
                            list.put(40601, 48780);
                            list.put(40602, 48780);
                            list.put(40603, 48780);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 100000);
                            list.put(30042, 100000);
                            list.put(30043, 100000);
                            list.put(30044, 100000);
                            list.put(30051, 60000);
                            list.put(30052, 60000);
                            list.put(30055, 80000);
                            list.put(30070, 60000);
                            list.put(30086, 80000);
                            list.put(30291, 60000);
                            list.put(30601, 60000);
                            list.put(40601, 40000);
                            list.put(40041, 9756);
                            list.put(40042, 9756);
                            list.put(40043, 9756);
                            list.put(40044, 9756);
                            list.put(40051, 4878);
                            list.put(40052, 4878);
                            list.put(40055, 4878);
                            list.put(40070, 4878);
                            list.put(40086, 7317);
                            list.put(42095, 4878);
                            list.put(42096, 4878);
                            list.put(40291, 4878);
                            list.put(40292, 4878);
                            list.put(40601, 4878);
                            list.put(40602, 4878);
                            list.put(40603, 4878);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 110000);
                        list.put(30042, 110000);
                        list.put(30043, 110000);
                        list.put(30044, 110000);
                        list.put(30051, 66000);
                        list.put(30052, 66000);
                        list.put(30055, 88000);
                        list.put(30070, 66000);
                        list.put(30086, 88000);
                        list.put(30291, 66000);
                        list.put(30601, 66000);
                        list.put(40601, 44000);
                        list.put(40041, 976);
                        list.put(40042, 976);
                        list.put(40043, 976);
                        list.put(40044, 976);
                        list.put(40051, 488);
                        list.put(40052, 488);
                        list.put(40055, 488);
                        list.put(40070, 488);
                        list.put(40086, 732);
                        list.put(42095, 488);
                        list.put(42096, 488);
                        list.put(40291, 488);
                        list.put(40292, 488);
                        list.put(40601, 488);
                        list.put(40602, 488);
                        list.put(40603, 488);
                        break;
                    }
                    case 1102: {
                        if (num == 1) {
                            list.put(40041, 108108);
                            list.put(40042, 108108);
                            list.put(40043, 108108);
                            list.put(40044, 108108);
                            list.put(40054, 108108);
                            list.put(40045, 108108);
                            list.put(40046, 108108);
                            list.put(40086, 81081);
                            list.put(40357, 81081);
                            list.put(40356, 81081);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 76923);
                            list.put(30042, 76923);
                            list.put(30043, 76923);
                            list.put(30044, 76923);
                            list.put(30054, 61538);
                            list.put(30045, 92308);
                            list.put(30046, 92308);
                            list.put(30086, 61538);
                            list.put(30357, 61538);
                            list.put(30356, 61538);
                            list.put(30551, 61538);
                            list.put(40041, 21622);
                            list.put(40042, 21622);
                            list.put(40043, 21622);
                            list.put(40044, 21622);
                            list.put(40054, 21622);
                            list.put(40045, 21622);
                            list.put(40046, 21622);
                            list.put(40086, 16216);
                            list.put(40357, 16216);
                            list.put(40356, 16216);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 91346);
                        list.put(30042, 91346);
                        list.put(30043, 91346);
                        list.put(30044, 91346);
                        list.put(30054, 73077);
                        list.put(30045, 109615);
                        list.put(30046, 109615);
                        list.put(30086, 73077);
                        list.put(30357, 73077);
                        list.put(30356, 73077);
                        list.put(30551, 73077);
                        list.put(40041, 5405);
                        list.put(40042, 5405);
                        list.put(40043, 5405);
                        list.put(40044, 5405);
                        list.put(40054, 5405);
                        list.put(40045, 5405);
                        list.put(40046, 5405);
                        list.put(40086, 4054);
                        list.put(40357, 4054);
                        list.put(40356, 4054);
                        break;
                    }
                    case 1004: {
                        if (num == 1) {
                            list.put(40041, 80000);
                            list.put(40042, 80000);
                            list.put(40043, 80000);
                            list.put(40044, 80000);
                            list.put(40054, 80000);
                            list.put(40045, 80000);
                            list.put(40046, 80000);
                            list.put(40086, 60000);
                            list.put(40106, 60000);
                            list.put(40107, 40000);
                            list.put(40357, 60000);
                            list.put(40356, 60000);
                            list.put(40557, 40000);
                            list.put(40556, 60000);
                            list.put(41006, 60000);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 64516);
                            list.put(30042, 64516);
                            list.put(30043, 64516);
                            list.put(30044, 64516);
                            list.put(30054, 51613);
                            list.put(30045, 77419);
                            list.put(30046, 77419);
                            list.put(30086, 51613);
                            list.put(30106, 51613);
                            list.put(30107, 25806);
                            list.put(30357, 51613);
                            list.put(30356, 51613);
                            list.put(30551, 51613);
                            list.put(31002, 51613);
                            list.put(40041, 16000);
                            list.put(40042, 16000);
                            list.put(40043, 16000);
                            list.put(40044, 16000);
                            list.put(40054, 16000);
                            list.put(40045, 16000);
                            list.put(40046, 16000);
                            list.put(40086, 12000);
                            list.put(40106, 12000);
                            list.put(40107, 8000);
                            list.put(40357, 12000);
                            list.put(40356, 12000);
                            list.put(40557, 8000);
                            list.put(40556, 12000);
                            list.put(41006, 12000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 76613);
                        list.put(30042, 76613);
                        list.put(30043, 76613);
                        list.put(30044, 76613);
                        list.put(30045, 91935);
                        list.put(30046, 91935);
                        list.put(30054, 61290);
                        list.put(30086, 61290);
                        list.put(30106, 61290);
                        list.put(30107, 30645);
                        list.put(30357, 61290);
                        list.put(30356, 61290);
                        list.put(30551, 61290);
                        list.put(31002, 61290);
                        list.put(40041, 4000);
                        list.put(40042, 4000);
                        list.put(40043, 4000);
                        list.put(40044, 4000);
                        list.put(40054, 4000);
                        list.put(40045, 4000);
                        list.put(40046, 4000);
                        list.put(40086, 3000);
                        list.put(40106, 3000);
                        list.put(40107, 2000);
                        list.put(40357, 3000);
                        list.put(40356, 3000);
                        list.put(40557, 2000);
                        list.put(40556, 3000);
                        list.put(41006, 3000);
                        break;
                    }
                    case 1132: {
                        if (num == 1) {
                            list.put(40041, 108108);
                            list.put(40042, 108108);
                            list.put(40043, 108108);
                            list.put(40044, 108108);
                            list.put(40054, 108108);
                            list.put(40045, 108108);
                            list.put(40046, 108108);
                            list.put(40086, 81081);
                            list.put(40357, 81081);
                            list.put(40356, 81081);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 76923);
                            list.put(30042, 76923);
                            list.put(30043, 76923);
                            list.put(30044, 76923);
                            list.put(30054, 61538);
                            list.put(30045, 92308);
                            list.put(30046, 92308);
                            list.put(30086, 61538);
                            list.put(30357, 61538);
                            list.put(30356, 61538);
                            list.put(30551, 61538);
                            list.put(40041, 21622);
                            list.put(40042, 21622);
                            list.put(40043, 21622);
                            list.put(40044, 21622);
                            list.put(40054, 21622);
                            list.put(40045, 21622);
                            list.put(40046, 21622);
                            list.put(40086, 16216);
                            list.put(40357, 16216);
                            list.put(40356, 16216);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 91346);
                        list.put(30042, 91346);
                        list.put(30043, 91346);
                        list.put(30044, 91346);
                        list.put(30054, 73077);
                        list.put(30045, 109615);
                        list.put(30046, 109615);
                        list.put(30086, 73077);
                        list.put(30357, 73077);
                        list.put(30356, 73077);
                        list.put(30551, 73077);
                        list.put(40041, 5405);
                        list.put(40042, 5405);
                        list.put(40043, 5405);
                        list.put(40044, 5405);
                        list.put(40054, 5405);
                        list.put(40045, 5405);
                        list.put(40046, 5405);
                        list.put(40086, 4054);
                        list.put(40357, 4054);
                        list.put(40356, 4054);
                        break;
                    }
                    case 1042: {
                        if (num == 1) {
                            list.put(40041, 88889);
                            list.put(40042, 88889);
                            list.put(40043, 88889);
                            list.put(40044, 88889);
                            list.put(40054, 88889);
                            list.put(40045, 88889);
                            list.put(40046, 88889);
                            list.put(40086, 66667);
                            list.put(40111, 44444);
                            list.put(40357, 66667);
                            list.put(40356, 66667);
                            list.put(40366, 66667);
                            list.put(40371, 66667);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 60606);
                            list.put(30042, 60606);
                            list.put(30043, 60606);
                            list.put(30044, 60606);
                            list.put(30054, 48485);
                            list.put(30045, 72727);
                            list.put(30046, 72727);
                            list.put(30086, 48485);
                            list.put(30371, 48485);
                            list.put(30366, 48485);
                            list.put(30357, 48485);
                            list.put(30356, 48485);
                            list.put(30551, 48485);
                            list.put(30376, 48485);
                            list.put(30377, 24242);
                            list.put(40041, 17778);
                            list.put(40042, 17778);
                            list.put(40043, 17778);
                            list.put(40044, 17778);
                            list.put(40054, 17778);
                            list.put(40045, 17778);
                            list.put(40046, 17778);
                            list.put(40086, 13333);
                            list.put(40111, 8889);
                            list.put(40357, 13333);
                            list.put(40356, 13333);
                            list.put(40366, 13333);
                            list.put(40371, 13333);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 71970);
                        list.put(30042, 71970);
                        list.put(30043, 71970);
                        list.put(30044, 71970);
                        list.put(30054, 57576);
                        list.put(30045, 86364);
                        list.put(30046, 86364);
                        list.put(30086, 57576);
                        list.put(30371, 57576);
                        list.put(30366, 57576);
                        list.put(30357, 57576);
                        list.put(30356, 57576);
                        list.put(30551, 57576);
                        list.put(30376, 57576);
                        list.put(30377, 28788);
                        list.put(40041, 4444);
                        list.put(40042, 4444);
                        list.put(40043, 4444);
                        list.put(40044, 4444);
                        list.put(40054, 4444);
                        list.put(40045, 4444);
                        list.put(40046, 4444);
                        list.put(40086, 3333);
                        list.put(40111, 2222);
                        list.put(40357, 3333);
                        list.put(40356, 3333);
                        list.put(40366, 3333);
                        list.put(40371, 3333);
                        break;
                    }
                    case 1073: {
                        if (num == 1) {
                            list.put(40041, 100000);
                            list.put(40042, 100000);
                            list.put(40043, 100000);
                            list.put(40044, 100000);
                            list.put(40054, 100000);
                            list.put(40045, 100000);
                            list.put(40046, 100000);
                            list.put(40086, 75000);
                            list.put(40357, 75000);
                            list.put(40356, 75000);
                            list.put(41005, 75000);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 71429);
                            list.put(30042, 71429);
                            list.put(30043, 71429);
                            list.put(30044, 71429);
                            list.put(30054, 57143);
                            list.put(30045, 85714);
                            list.put(30046, 85714);
                            list.put(30086, 57143);
                            list.put(30357, 57143);
                            list.put(30356, 57143);
                            list.put(30551, 57143);
                            list.put(31001, 57143);
                            list.put(40041, 20000);
                            list.put(40042, 20000);
                            list.put(40043, 20000);
                            list.put(40044, 20000);
                            list.put(40054, 20000);
                            list.put(40045, 20000);
                            list.put(40046, 20000);
                            list.put(40086, 15000);
                            list.put(40357, 15000);
                            list.put(40356, 15000);
                            list.put(41005, 15000);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 84821);
                        list.put(30042, 84821);
                        list.put(30043, 84821);
                        list.put(30044, 84821);
                        list.put(30054, 67857);
                        list.put(30045, 101786);
                        list.put(30046, 101786);
                        list.put(30086, 67857);
                        list.put(30357, 67857);
                        list.put(30356, 67857);
                        list.put(30551, 67857);
                        list.put(31001, 67857);
                        list.put(40041, 5000);
                        list.put(40042, 5000);
                        list.put(40043, 5000);
                        list.put(40044, 5000);
                        list.put(40054, 5000);
                        list.put(40045, 5000);
                        list.put(40046, 5000);
                        list.put(40086, 3750);
                        list.put(40357, 3750);
                        list.put(40356, 3750);
                        list.put(41005, 3750);
                        break;
                    }
                    case 1082: {
                        if (num == 1) {
                            list.put(40041, 90909);
                            list.put(40042, 90909);
                            list.put(40043, 90909);
                            list.put(40044, 90909);
                            list.put(40054, 90909);
                            list.put(40045, 90909);
                            list.put(40046, 90909);
                            list.put(40086, 68182);
                            list.put(40056, 45455);
                            list.put(40357, 68182);
                            list.put(40356, 68182);
                            list.put(41007, 68182);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 66667);
                            list.put(30042, 66667);
                            list.put(30043, 66667);
                            list.put(30044, 66667);
                            list.put(30054, 53333);
                            list.put(30045, 80000);
                            list.put(30046, 80000);
                            list.put(30086, 53333);
                            list.put(32091, 13333);
                            list.put(32092, 13333);
                            list.put(32093, 13333);
                            list.put(32094, 13333);
                            list.put(30357, 53333);
                            list.put(30356, 53333);
                            list.put(30551, 53333);
                            list.put(31003, 53333);
                            list.put(40041, 18182);
                            list.put(40042, 18182);
                            list.put(40043, 18182);
                            list.put(40044, 18182);
                            list.put(40054, 18182);
                            list.put(40045, 18182);
                            list.put(40046, 18182);
                            list.put(40086, 13636);
                            list.put(40056, 9091);
                            list.put(40357, 13636);
                            list.put(40356, 13636);
                            list.put(41007, 13636);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 79167);
                        list.put(30042, 79167);
                        list.put(30043, 79167);
                        list.put(30044, 79167);
                        list.put(30054, 63333);
                        list.put(30045, 95000);
                        list.put(30046, 95000);
                        list.put(30086, 63333);
                        list.put(32091, 15833);
                        list.put(32092, 15833);
                        list.put(32093, 15833);
                        list.put(32094, 15833);
                        list.put(30357, 63333);
                        list.put(30356, 63333);
                        list.put(30551, 63333);
                        list.put(31003, 63333);
                        list.put(40041, 4545);
                        list.put(40042, 4545);
                        list.put(40043, 4545);
                        list.put(40044, 4545);
                        list.put(40054, 4545);
                        list.put(40045, 4545);
                        list.put(40046, 4545);
                        list.put(40086, 3409);
                        list.put(40056, 2273);
                        list.put(40357, 3409);
                        list.put(40356, 3409);
                        list.put(41007, 3409);
                        break;
                    }
                    case 1062: {
                        if (num == 1) {
                            list.put(40041, 102564);
                            list.put(40042, 102564);
                            list.put(40043, 102564);
                            list.put(40044, 102564);
                            list.put(40054, 102564);
                            list.put(40045, 102564);
                            list.put(40046, 102564);
                            list.put(40086, 76923);
                            list.put(40086, 51282);
                            list.put(40357, 76923);
                            list.put(40356, 76923);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 71429);
                            list.put(30042, 71429);
                            list.put(30043, 71429);
                            list.put(30044, 71429);
                            list.put(30054, 57143);
                            list.put(30045, 85714);
                            list.put(30046, 85714);
                            list.put(30086, 57143);
                            list.put(30357, 57143);
                            list.put(30356, 57143);
                            list.put(30551, 57143);
                            list.put(31004, 57143);
                            list.put(40041, 20513);
                            list.put(40042, 20513);
                            list.put(40043, 20513);
                            list.put(40044, 20513);
                            list.put(40054, 20513);
                            list.put(40045, 20513);
                            list.put(40046, 20513);
                            list.put(40086, 15385);
                            list.put(40086, 10256);
                            list.put(40357, 15385);
                            list.put(40356, 15385);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 84821);
                        list.put(30042, 84821);
                        list.put(30043, 84821);
                        list.put(30044, 84821);
                        list.put(30054, 67857);
                        list.put(30045, 101786);
                        list.put(30046, 101786);
                        list.put(30086, 67857);
                        list.put(30357, 67857);
                        list.put(30356, 67857);
                        list.put(30551, 67857);
                        list.put(31004, 67857);
                        list.put(40041, 5128);
                        list.put(40042, 5128);
                        list.put(40043, 5128);
                        list.put(40044, 5128);
                        list.put(40054, 5128);
                        list.put(40045, 5128);
                        list.put(40046, 5128);
                        list.put(40086, 3846);
                        list.put(40086, 2564);
                        list.put(40357, 3846);
                        list.put(40356, 3846);
                        break;
                    }
                    case 1672: {
                        if (num == 1) {
                            list.put(40041, 129032);
                            list.put(40042, 129032);
                            list.put(40043, 129032);
                            list.put(40044, 129032);
                            list.put(40054, 129032);
                            list.put(40045, 129032);
                            list.put(40046, 129032);
                            list.put(40086, 96774);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 90909);
                            list.put(30042, 90909);
                            list.put(30043, 90909);
                            list.put(30044, 90909);
                            list.put(30054, 72727);
                            list.put(30045, 109091);
                            list.put(30046, 109091);
                            list.put(30086, 72727);
                            list.put(30551, 72727);
                            list.put(40041, 25806);
                            list.put(40042, 25806);
                            list.put(40043, 25806);
                            list.put(40044, 25806);
                            list.put(40054, 25806);
                            list.put(40045, 25806);
                            list.put(40046, 25806);
                            list.put(40086, 19355);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 107955);
                        list.put(30042, 107955);
                        list.put(30043, 107955);
                        list.put(30044, 107955);
                        list.put(30054, 86364);
                        list.put(30045, 129545);
                        list.put(30046, 129545);
                        list.put(30086, 86364);
                        list.put(30551, 86364);
                        list.put(40041, 6452);
                        list.put(40042, 6452);
                        list.put(40043, 6452);
                        list.put(40044, 6452);
                        list.put(40054, 6452);
                        list.put(40045, 6452);
                        list.put(40046, 6452);
                        list.put(40086, 4839);
                        break;
                    }
                    case 1122: {
                        if (num == 1) {
                            list.put(40041, 93023);
                            list.put(40042, 93023);
                            list.put(40043, 93023);
                            list.put(40044, 93023);
                            list.put(40054, 93023);
                            list.put(40045, 93023);
                            list.put(40046, 93023);
                            list.put(40086, 69767);
                            list.put(40650, 69767);
                            list.put(40656, 69767);
                            list.put(40501, 69767);
                            list.put(40502, 69767);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 90909);
                            list.put(30042, 90909);
                            list.put(30043, 90909);
                            list.put(30044, 90909);
                            list.put(30054, 72727);
                            list.put(30045, 109091);
                            list.put(30046, 109091);
                            list.put(30086, 72727);
                            list.put(30551, 72727);
                            list.put(40041, 18605);
                            list.put(40042, 18605);
                            list.put(40043, 18605);
                            list.put(40044, 18605);
                            list.put(40054, 18605);
                            list.put(40045, 18605);
                            list.put(40046, 18605);
                            list.put(40086, 13953);
                            list.put(40650, 13953);
                            list.put(40656, 13953);
                            list.put(40501, 13953);
                            list.put(40502, 13953);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 107955);
                        list.put(30042, 107955);
                        list.put(30043, 107955);
                        list.put(30044, 107955);
                        list.put(30054, 86364);
                        list.put(30045, 129545);
                        list.put(30046, 129545);
                        list.put(30086, 86364);
                        list.put(30551, 86364);
                        list.put(40041, 4651);
                        list.put(40042, 4651);
                        list.put(40043, 4651);
                        list.put(40044, 4651);
                        list.put(40054, 4651);
                        list.put(40045, 4651);
                        list.put(40046, 4651);
                        list.put(40086, 3488);
                        list.put(40650, 3488);
                        list.put(40656, 3488);
                        list.put(40501, 3488);
                        list.put(40502, 3488);
                        break;
                    }
                    case 1190:
                    case 1191: {
                        if (num == 1) {
                            list.put(40041, 114286);
                            list.put(40042, 114286);
                            list.put(40043, 114286);
                            list.put(40044, 114286);
                            list.put(40051, 57143);
                            list.put(40052, 57143);
                            list.put(40055, 57143);
                            list.put(40070, 57143);
                            list.put(40086, 85714);
                            list.put(42095, 57143);
                            list.put(42096, 57143);
                            list.put(40291, 57143);
                            list.put(40292, 57143);
                            break;
                        }
                        if (num == 2) {
                            list.put(30041, 100000);
                            list.put(30042, 100000);
                            list.put(30043, 100000);
                            list.put(30044, 100000);
                            list.put(30051, 60000);
                            list.put(30052, 60000);
                            list.put(30055, 80000);
                            list.put(30070, 60000);
                            list.put(30086, 80000);
                            list.put(30291, 60000);
                            list.put(40041, 22857);
                            list.put(40042, 22857);
                            list.put(40043, 22857);
                            list.put(40044, 22857);
                            list.put(40051, 11429);
                            list.put(40052, 11429);
                            list.put(40055, 11429);
                            list.put(40070, 11429);
                            list.put(40086, 17143);
                            list.put(42095, 11429);
                            list.put(42096, 11429);
                            list.put(40291, 11429);
                            list.put(40292, 11429);
                            break;
                        }
                        if (num != 3) break;
                        list.put(30041, 118750);
                        list.put(30042, 118750);
                        list.put(30043, 118750);
                        list.put(30044, 118750);
                        list.put(30051, 71250);
                        list.put(30052, 71250);
                        list.put(30055, 95000);
                        list.put(30070, 71250);
                        list.put(30086, 95000);
                        list.put(30291, 71250);
                        list.put(40041, 5714);
                        list.put(40042, 5714);
                        list.put(40043, 5714);
                        list.put(40044, 5714);
                        list.put(40051, 2857);
                        list.put(40052, 2857);
                        list.put(40055, 2857);
                        list.put(40070, 2857);
                        list.put(40086, 4286);
                        list.put(42095, 2857);
                        list.put(42096, 2857);
                        list.put(40291, 2857);
                        list.put(40292, 2857);
                        break;
                    }
                }
            }
            if (!list.isEmpty()) {
                while (potentialid == 0) {
                    int potentialidt = CubeOption.getWeightedRandom(list);
                    if (potentialidt == 0) continue;
                    potentialid = potentialidt;
                    if (num == 3 && !GameConstants.getPotentialCheck(potentialid, args[0], args[1])) {
                        potentialid = 0;
                        continue;
                    }
                    break;
                }
            } else {
                potentialid = 1;
            }
            if (MapleItemInformationProvider.getInstance().getReqLevel(itemid) >= 80) break block154;
            if (potentialid >= 163 && potentialid <= 166) {
                potentialid -= 162;
            }
            if (potentialid == 203) {
                potentialid = MapleItemInformationProvider.getInstance().getReqLevel(itemid) == 10 ? 1 : 201;
            }
        }
        return potentialid;
    }

    public static int getPlatinumUnlimitiedCubePotentialId(int itemid, int grade, int num, int ... args) {//num == 옵션 붙는 줄 
        LinkedHashMap<Integer, Integer> list = new LinkedHashMap<Integer, Integer>();
        int potentialid = 0;
        int type = itemid / 1000;
        type = GameConstants.isWeapon(itemid) || GameConstants.isSecondaryWeapon(itemid) ? 1302 : (GameConstants.isCap(itemid) ? 1004 : (GameConstants.isCape(itemid) ? 1102 : (type == 1132 || type == 1152 ? 1132 : (GameConstants.isLongcoat(itemid) || type >= 1040 && type <= 1042 ? 1042 : (type >= 1070 && type <= 1073 ? 1073 : (type >= 1080 && type <= 1082 ? 1082 : (type >= 1061 && type <= 1063 ? 1062 : (type == 1672 ? 1672 : (type == 1190 || type == 1191 ? 1190 : 1122)))))))));
        if (grade == 4) {
            switch (type) {
                case 1302: {
                    if (num == 1) {
                        list.put(40041, 100000);//힘
                        list.put(40042, 100000);//덱
                        list.put(40043, 100000);//인트
                        list.put(40044, 100000);//럭
                        list.put(40051, 83333);//공
                        list.put(40052, 83333);//마
                        list.put(40055, 83333);//크확
                        list.put(40070, 83333);//데미지
                        list.put(40086, 100000);//올탯
                        list.put(40292, 83333);//방무
                        list.put(40603, 83333);//보공
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 100000);
                        list.put(40042, 100000);
                        list.put(40043, 100000);
                        list.put(40044, 100000);
                        list.put(40051, 83333);
                        list.put(40052, 83333);
                        list.put(40055, 83333);
                        list.put(40070, 83333);
                        list.put(40086, 100000);
                        list.put(40292, 83333);
                        list.put(40603, 83333);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 100000);
                    list.put(40042, 100000);
                    list.put(40043, 100000);
                    list.put(40044, 100000);
                    list.put(40051, 83333);
                    list.put(40052, 83333);
                    list.put(40055, 83333);
                    list.put(40070, 83333);
                    list.put(40086, 100000);
                    list.put(40292, 83333);
                    list.put(40603, 83333);
                    break;
                }
                case 1004: {
                    if (num == 1) {
                        list.put(40041, 150000);
                        list.put(40042, 150000);
                        list.put(40043, 150000);
                        list.put(40044, 150000);
                        list.put(40045, 150000);
                        list.put(40086, 150000);
                        list.put(40557, 100000);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 150000);
                        list.put(40042, 150000);
                        list.put(40043, 150000);
                        list.put(40044, 150000);
                        list.put(40045, 150000);
                        list.put(40086, 150000);
                        list.put(40557, 100000);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 150000);
                    list.put(40042, 150000);
                    list.put(40043, 150000);
                    list.put(40044, 150000);
                    list.put(40045, 150000);
                    list.put(40086, 150000);
                    list.put(40557, 100000);
                    break;
                }
                case 1082: {
                    if (num == 1) {
                        list.put(40041, 144444);
                        list.put(40042, 144444);
                        list.put(40043, 144444);
                        list.put(40044, 144444);
                        list.put(40045, 144444);
                        list.put(40086, 144444);
                        list.put(40056, 133336);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 144444);
                        list.put(40042, 144444);
                        list.put(40043, 144444);
                        list.put(40044, 144444);
                        list.put(40045, 144444);
                        list.put(40086, 144444);
                        list.put(40056, 133336);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 144444);
                    list.put(40042, 144444);
                    list.put(40043, 144444);
                    list.put(40044, 144444);
                    list.put(40045, 144444);
                    list.put(40086, 144444);
                    list.put(40056, 133336);
                    break;
                }
                case 1190:
                case 1191: {
                    if (num == 1) {
                        list.put(40041, 50000);
                        list.put(40042, 50000);
                        list.put(40043, 50000);
                        list.put(40044, 50000);
                        list.put(40051, 166666);
                        list.put(40052, 166666);
                        list.put(40055, 100000);
                        list.put(40070, 100000);
                        list.put(40086, 100000);
                        list.put(40292, 166666);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 50000);
                        list.put(40042, 50000);
                        list.put(40043, 50000);
                        list.put(40044, 50000);
                        list.put(40051, 166666);
                        list.put(40052, 166666);
                        list.put(40055, 100000);
                        list.put(40070, 100000);
                        list.put(40086, 100000);
                        list.put(40292, 166666);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 50000);
                    list.put(40042, 50000);
                    list.put(40043, 50000);
                    list.put(40044, 50000);
                    list.put(40051, 166666);
                    list.put(40052, 166666);
                    list.put(40055, 100000);
                    list.put(40070, 100000);
                    list.put(40086, 100000);
                    list.put(40292, 166666);
                    break;
                }
                case 1042:
                case 1053:
                case 1062:
                case 1073:
                case 1102:
                case 1132:
                case 1152:
                case 1672: {
                    if (num == 1) {
                        list.put(40041, 175000);
                        list.put(40042, 175000);
                        list.put(40043, 175000);
                        list.put(40044, 175000);
                        list.put(40045, 125000);
                        list.put(40086, 175000);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 175000);
                        list.put(40042, 175000);
                        list.put(40043, 175000);
                        list.put(40044, 175000);
                        list.put(40045, 125000);
                        list.put(40086, 175000);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 175000);
                    list.put(40042, 175000);
                    list.put(40043, 175000);
                    list.put(40044, 175000);
                    list.put(40045, 125000);
                    list.put(40086, 175000);
                    break;
                }
                case 1122: {
                    if (num == 1) {
                        list.put(40041, 133333);
                        list.put(40042, 133333);
                        list.put(40043, 133333);
                        list.put(40044, 133333);
                        list.put(40045, 133333);
                        list.put(40086, 133333);
                        list.put(40650, 100001);
                        list.put(40656, 100001);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 133333);
                        list.put(40042, 133333);
                        list.put(40043, 133333);
                        list.put(40044, 133333);
                        list.put(40045, 133333);
                        list.put(40086, 133333);
                        list.put(40650, 100001);
                        list.put(40656, 100001);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 133333);
                    list.put(40042, 133333);
                    list.put(40043, 133333);
                    list.put(40044, 133333);
                    list.put(40045, 133333);
                    list.put(40086, 133333);
                    list.put(40650, 100001);
                    list.put(40656, 100001);
                }
            }
        }
        if (!list.isEmpty()) {
            while (potentialid == 0) {
                int potentialidt = CubeOption.getWeightedRandom(list);
                if (potentialidt == 0) continue;
                potentialid = potentialidt;
                if (num == 3 && !GameConstants.getPotentialCheck(potentialid, args[0], args[1])) {
                    potentialid = 0;
                    continue;
                }
                break;
            }
        } else {
            potentialid = 1;
        }
        return potentialid;
    }

    public static int getUnlimitiedCubePotentialId(int itemid, int grade, int num, int ... args) {
        LinkedHashMap<Integer, Integer> list = new LinkedHashMap<Integer, Integer>();
        int potentialid = 0;
        int type = itemid / 1000;
        type = GameConstants.isWeapon(itemid) || GameConstants.isSecondaryWeapon(itemid) ? 1302 : (GameConstants.isCap(itemid) ? 1004 : (GameConstants.isCape(itemid) ? 1102 : (type == 1132 || type == 1152 ? 1132 : (GameConstants.isLongcoat(itemid) || type >= 1040 && type <= 1042 ? 1042 : (type >= 1070 && type <= 1073 ? 1073 : (type >= 1080 && type <= 1082 ? 1082 : (type >= 1061 && type <= 1063 ? 1062 : (type == 1672 ? 1672 : (type == 1190 || type == 1191 ? 1190 : 1122)))))))));
        if (grade == 4) {
            switch (type) {
                case 1302: {
                    if (num == 1) {
                        list.put(40041, 125000);
                        list.put(40042, 125000);
                        list.put(40043, 125000);
                        list.put(40044, 125000);
                        list.put(40051, 62500);
                        list.put(40052, 62500);
                        list.put(40055, 62500);
                        list.put(40070, 62500);
                        list.put(40086, 125000);
                        list.put(40292, 62500);
                        list.put(40603, 62500);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 125000);
                        list.put(40042, 125000);
                        list.put(40043, 125000);
                        list.put(40044, 125000);
                        list.put(40051, 62500);
                        list.put(40052, 62500);
                        list.put(40055, 62500);
                        list.put(40070, 62500);
                        list.put(40086, 125000);
                        list.put(40292, 62500);
                        list.put(40603, 62500);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 125000);
                    list.put(40042, 125000);
                    list.put(40043, 125000);
                    list.put(40044, 125000);
                    list.put(40051, 62500);
                    list.put(40052, 62500);
                    list.put(40055, 62500);
                    list.put(40070, 62500);
                    list.put(40086, 125000);
                    list.put(40292, 62500);
                    list.put(40603, 62500);
                    break;
                }
                case 1004: {
                    if (num == 1) {
                        list.put(40041, 135959);
                        list.put(40042, 135959);
                        list.put(40043, 135959);
                        list.put(40044, 135959);
                        list.put(40045, 135959);
                        list.put(40046, 135959);
                        list.put(40086, 135959);
                        list.put(40557, 48287);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 135959);
                        list.put(40042, 135959);
                        list.put(40043, 135959);
                        list.put(40044, 135959);
                        list.put(40045, 135959);
                        list.put(40046, 135959);
                        list.put(40086, 135959);
                        list.put(40557, 48287);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 135959);
                    list.put(40042, 135959);
                    list.put(40043, 135959);
                    list.put(40044, 135959);
                    list.put(40045, 135959);
                    list.put(40046, 135959);
                    list.put(40086, 135959);
                    list.put(40557, 48287);
                    break;
                }
                case 1082: {
                    if (num == 1) {
                        list.put(40041, 135000);
                        list.put(40042, 135000);
                        list.put(40043, 135000);
                        list.put(40044, 135000);
                        list.put(40045, 135000);
                        list.put(40046, 135000);
                        list.put(40086, 135000);
                        list.put(40056, 55000);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 135000);
                        list.put(40042, 135000);
                        list.put(40043, 135000);
                        list.put(40044, 135000);
                        list.put(40045, 135000);
                        list.put(40046, 135000);
                        list.put(40086, 135000);
                        list.put(40056, 55000);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 135000);
                    list.put(40042, 135000);
                    list.put(40043, 135000);
                    list.put(40044, 135000);
                    list.put(40045, 135000);
                    list.put(40046, 135000);
                    list.put(40086, 135000);
                    list.put(40056, 55000);
                    break;
                }
                case 1190:
                case 1191: {
                    if (num == 1) {
                        list.put(40041, 100000);
                        list.put(40042, 100000);
                        list.put(40043, 100000);
                        list.put(40044, 100000);
                        list.put(40051, 100000);
                        list.put(40052, 100000);
                        list.put(40055, 100000);
                        list.put(40070, 100000);
                        list.put(40086, 100000);
                        list.put(40292, 100000);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 100000);
                        list.put(40042, 100000);
                        list.put(40043, 100000);
                        list.put(40044, 100000);
                        list.put(40051, 100000);
                        list.put(40052, 100000);
                        list.put(40055, 100000);
                        list.put(40070, 100000);
                        list.put(40086, 100000);
                        list.put(40292, 100000);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 100000);
                    list.put(40042, 100000);
                    list.put(40043, 100000);
                    list.put(40044, 100000);
                    list.put(40051, 100000);
                    list.put(40052, 100000);
                    list.put(40055, 100000);
                    list.put(40070, 100000);
                    list.put(40086, 100000);
                    list.put(40292, 100000);
                    break;
                }
                case 1042:
                case 1053:
                case 1062:
                case 1073:
                case 1102:
                case 1132:
                case 1152:
                case 1672: {
                    if (num == 1) {
                        list.put(40041, 142857);
                        list.put(40042, 142857);
                        list.put(40043, 142857);
                        list.put(40044, 142857);
                        list.put(40045, 142857);
                        list.put(40046, 142857);
                        list.put(40086, 142857);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 142857);
                        list.put(40042, 142857);
                        list.put(40043, 142857);
                        list.put(40044, 142857);
                        list.put(40045, 142857);
                        list.put(40046, 142857);
                        list.put(40086, 142857);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 142857);
                    list.put(40042, 142857);
                    list.put(40043, 142857);
                    list.put(40044, 142857);
                    list.put(40045, 142857);
                    list.put(40046, 142857);
                    list.put(40086, 142857);
                    break;
                }
                case 1122: {
                    if (num == 1) {
                        list.put(40041, 115656);
                        list.put(40042, 115656);
                        list.put(40043, 115656);
                        list.put(40044, 115656);
                        list.put(40045, 115656);
                        list.put(40046, 115656);
                        list.put(40086, 115656);
                        list.put(40650, 95204);
                        list.put(40656, 95204);
                        break;
                    }
                    if (num == 2) {
                        list.put(40041, 115656);
                        list.put(40042, 115656);
                        list.put(40043, 115656);
                        list.put(40044, 115656);
                        list.put(40045, 115656);
                        list.put(40046, 115656);
                        list.put(40086, 115656);
                        list.put(40650, 95204);
                        list.put(40656, 95204);
                        break;
                    }
                    if (num != 3) break;
                    list.put(40041, 115656);
                    list.put(40042, 115656);
                    list.put(40043, 115656);
                    list.put(40044, 115656);
                    list.put(40045, 115656);
                    list.put(40046, 115656);
                    list.put(40086, 115656);
                    list.put(40650, 95204);
                    list.put(40656, 95204);
                }
            }
        }
        if (!list.isEmpty()) {
            while (potentialid == 0) {
                int potentialidt = CubeOption.getWeightedRandom(list);
                if (potentialidt == 0) continue;
                potentialid = potentialidt;
                if (num == 3 && !GameConstants.getPotentialCheck(potentialid, args[0], args[1])) {
                    potentialid = 0;
                    continue;
                }
                break;
            }
        } else {
            potentialid = 1;
        }
        for (Map.Entry l : list.entrySet()) {
            if ((Integer)l.getKey() != potentialid) continue;
            double a = ((Integer)l.getValue()).intValue();
            double r = a / 10000.0;
            DecimalFormat form = new DecimalFormat("#.#####");
            break;
        }
        return potentialid;
    }

    public static int getWeightedRandom(Map<Integer, Integer> weights) {
        int potentialid = 0;
        double bestValue = Double.MAX_VALUE;
        for (Map.Entry<Integer, Integer> element : weights.entrySet()) {
            double a = element.getValue().intValue();
            double r = a / 10000.0;
            double value = -Math.log(Randomizer.nextDouble()) / r;
            if (!(value < bestValue)) continue;
            bestValue = value;
            potentialid = element.getKey();
        }
        return potentialid;
    }
}

