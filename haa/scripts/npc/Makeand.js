var enter = "\r\n";
var seld = -1;

var items = [
   {'itemid' : 1662000, 'qty' : 1, 'allstat' : 10, 'atk' : 10,
   'recipes' : [[4036491, 20], [4310237, 50], [4009005, 20]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1662001, 'qty' : 1, 'allstat' : 10, 'atk' : 10,
   'recipes' : [[4036491, 20], [4310237, 50], [4009005, 20]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1662002, 'qty' : 1, 'allstat' : 30, 'atk' : 30,
   'recipes' : [[1662000, 1], [4036491, 40], [4310237, 100], [4009005, 40]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1662003, 'qty' : 1, 'allstat' : 30, 'atk' : 30,
   'recipes' : [[1662001, 1], [4036491, 40], [4310237, 100], [4009005, 40]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1662164, 'qty' : 1, 'allstat' : 50, 'atk' : 50,
   'recipes' : [[1662002, 1], [4036491, 80], [4310237, 200], [4009005, 80]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1662165, 'qty' : 1, 'allstat' : 50, 'atk' : 50,
   'recipes' : [[1662003, 1], [4036491, 80], [4310237, 200], [4009005, 80]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1672000, 'qty' : 1, 'allstat' : 10, 'atk' : 10,
   'recipes' : [[4036491, 20], [4310237, 50], [4009005, 20]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1672003, 'qty' : 1, 'allstat' : 30, 'atk' : 30,
   'recipes' : [[1672000, 1], [4036491, 40], [4310237, 100], [4009005, 40]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
   {'itemid' : 1672007, 'qty' : 1, 'allstat' : 50, 'atk' : 50,
   'recipes' : [[1672003, 1], [4036491, 80], [4310237, 200], [4009005, 80]], 'price' : 10, 'chance' : 50,
   'fail' : [[4001715, 1]]
   },
] // 올스탯과 공마는 장비아이템에만 적용됨

var item;
var isEquip = false;
var canMake = false;

function start() {
   status = -1;
   action(1, 0, 0);
}
function action(mode, type, sel) {
   if (mode == 1) {
      status++;
   } else {
      cm.dispose();
      return;
       }
   if (status == 0) {
      var msg = "#fs11#안드로이드를 제작할 수 있는 레시피가 적힌 도감이다.#k#n"+enter;
      msg += "";
      for (i = 0; i < items.length; i++)
         msg += "#fs11##b#L"+i+"##i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# "+items[i]['qty']+"개#k"+enter;

      cm.sendSimple(msg);
   } else if (status == 1) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1000000) == 1;

      canMake = checkItems(item);

      var msg = "#fs11#선택한 아이템은 다음과 같다.#fs11##b"+enter;
      msg += "#fs11#아이템 : #i"+item['itemid']+"##z"+item['itemid']+"# "+item['qty']+"개"+enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "올스탯 : +"+item['allstat']+enter;
         if (item['atk'] > 0)
            msg += "공격력, 마력 : +"+item['atk']+enter;
      }

      msg += enter;
      msg += "#fs11##k선택한 아이템을 제작하기 위한 레시피, #fs11# #b제작 확률은 50%#k\r\n제작 실패시 #b#i4001715##z4001715##k 지급#d"+enter+enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#b#i"+item['recipes'][i][0]+"##z"+item['recipes'][i][0]+"# "+item['recipes'][i][1]+"개 #r/ #c"+item['recipes'][i][0]+"#개 보유 중#k"+enter;
      }

      if (item['price'] > 0)
        // msg += "#i5200002#"+item['price']+" 메소"+enter;
      msg +="#fs11#"+enter;
      msg += canMake ? "#r선택하신 아이템을 만들기 위한 재료들이 모두 모였습니다."+enter+"정말 제작하시려면 '예'를 눌러주세요." : "#r선택하신 아이템을 만들기 위한 재료들이 충분하지 않습니다.";

      if (canMake) cm.sendYesNo(msg);
      else {
         cm.sendOk(msg);
         cm.dispose();
      }
      
   } else if (status == 2) {
      canMake = checkItems(item);

      if (!canMake) {
         cm.sendOk("재료가 충분한지 다시 한 번 확인해보게");
         cm.dispose();
         return;
      }
      payItems(item);
      if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
         gainItem(item);
         cm.sendOk("#fs11#제작에 성공하였다네");
      } else {
         cm.sendOk("#fs11#제작에 실패했습니다.");
         gainFail(item);
      }
      cm.dispose();
   }
}
function checkItems(i) {
   recipe = i['recipes'];
   ret = true;

   for (j = 0; j < recipe.length; j++) {
      if (!cm.haveItem(recipe[j][0], recipe[j][1])) {
         //cm.getPlayer().dropMessage(6, "fas");
         ret = false;
         break;
      }
   }
   if (ret) ret = cm.getPlayer().getMeso() >= i['price'];

   return ret;
}

function payItems(i) {
   recipe = i['recipes'];
   for (j = 0; j < recipe.length; j++) {
      if (Math.floor(recipe[j][0] / 1000000) == 1)
         Packages.server.MapleInventoryManipulator.removeById(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, recipe[j][0], 1, false, false);
      else cm.gainItem(recipe[j][0], -recipe[j][1]);
           cm.gainMeso(-1000000);
   }
}

function gainItem(i) {
   ise = Math.floor(i['itemid'] / 1000000) == 1;
   if (ise) {
      vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i['itemid']);
      if (i['allstat'] > 0) {
         vitem.setStr(i['allstat']);
         vitem.setDex(i['allstat']);
         vitem.setInt(i['allstat']);
         vitem.setLuk(i['allstat']);
      }
      if (i['atk'] > 0) {
         vitem.setWatk(i['atk']);
         vitem.setMatk(i['atk']);
      }
      Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
   } else {
      cm.gainItem(i['itemid'], i['qty']);
   }
}
function gainFail(i) {
   fail = i['fail'];

   for (j = 0; j < fail.length; j++) {
      cm.gainItem(fail[j][0], fail[j][1]);
   }
}