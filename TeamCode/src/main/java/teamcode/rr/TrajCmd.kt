package teamcode.rr

import teamcode.rr.drive.SampleMecanumDrive
import com.asiankoala.koawalib.command.commands.Cmd
import teamcode.rr.trajectorysequence.TrajectorySequence

class TrajCmd(
    private val drive: SampleMecanumDrive,
    private val traj: TrajectorySequence
) : Cmd() {
    override fun initialize() {
        drive.followTrajectorySequenceAsync(traj)
    }

    override fun execute() {
        drive.update()
    }

    override val isFinished: Boolean
        get() = !drive.isBusy()
}