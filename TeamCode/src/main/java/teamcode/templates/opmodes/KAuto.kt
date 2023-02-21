package teamcode.templates.opmodes

import com.asiankoala.koawalib.command.KOpMode
import com.asiankoala.koawalib.command.commands.WaitUntilCmd
import com.asiankoala.koawalib.command.group.SequentialGroup
import com.asiankoala.koawalib.logger.Logger
import com.asiankoala.koawalib.logger.LoggerConfig
import com.asiankoala.koawalib.util.OpModeState

class KAuto : KOpMode(true) {
    override fun mInit() {
        Logger.config = LoggerConfig.DASHBOARD_CONFIG
        + SequentialGroup(
            WaitUntilCmd { opModeState == OpModeState.PLAY },
            // do whatever here
        )
    }
}