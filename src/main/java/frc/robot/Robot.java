// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {

  private DifferentialDrive drive;
  private Joystick stick;

  private final Side left = new Side(0, false);
  private final Side right = new Side(1, true);
  private final CANSparkMax intake = new CANSparkMax(5, MotorType.kBrushed);
  private final CANSparkMax shooter = new CANSparkMax(6, MotorType.kBrushed);

  private final Timer timer = new Timer();

  @Override
  public void robotInit() {
    drive = new DifferentialDrive(left.group, right.group);
    stick = new Joystick(0);
  }

  @Override
  public void autonomousPeriodic() {
    super.autonomousPeriodic();

    // goBackward();
    if (timer.get() < 1) {
      turnLeft(0.5);
    }else {
      drive.stopMotor();
    }

  }

  public void goForward(double speed) {
    left.set(speed);
    right.set(-speed);
  }

  public void goBackward(double speed) {
    left.set(-speed);
    right.set(speed);
  }

  public void turnLeft(double speed) {
    left.set(speed);
    right.set(speed);
  }

  public void turnRight(double speed) {
    left.set(-speed);
    right.set(-speed);
  }

  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-stick.getX(), stick.getY());
    this.updateIntake();
    this.updateShooter();
  }

  private void updateIntake() {
    if (stick.getRawButtonPressed(Buttons.DPAD_UP)) {
      intake.set(-1.0);
    } else if (stick.getRawButtonReleased(Buttons.DPAD_UP)) {
      intake.set(0.0);
    } else if (stick.getRawButtonPressed(Buttons.DPAD_DOWN)) {
      intake.set(1.0);
    } else if (stick.getRawButtonReleased(Buttons.DPAD_DOWN)) {
      intake.set(0.0);
    }
  }

  private void updateShooter() {
    if (stick.getTriggerPressed()) {
      this.shooter.set(1.0);
    } else if (stick.getTriggerReleased()) {
      this.shooter.set(0.0);
    }
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    drive.arcadeDrive(10, 0);
    try {
      wait(2);
      drive.arcadeDrive(0, 0);
      drive.arcadeDrive(0, 180);
      drive.arcadeDrive(10, 0);
      drive.arcadeDrive(0, 0);
    } catch (InterruptedException e) {
      drive.stopMotor();
    }
  }

}
