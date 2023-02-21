package teamcode.tuners

import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.control.controller.PIDGains
import com.asiankoala.koawalib.control.motor.FFGains
import com.asiankoala.koawalib.hardware.motor.EncoderFactory
import com.asiankoala.koawalib.hardware.motor.MotorFactory
import com.asiankoala.koawalib.logger.Logger
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import teamcode.tuners.configs.PIDConfig

@TeleOp
class PIDTuner : KOpMode(true) {
    private var atHome = true
    override fun mInit() {
        val motor = MotorFactory("motor name here")
            .float
            .createEncoder(
                EncoderFactory(1.0)
            )
            .withPositionControl(
                PIDGains(PIDConfig.kP, PIDConfig.kI, PIDConfig.kD),
                FFGains(kG = PIDConfig.kG, kCos = PIDConfig.kCos),
                allowedPositionError = PIDConfig.allowedPositionError
            )
            .build()

        driver.rightTrigger.onPress(InstantCmd({
            if(atHome) {
                motor.setPositionTarget(PIDConfig.target)
                atHome = false
            }
        }))

        driver.leftTrigger.onPress(InstantCmd({
            if(!atHome) {
                motor.setPositionTarget(PIDConfig.home)
                atHome = true
            }
        }))
    }

    override fun mInitLoop() {
        Logger.put("Waiting for OpMode to start...")
    }

    override fun mLoop() {
        if(atHome) Logger.put("Press right trigger to go to target")
        else Logger.put("Press left trigger to go to home")
    }
}
