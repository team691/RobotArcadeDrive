// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.joystick.JoystickType;

public class Robot extends TimedRobot {

  /// Motors
  private final Side left = new Side(new int[] { 2, 3 }, false);
  private final Side right = new Side(new int[] { 1, 4 }, true);
  private final CANSparkMax intake = new CANSparkMax(5, MotorType.kBrushed);
  private final CANSparkMax shooter = new CANSparkMax(6, MotorType.kBrushless);

  private final DifferentialDrive drive = new DifferentialDrive(left, right);

  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  private final Timer timer = new Timer();
  
  private final JoystickType stick0 = JoystickType.identify(0);
  private final JoystickType stick1 = stick0;//JoystickType.identify(1);

  private double kP = 1;
  private double heading;



  @Override
  public void robotInit() {
    super.robotInit();
    ShuffleboardTab tab = Shuffleboard.getTab("Main");
    tab.add(gyro);
    // SmartDashboard.putString("Joystick 0", stick0.getName());
    // SmartDashboard.putString("Joystick 1", stick1.getName());
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    gyro.reset();
    timer.start();
    heading = gyro.getAngle();
  }

  @Override
  public void autonomousPeriodic() {
    super.autonomousPeriodic();

    double error = 90 - gyro.getAngle();

    drive.tankDrive(kP * error, kP * error);

    // goBackward();
    // if (timer.get() < 1) {
    //   turnLeft(0.5);
    // } else {
    //   drive.stopMotor();
    // }

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
    drive.tankDrive(-stick0.getX(), stick0.getY());
    double speed = this.stick1.getSpeed();
    this.updateIntake(speed);
    this.updateShooter(speed);
  }

  private void updateIntake(double speed) {
    // double speed = 0.25;
    if (stick1.getIntakeDown()) {
      intake.set(speed);
    } else if (stick1.getIntakeUp()) {
      intake.set(-speed);
    } else {
      intake.set(0.0);
    }
  }

  private void updateShooter(double speed) {
    if (stick1.getTrigger()) {
      this.shooter.set(speed);
    } else if (stick1.getTriggerReleased()) {
      this.shooter.set(0.0);
    }
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    // drive.tankDrive(10, 0);
    // try {
    //   wait(2);
    //   drive.tankDrive(0, 0);
    //   drive.tankDrive(0, 180);
    //   drive.tankDrive(10, 0);
    //   drive.tankDrive(0, 0);
    // } catch (InterruptedException e) {
    //   drive.stopMotor();
    // }
  }

}
