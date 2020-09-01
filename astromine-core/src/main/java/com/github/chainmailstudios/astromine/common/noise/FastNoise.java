/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.common.noise;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class FastNoise {
	private static final Vector2f[] TWO_DIMENSIONAL_GRADIENT = { new Vector2f(-1, -1), new Vector2f(1, -1), new Vector2f(-1, 1), new Vector2f(1, 1), new Vector2f(0, -1), new Vector2f(-1, 0), new Vector2f(0, 1), new Vector2f(1, 0), };
	private static final Vector3f[] THREE_DIMENSIONAL_GRADIENT = { new Vector3f(1, 1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0), new Vector3f(1, 0, 1), new Vector3f(-1, 0, 1), new Vector3f(1, 0, -1), new Vector3f(-1, 0, -1), new Vector3f(0, 1,
		1), new Vector3f(0, -1, 1), new Vector3f(0, 1, -1), new Vector3f(0, -1, -1), new Vector3f(1, 1, 0), new Vector3f(0, -1, 1), new Vector3f(-1, 1, 0), new Vector3f(0, -1, -1), };
	private static final Vector2f[] TWO_DIMENSIONAL_CELLULAR = { new Vector2f(-0.4313539279f, 0.1281943404f), new Vector2f(-0.1733316799f, 0.415278375f), new Vector2f(-0.2821957395f, -0.3505218461f), new Vector2f(-0.2806473808f, 0.3517627718f), new Vector2f(0.3125508975f,
		-0.3237467165f), new Vector2f(0.3383018443f, -0.2967353402f), new Vector2f(-0.4393982022f, -0.09710417025f), new Vector2f(-0.4460443703f, -0.05953502905f), new Vector2f(-0.302223039f, 0.3334085102f), new Vector2f(-0.212681052f, -0.3965687458f), new Vector2f(
			-0.2991156529f, 0.3361990872f), new Vector2f(0.2293323691f, 0.3871778202f), new Vector2f(0.4475439151f, -0.04695150755f), new Vector2f(0.1777518f, 0.41340573f), new Vector2f(0.1688522499f, -0.4171197882f), new Vector2f(-0.0976597166f, 0.4392750616f), new Vector2f(
				0.08450188373f, 0.4419948321f), new Vector2f(-0.4098760448f, -0.1857461384f), new Vector2f(0.3476585782f, -0.2857157906f), new Vector2f(-0.3350670039f, -0.30038326f), new Vector2f(0.2298190031f, -0.3868891648f), new Vector2f(-0.01069924099f, 0.449872789f),
		new Vector2f(-0.4460141246f, -0.05976119672f), new Vector2f(0.3650293864f, 0.2631606867f), new Vector2f(-0.349479423f, 0.2834856838f), new Vector2f(-0.4122720642f, 0.1803655873f), new Vector2f(-0.267327811f, 0.3619887311f), new Vector2f(0.322124041f, -0.3142230135f),
		new Vector2f(0.2880445931f, -0.3457315612f), new Vector2f(0.3892170926f, -0.2258540565f), new Vector2f(0.4492085018f, -0.02667811596f), new Vector2f(-0.4497724772f, 0.01430799601f), new Vector2f(0.1278175387f, -0.4314657307f), new Vector2f(-0.03572100503f, 0.4485799926f),
		new Vector2f(-0.4297407068f, -0.1335025276f), new Vector2f(-0.3217817723f, 0.3145735065f), new Vector2f(-0.3057158873f, 0.3302087162f), new Vector2f(-0.414503978f, 0.1751754899f), new Vector2f(-0.3738139881f, 0.2505256519f), new Vector2f(0.2236891408f, -0.3904653228f),
		new Vector2f(0.002967775577f, -0.4499902136f), new Vector2f(0.1747128327f, -0.4146991995f), new Vector2f(-0.4423772489f, -0.08247647938f), new Vector2f(-0.2763960987f, -0.355112935f), new Vector2f(-0.4019385906f, -0.2023496216f), new Vector2f(0.3871414161f,
			-0.2293938184f), new Vector2f(-0.430008727f, 0.1326367019f), new Vector2f(-0.03037574274f, -0.4489736231f), new Vector2f(-0.3486181573f, 0.2845441624f), new Vector2f(0.04553517144f, -0.4476902368f), new Vector2f(-0.0375802926f, 0.4484280562f), new Vector2f(
				0.3266408905f, 0.3095250049f), new Vector2f(0.06540017593f, -0.4452222108f), new Vector2f(0.03409025829f, 0.448706869f), new Vector2f(-0.4449193635f, 0.06742966669f), new Vector2f(-0.4255936157f, -0.1461850686f), new Vector2f(0.449917292f, 0.008627302568f),
		new Vector2f(0.05242606404f, 0.4469356864f), new Vector2f(-0.4495305179f, -0.02055026661f), new Vector2f(-0.1204775703f, 0.4335725488f), new Vector2f(-0.341986385f, -0.2924813028f), new Vector2f(0.3865320182f, 0.2304191809f), new Vector2f(0.04506097811f, -0.447738214f),
		new Vector2f(-0.06283465979f, 0.4455915232f), new Vector2f(0.3932600341f, -0.2187385324f), new Vector2f(0.4472261803f, -0.04988730975f), new Vector2f(0.3753571011f, -0.2482076684f), new Vector2f(-0.273662295f, 0.357223947f), new Vector2f(0.1700461538f, 0.4166344988f),
		new Vector2f(0.4102692229f, 0.1848760794f), new Vector2f(0.323227187f, -0.3130881435f), new Vector2f(-0.2882310238f, -0.3455761521f), new Vector2f(0.2050972664f, 0.4005435199f), new Vector2f(0.4414085979f, -0.08751256895f), new Vector2f(-0.1684700334f, 0.4172743077f),
		new Vector2f(-0.003978032396f, 0.4499824166f), new Vector2f(-0.2055133639f, 0.4003301853f), new Vector2f(-0.006095674897f, -0.4499587123f), new Vector2f(-0.1196228124f, -0.4338091548f), new Vector2f(0.3901528491f, -0.2242337048f), new Vector2f(0.01723531752f,
			0.4496698165f), new Vector2f(-0.3015070339f, 0.3340561458f), new Vector2f(-0.01514262423f, -0.4497451511f), new Vector2f(-0.4142574071f, -0.1757577897f), new Vector2f(-0.1916377265f, -0.4071547394f), new Vector2f(0.3749248747f, 0.2488600778f), new Vector2f(
				-0.2237774255f, 0.3904147331f), new Vector2f(-0.4166343106f, -0.1700466149f), new Vector2f(0.3619171625f, 0.267424695f), new Vector2f(0.1891126846f, -0.4083336779f), new Vector2f(-0.3127425077f, 0.323561623f), new Vector2f(-0.3281807787f, 0.307891826f),
		new Vector2f(-0.2294806661f, 0.3870899429f), new Vector2f(-0.3445266136f, 0.2894847362f), new Vector2f(-0.4167095422f, -0.1698621719f), new Vector2f(-0.257890321f, -0.3687717212f), new Vector2f(-0.3612037825f, 0.2683874578f), new Vector2f(0.2267996491f, 0.3886668486f),
		new Vector2f(0.207157062f, 0.3994821043f), new Vector2f(0.08355176718f, -0.4421754202f), new Vector2f(-0.4312233307f, 0.1286329626f), new Vector2f(0.3257055497f, 0.3105090899f), new Vector2f(0.177701095f, -0.4134275279f), new Vector2f(-0.445182522f, 0.06566979625f),
		new Vector2f(0.3955143435f, 0.2146355146f), new Vector2f(-0.4264613988f, 0.1436338239f), new Vector2f(-0.3793799665f, -0.2420141339f), new Vector2f(0.04617599081f, -0.4476245948f), new Vector2f(-0.371405428f, -0.2540826796f), new Vector2f(0.2563570295f, -0.3698392535f),
		new Vector2f(0.03476646309f, 0.4486549822f), new Vector2f(-0.3065454405f, 0.3294387544f), new Vector2f(-0.2256979823f, 0.3893076172f), new Vector2f(0.4116448463f, -0.1817925206f), new Vector2f(-0.2907745828f, -0.3434387019f), new Vector2f(0.2842278468f, -0.348876097f),
		new Vector2f(0.3114589359f, -0.3247973695f), new Vector2f(0.4464155859f, -0.0566844308f), new Vector2f(-0.3037334033f, -0.3320331606f), new Vector2f(0.4079607166f, 0.1899159123f), new Vector2f(-0.3486948919f, -0.2844501228f), new Vector2f(0.3264821436f, 0.3096924441f),
		new Vector2f(0.3211142406f, 0.3152548881f), new Vector2f(0.01183382662f, 0.4498443737f), new Vector2f(0.4333844092f, 0.1211526057f), new Vector2f(0.3118668416f, 0.324405723f), new Vector2f(-0.272753471f, 0.3579183483f), new Vector2f(-0.422228622f, -0.1556373694f),
		new Vector2f(-0.1009700099f, -0.4385260051f), new Vector2f(-0.2741171231f, -0.3568750521f), new Vector2f(-0.1465125133f, 0.4254810025f), new Vector2f(0.2302279044f, -0.3866459777f), new Vector2f(-0.3699435608f, 0.2562064828f), new Vector2f(0.105700352f, -0.4374099171f),
		new Vector2f(-0.2646713633f, 0.3639355292f), new Vector2f(0.3521828122f, 0.2801200935f), new Vector2f(-0.1864187807f, -0.4095705534f), new Vector2f(0.1994492955f, -0.4033856449f), new Vector2f(0.3937065066f, 0.2179339044f), new Vector2f(-0.3226158377f, 0.3137180602f),
		new Vector2f(0.3796235338f, 0.2416318948f), new Vector2f(0.1482921929f, 0.4248640083f), new Vector2f(-0.407400394f, 0.1911149365f), new Vector2f(0.4212853031f, 0.1581729856f), new Vector2f(-0.2621297173f, 0.3657704353f), new Vector2f(-0.2536986953f, -0.3716678248f),
		new Vector2f(-0.2100236383f, 0.3979825013f), new Vector2f(0.3624152444f, 0.2667493029f), new Vector2f(-0.3645038479f, -0.2638881295f), new Vector2f(0.2318486784f, 0.3856762766f), new Vector2f(-0.3260457004f, 0.3101519002f), new Vector2f(-0.2130045332f, -0.3963950918f),
		new Vector2f(0.3814998766f, -0.2386584257f), new Vector2f(-0.342977305f, 0.2913186713f), new Vector2f(-0.4355865605f, 0.1129794154f), new Vector2f(-0.2104679605f, 0.3977477059f), new Vector2f(0.3348364681f, -0.3006402163f), new Vector2f(0.3430468811f, 0.2912367377f),
		new Vector2f(-0.2291836801f, -0.3872658529f), new Vector2f(0.2547707298f, -0.3709337882f), new Vector2f(0.4236174945f, -0.151816397f), new Vector2f(-0.15387742f, 0.4228731957f), new Vector2f(-0.4407449312f, 0.09079595574f), new Vector2f(-0.06805276192f, -0.444824484f),
		new Vector2f(0.4453517192f, -0.06451237284f), new Vector2f(0.2562464609f, -0.3699158705f), new Vector2f(0.3278198355f, -0.3082761026f), new Vector2f(-0.4122774207f, -0.1803533432f), new Vector2f(0.3354090914f, -0.3000012356f), new Vector2f(0.446632869f, -0.05494615882f),
		new Vector2f(-0.1608953296f, 0.4202531296f), new Vector2f(-0.09463954939f, 0.4399356268f), new Vector2f(-0.02637688324f, -0.4492262904f), new Vector2f(0.447102804f, -0.05098119915f), new Vector2f(-0.4365670908f, 0.1091291678f), new Vector2f(-0.3959858651f, 0.2137643437f),
		new Vector2f(-0.4240048207f, -0.1507312575f), new Vector2f(-0.3882794568f, 0.2274622243f), new Vector2f(-0.4283652566f, -0.1378521198f), new Vector2f(0.3303888091f, 0.305521251f), new Vector2f(0.3321434919f, -0.3036127481f), new Vector2f(-0.413021046f, -0.1786438231f),
		new Vector2f(0.08403060337f, -0.4420846725f), new Vector2f(-0.3822882919f, 0.2373934748f), new Vector2f(-0.3712395594f, -0.2543249683f), new Vector2f(0.4472363971f, -0.04979563372f), new Vector2f(-0.4466591209f, 0.05473234629f), new Vector2f(0.0486272539f,
			-0.4473649407f), new Vector2f(-0.4203101295f, -0.1607463688f), new Vector2f(0.2205360833f, 0.39225481f), new Vector2f(-0.3624900666f, 0.2666476169f), new Vector2f(-0.4036086833f, -0.1989975647f), new Vector2f(0.2152727807f, 0.3951678503f), new Vector2f(-0.4359392962f,
				-0.1116106179f), new Vector2f(0.4178354266f, 0.1670735057f), new Vector2f(0.2007630161f, 0.4027334247f), new Vector2f(-0.07278067175f, -0.4440754146f), new Vector2f(0.3644748615f, -0.2639281632f), new Vector2f(-0.4317451775f, 0.126870413f), new Vector2f(
					-0.297436456f, 0.3376855855f), new Vector2f(-0.2998672222f, 0.3355289094f), new Vector2f(-0.2673674124f, 0.3619594822f), new Vector2f(0.2808423357f, 0.3516071423f), new Vector2f(0.3498946567f, 0.2829730186f), new Vector2f(-0.2229685561f, 0.390877248f),
		new Vector2f(0.3305823267f, 0.3053118493f), new Vector2f(-0.2436681211f, -0.3783197679f), new Vector2f(-0.03402776529f, 0.4487116125f), new Vector2f(-0.319358823f, 0.3170330301f), new Vector2f(0.4454633477f, -0.06373700535f), new Vector2f(0.4483504221f, 0.03849544189f),
		new Vector2f(-0.4427358436f, -0.08052932871f), new Vector2f(0.05452298565f, 0.4466847255f), new Vector2f(-0.2812560807f, 0.3512762688f), new Vector2f(0.1266696921f, 0.4318041097f), new Vector2f(-0.3735981243f, 0.2508474468f), new Vector2f(0.2959708351f, -0.3389708908f),
		new Vector2f(-0.3714377181f, 0.254035473f), new Vector2f(-0.404467102f, -0.1972469604f), new Vector2f(0.1636165687f, -0.419201167f), new Vector2f(0.3289185495f, -0.3071035458f), new Vector2f(-0.2494824991f, -0.3745109914f), new Vector2f(0.03283133272f, 0.4488007393f),
		new Vector2f(-0.166306057f, -0.4181414777f), new Vector2f(-0.106833179f, 0.4371346153f), new Vector2f(0.06440260376f, -0.4453676062f), new Vector2f(-0.4483230967f, 0.03881238203f), new Vector2f(-0.421377757f, -0.1579265206f), new Vector2f(0.05097920662f, -0.4471030312f),
		new Vector2f(0.2050584153f, -0.4005634111f), new Vector2f(0.4178098529f, -0.167137449f), new Vector2f(-0.3565189504f, -0.2745801121f), new Vector2f(0.4478398129f, 0.04403977727f), new Vector2f(-0.3399999602f, -0.2947881053f), new Vector2f(0.3767121994f, 0.2461461331f),
		new Vector2f(-0.3138934434f, 0.3224451987f), new Vector2f(-0.1462001792f, -0.4255884251f), new Vector2f(0.3970290489f, -0.2118205239f), new Vector2f(0.4459149305f, -0.06049689889f), new Vector2f(-0.4104889426f, -0.1843877112f), new Vector2f(0.1475103971f, -0.4251360756f),
		new Vector2f(0.09258030352f, 0.4403735771f), new Vector2f(-0.1589664637f, -0.4209865359f), new Vector2f(0.2482445008f, 0.3753327428f), new Vector2f(0.4383624232f, -0.1016778537f), new Vector2f(0.06242802956f, 0.4456486745f), new Vector2f(0.2846591015f, -0.3485243118f),
		new Vector2f(-0.344202744f, -0.2898697484f), new Vector2f(0.1198188883f, -0.4337550392f), new Vector2f(-0.243590703f, 0.3783696201f), new Vector2f(0.2958191174f, -0.3391033025f), new Vector2f(-0.1164007991f, 0.4346847754f), new Vector2f(0.1274037151f, -0.4315881062f),
		new Vector2f(0.368047306f, 0.2589231171f), new Vector2f(0.2451436949f, 0.3773652989f), new Vector2f(-0.4314509715f, 0.12786735f), };
	private static final Vector3f[] THREE_DIMENSIONAL_CELLULAR = { new Vector3f(0.1453787434f, -0.4149781685f, -0.0956981749f), new Vector3f(-0.01242829687f, -0.1457918398f, -0.4255470325f), new Vector3f(0.2877979582f, -0.02606483451f, -0.3449535616f), new Vector3f(
		-0.07732986802f, 0.2377094325f, 0.3741848704f), new Vector3f(0.1107205875f, -0.3552302079f, -0.2530858567f), new Vector3f(0.2755209141f, 0.2640521179f, -0.238463215f), new Vector3f(0.294168941f, 0.1526064594f, 0.3044271714f), new Vector3f(0.4000921098f, -0.2034056362f,
			0.03244149937f), new Vector3f(-0.1697304074f, 0.3970864695f, -0.1265461359f), new Vector3f(-0.1483224484f, -0.3859694688f, 0.1775613147f), new Vector3f(0.2623596946f, -0.2354852944f, 0.2796677792f), new Vector3f(-0.2709003183f, 0.3505271138f, -0.07901746678f),
		new Vector3f(-0.03516550699f, 0.3885234328f, 0.2243054374f), new Vector3f(-0.1267712655f, 0.1920044036f, 0.3867342179f), new Vector3f(0.02952021915f, 0.4409685861f, 0.08470692262f), new Vector3f(-0.2806854217f, -0.266996757f, 0.2289725438f), new Vector3f(-0.171159547f,
			0.2141185563f, 0.3568720405f), new Vector3f(0.2113227183f, 0.3902405947f, -0.07453178509f), new Vector3f(-0.1024352839f, 0.2128044156f, -0.3830421561f), new Vector3f(-0.3304249877f, -0.1566986703f, 0.2622305365f), new Vector3f(0.2091111325f, 0.3133278055f,
				-0.2461670583f), new Vector3f(0.344678154f, -0.1944240454f, -0.2142341261f), new Vector3f(0.1984478035f, -0.3214342325f, -0.2445373252f), new Vector3f(-0.2929008603f, 0.2262915116f, 0.2559320961f), new Vector3f(-0.1617332831f, 0.006314769776f, -0.4198838754f),
		new Vector3f(-0.3582060271f, -0.148303178f, -0.2284613961f), new Vector3f(-0.1852067326f, -0.3454119342f, -0.2211087107f), new Vector3f(0.3046301062f, 0.1026310383f, 0.314908508f), new Vector3f(-0.03816768434f, -0.2551766358f, -0.3686842991f), new Vector3f(-0.4084952196f,
			0.1805950793f, 0.05492788837f), new Vector3f(-0.02687443361f, -0.2749741471f, 0.3551999201f), new Vector3f(-0.03801098351f, 0.3277859044f, 0.3059600725f), new Vector3f(0.2371120802f, 0.2900386767f, -0.2493099024f), new Vector3f(0.4447660503f, 0.03946930643f,
				0.05590469027f), new Vector3f(0.01985147278f, -0.01503183293f, -0.4493105419f), new Vector3f(0.4274339143f, 0.03345994256f, -0.1366772882f), new Vector3f(-0.2072988631f, 0.2871414597f, -0.2776273824f), new Vector3f(-0.3791240978f, 0.1281177671f, 0.2057929936f),
		new Vector3f(-0.2098721267f, -0.1007087278f, -0.3851122467f), new Vector3f(0.01582798878f, 0.4263894424f, 0.1429738373f), new Vector3f(-0.1888129464f, -0.3160996813f, -0.2587096108f), new Vector3f(0.1612988974f, -0.1974805082f, -0.3707885038f), new Vector3f(
			-0.08974491322f, 0.229148752f, -0.3767448739f), new Vector3f(0.07041229526f, 0.4150230285f, -0.1590534329f), new Vector3f(-0.1082925611f, -0.1586061639f, 0.4069604477f), new Vector3f(0.2474100658f, -0.3309414609f, 0.1782302128f), new Vector3f(-0.1068836661f,
				-0.2701644537f, -0.3436379634f), new Vector3f(0.2396452163f, 0.06803600538f, -0.3747549496f), new Vector3f(-0.3063886072f, 0.2597428179f, 0.2028785103f), new Vector3f(0.1593342891f, -0.3114350249f, -0.2830561951f), new Vector3f(0.2709690528f, 0.1412648683f,
					-0.3303331794f), new Vector3f(-0.1519780427f, 0.3623355133f, 0.2193527988f), new Vector3f(0.1699773681f, 0.3456012883f, 0.2327390037f), new Vector3f(-0.1986155616f, 0.3836276443f, -0.1260225743f), new Vector3f(-0.1887482106f, -0.2050154888f, -0.353330953f),
		new Vector3f(0.2659103394f, 0.3015631259f, -0.2021172246f), new Vector3f(-0.08838976154f, -0.4288819642f, -0.1036702021f), new Vector3f(-0.04201869311f, 0.3099592485f, 0.3235115047f), new Vector3f(-0.3230334656f, 0.201549922f, -0.2398478873f), new Vector3f(0.2612720941f,
			0.2759854499f, -0.2409749453f), new Vector3f(0.385713046f, 0.2193460345f, 0.07491837764f), new Vector3f(0.07654967953f, 0.3721732183f, 0.241095919f), new Vector3f(0.4317038818f, -0.02577753072f, 0.1243675091f), new Vector3f(-0.2890436293f, -0.3418179959f,
				-0.04598084447f), new Vector3f(-0.2201947582f, 0.383023377f, -0.08548310451f), new Vector3f(0.4161322773f, -0.1669634289f, -0.03817251927f), new Vector3f(0.2204718095f, 0.02654238946f, -0.391391981f), new Vector3f(-0.1040307469f, 0.3890079625f, -0.2008741118f),
		new Vector3f(-0.1432122615f, 0.371614387f, -0.2095065525f), new Vector3f(0.3978380468f, -0.06206669342f, 0.2009293758f), new Vector3f(-0.2599274663f, 0.2616724959f, -0.2578084893f), new Vector3f(0.4032618332f, -0.1124593585f, 0.1650235939f), new Vector3f(-0.08953470255f,
			-0.3048244735f, 0.3186935478f), new Vector3f(0.118937202f, -0.2875221847f, 0.325092195f), new Vector3f(0.02167047076f, -0.03284630549f, -0.4482761547f), new Vector3f(-0.3411343612f, 0.2500031105f, 0.1537068389f), new Vector3f(0.3162964612f, 0.3082064153f,
				-0.08640228117f), new Vector3f(0.2355138889f, -0.3439334267f, -0.1695376245f), new Vector3f(-0.02874541518f, -0.3955933019f, 0.2125550295f), new Vector3f(-0.2461455173f, 0.02020282325f, -0.3761704803f), new Vector3f(0.04208029445f, -0.4470439576f, 0.02968078139f),
		new Vector3f(0.2727458746f, 0.2288471896f, -0.2752065618f), new Vector3f(-0.1347522818f, -0.02720848277f, -0.4284874806f), new Vector3f(0.3829624424f, 0.1231931484f, -0.2016512234f), new Vector3f(-0.3547613644f, 0.1271702173f, 0.2459107769f), new Vector3f(0.2305790207f,
			0.3063895591f, 0.2354968222f), new Vector3f(-0.08323845599f, -0.1922245118f, 0.3982726409f), new Vector3f(0.2993663085f, -0.2619918095f, -0.2103333191f), new Vector3f(-0.2154865723f, 0.2706747713f, 0.287751117f), new Vector3f(0.01683355354f, -0.2680655787f,
				-0.3610505186f), new Vector3f(0.05240429123f, 0.4335128183f, -0.1087217856f), new Vector3f(0.00940104872f, -0.4472890582f, 0.04841609928f), new Vector3f(0.3465688735f, 0.01141914583f, -0.2868093776f), new Vector3f(-0.3706867948f, -0.2551104378f, 0.003156692623f),
		new Vector3f(0.2741169781f, 0.2139972417f, -0.2855959784f), new Vector3f(0.06413433865f, 0.1708718512f, 0.4113266307f), new Vector3f(-0.388187972f, -0.03973280434f, -0.2241236325f), new Vector3f(0.06419469312f, -0.2803682491f, 0.3460819069f), new Vector3f(-0.1986120739f,
			-0.3391173584f, 0.2192091725f), new Vector3f(-0.203203009f, -0.3871641506f, 0.1063600375f), new Vector3f(-0.1389736354f, -0.2775901578f, -0.3257760473f), new Vector3f(-0.06555641638f, 0.342253257f, -0.2847192729f), new Vector3f(-0.2529246486f, -0.2904227915f,
				0.2327739768f), new Vector3f(0.1444476522f, 0.1069184044f, 0.4125570634f), new Vector3f(-0.3643780054f, -0.2447099973f, -0.09922543227f), new Vector3f(0.4286142488f, -0.1358496089f, -0.01829506817f), new Vector3f(0.165872923f, -0.3136808464f, -0.2767498872f),
		new Vector3f(0.2219610524f, -0.3658139958f, 0.1393320198f), new Vector3f(0.04322940318f, -0.3832730794f, 0.2318037215f), new Vector3f(-0.08481269795f, -0.4404869674f, -0.03574965489f), new Vector3f(0.1822082075f, -0.3953259299f, 0.1140946023f), new Vector3f(
			-0.3269323334f, 0.3036542563f, 0.05838957105f), new Vector3f(-0.4080485344f, 0.04227858267f, -0.184956522f), new Vector3f(0.2676025294f, -0.01299671652f, 0.36155217f), new Vector3f(0.3024892441f, -0.1009990293f, -0.3174892964f), new Vector3f(0.1448494052f,
				0.425921681f, -0.0104580805f), new Vector3f(0.4198402157f, 0.08062320474f, 0.1404780841f), new Vector3f(-0.3008872161f, -0.333040905f, -0.03241355801f), new Vector3f(0.3639310428f, -0.1291284382f, -0.2310412139f), new Vector3f(0.3295806598f, 0.0184175994f,
					-0.3058388149f), new Vector3f(0.2776259487f, -0.2974929052f, -0.1921504723f), new Vector3f(0.4149000507f, -0.144793182f, -0.09691688386f), new Vector3f(0.145016715f, -0.0398992945f, 0.4241205002f), new Vector3f(0.09299023471f, -0.299732164f, -0.3225111565f),
		new Vector3f(0.1028907093f, -0.361266869f, 0.247789732f), new Vector3f(0.2683057049f, -0.07076041213f, -0.3542668666f), new Vector3f(-0.4227307273f, -0.07933161816f, -0.1323073187f), new Vector3f(-0.1781224702f, 0.1806857196f, -0.3716517945f), new Vector3f(0.4390788626f,
			-0.02841848598f, -0.09435116353f), new Vector3f(0.2972583585f, 0.2382799621f, -0.2394997452f), new Vector3f(-0.1707002821f, 0.2215845691f, 0.3525077196f), new Vector3f(0.3806686614f, 0.1471852559f, -0.1895464869f), new Vector3f(-0.1751445661f, -0.274887877f,
				0.3102596268f), new Vector3f(-0.2227237566f, -0.2316778837f, 0.3149912482f), new Vector3f(0.1369633021f, 0.1341343041f, -0.4071228836f), new Vector3f(-0.3529503428f, -0.2472893463f, -0.129514612f), new Vector3f(-0.2590744185f, -0.2985577559f, -0.2150435121f),
		new Vector3f(-0.3784019401f, 0.2199816631f, -0.1044989934f), new Vector3f(-0.05635805671f, 0.1485737441f, 0.4210102279f), new Vector3f(0.3251428613f, 0.09666046873f, -0.2957006485f), new Vector3f(-0.4190995804f, 0.1406751354f, -0.08405978803f), new Vector3f(
			-0.3253150961f, -0.3080335042f, -0.04225456877f), new Vector3f(0.2857945863f, -0.05796152095f, 0.3427271751f), new Vector3f(-0.2733604046f, 0.1973770973f, -0.2980207554f), new Vector3f(0.219003657f, 0.2410037886f, -0.3105713639f), new Vector3f(0.3182767252f,
				-0.271342949f, 0.1660509868f), new Vector3f(-0.03222023115f, -0.3331161506f, -0.300824678f), new Vector3f(-0.3087780231f, 0.1992794134f, -0.2596995338f), new Vector3f(-0.06487611647f, -0.4311322747f, 0.1114273361f), new Vector3f(0.3921171432f, -0.06294284106f,
					-0.2116183942f), new Vector3f(-0.1606404506f, -0.358928121f, -0.2187812825f), new Vector3f(-0.03767771199f, -0.2290351443f, 0.3855169162f), new Vector3f(0.1394866832f, -0.3602213994f, 0.2308332918f), new Vector3f(-0.4345093872f, 0.005751117145f,
						0.1169124335f), new Vector3f(-0.1044637494f, 0.4168128432f, -0.1336202785f), new Vector3f(0.2658727501f, 0.2551943237f, 0.2582393035f), new Vector3f(0.2051461999f, 0.1975390727f, 0.3484154868f), new Vector3f(-0.266085566f, 0.23483312f, 0.2766800993f),
		new Vector3f(0.07849405464f, -0.3300346342f, -0.2956616708f), new Vector3f(-0.2160686338f, 0.05376451292f, -0.3910546287f), new Vector3f(-0.185779186f, 0.2148499206f, 0.3490352499f), new Vector3f(0.02492421743f, -0.3229954284f, -0.3123343347f), new Vector3f(-0.120167831f,
			0.4017266681f, 0.1633259825f), new Vector3f(-0.02160084693f, -0.06885389554f, 0.4441762538f), new Vector3f(0.2597670064f, 0.3096300784f, 0.1978643903f), new Vector3f(-0.1611553854f, -0.09823036005f, 0.4085091653f), new Vector3f(-0.3278896792f, 0.1461670309f,
				0.2713366126f), new Vector3f(0.2822734956f, 0.03754421121f, -0.3484423997f), new Vector3f(0.03169341113f, 0.347405252f, -0.2842624114f), new Vector3f(0.2202613604f, -0.3460788041f, -0.1849713341f), new Vector3f(0.2933396046f, 0.3031973659f, 0.1565989581f),
		new Vector3f(-0.3194922995f, 0.2453752201f, -0.200538455f), new Vector3f(-0.3441586045f, -0.1698856132f, -0.2349334659f), new Vector3f(0.2703645948f, -0.3574277231f, 0.04060059933f), new Vector3f(0.2298568861f, 0.3744156221f, 0.0973588921f), new Vector3f(0.09326603877f,
			-0.3170108894f, 0.3054595587f), new Vector3f(-0.1116165319f, -0.2985018719f, 0.3177080142f), new Vector3f(0.2172907365f, -0.3460005203f, -0.1885958001f), new Vector3f(0.1991339479f, 0.3820341668f, -0.1299829458f), new Vector3f(-0.0541918155f, -0.2103145071f,
				0.39412061f), new Vector3f(0.08871336998f, 0.2012117383f, 0.3926114802f), new Vector3f(0.2787673278f, 0.3505404674f, 0.04370535101f), new Vector3f(-0.322166438f, 0.3067213525f, 0.06804996813f), new Vector3f(-0.4277366384f, 0.132066775f, 0.04582286686f),
		new Vector3f(0.240131882f, -0.1612516055f, 0.344723946f), new Vector3f(0.1448607981f, -0.2387819045f, 0.3528435224f), new Vector3f(-0.3837065682f, -0.2206398454f, 0.08116235683f), new Vector3f(-0.4382627882f, -0.09082753406f, -0.04664855374f), new Vector3f(-0.37728353f,
			0.05445141085f, 0.2391488697f), new Vector3f(0.1259579313f, 0.348394558f, 0.2554522098f), new Vector3f(-0.1406285511f, -0.270877371f, -0.3306796947f), new Vector3f(-0.1580694418f, 0.4162931958f, -0.06491553533f), new Vector3f(0.2477612106f, -0.2927867412f,
				-0.2353514536f), new Vector3f(0.2916132853f, 0.3312535401f, 0.08793624968f), new Vector3f(0.07365265219f, -0.1666159848f, 0.411478311f), new Vector3f(-0.26126526f, -0.2422237692f, 0.2748965434f), new Vector3f(-0.3721862032f, 0.252790166f, 0.008634938242f),
		new Vector3f(-0.3691191571f, -0.255281188f, 0.03290232422f), new Vector3f(0.2278441737f, -0.3358364886f, 0.1944244981f), new Vector3f(0.363398169f, -0.2310190248f, 0.1306597909f), new Vector3f(-0.304231482f, -0.2698452035f, 0.1926830856f), new Vector3f(-0.3199312232f,
			0.316332536f, -0.008816977938f), new Vector3f(0.2874852279f, 0.1642275508f, -0.304764754f), new Vector3f(-0.1451096801f, 0.3277541114f, -0.2720669462f), new Vector3f(0.3220090754f, 0.0511344108f, 0.3101538769f), new Vector3f(-0.1247400865f, -0.04333605335f,
				-0.4301882115f), new Vector3f(-0.2829555867f, -0.3056190617f, -0.1703910946f), new Vector3f(0.1069384374f, 0.3491024667f, -0.2630430352f), new Vector3f(-0.1420661144f, -0.3055376754f, -0.2982682484f), new Vector3f(-0.250548338f, 0.3156466809f, -0.2002316239f),
		new Vector3f(0.3265787872f, 0.1871229129f, 0.2466400438f), new Vector3f(0.07646097258f, -0.3026690852f, 0.324106687f), new Vector3f(0.3451771584f, 0.2757120714f, -0.0856480183f), new Vector3f(0.298137964f, 0.2852657134f, 0.179547284f), new Vector3f(0.2812250376f,
			0.3466716415f, 0.05684409612f), new Vector3f(0.4390345476f, -0.09790429955f, -0.01278335452f), new Vector3f(0.2148373234f, 0.1850172527f, 0.3494474791f), new Vector3f(0.2595421179f, -0.07946825393f, 0.3589187731f), new Vector3f(0.3182823114f, -0.307355516f,
				-0.08203022006f), new Vector3f(-0.4089859285f, -0.04647718411f, 0.1818526372f), new Vector3f(-0.2826749061f, 0.07417482322f, 0.3421885344f), new Vector3f(0.3483864637f, 0.225442246f, -0.1740766085f), new Vector3f(-0.3226415069f, -0.1420585388f, -0.2796816575f),
		new Vector3f(0.4330734858f, -0.118868561f, -0.02859407492f), new Vector3f(-0.08717822568f, -0.3909896417f, -0.2050050172f), new Vector3f(-0.2149678299f, 0.3939973956f, -0.03247898316f), new Vector3f(-0.2687330705f, 0.322686276f, -0.1617284888f), new Vector3f(
			0.2105665099f, -0.1961317136f, -0.3459683451f), new Vector3f(0.4361845915f, -0.1105517485f, 0.004616608544f), new Vector3f(0.05333333359f, -0.313639498f, -0.3182543336f), new Vector3f(-0.05986216652f, 0.1361029153f, -0.4247264031f), new Vector3f(0.3664988455f,
				0.2550543014f, -0.05590974511f), new Vector3f(-0.2341015558f, -0.182405731f, 0.3382670703f), new Vector3f(-0.04730947785f, -0.4222150243f, -0.1483114513f), new Vector3f(-0.2391566239f, -0.2577696514f, -0.2808182972f), new Vector3f(-0.1242081035f, 0.4256953395f,
					-0.07652336246f), new Vector3f(0.2614832715f, -0.3650179274f, 0.02980623099f), new Vector3f(-0.2728794681f, -0.3499628774f, 0.07458404908f), new Vector3f(0.007892900508f, -0.1672771315f, 0.4176793787f), new Vector3f(-0.01730330376f, 0.2978486637f,
						-0.3368779738f), new Vector3f(0.2054835762f, -0.3252600376f, -0.2334146693f), new Vector3f(-0.3231994983f, 0.1564282844f, -0.2712420987f), new Vector3f(-0.2669545963f, 0.2599343665f, -0.2523278991f), new Vector3f(-0.05554372779f, 0.3170813944f,
							-0.3144428146f), new Vector3f(-0.2083935713f, -0.310922837f, -0.2497981362f), new Vector3f(0.06989323478f, -0.3156141536f, 0.3130537363f), new Vector3f(0.3847566193f, -0.1605309138f, -0.1693876312f), new Vector3f(-0.3026215288f, -0.3001537679f,
								-0.1443188342f), new Vector3f(0.3450735512f, 0.08611519592f, 0.2756962409f), new Vector3f(0.1814473292f, -0.2788782453f, -0.3029914042f), new Vector3f(-0.03855010448f, 0.09795110726f, 0.4375151083f), new Vector3f(0.3533670318f, 0.2665752752f,
									0.08105160988f), new Vector3f(-0.007945601311f, 0.140359426f, -0.4274764309f), new Vector3f(0.4063099273f, -0.1491768253f, -0.1231199324f), new Vector3f(-0.2016773589f, 0.008816271194f, -0.4021797064f), new Vector3f(-0.07527055435f,
										-0.425643481f, -0.1251477955f), };
	// Hashing
	private final static int X_PRIME = 1619;
	private final static int Y_PRIME = 31337;
	private final static int Z_PRIME = 6971;
	private final static int W_PRIME = 1013;
	private final static float F3 = (float) (1.0 / 3.0);
	private final static float G3 = (float) (1.0 / 6.0);
	private final static float G33 = G3 * 3 - 1;
	private final static float SQRT3 = (float) 1.7320508075688772935274463415059;
	private final static float F2 = 0.5f * (SQRT3 - 1.0f);
	private final static float G2 = (3.0f - SQRT3) / 6.0f;
	private static final byte[] SIMPLEX_4D = { 0, 1, 2, 3, 0, 1, 3, 2, 0, 0, 0, 0, 0, 2, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 3, 1, 2, 0, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 0, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 1, 2, 3, 1, 0, 1, 0, 2, 3, 1, 0, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 0, 0, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1, 2, 3, 0, 2, 1, 0, 0, 0, 0, 3, 1, 2, 0, 2, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 3, 2, 0, 1, 3, 2, 1, 0 };
	private final static float F4 = (float) ((2.23606797 - 1.0) / 4.0);
	private final static float G4 = (float) ((5.0 - 2.23606797) / 20.0);
	private final static float CUBIC_3D_BOUNDING = 1 / (float) (1.5 * 1.5 * 1.5);
	private final static float CUBIC_2D_BOUNDING = 1 / (float) (1.5 * 1.5);
	private int seed = 1337;
	private float frequency = (float) 0.01;
	private Interpolation interpolation = Interpolation.QUINTIC;
	private NoiseType noiseType = NoiseType.SIMPLEX;
	private int octaves = 3;
	private float lacunarity = (float) 2.0;
	private float gain = (float) 0.5;
	private FractalType fractalType = FractalType.FRACTIONAL_BROWNIAN;
	private float fractalBounding;
	private CellularDistanceFunction cellularDistanceFunction = CellularDistanceFunction.EUCLIDEAN;
	private CellularReturnType cellularReturnType = CellularReturnType.CELL_VALUE;
	private FastNoise cellularNoiseLookup = null;
	private float gradientPerturbAmp = (float) (1.0 / 0.45);

	public FastNoise() {
		this(1337);
	}

	public FastNoise(int seed) {
		this.seed = seed;
		calculateFractalBounding();
	}

	// Returns a 0 float/double
	public static float getDecimalType() {
		return 0;
	}

	private static int fastFloor(float f) {
		return (f >= 0 ? (int) f : (int) f - 1);
	}

	private static int fastRound(float f) {
		return (f >= 0) ? (int) (f + (float) 0.5) : (int) (f - (float) 0.5);
	}

	private static float interpolateLinear(float a, float b, float t) {
		return a + t * (b - a);
	}

	private static float interpolateHermiteFunction(float t) {
		return t * t * (3 - 2 * t);
	}

	private static float interpolateQuinticFunction(float t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static float interpolateLinearCubic(float a, float b, float c, float d, float t) {
		float p = (d - c) - (a - b);
		return t * t * t * p + t * t * ((a - b) - p) + t * (c - a) + b;
	}

	private static int twoDimensionalHash(int seed, int x, int y) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		return hash;
	}

	private static int threeDimensionalHash(int seed, int x, int y, int z) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;
		hash ^= Z_PRIME * z;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		return hash;
	}

	private static int fourDimensionalHash(int seed, int x, int y, int z, int w) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;
		hash ^= Z_PRIME * z;
		hash ^= W_PRIME * w;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		return hash;
	}

	private static float twoDimensionalCoordinateValue(int seed, int x, int y) {
		int n = seed;
		n ^= X_PRIME * x;
		n ^= Y_PRIME * y;

		return (n * n * n * 60493) / (float) 2147483648.0;
	}

	private static float threeDimensionalCoordinateValue(int seed, int x, int y, int z) {
		int n = seed;
		n ^= X_PRIME * x;
		n ^= Y_PRIME * y;
		n ^= Z_PRIME * z;

		return (n * n * n * 60493) / (float) 2147483648.0;
	}

	private static float fourDimensionalCoordinateValue(int seed, int x, int y, int z, int w) {
		int n = seed;
		n ^= X_PRIME * x;
		n ^= Y_PRIME * y;
		n ^= Z_PRIME * z;
		n ^= W_PRIME * w;

		return (n * n * n * 60493) / (float) 2147483648.0;
	}

	private static float twoDimensionalCoordinateGradient(int seed, int x, int y, float xd, float yd) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Vector2f g = TWO_DIMENSIONAL_GRADIENT[hash & 7];

		return xd * g.x + yd * g.y;
	}

	private static float threeDimensionalCoordinateGradient(int seed, int x, int y, int z, float xd, float yd, float zd) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;
		hash ^= Z_PRIME * z;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Vector3f g = THREE_DIMENSIONAL_GRADIENT[hash & 15];

		return xd * g.x + yd * g.y + zd * g.z;
	}

	private static float fourDimensionalCoordinateGradient(int seed, int x, int y, int z, int w, float xd, float yd, float zd, float wd) {
		int hash = seed;
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;
		hash ^= Z_PRIME * z;
		hash ^= W_PRIME * w;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		hash &= 31;
		float a = yd, b = zd, c = wd; // X,Y,Z
		switch (hash >> 3) { // OR, DEPENDING ON HIGH ORDER 2 BITS:
			case 1:
				a = wd;
				b = xd;
				c = yd;
				break; // W,X,Y
			case 2:
				a = zd;
				b = wd;
				c = xd;
				break; // Z,W,X
			case 3:
				a = yd;
				b = zd;
				c = wd;
				break; // Y,Z,W
		}
		return ((hash & 4) == 0 ? -a : a) + ((hash & 2) == 0 ? -b : b) + ((hash & 1) == 0 ? -c : c);
	}

	// Returns the seed used by this object
	public int getSeed() {
		return seed;
	}

	// Sets seed used for all noise types
	// Default: 1337
	public void setSeed(int seed) {
		this.seed = seed;
	}

	// Sets frequency for all noise types
	// Default: 0.01
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	// Changes the interpolation method used to smooth between noise values
	// Possible interpolation methods (lowest to highest quality) :
	// - Linear
	// - Hermite
	// - Quintic
	// Used in Value, Gradient Noise and Position Perturbing
	// Default: Quintic
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	// Sets noise return type of GetNoise(...)
	// Default: Simplex
	public void setNoiseType(NoiseType noiseType) {
		this.noiseType = noiseType;
	}

	// Sets octave count for all fractal noise types
	// Default: 3
	public void setFractalOctaves(int octaves) {
		this.octaves = octaves;
		calculateFractalBounding();
	}

	// Sets octave lacunarity for all fractal noise types
	// Default: 2.0
	public void setFractalLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
	}

	// Sets octave gain for all fractal noise types
	// Default: 0.5
	public void setFractalGain(float gain) {
		this.gain = gain;
		calculateFractalBounding();
	}

	// Sets method for combining octaves in all fractal noise types
	// Default: FBM
	public void setFractalType(FractalType fractalType) {
		this.fractalType = fractalType;
	}

	// Sets return type from cellular noise calculations
	// Note: NoiseLookup requires another FastNoise object be set with SetCellularNoiseLookup() to function
	// Default: CellValue
	public void setCellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction) {
		this.cellularDistanceFunction = cellularDistanceFunction;
	}

	// White Noise

	// Sets distance function used in cellular noise calculations
	// Default: Euclidean
	public void setCellularReturnType(CellularReturnType cellularReturnType) {
		this.cellularReturnType = cellularReturnType;
	}

	// Noise used to calculate a cell value if cellular return type is NoiseLookup
	// The lookup value is acquired through GetNoise() so ensure you SetNoiseType() on the noise lookup, value, gradient or simplex is recommended
	public void setCellularNoiseLookup(FastNoise noise) {
		cellularNoiseLookup = noise;
	}

	// Sets the maximum perturb distance from original location when using GradientPerturb{Fractal}(...)
	// Default: 1.0
	public void setGradientPerturbAmp(float gradientPerturbAmp) {
		this.gradientPerturbAmp = gradientPerturbAmp / (float) 0.45;
	}

	private void calculateFractalBounding() {
		float amp = gain;
		float ampFractal = 1;
		for (int i = 1; i < octaves; i++) {
			ampFractal += amp;
			amp *= gain;
		}
		fractalBounding = 1 / ampFractal;
	}

	public float getNoise(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (noiseType) {
			case VALUE:
				return singleValue(seed, x, y, z);
			case VALUE_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleValueFractalFractionalBrownian(x, y, z);
					case BILLOW:
						return singleValueFractalBillow(x, y, z);
					case RIGID_MULTIPLE:
						return singleValueFractalRigidMultiple(x, y, z);
					default:
						return 0;
				}
			case PERLIN:
				return singlePerlin(seed, x, y, z);
			case PERLIN_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singlePerlinFractalFractionalBrownian(x, y, z);
					case BILLOW:
						return singlePerlinFractalBillow(x, y, z);
					case RIGID_MULTIPLE:
						return singlePerlinFractalRigidMultiple(x, y, z);
					default:
						return 0;
				}
			case SIMPLEX:
				return singleSimplex(seed, x, y, z);
			case SIMPLEX_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleSimplexFractalFractionalBrownian(x, y, z);
					case BILLOW:
						return singleSimplexFractalBillow(x, y, z);
					case RIGID_MULTIPLE:
						return singleSimplexFractalRigidMultiple(x, y, z);
					default:
						return 0;
				}
			case CELLULAR:
				switch (cellularReturnType) {
					case CELL_VALUE:
					case NOISE_LOOKUP:
					case DISTANCE:
						return singleCellular(x, y, z);
					default:
						return singleCellularToEdge(x, y, z);
				}
			case WHITE:
				return getWhiteNoise(x, y, z);
			case CUBIC:
				return singleCubic(seed, x, y, z);
			case CUBIC_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleCubicFractalFractionalBrownian(x, y, z);
					case BILLOW:
						return singleCubicFractalBillow(x, y, z);
					case RIGID_MULTIPLE:
						return singleCubicFractalRigidMultiple(x, y, z);
					default:
						return 0;
				}
			default:
				return 0;
		}
	}

	public float getNoise(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (noiseType) {
			case VALUE:
				return singleValue(seed, x, y);
			case VALUE_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleValueFractalFractionalBrownian(x, y);
					case BILLOW:
						return singleValueFractalBillow(x, y);
					case RIGID_MULTIPLE:
						return singleValueFractalRigidMultiple(x, y);
					default:
						return 0;
				}
			case PERLIN:
				return singlePerlin(seed, x, y);
			case PERLIN_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singlePerlinFractalFractionalBrownian(x, y);
					case BILLOW:
						return singlePerlinFractalBillow(x, y);
					case RIGID_MULTIPLE:
						return singlePerlinFractalRigidMultiple(x, y);
					default:
						return 0;
				}
			case SIMPLEX:
				return singleSimplex(seed, x, y);
			case SIMPLEX_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleSimplexFractalFractionalBrownian(x, y);
					case BILLOW:
						return singleSimplexFractalBillow(x, y);
					case RIGID_MULTIPLE:
						return singleSimplexFractalRigidMultiple(x, y);
					default:
						return 0;
				}
			case CELLULAR:
				switch (cellularReturnType) {
					case CELL_VALUE:
					case NOISE_LOOKUP:
					case DISTANCE:
						return singleCellular(x, y);
					default:
						return singleCellularToEdge(x, y);
				}
			case WHITE:
				return getWhiteNoise(x, y);
			case CUBIC:
				return singleCubic(seed, x, y);
			case CUBIC_FRACTAL:
				switch (fractalType) {
					case FRACTIONAL_BROWNIAN:
						return singleCubicFractalFractionalBrownian(x, y);
					case BILLOW:
						return singleCubicFractalBillow(x, y);
					case RIGID_MULTIPLE:
						return singleCubicFractalRigidMultiple(x, y);
					default:
						return 0;
				}
			default:
				return 0;
		}
	}

	private int floatToInt(float f) {
		int i = Float.floatToRawIntBits(f);

		return i ^ (i >> 16);
	}

	public float getWhiteNoise(float x, float y, float z, float w) {
		int xi = floatToInt(x);
		int yi = floatToInt(y);
		int zi = floatToInt(z);
		int wi = floatToInt(w);

		return fourDimensionalCoordinateValue(seed, xi, yi, zi, wi);
	}

	public float getWhiteNoise(float x, float y, float z) {
		int xi = floatToInt(x);
		int yi = floatToInt(y);
		int zi = floatToInt(z);

		return threeDimensionalCoordinateValue(seed, xi, yi, zi);
	}

	public float getWhiteNoise(float x, float y) {
		int xi = floatToInt(x);
		int yi = floatToInt(y);

		return twoDimensionalCoordinateValue(seed, xi, yi);
	}

	public float getWhiteNoiseInteger(int x, int y, int z, int w) {
		return fourDimensionalCoordinateValue(seed, x, y, z, w);
	}

	public float getWhiteNoiseInteger(int x, int y, int z) {
		return threeDimensionalCoordinateValue(seed, x, y, z);
	}

	public float getWhiteNoiseInteger(int x, int y) {
		return twoDimensionalCoordinateValue(seed, x, y);
	}

	// Value Noise
	public float getValueFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleValueFractalFractionalBrownian(x, y, z);
			case BILLOW:
				return singleValueFractalBillow(x, y, z);
			case RIGID_MULTIPLE:
				return singleValueFractalRigidMultiple(x, y, z);
			default:
				return 0;
		}
	}

	private float singleValueFractalFractionalBrownian(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleValue(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleValue(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleValue(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleValue(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalRigidMultiple(float x, float y, float z) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleValue(seed, x, y, z));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleValue(++seed, x, y, z))) * amp;
		}

		return sum;
	}

	public float getValue(float x, float y, float z) {
		return singleValue(seed, x * frequency, y * frequency, z * frequency);
	}

	private float singleValue(int seed, float x, float y, float z) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int z0 = fastFloor(z);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = x - x0;
				ys = y - y0;
				zs = z - z0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(x - x0);
				ys = interpolateHermiteFunction(y - y0);
				zs = interpolateHermiteFunction(z - z0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(x - x0);
				ys = interpolateQuinticFunction(y - y0);
				zs = interpolateQuinticFunction(z - z0);
				break;
		}

		float xf00 = interpolateLinear(threeDimensionalCoordinateValue(seed, x0, y0, z0), threeDimensionalCoordinateValue(seed, x1, y0, z0), xs);
		float xf10 = interpolateLinear(threeDimensionalCoordinateValue(seed, x0, y1, z0), threeDimensionalCoordinateValue(seed, x1, y1, z0), xs);
		float xf01 = interpolateLinear(threeDimensionalCoordinateValue(seed, x0, y0, z1), threeDimensionalCoordinateValue(seed, x1, y0, z1), xs);
		float xf11 = interpolateLinear(threeDimensionalCoordinateValue(seed, x0, y1, z1), threeDimensionalCoordinateValue(seed, x1, y1, z1), xs);

		float yf0 = interpolateLinear(xf00, xf10, ys);
		float yf1 = interpolateLinear(xf01, xf11, ys);

		return interpolateLinear(yf0, yf1, zs);
	}

	public float getValueFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleValueFractalFractionalBrownian(x, y);
			case BILLOW:
				return singleValueFractalBillow(x, y);
			case RIGID_MULTIPLE:
				return singleValueFractalRigidMultiple(x, y);
			default:
				return 0;
		}
	}

	private float singleValueFractalFractionalBrownian(float x, float y) {
		int seed = this.seed;
		float sum = singleValue(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleValue(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleValue(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			amp *= gain;
			sum += (Math.abs(singleValue(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleValueFractalRigidMultiple(float x, float y) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleValue(seed, x, y));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleValue(++seed, x, y))) * amp;
		}

		return sum;
	}

	public float getValue(float x, float y) {
		return singleValue(seed, x * frequency, y * frequency);
	}

	private float singleValue(int seed, float x, float y) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = x - x0;
				ys = y - y0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(x - x0);
				ys = interpolateHermiteFunction(y - y0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(x - x0);
				ys = interpolateQuinticFunction(y - y0);
				break;
		}

		float xf0 = interpolateLinear(twoDimensionalCoordinateValue(seed, x0, y0), twoDimensionalCoordinateValue(seed, x1, y0), xs);
		float xf1 = interpolateLinear(twoDimensionalCoordinateValue(seed, x0, y1), twoDimensionalCoordinateValue(seed, x1, y1), xs);

		return interpolateLinear(xf0, xf1, ys);
	}

	// Gradient Noise
	public float getPerlinFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singlePerlinFractalFractionalBrownian(x, y, z);
			case BILLOW:
				return singlePerlinFractalBillow(x, y, z);
			case RIGID_MULTIPLE:
				return singlePerlinFractalRigidMultiple(x, y, z);
			default:
				return 0;
		}
	}

	private float singlePerlinFractalFractionalBrownian(float x, float y, float z) {
		int seed = this.seed;
		float sum = singlePerlin(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singlePerlin(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singlePerlin(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singlePerlin(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalRigidMultiple(float x, float y, float z) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singlePerlin(seed, x, y, z));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singlePerlin(++seed, x, y, z))) * amp;
		}

		return sum;
	}

	public float getPerlin(float x, float y, float z) {
		return singlePerlin(seed, x * frequency, y * frequency, z * frequency);
	}

	private float singlePerlin(int seed, float x, float y, float z) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int z0 = fastFloor(z);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = x - x0;
				ys = y - y0;
				zs = z - z0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(x - x0);
				ys = interpolateHermiteFunction(y - y0);
				zs = interpolateHermiteFunction(z - z0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(x - x0);
				ys = interpolateQuinticFunction(y - y0);
				zs = interpolateQuinticFunction(z - z0);
				break;
		}

		float xd0 = x - x0;
		float yd0 = y - y0;
		float zd0 = z - z0;
		float xd1 = xd0 - 1;
		float yd1 = yd0 - 1;
		float zd1 = zd0 - 1;

		float xf00 = interpolateLinear(threeDimensionalCoordinateGradient(seed, x0, y0, z0, xd0, yd0, zd0), threeDimensionalCoordinateGradient(seed, x1, y0, z0, xd1, yd0, zd0), xs);
		float xf10 = interpolateLinear(threeDimensionalCoordinateGradient(seed, x0, y1, z0, xd0, yd1, zd0), threeDimensionalCoordinateGradient(seed, x1, y1, z0, xd1, yd1, zd0), xs);
		float xf01 = interpolateLinear(threeDimensionalCoordinateGradient(seed, x0, y0, z1, xd0, yd0, zd1), threeDimensionalCoordinateGradient(seed, x1, y0, z1, xd1, yd0, zd1), xs);
		float xf11 = interpolateLinear(threeDimensionalCoordinateGradient(seed, x0, y1, z1, xd0, yd1, zd1), threeDimensionalCoordinateGradient(seed, x1, y1, z1, xd1, yd1, zd1), xs);

		float yf0 = interpolateLinear(xf00, xf10, ys);
		float yf1 = interpolateLinear(xf01, xf11, ys);

		return interpolateLinear(yf0, yf1, zs);
	}

	public float getPerlinFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singlePerlinFractalFractionalBrownian(x, y);
			case BILLOW:
				return singlePerlinFractalBillow(x, y);
			case RIGID_MULTIPLE:
				return singlePerlinFractalRigidMultiple(x, y);
			default:
				return 0;
		}
	}

	private float singlePerlinFractalFractionalBrownian(float x, float y) {
		int seed = this.seed;
		float sum = singlePerlin(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singlePerlin(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singlePerlin(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singlePerlin(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singlePerlinFractalRigidMultiple(float x, float y) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singlePerlin(seed, x, y));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singlePerlin(++seed, x, y))) * amp;
		}

		return sum;
	}

	public float getPerlin(float x, float y) {
		return singlePerlin(seed, x * frequency, y * frequency);
	}

	private float singlePerlin(int seed, float x, float y) {
		int x0 = fastFloor(x);
		int y0 = fastFloor(y);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = x - x0;
				ys = y - y0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(x - x0);
				ys = interpolateHermiteFunction(y - y0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(x - x0);
				ys = interpolateQuinticFunction(y - y0);
				break;
		}

		float xd0 = x - x0;
		float yd0 = y - y0;
		float xd1 = xd0 - 1;
		float yd1 = yd0 - 1;

		float xf0 = interpolateLinear(twoDimensionalCoordinateGradient(seed, x0, y0, xd0, yd0), twoDimensionalCoordinateGradient(seed, x1, y0, xd1, yd0), xs);
		float xf1 = interpolateLinear(twoDimensionalCoordinateGradient(seed, x0, y1, xd0, yd1), twoDimensionalCoordinateGradient(seed, x1, y1, xd1, yd1), xs);

		return interpolateLinear(xf0, xf1, ys);
	}

	// Simplex Noise
	public float getSimplexFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleSimplexFractalFractionalBrownian(x, y, z);
			case BILLOW:
				return singleSimplexFractalBillow(x, y, z);
			case RIGID_MULTIPLE:
				return singleSimplexFractalRigidMultiple(x, y, z);
			default:
				return 0;
		}
	}

	private float singleSimplexFractalFractionalBrownian(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleSimplex(seed, x, y, z);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleSimplex(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleSimplexFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleSimplex(seed, x, y, z)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleSimplex(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleSimplexFractalRigidMultiple(float x, float y, float z) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleSimplex(seed, x, y, z));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleSimplex(++seed, x, y, z))) * amp;
		}

		return sum;
	}

	public float getSimplex(float x, float y, float z) {
		return singleSimplex(seed, x * frequency, y * frequency, z * frequency);
	}

	private float singleSimplex(int seed, float x, float y, float z) {
		float t = (x + y + z) * F3;
		int i = fastFloor(x + t);
		int j = fastFloor(y + t);
		int k = fastFloor(z + t);

		t = (i + j + k) * G3;
		float x0 = x - (i - t);
		float y0 = y - (j - t);
		float z0 = z - (k - t);

		int i1, j1, k1;
		int i2, j2, k2;

		if (x0 >= y0) {
			if (y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} else if (x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} else // x0 < z0
			{
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
		} else // x0 < y0
		{
			if (y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else if (x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else // x0 >= z0
			{
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			}
		}

		float x1 = x0 - i1 + G3;
		float y1 = y0 - j1 + G3;
		float z1 = z0 - k1 + G3;
		float x2 = x0 - i2 + F3;
		float y2 = y0 - j2 + F3;
		float z2 = z0 - k2 + F3;
		float x3 = x0 + G33;
		float y3 = y0 + G33;
		float z3 = z0 + G33;

		float n0, n1, n2, n3;

		t = (float) 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
		if (t < 0)
			n0 = 0;
		else {
			t *= t;
			n0 = t * t * threeDimensionalCoordinateGradient(seed, i, j, k, x0, y0, z0);
		}

		t = (float) 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
		if (t < 0)
			n1 = 0;
		else {
			t *= t;
			n1 = t * t * threeDimensionalCoordinateGradient(seed, i + i1, j + j1, k + k1, x1, y1, z1);
		}

		t = (float) 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
		if (t < 0)
			n2 = 0;
		else {
			t *= t;
			n2 = t * t * threeDimensionalCoordinateGradient(seed, i + i2, j + j2, k + k2, x2, y2, z2);
		}

		t = (float) 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
		if (t < 0)
			n3 = 0;
		else {
			t *= t;
			n3 = t * t * threeDimensionalCoordinateGradient(seed, i + 1, j + 1, k + 1, x3, y3, z3);
		}

		return 32 * (n0 + n1 + n2 + n3);
	}

	public float getSimplexFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleSimplexFractalFractionalBrownian(x, y);
			case BILLOW:
				return singleSimplexFractalBillow(x, y);
			case RIGID_MULTIPLE:
				return singleSimplexFractalRigidMultiple(x, y);
			default:
				return 0;
		}
	}

	private float singleSimplexFractalFractionalBrownian(float x, float y) {
		int seed = this.seed;
		float sum = singleSimplex(seed, x, y);
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleSimplex(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	// private final static float F2 = (float) (1.0 / 2.0);
	// private final static float G2 = (float) (1.0 / 4.0);

	private float singleSimplexFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleSimplex(seed, x, y)) * 2 - 1;
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleSimplex(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleSimplexFractalRigidMultiple(float x, float y) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleSimplex(seed, x, y));
		float amp = 1;

		for (int i = 1; i < octaves; i++) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleSimplex(++seed, x, y))) * amp;
		}

		return sum;
	}

	public float getSimplex(float x, float y) {
		return singleSimplex(seed, x * frequency, y * frequency);
	}

	private float singleSimplex(int seed, float x, float y) {
		float t = (x + y) * F2;
		int i = fastFloor(x + t);
		int j = fastFloor(y + t);

		t = (i + j) * G2;
		float X0 = i - t;
		float Y0 = j - t;

		float x0 = x - X0;
		float y0 = y - Y0;

		int i1, j1;
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} else {
			i1 = 0;
			j1 = 1;
		}

		float x1 = x0 - i1 + G2;
		float y1 = y0 - j1 + G2;
		float x2 = x0 - 1 + 2 * G2;
		float y2 = y0 - 1 + 2 * G2;

		float n0, n1, n2;

		t = (float) 0.5 - x0 * x0 - y0 * y0;
		if (t < 0)
			n0 = 0;
		else {
			t *= t;
			n0 = t * t * twoDimensionalCoordinateGradient(seed, i, j, x0, y0);
		}

		t = (float) 0.5 - x1 * x1 - y1 * y1;
		if (t < 0)
			n1 = 0;
		else {
			t *= t;
			n1 = t * t * twoDimensionalCoordinateGradient(seed, i + i1, j + j1, x1, y1);
		}

		t = (float) 0.5 - x2 * x2 - y2 * y2;
		if (t < 0)
			n2 = 0;
		else {
			t *= t;
			n2 = t * t * twoDimensionalCoordinateGradient(seed, i + 1, j + 1, x2, y2);
		}

		return 50 * (n0 + n1 + n2);
	}

	public float getSimplex(float x, float y, float z, float w) {
		return singleSimplex(seed, x * frequency, y * frequency, z * frequency, w * frequency);
	}

	private float singleSimplex(int seed, float x, float y, float z, float w) {
		float n0, n1, n2, n3, n4;
		float t = (x + y + z + w) * F4;
		int i = fastFloor(x + t);
		int j = fastFloor(y + t);
		int k = fastFloor(z + t);
		int l = fastFloor(w + t);
		t = (i + j + k + l) * G4;
		float X0 = i - t;
		float Y0 = j - t;
		float Z0 = k - t;
		float W0 = l - t;
		float x0 = x - X0;
		float y0 = y - Y0;
		float z0 = z - Z0;
		float w0 = w - W0;

		int c = (x0 > y0) ? 32 : 0;
		c += (x0 > z0) ? 16 : 0;
		c += (y0 > z0) ? 8 : 0;
		c += (x0 > w0) ? 4 : 0;
		c += (y0 > w0) ? 2 : 0;
		c += (z0 > w0) ? 1 : 0;
		c <<= 2;

		int i1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
		int i2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
		int i3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
		int j1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
		int j2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
		int j3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
		int k1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
		int k2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
		int k3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
		int l1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
		int l2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
		int l3 = SIMPLEX_4D[c] >= 1 ? 1 : 0;

		float x1 = x0 - i1 + G4;
		float y1 = y0 - j1 + G4;
		float z1 = z0 - k1 + G4;
		float w1 = w0 - l1 + G4;
		float x2 = x0 - i2 + 2 * G4;
		float y2 = y0 - j2 + 2 * G4;
		float z2 = z0 - k2 + 2 * G4;
		float w2 = w0 - l2 + 2 * G4;
		float x3 = x0 - i3 + 3 * G4;
		float y3 = y0 - j3 + 3 * G4;
		float z3 = z0 - k3 + 3 * G4;
		float w3 = w0 - l3 + 3 * G4;
		float x4 = x0 - 1 + 4 * G4;
		float y4 = y0 - 1 + 4 * G4;
		float z4 = z0 - 1 + 4 * G4;
		float w4 = w0 - 1 + 4 * G4;

		t = (float) 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		if (t < 0)
			n0 = 0;
		else {
			t *= t;
			n0 = t * t * fourDimensionalCoordinateGradient(seed, i, j, k, l, x0, y0, z0, w0);
		}
		t = (float) 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		if (t < 0)
			n1 = 0;
		else {
			t *= t;
			n1 = t * t * fourDimensionalCoordinateGradient(seed, i + i1, j + j1, k + k1, l + l1, x1, y1, z1, w1);
		}
		t = (float) 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		if (t < 0)
			n2 = 0;
		else {
			t *= t;
			n2 = t * t * fourDimensionalCoordinateGradient(seed, i + i2, j + j2, k + k2, l + l2, x2, y2, z2, w2);
		}
		t = (float) 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		if (t < 0)
			n3 = 0;
		else {
			t *= t;
			n3 = t * t * fourDimensionalCoordinateGradient(seed, i + i3, j + j3, k + k3, l + l3, x3, y3, z3, w3);
		}
		t = (float) 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		if (t < 0)
			n4 = 0;
		else {
			t *= t;
			n4 = t * t * fourDimensionalCoordinateGradient(seed, i + 1, j + 1, k + 1, l + 1, x4, y4, z4, w4);
		}

		return 27 * (n0 + n1 + n2 + n3 + n4);
	}

	// Cubic Noise
	public float cubicFractal(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleCubicFractalFractionalBrownian(x, y, z);
			case BILLOW:
				return singleCubicFractalBillow(x, y, z);
			case RIGID_MULTIPLE:
				return singleCubicFractalRigidMultiple(x, y, z);
			default:
				return 0;
		}
	}

	private float singleCubicFractalFractionalBrownian(float x, float y, float z) {
		int seed = this.seed;
		float sum = singleCubic(seed, x, y, z);
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += singleCubic(++seed, x, y, z) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalBillow(float x, float y, float z) {
		int seed = this.seed;
		float sum = Math.abs(singleCubic(seed, x, y, z)) * 2 - 1;
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleCubic(++seed, x, y, z)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalRigidMultiple(float x, float y, float z) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleCubic(seed, x, y, z));
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleCubic(++seed, x, y, z))) * amp;
		}

		return sum;
	}

	public float getCubic(float x, float y, float z) {
		return singleCubic(seed, x * frequency, y * frequency, z * frequency);
	}

	private float singleCubic(int seed, float x, float y, float z) {
		int x1 = fastFloor(x);
		int y1 = fastFloor(y);
		int z1 = fastFloor(z);

		int x0 = x1 - 1;
		int y0 = y1 - 1;
		int z0 = z1 - 1;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;
		int x3 = x1 + 2;
		int y3 = y1 + 2;
		int z3 = z1 + 2;

		float xs = x - (float) x1;
		float ys = y - (float) y1;
		float zs = z - (float) z1;

		return interpolateLinearCubic(interpolateLinearCubic(interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y0, z0), threeDimensionalCoordinateValue(seed, x1, y0, z0), threeDimensionalCoordinateValue(seed, x2, y0, z0), threeDimensionalCoordinateValue(seed, x3,
			y0, z0), xs), interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y1, z0), threeDimensionalCoordinateValue(seed, x1, y1, z0), threeDimensionalCoordinateValue(seed, x2, y1, z0), threeDimensionalCoordinateValue(seed, x3, y1, z0), xs),
			interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y2, z0), threeDimensionalCoordinateValue(seed, x1, y2, z0), threeDimensionalCoordinateValue(seed, x2, y2, z0), threeDimensionalCoordinateValue(seed, x3, y2, z0), xs), interpolateLinearCubic(
				threeDimensionalCoordinateValue(seed, x0, y3, z0), threeDimensionalCoordinateValue(seed, x1, y3, z0), threeDimensionalCoordinateValue(seed, x2, y3, z0), threeDimensionalCoordinateValue(seed, x3, y3, z0), xs), ys), interpolateLinearCubic(interpolateLinearCubic(
					threeDimensionalCoordinateValue(seed, x0, y0, z1), threeDimensionalCoordinateValue(seed, x1, y0, z1), threeDimensionalCoordinateValue(seed, x2, y0, z1), threeDimensionalCoordinateValue(seed, x3, y0, z1), xs), interpolateLinearCubic(
						threeDimensionalCoordinateValue(seed, x0, y1, z1), threeDimensionalCoordinateValue(seed, x1, y1, z1), threeDimensionalCoordinateValue(seed, x2, y1, z1), threeDimensionalCoordinateValue(seed, x3, y1, z1), xs), interpolateLinearCubic(
							threeDimensionalCoordinateValue(seed, x0, y2, z1), threeDimensionalCoordinateValue(seed, x1, y2, z1), threeDimensionalCoordinateValue(seed, x2, y2, z1), threeDimensionalCoordinateValue(seed, x3, y2, z1), xs), interpolateLinearCubic(
								threeDimensionalCoordinateValue(seed, x0, y3, z1), threeDimensionalCoordinateValue(seed, x1, y3, z1), threeDimensionalCoordinateValue(seed, x2, y3, z1), threeDimensionalCoordinateValue(seed, x3, y3, z1), xs), ys), interpolateLinearCubic(
									interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y0, z2), threeDimensionalCoordinateValue(seed, x1, y0, z2), threeDimensionalCoordinateValue(seed, x2, y0, z2), threeDimensionalCoordinateValue(seed, x3, y0, z2), xs),
									interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y1, z2), threeDimensionalCoordinateValue(seed, x1, y1, z2), threeDimensionalCoordinateValue(seed, x2, y1, z2), threeDimensionalCoordinateValue(seed, x3, y1, z2), xs),
									interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y2, z2), threeDimensionalCoordinateValue(seed, x1, y2, z2), threeDimensionalCoordinateValue(seed, x2, y2, z2), threeDimensionalCoordinateValue(seed, x3, y2, z2), xs),
									interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y3, z2), threeDimensionalCoordinateValue(seed, x1, y3, z2), threeDimensionalCoordinateValue(seed, x2, y3, z2), threeDimensionalCoordinateValue(seed, x3, y3, z2), xs), ys),
			interpolateLinearCubic(interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y0, z3), threeDimensionalCoordinateValue(seed, x1, y0, z3), threeDimensionalCoordinateValue(seed, x2, y0, z3), threeDimensionalCoordinateValue(seed, x3, y0, z3), xs),
				interpolateLinearCubic(threeDimensionalCoordinateValue(seed, x0, y1, z3), threeDimensionalCoordinateValue(seed, x1, y1, z3), threeDimensionalCoordinateValue(seed, x2, y1, z3), threeDimensionalCoordinateValue(seed, x3, y1, z3), xs), interpolateLinearCubic(
					threeDimensionalCoordinateValue(seed, x0, y2, z3), threeDimensionalCoordinateValue(seed, x1, y2, z3), threeDimensionalCoordinateValue(seed, x2, y2, z3), threeDimensionalCoordinateValue(seed, x3, y2, z3), xs), interpolateLinearCubic(
						threeDimensionalCoordinateValue(seed, x0, y3, z3), threeDimensionalCoordinateValue(seed, x1, y3, z3), threeDimensionalCoordinateValue(seed, x2, y3, z3), threeDimensionalCoordinateValue(seed, x3, y3, z3), xs), ys), zs) * CUBIC_3D_BOUNDING;
	}

	public float cubicFractal(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (fractalType) {
			case FRACTIONAL_BROWNIAN:
				return singleCubicFractalFractionalBrownian(x, y);
			case BILLOW:
				return singleCubicFractalBillow(x, y);
			case RIGID_MULTIPLE:
				return singleCubicFractalRigidMultiple(x, y);
			default:
				return 0;
		}
	}

	private float singleCubicFractalFractionalBrownian(float x, float y) {
		int seed = this.seed;
		float sum = singleCubic(seed, x, y);
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += singleCubic(++seed, x, y) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalBillow(float x, float y) {
		int seed = this.seed;
		float sum = Math.abs(singleCubic(seed, x, y)) * 2 - 1;
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum += (Math.abs(singleCubic(++seed, x, y)) * 2 - 1) * amp;
		}

		return sum * fractalBounding;
	}

	private float singleCubicFractalRigidMultiple(float x, float y) {
		int seed = this.seed;
		float sum = 1 - Math.abs(singleCubic(seed, x, y));
		float amp = 1;
		int i = 0;

		while (++i < octaves) {
			x *= lacunarity;
			y *= lacunarity;

			amp *= gain;
			sum -= (1 - Math.abs(singleCubic(++seed, x, y))) * amp;
		}

		return sum;
	}

	public float getCubic(float x, float y) {
		x *= frequency;
		y *= frequency;

		return singleCubic(0, x, y);
	}

	private float singleCubic(int seed, float x, float y) {
		int x1 = fastFloor(x);
		int y1 = fastFloor(y);

		int x0 = x1 - 1;
		int y0 = y1 - 1;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int x3 = x1 + 2;
		int y3 = y1 + 2;

		float xs = x - (float) x1;
		float ys = y - (float) y1;

		return interpolateLinearCubic(interpolateLinearCubic(twoDimensionalCoordinateValue(seed, x0, y0), twoDimensionalCoordinateValue(seed, x1, y0), twoDimensionalCoordinateValue(seed, x2, y0), twoDimensionalCoordinateValue(seed, x3, y0), xs), interpolateLinearCubic(
			twoDimensionalCoordinateValue(seed, x0, y1), twoDimensionalCoordinateValue(seed, x1, y1), twoDimensionalCoordinateValue(seed, x2, y1), twoDimensionalCoordinateValue(seed, x3, y1), xs), interpolateLinearCubic(twoDimensionalCoordinateValue(seed, x0, y2),
				twoDimensionalCoordinateValue(seed, x1, y2), twoDimensionalCoordinateValue(seed, x2, y2), twoDimensionalCoordinateValue(seed, x3, y2), xs), interpolateLinearCubic(twoDimensionalCoordinateValue(seed, x0, y3), twoDimensionalCoordinateValue(seed, x1, y3),
					twoDimensionalCoordinateValue(seed, x2, y3), twoDimensionalCoordinateValue(seed, x3, y3), xs), ys) * CUBIC_2D_BOUNDING;
	}

	// Cellular Noise
	public float getCellular(float x, float y, float z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		switch (cellularReturnType) {
			case CELL_VALUE:
			case NOISE_LOOKUP:
			case DISTANCE:
				return singleCellular(x, y, z);
			default:
				return singleCellularToEdge(x, y, z);
		}
	}

	private float singleCellular(float x, float y, float z) {
		int xr = fastRound(x);
		int yr = fastRound(y);
		int zr = fastRound(z);

		float distance = 999999;
		int xc = 0, yc = 0, zc = 0;

		switch (cellularDistanceFunction) {
			case EUCLIDEAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

							if (newDistance < distance) {
								distance = newDistance;
								xc = xi;
								yc = yi;
								zc = zi;
							}
						}
					}
				}
				break;
			case MANHATTAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

							if (newDistance < distance) {
								distance = newDistance;
								xc = xi;
								yc = yi;
								zc = zi;
							}
						}
					}
				}
				break;
			case NATURAL:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);

							if (newDistance < distance) {
								distance = newDistance;
								xc = xi;
								yc = yi;
								zc = zi;
							}
						}
					}
				}
				break;
		}

		switch (cellularReturnType) {
			case CELL_VALUE:
				return threeDimensionalCoordinateValue(0, xc, yc, zc);

			case NOISE_LOOKUP:
				Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xc, yc, zc) & 255];
				return cellularNoiseLookup.getNoise(xc + vec.x, yc + vec.y, zc + vec.z);

			case DISTANCE:
				return distance - 1;
			default:
				return 0;
		}
	}

	private float singleCellularToEdge(float x, float y, float z) {
		int xr = fastRound(x);
		int yr = fastRound(y);
		int zr = fastRound(z);

		float distance = 999999;
		float distance2 = 999999;

		switch (cellularDistanceFunction) {
			case EUCLIDEAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

							distance2 = Math.max(Math.min(distance2, newDistance), distance);
							distance = Math.min(distance, newDistance);
						}
					}
				}
				break;
			case MANHATTAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

							distance2 = Math.max(Math.min(distance2, newDistance), distance);
							distance = Math.min(distance, newDistance);
						}
					}
				}
				break;
			case NATURAL:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						for (int zi = zr - 1; zi <= zr + 1; zi++) {
							Vector3f vec = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, xi, yi, zi) & 255];

							float vecX = xi - x + vec.x;
							float vecY = yi - y + vec.y;
							float vecZ = zi - z + vec.z;

							float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);

							distance2 = Math.max(Math.min(distance2, newDistance), distance);
							distance = Math.min(distance, newDistance);
						}
					}
				}
				break;
			default:
				break;
		}

		switch (cellularReturnType) {
			case DISTANCE_TO:
				return distance2 - 1;
			case DISTANCE_TO_ADDITION:
				return distance2 + distance - 1;
			case DISTANCE_TO_SUBTRACTION:
				return distance2 - distance - 1;
			case DISTANCE_TO_MULTIPLICATION:
				return distance2 * distance - 1;
			case DISTANCE_TO_DIVISION:
				return distance / distance2 - 1;
			default:
				return 0;
		}
	}

	public float getCellular(float x, float y) {
		x *= frequency;
		y *= frequency;

		switch (cellularReturnType) {
			case CELL_VALUE:
			case NOISE_LOOKUP:
			case DISTANCE:
				return singleCellular(x, y);
			default:
				return singleCellularToEdge(x, y);
		}
	}

	private float singleCellular(float x, float y) {
		int xr = fastRound(x);
		int yr = fastRound(y);

		float distance = 999999;
		int xc = 0, yc = 0;

		switch (cellularDistanceFunction) {
			default:
			case EUCLIDEAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = vecX * vecX + vecY * vecY;

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
						}
					}
				}
				break;
			case MANHATTAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = (Math.abs(vecX) + Math.abs(vecY));

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
						}
					}
				}
				break;
			case NATURAL:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

						if (newDistance < distance) {
							distance = newDistance;
							xc = xi;
							yc = yi;
						}
					}
				}
				break;
		}

		switch (cellularReturnType) {
			case CELL_VALUE:
				return twoDimensionalCoordinateValue(0, xc, yc);

			case NOISE_LOOKUP:
				Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xc, yc) & 255];
				return cellularNoiseLookup.getNoise(xc + vec.x, yc + vec.y);

			case DISTANCE:
				return distance - 1;
			default:
				return 0;
		}
	}

	private float singleCellularToEdge(float x, float y) {
		int xr = fastRound(x);
		int yr = fastRound(y);

		float distance = 999999;
		float distance2 = 999999;

		switch (cellularDistanceFunction) {
			default:
			case EUCLIDEAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = vecX * vecX + vecY * vecY;

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
				break;
			case MANHATTAN:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = Math.abs(vecX) + Math.abs(vecY);

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
				break;
			case NATURAL:
				for (int xi = xr - 1; xi <= xr + 1; xi++) {
					for (int yi = yr - 1; yi <= yr + 1; yi++) {
						Vector2f vec = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, xi, yi) & 255];

						float vecX = xi - x + vec.x;
						float vecY = yi - y + vec.y;

						float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

						distance2 = Math.max(Math.min(distance2, newDistance), distance);
						distance = Math.min(distance, newDistance);
					}
				}
				break;
		}

		switch (cellularReturnType) {
			case DISTANCE_TO:
				return distance2 - 1;
			case DISTANCE_TO_ADDITION:
				return distance2 + distance - 1;
			case DISTANCE_TO_SUBTRACTION:
				return distance2 - distance - 1;
			case DISTANCE_TO_MULTIPLICATION:
				return distance2 * distance - 1;
			case DISTANCE_TO_DIVISION:
				return distance / distance2 - 1;
			default:
				return 0;
		}
	}

	public void gradientPerturbed(Vector3f v3) {
		singleGradientPerturbed(seed, gradientPerturbAmp, frequency, v3);
	}

	public void gradientPerturbedFractal(Vector3f v3) {
		int seed = this.seed;
		float amp = gradientPerturbAmp * fractalBounding;
		float freq = frequency;

		singleGradientPerturbed(seed, amp, frequency, v3);

		for (int i = 1; i < octaves; i++) {
			freq *= lacunarity;
			amp *= gain;
			singleGradientPerturbed(++seed, amp, freq, v3);
		}
	}

	private void singleGradientPerturbed(int seed, float perturbAmp, float frequency, Vector3f v3) {
		float xf = v3.x * frequency;
		float yf = v3.y * frequency;
		float zf = v3.z * frequency;

		int x0 = fastFloor(xf);
		int y0 = fastFloor(yf);
		int z0 = fastFloor(zf);
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		int z1 = z0 + 1;

		float xs, ys, zs;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = xf - x0;
				ys = yf - y0;
				zs = zf - z0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(xf - x0);
				ys = interpolateHermiteFunction(yf - y0);
				zs = interpolateHermiteFunction(zf - z0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(xf - x0);
				ys = interpolateQuinticFunction(yf - y0);
				zs = interpolateQuinticFunction(zf - z0);
				break;
		}

		Vector3f vec0 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x0, y0, z0) & 255];
		Vector3f vec1 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x1, y0, z0) & 255];

		float lx0x = interpolateLinear(vec0.x, vec1.x, xs);
		float ly0x = interpolateLinear(vec0.y, vec1.y, xs);
		float lz0x = interpolateLinear(vec0.z, vec1.z, xs);

		vec0 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x0, y1, z0) & 255];
		vec1 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x1, y1, z0) & 255];

		float lx1x = interpolateLinear(vec0.x, vec1.x, xs);
		float ly1x = interpolateLinear(vec0.y, vec1.y, xs);
		float lz1x = interpolateLinear(vec0.z, vec1.z, xs);

		float lx0y = interpolateLinear(lx0x, lx1x, ys);
		float ly0y = interpolateLinear(ly0x, ly1x, ys);
		float lz0y = interpolateLinear(lz0x, lz1x, ys);

		vec0 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x0, y0, z1) & 255];
		vec1 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x1, y0, z1) & 255];

		lx0x = interpolateLinear(vec0.x, vec1.x, xs);
		ly0x = interpolateLinear(vec0.y, vec1.y, xs);
		lz0x = interpolateLinear(vec0.z, vec1.z, xs);

		vec0 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x0, y1, z1) & 255];
		vec1 = THREE_DIMENSIONAL_CELLULAR[threeDimensionalHash(seed, x1, y1, z1) & 255];

		lx1x = interpolateLinear(vec0.x, vec1.x, xs);
		ly1x = interpolateLinear(vec0.y, vec1.y, xs);
		lz1x = interpolateLinear(vec0.z, vec1.z, xs);

		v3.x += interpolateLinear(lx0y, interpolateLinear(lx0x, lx1x, ys), zs) * perturbAmp;
		v3.y += interpolateLinear(ly0y, interpolateLinear(ly0x, ly1x, ys), zs) * perturbAmp;
		v3.z += interpolateLinear(lz0y, interpolateLinear(lz0x, lz1x, ys), zs) * perturbAmp;
	}

	public void gradientPerturbed(Vector2f v2) {
		singleGradientPerturbed(seed, gradientPerturbAmp, frequency, v2);
	}

	public void gradientPerturbedFractal(Vector2f v2) {
		int seed = this.seed;
		float amp = gradientPerturbAmp * fractalBounding;
		float freq = frequency;

		singleGradientPerturbed(seed, amp, frequency, v2);

		for (int i = 1; i < octaves; i++) {
			freq *= lacunarity;
			amp *= gain;
			singleGradientPerturbed(++seed, amp, freq, v2);
		}
	}

	private void singleGradientPerturbed(int seed, float perturbAmp, float frequency, Vector2f v2) {
		float xf = v2.x * frequency;
		float yf = v2.y * frequency;

		int x0 = fastFloor(xf);
		int y0 = fastFloor(yf);
		int x1 = x0 + 1;
		int y1 = y0 + 1;

		float xs, ys;
		switch (interpolation) {
			default:
			case LINEAR:
				xs = xf - x0;
				ys = yf - y0;
				break;
			case HERMITE:
				xs = interpolateHermiteFunction(xf - x0);
				ys = interpolateHermiteFunction(yf - y0);
				break;
			case QUINTIC:
				xs = interpolateQuinticFunction(xf - x0);
				ys = interpolateQuinticFunction(yf - y0);
				break;
		}

		Vector2f vec0 = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, x0, y0) & 255];
		Vector2f vec1 = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, x1, y0) & 255];

		float lx0x = interpolateLinear(vec0.x, vec1.x, xs);
		float ly0x = interpolateLinear(vec0.y, vec1.y, xs);

		vec0 = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, x0, y1) & 255];
		vec1 = TWO_DIMENSIONAL_CELLULAR[twoDimensionalHash(seed, x1, y1) & 255];

		float lx1x = interpolateLinear(vec0.x, vec1.x, xs);
		float ly1x = interpolateLinear(vec0.y, vec1.y, xs);

		v2.x += interpolateLinear(lx0x, lx1x, ys) * perturbAmp;
		v2.y += interpolateLinear(ly0x, ly1x, ys) * perturbAmp;
	}

	public enum NoiseType {
		VALUE,
		VALUE_FRACTAL,
		PERLIN,
		PERLIN_FRACTAL,
		SIMPLEX,
		SIMPLEX_FRACTAL,
		CELLULAR,
		WHITE,
		CUBIC,
		CUBIC_FRACTAL

	}

	public enum Interpolation {
		LINEAR,
		HERMITE,
		QUINTIC
	}

	public enum FractalType {
		FRACTIONAL_BROWNIAN,
		BILLOW,
		RIGID_MULTIPLE
	}

	public enum CellularDistanceFunction {
		EUCLIDEAN,
		MANHATTAN,
		NATURAL
	}

	public enum CellularReturnType {
		CELL_VALUE,
		NOISE_LOOKUP,
		DISTANCE,
		DISTANCE_TO,
		DISTANCE_TO_ADDITION,
		DISTANCE_TO_SUBTRACTION,
		DISTANCE_TO_MULTIPLICATION,
		DISTANCE_TO_DIVISION
	}
}
