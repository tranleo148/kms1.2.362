


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    슈피겔만 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062508

    엔피시 이름 : 들꽃의 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

    엔피시 설명 : 블루밍 플라워


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.scripting);
var status = -1;


function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "흐흥흥~ 용사님께선 무엇이 궁금하신가여~?\r\n"
        말 += "#L0##b#e<블루밍 포레스트>#n에 대해 알려줘.\r\n"
        말 += "#L1##b#e<블루밍 플라워>#n에 대해 알려줘.\r\n"
        말 += "#L2##b#e<블루밍 코인샵>#n에 대해 알려줘.\r\n"
        말 += "#L3##b#e<위시 코인>#n에 대해 알려줘.\r\n"
        말 += "#L4##b#e<플로라 블레싱>#n에 대해 알려줘.\r\n"
        말 += "#L5##b#e<블루밍 모먼트>#n에 대해 알려줘.\r\n"
        말 += "#L6##b#e<유니온 가드닝>#n에 대해 알려줘.\r\n"
        말 += "#L7##b#e<돌정령을 구해달람!>#n에 대해 알려줘.\r\n"
        말 += "#L8##b#e<블루밍 레이스>#n에 대해 알려줘.#k\r\n"
        말 += "#L9#더 이상 궁금한건 없어."
        qm.sendOkS(말, 4, 9062508);
    } else if (status == 1) {
        sel = selection;
        switch (sel) {
            case 0:
                말 = "흥얼흥얼~ 온몸을 적시는 밝은 햇빛~\r\n"
                말 += "이불을 감싸 덮은 것처럼 따스한 햇살~\r\n"
                말 += "아! 정말 아름다운 봄이에여!\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 1:
                말 = "블루밍 포레스트의 #r잠든 꽃#k들을 깨우면?\r\n"
                말 += "#r다양한 보상#k을 팍팍 드려여!\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 2:
                말 = "블루밍 포레스트에 놀러 온 정령 친구들에게서\r\n"
                말 += "#r특별한 물건#k도 구매하실 수 있어여!\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 3:
                말 = "두.근.두.근.콩.닥.콩.닥.기.분.좋.아. 예!\r\n"
                말 += "안녕~ #b#e위시 코인#k#n이 처음인 친구에게~\r\n"
                말 += "#b내가 직접#k 설명해 줄게~!\r\n"
                qm.sendNextS(말, 4, 9062536);
                break;
            case 4:
                말 = "메이플 월드 곳곳에 #b#e아름다운 꽃#k#n이 피도록 도와주세요!\r\n"
                qm.sendNextS(말, 4, 9062533);
                break;
            case 5:
                말 = "아! 따사로운 햇살~ 기분 좋은 봄바람~\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 6:
                말 = "#r#e블루밍 포레스트#k#n의 꽃이 자라려면 #b#e잡초#k#n를 제거해줘야 해.\r\n"
                qm.sendNextS(말, 4, 9062516);
                break;
            case 7:
                말 = "아앗?! 블루밍 포레스트의 아름다운 꽃들을 구경하러 왔다가\r\n"
                말 += "#r곤경에 처한 정령 친구#k들이 있대여..!!\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 8:
                말 = "블루밍 포레스트를 이곳저곳 구경하던 조그만 정령이\r\n"
                말 += "#r엄청난 장소#k를 발견했대여..!!\r\n"
                qm.sendNextS(말, 4, 9062508);
                break;
            case 9:
                if (Randomizer.isSuccess(40)) {
                    qm.sendSimpleS("어맛..! #r#e붉은 장미#k#n처럼 정열적인 용사님..!", 4, 9062508);
                } else if (Randomizer.isSuccess(40)) {
                    qm.sendSimpleS("꺄륵! 용사님은 #b#e물망초#k#n처럼 진실한 마음을 가진 분이시군여!!", 4, 9062508);
                } else {
                    qm.sendSimpleS("헤헤~ #e무궁화#n처럼 언제나 용사님을 기다리고 있을게여~!", 4, 9062508);
                }
                qm.dispose();
        }
    } else if (status == 2) {
        switch (sel) {
            case 0:
                말 = "햇살 가득한 들판을 뛰노는 꿈을 꾸고 있었는데~?\r\n"
                말 += "간지러운 기분에 눈을 떠보니 숲에 봄기운이 가득한 거예여!!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "잠들어 있는 친구들을 깨우는 방법은 아주 간단해여!\r\n"
                말 += "바로 #r#e따듯한 햇살#k#n을 주는 거예여!\r\n\r\n"
                말 += "꽃이 피어나면 제가 #b#e특별한 선물#k#n을 드릴게여!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "#b#i4310310##z4310310##k을 주면 #b'나무의 정령'#k들에게\r\n"
                말 += "다양한 물건을 구매할 수 있지여!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 3:
                말 = "누구보다, 빠른, 나는, 강한 보스들을 몰래 지나치며~\r\n"
                말 += "#b#e위시 코인#k#n 숨겨놓았지~ 스.릴.넘.쳐 예!\r\n"
                qm.sendNextPrevS(말, 4, 9062536);
                break;
            case 4:
                말 = "#r#e레벨 범위 몬스터#k#n를 사냥하다 보면 일정 확률로\r\n"
                말 += "#i2633343# #b따스한 햇살#k을 얻을 수 있어요.\r\n"
                말 += "#b따스한 햇살#k을 모을 때마다 #b<꽃의 보석>#k에 힘이\r\n"
                말 += "1칸 충전되고 #i4310310# #b블루밍 코인#k 1개를 획득할 수 있어요!\r\n\r\n\r\n"
                말 += "#r※ 레벨 범위 몬스터는 캐릭터의 레벨을 기준으로 #e-20레벨 에서 +20레벨 범위#n에 해당하는 몬스터를 의미합니다."
                qm.sendNextPrevS(말, 4, 9062533);
                break;
            case 5:
                말 = "블루밍 포레스트의 #r햇살#k과 #b바람#k은\r\n"
                말 += "아주 특별하단 걸 알고 계신가여?!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "#b#e잡초 제거#k#n는 아주 간단해.\r\n"
                말 += "#r#e메이플 유니온#k#n에 소속된 캐릭터가 알아서 제거해 줄 거야!\r\n\r\n\r\n"
                말 += "#r※ 잡초 제거는 자동으로 진행됩니다."
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                말 = "내가 #b직접 설명#k하겠담!\r\n"
                말 += "향기로운 꽃들을 가까이서 구경하려다 그만...\r\n"
                말 += "친구들이 또 #e#b꽃줄기#k#n에 묶여 못 나오고 있담.\r\n"
                qm.sendNextPrevS(말, 4, 9062506);
                break;
            case 8:
                말 = "응, 지금부터는 내가 #b직접 설명#k해줄게.\r\n"
                말 += "블루밍 포레스트 곳곳을 구경하며 돌아다니는데\r\n"
                말 += "#b#e특별한 장소#k#n를 발견해서 그곳을 나와 정령들이 조금 손을 봤어.\r\n"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 3) {
        switch (sel) {
            case 0:
                말 = "메이플 월드의 평화가 #e#b18주년 간#k#n 지켜진 것을 기뻐하며\r\n"
                말 += "에르다들이 하나 둘 모여 눈부시게 따뜻한 빛을 내자~?\r\n\r\n"
                말 += "#b아르카나의 깊은 숲#k에 #b화사한 꽃#k들이 피어나기 시작했대여~!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "햇살은 어떻게 주냐고여?\r\n"
                말 += "#b하루에 한번,#k 일일 블루밍 코인 제한량을 모두 모우고 나서\r\n"
                말 += "저를 찾아와 #b#e<햇살 주기> 버튼#k#n을 눌러주세여!\r\n\r\n\r\n"
                말 += "#r※ 블루밍 플라워는 일일 블루밍 코인 제한량을 달성한 캐릭터만 진행할 수 있습니다."
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "#i4310310##r#z4310310##k은 꽃의 정령들과 함께 메이플 월드를 돌아다니며 #r따스한 햇살#k을 모으다 보면 생겨날 거예여~!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 3:
                말 = "하지만~ #b#e위시 코인#k#n 찾아오는 건 너무 어려워~\r\n"
                qm.sendNextPrevS(말, 4, 9062536);
                break;
            case 4:
                말 = "#b<꽃의 보석>#k에 힘이 10칸 모두 충전되면 #b#e플로라 블레싱#k#n이 시작돼요!\r\n"
                말 += "플로라 블레싱이 시작되면 #r#e정령들이 능력#k#n을 발휘할 거예요!\r\n"
                qm.sendNextPrevS(말, 4, 9062533);
                break;
            case 5:
                말 = "블루밍 포레스트에 #e#b오전 10시#n부터 #e#b다음날 오전 2시#k#n 사이,\r\n"
                말 += "#b#e매 시 30분마다#k#n 번갈아 내리는 햇살과 바람을 만끽하면~?\r\n\r\n"
                말 += "잠시 동안 아주 #r특별한 능력치#k가 생긴다고 해여~!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "제거한 잡코만큼 내 #b#e블루밍 코인#k#n을 나눠 줄게.\r\n"
                말 += "잡초는 #r#e1시간마다 1개#k#n씩 뽑을 수 있어.\r\n\r\n\r\n"
                말 += "#r※ 유니온 캐릭터 1명이 1시간에 1개의 잡초를 제거합니다."
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                말 = "#b제한 시간 2분#k 동안 #b꽃 덤불 몬스터를 처치#k하면\r\n"
                말 += "획득한 점수에 따라 #b#e스피릿 포이트#k#n를 줄 것이담.\r\n"
                qm.sendNextPrevS(말, 4, 9062506);
                break;
            case 8:
                말 = "#b제한 시간 5분#k 동안 #b총 5단계#k로 구성된 코스를 완주하면 끝이야.\r\n"
                말 += "도착한 순서와 달성률에 따라 #i4310310# #b블루밍 코인#k을 줄 거야.\r\n"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 4) {
        switch (sel) {
            case 0:
                말 = "이제 막 깊은 잠에서 깨 피어나기 시작한 #r꽃#k들과\r\n"
                말 += "#r신비한 정령들#k이 가득한 #r#e<블루밍 포레스트>#k#n !!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "꽃들이 피어날 때마다 보답으로 #r특별한 보상#k과 함께!\r\n"
                말 += "#r블루밍 축복#k을 한 번씩 내려드릴게여!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "헤헤~ 나무의 정령들은 무뚝뚝해 보이지만\r\n"
                말 += "정말 #r아낌없이#k 주는 친구들이라니까여~?\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 3:
                말 = "보스를~ 처치하고~ #b#e위시 코인#k#n 찾아~ 내게오면~\r\n"
                말 += "특.별.한! 물.건! 줄.게~ 예!\r\n\r\n"
                말 += "#e※위시 코인샵 이용 기간\r\n"
                말 += "#r6월 20일 오후 11시 59분까지\r\n\r\n"
                말 += "#e※위시 코인 획득 가능 기간\r\n"
                말 += "#r4월 29일 자정 ~ 6월 16일 오후 11시 59분까지\r\n\r\n"
                말 += "#e※위시 코인 획득 제한량\r\n"
                말 += "주간 최대 #r400개#k까지#n\r\n\r\n"
                말 += "(주간 위시 코인 획득 기록은 #r매주 목요일 자정#k에 초기화)"
                qm.sendNextPrevS(말, 4, 9062536);
                break;
            case 4:
                말 = "저의 능력은 #b#e꽃씨 뿌리기#k#n예요!\r\n\r\n"
                말 += "꽃씨를 펑!펑! 뿌리고!\r\n"
                말 += "#i4310310# #b블루밍 코인#k도 #e#r20개#k#n 드릴게요!\r\n"
                qm.sendNextPrevS(말, 4, 9062533);
                break;
            case 5:
                말 = "#r#e따스한 봄 햇살#k#n은 10시 30분부터 매 #r#e짝수 시간#k#n에 비추고~\r\n\r\n"
                말 += "#e- 따스한 봄 햇살 : 30분간 #r경험치 15%#k 증가"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "메이플 유니온에 소속된 캐릭터를 많이 데려와서 잡초 제거를 빠르게 하면 물론 좋겠지만...\r\n\r\n"
                말 += "#e그럼 너무 힘들겠지?"
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                말 = "획득한 스피릿 포인트는 #r#e성장의 비약#k#n과 교환해 주겠담.\r\n"
                qm.sendNextPrevS(말, 4, 9062506);
                break;
            case 8:
                말 = "#b최대 3등#k까지는 더 많은 #i4310310# #b블루밍 코인#k을 줄게."
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 5) {
        switch (sel) {
            case 0:
                말 = "메이플 월드에 #b아름다운 봄#k이 찾아온 것은\r\n"
                말 += "모두 #b#e#h ##n#k님 덕분이에여!!!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "#b블루밍 축복#k을 잘 모아서 사용하면?\r\n"
                말 += "#e#b원하는 스킬들을 강화#k#n할 수 있으니 더 강해진 용사님?!\r\n\r\n"
                말 += "더 많은 햇살을 보내주실 거라고 믿어여~!!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "용사님이시라면 #r좋은 물건#k들을 많이 줄 테니,\r\n"
                말 += "한번 꼭 둘러보세여~!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 3:
                말 = "강한 보스일수록~ 숨겨둔 #b#e위시 코인#k#n도 많다는 걸 알아둬~\r\n"
                말 += "자세한 내용은 #b나에게 찾아와#k 다시 말.을.걸.어.줘. 예!"
                qm.sendNextPrevS(말, 4, 9062536);
                break;
            case 4:
                말 = "들꽃의 정령과 #b#e'블루밍 플라워'#k#n를 통해 다른 꽃이 깨어나면 함께하는 꽃의 정령이 늘어날 거예요!\r\n\r\n"
                말 += "#r※ 잠들어 있는 꽃이 깨어나면 '플로라 블레싱'의 효과가 강화됩니다.\r\n"
                qm.sendNextPrevS(말, 4, 9062533);
                break;
            case 5:
                말 = "#b#e시원한 봄바람#k#n은 11시 30분부터 매 #b#e홀수 시간#k#n에 불러오져!\r\n\r\n"
                말 += "#e- 시원한 봄바람 : 30분간 #b올스탯 15#k 증가,\r\n"
                말 += "#e                    : #b공격력/마력 15#k 증가,\r\n"
                말 += "#e                    : #b보스 몬스터 데미지 15%#k 증가,\r\n"
                말 += "#e                    : #b방어율 무시 15%#k 증가,\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "날 도와줄 수 있는 만큼만 부탁할게!\r\n"
                말 += "#b#e유니온 레벨#k#n이 높다면 조금 마음 편히 부탁할 수 있겠어.\r\n\r\n\r\n"
                말 += "#r※ 유니온 레벨이 높을수록 잡초를 제거하는 캐릭터 수가 증가합니다."
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                말 = "강한 친구! 설명은 여기까지담.\r\n"
                말 += "친구들을 도와주고 싶으면 #b나를 찾아와#k 말을 걸어달람."
                qm.sendNextPrevS(말, 4, 9062506);
                break;
            case 8:
                말 = "랭킹에 들지 못한 친구들도 충분히 많은 #i4310310# #b블루밍 코인#k을 준비했으니 포기하지 말고 끝까지 완주해줘.\r\n"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 6) {
        switch (sel) {
            case 0:
                말 = "밝은 빛이 사라지면 피어난 꽃들은 다시 잠에 들 거예여.\r\n"
                말 += "꽃이 지기 전까지 #r#e<블루밍 포레스트>#k#n에서 #r포근한 봄바람#k을 만끽하세여~!!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "그렇지만 기분 좋은 꿈을 꾸고있을 때 억지로 깨우면 친구들이 싫어할 수도 있겠져?\r\n\r\n"
                말 += "잠든 꽃들을 깨워 활짝 피우는 건 #r#e하루에 한 번#k#n이면 충분할것 같아여~!\r\n\r\n\r\n"
                말 += "#r※ 블루밍 플라워는 월드당 하루에 한 번만 진행할 수\r\n 있습니다."
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "그럼 신비한 정령들과 함께 블루밍 포레스트의 따스한 봄을 즐겨보세여!!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 3:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 4:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 5:
                말 = "#r따스한 햇살#k과 #b시원한 바람#k이 지나가는 #r#e<블루밍 모먼트>#k#n !!\r\n"
                말 += "용사님도 이곳에서 함께 만끽해여~!!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "너무 많이 부탁하면 미안하니까...\r\n"
                말 += "#b#e유니온 레벨#k#n에 따라 이 정도...?\r\n\r\n\r\n"
                말 += "#e#r[잡초 제거 캐릭터 수]#n\r\n"
                말 += "- 유니온 레벨 2000 미만 : 1명\r\n"
                말 += "- 유니온 레벨 3500 미만 : 2명\r\n"
                말 += "- 유니온 레벨 5000 미만 : 3명\r\n"
                말 += "- 유니온 레벨 6500 미만 : 4명\r\n"
                말 += "- 유니온 레벨 8000 미만 : 5명\r\n"
                말 += "- 유니온 레벨 8000 이상 : 6명\r\n"
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062506);
                break;
            case 8:
                말 = "#b#e블루밍 레이스#k#n는 #e#b오전 10시#k#n부터 #e#b자정 전#k#n까지 #e#b매시 15분, 45분#k#n에 머리 위 #e#b초대장#k#n을 통해 입장할 수 있어.\r\n"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 7) {
        switch (sel) {
            case 0:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 1:
                말 = "참! 아직 햇살을 받을 준비가 되지 않은 꽃들도 있어여!\r\n"
                말 += "#b#e매주 3개의 꽃봉오리#k#n가 햇빛을 받고 싶어서 나뭇잎 사이로 얼굴을 내밀 거예여!\r\n\r\n\r\n"
                말 += "#r※ 블루밍 플라워는 매주 목요일 오전 10시에 3개씩\r\n 해금됩니다."
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                말 = "#e[블루밍 코인샵 이용 기간]#n\r\n"
                말 += "- 2021년 6월 20일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 4:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 5:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "그리고... 잡초 바구니는 크지 않으니까 가끔 들려서 비워줘!\r\n\r\n\r\n"
                말 += "#r※ 잡초 제거는 최대 #e24시간까지#n만 가능하며, 중간에\r\n 보상을 수령하는 경우 0부터 다시 시작합니다.\r\n"
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 7:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 8:
                말 = "으헤헷, 알겠지? 그러니까 꼭 #b#e초대장#k#n을 수락해줘!"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 8) {
        switch (sel) {
            case 0:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 1:
                말 = "그리고 #r#e세 번째 꽃#k#n이 필 때마다 #b#e더 좋은 보상#k#n과\r\n"
                말 += "#b#e더 많은 스킬 포인트#k#n를 드릴게여!\r\n\r\n\r\n"
                말 += "#r※ 블루밍 플라워 진행 시 1/1/3개의 블루밍 스킬 포인트를 받을 수 있습니다."
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 2:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 5:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
            case 6:
                말 = "도와줘서 고마워!\r\n"
                말 += "꽃들이 모두 행복했으면 좋겠다.\r\n"
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 8:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 5월 19일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062509);
                break;
        }
    } else if (status == 9) {
        switch (sel) {
            case 1:
                말 = "화면 왼쪽 #b별 알람이 아이콘#k 또는 #b저#k를 통해 언제든\r\n"
                말 += "#e#b<블루밍 플라워>#k#n 진행 상태를 확인하실 수 있어여!"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062516);
                break;
            case 8:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
        }
    } else if (status == 10) {
        switch (sel) {
            case 1:
                말 = "흥흥~ 따스한 햇살~ 다른 꽃 정령 친구들도 어서 깨어나 함께 부드러운 봄바람을 만끽하면 좋겠어여~!!!\r\n"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
            case 6:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
        }
    } else if (status == 11) {
        switch (sel) {
            case 1:
                말 = "#e[이벤트 기간]#n\r\n"
                말 += "- 2021년 6월 16일 23시 59분까지"
                qm.sendNextPrevS(말, 4, 9062508);
                break;
        }
    } else if (status == 12) {
        switch (sel) {
            case 1:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(),9062508, 100791);
                break;
        }
    }
}
