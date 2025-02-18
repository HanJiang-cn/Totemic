package pokefenn.totemic.api.totem;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * A TotemEffect which applies a {@link MobEffect} to all Players near the Totem Pole.
 */
public class PotionTotemEffect extends PlayerTotemEffect implements MedicineBagEffect {
    /**
     * The mob effect to apply.
     */
    protected final MobEffect mobEffect;

    /**
     * If {@code true}, the effect's amplifier will be scaled based on repetition and music.
     * Otherwise, the amplifier will be 0.
     */
    protected final boolean scaleAmplifier;

    /**
     * Constructs a new PotionTotemEffect with default interval and scaling amplifier.
     * @param mobEffect The mob effect to apply.
     */
    public PotionTotemEffect(MobEffect mobEffect) {
        this(mobEffect, true);
    }

    /**
     * Constructs a new PotionTotemEffect with default interval.
     * @param mobEffect      The mob effect to apply.
     * @param scaleAmplifier if {@code true}, the effect's amplifier will be scaled based on repetition and music.
     *                       Otherwise, the amplifier will be 0.
     */
    public PotionTotemEffect(MobEffect mobEffect, boolean scaleAmplifier) {
        this(mobEffect, scaleAmplifier, DEFAULT_INTERVAL);
    }

    /**
     * Constructs a new PotionTotemEffect.
     * @param mobEffect      The mob effect to apply.
     * @param scaleAmplifier if {@code true}, the effect's amplifier will be scaled based on repetition and music.
     *                       Otherwise, the amplifier will be 0.
     * @param interval       the time in ticks until the mob effect is renewed.
     */
    public PotionTotemEffect(MobEffect mobEffect, boolean scaleAmplifier, int interval) {
        super(interval);
        this.mobEffect = Objects.requireNonNull(mobEffect);
        this.scaleAmplifier = scaleAmplifier;
    }

    /**
     * @deprecated Use the above version without the Supplier. Make sure that the MobEffect exists at the time of construction
     * (which might require using DeferredRegister or similar).
     */
    @Deprecated
    public PotionTotemEffect(Supplier<? extends MobEffect> mobEffect) {
        this(mobEffect.get());
    }

    /**
     * @deprecated Use the above version without the Supplier. Make sure that the MobEffect exists at the time of construction
     * (which might require using DeferredRegister or similar).
     */
    @Deprecated
    public PotionTotemEffect(Supplier<? extends MobEffect> mobEffect, boolean scaleAmplifier) {
        this(mobEffect.get(), scaleAmplifier);
    }

    /**
     * @deprecated Use the above version without the Supplier. Make sure that the MobEffect exists at the time of construction
     * (which might require using DeferredRegister or similar).
     */
    @Deprecated
    public PotionTotemEffect(Supplier<? extends MobEffect> mobEffect, boolean scaleAmplifier, int interval) {
        this(mobEffect.get(), scaleAmplifier, interval);
    }

    /**
     * Returns the amplifier that should be used for this effect.<p>
     * In case {@link #scaleAmplifier} is {@code true}, this method returns a value between 0 and 3, depending on the repetition and the amount of music in the Totem Base.
     * Otherwise, the value is 0.
     */
    protected int getAmplifier(LivingEntity entity, int repetition, TotemEffectContext context) {
        final int musicThreshold = TotemEffectAPI.MAX_TOTEM_EFFECT_MUSIC * 3 / 4;
        if(scaleAmplifier)
            return (repetition - 1) / 2 + (context.getTotemEffectMusic() >= musicThreshold ? 1 : 0);
        else
            return 0;
    }

    /**
     * Returns the amplifier that should be used for this effect, when it is used with a Medicine Bag.<p>
     * In case {@link #scaleAmplifier} is {@code true}, this method returns a value between 0 and 2, depending on the Efficiency enchantment level of the Medicine Bag.
     * Otherwise, the value is 0.
     */
    protected int getAmplifierForMedicineBag(Player player, ItemStack medicineBag, int charge) {
        if(scaleAmplifier)
            return medicineBag.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) / 2;
        else
            return 0;
    }

    /**
     * Returns how many ticks the mob effect should linger after leaving the range or closing the Medicine Bag.<p>
     * The default value is 20 ticks.
     */
    protected int getLingeringTime() {
        return 20;
    }

    /**
     * Returns the MobEffectInstance that should be applied to the given entity.
     */
    protected MobEffectInstance getEffectInstance(LivingEntity entity, int repetition, TotemEffectContext context) {
        return new MobEffectInstance(mobEffect, getInterval() + getLingeringTime(), getAmplifier(entity, repetition, context), true, false);
    }

    /**
     * Returns the MobEffectInstance that should be applied to the given player from a Medicine Bag.
     */
    protected MobEffectInstance getEffectInstanceForMedicineBag(Player player, ItemStack medicineBag, int charge) {
        return new MobEffectInstance(mobEffect, getInterval() + getLingeringTime(), getAmplifierForMedicineBag(player, medicineBag, charge), true, false);
    }

    @Override
    public void applyTo(Player player, int repetition, TotemEffectContext context) {
        player.addEffect(getEffectInstance(player, repetition, context));
    }

    @SuppressWarnings("resource")
    @Override
    public void medicineBagEffect(Player player, ItemStack medicineBag, int charge) {
        if(!player.level().isClientSide)
            player.addEffect(getEffectInstanceForMedicineBag(player, medicineBag, charge));
    }
}
