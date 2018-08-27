package com.frc5104.autopaths;

import com.frc5104.autocommands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;
import com.frc5104.pathfinder.Waypoint;

public class RL extends CommandGroup {
	public RL() {
		Waypoint[] points = {
			new Waypoint(0, 0, 0),
			new Waypoint(0, 10, 0)
		};
		addSequential(new MotionProfile(points));
    	addParallel(new StopDrive());
	}
}
