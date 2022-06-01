 //Maked By 키네시스, 라피스 (네이트온: Kinesis8@nate.com , 디스코드 : 라피스#2519)

 importPackage(Packages.database);
 importPackage(java.lang);
 
 var time, Year, Month, Day, Today, Hour, Minute, NowTime;
 
 function UpdateTime() { //매 status 마다 시간 동기화
	time = System.currentTimeMillis();
	NowTime = new java.text.SimpleDateFormat("YYYY년 M월 d일 HH시 m분 s초").format(time);
	Year = new java.text.SimpleDateFormat("YYYY").format(time);
	Month = new java.text.SimpleDateFormat("MM").format(time);
	Day = new java.text.SimpleDateFormat("dd").format(time);
	Hour = new java.text.SimpleDateFormat("HH").format(time);
	Minute = new java.text.SimpleDateFormat("mm").format(time);
	Today = Integer.parseInt(Year + "" + Month + "" + Day);
 }
 
 function getCount(Today) {
	var count = 0;
	var con = null;
	var ps = null;
	var rs = null;
	try {
		//회차 확인하기
		con = DatabaseConnection.getConnection();
		ps = con.prepareStatement("SELECT * FROM `bettingresult` WHERE `date` = ?");
		ps.setInt(1, Today);
		rs = ps.executeQuery();
		while (rs.next()) {
			count++;
			if (rs.getInt("count") != count) {
				System.out.println("오늘의 회차값 정보가 누락되거나 일치하지 않습니다. 쿼리를 확인해주세요.");
				World.Broadcast.broadcastMessage(CWvsContext.serverNotice(2, "[시스템] : 자동게임 시스템에 문제가 있을 수 있으니 상태를 점검해 주세요."));
			}                            
		}
		rs.close();
		ps.close();
		con.close();
		return (count + 1);
	} catch (e) {
		cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
		cm.dispose();
		return;
	} finally {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (con != null) {
			con.close();
		}
	}
 }
 
 function getResult(ToDate) {
	var con = null;
	var ps = null;
	var rs = null;
	var line = 0;
	var say = ToDate.toString().substring(0, 4) + "년 " + ToDate.toString().substring(4, 6) + "월 " + ToDate.toString().substring(6, 8) + "일 ";
	say += "홀짝 추첨결과 입니다.\r\n\r\n";
	try {
		//회차 확인하기
		con = DatabaseConnection.getConnection();
		ps = con.prepareStatement("SELECT * FROM `bettingresult` WHERE `date` = ?");
		ps.setInt(1, Integer.parseInt(ToDate));
		rs = ps.executeQuery();
		while (rs.next()) {
			say += rs.getInt("count") + "회 : " + (rs.getString("holjjack") == "홀" ? "#r홀#k" : "#b짝#k") + " / ";
			if (line%4 == 3) {
				say += "\r\n";
			}
			line++;
		}
		rs.close();
		ps.close();
		con.close();
		cm.sendOk(say);
		cm.dispose();
		return;
	} catch (e) {
		cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
		cm.dispose();
		return;
	} finally {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (con != null) {
			con.close();
		}
	}
 }
 
 function setBetting(choicecount, holjjack, money) {
	var con = null;
	var ps = null;
	var rs = null;
	var exist = false;
	try {
		con = DatabaseConnection.getConnection();
		ps = con.prepareStatement("SELECT * FROM `betting` WHERE `accountid` = ? AND `date` = ? AND `count` = ? AND `gametype` = ?");
		ps.setInt(1, cm.getPlayer().getAccountID());
		ps.setInt(2, Today);
		ps.setInt(3, choicecount);
		ps.setString(4, "홀짝");
		rs = ps.executeQuery();
		while (rs.next()) {
			exist = true;
		}
		rs.close();
		ps.close();
		if (exist == false) {
			cm.gainMeso(-money);
			ps = con.prepareStatement("INSERT INTO `betting` (`accountid`, `date`, `time`, `count`, `holjjack`, `leftright`, `threefour`, `gametype`, `betting`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, cm.getPlayer().getAccountID());
			ps.setInt(2, Today);
			ps.setString(3, Hour+Minute);
			ps.setInt(4, choicecount);
			ps.setString(5, holjjack);
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "홀짝");
			ps.setLong(9, money);
			ps.executeUpdate();
			ps.close();
			con.close();
			cm.sendOk("베팅이 완료되었습니다.");
			cm.dispose();
			return;
		} else {
			con.close();
			cm.sendOk("이미 베팅을 하였습니다.");
			cm.dispose();
			return;
		}
	} catch (e) {
		cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
		cm.dispose();
		return;
	} finally {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (con != null) {
			con.close();
		}
	}
 }
 
 function getBetResult() {
	var con = null;
	var ps = null;
	var rs = null;
	var check = 0;
	var say = "   아직 확인하지 않았거나 수령하지 못한 베팅 결과 입니다.\r\n   클릭 시 메소가 수령되며, 베팅기록은 삭제됩니다.\r\n   (메소가 꽉 차있어 수령하지 못하는 것은 본인 책임)\r\n\r\n";
	try {
		//회차 확인하기
		con = DatabaseConnection.getConnection();
		ps = con.prepareStatement("SELECT `betting`.`id`, `betting`.`accountid`, `betting`.`date`, `betting`.`count`, `betting`.`holjjack` AS `bet`, `bettingresult`.`holjjack` AS `result`, `betting`.`betting` FROM `betting` " +
		"INNER JOIN `bettingresult` ON `betting`.`count` = `bettingresult`.`count` AND `betting`.`date` = `bettingresult`.`date` WHERE `betting`.`accountid` = ? AND `betting`.`gametype` = '홀짝'");
		ps.setInt(1, cm.getPlayer().getAccountID());
		rs = ps.executeQuery();
		while (rs.next()) {
			say += "#L" + rs.getInt("id") + "#" + rs.getInt("date").toString().substring(0, 4) + "년 " + rs.getInt("date").toString().substring(4, 6) + "월 " + rs.getInt("date").toString().substring(6, 8) + "일 - " +
			rs.getInt("count") + "회 : " + (rs.getString("result") == "홀" ? "#r홀#k" : "#b짝#k") + "  //  " + 
			"베팅 : " + (rs.getString("bet") == "홀" ? "#r홀#k" : "#b짝#k") + " #e#d-#n#k " + (rs.getString("bet") == rs.getString("result") ? "#e#d적중#n#k" : "#e#d미적중#n#k") + "\r\n" +
			"   베팅금 : " + rs.getLong("betting") + "메소 // 당첨금 : " + (rs.getString("bet") == rs.getString("result") ? "#e#b" + Math.floor(rs.getLong("betting") * 배당) + "메소#n#k" : "#e#r0메소#n#k") + "#l\r\n\r\n";
			check++;
		}
		rs.close();
		ps.close();
		con.close();
		if (check <= 0) {
			cm.sendOk("베팅 기록이 존재하지 않습니다.");
			cm.dispose();
			return;
		}
		cm.sendSimple(say);
	} catch (e) {
		cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
		cm.dispose();
		return;
	} finally {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (con != null) {
			con.close();
		}
	}
 }
 
 function getReward(getid) {
	var con = null;
	var ps = null;
	var meso = 0;
	var say = "메소가 정상 수령 되었으며\r\n해당 베팅 기록이 삭제되었습니다.\r\n";
	try {
		//회차 확인하기
		con = DatabaseConnection.getConnection();
		ps = con.prepareStatement("SELECT `betting`.`id`, `betting`.`accountid`, `betting`.`date`, `betting`.`count`, `betting`.`holjjack` AS `bet`, `bettingresult`.`holjjack` AS `result`, `betting`.`betting` FROM `betting` " +
		"INNER JOIN `bettingresult` ON `betting`.`count` = `bettingresult`.`count` AND `betting`.`date` = `bettingresult`.`date` WHERE `betting`.`id` = ? AND `betting`.`accountid` = ? AND `betting`.`gametype` = '홀짝'");
		ps.setInt(1, getid);
		ps.setInt(2, cm.getPlayer().getAccountID());
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getString("bet") == rs.getString("result")) {
				meso = rs.getLong("betting");
			} else {
				meso = 0;
			}
		}
		rs.close();
		ps.close();
		ps = con.prepareStatement("DELETE FROM `betting` WHERE `id` = ?");
		ps.setInt(1, getid);
		ps.executeUpdate();
		ps.close();
		con.close();
		cm.gainMeso(meso * 배당);
		cm.sendOk(say);
		cm.dispose();
		return;
	} catch (e) {
		cm.sendOk("오류가 발생하였습니다.\r\n\r\n" + e);
		cm.dispose();
		return;
	} finally {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (con != null) {
			con.close();
		}
	}
 }
 
 var 베팅주기 = 2; //분
 var betcount = 0;
 var choicecount = 0;
 var holjjack = "";
 var money = 0;
 var 최대금액 = 5000000000;
 var 배당 = 1.9;
 
 var canUse = true; //점검시 막을 지 유무 체크
 
 var choice = 0;
 var status = -1;
 
 function start() {
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
		UpdateTime();
		betCount = getCount(Today);
		var say = "   게임 종류 : 홀짝\r\n";   
		say += "   현재 시간 : " + NowTime + "\r\n";
		say += "   현재 추첨 진행 중인 회차 : #e#b" + betCount + "회#n#k\r\n";
		say += "   회차 당 추첨 간격 : " + 베팅주기 + "분\r\n\r\n";
		say += "   원하시는 메뉴를 선택해 주세요.\r\n\r\n";
		say += "#L1#홀짝 베팅하기#l #L2#추첨 결과 확인#l #L3#당첨금 수령하기#l\r\n";
		cm.sendSimple(say);
	} else if (status == 1) {
		UpdateTime();
		if (selection == 1) {
			choice = 1;
			if ((Hour == 23 && Minute >= 51) || (Hour == 0 && Minute < 3) || (cm.getPlayer().getGMLevel() < 1 && canUse == false)) {
				cm.sendOk("자동게임 시스템 점검시간 입니다. 23:51 ~ 00:01\r\n자정 전 마지막 5회차는 00:02분 부터 결과를 확인할 수 있습니다.");
				dispose();
				return;
			}
			var say = "   현재시간 : " + NowTime + "\r\n   베팅하기 원하시는 회차를 선택해 주세요.#e#b\r\n\r\n";
			if (Minute%베팅주기!=(베팅주기-1)) {
				say += "#L" + betCount + "# " + betCount + "회#l";
			}
			say += "#L" + (betCount + 1) + "# " + (betCount + 1) + "회#l";
			say += "#L" + (betCount + 2) + "# " + (betCount + 2) + "회#l";
			say += "#L" + (betCount + 3) + "# " + (betCount + 3) + "회#l";
			say += "#L" + (betCount + 4) + "# " + (betCount + 4) + "회#l";
			cm.sendSimple(say);
		} else if (selection == 2) {
			choice = 2;
			cm.sendGetNumber("어느 날의 추첨 결과를 확인하시겠습니까?\r\n(예 : 2021년 3월 1일의 당첨결과 확인 시 20210301 입력)", 0, 20220101, 20221231);
		} else if (selection == 3) {
			choice = 3;
			getBetResult();
		} else {
			cm.dispose();
			return;
		}
	} else if (status == 2) {
		UpdateTime();
		if (choice == 1) {
			if ((Hour == 23 && Minute >= 51) || (Hour == 0 && Minute < 3) || (cm.getPlayer().getGMLevel() < 1 && canUse == false)) {
				cm.sendOk("자동게임 시스템 점검시간 입니다. 23:51 ~ 00:01");
				dispose();
				return;
			}
			if (selection == betCount && (Minute%베팅주기==(베팅주기-1))) {
				cm.sendOk("당첨 결과가 나오기 1분 전부터는 베팅하실 수 없습니다.\r\n\r\n현재시간 : " + NowTime);
				dispose();
				return;
			}
			choicecount = selection;
			cm.sendSimple("   현재시간 : " + NowTime + "\r\n\r\n   #e#b" + choicecount + "회차#n#k에 베팅할 #e#r홀#e#d/#e#b짝#n#k을 선택해 주세요.\r\n#L1##e#r홀#n#k#l #L2##e#b짝#n#k#l");
		} else if (choice == 2) {
			getResult(selection);
		} else if (choice == 3) {
			getReward(selection);
		} else {
			cm.dispose();
			return;
		}
	} else if (status == 3) {
		UpdateTime();
		if (choice == 1) {
			if ((Hour == 23 && Minute >= 51) || (Hour == 0 && Minute < 3) || (cm.getPlayer().getGMLevel() < 1 && canUse == false)) {
				cm.sendOk("자동게임 시스템 점검시간 입니다. 23:51 ~ 00:01");
				dispose();
				return;
			}
			if (choicecount == betCount && (Minute%베팅주기==(베팅주기-1))) {
				cm.sendOk("당첨 결과가 나오기 1분 전부터는 베팅하실 수 없습니다.\r\n\r\n현재시간 : " + NowTime);
				dispose();
				return;
			}
			if (selection == 1) {
				holjjack = "홀";
			} else if (selection == 2) {
				holjjack = "짝";
			} else {
				cm.dispose();
				return;
			}
			cm.sendGetText("현재시간 : " + NowTime + "\r\n\r\n#e#b" + choicecount + "회차#n#k에 '" + ((holjjack == "홀") ? "#e#r홀#n#k" : "#e#b짝#n#k") + "'을 선택하셨습니다.\r\n"
			+ "베팅하실 금액을 입력해주세요.\r\n(최대 2000억까지 베팅가능)");
		} else if (choice == 2) {
			cm.dispose();
			return;
		} else if (choice == 3) {
			cm.dispose();
			return;
		} else {
			cm.dispose();
			return;
		}
	} else if (status == 4) {
		UpdateTime();
		if (choice == 1) {
			if ((Hour == 23 && Minute >= 51) || (Hour == 0 && Minute < 3) || (cm.getPlayer().getGMLevel() < 1 && canUse == false)) {
				cm.sendOk("자동게임 시스템 점검시간 입니다. 23:51 ~ 00:01");
				dispose();
				return;
			}
			if (choicecount == betCount && (Minute%베팅주기==(베팅주기-1))) {
				cm.sendOk("당첨 결과가 나오기 1분 전부터는 베팅하실 수 없습니다.\r\n\r\n현재시간 : " + NowTime);
				dispose();
				return;
			}
			try {
				if (cm.getText().length() > 15) {
					cm.sendOk("잘못된 값을 입력하셨습니다.");
					cm.dispose();
					return;
				}
				if (isNaN(cm.getText())) {
					cm.sendOk("숫자가 아닌 값을 입력하셨습니다.");
					cm.dispose();
					return;
				}
				money = Long.parseLong(cm.getText());
				if (money < 1 || money > 최대금액) {
					cm.sendOk("베팅이 불가능한 금액입니다.");
					cm.dispose();
					return;
				}
				if (money > cm.getPlayer().getMeso()) {
					cm.sendOk("현재 가지고 있는 메소보다 많은 금액을 베팅시도 하였습니다.");
					cm.dispose();
					return;
				}
			} catch (e) {
				cm.sendOk("잘못된 값을 입력하셨습니다.");
				cm.dispose();
				return;
			}
			cm.sendYesNo("현재시간 : " + NowTime + "\r\n\r\n#e#b" + choicecount + "회차#n#k에 '" + ((holjjack == "홀") ? "#e#r홀#n#k" : "#e#b짝#n#k") + "'을 선택하셨습니다.\r\n\r\n" 
			 + "정말로 " + cm.getText() + "메소를 베팅하시겠습니까?");
		} else if (choice == 2) {
			cm.dispose();
			return;
		} else if (choice == 3) {
			cm.dispose();
			return;
		} else {
			cm.dispose();
			return;
		}
	} else if (status == 5) {
		UpdateTime();
		if (choice == 1) {
			if ((Hour == 23 && Minute >= 51) || (Hour == 0 && Minute < 3) || (cm.getPlayer().getGMLevel() < 1 && canUse == false)) {
				cm.sendOk("자동게임 시스템 점검시간 입니다. 23:51 ~ 00:01");
				dispose();
				return;
			}
			if (choicecount == betCount && (Minute%베팅주기==(베팅주기-1))) {
				cm.sendOk("당첨 결과가 나오기 1분 전부터는 베팅하실 수 없습니다.\r\n\r\n현재시간 : " + NowTime);
				dispose();
				return;
			}
			if (cm.getPlayer().getMeso() < money) {
				cm.sendOk("갖고 있는 금액보다 더 많은 금액을 입력하였습니다.");
				cm.dispose();
				return;
			}
			setBetting(choicecount, holjjack, money);
		} else if (choice == 2) {
			cm.dispose();
			return;
		} else if (choice == 3) {
			cm.dispose();
			return;
		} else {
			cm.dispose();
			return;
		}
	} else {
		cm.dispose();
		return;
	}
 }