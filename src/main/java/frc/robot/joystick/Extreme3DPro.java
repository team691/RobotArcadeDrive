package frc.robot.joystick;


public class Extreme3DPro extends JoystickType {

    public static final String NAME = "Extreme 3D pro";

    private static final int TRIGGER = 1;
    private static final int STICK_SIDE_THUMB = 2;
    private static final int STICK_LEFT_LOWER = 3;
    private static final int STICK_RIGHT_LOWER = 4;
    private static final int STICK_LEFT_UPPER = 5;
    private static final int STICK_RIGHT_UPPER = 6;

    public Extreme3DPro(final int id, final boolean primary) {
        super(id, primary);
    }

    @Override
    public boolean getIntakeUp() {
        // if (primary) {
            // return false;
        // } else {
            return this.getRawButton(STICK_RIGHT_UPPER);
        // }
    }

    @Override
    public boolean getIntakeDown() {
        // if (primary) {
            // return false;
        // } else {
            return this.getRawButton(STICK_RIGHT_LOWER);
        // }
    }

    @Override
    public double getSpeed() {
        return super.getRawAxis(3);//(super.getRawAxis(3) + 1.0) / 2.0;
    }

}
