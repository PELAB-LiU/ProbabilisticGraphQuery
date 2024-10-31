package hu.bme.mit.inf.querytransformation.query;

import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationParameter;
import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationValidator;

@SuppressWarnings("all")
public class Weight extends PatternAnnotationValidator {
  private static final String NAME = "KGate";

  private static final String DESCRIPTION = "TODO";

  private static final PatternAnnotationParameter PROBABILITY = new PatternAnnotationParameter("weights", PatternAnnotationParameter.VARIABLEREFERENCE, "Probability of the basic event.", false, true);

  public Weight() {
    super(Weight.NAME, Weight.DESCRIPTION, Weight.PROBABILITY);
  }
}
