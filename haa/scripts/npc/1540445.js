var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
       Packages.client.BattleReverse.addQueue(cm.getPlayer());
	   cm.dispose();
    }
}
/*
enum $1B090E5F5CD629894C98A4A98568F779 //1.2.29
{
  InGameDirectionEvent_ForcedAction = 0x0,
  InGameDirectionEvent_Delay = 0x1,
  InGameDirectionEvent_EffectPlay = 0x2,
  InGameDirectionEvent_ForcedInput = 0x3,
  InGameDirectionEvent_PatternInputRequest = 0x4,
  InGameDirectionEvent_CameraMove = 0x5,
  InGameDirectionEvent_CameraOnCharacter = 0x6,
  InGameDirectionEvent_CameraZoom = 0x7,
  InGameDirectionEvent_CameraReleaseFromUserPoint = 0x8,
  InGameDirectionEvent_VansheeMode = 0x9,
  InGameDirectionEvent_FaceOff = 0xA,
  InGameDirectionEvent_Monologue = 0xB,
  InGameDirectionEvent_MonologueScroll = 0xC,
  InGameDirectionEvent_AvatarLookSet = 0xD,
  InGameDirectionEvent_RemoveAdditionalEffect = 0xE,
  InGameDirectionEvent_ForcedMove = 0xF,
  InGameDirectionEvent_ForcedFlip = 0x10,
  InGameDirectionEvent_InputUI = 0x11,
};
*/