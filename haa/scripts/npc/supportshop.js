importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);

별파 = "#fUI/GuildMark.img/Mark/Pattern/00004001/11#"
별노 = "#fUI/GuildMark.img/Mark/Pattern/00004001/3#"
별흰 = "#fUI/GuildMark.img/Mark/Pattern/00004001/15#"
별갈 = "#fUI/GuildMark.img/Mark/Pattern/00004001/5#"
별빨 = "#fUI/GuildMark.img/Mark/Pattern/00004001/1#"
별검 = "#fUI/GuildMark.img/Mark/Pattern/00004001/16#"
별보 = "#fUI/GuildMark.img/Mark/Pattern/00004001/13#"
별 = "#fUI/FarmUI.img/objectStatus/star/whole#";
S = "#fUI/CashShop.img/CSEffect/today/0#"
데미지 = "22170075"
횟수 = "22140020"
dd = Math.floor(Math.random() * 5) + 3 // 최소 10 최대 35 , 혼테일

importPackage(Packages.constants);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }2630127
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {

	               var a = "#fs11##b#h0#님#fc0xFF000000# 의 서포트 포인트#fc0xFF000000# : #e#r"+cm.getPlayer().getHPoint()+" P#fc0xFF000000##n\r\n"; 
		a += "#L1000##d#i2048753##z2048753# 5개#k#l\r\n             #fc0xFF000000#서포트 포인트#k #e#r800P#fc0xFF000000##n\r\n";
		a += "#L1007##d#i2630127##z2630127# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트#k #e#r6000P#fc0xFF000000##n\r\n";

		//a += "#L1013##d#i5060048##z5060048# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트#k #e#r1000P#fc0xFF000000##n\r\n"; //골드애플 1013


		a += "#L1008##d#i2046981##z2046981# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n"; //홍보줌서 800포
		a += "#L1009##d#i2047810##z2047810# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n";
		a += "#L1010##d#i2046970##z2046970# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n";
		a += "#L1011##d#i2046831##z2046831# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n";
		a += "#L1012##d#i2046832##z2046832# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n";


		a += "#L1003##d#i5068300##z5068300# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r1000P#fc0xFF000000##n\r\n";
		a += "#L1004##d#i5069100##z5069100# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r1000P#fc0xFF000000##n\r\n";

		a += "#L1006##d#i5121060##z5121060# 1개#k#l\r\n             #fc0xFF000000#서포트 포인트 #e#r800P#fc0xFF000000##n\r\n";
		cm.sendSimple(a);
	        } else if (selection == 3621) {
		cm.dispose();
		cm.openNpc(9000267);

	        } else if (selection == 3623) {
		cm.dispose();
		cm.openNpc(9000268);


	        } else if (selection == 3624) {
		cm.dispose();
		cm.openNpc(9000269);

	        } else if (selection == 3625) {
		cm.dispose();
		cm.openNpc(9000219);

	        } else if (selection == 3626) {
		cm.dispose();
		cm.openNpc(1530141);

                     } else if (selection == 3622) {
		cm.dispose();
		cm.openNpc(9001010);

		} else if (selection == 1000) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2048753)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2048753, 5);
		        cm.sendOk("#b서포트 포인트#k 로 #i2048753# #r 5개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1001) {
		if (cm.getPlayer().getHPoint() >= 800) {
		    if (cm.canHold(5062005)) {
			cm.getPlayer().gainHPoint(-800);
			cm.gainItem(5062005, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5062005# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}




		} else if (selection == 1002) {
		if (cm.getPlayer().getHPoint() >= 800) {
		    if (cm.canHold(5062503)) {
			cm.getPlayer().gainHPoint(-800);
			cm.gainItem(5062503, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5062503# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

	} else if (selection == 1055) {
		if (cm.getPlayer().getHPoint() >= 80000) {
		    if (cm.canHold(5330000)) {
			cm.getPlayer().gainHPoint(-80000);
			cm.gainItem(5330000, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5330000# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}
         
        } else if (selection == 1045) {
		if (cm.getPlayer().getHPoint() >= 220000) {
		    if (cm.canHold(2437122)) {
			cm.getPlayer().gainHPoint(-220000);
			cm.gainItem(2437122, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2437122# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

        } else if (selection == 1046) {
		if (cm.getPlayer().getHPoint() >= 220000) {
		    if (cm.canHold(2023287)) {
			cm.getPlayer().gainHPoint(-220000);
			cm.gainItem(2023287, 2);
		        cm.sendOk("#b서포트 포인트#k 로 #i2023287# #r 2 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}
        
        } else if (selection == 1047) {
		if (cm.getPlayer().getHPoint() >= 350000) {
		    if (cm.canHold(5068305)) {
			cm.getPlayer().gainHPoint(-350000);
			cm.gainItem(5068305, 5);
		        cm.sendOk("#b서포트 포인트#k 로 #i5068305# #r 5 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1003) {
		if (cm.getPlayer().getHPoint() >= 200) {
		    if (cm.canHold(5068300)) {
			cm.getPlayer().gainHPoint(-200);
			cm.gainItem(5068300, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5068300# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1004) {
		if (cm.getPlayer().getHPoint() >= 200) {
		    if (cm.canHold(5069100)) {
			cm.getPlayer().gainHPoint(-200);
			cm.gainItem(5069100, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5069100# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}
                
                } else if (selection == 1084) {
		if (cm.getPlayer().getHPoint() >= 490000) {
		    if (cm.canHold(2431087)) {
			cm.getPlayer().gainHPoint(-490000);
			cm.gainItem(2431087, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2431087# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1099) {
		if (cm.getPlayer().getHPoint() >= 80000) {
		    if (cm.canHold(5062005)) {
			cm.getPlayer().gainHPoint(-80000);
			cm.gainItem(5062005, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5062005# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1005) {
		if (cm.getPlayer().getHPoint() >= 900) {
		    if (cm.canHold(2430041)) {
			cm.getPlayer().gainHPoint(-900);
			cm.gainItem(2430041, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2430041# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1006) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(5121060)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(5121060, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5121060# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1007) {
		if (cm.getPlayer().getHPoint() >= 3000) {
		    if (cm.canHold(2630127)) {
			cm.getPlayer().gainHPoint(-3000);
			cm.gainItem(2630127, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2630127# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1008) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2046981)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2046981, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2046981# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1009) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2047810)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2047810, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2047810# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1010) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2046970)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2046970, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2046970# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1011) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2046831)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2046831, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2046831# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1012) {
		if (cm.getPlayer().getHPoint() >= 400) {
		    if (cm.canHold(2046832)) {
			cm.getPlayer().gainHPoint(-400);
			cm.gainItem(2046832, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2046832# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1013) {
		if (cm.getPlayer().getHPoint() >= 300) {
		    if (cm.canHold(2046970)) {
			cm.getPlayer().gainHPoint(-300);
			cm.gainItem(5060048, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5060048# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}


		} else if (selection == 1014) {
		if (cm.getPlayer().getHPoint() >= 900) {
		    if (cm.canHold(5068305)) {
			cm.getPlayer().gainHPoint(-900);
			cm.gainItem(5068305, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i5068305# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1015) {
		if (cm.getPlayer().getHPoint() >= 8000) {
		    if (cm.canHold(2048049)) {
			cm.getPlayer().gainHPoint(-8000);
			cm.gainItem(2048049, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2048047# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}


		} else if (selection == 1016) {
		if (cm.getPlayer().getHPoint() >= 8000) {
		    if (cm.canHold(2048050)) {
			cm.getPlayer().gainHPoint(-8000);
			cm.gainItem(2048050, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2048048# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1017) {
		if (cm.getPlayer().getHPoint() >= 15000) {
		    if (cm.canHold(2470007)) {
			cm.getPlayer().gainHPoint(-15000);
			cm.gainItem(2470007, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2470007# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1018) {
		if (cm.getPlayer().getHPoint() >= 6000) {
		    if (cm.canHold(2450064)) {
			cm.getPlayer().gainHPoint(-6000);
			cm.gainItem(2450064, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2450064# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1019) {
		if (cm.getPlayer().getHPoint() >= 10000) {
		    if (cm.canHold(2450163)) {
			cm.getPlayer().gainHPoint(-10000);
			cm.gainItem(2450163, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2450163# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

	        } else if (selection == 1020) {
		cm.dispose();
 	        cm.openNpc(1031001);

		} else if (selection == 1021) {
		if (cm.getPlayer().getHPoint() >= 12000) {
		    if (cm.canHold(2630755)) {
			cm.getPlayer().gainHPoint(-12000);
			cm.gainItem(2630755, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2630755# 을 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1022) {
		if (cm.getPlayer().getHPoint() >= 10000) {
		    if (cm.canHold(2630755)) {
			cm.getPlayer().gainHPoint(-10000);
			cm.gainItem(4021031, 100);
		        cm.sendOk("#b서포트 포인트#k 로 #i4021031# 100개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1023) {
		if (cm.getPlayer().getHPoint() >= 8000) {
		    if (cm.canHold(2630756)) {
			cm.getPlayer().gainHPoint(-8000);
			cm.gainItem(2630756, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2630756# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1024) {
		if (cm.getPlayer().getHPoint() >= 8000) {
		    if (cm.canHold(2630551)) {
			cm.getPlayer().gainHPoint(-8000);
			cm.gainItem(2630551, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2630551# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

	        } else if (selection == 1025) {
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 3003273, "iicashItemsearch");


                } else if (selection == 1026) {
		if (cm.getPlayer().getHPoint() >= 15000) {
		    if (cm.canHold(5121060)) {
			cm.getPlayer().gainHPoint(-15000);
			cm.gainItem(5121060, 2);
		        cm.sendOk("#b서포트 포인트#k 로 #i5121060# 2개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1027) {
		if (cm.getPlayer().getHPoint() >= 50000) {
		    if (cm.canHold(4034803)) {
			cm.getPlayer().gainHPoint(-50000);
			cm.gainItem(4034803, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i4034803# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}		

                } else if (selection == 1035) {
		if (cm.getPlayer().getHPoint() >= 100000) {
		    if (cm.canHold(2437121)) {
			cm.getPlayer().gainHPoint(-100000);
			cm.gainItem(2437121, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2437121# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1036) {
		if (cm.getPlayer().getHPoint() >= 990000) {
		    if (cm.canHold(2439209)) {
			cm.getPlayer().gainHPoint(-990000);
			cm.gainItem(2439209, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2439209# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

                } else if (selection == 1037) {
		if (cm.getPlayer().getHPoint() >= 8000) {
		    if (cm.canHold(2046832)) {
			cm.getPlayer().gainHPoint(-8000);
			cm.gainItem(2046832, 1);
		        //cm.sendOk("#b서포트 포인트#k 로 #i2046832# 1개를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r캐시 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

	} else if (selection == 1090) {
		if (cm.getPlayer().getHPoint() >= 3000) {
		    if (cm.canHold(4021031)) {
			cm.getPlayer().gainHPoint(-3000);
			cm.gainItem(2435748, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2435748# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1091) {
		if (cm.getPlayer().getHPoint() >= 15000) {
		    if (cm.canHold(2435748)) {
			cm.getPlayer().gainHPoint(-15000);
			cm.gainItem(2435748, 5);
		        cm.sendOk("#b서포트 포인트#k 로 #i2435748# #r 5 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		} else if (selection == 1092) {
		if (cm.getPlayer().getHPoint() >= 30000) {
		    if (cm.canHold(4021031)) {
			cm.getPlayer().gainHPoint(-30000);
			cm.gainItem(2437157, 1);
		        cm.sendOk("#b서포트 포인트#k 로 #i2437157# #r 1 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}


		} else if (selection == 1093) {
		if (cm.getPlayer().getHPoint() >= 150000) {
		    if (cm.canHold(4021031)) {
			cm.getPlayer().gainHPoint(-150000);
			cm.gainItem(2437157, 5);
		        cm.sendOk("#b서포트 포인트#k 로 #i2437157# #r 5 개#k 를 구입 하셨습니다.");
			cm.dispose();
		    } else {
		        cm.sendOk("#r소비 칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fs11##b서포트 포인트#k 가 부족합니다.");
		    cm.dispose();
		}

		}
	}
}