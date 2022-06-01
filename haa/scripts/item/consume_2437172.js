var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.constants);
importPackage(Packages.packet.creators);importPackage(Packages.client.items);
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
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
        if (cm.getPlayer().getLevel() >= 10 && cm.getPlayer().getKeyValue(0, "30시간")) {
		cm.gainItem(5062500, 1000);
		cm.gainItem(5062010, 1000);
		cm.gainItem(4001715, 30);
		cm.gainItem(4000492, 1);
		cm.gainItem(4031289, 1);
                cm.getPlayer().gainAp(20);
	        //cm.getPlayer().setKeyValue(201909081, "chair_p", cm.getPlayer().getKeyValue(201909081, "chair_p") + 1799);
                cm.getPlayer().setKeyValue(0, "30시간", ""+(cm.getPlayer().getKeyValue(0, "30시간") + 1));
		cm.gainItem(2437172, -1);
		cm.dispose();
    } else {
     cm.sendOk("#r조건이 일치하지 않습니다.#k");
     cm.dispose();
}
	}
}
