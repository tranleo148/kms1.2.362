importPackage(Packages.constants);


var status = -1;

var 별 = "#fUI/FarmUI.img/objectStatus/star/whole#";
var HOT = "#fUI/CashShop.img/CSEffect/hot/0#";
var NEW = "#fUI/CashShop.img/CSEffect/new/0#";


function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {

        var choose = "#fs11##fc0xFF050099##k\r\n";
        choose += "#L1##fUI/UIWindow2.img/MobGage/Mob/9300902##e 월묘의 떡#n#l\r\n";


        cm.sendSimple(choose);

    } else if (selection == 1) {//월묘
        cm.dispose();
	cm.warpParty(933000000);
    }

}