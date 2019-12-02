package robots;
import robocode.*;
import robocode.util.Utils;
import java.awt.geom.*;

//#if ColorBlueBlackYellow
import java.awt.Color;
//#endif

//#if WaveSrufing
import java.lang.*;         // for Double and Integer objects
import java.util.ArrayList; // for collection of waves
//#endif


public class Main extends AdvancedRobot {
	//#if WaveSurfing
	 public static int BINS = 47;
	    public static double _surfStats[] = new double[BINS]; // we'll use 47 bins
	    public Point2D.Double _myLocation;     // our bot's location
	    public Point2D.Double _enemyLocation;  // enemy bot's location

	    public ArrayList _enemyWaves;
	    public ArrayList _surfDirections;
	    public ArrayList _surfAbsBearings;
		public static double _oppEnergy = 100.0;

	    public static Rectangle2D.Double _fieldRect
        = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);
    public static double WALL_STICK = 160;
	//#endif
		
	//#if GuessFactorGun
	private static final double BULLET_POWER = 1.9;
	private static double lateralDirection;	
	private static double lastEnemyVelocity;
	//#endif	
	
	//#if RandomOrbitalMovement
	private static GFTMovement movement;
	public GFTargetingBot() {
		movement = new GFTMovement(this);	
	}
	//#endif
	
	public void run() {
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		//#if ColorBlueBlackYellow
		setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
		//#endif
		
		//#if GuessFactorGun
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		//#endif
		
		//#if WaveSurfing
        _enemyWaves = new ArrayList();
        _surfDirections = new ArrayList();
        _surfAbsBearings = new ArrayList();
        //#endif
        
        do {
			turnRadarRightRadians(Double.POSITIVE_INFINITY); 
		} while (true);
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
		//#if WaveSurfing
		_myLocation = new Point2D.Double(getX(), getY());

        double lateralVelocity = getVelocity()*Math.sin(e.getBearingRadians());

        setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);

        _surfDirections.add(0,
            new Integer((lateralVelocity >= 0) ? 1 : -1));
        _surfAbsBearings.add(0, new Double(enemyAbsoluteBearing + Math.PI));


        double bulletPower = _oppEnergy - e.getEnergy();
        if (bulletPower < 3.01 && bulletPower > 0.09
            && _surfDirections.size() > 2) {
            EnemyWave ew = new EnemyWave();
            ew.fireTime = getTime() - 1;
            ew.bulletVelocity = bulletVelocity(bulletPower);
            ew.distanceTraveled = bulletVelocity(bulletPower);
            ew.direction = ((Integer)_surfDirections.get(2)).intValue();
            ew.directAngle = ((Double)_surfAbsBearings.get(2)).doubleValue();
            ew.fireLocation = (Point2D.Double)_enemyLocation.clone(); // last tick

            _enemyWaves.add(ew);
        }

        _oppEnergy = e.getEnergy();

        // update after EnemyWave detection, because that needs the previous
        // enemy location as the source of the wave
        _enemyLocation = project(_myLocation, enemyAbsoluteBearing, e.getDistance());

        updateWaves();
        doSurfing();
		//#endif
        
		//if GuessFactorGun
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(this);
		wave.gunLocation = new Point2D.Double(getX(), getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		setFire(wave.bulletPower);
		if (getEnergy() >= BULLET_POWER) {
			addCustomEvent(wave);
		}
		//#endif
		
		
	}
	
	

}
