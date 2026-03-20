package CatanAssignment1;

/**
 * Immediate value rule for VP-earning builds.
 * - Settlement: 1.0
 * - City: 1.0
 */
public class VpEarningRule implements ImmediateValueRule {

    @Override
    public double valueIfApplies(MachinePlayer agent, BuildAction action) {
        if (action == null) {
            return 0.0;
        }
        if (action.getType() == BuildType.SETTLEMENT || action.getType() == BuildType.CITY) {
            return 1.0;
        }
        return 0.0;
    }
}

