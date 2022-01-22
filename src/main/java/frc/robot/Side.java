package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Side {

    public final CANSparkMax[] motors;
    public final MotorControllerGroup group;

    public Side(int[] ids, boolean inverted) {
        this.motors = new CANSparkMax[ids.length];
        for (int i = 0; i < this.motors.length; i++) {
            this.motors[i] = new CANSparkMax(ids[i], MotorType.kBrushed);
        }
        this.group = new MotorControllerGroup(this.motors);
        this.group.setInverted(inverted);
    }

    public void set(double speed) {
        this.group.set(speed);
    }

}
