package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.sensor.AggresivePiglinSpecificSensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPE = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, NetherInvader.MODID);
    public static final DeferredHolder<SensorType<?>, SensorType<AggresivePiglinSpecificSensor>> ANGER_PIGLIN_SENSOR = SENSOR_TYPE.register("agressive_piglin", (properties) -> new SensorType<>(AggresivePiglinSpecificSensor::new));

}
