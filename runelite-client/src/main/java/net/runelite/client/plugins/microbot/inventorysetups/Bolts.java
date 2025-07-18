package net.runelite.client.plugins.microbot.inventorysetups;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.gameval.ItemID;
import static net.runelite.api.gameval.ItemID.*;

/**
 * @author robbie, created on 19/09/2021 12:34
 */
public enum Bolts
{
	BRONZE(1, BOLT),
	BLURITE(2, XBOWS_CROSSBOW_BOLTS_BLURITE),
	IRON(3, XBOWS_CROSSBOW_BOLTS_IRON),
	STEEL(4, XBOWS_CROSSBOW_BOLTS_STEEL),
	MITHRIL(5, XBOWS_CROSSBOW_BOLTS_MITHRIL),
	ADAMANT(6, XBOWS_CROSSBOW_BOLTS_ADAMANTITE),
	RUNITE(7, XBOWS_CROSSBOW_BOLTS_RUNITE),
	SILVER(8, XBOWS_CROSSBOW_BOLTS_SILVER),

	BRONZE_POISON1(9, POISON_BOLT),
	BLURITE_POISON1(10, XBOWS_CROSSBOW_BOLTS_BLURITE_2),
	IRON_POISON1(11, XBOWS_CROSSBOW_BOLTS_IRON_2),
	STEEL_POISON1(12, XBOWS_CROSSBOW_BOLTS_STEEL_2),
	MITHRIL_POISON1(13, XBOWS_CROSSBOW_BOLTS_MITHRIL_2),
	ADAMANT_POISON1(14, XBOWS_CROSSBOW_BOLTS_ADAMANTITE_2),
	RUNITE_POISON1(15, XBOWS_CROSSBOW_BOLTS_RUNITE_2),
	SILVER_POISON1(16, XBOWS_CROSSBOW_BOLTS_SILVER_2),

	BRONZE_POISON2(17, POISON_BOLT_),
	BLURITE_POISON2(18, XBOWS_CROSSBOW_BOLTS_BLURITE_3),
	IRON_POISON2(19, XBOWS_CROSSBOW_BOLTS_IRON_3),
	STEEL_POISON2(20, XBOWS_CROSSBOW_BOLTS_STEEL_3),
	MITHRIL_POISON2(21, XBOWS_CROSSBOW_BOLTS_MITHRIL_3),
	ADAMANT_POISON2(22, XBOWS_CROSSBOW_BOLTS_ADAMANTITE_3),
	RUNITE_POISON2(23, XBOWS_CROSSBOW_BOLTS_RUNITE_3),
	SILVER_POISON2(24, XBOWS_CROSSBOW_BOLTS_SILVER_3),

	BRONZE_POISON3(25, POISON_BOLT__),
	BLURITE_POISON3(26, XBOWS_CROSSBOW_BOLTS_BLURITE_4),
	IRON_POISON3(27, XBOWS_CROSSBOW_BOLTS_IRON_4),
	STEEL_POISON3(28, XBOWS_CROSSBOW_BOLTS_STEEL_4),
	MITHRIL_POISON3(29, XBOWS_CROSSBOW_BOLTS_MITHRIL_4),
	ADAMANT_POISON3(30, XBOWS_CROSSBOW_BOLTS_ADAMANTITE_4),
	RUNITE_POISON3(31, XBOWS_CROSSBOW_BOLTS_RUNITE_4),
	SILVER_POISON3(32, XBOWS_CROSSBOW_BOLTS_SILVER_4),

	OPAL(33, OPAL_BOLT),
	JADE(34, XBOWS_BOLT_TIPS_JADE),
	PEARL(35, PEARL_BOLT),
	TOPAZ(36, XBOWS_BOLT_TIPS_REDTOPAZ),
	SAPPHIRE(37, XBOWS_BOLT_TIPS_SAPPHIRE),
	EMERALD(38, XBOWS_BOLT_TIPS_EMERALD),
	RUBY(39, XBOWS_BOLT_TIPS_RUBY),
	DIAMOND(40, XBOWS_BOLT_TIPS_DIAMOND),
	DRAGONSTONE(41, XBOWS_BOLT_TIPS_DRAGONSTONE),
	ONYX(42, XBOWS_BOLT_TIPS_ONYX),

	OPAL_E(43, OPAL_BOLT2),
	JADE_E(44, XBOWS_BOLT_TIPS_JADE_2),
	PEARL_E(45, PEARL_BOLT2),
	TOPAZ_E(46, XBOWS_BOLT_TIPS_REDTOPAZ_2),
	SAPPHIRE_E(47, XBOWS_BOLT_TIPS_SAPPHIRE_2),
	EMERALD_E(48, XBOWS_BOLT_TIPS_EMERALD_2),
	RUBY_E(49, XBOWS_BOLT_TIPS_RUBY_2),
	DIAMOND_E(50, XBOWS_BOLT_TIPS_DIAMOND_2),
	DRAGONSTONE_E(51, XBOWS_BOLT_TIPS_DRAGONSTONE_2),
	ONYX_E(52, XBOWS_BOLT_TIPS_ONYX_2),

	MITH_GRAPPLE(53, XBOWS_GRAPPLE_TIP_BOLT_MITHRIL_ROPE),
	BARBED(54, BARBED_BOLT),
	BONE(55, DTTD_BONE_CROSSBOW_BOLT),
	BROAD(56, SLAYER_BROAD_BOLT),
	AMETHYST_BROAD(57, SLAYER_BROAD_BOLT_AMETHYST),

	DRAGON(58, DRAGON_BOLTS),
	DRAGON_POISON1(59, DRAGON_BOLTS_P),
	DRAGON_POISON2(60, DRAGON_BOLTS_P_),
	DRAGON_POISON3(61, DRAGON_BOLTS_P__),
	OPAL_DRAGON(62, DRAGON_BOLTS_ENCHANTED_OPAL),
	JADE_DRAGON(63, DRAGON_BOLTS_ENCHANTED_JADE),
	PEARL_DRAGON(64, DRAGON_BOLTS_ENCHANTED_PEARL),
	TOPAZ_DRAGON(65, DRAGON_BOLTS_ENCHANTED_TOPAZ),
	SAPPHIRE_DRAGON(66, DRAGON_BOLTS_ENCHANTED_SAPPHIRE),
	EMERALD_DRAGON(67, DRAGON_BOLTS_ENCHANTED_EMERALD),
	RUBY_DRAGON(68, DRAGON_BOLTS_ENCHANTED_RUBY),
	DIAMOND_DRAGON(69, DRAGON_BOLTS_ENCHANTED_DIAMOND),
	DRAGONSTONE_DRAGON(70, DRAGON_BOLTS_ENCHANTED_DRAGONSTONE),
	ONYX_DRAGON(71, DRAGON_BOLTS_ENCHANTED_ONYX),

	OPAL_DRAGON_E(72, DRAGON_BOLTS_UNENCHANTED_OPAL),
	JADE_DRAGON_E(73, DRAGON_BOLTS_UNENCHANTED_JADE),
	PEARL_DRAGON_E(74, DRAGON_BOLTS_UNENCHANTED_PEARL),
	TOPAZ_DRAGON_E(75, DRAGON_BOLTS_UNENCHANTED_TOPAZ),
	SAPPHIRE_DRAGON_E(76, DRAGON_BOLTS_UNENCHANTED_SAPPHIRE),
	EMERALD_DRAGON_E(77, DRAGON_BOLTS_UNENCHANTED_EMERALD),
	RUBY_DRAGON_E(78, DRAGON_BOLTS_UNENCHANTED_RUBY),
	DIAMOND_DRAGON_E(79, DRAGON_BOLTS_UNENCHANTED_DIAMOND),
	DRAGONSTONE_DRAGON_E(80, DRAGON_BOLTS_UNENCHANTED_DRAGONSTONE),
	ONYX_DRAGON_E(81, DRAGON_BOLTS_UNENCHANTED_ONYX),

	BOLT_RACK(82, BARROWS_KARIL_AMMO);

	@Getter
	private final int id;
	@Getter
	private final int itemId;

	@Getter
	@Setter
	private BufferedImage image;

	private static final Map<Integer, Bolts> bolts;

	static
	{
		ImmutableMap.Builder<Integer, Bolts> builder = new ImmutableMap.Builder<>();
		for (Bolts bolt : values())
		{
			builder.put(bolt.getId(), bolt);
		}
		bolts = builder.build();
	}

	Bolts(int id, int itemId)
	{
		this.id = id;
		this.itemId = itemId;
	}

	public static Bolts getBolt(int varbit)
	{
		return bolts.get(varbit);
	}

}
