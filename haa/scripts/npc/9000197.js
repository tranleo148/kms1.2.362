importPackage(Packages.constants);
importPackage(Packages.database);
importPackage(java.lang);

function ConvertNumber(number) { //모 블로그 참조함, 이 부분에 대해서는 호크아이(hawkeye888@nate.com) 에게 저작권이 없음
    var inputNumber  = number < 0 ? false : number;
    var unitWords    = ['', '만 ', '억 ', '조 ', '경 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length;
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n(파싱오류)");
        cm.dispose();
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}

var Time = new Date();
var Year = Time.getFullYear() + "";
var Month = Time.getMonth() + 1 + "";
var Date = Time.getDate() + "";
if (Month < 10) {
    Month = "0" + Month;    
}
if (Date < 10) {
    Date = "0" + Date;    
}
var Today = parseInt(Year + Month + Date);

var admin = 0;
var rewarddate = 0;
var rank = 0;
var characterid = 0;
var name = "";
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        /*cm.getPlayer().DamageMeterMap = 450002250;
        cm.getPlayer().DamageMeterMonster = 9300800;
        cm.getPlayer().DamageMeterTime = 30;
        cm.getPlayer().DamageMobX = 200;
        cm.getPlayer().DamageMobY = 100;
        cm.getPlayer().DamageMeterExitMap = 180000000;*/
        var say = "";
        if (cm.getPlayer().getGMLevel() > 5) {
            say += "\r\n\r\n\r\n   <관리자메뉴>\r\n#L4#랭킹초기화#l\r\n\r\n"//#L5#랭커 보상 지급하기#l;
        }
        cm.sendSimple("   데미지 측정을 시작하시겠습니까?\r\n" +
        "   이전 기록 데미지 : " + cm.getPlayer().DamageMeter + "\r\n" +
        "#L1#데미지 측정하기 (2분)#l\r\n#L2#오늘의 데미지 랭킹 보기#l\r\n#L3#이전 데미지 랭킹 보기#l" + say);
    } else if (status == 1) {
        if (selection == 1) {
            var em = cm.getEventManager("DamageMeter");
            if (em == null) {
                cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.");
                cm.dispose();
                return;
            } else if (em.getProperty("entry").equals("false")) {
                cm.sendOk("다른 유저가 현재 데미지 기록 중입니다. 1분후에 다시 시도해 주세요.");
                cm.dispose();
                return;
            } else {
                cm.getPlayer().DamageMeter = 0;
                em.startInstance(cm.getPlayer());
                cm.dispose();
            }
        } else if (selection == 2) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + Today + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                var say = "오늘의 데미지미터 랭킹입니다.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count +"위 - " + rs.getString("name") + "   데미지 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("아직 등록된 기록이 없거나 오류가 발생하였습니다.\r\n" + e);
                cm.dispose();
                return;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (e) {
        
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (e) {
        
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (e) {
        
                    }
                }
            }
        } else if (selection == 3) {
            admin = 0;
            cm.sendGetNumber("어떤 날의 랭킹을 보시겠습니까?\r\n날짜를 아래와 같이 입력해주세요.\r\n예) 20200101", 0, 20200101, 99999999);
        } else if (selection == 4 && cm.getPlayer().getGMLevel() > 5) {
            admin = 1;
            cm.sendYesNo("정말로 데미지미터 랭킹을 모두 초기화 하시겠습니까?\r\n모든 날짜의 기록이 삭제 됩니다!");
        } else if (selection == 5 && cm.getPlayer().getGMLevel() > 5) {
            admin = 2;
            cm.sendGetNumber("어떤 날의 랭커 보상을 지급하시겠습니까?\r\n날짜를 아래와 같이 입력해주세요.\r\n예) 20200101", 0, 20200101, 99999999);
        } else {
            cm.dispose();
            return;
        }
    } else if (status == 2) {
        if (admin == 0) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + selection + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                var say = selection.toString().substring(0,4) + "년 " + selection.toString().substring(4,6) + "월 " + selection.toString().substring(6,8) + "일의 데미지미터 랭킹입니다.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count +"위 - " + rs.getString("name") + "   데미지 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("아직 등록된 기록이 없거나 오류가 발생하였습니다.\r\n" + e);
                cm.dispose();
                return;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (e) {
        
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (e) {
        
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (e) {
        
                    }
                }
            }
        } else if (admin == 1 && cm.getPlayer().getGMLevel() > 5) {
            var con = null;
            var ps = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("DELETE FROM `DamageMeter`");
                ps.executeUpdate();
                ps.close();
                con.close();
                cm.sendOk("데미지미터 기록 초기화가 완료 되었습니다.");
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("오류가 발생하였습니다.\r\n" + e);
                cm.dispose();
                return;
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (e) {
        
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (e) {
        
                    }
                }
            }
        } else if (admin == 2 && cm.getPlayer().getGMLevel() > 5) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + selection + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                rewarddate = selection;
                var say = selection.toString().substring(0,4) + "년 " + selection.toString().substring(4,6) + "월 " + selection.toString().substring(6,8) + "일의 데미지미터 랭킹입니다.\r\n" +
                "해당 닉네임을 선택하시면 랭킹 보상 지급이 가능합니다.\r\n";
                while (rs.next()) {
                    count++;
                    say += "#L" + rs.getInt("id") + "#" + count +"위 - " + rs.getString("name") + "   데미지 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendSimple(say);
            } catch (e) {
                cm.sendOk("아직 등록된 기록이 없거나 오류가 발생하였습니다.\r\n" + e);
                cm.dispose();
                return;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (e) {
        
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (e) {
        
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (e) {
        
                    }
                }
            }
        } else {
            cm.dispose();
            return;
        }
    } else if (status == 3 && cm.getPlayer().getGMLevel() > 5) {
        var con = null;
        var ps = null;
        var rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + rewarddate + " ORDER BY `damage` DESC");
            rs = ps.executeQuery();
            var count = 0;
            var say = rewarddate.toString().substring(0,4) + "년 " + rewarddate.toString().substring(4,6) + "월 " + rewarddate.toString().substring(6,8) + "일의 ";
            while (rs.next()) {
                count++;
                if (rs.getInt("id") == selection) {
                    rank = count;
                    characterid = rs.getInt("characterid");
                    name = rs.getString("name");
                    say += rs.getString("name") + "유저의 데미지미터 랭킹입니다.\r\n\r\n";
                    say += count +"위 - 데미지 : " + ConvertNumber(rs.getLong("damage"));
                    say += "\r\n\r\n랭커 " + count + "등 보상을 지급하시겠습니까?";
                }
            }
            rs.close();
            ps.close();
            con.close();
            cm.sendYesNo(say);
        } catch (e) {
            cm.sendOk("선택된 유저가 없거나 오류가 발생하였습니다.\r\n" + e);
            cm.dispose();
            return;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (e) {
    
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (e) {
    
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (e) {
    
                }
            }
        }
    } else if (status == 4 && cm.getPlayer().getGMLevel() > 5) {
        //1등 2022424 20개 + 4001126 500개 + 4310024 5개
        //2등 2022424 10개 + 4001126 300개 + 4310024 3개
        //3등 2022424 5개 + 4001126 100개 + 4310024 1개
        var channel = Packages.handling.world.World.Find.findChannel(characterid);
        if (rank == 1) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 20, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 500, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 5, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
        } else if (rank == 2) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 10, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 300, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 3, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
        } else if (rank == 3) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 5, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 100, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 1, characterid, "[데미지미터]", "데미지미터 " + rank + "등 보상", channel >= 0);
        } else {
            cm.sendOk("1~3등 밖에 있는 유저이므로 보상이 없습니다.");
            cm.dispose();
            return;
        }
        if (channel >= 0) {
            Packages.handling.world.World.Broadcast.sendPacket(characterid, Packages.tools.MaplePacketCreator.sendDuey(28, null, null));
            Packages.handling.world.World.Broadcast.sendPacket(characterid, Packages.tools.MaplePacketCreator.serverNotice(2, "[시스템] : 데미지미터 " + rank + "등 보상이 택배로 지급되었습니다."));
        }
        cm.sendOk(name + "유저에게 " + rank + "등 보상을 지급하였습니다.");
        cm.dispose();
        return;
    } else {
        cm.dispose();
        return;
    }
}