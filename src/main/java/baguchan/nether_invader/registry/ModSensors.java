package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.sensor.AggresivePiglinSpecificSensor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPE = DeferredRegister.create(Registries.SENSOR_TYPE, NetherInvader.MODID);
    public static final RegistryObject<SensorType<AggresivePiglinSpecificSensor>> ANGER_PIGLIN_SENSOR = SENSOR_TYPE.register("agressive_piglin", () -> new SensorType<>(AggresivePiglinSpecificSensor::new));

}
