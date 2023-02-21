package teamcode.tuners

import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.command.commands.LoopCmd
import com.asiankoala.koawalib.command.commands.LoopUntilCmd
import com.asiankoala.koawalib.command.group.SequentialGroup
import com.asiankoala.koawalib.hardware.motor.EncoderFactory
import com.asiankoala.koawalib.hardware.motor.MotorFactory
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import teamcode.tuners.configs.TicksPerUnitConfig
import kotlin.math.absoluteValue

@TeleOp
class TicksPerUnitTuner : KOpMode(true) {
    override fun mInit() {
        val motor = MotorFactory("motor name here")
            .float
            .createEncoder(EncoderFactory(1.0))
            .build()

        var start = 0.0
        var end = 0.0

        + SequentialGroup(
            LoopUntilCmd({ Logger.put("Waiting for OpMode to start...") }, { opModeState == OpModeState.PLAY }),
            LoopUntilCmd({ Logger.put("Press A to start measuring") }, driver.a),
            InstantCmd({ start = motor.pos }),
            LoopUntilCmd({ Logger.put("Press A again to stop measuring")}, driver.a),
            InstantCmd({ end = motor.pos }),
            LoopCmd({ Logger.put("Calculated Ticks Per Unit: ${((end - start) / TicksPerUnitConfig.deltaDistance).absoluteValue}") })
        )
    }
}