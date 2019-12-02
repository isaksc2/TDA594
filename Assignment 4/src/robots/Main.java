package robots;
import robocode.*;
import robocode.util.Utils;
//#if GuessFactorGun
import java.awt.Color;
//#endif

//#if GuessFactorGun
import java.awt.geom.*;     // for Point2D's
import java.lang.*;         // for Double and Integer objects
import java.util.ArrayList; // for collection of waves
//#endif

public class Main extends AdvancedRobot {
	//#if GuessFactorGun
	private static final double BULLET_POWER = 1.9;
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	//#endif
	
	
	
	
	public void run() {
		//#if GuessFactorGun
		setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		//#endif
	}
	
	

}
