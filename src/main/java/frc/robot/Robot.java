// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import java.util.Timer;

//import edu.wpi.first.wpilibj.motorcontrol.CANSparkMax;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick stick;
  private Joystick stick2;
  private XboxController c;
 // private Joystick m_rightStick;

  private final CANSparkMax m_leftMotor1 = new CANSparkMax(3, MotorType.kBrushed);
  private final CANSparkMax m_leftMotor2 = new CANSparkMax(4, MotorType.kBrushed);

  MotorControllerGroup m_left = new MotorControllerGroup(m_leftMotor1, m_leftMotor2);

  private final CANSparkMax m_rightMotor1 = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax m_rightMotor2 = new CANSparkMax(2, MotorType.kBrushed);

  MotorControllerGroup m_right = new MotorControllerGroup(m_rightMotor1, m_rightMotor2);

  Timer m_timer = new Timer();
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
   
    m_right.setInverted(true);
   
    m_myRobot = new DifferentialDrive(m_left, m_right);
    stick = new Joystick(0);
    //stick2 = new Joystick(1);
    //c = new XboxController(0);
  
    //m_rightStick = new Joystick(1);
  }
  @Override
  public void autonomousInit(){
    //Code for Auto period goes under this method
   
    /*
    // Makes the robot go forward, turn around, then back
   //m_myRobot.arcadeDrive(10, 0);
   m_myRobot.arcadeDrive(-stick.getX(), stick.getY());
   try{
      wait(2);
    } catch (InterruptedException e){
    }
    m_myRobot.arcadeDrive(0, 0);
    m_myRobot.arcadeDrive(0, 180);
    try{
      wait(2);
    } catch (InterruptedException e){
    }
    m_myRobot.arcadeDrive(10, 0);
    try{
      wait(2);
    } catch (InterruptedException e){
    }
    m_myRobot.arcadeDrive(0, 0);

    */
    m_timer.reset();
    m_timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    // TODO Auto-generated method stub
    super.autonomousPeriodic();
    if(m_timer.get() < 10.0){
      //m_myRobot.arcadeDrive(0.5, 0.0);
     //goForward();
     m_left.set(0.25);
     m_right.set(-0.25);
     
    }
    //m_left.set(-0.1);
    //m_right.set(0.1);
   
    else{
      m_myRobot.stopMotor();
      
    }
  }
  public void goForwad(){
    m_left.set(0.1);
    m_right.set(-0.1);
  }

  @Override
  public void teleopPeriodic() {
    m_myRobot.arcadeDrive(-stick.getX(), stick.getY());
  
  
     //m_left.set(stick.getY() + stick.getX());
   //m_left.set(stick.getY() + stick.getX());
   //m_right.set(stick.getY() - stick.getX());
    //SmartDashboard.putNumber("X", stick.getX());
    //SmartDashboard.putNumber("Y", stick.getY());
   //m_myRobot.tankDrive(stick.getY(), stick.get(Z));
    //m_myRobot.tankDrive(c.getY(), c.getX());
  }

 // public void auto
}
