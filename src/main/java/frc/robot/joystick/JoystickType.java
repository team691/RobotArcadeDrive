package frc.robot.joystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public abstract class JoystickType extends Joystick {
    
    public static JoystickType identify(final int id) {
        switch (DriverStation.getJoystickName(id)) {
            case Attack3.NAME: return new Attack3(id, id == 0);
            case Extreme3DPro.NAME: return new Extreme3DPro(id, id == 0);
            default: return new Attack3(id, id == 0);
        }
    }

    public final boolean primary;

    public JoystickType(int id, final boolean primary) {
        super(id);
        this.primary = primary;
    }

    public abstract boolean getIntakeUp();

    public abstract boolean getIntakeDown();

    public abstract double getSpeed();

}
