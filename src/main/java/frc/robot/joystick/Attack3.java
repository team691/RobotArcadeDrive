package frc.robot.joystick;

public class Attack3 extends JoystickType {

    public static final String NAME = "Logitech Attack 3";

    private static final int TRIGGER = 1;
    private static final int DPAD_DOWN = 2;
    private static final int DPAD_UP = 3;
    private static final int DPAD_LEFT = 4;
    private static final int DPAD_RIGHT = 5;

    public Attack3(final int id, final boolean primary) {
        super(id, primary);
    }

    @Override
    public boolean getIntakeUp() {
        return super.getRawButton(DPAD_UP);
    }

    @Override
    public boolean getIntakeDown() {
        return super.getRawButton(DPAD_DOWN);
    }

    @Override
    public double getSpeed() {
        return (-super.getZ() + 1.0) / 2.0;
    }
}
