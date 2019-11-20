
public class PropositionalFormula {
	static boolean TDA594;

	// Movement
	static boolean Movement;
    static boolean WaveSurfing;
    static boolean TrueSurfing;
    static boolean GoToSurfing;
    static boolean PrecisePrediction;
    static boolean Flattener;
    static boolean RammingMovement;
	static boolean RandomMovement;
	static boolean Fluid;
	static boolean Orbital;
	static boolean MinimumRiskMovement;
	static boolean StopAndGo;
	static boolean ExactPathPredictorMovement;
	static boolean GuessFactorDodging;
	static boolean OscillatorMovement;

	// Targeting
	static boolean Targeting;
	static boolean GuessFactorGun;
	static boolean HeadOnTargeting;
	static boolean CircularTargeting;
	static boolean LinearTargeting;
	static boolean PlayItForward;
	static boolean KDTreeTargeting;
	static boolean RandomTargeting;
	static boolean PatternMatching;
	static boolean CrowdTargeting;

	// Utilities
	static boolean Utilities;
	static boolean Segmentation;
	static boolean DynamicClustering;
    static boolean MovementStratChange;
    static boolean TargetingStratChange;
    static int nbrVirtualGuns = 0;
    // VirtualGuns = nbrVirtualGuns > 0
    static boolean RadarStratChange;
	static boolean EnergyManagement;
	static boolean MatchTypeIdentification;

	// Radar
	static boolean Radar;
	static boolean TurnMultiplier;
	static boolean InfinityLock;
	static boolean SpinRadar;

	// Data Management
	static boolean DataManagement;
	static boolean SaveTargetingData;
	static boolean SaveBulletSynchronizedSnapshots;
	static boolean SaveMovementData;
	static boolean SaveWaveSurfingStats;
	static boolean SaveGuessFactor;

	static private boolean implies(boolean x, boolean y) {
	    return ((x && y)||(!x && y)||(!x && !y));
    }

    static private boolean iff(boolean x, boolean y) {
	    return ((x && y) || (!x && !y));
    }

    private static boolean evaluateFormula() {

	    boolean VirtualGuns = nbrVirtualGuns > 0;

        boolean formula =
            // dependencies shown in the model, indentation means 1 level down
            // Movement
            implies(Movement, TDA594) &&
                iff(Movement, WaveSurfing || PrecisePrediction || Flattener || RammingMovement || RandomMovement ||
                        MinimumRiskMovement || StopAndGo || ExactPathPredictorMovement ||
                        GuessFactorDodging || OscillatorMovement) &&
                (iff(WaveSurfing, TrueSurfing || GoToSurfing )) && !(TrueSurfing && GoToSurfing ) &&
                implies(Fluid, RandomMovement)  &&
                implies(Orbital, RandomMovement) &&

            // Targeting
            implies(Targeting, TDA594) &&
                iff(Targeting, GuessFactorGun ||HeadOnTargeting || CircularTargeting ||
                        LinearTargeting || PlayItForward || RandomTargeting || PatternMatching || CrowdTargeting) &&
                implies(KDTreeTargeting, PlayItForward) &&

            // Utilities
            implies(Utilities, TDA594) &&
                iff(Utilities, Segmentation || DynamicClustering || MovementStratChange || TargetingStratChange ||
                        RadarStratChange || EnergyManagement || MatchTypeIdentification) &&
                implies(VirtualGuns, TargetingStratChange) &&

            // Radar
            implies(Radar, TDA594) &&
                iff(Radar, TurnMultiplier || InfinityLock || SpinRadar) &&

            // Data Management
            implies(DataManagement, TDA594) &&
                iff(DataManagement, SaveTargetingData || SaveMovementData || SaveGuessFactor) &&
                implies(SaveBulletSynchronizedSnapshots, SaveTargetingData) &&
                implies(SaveWaveSurfingStats, SaveMovementData) &&

            // cross tree constraints
            // -------------------------------------------

            // Movement
            implies(PrecisePrediction, WaveSurfing) &&
            implies(Flattener, WaveSurfing) &&
            implies(GuessFactorDodging, WaveSurfing) &&

            // Targeting
            implies(Targeting, Radar) &&

            // Utilities
            implies(Segmentation, WaveSurfing || GuessFactorGun) &&
            implies(DynamicClustering, WaveSurfing || GuessFactorGun) &&
            implies(MovementStratChange, Movement) &&
            implies(TargetingStratChange, Targeting) &&
            implies(VirtualGuns, Targeting) &&
            implies(RadarStratChange, Radar) &&
            implies(EnergyManagement, Targeting) &&
            implies(MatchTypeIdentification, Targeting || Movement) &&

            // DataManagement
            implies(SaveTargetingData, Targeting) &&
            implies(SaveMovementData, Movement) &&
            implies(SaveWaveSurfingStats, WaveSurfing) &&
            implies(SaveGuessFactor, GuessFactorGun || GuessFactorDodging);

        return formula;
    }

	public static void main(String[] args) {
	    TDA594 = true;
        Fluid = true;
        Movement = true;
        MinimumRiskMovement = false;
        RandomMovement = true;
	    System.out.println(evaluateFormula());
    }
}
