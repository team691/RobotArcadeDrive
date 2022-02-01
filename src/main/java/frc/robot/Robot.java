// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import java.util.Timer;

//import edu.wpi.first.wpilibj.motorcontrol.CANSparkMax;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;
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

  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  
double heading;

  private final CANSparkMax m_leftMotor1 = new CANSparkMax(1, MotorType.kBrushless);
  private final CANSparkMax m_leftMotor2 = new CANSparkMax(2, MotorType.kBrushless);

  MotorControllerGroup m_left = new MotorControllerGroup(m_leftMotor1, m_leftMotor2);

  private final CANSparkMax m_rightMotor1 = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax m_rightMotor2 = new CANSparkMax(4, MotorType.kBrushless);

  MotorControllerGroup m_right = new MotorControllerGroup(m_rightMotor1, m_rightMotor2);


  private final CANSparkMax m_intake = new CANSparkMax(5, MotorType.kBrushless);
  //private final CANSparkMax m_shoot = new CANSparkMax(6, MotorType.kBrushless);

  private final TalonFX m_shoot= new TalonFX(7);
  
  private final AnalogPotentiometer sensUltrasonic  = new AnalogPotentiometer(0);
  //private final SparkMaxRelativeEncoder = new SparkMaxRelativeEncoder();
  //private final TalonFXControlMode mc = new TalonFXControlMode(0);
  
  private final RelativeEncoder encoder = m_intake.getEncoder();
  private final RelativeEncoder encodeL1 = m_leftMotor1.getEncoder();
  private final RelativeEncoder encodeL2 = m_leftMotor2.getEncoder();
  private final RelativeEncoder encodeR1 = m_rightMotor1.getEncoder();
  private final RelativeEncoder encodeR2 = m_rightMotor2.getEncoder();
  double p = encoder.getPosition();
  
  Timer m_timer = new Timer();
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
   encodeL1.setPosition(0);
   encodeL2.setPosition(0);
   encodeR1.setPosition(0);
   encodeR2.setPosition(0);
   m_right.setInverted(true);
   

   // double kP = 1;

    m_myRobot = new DifferentialDrive(m_left, m_right);
    stick = new Joystick(1);
    stick2 = new Joystick(2);

    
    //c = new XboxController(0);
  
    //m_rightStick = new Joystick(1);
  }
  @Override
  public void autonomousInit(){
    //Code for Auto period goes under this method
    //heading = gyro.getAngle();
    m_timer.reset();
    m_timer.start();

    //gyro.reset();
    //heading = gyro.getAngle();
  }

  @Override
  public void autonomousPeriodic() {
    // TODO Auto-generated method stub
    super.autonomousPeriodic();

  encodeL1.setPositionConversionFactor(1.4);
  encodeL2.setPositionConversionFactor(1.4);
  encodeR1.setPositionConversionFactor(1.4);
  encodeR2.setPositionConversionFactor(1.4);


   SmartDashboard.putNumber("EncoderL1 inches", encodeL1.getPosition());
   SmartDashboard.putNumber("EncoderL2 inches", encodeL2.getPosition());
   SmartDashboard.putNumber("EncoderR1 inches", encodeR1.getPosition());
   SmartDashboard.putNumber("EncoderR2 inches", encodeR2.getPosition());
   
   /*while(encodeL1.getPosition() <= 120){
      goForward();
   }*/
   
   if(m_timer.get() <=20){
   while(encodeL1.getPosition() <= 60){
    goForward();
  }
}
    if(m_timer.get() > 20){
    while(encodeL1.getPosition() >= 0){
      goBackward();
    }
  }
    // SmartDashboard.putNumber("Angle", gyro.getAngle());
    // goBackward();
    
    
    /*if(m_timer.get() < 2){
        goForwad();
    }
    */
    //turnLeft(45);
   // turnLeft(45);
    
    /*else if(m_timer.get() < 11 && m_timer.get() >= 10){
      turnRight();
     }

    else if(m_timer.get() < 21.0 && m_timer.get() >= 11){
      
     goForwad();
  
     
    }
    else if(m_timer.get() < 21.5 && m_timer.get() >= 21){
      turnLeft();
     }
    */
   
  /* else{
      m_myRobot.stopMotor();
  }*/

}
  public void goForward(){
    m_left.set(0.25);
    m_right.set(0.25);
  }

  public void goBackward(){
    m_left.set(-0.25);
    m_right.set(-0.25);
  }
  public void turnLeft(){
    m_left.set(-0.5);
    m_right.set(0.5);
  
}
  public void turnRight(){ 
    m_left.set(0.5);
    m_right.set(-0.5);
  }  
  @Override
  public void teleopPeriodic() {
    m_myRobot.arcadeDrive(-stick.getY(), stick2.getZ());
    //m_myRobot.tankDrive(-stick2.getY(), stick.getY());
    //m_intake.set(stick.getZ());
  // m_intake.set(0.3);
   //encoder.setPositionConversionFactor(0.165);

   encodeL1.setPositionConversionFactor(Math.PI/2);
   encodeL2.setPositionConversionFactor(Math.PI/2);
   encodeR1.setPositionConversionFactor(Math.PI/2);
   encodeR2.setPositionConversionFactor(Math.PI/2);


   SmartDashboard.putNumber("EncoderL1 inches", encodeL1.getPosition());
   SmartDashboard.putNumber("EncoderL2 inches", encodeL2.getPosition());
   SmartDashboard.putNumber("EncoderR1 inches", encodeR1.getPosition());
   SmartDashboard.putNumber("EncoderR2 inches", encodeR2.getPosition());

   SmartDashboard.putNumber("Get", sensUltrasonic.get());
    /*if(sensUltrasonic.getRangeInches() <= 10){
      m_intake.set(0.5);
    }*/

      /*if(sensUltrasonic.get() < 0.015){
        m_intake.set(0.5);
      }*/
      if(stick.getRawButtonPressed(2) == true){
        m_intake.set(-1);
     if(stick.getTriggerPressed() == true){
      m_intake.set(-1);
    }
    else if(stick.getRawButtonReleased(2) == true){
      m_intake.set(0);
    }
  if(stick.getRawButtonPressed(3) == true){
    m_intake.set(1);
  } else if(stick.getRawButtonReleased(3) == true){
    m_intake.set(0.0);
  }
  if(stick.getTriggerPressed() == true){
    m_shoot.set(ControlMode.PercentOutput, 0.5);
  } else if(stick.getTriggerReleased() == true){
    m_shoot.set(ControlMode.PercentOutput, 0.0);
  }
    //neoTest.set(.5);
    
     //m_left.set(stick.getY());
 // m_left.set(stick.getY() + stick.getX());
  // m_right.set(stick.getY() - stick.getX());
    //SmartDashboard.putNumber("X", stick.getX());
    
  }

 // public void auto
}
}