var itmelist= [
[4001760, 50000, 1],
[5060048, 9900, 5],
[2630648, 100000, 1],
[2630127, 15000, 1],
[2049372, 9900, 1],
[2048753, 19900, 50],
[5062006, 20000, 10],
[2046076, 5000, 1],
[2046076, 25000, 5],
[2046077, 5000, 1],
[2046077, 25000, 5],
[2046150, 5000, 1],
[2046150, 25000, 5],
[2046340, 5000, 1],
[2046340, 25000, 5],
[2046341, 5000, 1],
[2046341, 25000, 5],
[2048047, 5000, 1],
[2048047, 25000, 5],
[2048048, 5000, 1],
[2048048, 25000, 5],
[2046251, 5000, 1],
[2046251, 25000, 5],
[2632130, 40000, 1],
[2632131, 40000, 1],
[2632132, 40000, 1],
[2632133, 40000, 1],
[2632134, 40000, 1],
[2632135, 40000, 1],
[2439304, 3000000, 1],
[2633584, 3000000, 1],
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {

	              
         var a = "#fs11##fc0xFFFF3366##h0# #fc0xFF000000#님의 도네이션 포인트 : #fc0xFFFF3366#"+cm.getPlayer().getDonationPoint()+" P#k#n\r\n"; 
		for(var i = 0; i<18; i++){
			a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#k #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n";
		}
		for(var i = 18; i<24; i++){
			a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화비용 별도\r\n";
		}
		for(var i = 24; i<itmelist.length; i++){
			a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화1비용 별도\r\n";
		}
		cm.sendSimple(a);

		} else if (selection >= 0 && selection <= itmelist.length) {
		if (cm.getPlayer().getDonationPoint() >= itmelist[selection][1]) {
		    if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
		        cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
		        cm.dispose();
			}
		    if (cm.canHold(itmelist[selection][0])) {
		        cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
		        cm.dispose();
			}
				cm.getPlayer().gainDonationPoint(-itmelist[selection][1]);
				cm.gainItem(itmelist[selection][0], itmelist[selection][2]);
		        cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
				cm.dispose();
		} else {
		    cm.sendOk("#fs11##b후원 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		}
	}
}