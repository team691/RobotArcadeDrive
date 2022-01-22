package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Side extends MotorControllerGroup {

    private static CANSparkMax[] motors(int[] ids) {
        CANSparkMax[] motors = new CANSparkMax[ids.length];
        for (int i = 0; i < motors.length; i++) {
            motors[i] = new CANSparkMax(ids[i], MotorType.kBrushed);
        }
        return motors;
    }

    public Side(int[] ids, boolean inverted) {
        super(motors(ids));
        this.setInverted(inverted);
    }

}
