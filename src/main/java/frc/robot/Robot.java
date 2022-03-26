// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.filter.SlewRateLimiter;
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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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

  //private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  
double heading;


//camera
UsbCamera camera = new UsbCamera("Camera", "driver");
//CameraServer cs = new CameraSever();


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
 
  MotorControllerGroup m_shoot = new MotorControllerGroup(shoot1, shoot2);

  private final CANSparkMax uptake= new CANSparkMax(9, MotorType.kBrushed);

  private final WPI_TalonFX climb1= new WPI_TalonFX(10);
  private final WPI_TalonFX climb2= new WPI_TalonFX(11);
  MotorControllerGroup climb = new MotorControllerGroup(climb1, climb2);
  //private final CANSparkMax m_shoot = new CANSparkMax(6, MotorType.kBrushless);

  
 
  //private final CANSparkMax uptake3 = new CANSparkMax(, MotorType.kBrushless);
  //MotorControllerGroup uptake = new MotorControllerGroup(uptake1, uptake2); 

  //Sensor for uptake
  private final AnalogInput sensUltrasonic  = new AnalogInput(0);

  private final SlewRateLimiter accel = new SlewRateLimiter(1.4);
  private final SlewRateLimiter accelT = new SlewRateLimiter(1.35);
  
  //Motor Encoders
  private final RelativeEncoder encodeL1 = m_leftMotor1.getEncoder();
  private final RelativeEncoder encodeL2 = m_leftMotor2.getEncoder();
  private final RelativeEncoder encodeR1 = m_rightMotor1.getEncoder();
  private final RelativeEncoder encodeR2 = m_rightMotor2.getEncoder();

  private int autoMode = 0;
 
  SendableChooser auto = new SendableChooser();
	//auto.addDefault(autoNone, new AutoNone());
	//auto.addObject();
  //LED controls
  /*private final CANdle candle = new CANdle (0);
  private final RainbowAnimation r = new RainbowAnimation(1,1,140);
  private final FireAnimation f = new FireAnimation(1,1,8,.2,.4);
  private final RgbFadeAnimation fa = new RgbFadeAnimation (1,.7,8);
  private final StrobeAnimation s = new StrobeAnimation (100,200,8, 255, 1, 8);
  private final ColorFlowAnimation cf = new ColorFlowAnimation(100,200,8, 255, 1, 8, Direction.Backward);
  */
  //Pnuematics
  private final PneumaticHub pHub = new PneumaticHub(62);
  private final DoubleSolenoid dSolenoid1 = pHub.makeDoubleSolenoid (8,9);
  private final DoubleSolenoid dSolenoid2 = pHub.makeDoubleSolenoid (6,7);
  private DoubleSolenoid.Value state = DoubleSolenoid.Value.kReverse;
  
  Timer m_timer = new Timer();
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
  
  //reset encoders
   encodeL1.setPosition(0);
   encodeL2.setPosition(0);
   encodeR1.setPosition(0);
   encodeR2.setPosition(0);

   //invert motors and set climber to breaking
   m_right.setInverted(true);
   shoot2.setInverted(true);
   climb1.setNeutralMode(NeutralMode.Brake);
   climb2.setNeutralMode(NeutralMode.Brake);
   //climb2.setInverted(true);
   
    //Drive train and input
    m_myRobot = new DifferentialDrive(m_left, m_right);
    stick = new Joystick(0);
    stick2 = new Joystick(1);
    c = new XboxController(2);
   
    //camera feed
    CameraServer.addCamera(camera);
    CameraServer.startAutomaticCapture();
    CameraServer.getVideo();
   
  
    //m_rightStick = new Joystick(1);

  }
  @Override
  public void robotPeriodic() {
      // TODO Auto-generated method stub
      super.robotPeriodic();
    
      //selects auto mode
      if(stick.getRawButton(7)){
        autoMode = 0;
        System.out.println("Low goal auto selected");
      }
      if(stick.getRawButton(8)){
        autoMode = 1;
        System.out.println("High goal auto selected");
      }
      SmartDashboard.putNumber("Auto", autoMode);
  }
  @Override
  public void autonomousInit(){
    m_timer.reset();
    
    encodeL1.setPosition(0);
    encodeL2.setPosition(0);
    encodeR1.setPosition(0);
    encodeR2.setPosition(0);
     
    //autoMode = SmartDashboard.getNumber("Auto Selector", 0);
    //System.out.println(SmartDashboard.getNumber("Auto Selector", 1));
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
   if(autoMode == 0){
     m_timer.start();
    if(m_timer.get() <= 2){ 
      m_shoot.set(.5);
      uptake.set(.7);
  }
    else if(m_timer.get() <= 4  && m_timer.get() > 2){
        kicker.set(1);
      }
    else if (m_timer.get() > 4){
      kicker.set(0);
      m_shoot.set(0);
      uptake.set(0);
      if(encodeL1.getPosition() >= -72){
        goBackward();
    }
              else{
        stop();
      }
    }
   }
    if(autoMode == 1){
    if(encodeL1.getPosition() >= -72/3){
      goBackward();
      m_shoot.set(1);
      uptake.set(.7);
  }
  else{
    if(m_timer.get() < 1){
    m_timer.start();
    }
 else if(m_timer.get() > 1 && m_timer.get() < 2){
    kicker.set(1);
  }
  else if(m_timer.get() > 2 && encodeL1.getPosition() >= -72){
    m_shoot.set(0);
    kicker.set(0);
    uptake.set(0);
    goBackward();
  }
}
}
   /*if(m_timer.get() <= 2){ 
      m_shoot.set(.5);
      uptake.set(.7);
  }
    else if(m_timer.get() <= 4  && m_timer.get() > 2){
        kicker.set(1);
      }
    else if (m_timer.get() > 4){
      kicker.set(0);
      m_shoot.set(0);
      uptake.set(0);
      if(encodeL1.getPosition() >= -72){
        goBackward();
    }
      else{
        stop();
      }
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
  public void stop(){ 
    m_left.set(0);
    m_right.set(0);
  }  
  @Override
  public void teleopPeriodic() {
   // candle.setLEDs(255, 0, 0);

    //Makes robot drive
    //divide Z by 1.2
   if(stick2.getRawButton(2)){
    m_myRobot.arcadeDrive(-stick.getY(), stick2.getZ());
   }
   else{
    m_myRobot.arcadeDrive(-accel.calculate(stick.getY()), stick2.getZ()/1.2);
   }
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
 // SmartDashboard.getNumber("rpm", shoot1.getSelectedSensorVelocity());
  //System.out.println(shoot1.getSelectedSensorVelocity());
  //intake 
    if(c.getRightBumperPressed()){
        m_intake.set(-.9);
        uptake.set(.9);
        //candle.setLEDs(255, 255, 255);
    }
    else if(c.getLeftBumperPressed()){
      m_intake.set(.9);
      uptake.set(-.7);
     // uptake.set(.7);
      //candle.setLEDs(0, 0, 0);
    }
    else if(c.getRightBumperReleased()  || c.getLeftBumperReleased()){
      m_intake.set(0);
      uptake.set(0);
    }

 
 //Activates kicker to launch ball
  if (stick2.getTriggerPressed()){
    //candle.setLEDs(0,0,255);
    //was 1
    kicker.set(1);
    uptake.set(0.7);
    }
    else if (stick2.getTriggerReleased()){
      kicker.set(0);
      uptake.set(0);
    }
    //Low goal, point blank
    if(c.getRawButtonPressed(2)){
     //.5
      m_shoot.set(.5);
     // uptake.set(.7);
    }
    //high, tarmac
    else if(c.getRawButtonPressed(3)){
      m_shoot.set(1);
      //uptake.set(.7);
    }

    else if(c.getRawButtonPressed(4)){
      m_shoot.set(.1);
      //uptake.set(.7);
    }
    else if (c.getRawButtonReleased(2) || c.getRawButtonReleased(3) || c.getRawButtonReleased(4)){                                   
      m_shoot.set(0);                                                                                                                                   
      uptake.set(0);
    }
  
  
   //SmartDashboard.getNumber("rpm", shoot1.getMotorOutputPercent());
   /* if(shoot1.getSelectedSensorVelocity() > rpm * speed * .8){
      kicker.set(1);
      uptake.set(.7);
    }*/
    
 //pnuematics
 if(c.getRawButtonPressed(8)){
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
  if (c.getRawButtonPressed(9)){
  //candle.setLEDs(0,0,255);
    climb.set(1);
  
  }
  else if(c.getRawButtonReleased(9)){
    climb.set(0);
  }
  if (stick.getTriggerPressed()){
    //candle.setLEDs(0,0,255);
    climb.set(-.5);
    
    }
    else if(stick.getTriggerReleased()){
      climb.set(0);
    }


if(c.getRawButtonPressed(7)){
  uptake.set(.5);
}
else if (c.getRawButtonReleased(7)){

uptake.set(0);
}
     //m_left.set(stick.getY());
 // m_left.set(stick.getY() + stick.getX());
  // m_right.set(stick.getY() - stick.getX());
    if(encodeL1.getVelocity() > 0){
     // candle.setLEDs(255, 255, 0);
    }
    else if(encodeL1.getVelocity() < 0){
      //candle.setLEDs (127, 0, 255);
    }
  }


public void disabledInit() {
  super.disabledInit();
  //candle.setLEDs(255,0,0);
}
@Override
public void disabledExit() {
  
  super.disabledExit();
 // candle.setLEDs(0,255,0);
}

}