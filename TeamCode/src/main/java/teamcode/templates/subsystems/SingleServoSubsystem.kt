package teamcode.templates.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.KSubsystem

class SingleServoSubsystem : KSubsystem() {
    private val servo = KServo("enter servo name here")
        .startAt(TODO("enter start position here"))

    fun setPos(pos: Double) {
        servo.position = pos
    }
}