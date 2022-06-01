var inz = [[2049501, 50], [2049701, 50], [2048701,50]];

function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	{	
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;
	else
	St--;

	if(St == 0)
	{
		txt = "혹시 #b#i2430481:# #t2430481##k을 가지고 있으신가요?\r\n큐브 조각을 모아서 새로운 아이템을 만들어드리고 있는데 해보시겠는지…\r\n#l";
		for(i = 0; i < inz.length; i++)
		{
			txt += "#L"+ i +"##r("+inz[i][1]+"개 필요)#b #i"+inz[i][0]+":# #t"+inz[i][0]+"#\r\n";
		}
		cm.sendSimple(txt);
	}

	else if(St == 1)
	{
		if(!cm.haveItem(2430481, inz[S][1]))
		{
			cm.getPlayer().dropMessage(1, "마스터 미라클 큐브 조각이 "+Number(inz[S][1] - cm.itemQuantity(2430481))+"개 부족합니다");
			cm.dispose();
			return;
		}

		if(!cm.canHold(inz[S][0]))
		{
			cm.getPlayer().dropMessage(1, "인벤토리에 여유 슬롯이 없습니다.");
			cm.dispose();
			return;
		}

		cm.gainItem(2430481, -inz[S][1]);
		cm.gainItem(inz[S][0], 1);
		cm.getPlayer().dropMessage(1, "교환이 완료되었습니다.");
		cm.dispose();
	}
}