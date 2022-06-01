importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);

importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var sel = -1;

var banitem = [1012029, 1012038, 1012041, 1012042, 1012043, 1012049, 1012051, 1012057, 1012081, 1012090, 1012099, 1012100, 1012112, 1012113, 1012114, 1012131, 1012133, 1012147, 1012159, 1012166, 1012179, 1012180, 1012208, 1012253, 1012275, 1012298, 1012315, 1012390, 1012413, 1012462, 1012474, 1012486, 1012487, 1012488, 1012509, 1012510, 1012511, 1012534, 1012552, 1012572, 1012603, 1012609, 1012619, 1012623, 1022046, 1022047, 1022048, 1022057, 1022065, 1022066, 1022084, 1022085, 1022087, 1022090, 1022095, 1022104, 1022121, 1022122, 1022173, 1022194, 1022201, 1022227, 1022249, 1022250, 1022257, 1022258, 1022266, 1022269, 1022270, 1022275, 1032071, 1032072, 1032073, 1032074, 1032138, 1032145, 1032175, 1032204, 1032233, 1032234, 1032255, 1032260, 1032262, 1000024, 1000027, 1000028, 1000041, 1000043, 1000045, 1000046, 1000061, 1000071, 1000081, 1000084, 1000087, 1001034, 1001039, 1001040, 1001041, 1001058, 1001061, 1001069, 1001088, 1001094, 1001105, 1001108, 1002469, 1002470, 1002472, 1002489, 1002490, 1002542, 1002543, 1002544, 1002545, 1002548, 1002549, 1002555, 1002565, 1002567, 1002570, 1002582, 1002583, 1002593, 1002594, 1002596, 1002605, 1002609, 1002653, 1002654, 1002672, 1002673, 1002674, 1002678, 1002679, 1002692, 1002693, 1002694, 1002695, 1002696, 1002697, 1002700, 1002701, 1002703, 1002704, 1002705, 1002706, 1002725, 1002726, 1002727, 1002734, 1002741, 1002752, 1002754, 1002759, 1002760, 1002761, 1002775, 1002811, 1002823, 1002831, 1002834, 1002835, 1002836, 1002837, 1002844, 1002847, 1002876, 1002882, 1002886, 1002887, 1002888, 1002889, 1002890, 1002891, 1002937, 1002941, 1002942, 1002956, 1002957, 1002961, 1002967, 1002968, 1002975, 1002983, 1002984, 1002985, 1002987, 1002998, 1002999, 1003000, 1003001, 1003005, 1003014, 1003015, 1003022, 1003044, 1003047, 1003053, 1003054, 1003059, 1003060, 1003074, 1003079, 1003080, 1003082, 1003101, 1003121, 1003130, 1003144, 1003145, 1003146, 1003148, 1003161, 1003182, 1003185, 1003186, 1003187, 1003202, 1003203, 1003208, 1003238, 1003241, 1003247, 1003263, 1003272, 1003377, 1003386, 1003387, 1003403, 1003404, 1003417, 1003459, 1003462, 1003463, 1003482, 1003483, 1003484, 1003485, 1003486, 1003487, 1003489, 1003490, 1003504, 1003505, 1003506, 1003508, 1003509, 1003510, 1003516, 1003517, 1003518, 1003531, 1003532, 1003533, 1003536, 1003559, 1003560, 1003594, 1003626, 1003643, 1003654, 1003655, 1003656, 1003657, 1003658, 1003666, 1003667, 1003670, 1003671, 1003672, 1003673, 1003699, 1003713, 1003730, 1003735, 1003742, 1003743, 1003749, 1003759, 1003760, 1003761, 1003775, 1003790, 1003802, 1003804, 1003815, 1003825, 1003826, 1003827, 1003829, 1003830, 1003843, 1003861, 1003889, 1003897, 1003917, 1003918, 1003919, 1003934, 1003936, 1003948, 1003949, 1003950, 1003962, 1003963, 1003964, 1003975, 1004001, 1004015, 1004016, 1004018, 1004024, 1004040, 1004041, 1004042, 1004043, 1004044, 1004045, 1004046, 1004047, 1004073, 1004074, 1004090, 1004091, 1004092, 1004093, 1004094, 1004106, 1004108, 1004110, 1004111, 1004113, 1004122, 1004123, 1004125, 1004126, 1004136, 1004137, 1004157, 1004178, 1004190, 1004191, 1004193, 1004200, 1004201, 1004202, 1004203, 1004204, 1004205, 1004209, 1004252, 1004253, 1004282, 1004284, 1004285, 1004295, 1004296, 1004298, 1004299, 1004301, 1004302, 1004303, 1004304, 1004305, 1004307, 1004308, 1004309, 1004310, 1004311, 1004313, 1004314, 1004315, 1004316, 1004317, 1004318, 1004319, 1004320, 1004321, 1004322, 1004323, 1004324, 1004325, 1004326, 1004332, 1004386, 1004393, 1004394, 1004395, 1004396, 1004397, 1004398, 1004399, 1004400, 1004401, 1004402, 1004406, 1004413, 1004417, 1004418, 1004419, 1004428, 1004438, 1004439, 1004440, 1004448, 1004455, 1004456, 1004461, 1004462, 1004463, 1004471, 1004478, 1004479, 1004482, 1004483, 1004499, 1004500, 1004501, 1004504, 1004505, 1004506, 1004508, 1004515, 1004534, 1004535, 1004538, 1004539, 1004544, 1004545, 1004546, 1004547, 1004548, 1004559, 1004560, 1004561, 1004562, 1004563, 1004570, 1004571, 1004600, 1004601, 1004612, 1004613, 1004614, 1004635, 1004638, 1004659, 1004660, 1004661, 1004662, 1004671, 1004672, 1004673, 1004706, 1004708, 1004712, 1004713, 1004720, 1004721, 1004722, 1004733, 1004734, 1004757, 1004758, 1004759, 1004760, 1004762, 1004787, 1004788, 1004790, 1004799, 1004800, 1004801, 1004802, 1004803, 1004804, 1004805, 1004806, 1004825, 1004826, 1004827, 1004828, 1004829, 1004830, 1004831, 1004832, 1004833, 1004834, 1004835, 1004839, 1004840, 1004841, 1004842, 1004843, 1004853, 1004854, 1004858, 1004859, 1004860, 1004862, 1004863, 1004874, 1004876, 1004877, 1004878, 1004879, 1004880, 1004889, 1004890, 1004909, 1004912, 1004916, 1004925, 1004932, 1004933, 1004934, 1004936, 1004937, 1004938, 1004939, 1004940, 1004941, 1004942, 1004945, 1004946, 1004950, 1004951, 1004952, 1004953, 1004961, 1004964, 1004981, 1004982, 1004983, 1004984, 1004996, 1004997, 1004998, 1005003, 1005004, 1005006, 1005007, 1005008, 1005009, 1005010, 1005011, 1005012, 1005013, 1100000, 1102098, 1102144, 1102150, 1102156, 1102162, 1102171, 1102208, 1102209, 1102210, 1102213, 1102214, 1102216, 1102217, 1102218, 1102220, 1102221, 1102229, 1102230, 1102233, 1102238, 1102240, 1102243, 1102252, 1102253, 1102257, 1102259, 1102291, 1102296, 1102300, 1102301, 1102318, 1102336, 1102343, 1102344, 1102373, 1102374, 1102387, 1102388, 1102391, 1102392, 1102396, 1102419, 1102423, 1102424, 1102425, 1102426, 1102427, 1102428, 1102429, 1102430, 1102431, 1102432, 1102433, 1102434, 1102435, 1102436, 1102437, 1102438, 1102461, 1102465, 1102486, 1102488, 1102508, 1102513, 1102549, 1102555, 1102604, 1102605, 1102613, 1102614, 1102622, 1102631, 1102640, 1102641, 1102642, 1102651, 1102652, 1102653, 1102654, 1102655, 1102658, 1102668, 1102673, 1102682, 1102683, 1102684, 1102697, 1102705, 1102723, 1102724, 1102726, 1102756, 1102779, 1102780, 1102781, 1102782, 1102783, 1102784, 1102785, 1102786, 1102787, 1102788, 1102798, 1102800, 1102801, 1102808, 1102818, 1102820, 1102827, 1102835, 1102839, 1102842, 1102860, 1102868, 1102884, 1102885, 1102886, 1102908, 1102910, 1102912, 1102913, 1102927, 1102937, 1102938, 1102939, 1102953, 1102954, 1102955, 1102956, 1102957, 1102958, 1102966, 1102968, 1102969, 1102970, 1102971, 1102972, 1102975, 1102978, 1102982, 1102983, 1102984, 1102991, 1102995, 1103000, 1103003, 1103004, 1103007, 1103008, 1103009, 1103012, 1103026, 1103027, 1103028, 1103032, 1103033, 1103034, 1103037, 1103038, 1103039, 1103040, 1103042, 1040139, 1040140, 1040141, 1040143, 1040148, 1040194, 1040195, 1040196, 1041140, 1041143, 1041144, 1041146, 1041147, 1041193, 1041196, 1041197, 1041198, 1042064, 1042065, 1042159, 1042166, 1042171, 1042176, 1042186, 1042187, 1042189, 1042192, 1042208, 1042220, 1042228, 1042229, 1042232, 1042251, 1042252, 1042259, 1042263, 1042266, 1042277, 1042291, 1042293, 1042311, 1042320, 1042329, 1042332, 1042333, 1042336, 1042338, 1042339, 1042343, 1042345, 1042346, 1042348, 1042350, 1042354, 1042355, 1042356, 1042361, 1042367, 1042375, 1042376, 1042378, 1048000, 1048001, 1048002, 1049000, 1102380, 1081006, 1082165, 1082251, 1082253, 1082255, 1082267, 1082274, 1082407, 1082408, 1082421, 1082422, 1082423, 1082448, 1082495, 1082500, 1082502, 1082503, 1082504, 1082511, 1082517, 1082523, 1082541, 1082542, 1082550, 1082554, 1082558, 1082561, 1082563, 1082564, 1082581, 1082585, 1082591, 1082592, 1082623, 1082624, 1082631, 1082641, 1082685, 1082689, 1082692, 1082694, 1082702, 1082712, 1082713, 1082714, 1082715, 1082717, 1082718, 1050121, 1050122, 1050124, 1050129, 1050140, 1050143, 1050145, 1050157, 1050158, 1050159, 1050160, 1050170, 1050171, 1050179, 1050220, 1050221, 1050241, 1050256, 1050314, 1050315, 1050360, 1050366, 1050387, 1050401, 1050418, 1050419, 1051129, 1051130, 1051138, 1051145, 1051146, 1051148, 1051161, 1051169, 1051174, 1051185, 1051195, 1051196, 1051198, 1051206, 1051208, 1051212, 1051221, 1051231, 1051270, 1051271, 1051290, 1051312, 1051332, 1051333, 1051385, 1051386, 1051425, 1051430, 1051436, 1051457, 1051470, 1051487, 1051488, 1052054, 1052060, 1052061, 1052062, 1052063, 1052064, 1052065, 1052066, 1052069, 1052070, 1052073, 1052079, 1052082, 1052085, 1052086, 1052089, 1052135, 1052136, 1052147, 1052170, 1052174, 1052192, 1052198, 1052199, 1052204, 1052205, 1052206, 1052207, 1052210, 1052211, 1052212, 1052213, 1052214, 1052218, 1052225, 1052229, 1052232, 1052233, 1052246, 1052268, 1052286, 1052292, 1052293, 1052294, 1052296, 1052298, 1052306, 1052324, 1052329, 1052330, 1052331, 1052338, 1052339, 1052340, 1052349, 1052367, 1052368, 1052369, 1052370, 1052372, 1052373, 1052408, 1052410, 1052411, 1052418, 1052419, 1052421, 1052423, 1052425, 1052426, 1052435, 1052439, 1052440, 1052442, 1052443, 1052445, 1052446, 1052447, 1052448, 1052449, 1052455, 1052458, 1052459, 1052474, 1052503, 1052531, 1052550, 1052551, 1052552, 1052554, 1052571, 1052574, 1052575, 1052594, 1052597, 1052602, 1052604, 1052610, 1052634, 1052643, 1052644, 1052654, 1052656, 1052657, 1052660, 1052662, 1052663, 1052668, 1052675, 1052676, 1052677, 1052678, 1052679, 1052682, 1052684, 1052685, 1052724, 1052725, 1052726, 1052727, 1052728, 1052746, 1052747, 1052749, 1052750, 1052771, 1052773, 1052774, 1052779, 1052780, 1052781, 1052782, 1052811, 1052812, 1052837, 1052838, 1052842, 1052843, 1052844, 1052870, 1052871, 1052872, 1052873, 1052874, 1052876, 1052891, 1052894, 1052895, 1052899, 1052901, 1052902, 1052903, 1052904, 1052910, 1052911, 1052916, 1052917, 1052920, 1052921, 1052922, 1052924, 1052925, 1052926, 1052946, 1052947, 1052949, 1052951, 1052955, 1052956, 1052957, 1052958, 1052959, 1052960, 1052961, 1052965, 1052966, 1052967, 1052999, 1053000, 1053001, 1053014, 1053016, 1053017, 1053022, 1053023, 1053024, 1053025, 1053031, 1053032, 1053041, 1053042, 1053045, 1053046, 1053047, 1053048, 1053051, 1053052, 1053053, 1053056, 1053057, 1053058, 1053059, 1053060, 1053061, 1053083, 1053084, 1053085, 1053086, 1053087, 1053088, 1053089, 1053090, 1053091, 1053092, 1053093, 1053094, 1053099, 1053103, 1053104, 1053105, 1053106, 1053107, 1053109, 1053110, 1053115, 1053116, 1053117, 1053118, 1053119, 1053124, 1053125, 1053126, 1053127, 1053130, 1053131, 1053132, 1053133, 1053134, 1053138, 1053142, 1053144, 1053145, 1053146, 1053148, 1053155, 1053156, 1053157, 1053162, 1053163, 1053164, 1053168, 1053169, 1053170, 1053171, 1053172, 1053173, 1053174, 1053175, 1053176, 1053177, 1053180, 1053183, 1053184, 1053195, 1053196, 1053197, 1053198, 1053199, 1053200, 1053201, 1053202, 1053207, 1053208, 1053209, 1053210, 1053217, 1053218, 1053219, 1053220, 1053221, 1053222, 1060122, 1060123, 1060125, 1060139, 1060179, 1060181, 1060187, 1060188, 1060189, 1061143, 1061144, 1061145, 1061147, 1061204, 1061207, 1061210, 1061211, 1061212, 1061213, 1062049, 1062050, 1062117, 1062163, 1062170, 1062173, 1062188, 1062189, 1062204, 1062210, 1062211, 1062217, 1062218, 1062220, 1062222, 1062223, 1062228, 1062235, 1062236, 1062244, 1062245, 1112115, 1112127, 1112129, 1112130, 1112131, 1112132, 1112134, 1112136, 1112137, 1112152, 1112157, 1112159, 1112162, 1112163, 1112164, 1112166, 1112171, 1112172, 1112177, 1112179, 1112181, 1112190, 1112193, 1112194, 1112195, 1112196, 1112197, 1112198, 1112199, 1112237, 1112240, 1112244, 1112264, 1112265, 1112269, 1112271, 1112274, 1112275, 1112276, 1112278, 1112283, 1112284, 1112289, 1112291, 1112294, 1112724, 1112757, 1112808, 1115003, 1115006, 1115007, 1115008, 1115009, 1115010, 1115011, 1115015, 1115022, 1115023, 1115024, 1115025, 1115026, 1115027, 1115029, 1115030, 1115031, 1115032, 1115034, 1115038, 1115039, 1115040, 1115044, 1115046, 1115047, 1115048, 1115111, 1115112, 1115113, 1115114, 1115115, 1115116, 1115118, 1115119, 1115120, 1115121, 1115123, 1115127, 1115128, 1115129, 1115133, 1115135, 1115136, 1115137, 1070007, 1070008, 1070014, 1070015, 1070018, 1070031, 1070068, 1070075, 1071019, 1071024, 1071025, 1071048, 1071084, 1071092, 1072254, 1072255, 1072270, 1072271, 1072277, 1072348, 1072374, 1072380, 1072385, 1072386, 1072387, 1072388, 1072389, 1072392, 1072393, 1072396, 1072397, 1072398, 1072404, 1072405, 1072406, 1072408, 1072410, 1072417, 1072437, 1072439, 1072440, 1072456, 1072464, 1072465, 1072466, 1072467, 1072468, 1072469, 1072470, 1072482, 1072495, 1072507, 1072509, 1072517, 1072520, 1072532, 1072536, 1072537, 1072609, 1072627, 1072628, 1072631, 1072637, 1072647, 1072648, 1072649, 1072650, 1072651, 1072652, 1072658, 1072662, 1072676, 1072680, 1072681, 1072708, 1072729, 1072749, 1072750, 1072770, 1072771, 1072772, 1072773, 1072779, 1072780, 1072781, 1072791, 1072800, 1072807, 1072812, 1072813, 1072820, 1072839, 1072840, 1072848, 1072854, 1072855, 1072863, 1072864, 1072865, 1072869, 1072873, 1072875, 1072878, 1072879, 1072880, 1072881, 1072883, 1072884, 1072909, 1072910, 1072913, 1072916, 1072917, 1072918, 1072919, 1072920, 1072921, 1072922, 1072923, 1072924, 1072925, 1072926, 1072942, 1072944, 1072945, 1072949, 1072950, 1072979, 1072980, 1072999, 1073009, 1073014, 1073022, 1073024, 1073027, 1073036, 1073038, 1073040, 1073046, 1073047, 1073051, 1073052, 1073055, 1073060, 1073061, 1073062, 1073074, 1073080, 1073096, 1073106, 1073107, 1073108, 1073129, 1073132, 1073133, 1073134, 1073135, 1073145, 1073150, 1073153, 1073154, 1073155, 1073157, 1073167, 1073168, 1073177, 1073178, 1073179, 1073180, 1073181, 1073182, 1073185, 1073188, 1073189, 1073192, 1073195, 1073196, 1073203, 1073204, 1073205, 1073215, 1073216, 1073217, 1073222, 1073223, 1073228, 1073229, 1073230, 1701000, 1702051, 1702052, 1702053, 1702054, 1702071, 1702075, 1702076, 1702077, 1702089, 1702094, 1702095, 1702097, 1702101, 1702102, 1702104, 1702105, 1702113, 1702116, 1702117, 1702126, 1702128, 1702129, 1702130, 1702131, 1702132, 1702135, 1702138, 1702139, 1702140, 1702143, 1702148, 1702152, 1702153, 1702156, 1702157, 1702161, 1702163, 1702164, 1702171, 1702175, 1702178, 1702183, 1702184, 1702185, 1702186, 1702187, 1702189, 1702193, 1702197, 1702198, 1702200, 1702201, 1702204, 1702208, 1702209, 1702210, 1702213, 1702214, 1702215, 1702218, 1702219, 1702220, 1702222, 1702223, 1702224, 1702226, 1702228, 1702230, 1702231, 1702232, 1702234, 1702235, 1702236, 1702239, 1702240, 1702246, 1702249, 1702250, 1702251, 1702258, 1702259, 1702260, 1702261, 1702262, 1702263, 1702264, 1702266, 1702268, 1702274, 1702276, 1702278, 1702280, 1702281, 1702282, 1702283, 1702284, 1702285, 1702286, 1702295, 1702296, 1702301, 1702302, 1702303, 1702305, 1702310, 1702311, 1702324, 1702333, 1702335, 1702341, 1702344, 1702345, 1702346, 1702351, 1702355, 1702356, 1702359, 1702360, 1702371, 1702372, 1702374, 1702376, 1702379, 1702380, 1702381, 1702392, 1702393, 1702401, 1702402, 1702403, 1702404, 1702407, 1702408, 1702409, 1702417, 1702459, 1702460, 1702462, 1702472, 1702474, 1702475, 1702479, 1702489, 1702491, 1702492, 1702499, 1702506, 1702507, 1702509, 1702510, 1702522, 1702526, 1702556, 1702557, 1702559, 1702560, 1702566, 1702567, 1702572, 1702579, 1702583, 1702588, 1702589, 1702600, 1702601, 1702602, 1702603, 1702604, 1702605, 1702606, 1702611, 1702626, 1702630, 1702633, 1702635, 1702640, 1702660, 1702671, 1702675, 1702682, 1702684, 1702685, 1702686, 1702692, 1702693, 1702697, 1702698, 1702699, 1702700, 1702710, 1702711, 1702712, 1702713, 1702714, 1702719, 1702722, 1702727, 1702733, 1702740, 1702742, 1702748, 1702750, 1702752, 1702753, 1702758, 1702764, 1702765, 1702773, 1702776, 1702777, 1702778, 1702783, 1702784, 1702785, 1004680, 1004681, 1012008, 1703979, 1342087, 1112911, 1112912, 11000298, 1000299, 1000300, 1000301, 1000302, 1000303, 1000304, 1000305, 1000306, 1000307, 1000308, 1000309, 1000310, 1000311, 1000312, 1000313, 1000314, 1000315, 1000316, 1000317, 1000318, 1000319, 1000320, 1000321, 1000322, 1000323, 1000324, 1000325, 1000326, 1000327, 1000328, 1000329, 1000330, 1000331, 1000332, 1000333, 1000334, 1000335, 1000336, 1000337, 1000338, 1000339, 1000340, 1000341, 1000342, 1000343, 1000344, 1000345, 1000346, 1000347, 1000348, 1000349, 1000350, 1000351, 1000352, 1000353, 1000354, 1000355, 1000356, 1000357, 1000358, 1000359, 1000360, 1000361, 1000362, 1000363, 1000364, 1000365, 1000421, 1000422, 1000423, 1000424, 1000425, 1000426, 1000427, 1000428, 1000429, 1000430, 1000431, 1000432, 1000433, 1000434, 1000435, 1000436, 1000437, 1000438, 1000439, 1000440, 1000441, 1000442, 1000443, 1000444, 1000445, 1000446, 1000447, 1000448, 1000449, 1000450, 1000451, 1000452, 1000454, 1000455, 1000456, 1000457, 1000458, 1000459, 1000460, 1000461, 1052715, 1052716, 1052720, 1052722, 1012378, 1012380, 1012381, 1012382, 1050800, 1050801, 1050803, 1012900, 1012901, 1012902, 1000462, 1000463, 1000464, 1000465, 1000466, 1000467, 1000468, 1000469, 1000470, 1000471, 1000472, 1000473, 1000474, 1000475, 1000476, 1000477, 1000478, 1000479, 1000480, 1000481, 1000482, 1000483, 1000484, 1000485, 1000486, 1000487, 1000488, 1000489, 1000490, 1000493, 1000494, 1000495, 1050804, 1050805, 1050821, 1050826, 1050827, 1050828, 1102113, 1102114, 1102115, 1102116, 1102117, 11000298, 1000299, 1000300, 1000301, 1000302, 1000303, 1000304, 1000305, 1000306, 1000307, 1000308, 1000309, 1000310, 1000311, 1000312, 1000313, 1000314, 1000315, 1000316, 1000317, 1000318, 1000319, 1000320, 1000321, 1000322, 1000323, 1000324, 1000325, 1000326, 1000327, 1000328, 1000329, 1000330, 1000331, 1000332, 1000333, 1000334, 1000335, 1000336, 1000337, 1000338, 1000339, 1000340, 1000341, 1000342, 1000343, 1000344, 1000345, 1000346, 1000347, 1000348, 1000349, 1000350, 1000351, 1000352, 1000353, 1000354, 1000355, 1000356, 1000357, 1000358, 1000359, 1000360, 1000361, 1000362, 1000363, 1000364, 1000365, 1000421, 1000422, 1000423, 1000424, 1000425, 1000426, 1000427, 1000428, 1000429, 1000430, 1000431, 1000432, 1000433, 1000434, 1000435, 1000436, 1000437, 1000438, 1000439, 1000440, 1000441, 1000442, 1000443, 1000444, 1000445, 1000446, 1000447, 1000448, 1000449, 1000450, 1000451, 1000452, 1000454, 1000455, 1000456, 1000457, 1000458, 1000459, 1000460, 1000461, 1052715, 1052716, 1052720, 1052722, 1012378, 1012380, 1012381, 1012382, 1050800, 1050801, 1050803, 1012900, 1012901, 1012902, 1000462, 1000463, 1000464, 1000465, 1000466, 1000467, 1000468, 1000469, 1000470, 1000471, 1000472, 1000473, 1000474, 1000475, 1000476, 1000477, 1000478, 1000479, 1000480, 1000481, 1000482, 1000483, 1000484, 1000485, 1000486, 1000487, 1000488, 1000489, 1000490, 1000493, 1000494, 1000495, 1050804, 1050805, 1050821, 1050826, 1050827, 1050828, 1102113, 1102114, 1102115, 1102116, 1102117, 1001298, 1001299, 1001300, 1001301, 1001302, 1001303, 1001304, 1001305, 1001306, 1001307, 1001308, 1001309, 1001310, 1001311, 1001312, 1001313, 1001314, 1001315, 1001316, 1001317, 1001318, 1001319, 1001320, 1001321, 1001322, 1001323, 1001324, 1001325, 1001326, 1001327, 1001328, 1001329, 1001330, 1001331, 1001332, 1001333, 1001334, 1001335, 1001336, 1001337, 1001338, 1001339, 1001340, 1001341, 1001342, 1001343, 1001344, 1001345, 1001346, 1001347, 1001348, 1001349, 1001350, 1001351, 1001352, 1001353, 1001354, 1001355, 1001356, 1001357, 1001358, 1001359, 1001360, 1001361, 1001362, 1001363, 1001364, 1001365, 1001421, 1001422, 1001423, 1001424, 1001425, 1001426, 1001427, 1001428, 1001429, 1001430, 1001431, 1001432, 1001433, 1001434, 1001435, 1001436, 1001437, 1001438, 1001439, 1001440, 1001441, 1001442, 1001443, 1001444, 1001445, 1001446, 1001447, 1001448, 1001449, 1001450, 1001451, 1001452, 1001453, 1001454, 1001455, 1001456, 1001457, 1001458, 1001459, 1001460, 1001461, 1052713, 1052714, 1052717, 1052718, 1052719, 1052721, 1052723, 1052729, 1052730, 1012378, 1012380, 1012381, 1012382, 1051800, 1051801, 1051803, 1051804, 1051805, 1051806, 1012900, 1012901, 1012902, 1001462, 1001463, 1001464, 1001465, 1001466, 1001467, 1001468, 1001469, 1001470, 1001471, 1001472, 1001473, 1001474, 1001475, 1001476, 1001477, 1001478, 1001479, 1001480, 1001481, 1001482, 1001483, 1001484, 1001485, 1001486, 1001487, 1001488, 1001489, 1001491, 1001493, 1001494, 1001495, 1051807, 1051808, 1051811, 1051812, 1051813, 1051814, 1051815, 1051817, 1051818, 1051819, 1051820, 1051821, 1051822, 1051823, 1051824, 1051825, 1051826, 1051827, 1051828, 1102113, 1102114, 1102115, 1102116, 1102117, 1117000, 1117001, 1117002, 1117003, 1117004, 1117005, 1117006, 1117007, 1117008, 1117009, 1117010, 1117011, 1117700, 1117701, 1117702, 1117703, 1117704, 1117705, 1117706, 1117707, 1117708, 1117709, 1117710, 1117711, 1000298, 1000299, 1000300, 1000301, 1000302, 1000303, 1000304, 1000305, 1000306, 1000307, 1000308, 1000309, 1000310, 1000311, 1000312, 1000313, 1000314, 1000315, 1000316, 1000317, 1000318, 1000319, 1000320, 1000321, 1000322, 1000323, 1000324, 1000325, 1000326, 1000327, 1000328, 1000329, 1000330, 1000331, 1000332, 1000333, 1000334, 1000335, 1000336, 1000337, 1000338, 1000339, 1000340, 1000341, 1000342, 1000343, 1000344, 1000345, 1000346, 1000347, 1000348, 1000349, 1000350, 1000351, 1000352, 1000353, 1000354, 1000355, 1000356, 1000357, 1000358, 1000359, 1000360, 1000361, 1000362, 1000363, 1000364, 1000365, 1000421, 1000422, 1000423, 1000424, 1000425, 1000426, 1000427, 1000428, 1000429, 1000430, 1000431, 1000432, 1000433, 1000434, 1000435, 1000436, 1000437, 1000438, 1000439, 1000440, 1000441, 1000442, 1000443, 1000444, 1000445, 1000446, 1000447, 1000448, 1000449, 1000450, 1000451, 1000452, 1000454, 1000455, 1000456, 1000457, 1000458, 1000459, 1000460, 1000461, 1052715, 1052716, 1052720, 1052722, 1012378, 1012380, 1012381, 1012382, 1050800, 1050801, 1050803, 1012900, 1012901, 1012902, 1000462, 1000463, 1000464, 1000465, 1000466, 1000467, 1000468, 1000469, 1000470, 1000471, 1000472, 1000473, 1000474, 1000475, 1000476, 1000477, 1000478, 1000479, 1000480, 1000481, 1000482, 1000483, 1000484, 1000485, 1000486, 1000487, 1000488, 1000489, 1000490, 1000493, 1000494, 1000495, 1050804, 1050805, 1050821, 1050826, 1050827, 1050828, 1102113, 1102114, 1102115, 1102116, 1102117, 1050829, 1050830, 1050831, 1050832, 1000497, 1000498, 1000499, 1000500, 1000501, 1000502, 1000503, 1000504, 1000505, 1000506, 1000508, 1000509, 1000510, 1000511, 1000512, 1000513, 1000514, 1000515, 1000516, 1000517, 1000518, 1000519, 1000520, 1000521, 1000522, 1000523, 1000524, 1000526, 1000527, 1000528, 1000530, 1000534, 1000535, 1000536, 1000539, 1000542, 1000543, 1102118, 1102119, 1000545, 1000546, 1000507, 1000529, 1000531, 1000537, 1000540, 1050833, 1073303, 1073317, 1073318, 1102121, 1102122, 1102123, 1117000, 1117001, 1117002, 1117003, 1117004, 1117005, 1117006, 1117007, 1117008, 1117009, 1117010, 1117011, 1117700, 1117701, 1117702, 1117703, 1117704, 1117705, 1117706, 1117707, 1117708, 1117709, 1117710, 1117711, 1001298, 1001299, 1001300, 1001301, 1001302, 1001303, 1001304, 1001305, 1001306, 1001307, 1001308, 1001309, 1001310, 1001311, 1001312, 1001313, 1001314, 1001315, 1001316, 1001317, 1001318, 1001319, 1001320, 1001321, 1001322, 1001323, 1001324, 1001325, 1001326, 1001327, 1001328, 1001329, 1001330, 1001331, 1001332, 1001333, 1001334, 1001335, 1001336, 1001337, 1001338, 1001339, 1001340, 1001341, 1001342, 1001343, 1001344, 1001345, 1001346, 1001347, 1001348, 1001349, 1001350, 1001351, 1001352, 1001353, 1001354, 1001355, 1001356, 1001357, 1001358, 1001359, 1001360, 1001361, 1001362, 1001363, 1001364, 1001365, 1001421, 1001422, 1001423, 1001424, 1001425, 1001426, 1001427, 1001428, 1001429, 1001430, 1001431, 1001432, 1001433, 1001434, 1001435, 1001436, 1001437, 1001438, 1001439, 1001440, 1001441, 1001442, 1001443, 1001444, 1001445, 1001446, 1001447, 1001448, 1001449, 1001450, 1001451, 1001452, 1001453, 1001454, 1001455, 1001456, 1001457, 1001458, 1001459, 1001460, 1001461, 1052713, 1052714, 1052717, 1052718, 1052719, 1052721, 1052723, 1052729, 1052730, 1012378, 1012380, 1012381, 1012382, 1051800, 1051801, 1051803, 1051804, 1051805, 1051806, 1012900, 1012901, 1012902, 1001462, 1001463, 1001464, 1001465, 1001466, 1001467, 1001468, 1001469, 1001470, 1001471, 1001472, 1001473, 1001474, 1001475, 1001476, 1001477, 1001478, 1001479, 1001480, 1001481, 1001482, 1001483, 1001484, 1001485, 1001486, 1001487, 1001488, 1001489, 1001491, 1001493, 1001494, 1001495, 1051807, 1051808, 1051811, 1051812, 1051813, 1051814, 1051815, 1051817, 1051818, 1051819, 1051820, 1051821, 1051822, 1051823, 1051824, 1051825, 1051826, 1051827, 1051828, 1102113, 1102114, 1102115, 1102116, 1102117, 1051829, 1051830, 1051831, 1051832, 1051833, 1051834, 1051835, 1051836, 1051837, 1001496, 1001497, 1001498, 1001499, 1001500, 1001501, 1001502, 1001503, 1001504, 1001505, 1001506, 1001508, 1001509, 1001510, 1001511, 1001512, 1001513, 1001514, 1001515, 1001516, 1001517, 1001518, 1001519, 1001520, 1001521, 1001522, 1001523, 1001524, 1001526, 1001527, 1001528, 1001530, 1001534, 1001535, 1001536, 1001539, 1001541, 1102118, 1102119, 1001545, 1001546, 1001507, 1001529, 1001531, 1001537, 1001540, 1051839, 1073303, 1073317, 1073318, 1102121, 1102122, 1102123, 1117000, 1117001, 1117002, 1117003, 1117004, 1117005, 1117006, 1117007, 1117008, 1117009, 1117010, 1117011, 1117700, 1117701, 1117702, 1117703, 1117704, 1117705, 1117706, 1117707, 1117708, 1117709, 1117710, 1117711, 1000298, 1000299, 1000300, 1000301, 1000302, 1000303, 1000304, 1000305, 1000306, 1000307, 1000308, 1000309, 1000310, 1000311, 1000312, 1000313, 1000314, 1000315, 1000316, 1000317, 1000318, 1000319, 1000320, 1000321, 1000322, 1000323, 1000324, 1000325, 1000326, 1000327, 1000328, 1000329, 1000330, 1000331, 1000332, 1000333, 1000334, 1000335, 1000336, 1000337, 1000338, 1000339, 1000340, 1000341, 1000342, 1000343, 1000344, 1000345, 1000346, 1000347, 1000348, 1000349, 1000350, 1000351, 1000352, 1000353, 1000354, 1000355, 1000356, 1000357, 1000358, 1000359, 1000360, 1000361, 1000362, 1000363, 1000364, 1000365, 1000421, 1000422, 1000423, 1000424, 1000425, 1000426, 1000427, 1000428, 1000429, 1000430, 1000431, 1000432, 1000433, 1000434, 1000435, 1000436, 1000437, 1000438, 1000439, 1000440, 1000441, 1000442, 1000443, 1000444, 1000445, 1000446, 1000447, 1000448, 1000449, 1000450, 1000451, 1000452, 1000454, 1000455, 1000456, 1000457, 1000458, 1000459, 1000460, 1000461, 1052715, 1052716, 1052720, 1052722, 1012378, 1012380, 1012381, 1012382, 1050800, 1050801, 1050803, 1012900, 1012901, 1012902, 1000462, 1000463, 1000464, 1000465, 1000466, 1000467, 1000468, 1000469, 1000470, 1000471, 1000472, 1000473, 1000474, 1000475, 1000476, 1000477, 1000478, 1000479, 1000480, 1000481, 1000482, 1000483, 1000484, 1000485, 1000486, 1000487, 1000488, 1000489, 1000490, 1000493, 1000494, 1000495, 1050804, 1050805, 1050821, 1050826, 1050827, 1050828, 1102113, 1102114, 1102115, 1102116, 1102117, 1050829, 1050830, 1050831, 1050832, 1000497, 1000498, 1000499, 1000500, 1000501, 1000502, 1000503, 1000504, 1000505, 1000506, 1000508, 1000509, 1000510, 1000511, 1000512, 1000513, 1000514, 1000515, 1000516, 1000517, 1000518, 1000519, 1000520, 1000521, 1000522, 1000523, 1000524, 1000526, 1000527, 1000528, 1000530, 1000534, 1000535, 1000536, 1000539, 1000542, 1000543, 1102118, 1102119, 1000545, 1000546, 1000507, 1000529, 1000531, 1000537, 1000540, 1050833, 1073303, 1073317, 1073318, 1102121, 1102122, 1102123, 1117000, 1117001, 1117002, 1117003, 1117004, 1117005, 1117006, 1117007, 1117008, 1117009, 1117010, 1117011, 1117700, 1117701, 1117702, 1117703, 1117704, 1117705, 1117706, 1117707, 1117708, 1117709, 1117710, 1117711, 1001298, 1001299, 1001300, 1001301, 1001302, 1001303, 1001304, 1001305, 1001306, 1001307, 1001308, 1001309, 1001310, 1001311, 1001312, 1001313, 1001314, 1001315, 1001316, 1001317, 1001318, 1001319, 1001320, 1001321, 1001322, 1001323, 1001324, 1001325, 1001326, 1001327, 1001328, 1001329, 1001330, 1001331, 1001332, 1001333, 1001334, 1001335, 1001336, 1001337, 1001338, 1001339, 1001340, 1001341, 1001342, 1001343, 1001344, 1001345, 1001346, 1001347, 1001348, 1001349, 1001350, 1001351, 1001352, 1001353, 1001354, 1001355, 1001356, 1001357, 1001358, 1001359, 1001360, 1001361, 1001362, 1001363, 1001364, 1001365, 1001421, 1001422, 1001423, 1001424, 1001425, 1001426, 1001427, 1001428, 1001429, 1001430, 1001431, 1001432, 1001433, 1001434, 1001435, 1001436, 1001437, 1001438, 1001439, 1001440, 1001441, 1001442, 1001443, 1001444, 1001445, 1001446, 1001447, 1001448, 1001449, 1001450, 1001451, 1001452, 1001453, 1001454, 1001455, 1001456, 1001457, 1001458, 1001459, 1001460, 1001461, 1052713, 1052714, 1052717, 1052718, 1052719, 1052721, 1052723, 1052729, 1052730, 1012378, 1012380, 1012381, 1012382, 1051800, 1051801, 1051803, 1051804, 1051805, 1051806, 1012900, 1012901, 1012902, 1001462, 1001463, 1001464, 1001465, 1001466, 1001467, 1001468, 1001469, 1001470, 1001471, 1001472, 1001473, 1001474, 1001475, 1001476, 1001477, 1001478, 1001479, 1001480, 1001481, 1001482, 1001483, 1001484, 1001485, 1001486, 1001487, 1001488, 1001489, 1001491, 1001493, 1001494, 1001495, 1051807, 1051808, 1051811, 1051812, 1051813, 1051814, 1051815, 1051817, 1051818, 1051819, 1051820, 1051821, 1051822, 1051823, 1051824, 1051825, 1051826, 1051827, 1051828, 1102113, 1102114, 1102115, 1102116, 1102117, 1051829, 1051830, 1051831, 1051832, 1051833, 1051834, 1051835, 1051836, 1051837, 1001496, 1001497, 1001498, 1001499, 1001500, 1001501, 1001502, 1001503, 1001504, 1001505, 1001506, 1001508, 1001509, 1001510, 1001511, 1001512, 1001513, 1001514, 1001515, 1001516, 1001517, 1001518, 1001519, 1001520, 1001521, 1001522, 1001523, 1001524, 1001526, 1001527, 1001528, 1001530, 1001534, 1001535, 1001536, 1001539, 1001541, 1102118, 1102119, 1001545, 1001546, 1001507, 1001529, 1001531, 1001537, 1001540, 1051839, 1073303, 1073317, 1073318, 1102121, 1102122, 1102123, 1117000, 1117001, 1117002, 1117003, 1117004, 1117005, 1117006, 1117007, 1117008, 1117009, 1117010, 1117011, 1117700, 1117701, 1117702, 1117703, 1117704, 1117705, 1117706, 1117707, 1117708, 1117709, 1117710, 1117711, 1119000, 1022080];

function action(mode, type, selection) {
    if (status == 3 && selection == -1 && type == 3 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        if (cm.getPlayer().getHPoint() >= 0) {
        cm.sendGetText("\r\n#fs11#원하는 아이템의 이름을 말해주세요.\r\n\r\n아이템의 정확한 명칭을 모르시나요?\r\n#b아이템의 이름 일부분만 입력해도 검색이 가능해요.#k\r\n#fs11#\r\n#r예) 치어 팡팡 → '팡팡' 으로 검색이 가능하답니다.#k\r\n\r\n");
} else {
		        cm.sendOk("#r홍보 포인트가 부족합니다#k");
		        cm.dispose();
		    }
    } else if (status == 1) {
        if (cm.getText().equals("") || cm.getText().equals(" ")) {
            cm.sendOk("잘못 입력하셨어요.");
            cm.dispose();
            return;
        }
        var t = cm.searchCashItem(cm.getText());
        if (t.equals("")) {
            cm.sendOk("검색된 결과가 없네요.");
            cm.dispose();
            return;
        }
        cm.sendSimple("#fs11#입력한 [#b" + cm.getText() + "#k]의 검색 결과예요.\r\n\r\n" + t);
    } else if (status == 2) {
        	sel = selection;
	if (!MapleItemInformationProvider.getInstance().isCash(sel)) {
		cm.sendOk("#fs11#오류가 발생 했어요.");
		cm.dispose();
		return;
	}
	if (banitem.indexOf(sel) != -1) {
		cm.sendOk("#fs11#해당 아이템은 구매할 수 없어요.");
		cm.dispose();
		return;
	}
        cm.sendYesNo("#fs11#정말 선택한 #i" + sel + "##b#t" + sel + "##k (을)를 지급 받으시겠어요?");
    } else if (status == 3) {
        var ii = MapleItemInformationProvider.getInstance();
	if (!MapleItemInformationProvider.getInstance().isCash(sel)) {
		cm.sendOk("#fs11#오류가 발생 했어요.");
		cm.dispose();
		return;
	}
	if (banitem.indexOf(sel) != -1) {
		cm.sendOk("#fs11#해당 아이템은 구매할 수 없어요.");
		cm.dispose();
		return;
	}
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(sel);
	item.setStr(300);
	item.setDex(300);
	item.setInt(300);
	item.setLuk(300);
	item.setWatk(300);
	item.setMatk(300);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	cm.gainItem(2431394, -1);
        cm.sendYesNo("#fs11#선택한 아이템을 지급했습니다.");
   } else if (status == 4) {
			cm.dispose();

   }
}