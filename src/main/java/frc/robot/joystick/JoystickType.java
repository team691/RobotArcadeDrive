package frc.robot.joystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public abstract class JoystickType extends Joystick {
    
    public static JoystickType identify(final int id) {
        switch (DriverStation.getJoystickName(id)) {
            case Attack3.NAME: return new Attack3(id);
            case Extreme3DPro.NAME: return new Extreme3DPro(id);
            default: return new Attack3(id);
        }
    }

    public JoystickType(int id) {
        super(id);
    }

    public abstract boolean getIntakeUp();

    public abstract boolean getIntakeDown();

    public abstract double getSpeed();

}
