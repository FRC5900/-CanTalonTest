/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;




/**
 * This sample program shows how to control a motor using a joystick. In the
 * operator control part of the program, the joystick is read and the value is
 * written to the motor.
 *
 * <p>Joystick analog values range from -1 to 1 and speed controller inputs also
 * range from -1 to 1 making it easy to work together.
 */
public class Robot extends TimedRobot 
{
  private static final int kJoystickPort = 0;
  private Joystick m_joystick;

  int encoder_value;

  TalonSRX motor = new TalonSRX(0);
  Talon motor2 = new Talon(0);
  Encoder enc = new Encoder(0, 1, true, Encoder.EncodingType.k1X);
 
  int Winch_State;
  int Winch_TargetCount;
  int Winch_CurrentCount;


  @Override
  public void robotInit() 
  {
    motor.set(ControlMode.PercentOutput, 0);
    m_joystick = new Joystick(kJoystickPort);
    enc.setMaxPeriod(.1);
    enc.setMinRate(10);
    enc.setDistancePerPulse(1);
    enc.setReverseDirection(true);
    enc.setSamplesToAverage(7);
    enc.reset();
    Winch_CurrentCount = 0;
    Winch_State = 0;
  }

  @Override
  public void teleopPeriodic() 
  {
   // motor.set(ControlMode.PercentOutput, m_joystick.getY());
   // motor2.set( m_joystick.getX());
    SmartDashboard.putNumber("Encoder", enc.get());

    if( m_joystick.getRawButton(1) == true )
    {
      if( Winch_State == 0 )
      {
        Winch_State = 1;
        enc.reset();
        Winch_TargetCount = 2000;
      }
    }
    Winch_Control();
  }



  public void Winch_Control()
  {
    switch ( Winch_State)
    {
      case 0:
        break;

      case 1:
        Winch_CurrentCount = enc.get();
        if( Winch_CurrentCount > ( Winch_TargetCount - 500) )
        {
          motor.set(ControlMode.PercentOutput, -0.10);
          
          Winch_State = 2;
        }
        else
        {
          motor.set(ControlMode.PercentOutput, -0.25);
        }
        break;

      case 2:
        Winch_CurrentCount = enc.get();
        if( Winch_CurrentCount > ( Winch_TargetCount ) )
        {
          motor.set(ControlMode.PercentOutput, 0.0);
          
          Winch_State = 0;
        }
        else
        {
          motor.set(ControlMode.PercentOutput, -0.10);
        }
        break;

      default:
        Winch_State = 0;
        motor.set(ControlMode.PercentOutput, 0.0);
        break;


    }


  }


}
