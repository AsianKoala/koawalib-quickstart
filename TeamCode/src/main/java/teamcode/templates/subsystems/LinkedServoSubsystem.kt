package teamcode.templates.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.KSubsystem

class LinkedServoSubsystem : KSubsystem() {
    private val servo1 = KServo("enter servo name here")
        .startAt(TODO("enter start pos here"))

    private val servo2 = KServo("enter servo name here")
        .reverse()
        .startAt(TODO("enter start pos here"))

    fun setPos(pos: Double) {
        servo1.position = pos
        servo2.position = pos
    }
}