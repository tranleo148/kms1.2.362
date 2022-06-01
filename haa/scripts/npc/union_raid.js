importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.tools.packet);

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
    outmap2 = 921172201
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (cm.getPlayer().getMapId() != outmap && cm.getPlayer().getMapId() != outmap2) {
            talk = "#r#e거대한 드래곤과의 결투#k#n가 용사님을 기다리고 있습니다!\r\n"
            talk += "#b#e유니온 레이드에 입장#k#n 하시겠습니까?"
            cm.sendYesNoS(talk,0x04,9010106);
        } else {
            if (coin == 0) {
                talk = "음~ 아직 유니온 코인을 하나도 얻지 못하셨군요? 획득이 너무 어려우시다면 시간을 조금 가진 뒤 들어와 보세요. 유니온의 구성원들이 열심히 코인을 모아 둘 거에요."
            } else {
                talk = "#i4310229# #b#z4310229##k을 #b" + coin + "개#k나 모으셨군요? 대단해요~"
            }
            cm.sendNextS(talk,0x04,9010106);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() != outmap && cm.getPlayer().getMapId() != outmap2) {
            if (cm.getClient().getKeyValue("UnionLaidLevel") == null) {
                cm.getClient().setKeyValue("UnionLaidLevel", "1");
            }
            cm.getPlayer().setSkillCustomInfo(232471, cm.getPlayer().getMapId(), 0);
            mobid = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 9833101 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 9833102 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 9833103 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 9833104 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 9833105 : 0;
            mobhp = 10000000000000;
            nowmobhp = (mobhp - (cm.getPlayer().getUnionAllNujuk() + cm.getPlayer().getUnionNujuk()));
            mobid2 = mobid + 100;
            mob2hp = 2500000000000;
            nowmob2hp = (mob2hp - cm.getPlayer().getUnionNujuk());
            rand = Randomizer.isSuccess(50) ? 921172000 : 921172100;
            cm.warp(rand);
            if (nowmobhp <= 0) {// 단계초기화
                cm.getPlayer().setUnionAllNujuk(0);
                cm.getPlayer().setUnionNujuk(0);
                cm.getPlayer().setUnionEnterTime(0);
                abc = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 2 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 3 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 4 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 5 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 1 : 0;
                nowmobhp = mobhp;
                nowmob2hp = mob2hp;
                cm.getPlayer().getClient().setKeyValue("UnionLaidLevel", ""+abc+"");
            }
            if (nowmob2hp <= 0) {
                nowmob2hp = 1;
            }
            mobid = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 9833101 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 9833102 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 9833103 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 9833104 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 9833105 : 0;
            mobid2 = mobid + 100;
            cm.spawnLinkMobsetHP(mobid, 2320,17, nowmobhp, mobhp);
            cm.spawnLinkMobsetHP(mobid2, 2320,17, nowmob2hp, mob2hp);
            cm.getPlayer().getClient().send(SLFCGPacket.CameraCtrl(14, 1, 100, 800, 800));
            cm.getPlayer().getClient().send(CField.setUnionRaidScore(cm.getPlayer().getUnionAllNujuk()));
            cm.getPlayer().getClient().send(CField.showUnionRaidHpUI(mobid, nowmob2hp, mob2hp, mobid2, nowmobhp, mobhp));
            cm.getPlayer().getClient().send(CField.setUnionRaidCoinNum(cm.getPlayer().getUnionCoin(), true));
            cm.dispose();
	        return;
        } else {
            cm.sendNextS("그럼 왔던곳으로 돌려 보내 드릴게요. 안녕히 가세요~",0x04,9010106);
        }
    } else if (status == 2) {
        cm.warp(cm.getPlayer().getSkillCustomValue0(232471));
        cm.dispose();
    }
}