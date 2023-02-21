package teamcode.templates.subsystems

import com.asiankoala.koawalib.control.controller.PIDGains
import com.asiankoala.koawalib.control.motor.FFGains
import com.asiankoala.koawalib.control.profile.MotionConstraints
import com.asiankoala.koawalib.hardware.motor.EncoderFactory
import com.asiankoala.koawalib.hardware.motor.MotorFactory
import com.asiankoala.koawalib.subsystem.KSubsystem

class LinkedMotorSubsystem : KSubsystem() {
    private val motor1 = MotorFactory("enter motor name here")
        .brake
        .createEncoder(
            EncoderFactory(1.0)
            .reverse
            .zero()
        )
        .withMotionProfileControl(
            PIDGains(0.0, 0.0, 0.0),
            FFGains(0.0, 0.0, 0.0, 0.0, 0.0),
            MotionConstraints(0.0, 0.0, 0.0),
            0.0,
            0.0,
        )
        .build()

    private val motor2 = MotorFactory("enter some other motor name here")
        .brake
        .reverse
        .build()

    fun setPos(pos: Double) {
        motor1.setProfileTarget(pos)
    }

    override fun periodic() {
        motor2.power = motor1.power
    }
}