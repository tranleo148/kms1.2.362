importPackage(Packages.tools.RandomStream);

var ii = Packages.server.MapleItemInformationProvider.getInstance();
var GameConstants = Packages.constants.GameConstants;
var MapleStat = Packages.client.MapleStat

var enter = '\r\n';
var reset = '#l#k';
var IS_DEBUGGING = false;
var MIN_SEARCHNAME_LENGTH = 2;
var status = -1;
var chat

var hairs = [];
var faces = [];
var searchedMolding = []

var firstSelection;

function start() {
    //if(!cm.getPlayer().isGM()) return;
    initializeMoldingData();

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

    chat = '#fs11#'

    if (status == 0) {
        chat += '안녕하세요! 저는 검색 성형/헤어를 관리해주고있습니다!' + enter
        chat += '#L0# 헤어 검색' + enter;
        chat += '#L1# 성형 검색'
        cm.sendSimple(chat)
    } else if (status == 1) {
        firstSelection = selection;
        chat += '찾으실 ' + (selection == 0 ? '헤어' : '성형') + '이름을 입력해주세요.'
        cm.sendGetText(chat);
    } else if (status == 2) {
        var searchTarget = cm.getText();
        var isHairId = firstSelection == 0;

        if (searchTarget.length >= MIN_SEARCHNAME_LENGTH) {
            if (isHairId) { //헤어일 때
                chat += '마음에 드는 헤어를 선택해 봐'
                searchedMolding = searchHair(searchTarget)
            } else {
                chat += '마음에 드는 얼굴을 선택해 봐'
                searchedMolding = searchFace(searchTarget)
            }

            if (searchedMolding.length > 0) {
                cm.sendStyle(chat, searchedMolding);
            } else {
                cm.sendOk('#fs11# 검색된 ' + (isHairId ? '헤어가' : '성형이') + ' 없습니다.');
            }
        } else {
            chat += '글자수가 너무 짧습니다.' + enter;
            chat += '최소 ' + MIN_SEARCHNAME_LENGTH + '글자 이상 입력해주세요'
            cm.sendOk(chat)
        }

    } else if (status == 3) {
	cm.getPlayer().dropMessage(5,""+searchedMolding[selection]);
        if (selection >= 0) {
            setAvatar(searchedMolding[selection]);
        }
        resetUserInput();
        status = -1;
        action(1, 0, 0);
    } else {
        resetUserInput()
        status = -1;
        action(1, 0, 0);
    }

}

function resetUserInput() {
    firstSelection = null;
    searchedMolding = [];
}

function searchHair(searchName) {
    var validHairIds = []
    for (var i = 0; i < hairs.length; i++) {
        var hair = hairs[i];
        var hairName = hair.right
        var hairId = hair.left
        if (hairId % 10 == 0) {
            if (hairName.indexOf(searchName) != -1) {
                validHairIds.push(hairId);
            } else if (hairName.indexOf(searchName[0]) != -1) {
                var index = 0;
                var isShortName = true;
                while (searchName[index] != null) {
                    var slicedSearchName = searchName[index];

                    if (hairName.indexOf(slicedSearchName) == -1) {
                        isShortName = false;
                        break;
                    }
                    index++;
                }
                if (isShortName) {
                    validHairIds.push(hairId);
                }
            }
        }

    }
    return validHairIds;
}

function searchFace(searchName) {
    var validFaceIds = []
    for (var i = 0; i < faces.length; i++) {
        var face = faces[i];
        var faceId = face.left;
        var faceName = face.right
        if (faceName.indexOf(searchName) != -1) {

            if (Math.floor(faceId / 100 % 10) == 0) {
                validFaceIds.push(faceId);
            }
        } else if (faceName.indexOf(searchName[0]) != -1) {
            var index = 0;
            var isShortName = true;
            while (searchName[index] != null) {
                var slicedSearchName = searchName[index];

                if (faceName.indexOf(slicedSearchName) == -1) {
                    isShortName = false;
                    break;
                }
                index++;
            }
            if (isShortName) {
                validFaceIds.push(faceId);
            }
        }
    }
    return validFaceIds;
}

function initializeMoldingData() {
    var it = ii.getAllEquips().iterator();
    while (it.hasNext()) {
        var avatar = it.next();
        var avatarId = avatar.getLeft()

        if (isHair(avatarId)) {
            hairs.push(avatar)
        } else if (isFace(avatarId)) {
            faces.push(avatar)
        }
    }
}

function setAvatar(args) {
    if (isHair(args)) {
        cm.setHair(args);
        //cm.getPlayer().setHair(args);
        //cm.getPlayer().updateSingleStat(MapleStat.HAIR, args);
    } else if (isFace(args)) {
        cm.setFace(args);
        //cm.getPlayer().setFace(args);
        //cm.getPlayer().updateSingleStat(MapleStat.FACE, args);
    }
    cm.getPlayer().equipChanged();
}

function isHair(itemId) {
    return Math.floor(itemId / 10000) == 3 || Math.floor(itemId / 10000) == 4 || Math.floor(itemId / 10000) == 6;
}

function isFace(itemId) {
    return Math.floor(itemId / 10000) == 2 || Math.floor(itemId / 10000) == 5;
}


////////////
function Color(a, r, g, b) {
    var hexcode = ''
    var alpha = 'FF';
    var red;
    var green;
    var blue;

    if (b != null) {
        alpha = formattedHex(a);
        red = formattedHex(r);
        green = formattedHex(g);
        blue = formattedHex(b);

    } else if (g != null) {
        red = formattedHex(a);
        green = formattedHex(r);
        blue = formattedHex(g);
    }

    if (red == null) {
        hexcode = a;
    } else {
        hexcode = alpha + red + green + blue
    }

    return '#fc0x' + hexcode + '#'
}

function formattedHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? '0' + hex : hex;
}

function FFColor(hexcode) {
    return Color('FF' + hexcode);
}

function formattedMeso(meso) {
    //억단위
    var upperMeso = Math.floor(meso / 100000000);
    var upperLeftMeso = meso % 100000000;
    //1억 5천이면 아래에 5천이 남아있음 
    //천만단위
    var lowerMeso = Math.floor(upperLeftMeso / 10000);
    var lowerLeftMeso = upperLeftMeso % 10000;

    var mesoString = '';

    if (upperMeso >= 1)
        mesoString += upperMeso + '억';
    if (lowerMeso > 0)
        mesoString += lowerMeso + '만';
    if (lowerLeftMeso > 0)
        mesoString += lowerLeftMeso;

    mesoString += '메소';


    return mesoString;
}

function newMap() {
    var map = {};
    map.value = {};
    map.getKey = function (id) {
        return 'k_' + id;
    };
    map.put = function (id, value) {
        var key = map.getKey(id);
        map.value[key] = value;
    };
    map.contains = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return true;
        } else {
            return false;
        }
    };
    map.get = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return map.value[key];
        }
        return null;
    };
    map.remove = function (id) {
        var key = map.getKey(id);
        if (map.contains(id)) {
            map.value[key] = undefined;
        }
    };

    map.getList = function () {
        return map.value;
    }

    return map;
}


function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}

function print(text) {
    java.lang.System.out.println(text)
}

function getMonsterImage(mobId) {
    return '#fMob/' + mobId + '.img/stand/0#'
}

function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ['', '만', '억', '조', '경'];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = '';

    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }

    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }

    return resultString;
}

function getDate() {
    var data = new Date();
    var month = data.getMonth() < 10 ? '0' + (data.getMonth() + 1) : (data.getMonth() + 1) + '';
    var day = data.getDate() < 10 ? '0' + data.getDate() : data.getDate() + ''
    var date = (data.getYear() + 1900) + '' + month + '' + day;
    return date;
}

function getJobNameById(job) {
    switch (job) {
        case 0:
            return "초보자";
        case 100:
            return "검사";
        case 110:
            return "파이터";
        case 111:
            return "크루세이더";
        case 112:
            return "히어로";
        case 120:
            return "페이지";
        case 121:
            return "나이트";
        case 122:
            return "팔라딘";
        case 130:
            return "스피어맨";
        case 131:
            return "버서커";
        case 132:
            return "다크나이트";
        case 200:
            return "마법사";
        case 210:
            return "위자드(불,독)";
        case 211:
            return "메이지(불,독)";
        case 212:
            return "아크메이지(불,독)";
        case 220:
            return "위자드(썬,콜)";
        case 221:
            return "메이지(썬,콜)";
        case 222:
            return "아크메이지(썬,콜)";
        case 230:
            return "클레릭";
        case 231:
            return "프리스트";
        case 232:
            return "비숍";
        case 300:
            return "아처";
        case 310:
            return "헌터";
        case 311:
            return "레인저";
        case 312:
            return "보우마스터";
        case 320:
            return "사수";
        case 321:
            return "저격수";
        case 322:
            return "신궁";
        case 400:
            return "로그";
        case 410:
            return "어쌔신";
        case 411:
            return "허밋";
        case 412:
            return "나이트로드";
        case 420:
            return "시프";
        case 421:
            return "시프마스터";
        case 422:
            return "섀도어";
        case 430:
            return "세미듀어러";
        case 431:
            return "듀어러";
        case 432:
            return "듀얼마스터";
        case 433:
            return "슬래셔";
        case 434:
            return "듀얼블레이더";
        case 500:
            return "해적";
        case 510:
            return "인파이터";
        case 511:
            return "버커니어";
        case 512:
            return "바이퍼";
        case 520:
            return "건슬링거";
        case 521:
            return "발키리";
        case 522:
            return "캡틴";
        case 800:
            return "매니저";
        case 900:
            return "운영자";
        case 1000:
            return "노블레스";
        case 1100:
        case 1110:
        case 1111:
        case 1112:
            return "소울마스터";
        case 1200:
        case 1210:
        case 1211:
        case 1212:
            return "플레임위자드";
        case 1300:
        case 1310:
        case 1311:
        case 1312:
            return "윈드브레이커";
        case 1400:
        case 1410:
        case 1411:
        case 1412:
            return "나이트워커";
        case 1500:
        case 1510:
        case 1511:
        case 1512:
            return "스트라이커";
        case 2000:
            return "레전드";
        case 2100:
        case 2110:
        case 2111:
        case 2112:
            return "아란";
        case 2001:
        case 2200:
        case 2210:
        case 2211:
        case 2212:
        case 2213:
        case 2214:
        case 2215:
        case 2216:
        case 2217:
        case 2218:
            return "에반";
        case 3000:
            return "시티즌";
        case 3200:
        case 3210:
        case 3211:
        case 3212:
            return "배틀메이지";
        case 3300:
        case 3310:
        case 3311:
        case 3312:
            return "와일드헌터";
        case 3500:
        case 3510:
        case 3511:
        case 3512:
            return "메카닉";
        case 501:
            return "해적(캐논슈터)";
        case 530:
            return "캐논슈터";
        case 531:
            return "캐논블래스터";
        case 532:
            return "캐논마스터";
        case 2002:
        case 2300:
        case 2310:
        case 2311:
        case 2312:
            return "메르세데스";
        case 3001:
        case 3100:
        case 3110:
        case 3111:
        case 3112:
            return "데몬슬레이어";
        case 2003:
        case 2400:
        case 2410:
        case 2411:
        case 2412:
            return "팬텀";
        case 2004:
        case 2700:
        case 2710:
        case 2711:
        case 2712:
            return "루미너스";
        case 5000:
        case 5100:
        case 5110:
        case 5111:
        case 5112:
            return "미하일";
        case 6000:
        case 6100:
        case 6110:
        case 6111:
        case 6112:
            return "카이저";
        case 6001:
        case 6500:
        case 6510:
        case 6511:
        case 6512:
            return "엔젤릭버스터";
        case 3101:
        case 3120:
        case 3121:
        case 3122:
            return "데몬어벤져";
        case 3002:
        case 3600:
        case 3610:
        case 3611:
        case 3612:
            return "제논";
        case 10000:
            return "제로JR";
        case 10100:
            return "제로10100";
        case 10110:
            return "제로10110";
        case 10111:
            return "제로10111";
        case 10112:
            return "제로";
        case 2005:
            return "???";
        case 2500:
        case 2510:
        case 2511:
        case 2512:
            return "은월";
        case 14000:
        case 14200:
        case 14210:
        case 14211:
        case 14212:
            return "키네시스";
        case 15000:
        case 15200:
        case 15210:
        case 15211:
        case 15212:
            return "일리움";
        case 15001:
        case 15500:
        case 15510:
        case 15511:
        case 15512:
            return "아크";

        case 16000:
        case 16400:
        case 16410:
        case 16411:
        case 16412:
            return "호영";

        case 15002:
        case 15100:
        case 15110:
        case 15111:
        case 15112:
            return "아델";

        default:
            return "알수없음";
    }
}

function getFamilarJobCode(jobCode) {
    var jobList = [];
    var jobCodeLength = jobCode.toString().length;
    var baseJobCode = Math.floor(jobCode / 100) * 100;
    var subJobCode = 1;
    var minorJobCode = 2;

    if (GameConstants.isDualBlade(jobCode)) {
        minorJobCode = 4;
    } else if (GameConstants.isEvan(jobCode)) {
        minorJobCode = 8;
    }

    if (jobCodeLength == 3) {
        subJobCode = parseInt(jobCode.toString()[1]);
    } else {
        jobList.push(baseJobCode);
    }

    jobList.push(baseJobCode + (subJobCode * 10));
    for (var i = 1; i <= minorJobCode; i++) {
        jobList.push(baseJobCode + (subJobCode * 10) + i);
    }

    return jobList;
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
    return null
}

function writeLog(fileName, contents, isAppend) {
    var file = new java.io.File(fileName);
    if (!file.exists()) {
        file.createNewFile();
    }

    var pw = new java.io.PrintWriter(new java.io.FileWriter(file, isAppend));

    pw.println(contents)

    pw.flush();
    pw.close();
}

function addStringByIndex(targetString, stringBeAdded, index) {
    return [targetString.slice(0, index), stringBeAdded, targetString.slice(index)].join('');
}

function removeStringByIndex(targetString, index) {
    return targetString.slice(0, index) + targetString.slice(index + 1);
}