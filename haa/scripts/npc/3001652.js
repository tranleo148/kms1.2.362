importPackage(java.lang);
var enter = "\r\n";
var seld = -1;

var limit = 5;

var needs = [[4034939, 30], [4034941, 30]];
var special_needs = [[4031701, 50]];


var reward = [[4009005, 100, 200], [4310266, 50, 120], [2430041, 1, 1], [2435719, 10, 50], [2049360, 2, 3], [4031701, 5, 7], [2048717, 30, 50], [5060048, 1, 3], [4001716, 5, 10], [2431940, 1, 2], [2049153, 3, 5]];

var special_reward = 1112164;
var special_allstat = 200;
var special_atk = 100;

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
        var msg ="#fs 11##b시원하게 여름을 보낼 재료가 필요해!#k\r\n모든 사냥터에서 드롭되는#b#i4034939#30개, #i4034941#30개#k를 모아다 주면 특별한 보상을 줄게! 계정당 하루에 3번만 교환 가능하니 유의 하라구!#b"+enter;
        msg += "#L1#아이템 교환"+enter;
        msg += "#L2#이벤트 아이템 교환"+enter;

        cm.sendSimple(msg);
    } else if (status == 1) {
        
        if (cm.getClient().getKeyValue("day_qitem") == null)
            cm.getClient().setKeyValue("day_qitem", 0);

        if (Integer.parseInt(cm.getClient().getKeyValue("day_qitem")) >= limit) {
            cm.sendOk("오늘은 더 이상 교환할 수 없어! 내일 다시 찾아와줘");
            cm.dispose();
            return;
        }
        seld = sel;

        switch (seld) {
            case 1:
                var msg ="#fs11##b#i4034939#30개, #i4034941#30개#k를 모아오면 특별한 선물을 줄게~#b"+enter+enter;
                    msg +="#i4034939#  #c4034939# / 30\r\n#i4034941#  #c4034941# / 30"+enter;
        
                cm.sendNext(msg);
                break;
            case 2:
                var msg = "#fs11#아래의 아이템을 모아오면 특별한 아이템을 줄게!#b"+enter+enter;
                for (i = 0; i < special_needs.length; i++) {
                    msg +="#i4031701#  #c4031701# / 50\r\n"+enter;
                msg += "\r\n#b이벤트 보상#k\r\n#b#i"+special_reward+"##z"+special_reward+"# 올스탯 "+special_allstat+" | 공마 "+special_atk+enter;
                    msg +="#i4001716##z4001716# 30~50개#k"+enter;
                }
        
                cm.sendNext(msg);
                break;
        }
    } else if (status == 2) {
        var pass = true;
        switch (seld) {
            case 1:
                for (i = 0; i < needs.length; i++) {
                    if (!cm.haveItem(needs[i][0], needs[i][1])) {
                        pass = false;
                        break;
                    }
                }
        
                if (!pass) {
                    cm.sendOk("#fs11#아이템을 전부 모아오지 못한 것 같은데?");
                    cm.dispose();
                    return;
                }

                var msg = "#fs11#재료를 다 모아왔구나!?";
                cm.sendNext(msg);
                break;
            case 2:
                for (i = 0; i < special_needs.length; i++) {
                    if (!cm.haveItem(special_needs[i][0], special_needs[i][1])) {
                        pass = false;
                        break;
                    }
                }
        
                if (!pass) {
                    cm.sendOk("#fs11#아이템을 전부 모아오지 못한 것 같은데?");
                    cm.dispose();
                    return;
                }

                var msg = "#fs11#이번 이벤트의 특별한 아이템이야! 어때!?#b"+enter+enter;

                msg += "#L1##i"+special_reward+"##z"+special_reward+"# 올스탯 "+special_allstat+" | 공마 "+special_atk+enter;
                msg += "#L2##i4001716##z4001716# 30 ~ 50개"+enter;
                cm.sendSimple(msg);
                break;
        }
    } else if (status == 3) {
        switch (seld) {
            case 1:
                var msg ="#fs11#모아온 재료들을 주면 아래 아이템 목록 중 하나를 랜덤으로 줄게 어때?#b"+enter+enter;

                for (i = 0; i < reward.length; i++) {
         if (reward[i][0] == -5)
            msg += "#i4001716##z4001716# "+reward[i][1]+" ~ "+reward[i][2]+"개"+enter;
         else
                          msg += "#i"+reward[i][0]+"##z"+reward[i][0]+"# "+reward[i][1]+" ~ "+reward[i][2]+"개"+enter;
                }
                msg += enter+"#k정말 교환할거야? 오늘은 #b"+(limit - Integer.parseInt(cm.getClient().getKeyValue("day_qitem")))+"번#k 더 교환할 수 있어.";
                cm.sendYesNo(msg);
                break;
            case 2:
      seld2 = sel;
                var msg = "#fs11#어때 정말 멋진 아이템이지? 이번 이벤트가 끝나면 얻을 수 없으니 운 좋은줄 알라구!#b"+enter+enter;
      if (sel == 1)
         msg += "#i"+special_reward+"##z"+special_reward+"# 올스탯 "+special_allstat+" | 공마 "+special_atk;

                msg += enter+"#k정말 교환할거야? 오늘은 #b"+(limit - Integer.parseInt(cm.getClient().getKeyValue("day_qitem")))+"번#k 더 교환할 수 있어.";
                cm.sendYesNo(msg);
                break;
        }

    } else if (status == 4) {
        switch (seld) {
            case 1:
                a = Packages.server.Randomizer.rand(0, reward.length - 1);
                b = Packages.server.Randomizer.rand(reward[a][1], reward[a][2]);

                if (reward[a][0] != -5 && !cm.canHold(reward[a][0], b)) {
                    cm.sendOk("인벤토리에 공간이 있는지 확인해봐.");
                    cm.dispose();
                    return;
                }
                for (i = 0; i < needs.length; i++) {
                    cm.gainItem(needs[i][0], -needs[i][1]);
                }

                var msg = "교환이 완료되었어.";
      if (reward[a][0] == -5) {
         msg = "#i4001716##z4001716# #b"+b+"개#k 나왔어!";
      } else
                   cm.gainItem(reward[a][0], b);
                break;
            case 2:
                if (!cm.canHold(special_reward, 1) && seld == 1) {
                    cm.sendOk("인벤토리에 공간이 있는지 확인해줘.");
                    cm.dispose();
                    return;
                }
                for (i = 0; i < special_needs.length; i++) {
                    cm.gainItem(special_needs[i][0], -special_needs[i][1]);
                }
      if (seld2 == 1) {
         var msg = "교환이 완료되었어.";
                   gainItemS(special_reward, special_allstat, special_atk);
      } else if (seld2 == 2) {
                   b = Packages.server.Randomizer.rand(30, 50);
                   cm.gainItem(4001716, b);
         msg = "#i4001716##z4001716#가 #b"+b+"개#k 나왔어!";
      }
         
                break;
        }
        cm.getClient().setKeyValue("day_qitem", (Integer.parseInt(cm.getClient().getKeyValue("day_qitem")) + 1));
        cm.sendOk(msg);
        cm.dispose();
    }
}
function getData() {
   time = new Date();
   year = time.getFullYear();
   month = time.getMonth() + 1;
   if (month < 10) {
      month = "0"+month;
   }
   date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
   date = year+"."+month+"."+date2;
   day = time.getDay();
}
function gainItemS(id, as, atk) {
   item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(id);
   if (as > -1) {
      item.setStr(as);
      item.setDex(as);
      item.setInt(as);
      item.setLuk(as);
   }
   if (atk > -1) {
      item.setWatk(atk);
      item.setMatk(atk);
   }
   Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}