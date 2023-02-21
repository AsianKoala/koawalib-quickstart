package teamcode.rr.drive

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.*
import com.asiankoala.koawalib.hardware.motor.MotorFactory
import com.qualcomm.robotcore.hardware.HardwareMap
import teamcode.rr.drive.DriveConstants.HEADING_PID
import teamcode.rr.drive.DriveConstants.TRANSLATIONAL_PID
import teamcode.rr.trajectorysequence.TrajectorySequence
import teamcode.rr.trajectorysequence.TrajectorySequenceBuilder
import teamcode.rr.trajectorysequence.TrajectorySequenceRunner
import java.util.*


@Config
class SampleMecanumDrive(hardwareMap: HardwareMap) : MecanumDrive(
    DriveConstants.kV,
    DriveConstants.kA,
    DriveConstants.kStatic,
    DriveConstants.TRACK_WIDTH,
    DriveConstants.TRACK_WIDTH,
    LATERAL_MULTIPLIER
) {
    private val trajectorySequenceRunner: TrajectorySequenceRunner
    private val follower: TrajectoryFollower
    private val fl = MotorFactory("fl").reverse.brake.voltageCorrected.build()
    private val bl = MotorFactory("bl").reverse.brake.voltageCorrected.build()
    val br = MotorFactory("br").brake.voltageCorrected.build()
    private val fr = MotorFactory("fr").brake.voltageCorrected.build()

    fun trajectorySequenceBuilder(startPose: Pose2d?): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, ACCEL_CONSTRAINT,
            DriveConstants.MAX_ANG_VEL, DriveConstants.MAX_ANG_ACCEL
        )
    }

    fun turnAsync(angle: Double) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(poseEstimate)
                .turn(angle)
                .build()
        )
    }

    fun turn(angle: Double) {
        turnAsync(angle)
        waitForIdle()
    }

    fun followTrajectorySequenceAsync(trajectorySequence: TrajectorySequence?) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence)
    }

    fun trajectoryBuilder(startPose: Pose2d): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, baseVelConstraint = VEL_CONSTRAINT, baseAccelConstraint = ACCEL_CONSTRAINT)
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun followTrajectoryAsync(trajectory: Trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(trajectory.start())
                .addTrajectory(trajectory)
                .build()
        )
    }

    fun followTrajectory(trajectory: Trajectory) {
        followTrajectoryAsync(trajectory)
        waitForIdle()
    }

    fun followTrajectorySequence(trajectorySequence: TrajectorySequence?) {
        followTrajectorySequenceAsync(trajectorySequence)
        waitForIdle()
    }

    fun waitForIdle() {
        while (!Thread.currentThread().isInterrupted && isBusy()) update()
    }

    fun update() {
        updatePoseEstimate()
        val signal = trajectorySequenceRunner.update(poseEstimate, poseVelocity)
        signal?.let { setDriveSignal(it) }
    }

    fun isBusy() = trajectorySequenceRunner.isBusy

    override fun getWheelPositions(): List<Double> {
        return listOf()
    }

    override fun getWheelVelocities(): List<Double> {
        return listOf()
    }

    override fun setMotorPowers(
        frontLeft: Double,
        rearLeft: Double,
        rearRight: Double,
        frontRight: Double
    ) {
        fl.power = frontLeft
        bl.power = rearLeft
        br.power = rearRight
        fr.power = frontRight
    }

    override val rawExternalHeading: Double
        get() = 0.0

    fun setWeightedDrivePower(drivePower: Pose2d) {
        var vel = drivePower
        if ((Math.abs(drivePower.x) + Math.abs(drivePower.y)
                    + Math.abs(drivePower.heading)) > 1
        ) {
            // re-normalize the powers according to the weights
            val denom: Double =
                VX_WEIGHT * Math.abs(drivePower.x) + VY_WEIGHT * Math.abs(drivePower.y) + OMEGA_WEIGHT * Math.abs(
                    drivePower.heading
                )
            vel = Pose2d(
                VX_WEIGHT * drivePower.x,
                VY_WEIGHT * drivePower.y,
                OMEGA_WEIGHT * drivePower.heading
            ).div(denom)
        }
        setDrivePower(vel)
    }

    init {
        follower = HolonomicPIDVAFollower(
            TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
            Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.2
        )
        localizer = StandardTrackingWheelLocalizer(hardwareMap)
        trajectorySequenceRunner = TrajectorySequenceRunner(follower, HEADING_PID)
    }

    companion object {
        var LATERAL_MULTIPLIER = 1.12
        private val VEL_CONSTRAINT = getVelocityConstraint(
            DriveConstants.MAX_VEL,
            DriveConstants.MAX_ANG_VEL,
            DriveConstants.TRACK_WIDTH
        )
        private val ACCEL_CONSTRAINT = getAccelerationConstraint(DriveConstants.MAX_ACCEL)
        fun getVelocityConstraint(
            maxVel: Double,
            maxAngularVel: Double,
            trackWidth: Double = DriveConstants.TRACK_WIDTH
        ): TrajectoryVelocityConstraint {
            return MinVelocityConstraint(
                Arrays.asList(
                    AngularVelocityConstraint(maxAngularVel),
                    MecanumVelocityConstraint(maxVel, trackWidth)
                )
            )
        }

        fun getAccelerationConstraint(maxAccel: Double): TrajectoryAccelerationConstraint {
            return ProfileAccelerationConstraint(maxAccel)
        }
        var VX_WEIGHT = 1.0
        var VY_WEIGHT = 1.0
        var OMEGA_WEIGHT = 1.0
    }
}
