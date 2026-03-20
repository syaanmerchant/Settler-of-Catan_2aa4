package CatanAssignment1;

/**
 * Rule used for computing an immediate value for a candidate action.
 * If the rule does not apply, it should return 0.0.
 */
public interface ImmediateValueRule {

    double valueIfApplies(MachinePlayer agent, BuildAction action);
}

