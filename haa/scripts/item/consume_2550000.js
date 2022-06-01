importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.server.items);

var status = -1;
var need = 2550000
var arr = "1182000, 1182001, 1182002, 1182000, 1182001, 1182003, 1182004, 1182000, 1182001, 1182000, 1182001, 1182005, 1182000, 1182001, 1182000, 1182002, 1182003, 1182004, 1182005, 1182001, 1182000, 1182000, 1182001, 1182001, 1182000, 1182001, 1182002, 1182003";

var str = Math.floor(Math.random()*15);
var dex = Math.floor(Math.random()*15);
var lnt = Math.floor(Math.random()*15);
var luk = Math.floor(Math.random()*15);
var watk = Math.floor(Math.random()*10); 
var matk = Math.floor(Math.random()*10);
var wdef = Math.floor(Math.random()*300);
var mdef = Math.floor(Math.random()*300);
var acc = Math.floor(Math.random()*300);
var avoid = Math.floor(Math.random()*300);
var speed = Math.floor(Math.random()*40);
var jump  = Math.floor(Math.random()*20);
var hp = Math.floor(Math.random()*3000);
var mp  = Math.floor(Math.random()*3000);

var allstats = Math.floor(Math.random()*6);

var a = Math.floor(Math.random()*100);
var b = Math.floor(Math.random()*100);
var c = Math.floor(Math.random()*100);
var d = Math.floor(Math.random()*100);
var e = Math.floor(Math.random()*100);
var f = Math.floor(Math.random()*100);
var g = Math.floor(Math.random()*100);
var h = Math.floor(Math.random()*100);
var i = Math.floor(Math.random()*100);
var j = Math.floor(Math.random()*100);
var k = Math.floor(Math.random()*100);
var l = Math.floor(Math.random()*100);
var m = Math.floor(Math.random()*100);
var n = Math.floor(Math.random()*100);
var o = Math.floor(Math.random()*100);

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
 if (status == 0){
  if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() > 2){
   var itemid = arr.split(",")[Math.floor(Math.random()*28)/1];
   var item =  ItemInformation.getInstance().getEquipById(itemid);

   if(itemid == 1182000 || itemid == 1182001) {
   var n = 5
   } if (itemid == 1182002 || itemid == 1182003) {
   var n = 3
   } if (itemid == 1182004 || itemid == 1182005) {
   var n = 1
   }

   if(a < 40) {
   item.setStr(str/n);
   } if(b < 40) {
   item.setDex(dex/n);
   } if(c < 40) {
   item.setInt(lnt/n);
   } if(d < 40) {
   item.setLuk(luk/n);
   } if(e < 40) {
   item.setWatk(watk/n);
   } if(f < 40) {
   item.setMatk(matk/n);
   } if(g < 40) {
   item.setWdef(wdef/n);
   } if(h < 40) {
   item.setMdef(mdef/n);
   } if(i < 40) {
   item.setAcc(acc/n);
   } if(j < 40) {
   item.setAvoid(avoid/n);
   } if(k < 40) {
   item.setSpeed(speed/n);
   } if(l < 40) {
   item.setJump(jump/n);
   } if(m < 40) {
   item.setHp(hp/n);
   } if(n < 40) {
   item.setMp(mp/n);
   } if(o < 20) {
   item.setAllStatP(allstats/n);
   } 
   InventoryManipulator.addbyItem(cm.getClient(), item, true);
   cm.gainItem(need, -1);
   cm.dispose();
  } else {
   cm.sendOk("장비칸의 빈 공간이 없습니다. 2칸 이상의 여유 공간을 만드신 뒤 다시 시도해주세요.");
   cm.dispose();
  }
 }
}