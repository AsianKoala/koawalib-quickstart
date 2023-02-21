package teamcode.tuners.configs

import com.acmerobotics.dashboard.config.Config

@Config
object PIDConfig {
    @JvmField var target = 0.0
    @JvmField var home = 0.0
    @JvmField var kP = 0.0
    @JvmField var kI = 0.0
    @JvmField var kD = 0.0
    @JvmField var kG = 0.0
    @JvmField var kCos: Double? = null
    @JvmField var allowedPositionError = 0.0
}