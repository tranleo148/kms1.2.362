function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	{
		if(M == 0 && T == 5 && S == -1)
		{
			fw = new java.io.FileWriter("macro_test.txt", true);
			fw.write(""+new Date()+"	ID:"+cm.getPlayer().getId()+"	NAME:"+cm.getPlayer().getName()+"	ESC입력	MAP:"+cm.getPlayer().getMapId()+"\r\n");
			fw.close();
			cm.warp(100000000, 0);
		}
		cm.dispose();
		return;
	}
	else
	{

		St++;
	}

	if(St == 0)
	{
		starttime = new Date().getTime();

		//난수 설정 (1000~9999);
		Math1 = Math.floor(Math.random() * 9000) + 1000;

		//난수 저장
		cm.getPlayer().addKV("MACRO_TEST", Math1);

		toArray = cm.getPlayer().getV("MACRO_TEST") + "";

		arr = [];
		arr = toArray.split("");

		cm.getPlayer().dropMessage(5, "깜짝 퀴즈가 등장했습니다. ESC나 대화 그만하기를 눌러 퀴즈를 종료하면 마을로 강제 이동됩니다.");

		def_v = Math.floor(Math.random() * 9000) + 1000;
		selStr = "깜짝 퀴즈! 아래에 보이는 숫자는 몇 인가요?#fn나눔고딕 Extrabold#\r\n\r\n#e";

		for(i = 0; i < arr.length; i++)
		{
			if(i > 6)
			break;

			fontsize = Math.floor(Math.random() * 20) + 10;

			colorlist = ["#r", "#b", "#k", "#d"];
			fontcolor = colorlist[Math.floor(Math.random() * colorlist.length)];

			selStr += "#fs";
			selStr += fontsize;
			selStr += "#";

			selStr += fontcolor; 

			selStr += arr[i];
		}

		cm.sendGetNumber(selStr, def_v, 0, 9999);
	}

	else if(St == 1)
	{
		text = S;
		endtime = new Date().getTime();
		mapid = cm.getPlayer().getMapId();

		if(text == toArray) // 정답이면
		{
			cm.sendOkS("오! 시력이 좋으신데요? 퀴즈에 참여해 주셔서 감사합니다.", 4, 2007);
			cm.dispose();
			type = 0; // 성공
		}
		else
		{
			if(def_v == text)
			{
				type = 1; // 실패인데 그대로 엔터
			}	
			else
			{
				type = 2; // 실패인데 숫자는 바꿔서 냄
			}
	
			if (type == 1) {
			    cm.warp(100000000, 0);
			}
			cm.sendOkS("이런, 시력이 좋지 않으시네요. 다음번엔 더 잘 하실 수 있으시죠?", 4, 2007);
			cm.dispose();
		}

		fw = new java.io.FileWriter("macro_test.txt", true);
		txt = type == 0 ? "성공" : type == 1 ? "엔터" : "실패";
		fw.write(""+new Date()+"	ID:"+cm.getPlayer().getId()+"	NAME:"+cm.getPlayer().getName()+"	"+txt+"	MAP:"+ mapid +"	"+(endtime-starttime)/1000+"초	출력값:"+toArray+"	입력값:"+text+"\r\n");
		fw.close();
	}
}

