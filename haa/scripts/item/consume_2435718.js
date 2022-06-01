var status;
var items = [[2049360, 1], [2470003, 1], [5062009, 750], [5062010, 500], [2450064, 30], [2450124, 50], [2003551, 10], [2022462, 30], [2022463, 15]];
importPackage(Packages.server);

function start() {
    status = -1;
    action (1, 0, 0);
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
	var zz= "100% 확정 상품 : #i4310218:##z4310218:# 3 ~ 5개 지급.\r\n" +
		"#d#i2435718:##z2435718:#에서 획득 가능한 상품 목록입니다.\r\n";
		for (i = 0; i < items.length; i++) {
		zz += "#i" + items[i][0] + ":##z" + items[i][0] + ":# " + items[i][1] + "개\r\n";
		}
	zz += "상자를 개봉하시겠습니까?";
	cm.sendYesNo(zz);
    } else if (status == 1) {
	rand = Math.floor(Math.random() * items.length);
	rand2 = Randomizer.rand(3, 5);
	cm.gainItem(4310218, rand2);
	cm.gainItem(items[rand][0], items[rand][1]);
	cm.dispose();
	cm.gainItem(2435718, -1);
	cm.sendOk("#b[100% 획득!] #i4310218:##z4310218:#이 " + rand2 + "개 지급되었습니다.#k\r\n#i" + items[rand][0] + ":##z" + items[rand][0] + ":# 아이템이 " + items[rand][1] + "개 지급되었습니다.");
    }
}