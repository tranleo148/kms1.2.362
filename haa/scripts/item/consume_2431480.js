/*
제작자 : ljw5992@naver.com / Harmony_yeane@nate.com
*/

importPackage(Packages.client.items);

var status = -1;
var rand = Math.floor(Math.random()*10);

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
  cm.sendYesNo("핫타임 상자를 열겠보겠어? 장비칸을 2칸 이상 비워둬\r\n아이템 당첨확률은 30%야.");

 } else if (status == 1) {
if (rand < 3){
  if (cm.getPlayer().getInventory(HarmonyInventoryType.EQUIP).getNumFreeSlot() > 2){
   cm.sendOk("#i1112594##b(#z1112594##k)를 획득했습니다.");
   cm.gainItem(2431480, -1);
   cm.gainItem(itemid,1);
   cm.dispose();
  } else {
   cm.sendOk("장비창에 공간이 부족해");
   cm.dispose();
  }
}else{
   cm.sendOk("꽝");
}
 }
}
