package com.frc5104.autopaths;

import com.frc5104.autocommands.*;
import com.frc5104.main.Constants;

import jaci.pathfinder.Waypoint;

public class CL extends BreakerCommandGroup {
	Waypoint[] points = {
		new Waypoint(0, 0, 0),
		new Waypoint(12, -5.0 * Constants._xAngleMult, 0)
	};
	
	public void init() {
		add(new MotionProfile(points));
		add(new DriveStop());
	}
}
