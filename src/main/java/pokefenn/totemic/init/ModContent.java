package pokefenn.totemic.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.music.MusicInstrument;
import pokefenn.totemic.api.registry.TotemicRegisterEvent;
import pokefenn.totemic.api.totem.PotionTotemEffect;
import pokefenn.totemic.api.totem.TotemEffect;
import pokefenn.totemic.ceremony.BuffaloDanceCeremony;
import pokefenn.totemic.ceremony.CleansingCeremony;
import pokefenn.totemic.ceremony.DepthsCeremony;
import pokefenn.totemic.ceremony.EagleDanceCeremony;
import pokefenn.totemic.ceremony.FertilityCeremony;
import pokefenn.totemic.ceremony.FluteInfusionCeremony;
import pokefenn.totemic.ceremony.WarDanceCeremony;
import pokefenn.totemic.ceremony.WeatherCeremony;
import pokefenn.totemic.ceremony.ZaphkielWaltzCeremony;
import pokefenn.totemic.totem.EmptyTotemEffect;
import pokefenn.totemic.totem.OcelotTotemEffect;

public final class ModContent {
    public static final MusicInstrument flute = new MusicInstrument(resloc("flute"), 180, 3000);
    public static final MusicInstrument drum = new MusicInstrument(resloc("drum"), 240, 3300);
    public static final MusicInstrument wind_chime = new MusicInstrument(resloc("wind_chime"), 120, 1500);
    public static final MusicInstrument jingle_dress = new MusicInstrument(resloc("jingle_dress"), 180, 1500);
    public static final MusicInstrument rattle = new MusicInstrument(resloc("rattle"), 300, 3300);
    public static final MusicInstrument eagle_bone_whistle = new MusicInstrument(resloc("eagle_bone_whistle"), 360, 3600);
//    public static final MusicInstrument nether_pipe = new MusicInstrument(resloc("nether_pipe"), 240, 3900);

    public static final TotemEffect none = new EmptyTotemEffect(resloc("none"));
    public static final TotemEffect bat = new PotionTotemEffect(resloc("bat"), () -> MobEffects.SLOW_FALLING);
    public static final TotemEffect blaze = new PotionTotemEffect(resloc("blaze"), () -> MobEffects.FIRE_RESISTANCE);
    public static final TotemEffect buffalo = new PotionTotemEffect(resloc("buffalo"), () -> MobEffects.DIG_SPEED);
    public static final TotemEffect cow = new PotionTotemEffect(resloc("cow"), () -> MobEffects.DAMAGE_RESISTANCE) {
        @Override
        protected void applyTo(boolean isMedicineBag, Player player, int time, int amplifier) {
            super.applyTo(isMedicineBag, player, time, amplifier);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time, 0, true, false));
        }
    };
    public static final TotemEffect enderman = new PotionTotemEffect(resloc("enderman"), () -> MobEffects.NIGHT_VISION) {
        @Override
        protected int getLingeringTime() {
            return 210;
        }
    };
    public static final TotemEffect horse = new PotionTotemEffect(resloc("horse"), () -> MobEffects.MOVEMENT_SPEED);
    public static final TotemEffect ocelot = new OcelotTotemEffect(resloc("ocelot"));
    public static final TotemEffect pig = new PotionTotemEffect(resloc("pig"), () -> MobEffects.LUCK);
    public static final TotemEffect rabbit = new PotionTotemEffect(resloc("rabbit"), () -> MobEffects.JUMP);
    public static final TotemEffect spider = new PotionTotemEffect(resloc("spider"), ModMobEffects.spider);
    public static final TotemEffect squid = new PotionTotemEffect(resloc("squid"), () -> MobEffects.WATER_BREATHING);
    public static final TotemEffect wolf = new PotionTotemEffect(resloc("wolf"), () -> MobEffects.DAMAGE_BOOST);

    //Music amount landmarks:
    //6300: Flute + Drum
    //7800: Flute + Drum + full Wind Chime
    //9300: Flute + Drum + full Wind Chime + Jingle Dress
    //9600: Flute + Drum + Rattle
    //11100: Flute + Drum + Rattle + full Wind Chime
    //12600: Flute + Drum + Rattle + full Wind Chime + Jingle Dress
    //13200: Flute + Drum + Rattle + Eagle-Bone Whistle
    //14700: Flute + Drum + Rattle + Eagle-Bone Whistle + Jingle Dress
    //16200: Flute + Drum + Rattle + Eagle-Bone Whistle + Jingle Dress + full Wind Chime
    public static final Ceremony war_dance = new Ceremony(resloc("war_dance"), 4500, 20 * 20, () -> WarDanceCeremony.INSTANCE, drum, drum);
    public static final Ceremony depths = new Ceremony(resloc("depths"), 4500, 20 * 20, () -> DepthsCeremony.INSTANCE, flute, flute);
    public static final Ceremony fertility = new Ceremony(resloc("fertility"), 5280, 23 * 20, () -> FertilityCeremony.INSTANCE, flute, drum);
    public static final Ceremony zaphkiel_waltz = new Ceremony(resloc("zaphkiel_waltz"), 6720, 20 * 20, () -> ZaphkielWaltzCeremony.INSTANCE, wind_chime, flute);
    public static final Ceremony buffalo_dance = new Ceremony(resloc("buffalo_dance"), 7380, 24 * 20, () -> BuffaloDanceCeremony.INSTANCE, drum, wind_chime);
    public static final Ceremony rain = new Ceremony(resloc("rain"), 10980, 26 * 20, () -> WeatherCeremony.RAIN, drum, rattle);
    public static final Ceremony drought = new Ceremony(resloc("drought"), 10980, 26 * 20, () -> WeatherCeremony.DROUGHT, rattle, drum);
    public static final Ceremony flute_infusion = new Ceremony(resloc("flute_infusion"), 11340, 28 * 20, () -> FluteInfusionCeremony.INSTANCE, flute, rattle);
    public static final Ceremony eagle_dance = new Ceremony(resloc("eagle_dance"), 11580, 25 * 20, () -> EagleDanceCeremony.INSTANCE, rattle, wind_chime);
    public static final Ceremony cleansing = new Ceremony(resloc("cleansing"), 14700, 30 * 20, () -> CleansingCeremony.INSTANCE, eagle_bone_whistle, flute);

    @SubscribeEvent
    public static void instruments(TotemicRegisterEvent<MusicInstrument> event) {
        event.registerAll(
                flute.setItem(ModItems.flute.get()).setSound(ModSounds.flute),
                drum.setItem(ModBlocks.drum.get()).setSound(ModSounds.drum),
                wind_chime.setItem(ModBlocks.wind_chime.get()).setSound(ModSounds.wind_chime),
                jingle_dress.setItem(ModItems.jingle_dress.get()),
                rattle.setItem(ModItems.rattle.get()).setSound(ModSounds.rattle),
                eagle_bone_whistle.setItem(ModItems.eagle_bone_whistle.get()).setSound(ModSounds.eagle_bone_whistle));
    }

    @SubscribeEvent
    public static void totemEffects(TotemicRegisterEvent<TotemEffect> event) {
        event.registerAll(none, bat, blaze, buffalo, cow, enderman, horse, ocelot, pig, rabbit, spider, squid, wolf);
    }

    @SubscribeEvent
    public static void ceremonies(TotemicRegisterEvent<Ceremony> event) {
        event.registerAll(war_dance, depths, fertility, zaphkiel_waltz, buffalo_dance, rain, drought, flute_infusion, eagle_dance, cleansing);
    }

    private static ResourceLocation resloc(String path) {
        return new ResourceLocation(TotemicAPI.MOD_ID, path);
    }
}
