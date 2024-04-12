package pokefenn.totemic.api.totem;

import java.util.List;

/**
 * A {@link TotemCarving} which can be used with a Medicine Bag.
 */
public final class PortableTotemCarving extends TotemCarving {
    private final List<MedicineBagEffect> medicineBagEffects;

    /**
     * Constructs a new PortableTotemCarving with one effect.
     * @param effect the effect of the carving, which must be an instance of both TotemEffect and MedicineBagEffect.
     */
    public <T extends TotemEffect & MedicineBagEffect> PortableTotemCarving(T effect) {
        this(List.of(effect));
    }

    /**
     * Constructs a new PortableTotemCarving with multiple effects.
     * @param effects the constituent effects of the carving, which must be instances of both TotemEffect and MedicineBagEffect.
     */
    @SafeVarargs
    public <T extends TotemEffect & MedicineBagEffect> PortableTotemCarving(T... effects) {
        this(List.of(effects));
    }

    /**
     * Constructs a new PortableTotemCarving with multiple effects.
     * @param effects the constituent effects of the carving, which must be instances of both TotemEffect and MedicineBagEffect.
     */
    public <T extends TotemEffect & MedicineBagEffect> PortableTotemCarving(List<T> effects) {
        this(effects, effects);
    }

    /**
     * Constructs a new PortableTotemCarving with separate Totem and Medicine Bag effects.
     * @param totemEffects  a List of the Totem Effects to be applied when this carving is used on a Totem Pole.
     * @param medBagEffects a List of the Medicine Bag Effects to be applied when this carving is used with a Medicine Bag.
     */
    public PortableTotemCarving(List<? extends TotemEffect> totemEffects, List<? extends MedicineBagEffect> medBagEffects) {
        super(totemEffects);
        this.medicineBagEffects = List.copyOf(medBagEffects);
    }

    /**
     * Returns the carving's Medicine Bag effects.
     */
    public List<MedicineBagEffect> getMedicineBagEffects() {
        return medicineBagEffects;
    }
}
