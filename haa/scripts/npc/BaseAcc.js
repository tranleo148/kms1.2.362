// by MelonK
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

var status = -1;
isOk = true;
function start() {
   status = -1;
   action(1, 0, 0);
}

bossa = Math.floor(Math.random() * 30) + 250 // 최소 10 최대 35 , 혼테일
bossg = Math.floor(Math.random() * 30) + 250 // 최소 5, 최대 15, 혼테일

function action(mode, type, selection) {
   ing = [
      [[4310237,30], [4036491,5], [4021031,30]],
      [[1113067,1], [4021031,150]],
      [[1032241,1], [4021031,150]],
      [[1152170,1], [4021031,150]],
      [[1112593,1], [4021031,50]],
      [[1112665,1], [4021031,50]],
      [[1112664,1], [4021031,50]],
      [[1112666,1], [4021031,50]],
      [[1112667,1], [4021031,50]],
      ]; // 재료
   epsol = [1190302, 1113055, 1032200, 1152154, 1113067, 1113064, 1113063, 1113065, 1113066]; // 완제
   chance = 100; // 확률
   if (mode == -1 || mode == 0) {
      cm.dispose();
      return;
   }
   if (mode == 1) {
      status++;
   }
   if (status == 0) {
      말 = "\r\n#fs11##fc0xFF000000#기본 악세서리를 제작 하겠습니까?#n\r\n\r\n";
      말+= "#L0##b제작하겠습니다.";
      cm.sendSimple(말);
   } else if (status == 1) {
      말 = "\r\n#fs11##fc0xFF000000#제작하실 아이템을 선택해주세요#k\r\n"
      for (i=0; i<epsol.length; i++) {
         말 += "#L"+i+"# #i"+epsol[i]+"# #b#z"+epsol[i]+"##k\r\n";
      }
      cm.sendSimple(말);
   } else if (status == 2) {
      st = selection;
      말 = "#b#e선택된 아이템 : #k#n #i"+epsol[st]+"# #r#z"+epsol[st]+"##k\r\n\r\n"
      말+= "위 아이템을 제작하기 위해서는 아래와 같은 아이템이 필요합니다.\r\n\r\n";
      for (i=0; i<ing[st].length; i++) {
         말 += "#i"+ing[st][i][0]+"# #b#z"+ing[st][i][0]+"##k"
         if (cm.itemQuantity(ing[st][i][0]) >= ing[st][i][1]) {
            말 += "#fc0xFF41AF39##e ("+cm.itemQuantity(ing[st][i][0])+"/"+ing[st][i][1]+")#k#n\r\n";
         } else {
            말 += "#r#e ("+cm.itemQuantity(ing[st][i][0])+"/"+ing[st][i][1]+")#k#n\r\n";
            isOk = false;
         }
      }
      말 += "\r\n"
      if (isOk) {
         말 += "#L0# #b아이템을 제작하겠습니다.#k";
         cm.sendSimple(말);
      } else {
         말 += "#r재료가 충분하지 않아 아이템을 제작할 수 없습니다.#k";
         cm.sendOk(말);
         cm.dispose();
      }
   } else if (status == 3) {
        if (Math.floor(Math.random() * 100) <= 100) {
                    if (epsol[st] >= 2000000) {
                       cm.gainItem(epsol[st], 1);
      for (i=0; i<ing[st].length; i++) {
         cm.gainItem(ing[st][i][0], -ing[st][i][1]);
      }
         cm.sendOk("제작에 성공하셨습니다.");
                    }
         cm.gainItem(epsol[st], 1);
      for (i=0; i<ing[st].length; i++) {
         cm.gainItem(ing[st][i][0], -ing[st][i][1]);
      }
         cm.sendOk("제작에 성공하셨습니다.");
         //cm.broadcastSmega(8,"[제작] "+ cm.getPlayer().getName()+" 님이 스칼렛 방어구 제작에 성공하였습니다")
      } else {
      for (i=1; i<ing[st].length; i++) {
         cm.gainItem(ing[st][i][0], -ing[st][i][1]);
      }
         cm.sendOk("제작에 실패하셨습니다.");
         //cm.broadcastSmega(8,"[제작] "+ cm.getPlayer().getName()+" 님이 스칼렛 방어구 제작에 실패하였습니다")
      }
      cm.dispose();
      return;
   }
}