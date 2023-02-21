package teamcode.examples

import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.control.filter.Debouncer
import com.asiankoala.koawalib.logger.Logger
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class GamepadExamples : KOpMode() {
    override fun mInit() {
        driver.leftTrigger.setThreshold(0.5)
        driver.rightTrigger.setDebouncer(0.25)
        driver.leftTrigger.onPress(InstantCmd({}))
        driver.rightTrigger.whilePressed(InstantCmd({}))
        driver.leftStick.setXRateLimiter(0.1)
    }

    override fun mLoop() {
        Logger.put("right vector", driver.rightStick.vector)
    }
}