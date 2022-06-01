importPackage(Packages.constants);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);


req = [
[0, [[4001716,3], [4310266,100], [4310308,50]], 10],
[0, [[4001716,5], [4310266,200], [4310308,100]], 10],
[0, [[4001716,7], [4310266,300], [4310308,350]],  10],
[0, [[4001716,9], [4310266,400], [4310308,700]],  10],
[0, [[4001716,15], [4310266,500], [4310308,1500]],  10],
[0, [[4001716,25], [4310266,600], [4310308,2700]],  10],
[0, [[4001716,35], [4310266,700], [4310308,3000]],  10],
[0, [[4001716,50], [4310266,800], [4310308,5000]],  10]
]
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {     
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (gK() >= 8) {
            cm.sendOk("최고단계까지 승급을 하여 더 이상 승급을 하실 수 없습니다.");
            cm.dispose();
            return;
        }
        //if (gK() <= 0) { sK(1); }
        말 = "#fs11##fc0xFF990033##e보스랭크 승급 시스템#n#fc0xFF000000#이라네\r\n#b보스랭크 승급#fc0xFF000000#을 통해 더욱 더 강해져보지 않겠나!?\r\n\r\n"
        말+= "#L0##b"+(gK() + 1)+"레벨로 승급하겠습니다.#l\r\n";
        말+= "#L1##b보스랭크란 무엇인가요?#l"
        cm.sendSimple(말);
    } else if (status == 1) { 
        if (selection == 0) {
            말 = (gK() + 1) + "#fs11#레벨로 레벨업을 하기 위해선 아래와 같은 재료가 필요하다네.\r\n\r\n"

            for (i=0; i<req[gK()-0][1].length; i++) {
                말 += "#i"+req[gK()][1][i][0]+"# #b#z"+req[gK()-0][1][i][0]+"##r "+req[gK()][1][i][1]+"개#k\r\n"; // 개수 : cm.itemQuantity(req[gK()][1][i][0])
            }
            //말+= "#i4031138# #b메소 "+req[gK()][2]+"#k\r\n\r\n"
            말+= " \r\n#fs11##e#b정말 승급을 할텐가?#k#n"
            cm.sendYesNo(말);
        } else {
            cm.sendOk("#fs11##fc0xFF990033#[랭크별 승급버프]\r\n\r\n#b보스 공격력 + 10%\r\n\r\n#fc0xFF990033#[랭크별 혜택]\r\n\r\n#b[보스 랭크 1레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 노말 하드 스우, 데미안 입장횟수 +1#k\r\n\r\n #b[보스 랭크 2레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 노말 하드 루시드, 윌 입장횟수 +1#k\r\n\r\n #b[보스 랭크 3레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 크로스 보스 입장가능\r\n- 진힐라 입장 횟수 +1#k\r\n\r\n #b[보스 랭크 4레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 검은 마법사 입장횟수 +1\r\n- 더스크 입장 횟수 +1\r\n- 듄켈 입장횟수 +1\r\n\r\n #b[보스 랭크 5레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 노말 하드 스우, 데미안 입장횟수 +1#k\r\n\r\n #b[보스 랭크 6레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 노말 하드 루시드, 윌 입장횟수 +1#k\r\n\r\n #b[보스 랭크 7레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 아래 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 크로스 입장횟수 +1\r\n- 진힐라 입장횟수 +1#k\r\n\r\n #b[보스 랭크 8레벨 혜택]#k\r\n#fc0xFF6600CC#- 카루타 이하 모든 보스 입장횟수 +1 (시그너스 제외)\r\n- 검은 마법사 입장횟수 +1\r\n- 크로스 입장횟수 +1#k\r\n\r\n");
            cm.dispose();
        }
    } else if (status == 2) {
        if (cm.getPlayer().getBossPoint() < req[gK()][0]) {
            cm.sendOk("보스포인트가 부족합니다.");
            cm.dispose();
            return;
        }
        for (i=0; i<req[gK()-0][1].length; i++) {
            if (cm.itemQuantity(req[gK()][1][i][0]) < req[gK()][1][i][1]) {
                cm.sendOk("승급에 필요한 #e재료#n가 모자란 것 같군.");
                cm.dispose();
                return;
            }
        }
        if (cm.getPlayer().getMeso() < req[gK()][2]) {
            cm.sendOk("승급에 필요한 #e메소#n가 모자란 것 같군.");
            cm.dispose();
            return;
        }
        ggK = gK();
        if (Math.floor(Math.random() * 100) < 100) {
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[보스헌터] "+cm.getPlayer().getName()+"님이 "+(gK()+1)+"레벨로 레벨업에 성공하였습니다."));
           cm.getPlayer().setBossTier(gK() + 1);
           말 = "축하한다네! 승급에 성공했군\r\n"
           cm.showEffect(false,"tdAnbur/idea_hyperMagic");
           cm.sendOk(말);
        } else {
           cm.sendOk("레벨업에 실패하였습니다.")
        }
        cm.getPlayer().loseBossPoint(req[ggK][0]);
        for (i=0; i<req[ggK][1].length; i++) {
            cm.gainItem(req[ggK][1][i][0], -req[ggK][1][i][1]);
        }
        cm.gainMeso(-req[ggK][2]);
        cm.dispose();
    }
}

function gK() {
    return cm.getPlayer().getBossTier();
}

function sK() {
    return cm.getPlayer().setBossTier();
}