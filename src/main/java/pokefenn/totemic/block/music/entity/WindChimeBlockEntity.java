package pokefenn.totemic.block.music.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.music.MusicAPI;
import pokefenn.totemic.init.ModBlockEntities;
import pokefenn.totemic.init.ModContent;
import pokefenn.totemic.util.BlockUtil;

public class WindChimeBlockEntity extends BlockEntity {
    public static final int CONGESTION_RANGE = 8;
    public static final int MAX_NEARBY_CHIMES = 2;

    private int playingTimeLeft = 0;
    private int cooldown = 0; //Only used on the server side
    private boolean isCongested = false;

    private static final int PLAYING_TIME = 8 * 20;

    public WindChimeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.wind_chime.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WindChimeBlockEntity tile) {
        if(tile.isPlaying()) {
            if(tile.playingTimeLeft % 40 == 0)
                tile.playMusic(level, pos, state);

            tile.playingTimeLeft--;
            if(tile.playingTimeLeft <= 0)
                tile.setNotPlaying();
        }
        else {
            if(!tile.isCongested && !level.isClientSide) {
                tile.cooldown--;
                if(tile.cooldown <= 0)
                    tile.setPlaying(PLAYING_TIME);
            }

            if(tile.isCongested && level.isClientSide)
                tile.congestionParticles();
        }
    }

    private void playMusic(Level level, BlockPos pos, BlockState state) {
        var above = level.getBlockState(pos.above());
        int baseAmount = ModContent.wind_chime.get().getBaseOutput();
        int bonus = above.is(BlockTags.LEAVES) ? baseAmount/2 : 0;
        TotemicAPI.get().music().playMusic(level, Vec3.atBottomCenterOf(pos), null, ModContent.wind_chime.get(), MusicAPI.DEFAULT_RANGE, baseAmount + bonus);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(!level.isClientSide)
            isCongested = checkForCongestion();
    }

    public boolean isPlaying() {
        return playingTimeLeft > 0 && !isCongested;
    }

    public void setPlaying(int time) {
        playingTimeLeft = time;
        if(!level.isClientSide)
            level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, time); //Notify the client that the chime is playing
        setChanged();
    }

    public void setNotPlaying() {
        playingTimeLeft = 0;
        if(!level.isClientSide)
            cooldown = getRandomCooldown(level.random);
        setChanged();
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if(!level.isClientSide)
            return true;
        else {
            setPlaying(param);
            return true;
        }
    }

    private int getRandomCooldown(RandomSource rand) {
        return (int) (20.0 * (40.0 + 5.0 * rand.nextGaussian())); //40 ± 5 seconds
    }

    private boolean checkForCongestion() {
        long count = BlockUtil.getBlockEntitiesInRange(ModBlockEntities.wind_chime.get(), level, worldPosition, CONGESTION_RANGE)
                .filter(tile -> tile != this && !tile.isCongested)
                .limit(MAX_NEARBY_CHIMES + 1)
                .count();
        return count > MAX_NEARBY_CHIMES;
    }

    public void tryUncongest() {
        if(isCongested) {
            isCongested = checkForCongestion();
            if(!isCongested)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private void congestionParticles() {
        if(level.getGameTime() % 2 == 0) {
            var rand = level.getRandom();
            var pos = getBlockPos();
            level.addAlwaysVisibleParticle(ParticleTypes.CRIT, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, 0, 0);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(isPlaying())
            tag.putInt("PlayingTime", playingTimeLeft);
        else
            tag.putInt("Cooldown", cooldown);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("PlayingTime"))
            playingTimeLeft = tag.getInt("PlayingTime");
        else {
            playingTimeLeft = 0;
            cooldown = tag.getInt("Cooldown");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.putInt("PlayingTime", playingTimeLeft);
        tag.putBoolean("IsCongested", isCongested);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        playingTimeLeft = tag.getInt("PlayingTime");
        isCongested = tag.getBoolean("IsCongested");
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }
}
