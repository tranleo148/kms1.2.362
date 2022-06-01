importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

var status = -1;

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        qm.sendNext("지금 받지 않겠다는거야? 필요하면 다시 찾아와서 받아가도록 해. 매주 한 번 지급받을 수 있어.", 2010010);
        qm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 2) {
            qm.sendOk("인벤토리 소비탭 2칸을 비우고 다시 말을 걸어줘, 물품은 받아야 될 것 아니야?");
            qm.dispose();
            return;
        }
        qm.sendYesNo("\r\n#r매주 한 번#k 지급되는 #b길드 정기 지원 물품#k이 도착했어.\r\n지금 지원 물품을 받겠어?", 2010010);
    } else if (status == 1) {
        qm.forceCompleteQuest();
        var itemid = 2432290;
        var pootioncount = qm.getPlayer().getGuild().getSkillLevel(91000009) * 25;
        var itemcount = qm.getPlayer().getGuild().getSkillLevel(91000009) * 5;
        if (qm.getPlayer().getGuild().getSkillLevel(91000031) >= 1) {
            itemid = 2631501;
            pootioncount += 100;
            itemcount += 5;
        }
        if (qm.getPlayer().getGuild().getSkillLevel(91000032) >= 1) {
            itemid = 2631501;
            pootioncount += 100;
            itemcount += 5;
        }
        time = new Date();
        var day2 = time.getDay();
        var check = day2 == 1 ? 7 : day2 == 2 ? 6 : day2 == 3 ? 5 : day2 == 4 ? 4 : day2 == 5 ? 3 : day2 == 6 ? 2 : day2 == 0 ? 1 : 0;
        var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 1);
        inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * check));
        inz.setQuantity(itemcount);
        MapleInventoryManipulator.addbyItem(qm.getClient(), inz);
        inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(2002037, 0, 1);
        inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * check));
        inz.setQuantity(pootioncount);
        MapleInventoryManipulator.addbyItem(qm.getClient(), inz);
        qm.sendOk("지급받은 물품은 이번 주 일요일까지 쓸 수 있어.\r\n열심히 활동해서 스킬 레벨이 올라가면 정기 지원 물품을 더 많이 받을 수 있으니까 노력하라구.\r\n그럼 이번 주도 수고하고, 다음 주에 지원 물품이 다시 도착 하니까 그때 보자구.", 2010010);
        mon = time.getMonth() + 1;
        var st = mon < 10 ? "0" : ""
        st += ""+mon;
        qm.getPlayer().addKV("GuildBless", time.getFullYear() +""+ st +"" + time.getDate());
        qm.dispose();
    }
}