
public class PropositionalFormula {
	boolean TDA594;
	
	boolean Movement;
	boolean WaveSurfing;
	boolean TrueSurfing;
	boolean GoToSurfing;
	boolean PrecisPrediction;
	boolean Flattener;
	boolean RammingMovement;
	boolean RandomMovement;
	boolean Fluid;
	boolean Orbital;
	boolean MinimumRiskMovement;
	boolean StopAndGo;
	boolean ExactPathPredictorMovement;
	
	boolean Targeting;
	int nbrVirtualGuns;
	// VirtualGuns = nbrVirtualGuns > 0
	boolean VirtualGuns;
	boolean GuessFactorGun;
	boolean HeadOnTargeting;
	boolean CircularTargeting;
	boolean LinearTargeting;
	boolean PlayItForward;
	boolean KDTreeTargeting;
	boolean RandomTargeting;
	boolean PatternMatching;
	boolean CrowdTargeting;
	
	boolean Utilities;
	boolean Segmentation;
	boolean DynamicClustering;
    boolean MovementStratChange;
    boolean TargetingStratChange;
    boolean RadarStratChange;
	boolean EnergyManagement;
	boolean MatchTypeIdentification;
	
	boolean Radar;
	boolean TurnMultiplier;
	boolean InfinityLock;
	boolean SpinRadar;
	
	boolean DataManagement;
	boolean SaveTargetingData;
	boolean SaveBulletSynchronizedSnapshots;
	boolean SaveMovementData;
	boolean SaveWaveSurfingStats;
	boolean SaveGuessFactor;

	private boolean implies(boolean x, boolean y) {
	    return ((x && y)||(!x && y)||(!x && !y))
    }

    private boolean iff(boolean x, boolean y) {
	    return ((x && y) || (!x && !y))
    }

    boolean formula =
        // dependencies shown in the model, indentation means 1 level down
        implies(Movement, TDA594) &&
            iff(Movement, WaveSurfing || PrecisPrediction || Flattener || RammingMovement || RandomMovement ||
            MinimumRiskMovement || StopAndGo || ExactPathPredictorMovement) &&
                (iff(WaveSurfing, TrueSurfing || GoToSurfing )) && !(TrueSurfing && GoToSurfing ) &&
                implies(Fluid, RandomMovement)  &&
                implies(Orbital, RandomMovement)&&

        implies(Targeting, TDA594) &&
            iff(Targeting, VirtualGuns || GuessFactorGun ||HeadOnTargeting || CircularTargeting ||
            LinearTargeting || PlayItForward || RandomTargeting || PatternMatching || CrowdTargeting) &&
                implies(KDTreeTargeting, PlayItForward) &&

        implies(Utilities, TDA594) &&
            iff(Utilities, Segmentation || DynamicClustering || MovementStratChange || TargetingStratChange ||
            RadarStratChange || EnergyManagement || MatchTypeIdentification) &&

        implies(Radar, TDA594) &&
            iff(Radar, TurnMultiplier || InfinityLock || SpinRadar) &&

        implies(DataManagement, TDA594) &&
            iff(DataManagement, SaveTargetingData || SaveMovementData || SaveGuessFactor) &&
                implies(SaveBulletSynchronizedSnapshots, SaveTargetingData) &&
                implies(SaveWaveSurfingStats, SaveMovementData);

}
