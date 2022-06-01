

var JavaString = Packages.java.lang.String;
var Integer = Packages.java.lang.Integer;
var ii = Packages.server.MapleItemInformationProvider.getInstance();
var GameConstants = Packages.constants.GameConstants;
var MapleInventoryType = Packages.client.inventory.MapleInventoryType;
var Packet = Packages.tools.packet;
var InventoryPacket = Packet.CWvsContext.InventoryPacket;

var ItemOption = {
    STR: 0,
    DEX: 1,
    INT: 2,
    LUK: 3,
    HP: 4,
    MP: 5,
    ARMOR: 6,
    WATTACK: 7,
    MATTACK: 8,
    MOVEMENT: 9,
    JUMP: 10,
    PERCENT_BOSS_ATTACK: 11,
    LEVEL_DOWN: 12,
    PERCENT_TOTAL_ATTACK: 13,
    PERCENT_ALLSTAT: 14,
};

var FireOption = {
    STR: 0,
    DEX: 1,
    INT: 2,
    LUK: 3,
    STR_DEX: 4,
    STR_INT: 5,
    STR_LUK: 6,
    DEX_INT: 7,
    DEX_LUK: 8,
    INT_LUK: 9,
    HP: 10,
    MP: 11,
    ARMOR: 13,
    WATTACK: 17,
    MATTACK: 18,
    MOVEMENT: 19,
    JUMP: 20,
    PERCENT_BOSS_ATTACK: 21,
    LEVEL_DOWN: 22,
    PERCENT_TOTAL_ATTACK: 23,
    PERCENT_ALLSTAT: 24,
};

var FireStats = function () {
    this.fireStats = [];

    this.addStat = function (fireStat) {
        this.fireStats.push(fireStat);
    };

    this.getStats = function () {
        return this.fireStats;
    };

    this.getStat = function (fireOption) {
        for (var index in this.fireStats) {
            var fireStat = this.fireStats[index];
            var currentOption = fireStat.option;
            if (currentOption == fireOption) {
                return fireStat;
            }
        }
        return null;
    };

    this.getCalculratedValue = function (fireOption) {
        var fireStat = this.getStat(fireOption);
        if (fireStat != null) {
            return fireStat.calculratedValue;
        }
        return 0;
    };
};

var FireStat = function (option, value, calculratedValue) {
    this.option = option;
    this.value = value;
    this.calculratedValue = calculratedValue;
};

var SCRIPT_TITLE = "#e#b<원클릭 환생의 불꽃>#n#k";
var enter = "\r\n";
var ALLOWED_REBIRTH_FIRE_ITEM_ID = [2048717];
var SELECTION_EXIT = 1000;
var SELECTION_APPLY = 1001;
var status = -1;
var chat;
var selectedEquipments;
var selectedEquipment;
var selectedRebirthFireItemId;

function start() {
    //if(!cm.getPlayer().isGM()) return;
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    chat = "";
    chat += "#fs11#";
    chat += SCRIPT_TITLE + enter + enter;
    if (status == 0) {
        chat += "#b메이플스토리 편리한 원클릭 환불 시스템" + enter;
        chat += "#r사용하실 환생의 불꽃을 골라주세요." + enter;
        chat += getRebirthFireSelections();
        cm.sendSimple(chat);
    } else if (status == 1) {
        if (selection >= 0 && selection < ALLOWED_REBIRTH_FIRE_ITEM_ID) {
            selectedRebirthFireItemId = ALLOWED_REBIRTH_FIRE_ITEM_ID[selection];
        }

        selectedEquipments = getEquipItems(function (item) {
            print(item.getFire());
            if (item.getFire() > 0) {
                return true;
            }
            return false;
        });

        if (selectedEquipments.length > 0) {
            chat += "환생의 불꽃을 적용하실 장비를 골라주세요" + enter;
            for (var index in selectedEquipments) {
                var itemId = selectedEquipments[index].getItemId();
                chat += "#L" + index + "#";
                chat += "#v" + itemId + "#";
                chat += "#z" + itemId + "#";
                chat += enter;
            }
        } else {
            chat += "강화하실 수 있는 아이템이 없습니다";
            cm.dispose();
        }
        cm.sendOk(chat);
    } else if (status == 2) {
        if (selectedEquipment == null) {
            selectedEquipment = selectedEquipments[selection];
        }

        if (selectedEquipment != null && selectedRebirthFireItemId != null) {
            if (checkCost(selectedRebirthFireItemId)) {
                decreaseCost(selectedRebirthFireItemId);
                applyRebirthFire(selectedEquipment, selectedRebirthFireItemId);

                chat += getRebirthFireAppliedItemDescription(selectedEquipment) + enter;
                chat += "#L" + SELECTION_APPLY + "#다시 사용하기" + enter;
                chat += "#L" + SELECTION_EXIT + "#그만 사용하기" + enter;
            } else {
                chat += "#v" + selectedRebirthFireItemId + "#";
                chat += "#z" + selectedRebirthFireItemId + "#";
                chat += "이 부족합니다.";
                cm.dispose();
            }
        } else {
            cm.dispose();
        }
        cm.sendOk(chat);
    } else if (status == 3) {
        switch (selection) {
            case SELECTION_APPLY:
                status = 1;
                action(1, 0, 0);
                break;
            case SELECTION_EXIT:
              //  cm.getPlayer().reloadChar();
                cm.dispose();
                break;
            default:
                break;
        }
        return;
    }
}

function getRebirthFireSelections() {
    var selections = "";
    for (var index in ALLOWED_REBIRTH_FIRE_ITEM_ID) {
        var itemId = ALLOWED_REBIRTH_FIRE_ITEM_ID[index];
        selections += "#L" + index + "#";
        selections += "#v" + itemId + "#";
        selections += "#z" + itemId + "#";
        selections += enter;
    }
    return selections;
}

function getRebirthFireAppliedItemDescription(item) {
    var description = "";
    var original = ii.getEquipById(item.getItemId(), false);
    var fireStats = parsingRebirthFire(item);
    for (var index in ItemOption) {
        var currentItemOption = ItemOption[index];
        var calculratedValue = 0;
        switch (currentItemOption) {
            case ItemOption.STR:
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_DEX);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_INT);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_LUK);
                break;
            case ItemOption.DEX:
                calculratedValue += fireStats.getCalculratedValue(FireOption.DEX);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_DEX);
                calculratedValue += fireStats.getCalculratedValue(FireOption.DEX_INT);
                calculratedValue += fireStats.getCalculratedValue(FireOption.DEX_LUK);
                break;
            case ItemOption.INT:
                calculratedValue += fireStats.getCalculratedValue(FireOption.INT);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_INT);
                calculratedValue += fireStats.getCalculratedValue(FireOption.DEX_INT);
                calculratedValue += fireStats.getCalculratedValue(FireOption.INT_LUK);
                break;
            case ItemOption.LUK:
                calculratedValue += fireStats.getCalculratedValue(FireOption.LUK);
                calculratedValue += fireStats.getCalculratedValue(FireOption.STR_LUK);
                calculratedValue += fireStats.getCalculratedValue(FireOption.DEX_LUK);
                calculratedValue += fireStats.getCalculratedValue(FireOption.INT_LUK);
                break;
            default:
                var currentFireOption = convertItemOptionToFireOption(currentItemOption);
                calculratedValue += fireStats.getCalculratedValue(currentFireOption);
                break;
        }
        if (calculratedValue > 0) {
            var originalValue = getItemOption(original, currentItemOption);
            var itemValue = getItemOption(item, currentItemOption);
            var enhancedValue = itemValue - originalValue - calculratedValue;
            var isPercent = isPercentItemOption(currentItemOption);
            var isMinusValue = isMinusValueOption(currentItemOption);
            calculratedValue *= isMinusValue ? -1 : 1;

            description += getItemOptionName(currentItemOption) + ": ";
            if (!isMinusValue) {
                description += "+";
            }
            description += itemValue;

            if (isPercent) {
                description += "%";
            }

            description += " (";
            description += originalValue;
            description += "#fc0xFF7A9900# ";

            if (!isMinusValue) {
                description += "+";
            }

            description += calculratedValue;
            if (isPercent) {
                description += "%";
            }
            description += "#k";
            if (enhancedValue > 0) {
                description += "#fc0xFF00B3B3#";
                description += " +" + enhancedValue;
                if (isPercent) {
                    description += "%";
                }
                description += "#k";
            }
            description += ")";

            description += enter;
        }
    }
    return description;
}

function getCalculratedValue(option, value, itemId) {
    var ordinary = ii.getEquipById(itemId, false);
    var ordinaryPad = ordinary.watk > 0 ? ordinary.watk : ordinary.matk;
    var ordinaryMad = ordinary.matk > 0 ? ordinary.matk : ordinary.watk;
    var reqLevel = ii.getReqLevel(itemId);
    var calculratedValue;
    switch (option) {
        case FireOption.STR:
        case FireOption.DEX:
        case FireOption.INT:
        case FireOption.LUK:
            calculratedValue = (reqLevel / 20 + 1) * value;
            break;
        case FireOption.STR_DEX:
        case FireOption.STR_INT:
        case FireOption.STR_LUK:
        case FireOption.DEX_INT:
        case FireOption.DEX_LUK:
        case FireOption.INT_LUK:
            calculratedValue = (reqLevel / 40 + 1) * value;
            break;
        case FireOption.HP:
        case FireOption.MP:
            calculratedValue = reqLevel * 3 * value;
            break;
        case FireOption.ARMOR:
            calculratedValue = (reqLevel / 20 + 1) * value;
            break;
        case FireOption.WATTACK:
            if (GameConstants.isWeapon(itemId)) {
                switch (value) {
                    case 3:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryPad * 1200) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryPad * 1500) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryPad * 1800) / 10000 + 1;
                        }
                        break;
                    case 4:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryPad * 1760) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryPad * 2200) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryPad * 2640) / 10000 + 1;
                        }
                        break;
                    case 5:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryPad * 2420) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryPad * 3025) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryPad * 3630) / 10000 + 1;
                        }
                        break;
                    case 6:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryPad * 3200) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryPad * 4000) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryPad * 4800) / 10000 + 1;
                        }
                        break;
                    case 7:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryPad * 4100) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryPad * 5125) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryPad * 6150) / 10000 + 1;
                        }
                        break;
                }
            } else {
                calculratedValue = value;
            }
            break;
        case FireOption.MATTACK:
            if (GameConstants.isWeapon(itemId)) {
                switch (value) {
                    case 3:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryMad * 1200) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryMad * 1500) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryMad * 1800) / 10000 + 1;
                        }
                        break;
                    case 4:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryMad * 1760) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryMad * 2200) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryMad * 2640) / 10000 + 1;
                        }
                        break;
                    case 5:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryMad * 2420) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryMad * 3025) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryMad * 3630) / 10000 + 1;
                        }
                        break;
                    case 6:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryMad * 3200) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryMad * 4000) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryMad * 4800) / 10000 + 1;
                        }
                        break;
                    case 7:
                        if (reqLevel <= 150) {
                            calculratedValue = (ordinaryMad * 4100) / 10000 + 1;
                        } else if (reqLevel <= 160) {
                            calculratedValue = (ordinaryMad * 5125) / 10000 + 1;
                        } else {
                            calculratedValue = (ordinaryMad * 6150) / 10000 + 1;
                        }
                        break;
                }
            } else {
                calculratedValue = value;
            }
            break;
        case FireOption.MOVEMENT:
        case FireOption.JUMP:
        case FireOption.PERCENT_TOTAL_ATTACK:
        case FireOption.PERCENT_ALLSTAT:
            calculratedValue = value;
            break;

        case FireOption.PERCENT_BOSS_ATTACK:
            calculratedValue = value * 2;
            break;
        case FireOption.LEVEL_DOWN:
            calculratedValue = 5 * value;
            break;
    }
    return Math.floor(calculratedValue);
}

function applyRebirthFire(item, rebirthFireItemId) {
    item.resetRebirth(ii.getReqLevel(item.getItemId()));
    item.setFire(item.newRebirth(ii.getReqLevel(item.getItemId()), rebirthFireItemId, true));
    //item.setRebirth(ii.getReqLevel(item.getItemId()), rebirthFireItemId);
    cm.getClient().getSession().writeAndFlush(InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item));
    cm.getClient().getSession().writeAndFlush(InventoryPacket.updateInventorySlot(MapleInventoryType.EQUIP, item, false));
}

function parsingRebirthFire(item) {
    var rebirthFireString = JavaString.valueOf(item.getFire());
    var rebirthFireLength = rebirthFireString.length();
    var rebirthFireStats = new FireStats();
    var rebirthFireNumberArray = [];

    if (rebirthFireLength == 12) {
        rebirthFireNumberArray[3] = Integer.parseInt(rebirthFireString.substring(0, 3));
        rebirthFireNumberArray[2] = Integer.parseInt(rebirthFireString.substring(3, 6));
        rebirthFireNumberArray[1] = Integer.parseInt(rebirthFireString.substring(6, 9));
        rebirthFireNumberArray[0] = Integer.parseInt(rebirthFireString.substring(9));
    } else if (rebirthFireLength == 11) {
        rebirthFireNumberArray[3] = Integer.parseInt(rebirthFireString.substring(0, 2));
        rebirthFireNumberArray[2] = Integer.parseInt(rebirthFireString.substring(2, 5));
        rebirthFireNumberArray[1] = Integer.parseInt(rebirthFireString.substring(5, 8));
        rebirthFireNumberArray[0] = Integer.parseInt(rebirthFireString.substring(8));
    } else if (rebirthFireLength == 10) {
        rebirthFireNumberArray[3] = Integer.parseInt(rebirthFireString.substring(0, 1));
        rebirthFireNumberArray[2] = Integer.parseInt(rebirthFireString.substring(1, 4));
        rebirthFireNumberArray[1] = Integer.parseInt(rebirthFireString.substring(4, 7));
        rebirthFireNumberArray[0] = Integer.parseInt(rebirthFireString.substring(7));
    }

    for (var i = 0; i < 4; i++) {
        var option = Math.floor(rebirthFireNumberArray[i] / 10);
        var value = rebirthFireNumberArray[i] - Number(Math.floor(rebirthFireNumberArray[i] / 10) * 10);
        rebirthFireStats.addStat(new FireStat(option, value, getCalculratedValue(option, value, item.getItemId())));
    }

    return rebirthFireStats;
}

function convertItemOptionToFireOption(itemOption) {
    for (var index in ItemOption) {
        if (ItemOption[index] == itemOption) {
            return FireOption[String(index)];
        }
    }
    return null;
}

function isPercentItemOption(itemOption) {
    switch (itemOption) {
        case ItemOption.PERCENT_ALLSTAT:
        case ItemOption.PERCENT_BOSS_ATTACK:
        case ItemOption.PERCENT_TOTAL_ATTACK:
            return true;
    }
    return false;
}

function isMinusValueOption(itemOption) {
    return itemOption == ItemOption.LEVEL_DOWN;
}

function getItemOption(item, itemOption) {
    switch (itemOption) {
        case ItemOption.STR:
            return item.getStr();
        case ItemOption.DEX:
            return item.getDex();
        case ItemOption.INT:
            return item.getInt();
        case ItemOption.LUK:
            return item.getLuk();
        case ItemOption.HP:
            return item.getHp();
        case ItemOption.MP:
            return item.getMp();
        case ItemOption.ARMOR:
            return item.getWdef();
        case ItemOption.WATTACK:
            return item.getWatk();
        case ItemOption.MATTACK:
            return item.getMatk();
        case ItemOption.MOVEMENT:
            return item.getSpeed();
        case ItemOption.JUMP:
            return item.getJump();
        case ItemOption.PERCENT_BOSS_ATTACK:
            return item.getBossDamage();
        case ItemOption.LEVEL_DOWN:
            return item.getReqLevel();
        case ItemOption.PERCENT_TOTAL_ATTACK:
            return item.getTotalDamage();
        case ItemOption.PERCENT_ALLSTAT:
            return item.getAllStat();
    }
}

function getItemOptionName(itemOption) {
    switch (itemOption) {
        case ItemOption.STR:
            return "힘";
        case ItemOption.DEX:
            return "덱스";
        case ItemOption.INT:
            return "인트";
        case ItemOption.LUK:
            return "럭";
        case ItemOption.HP:
            return "체력";
        case ItemOption.MP:
            return "마나";
        case ItemOption.ARMOR:
            return "방어력";
        case ItemOption.WATTACK:
            return "공격력";
        case ItemOption.MATTACK:
            return "마력";
        case ItemOption.MOVEMENT:
            return "이동속도";
        case ItemOption.JUMP:
            return "점프력";
        case ItemOption.PERCENT_BOSS_ATTACK:
            return "보스공격력";
        case ItemOption.LEVEL_DOWN:
            return "착용레벨 감소";
        case ItemOption.PERCENT_TOTAL_ATTACK:
            return "데미지";
        case ItemOption.PERCENT_ALLSTAT:
            return "올스텟";
    }
}

function checkCost(itemId) {
    return cm.haveItem(itemId);
}

function decreaseCost(itemId) {
    if (checkCost(itemId)) {
        cm.gainItem(itemId, -1);
    }
}

////////////
function formattedMeso(meso) {
    //억단위
    var upperMeso = Math.floor(meso / 100000000);
    var upperLeftMeso = meso % 100000000;
    //1억 5천이면 아래에 5천이 남아있음
    //천만단위
    var lowerMeso = Math.floor(upperLeftMeso / 10000);
    var lowerLeftMeso = upperLeftMeso % 10000;

    var mesoString = "";

    if (upperMeso >= 1) mesoString += upperMeso + "억";
    if (lowerMeso > 0) mesoString += lowerMeso + "만";
    if (lowerLeftMeso > 0) mesoString += lowerLeftMeso;

    mesoString += " 메소";

    return mesoString;
}

function print(text) {
    var header = cm.getScript() != null ? cm.getScript() : cm.getNpc();
    java.lang.System.out.println("[" + header + "] " + text);
}

function getMonsterImage(mobId) {
    return "#fMob/" + mobId + ".img/stand/0#";
}

function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ["", "만", "억", "조", "경"];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = "";

    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }

    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = JavaString(resultArray[i]) + unitWords[i] + resultString;
    }

    return resultString;
}

function getDate() {
    var data = new Date();
    var month = data.getMonth() < 10 ? "0" + (data.getMonth() + 1) : data.getMonth() + 1 + "";
    var day = data.getDate() < 10 ? "0" + data.getDate() : data.getDate() + "";
    var date = data.getYear() + 1900 + "" + month + "" + day;
    return date;
}

function findPlayerByName(userName) {
    var World = Packages.handling.world.World;
    var ChannelServer = Packages.handling.channel.ChannelServer;
    if (World.Find.findChannel(userName) >= 0) {
        var player = ChannelServer.getInstance(World.Find.findChannel(userName)).getPlayerStorage().getCharacterByName(userName);
        if (player != null) {
            return player;
        }
    }
    return null;
}

function writeLog(fileName, contents, isAppend) {
    var file = new java.io.File(fileName);
    if (!file.exists()) {
        file.createNewFile();
    }

    var pw = new java.io.PrintWriter(new java.io.FileWriter(file, isAppend));

    pw.println(contents);

    pw.flush();
    pw.close();
}

function addStringByIndex(targetString, stringBeAdded, index) {
    return [targetString.slice(0, index), stringBeAdded, targetString.slice(index)].join("");
}

function removeStringByIndex(targetString, index) {
    return targetString.slice(0, index) + targetString.slice(index + 1);
}

function getItemsCatalog(itemList) {
    var chat = "";
    for (var i = 0; i < itemList.length; i++) {
        var currentItem = itemList[i];
        chat += "#L" + i + "#";
        chat += "#v" + currentItem.getItemId() + "#";
        chat += "#z" + currentItem.getItemId() + "#";
        chat += enter;
    }
    return chat;
}

function getInventoryItems(inventortType, composer) {
    var inventory = cm.getPlayer().getInventory(inventortType);
    var items = [];
    for (var i = 0; i < inventory.getSlotLimit(); i++) {
        var currentItem = inventory.getItem(i);
        if (currentItem != null) {
            if (composer(currentItem)) {
                items.push(currentItem);
            }
        }
    }
    return items;
}

function getEquipItems(composer) {
    return getInventoryItems(MapleInventoryType.EQUIP, composer);
}

function unpackingRewardString(rewardString) {
    var splittedRewardString = rewardString.split(";");
    var rewardItems = [];
    for (var i in splittedRewardString) {
        rewardItems.push({
            itemId: splittedRewardString[i].split(",")[0],
            quantity: splittedRewardString[i].split(",")[1],
        });
    }
    return rewardItems;
}

function packingRewardItems(rewardItems) {
    var rewardString = "";
    for (var i in rewardItems) {
        rewardString += rewardItems[i].itemId + "," + rewardItems[i].quantity;

        if (rewardItems.length < i) {
            rewardString += ";";
        }
    }
    return rewardString;
}

function log10(x) {
    return Math.floor(Math.log(x) / Math.log(10));
}

function gainMeso(meso) {
    var MAX_VALUE = Packages.java.lang.Integer.MAX_VALUE;
    if (Math.abs(meso) > MAX_VALUE) {
        var count = Math.floor(Math.abs(meso) / MAX_VALUE);
        var left = Math.floor(Math.abs(meso) % MAX_VALUE);
        for (var i = 1; i <= count; i++) {
            cm.gainMeso(MAX_VALUE * (meso > 0 ? 1 : -1));
        }
        if (let > 0) {
            cm.gainMeso(left * (meso > 0 ? 1 : -1));
        }
    } else {
        cm.gainMeso(meso);
    }
}

function gainItem(itemId, quantity) {
    var MAX_VALUE = Packages.java.lang.Short.MAX_VALUE;
    var isIncrement = quantity > 0;
    if (Math.abs(quantity) > MAX_VALUE) {
        var count = Math.floor(Math.abs(quantity) / MAX_VALUE);
        var left = Math.floor(Math.abs(quantity) % MAX_VALUE);
        for (var i = 1; i <= count; i++) {
            if (isIncrement) {
                cm.getPlayer().gainItem(itemId, MAX_VALUE);
            } else {
                cm.getPlayer().removeItem(itemId, MAX_VALUE);
            }
        }

        if (left > 0) {
            if (isIncrement) {
                cm.getPlayer().gainItem(itemId, left);
            } else {
                cm.getPlayer().removeItem(itemId, left);
            }
        }
    } else {
        if (isIncrement) {
            cm.getPlayer().gainItem(itemId, quantity);
        } else {
            cm.getPlayer().removeItem(itemId, quantity);
        }
    }
}

function addStarDustCoin(quantity) {
    var MAX_VALUE = Packages.java.lang.Integer.MAX_VALUE;

    if (Math.abs(quantity) > MAX_VALUE) {
        var count = Math.floor(Math.abs(quantity) / MAX_VALUE);
        var left = Math.floor(Math.abs(quantity) % MAX_VALUE);
        for (var i = 1; i <= count; i++) {
            cm.getPlayer().addStarDustCoin(MAX_VALUE * (quantity > 0 ? 1 : -1));
        }
        if (left > 0) {
            cm.getPlayer().addStarDustCoin(left * (quantity > 0 ? 1 : -1));
        }
    } else {
        cm.getPlayer().addStarDustCoin(quantity);
    }
}

function sliceValueWork(quantity, maxValue, worker) {
    var MAX_VALUE = maxValue;

    if (Math.abs(quantity) > MAX_VALUE) {
        var count = Math.floor(Math.abs(quantity) / MAX_VALUE);
        var left = Math.floor(Math.abs(quantity) % MAX_VALUE);
        for (var i = 1; i <= count; i++) {
            worker(MAX_VALUE * (quantity > 0 ? 1 : -1));
        }
        if (left > 0) {
            worker(left * (quantity > 0 ? 1 : -1));
        }
    } else {
        worker(quantity);
    }
}

function getConsumeItemId() {
    return parseInt(cm.getScript().split("_")[1]);
}
function dateStringToDate(dateString) {
    var year = parseInt(dateString.substring(0, 4));
    var month = parseInt(dateString.substring(5, 7)) - 1;
    var day = parseInt(dateString.substring(8, 10));
    return new Date(year, month, day);
}

function comma(x) {
    var commaNumber = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return commaNumber.split(",,").join(",");
}
