importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.database);

importPackage(java.sql);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.math);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    outmap = 921172200
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	if (cm.getPlayer().getGMLevel() == 0) {
	cm.sendOkS("유니온 레이드 오픈 준비중입니다. 잠시 기다려주세요 :D", 0x04, 9010106);
	cm.dispose();
	return;
	}
        if (cm.getPlayer().getMapId() != outmap) {
            talk = "#r#e거대한 드래곤과의 결투#k#n가 용사님을 기다리고 있습니다!\r\n"
            talk += "#b#e유니온 레이드에 입장#k#n 하시겠습니까?"
            cm.sendYesNoS(talk,0x04,9010106);
        } else {
            var eim = cm.getPlayer().getEventInstance();
            hp1 = (Long.MAX_VALUE - eim.getMobs().get(1).getHp() - cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp")) >= 10000000000000 ? 10000000000000 : (Long.MAX_VALUE - eim.getMobs().get(1).getHp() - cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp"))
            coin = Math.floor((hp1 + (Long.MAX_VALUE - eim.getMobs().get(0).getHp())) / 100000000000)
            if (coin == 0) {
                talk = "음~ 아직 유니온 코인을 하나도 얻지 못하셨군요? 획득이 너무 어려우시다면 시간을 조금 가진 뒤 들어와 보세요. 유니온의 구성원들이 열심히 코인을 모아 둘 거에요."
            } else {
                talk = "#i4310229# #b#z4310229##k을 #b" + coin + "개#k나 모으셨군요? 대단해요~"
            }
            cm.sendNextS(talk,0x04,9010106);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() != outmap) {
            if (cm.getPlayer().getKeyValue(20190622, "Union_Raid_Time_1") < 0) {
                cm.getPlayer().setKeyValue(20190622, "Union_Raid_Time_1", new Date().getTime());
            }
            if (cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp") < 0) {
                cm.getPlayer().setKeyValue(20190622, "Union_Raid_Hp", 0);
            }
            cm.getPlayer().setKeyValue(20190622, "Union_Raid_Atk", getAtk()); // 계산한걸로 변경
            cm.getEventManager("test1").startInstance_Solo("" + 921172000, cm.getPlayer());
            cm.dispose();
	    return;
        } else {
            cm.sendNextS("그럼 왔던곳으로 돌려 보내 드릴게요. 안녕히 가세요~",0x04,9010106);
        }
    } else if (status == 2) {
        cm.getPlayer().setKeyValue(20190622, "Union_Raid_Time_1", new Date().getTime());
        cm.getPlayer().setKeyValue(20190622, "Union_Raid_Hp", hp1);
	cm.warp(680000175);
        cm.dispose();
    }
}

function getAtk() {
	var UnionAtt = 0;
	var getUnionChr = 0;
 	var db = DatabaseConnection; // 얘를 닫으면 안되는거
        var con = db.getConnection();
        var getAcc = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + cm.getClient().getAccID() + " ORDER BY level DESC").executeQuery();
        while (getAcc.next()) {
            if (getUnionChr < 40) {
                if (getAcc.getInt("level") >= 60) {
      		    getUnionChr++;
                    UnionAtt += getAtkByLevel(getAcc.getInt("level"))
                }
            }
        }
        getAcc.close();
	return Math.floor(UnionAtt);
}

function getAtkByLevel(chrlevel) {
   if (chrlevel >= 240) {
      atk = 1.25
   } else if (chrlevel >= 230) {
      atk = 1.2
   } else if (chrlevel >= 220) {
      atk = 1.15
   } else if (chrlevel >= 210) {
      atk = 1.1
   } else if (chrlevel >= 200) {
      atk = 1
   } else if (chrlevel >= 180) {
      atk = 0.8
   } else if (chrlevel >= 140) {
      atk = 0.7
   } else if (chrlevel >= 100) {
      atk = 0.4
   } else {
      atk = 0.5
   }
   return ((chrlevel * chrlevel * chrlevel) * atk) + 12500;
}