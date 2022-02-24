// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import edu.wpi.first.wpilibj.PlAY;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;



import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  
  //All inputs
  private Joystick stick;
  private Joystick stick2;
  private XboxController c;
 // private Joystick m_rightStick;

  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  
double heading;

  //Motors
  private final CANSparkMax m_leftMotor1 = new CANSparkMax(1, MotorType.kBrushless);
  private final CANSparkMax m_leftMotor2 = new CANSparkMax(2, MotorType.kBrushless);
  MotorControllerGroup m_left = new MotorControllerGroup(m_leftMotor1, m_leftMotor2);

  private final CANSparkMax m_rightMotor1 = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax m_rightMotor2 = new CANSparkMax(4, MotorType.kBrushless);
  MotorControllerGroup m_right = new MotorControllerGroup(m_rightMotor1, m_rightMotor2);

  private final CANSparkMax m_intake = new CANSparkMax(5, MotorType.kBrushed);

  private final WPI_TalonFX shoot1= new WPI_TalonFX(6);
  private final WPI_TalonFX shoot2= new WPI_TalonFX(7);
  private final CANSparkMax kicker = new CANSparkMax(8, MotorType.kBrushed);
  MotorControllerGroup m_shoot = new MotorControllerGroup(shoot1, shoot2, kicker);
  //private final CANSparkMax m_shoot = new CANSparkMax(6, MotorType.kBrushless);

  
  private final CANSparkMax uptake= new CANSparkMax(9, MotorType.kBrushed);
  //private final CANSparkMax uptake3 = new CANSparkMax(, MotorType.kBrushless);
  //MotorControllerGroup uptake = new MotorControllerGroup(uptake1, uptake2); 

  //Sensor for uptake
  private final AnalogInput sensUltrasonic  = new AnalogInput(0);
  
  
  //Motor Encoders
  private final RelativeEncoder encodeL1 = m_leftMotor1.getEncoder();
  private final RelativeEncoder encodeL2 = m_leftMotor2.getEncoder();
  private final RelativeEncoder encodeR1 = m_rightMotor1.getEncoder();
  private final RelativeEncoder encodeR2 = m_rightMotor2.getEncoder();

  //LED controls
  private final CANdle candle = new CANdle (0);
  private final RainbowAnimation r = new RainbowAnimation(1,1,140);
  private final FireAnimation f = new FireAnimation(1,1,8,.2,.4);
  private final RgbFadeAnimation fa = new RgbFadeAnimation (1,.7,8);
  private final StrobeAnimation s = new StrobeAnimation (100,200,8, 255, 1, 8);
  private final ColorFlowAnimation cf = new ColorFlowAnimation(100,200,8, 255, 1, 8, Direction.Backward);

  //Pnuematics
  private final PneumaticHub pHub = new PneumaticHub(62);
  private final DoubleSolenoid dSolenoid1 = pHub.makeDoubleSolenoid (8,9);
  private final DoubleSolenoid dSolenoid2 = pHub.makeDoubleSolenoid (10,11);
  private DoubleSolenoid.Value state = DoubleSolenoid.Value.kReverse;
  
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
   shoot2.setInverted(true);
   

   // double kP = 1;

    m_myRobot = new DifferentialDrive(m_left, m_right);
    stick = new Joystick(0);
    stick2 = new Joystick(1);
    c = new XboxController(2);
    
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
    candle.setLEDs(255, 0, 0);
    m_myRobot.arcadeDrive(-stick.getY(), stick2.getZ()/1.2);
   //encoder.setPositionConversionFactor(0.165);

   encodeL1.setPositionConversionFactor(Math.PI/2);
   encodeL2.setPositionConversionFactor(Math.PI/2);
   encodeR1.setPositionConversionFactor(Math.PI/2);
   encodeR2.setPositionConversionFactor(Math.PI/2);


   /*SmartDashboard.putNumber("EncoderL1 inches", encodeL1.getPosition());
   SmartDashboard.putNumber("EncoderL2 inches", encodeL2.getPosition());
   SmartDashboard.putNumber("EncoderR1 inches", encodeR1.getPosition());
   SmartDashboard.putNumber("EncoderR2 inches", encodeR2.getPosition());

   SmartDashboard.putNumber("EncoderL1 speed", encodeL1.getVelocity());
   SmartDashboard.putNumber("EncoderL2 speed", encodeL2.getVelocity());
   SmartDashboard.putNumber("EncoderR1 speed", encodeR1.getVelocity());
   SmartDashboard.putNumber("EncoderR2 speed", encodeR2.getVelocity());*

   SmartDashboard.putNumber("Get Voltage", sensUltrasonic.getVoltage());
   if(sensUltrasonic.getVoltage() < 0.3){
    m_intake.set(0.5);
  }
  else{
    m_intake.set(0.0);
  }*/
   

  //intake 
    if(c.getRightBumperPressed() == true){
        m_intake.set(-1);
        candle.setLEDs(255, 255, 255);
    }
    else if(c.getLeftBumperPressed() == true){
      m_intake.set(1);
      candle.setLEDs(0, 0, 0);
    }
    else{
      m_intake.set(0);
    }

 
 //Set speed for shooting and shoots
  if (stick.getTriggerPressed()){
    candle.setLEDs(0,0,255);
    if (c.getXButtonPressed()){
      m_shoot.set(0.25);
    }
    if (c.getYButtonPressed()){
      m_shoot.set(0.75);
    }
    if (c.getBButtonPressed()){
      m_shoot.set(0.8);
    }
    if (c.getAButtonPressed()){
      m_shoot.set(1);
    }
    else{
      m_shoot.set(.5);
    }
    }
 else if (stick.getTriggerReleased()){
   m_shoot.set(0);
 }


 //pnuematics
 if(c.getLeftStickButtonPressed()){
 if(state == DoubleSolenoid.Value.kReverse){
  state = DoubleSolenoid.Value.kForward;
  dSolenoid1.set(state);
  dSolenoid2.set(state);
 }
 else{
  state = DoubleSolenoid.Value.kReverse;
  dSolenoid1.set(state);
  dSolenoid2.set(state);
  //candle.setLEDs (100,0,230);
 }
}


if(c.getRightStickButtonPressed()){
  uptake.set(.7);
}
else{
uptake.set(0);
}
     //m_left.set(stick.getY());
 // m_left.set(stick.getY() + stick.getX());
  // m_right.set(stick.getY() - stick.getX());
    if(encodeL1.getVelocity() > 0){
      candle.setLEDs(255, 255, 0);
    }
    else if(encodeL1.getVelocity() < 0){
      candle.setLEDs (127, 0, 255);
    }
  }


public void disabledInit() {
  super.disabledInit();
  candle.setLEDs(255,0,0);
}
@Override
public void disabledExit() {
  
  super.disabledExit();
  candle.setLEDs(0,255,0);
}

}