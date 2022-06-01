/*
제작자 : ljw5992@naver.com / dbg_yeane@nate.com
*/

importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.tools.RandomStream);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server.life);
importPackage(Packages.main.world);
importPackage(Packages.packet.creators);
importPackage(Packages.launch.world);

var status = -1;
var itemid = new Array(2048717,2049116,2048717,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2048717,2049116,2430632,2048702,2048703,2049116,2049116,2049116,2049116,2049116,2049122,2049360,4001550,4001551,4001209,1142249);

function start() {
 action(1, 0, 0);
}

function action(mode, type, selection) {
 if (mode == 1) {
  status++;
 } else {
  status--;
  cm.dispose();
 }
 if (status == 0) {
  cm.sendYesNo("핫타임 상자를 열겠보겠어? 모든칸 을 2칸 이상 비워둬");
 } else if (status == 1) {

  if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() > 2){
   itemSet = itemid[Math.floor(Math.random() * itemid.length)];
   cm.sendOk("#i" + itemSet + "##b(#z"+itemSet+"##k)를 획득했습니다. 루시아를 통해 얻어주세요!");
   cm.gainItem(2431536, -1);
  if(itemSet == 4001832) {
   cm.getPlayer().addRewardDB(cm.getPlayer().getId(),itemSet, Randomizer.rand(30,150));
   } else {
    cm.getPlayer().addRewardDB(cm.getPlayer().getId(),itemSet,1);
   }
   if(itemSet == 2049360 || itemSet == 2049122 || itemSet == 4001209 || itemSet == 1142249) {
   WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(20, cm.getPlayer().getName() + "님이 핫타임 상자에서 ("+Packages.server.items.ItemInformation.getInstance().getName(itemSet)+") 를 얻었습니다."));
   }
   cm.dispose();
  } else {
   cm.sendOk("장비창에 공간이 부족해");
   cm.dispose();
  }
 }
}
