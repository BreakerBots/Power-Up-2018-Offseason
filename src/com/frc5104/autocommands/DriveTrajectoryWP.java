package com.frc5104.autocommands;

import com.frc5104.calc.BreakerMath;
import com.frc5104.calc.BreakerTrajectoryGenerator;
import com.frc5104.main.Constants;
import com.frc5104.main.subsystems.Drive;
import com.frc5104.utilities.console;
import com.frc5104.utilities.console.c;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

/*Breakerbots Robotics Team 2018*/
/**
 * Generate and Follow a Trajectory using Pathfinder
 * (!) Deprication Notice
 */
public class DriveTrajectoryWP extends BreakerCommand {

	private Trajectory t;
	private TankModifier m;
	EncoderFollower l;
	EncoderFollower r;
		
    public DriveTrajectoryWP(Waypoint[] points) {
    	t = BreakerTrajectoryGenerator.getTrajectory(points);
    	m = BreakerTrajectoryGenerator.getTankModifier(t);
    	l = new EncoderFollower(m.getLeftTrajectory());
		r = new EncoderFollower(m.getRightTrajectory());
    }

    public void init() {
    	console.log(c.AUTO, "Running MP Path");
    	
    	//Reset Devices
    	Drive.Gyro.reset();
		Drive.encoders.reset(10);
		
		//Configure Tank Drive
		l.configureEncoder(0, (int) Math.round(Constants._ticksPerRevolution), Constants._wheelDiameter);
		l.configurePIDVA(Constants.AutonomousWP._PIDA[0], Constants.AutonomousWP._PIDA[1], Constants.AutonomousWP._PIDA[2], 1 / Constants.AutonomousWP._maxVelocity, Constants.AutonomousWP._PIDA[3]);
		r.configureEncoder(0, (int) Math.round(Constants._ticksPerRevolution), Constants._wheelDiameter);
		r.configurePIDVA(Constants.AutonomousWP._PIDA[0], Constants.AutonomousWP._PIDA[1], Constants.AutonomousWP._PIDA[2], 1 / Constants.AutonomousWP._maxVelocity, Constants.AutonomousWP._PIDA[3]);
		
		//Wait 100ms for Device Catchup
		try {
			Thread.sleep(100);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public boolean update() {
		int le = Drive.encoders.getLeft();
		int re = Drive.encoders.getRight();
		
		//Calculate Left and Right Speed for Wheels
		double ls = l.calculate(-le);
		double rs = r.calculate(-re);

		//Calculate Angle
		double a = Pathfinder.boundHalfDegrees((-1.0/80.0) * (Pathfinder.boundHalfDegrees(Pathfinder.r2d(l.getHeading()) - Drive.Gyro.getAngle())) * Constants.AutonomousWP._angleMult);
		
		//Clamp All Varibles
		a = BreakerMath.clamp(a, -1, 1);
		ls = BreakerMath.clamp(ls, -1, 1);
		rs = BreakerMath.clamp(rs, -1, 1);
		
		//Move The Wheels
		Drive.getInstance().set(
				-ls + a, //Left
				-rs - a  //Right
		);
		
		//Return
		return l.isFinished() && r.isFinished();
    }

    public void end() {
    	console.log(c.AUTO, "MP Path Finished");
    }
}
