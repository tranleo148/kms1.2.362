// SLFCG (kokose1234@mail.slfcg.kr)
// MelonK (quituser@naver.com)

importPackage(Packages.client);

var melonk = -1;
var selected = false;

function start() {
    action(1, 0, 0);
}

function action(jongwook, gulgi, syon) {
    if (jongwook != 1) {
        if (melonk == 2 && yein == 0 && jongwook == 0) {
            cm.sendOk("잘 생각해보고 다시 말을 걸어주세요.");
        }
        cm.dispose();
        return;
    }
    if (jongwook == 1) {
        melonk++;
    }
    if (melonk == 0) {
        if (cm.getPlayer().getMapId() == 921171100) {
            var temp = parseInt(cm.getPlayer().getKeyValue(15901, "stage"));
            var temp2 = parseInt(cm.getPlayer().getKeyValue(15901, "selectedStage"));
            if (temp == temp2) {
                talk = "음! 도전에 #r실패#k했군요?\r\n힘들다면 조금 낮은 단계부터 도전해 보는게 어때요?";
                cm.sendNext(talk)
            } else if (temp > temp2) {
                var success = temp - 1; //성공한 스테이지
                talk = "와~ #b" + success + "스테이지#k 까지 돌파하셨군요? 대단해요~\r\n\r\n";
                var b_best = parseInt(cm.getPlayer().getKeyValue(15901, "best_b"));
                var b_besttime = parseInt(cm.getPlayer().getKeyValue(15901, "besttime_b"));
                var best = parseInt(cm.getPlayer().getKeyValue(15901, "best"));
                var besttime = parseInt(cm.getPlayer().getKeyValue(15901, "besttime"));
                var b_rank = parseInt(cm.getPlayer().getKeyValue(15901, "rank_b"));
                var b_score = b_best * 1000 + (180 - b_besttime);
                var score = success * 1000 + (180 - besttime);
                DreamBreakerRank.EditRecord(cm.getPlayer().getName(), best, besttime);
                var rank = DreamBreakerRank.getRank(cm.getPlayer().getName());
                if ((rank > b_rank || score > b_score) && (rank > 0 && rank <= 100)) {
                    talk += "이번주 #r#e랭킹 신기록#k#n이군요! 랭킹에 등록해 드릴게요\r\n\r\n";
                }

                if (score > b_score) {
                    talk += "#r게다가 개인 신기록#k을 세우셨군요!";
                }
                cm.sendNext(talk)
            }
        } else {
            var date = new Date()
            var today = date.getFullYear() + ""
            today += new Date().getMonth() < 9 ? "0" : ""
            today += Number(date.getMonth() + 1) + ""
            today += new Date().getDate() < 10 ? "0" : ""
            today += date.getDate();
            resetKeyValue();
            if (today != cm.getPlayer().getKeyValue(20190131, "Date")) {
                cm.getPlayer().setKeyValue(20190131, "Date", today);
                cm.getPlayer().setKeyValue(20190131, "play", "0");
            }

            talk = "#b#e<드림브레이커>#k#n\r\n"
            talk += "아웅.... 언제쯤 편안하게 잠을 잘 수 있을까?\r\n\r\n"
            talk += "#L0# #b<드림브레이커>에 도전한다.#l\r\n"
            talk += "#L1# 나의 기록을 확인한다.#l\r\n"
            talk += "#L2# 주간 랭킹을 확인한다.#l\r\n"
            talk += "#L3# 설명을 듣는다.#l";
            cm.sendSimple(talk);
        }
    } else if (melonk == 1) {
        if (cm.getPlayer().getMapId() == 921171100) {
            cm.warp(450004000);
            cm.dispose();
        } else {
            if (!selected) {
                yein = syon;
                selected = true;
            }
            if (yein == 0) {
                talk = "당신의 최고 기록은 #r#e" + cm.getPlayer().getKeyValue(15901, "best") + "단계#k#n군요?\r\n"
                talk += "현재 도전할 수 있는 스테이지는 다음과 같아요.\r\n\r\n"
                floorstage = parseInt(cm.getPlayer().getKeyValue(15901, "best")) - parseInt(cm.getPlayer().getKeyValue(15901, "best") % 10);
                for (i = floorstage; i >= 0; i-=10) {
                    i = i == 0 ? 1 : i;
                    talk += "#L" + (i) + "##b" + (i) + "#k 단계\r\n";
                }
                cm.sendSimple(talk);
            } else if (yein == 1) {
                talk = "당신의 #e<드림브레이커 기록>#n을 알려드릴게요!\r\n\r\n"
                talk += "#e개인 최고기록 : #b" + cm.getPlayer().getKeyValue(15901, "best") + "스테이지#k#n\r\n"
                talk += "#e오늘 입장 횟수 : #b" + cm.getPlayer().getKeyValue(20190131, "play") + " 번#k#n\r\n"
                talk += "#e지난주 랭킹 : #b" + cm.getPlayer().getKeyValue(20190131, "lastweek") + "#k#n\r\n"
                cm.sendNext(talk);
                cm.dispose();
            } else if (yein == 2) {
                cm.ShowDreamBreakerRanking();
                cm.dispose();
            } else {
                talk = "무엇을 알려드릴까요?\r\n\r\n";
                talk += "#L0# #e드림브레이커 규칙#l\r\n"
                talk += "#L1# 드림포인트 획득과 스킬 사용#l\r\n"
                talk += "#L2# 드림브레이커 보상#l\r\n"
                talk += "#L3# 드림브레이커 랭킹#l\r\n";
                talk += "#L4# 설명을 듣지 않는다."
                cm.sendSimple(talk);
            }
        }
    } else if (melonk >= 2 && yein == 3) {
        if (melonk == 2) {
            chansoo = syon;
        }
        name = ["드림브레이커 규칙", "드림포인트", "드림브레이커 보상", "드림브레이커 랭킹"];
        dialogue = [
            ["루시드의 악몽을 멈추기 위해선 #b#e숙면의 오르골#k#n을 지키고 #r#e악몽의 오르골#k#n을 파괴해야 해요.",
                "총 #e5개의 방#n 중에서 #b#e숙면의 오르골#k#n이 더 많으면 #b#e노란색 게이지가 왼쪽으로#k#n 차오르고, #r#e악몽의 오르골#k#n이 더 많으면 #r#e보라색 게이지가 오른쪽으로#k#n 차오를 거예요.\r\n\r\n결과적으로 제한시간 #e3분 이내에 #b노란색 게이지#k#n를 다 채우면 스테이지를 클리어하게 되죠.",
                "드림브레이커는 #e하루 3회#n까지 입장 할 수 있어요.",
                "그럼 저를 도와 #r#e루시드의 악몽#k#n을 멈춰주세요!"
            ],
            ["#e드림포인트#n는 드림브레이커에서 #r#e스테이지를 클리어 할때마다#k#n 얻는 점수로, #b#e전략스킬#k#n을 발동핼 때 사용해요.\r\n\r\n획득되는 드림포인트는 #e매 10스테이지#n마다 #e10씩 증가#n하고 최대 #b#e3000점#k#n까지 누적할 수 있답니다.",
                "사용할 수 있는 #b#e전략스킬#n#k은 다음과 같아요.\r\n\r\n#e<게이지 홀드>#n\r\n드림포인트: 200 소모 / 5초간 게이지의 이동을 멈춤.\r\n\r\n#e<자각의 종>#n\r\n드림포인트: 300 소모 / 랜덤한 악몽의 오르골 1개를 제거.\r\n\r\n#e<헝겊인형 소환>#n\r\n드림포인트: 400 소모 / 주변의 몬스터를 도발하는 헝겊인형 소환. (15초간 유지)\r\n\r\n#e<폭파>#n\r\n드림포인트: 900 소모 / 모든 몬스터를 처치, 10초간 재소환을 막음.",
                "하나의 스테이지에서는 #r#e동일한 스킬을 두 번 사용할 수 없으니#k#n 스킬 사용은 신중하게 하세요!"
            ],
            ["스테이지를 클리어 하면 #e최종적으로 도달한 스테이지#n 만큼의 코인을 얻을 수 있어요.",
                "마지막으로 일일 #b도전 횟수를 모두 소모#k한 뒤 #b레헬른 중심가#k오른쪽에 위치한 #r주간 랭킹 1~5위 유저#k를 찾아가면 #b하루 1번 다양한 선물#k을 받을 수 있으니 꼭 찾아가 보세요!"
            ],
            ["스테이지를 클리어 하면 #b도달 스테이지 / 클리어 시간#k을 기준으로 #b주간 최고기록#k인 경우 #r자동으로 랭킹에 등록#k돼요.",
                "주간랭킹은 #b매 주 월요일 자정#k에 초기화 돼요.\r\n랭킹 정산을 위해 #r일요일 오후 11시 30분 부터 월요일 자정 12시 30분 까지는#k 입장이 제한 된답니다.",
                "그리고 주간 랭킹 #e1~5등#n에 기록된 캐릭터는 #b레헬른 중심가#k 한켠에 그 모습이 1주일간 기록되고 여러 용사님들에게 선물을 드리는 역할을 수행해요.",
                "#r최고의 드림브레이커#k가 되어 용사님들로부터 선망의 대상이 되어 보세요!"
            ]
        ];
        talk = "#e<" + name[chansoo] + ">#n\r\n\r\n";
        talk += dialogue[chansoo][melonk - 2];
        if (melonk == 2) {
            cm.sendNext(talk);
        } else {
            cm.sendNextPrev(talk);
            if (melonk - 1 == dialogue[chansoo].length) {
                melonk = 0;
            }
        }
    } else if (melonk == 2) {
        stage = syon;
        talk = "<드림브레이커> #b" + stage + "단계#k에 도전할 건가요?\r\n\r\n";
        talk += "#b오늘 도전 횟수 " + cm.getPlayer().getKeyValue(20190131, "play") + " / 3";
        cm.sendYesNo(talk);
    } else if (melonk == 3) {
        if (cm.getEventManager("DreamBreaker").getInstance("DreamBreaker") != null) {
            cm.sendOk("이미 누군가 도전 중이에요.");
            cm.dispose();
        } else {
            var event = cm.getEventManager("DreamBreaker").getInstance("DreamBreaker");
            if (event == null) {
                if (Number(cm.getPlayer().getKeyValue(20190131, "play")) < 3 || cm.getPlayer().isGM()) {
                    cm.getEventManager("DreamBreaker").startInstance_Solo("" + 921171000, cm.getPlayer());
                    cm.getPlayer().setKeyValue(20190131, "play", "" + (Number(cm.getPlayer().getKeyValue(20190131, "play")) + 1));
                    cm.getPlayer().setKeyValue(15901, "stage", "" + stage);
                    cm.getPlayer().setKeyValue(15901, "selectedStage", "" + stage);
                    cm.getPlayer().setKeyValue(15901, "clearTime", "0");
                    cm.getPlayer().setKeyValue(15901, "dream", cm.getPlayer().getKeyValue(15901, "dream"));
                    cm.dispose();
                } else {
                    cm.sendOk("오늘은 더 이상 #b#e<드림 브레이커>#k#n에 도전할 수 없어요.\r\n\r\n#r#e(1일 3회 입장 가능)#k#n");
                    cm.dispose();
                }
            } else {
                cm.sendOk("이미 누군가 도전 중이에요.");
                cm.dispose();
            }
        }
    }
}

function resetKeyValue() {
    if (parseInt(cm.getPlayer().getKeyValue(15901, "best")) <= 0) {
        cm.getPlayer().setKeyValue(15901, "best", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(20190131, "play")) <= 0) {
        cm.getPlayer().setKeyValue(20190131, "play", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(20190131, "clearTime")) <= 0) {
        cm.getPlayer().setKeyValue(15901, "clearTime", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(20190131, "lastweek")) <= 0) {
        cm.getPlayer().setKeyValue(20190131, "lastweek", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(15901, "dream")) <= 0) {
        cm.getPlayer().setKeyValue(15901, "dream", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(15901, "stage")) <= 0) {
        cm.getPlayer().setKeyValue(15901, "stage", "0");
    }
    if (parseInt(cm.getPlayer().getKeyValue(15901, "selectedStage")) <= 0) {
        cm.getPlayer().setKeyValue(15901, "selectedStage", "0");
    }
}