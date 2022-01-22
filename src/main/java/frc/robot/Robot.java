// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {

  /// Motors
  private final Side left = new Side(new int[] { 2, 3 }, false);
  private final Side right = new Side(new int[] { 1, 4 }, true);
  private final CANSparkMax intake = new CANSparkMax(5, MotorType.kBrushless);
  private final CANSparkMax shooter = new CANSparkMax(6, MotorType.kBrushless);

  private final DifferentialDrive drive = new DifferentialDrive(left, right);

  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  private final Timer timer = new Timer();
  
  private final Joystick stick = new Joystick(0);

  @Override
  public void autonomousPeriodic() {
    super.autonomousPeriodic();

    // goBackward();
    if (timer.get() < 1) {
      turnLeft(0.5);
    } else {
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
    double speed = (-this.stick.getZ() + 1.0) / 4.0;
    this.updateIntake(speed);
    this.updateShooter(speed);
  }

  private void updateIntake(double speed) {
    // double speed = 0.25;
    if (stick.getRawButton(Buttons.DPAD_DOWN)) {
      intake.set(speed);
    } else if (stick.getRawButton(Buttons.DPAD_UP)) {
      intake.set(-speed);
    } else if (stick.getRawButtonReleased(Buttons.DPAD_UP) || stick.getRawButtonReleased(Buttons.DPAD_DOWN)) {
      intake.set(0.0);
    }
  }

  private void updateShooter(double speed) {
    if (stick.getTrigger()) {
      this.shooter.set(speed);
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
