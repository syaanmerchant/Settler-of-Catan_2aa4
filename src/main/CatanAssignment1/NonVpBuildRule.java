package CatanAssignment1;

/**
 * Immediate value rule for non-VP builds.
 * - Road: 0.8
 */
public class NonVpBuildRule implements ImmediateValueRule {

    @Override
    public double valueIfApplies(MachinePlayer agent, BuildAction action) {
        if (action == null) {
            return 0.0;
        }
        if (action.getType() == BuildType.ROAD) {
            return 0.8;
        }
        return 0.0;
    }
}

