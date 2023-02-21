package teamcode.templates.opmodes

import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.commands.InstantCmd
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.logger.LoggerConfig
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class KTele : KOpMode(true) {
    override fun mInit() {
        Logger.config = LoggerConfig.DASHBOARD_CONFIG
        driver.rightTrigger.onPress(InstantCmd({ Logger.logInfo("lol idk") }))
    }
}