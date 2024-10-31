package hu.bme.mit.inf.querytransformation.query;

import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationParameter;
import org.eclipse.viatra.query.patternlanguage.emf.annotations.PatternAnnotationValidator;

@SuppressWarnings("all")
public class BasicEvent extends PatternAnnotationValidator {
  private static final String NAME = "BasicEvent";

  private static final String DESCRIPTION = "Define a basic event for each match of this query.";

  private static final PatternAnnotationParameter PROBABILITY = new PatternAnnotationParameter("probability", PatternAnnotationParameter.DOUBLE, "Probability of the basic event.", false, true);

  public BasicEvent() {
    super(BasicEvent.NAME, BasicEvent.DESCRIPTION, BasicEvent.PROBABILITY);
  }
}
