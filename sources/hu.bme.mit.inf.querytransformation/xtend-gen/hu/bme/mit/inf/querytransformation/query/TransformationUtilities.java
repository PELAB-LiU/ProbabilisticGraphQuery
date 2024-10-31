package hu.bme.mit.inf.querytransformation.query;

import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Annotation;
import org.eclipse.viatra.query.patternlanguage.emf.vql.AnnotationParameter;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.ValueReference;
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationUtilities {
  protected enum PSource {
    ANNOTATION,

    PARAMETER;
  }

  protected final VQLParser g = new VQLParser();

  public int reducedParameterCount(final Pattern pattern) {
    int _xifexpression = (int) 0;
    TransformationUtilities.PSource _probabilitySourceType = this.getProbabilitySourceType(pattern);
    boolean _tripleEquals = (_probabilitySourceType == TransformationUtilities.PSource.PARAMETER);
    if (_tripleEquals) {
      int _size = pattern.getParameters().size();
      _xifexpression = (_size - 1);
    } else {
      _xifexpression = pattern.getParameters().size();
    }
    return _xifexpression;
  }

  public int requiredPatternMaxArgCount(final PatternModel model) {
    final Function1<Pattern, Boolean> _function = (Pattern pattern) -> {
      return Boolean.valueOf(this.isBasicEventDefinition(pattern));
    };
    final Function2<Integer, Pattern, Integer> _function_1 = (Integer max, Pattern pattern) -> {
      return Integer.valueOf(Math.max((max).intValue(), this.reducedParameterCount(pattern)));
    };
    return (int) IterableExtensions.<Pattern, Integer>fold(IterableExtensions.<Pattern>filter(model.getPatterns(), _function), Integer.valueOf(0), _function_1);
  }

  public Iterable<Pattern> filterRequiredArg(final PatternModel model, final int count) {
    final Function1<Pattern, Boolean> _function = (Pattern pattern) -> {
      return Boolean.valueOf(this.isBasicEventDefinition(pattern));
    };
    final Function1<Pattern, Boolean> _function_1 = (Pattern pattern) -> {
      int _reducedParameterCount = this.reducedParameterCount(pattern);
      return Boolean.valueOf((_reducedParameterCount == count));
    };
    return IterableExtensions.<Pattern>filter(IterableExtensions.<Pattern>filter(model.getPatterns(), _function), _function_1);
  }

  public int countRequiredArg(final PatternModel model, final int count) {
    final Function1<Pattern, Boolean> _function = (Pattern pattern) -> {
      return Boolean.valueOf(this.isBasicEventDefinition(pattern));
    };
    final Function2<Integer, Pattern, Integer> _function_1 = (Integer cnt, Pattern pattern) -> {
      Integer _xifexpression = null;
      TransformationUtilities.PSource _probabilitySourceType = this.getProbabilitySourceType(pattern);
      boolean _tripleEquals = (_probabilitySourceType == TransformationUtilities.PSource.PARAMETER);
      if (_tripleEquals) {
        Integer _xifexpression_1 = null;
        int _size = pattern.getParameters().size();
        int _minus = (_size - 1);
        boolean _tripleEquals_1 = (_minus == count);
        if (_tripleEquals_1) {
          _xifexpression_1 = Integer.valueOf(((cnt).intValue() + 1));
        } else {
          _xifexpression_1 = cnt;
        }
        _xifexpression = _xifexpression_1;
      } else {
        Integer _xifexpression_2 = null;
        int _size_1 = pattern.getParameters().size();
        boolean _tripleEquals_2 = (_size_1 == count);
        if (_tripleEquals_2) {
          _xifexpression_2 = Integer.valueOf(((cnt).intValue() + 1));
        } else {
          _xifexpression_2 = cnt;
        }
        _xifexpression = _xifexpression_2;
      }
      return _xifexpression;
    };
    return (int) IterableExtensions.<Pattern, Integer>fold(IterableExtensions.<Pattern>filter(model.getPatterns(), _function), Integer.valueOf(0), _function_1);
  }

  public boolean isEventBlocked(final Pattern pattern) {
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf("NoEvent".equals(annotation.getName()));
    };
    return IterableExtensions.<Annotation>exists(pattern.getAnnotations(), _function);
  }

  public boolean isBasicEventDefinition(final Pattern pattern) {
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf("BasicEvent".equals(annotation.getName()));
    };
    return IterableExtensions.<Annotation>exists(pattern.getAnnotations(), _function);
  }

  public boolean isKGateConstraint(final Constraint constraint) {
    if ((constraint instanceof CompareConstraint)) {
      final ValueReference aggregator = ((CompareConstraint)constraint).getRightOperand();
      if ((aggregator instanceof AggregatedValue)) {
        return "KGate".equals(((AggregatedValue)aggregator).getAggregator().getSimpleName());
      }
    }
    return false;
  }

  public boolean isWeightAnnotation(final Annotation annotation, final String lhsname) {
    boolean _equals = "Weight".equals(annotation.getName());
    boolean _not = (!_equals);
    if (_not) {
      return false;
    }
    final Function1<AnnotationParameter, Boolean> _function = (AnnotationParameter param) -> {
      return Boolean.valueOf("lhsname".equals(param.getName()));
    };
    ValueReference _value = IterableExtensions.<AnnotationParameter>findFirst(annotation.getParameters(), _function).getValue();
    final StringValue lhsparameter = ((StringValue) _value);
    return lhsname.equals(lhsparameter.getValue());
  }

  public TransformationUtilities.PSource getProbabilitySourceType(final Pattern pattern) {
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf("BasicEvent".equals(annotation.getName()));
    };
    final Function1<AnnotationParameter, Boolean> _function_1 = (AnnotationParameter param) -> {
      return Boolean.valueOf("probability".equals(param.getName()));
    };
    final ValueReference value = IterableExtensions.<AnnotationParameter>findFirst(IterableExtensions.<Annotation>findFirst(pattern.getAnnotations(), _function).getParameters(), _function_1).getValue();
    if ((value instanceof VariableReference)) {
      return TransformationUtilities.PSource.PARAMETER;
    }
    if ((value instanceof NumberValue)) {
      return TransformationUtilities.PSource.ANNOTATION;
    }
    return null;
  }

  public VariableReference getProbabilityVariable(final Pattern pattern) {
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf("BasicEvent".equals(annotation.getName()));
    };
    final Function1<AnnotationParameter, Boolean> _function_1 = (AnnotationParameter param) -> {
      return Boolean.valueOf("probability".equals(param.getName()));
    };
    ValueReference _value = IterableExtensions.<AnnotationParameter>findFirst(IterableExtensions.<Annotation>findFirst(pattern.getAnnotations(), _function).getParameters(), _function_1).getValue();
    final VariableReference value = ((VariableReference) _value);
    return value;
  }

  public double getProbability(final Pattern pattern) {
    double _xblockexpression = (double) 0;
    {
      final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
        return Boolean.valueOf("BasicEvent".equals(annotation.getName()));
      };
      final Function1<AnnotationParameter, Boolean> _function_1 = (AnnotationParameter param) -> {
        return Boolean.valueOf("probability".equals(param.getName()));
      };
      ValueReference _value = IterableExtensions.<AnnotationParameter>findFirst(IterableExtensions.<Annotation>findFirst(pattern.getAnnotations(), _function).getParameters(), _function_1).getValue();
      final NumberValue value = ((NumberValue) _value);
      _xblockexpression = Double.parseDouble(value.getValue().getValue());
    }
    return _xblockexpression;
  }
}
