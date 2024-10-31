package hu.bme.mit.inf.querytransformation.query;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Annotation;
import org.eclipse.viatra.query.patternlanguage.emf.vql.AnnotationParameter;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CallableRelation;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.ValueReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import tracemodel.TracemodelPackage;

/**
 * UNIMPLEMENTED: negation, not patterns
 * TODO: collection export
 * 		fix stochastic version
 * TODO: stochastic patterns for basic event definitions (usability in find)
 */
@Deprecated
@SuppressWarnings("all")
public class ReliabilityPatternGeneration {
  private final Set<Pattern> stochasticPatterns = CollectionLiterals.<Pattern>newHashSet();

  private final Set<Pattern> basicEventPatterns = CollectionLiterals.<Pattern>newHashSet();

  private final Set<Pattern> needNotPattern = CollectionLiterals.<Pattern>newHashSet();

  private final VQLParser g = new VQLParser();

  public static Object doSetup() {
    return EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE);
  }

  public void test() {
    try {
      EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE);
      EMFPatternLanguageStandaloneSetup.doSetup();
      final PatternParsingResults parsed = PatternParserBuilder.instance().parse(
        Files.readString(
          Path.of(
            "/home/mate/graphquery/hu.bme.mit.inf.dslreasoner.domains.satellite1/src/hu/bme/mit/inf/dslreasoner/domains/satellite1/queries/reliability.vql")));
      if ((parsed.hasError() || IterableExtensions.isEmpty(parsed.getPatterns()))) {
        InputOutput.<String>println("Input contains errors or empty.");
        final Consumer<Issue> _function = (Issue err) -> {
          InputOutput.<Issue>println(err);
        };
        parsed.getErrors().forEach(_function);
      } else {
        EObject _eContainer = (((Pattern[])Conversions.unwrapArray(parsed.getPatterns(), Pattern.class))[0]).eContainer();
        final PatternModel patternmodel = ((PatternModel) _eContainer);
        final Pair<HashSet<Pattern>, HashSet<Pattern>> stochastics = this.getStochasticPatterns(patternmodel);
        this.stochasticPatterns.clear();
        this.stochasticPatterns.addAll(stochastics.getKey());
        this.basicEventPatterns.clear();
        this.basicEventPatterns.addAll(stochastics.getValue());
        StringConcatenation _builder = new StringConcatenation();
        CharSequence _generateImports = this.generateImports(patternmodel);
        _builder.append(_generateImports);
        _builder.newLineIfNotEmpty();
        CharSequence _generateUserQueries = this.generateUserQueries(patternmodel);
        _builder.append(_generateUserQueries);
        _builder.newLineIfNotEmpty();
        CharSequence _generateInterfaceQueries = this.generateInterfaceQueries();
        _builder.append(_generateInterfaceQueries);
        _builder.newLineIfNotEmpty();
        final String vqlcontent = _builder.toString();
        InputOutput.<String>println(vqlcontent);
        final PatternParsingResults parsed2 = PatternParserBuilder.instance().parse(vqlcontent);
        final Consumer<Issue> _function_1 = (Issue error) -> {
          System.err.println(error);
        };
        parsed2.getErrors().forEach(_function_1);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public String transformPatternFile(final File vql) {
    try {
      final PatternParsingResults parsed = PatternParserBuilder.instance().parse(
        Files.readString(Path.of(vql.getPath())));
      if ((parsed.hasError() || IterableExtensions.isEmpty(parsed.getPatterns()))) {
        InputOutput.<String>println("Input contains errors or empty.");
        final Consumer<Issue> _function = (Issue err) -> {
          InputOutput.<Issue>println(err);
        };
        parsed.getErrors().forEach(_function);
        throw new RuntimeException();
      } else {
        EObject _eContainer = (((Pattern[])Conversions.unwrapArray(parsed.getPatterns(), Pattern.class))[0]).eContainer();
        final PatternModel patternmodel = ((PatternModel) _eContainer);
        final Pair<HashSet<Pattern>, HashSet<Pattern>> stochastics = this.getStochasticPatterns(patternmodel);
        this.stochasticPatterns.clear();
        this.stochasticPatterns.addAll(stochastics.getKey());
        this.basicEventPatterns.clear();
        this.basicEventPatterns.addAll(stochastics.getValue());
        InputOutput.println();
        StringConcatenation _builder = new StringConcatenation();
        CharSequence _generateImports = this.generateImports(patternmodel);
        _builder.append(_generateImports);
        _builder.newLineIfNotEmpty();
        CharSequence _generateUserQueries = this.generateUserQueries(patternmodel);
        _builder.append(_generateUserQueries);
        _builder.newLineIfNotEmpty();
        CharSequence _generateInterfaceQueries = this.generateInterfaceQueries();
        _builder.append(_generateInterfaceQueries);
        _builder.newLineIfNotEmpty();
        final String vqlcontent = _builder.toString();
        InputOutput.<String>println(vqlcontent);
        final PatternParsingResults parsed2 = PatternParserBuilder.instance().parse(vqlcontent);
        final Consumer<Issue> _function_1 = (Issue error) -> {
          System.err.println(error);
        };
        parsed2.getErrors().forEach(_function_1);
        return vqlcontent;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public CharSequence generateImports(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Imports for stochastic queries");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("import \"http://www.eclipse.org/emf/2002/Ecore\"");
    _builder.newLine();
    _builder.append("import \"http://www.example.org/tracemodel\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java reliability.mdd.COLLECT;");
    _builder.newLine();
    _builder.append("import java reliability.mdd.OR;");
    _builder.newLine();
    _builder.append("import java hu.bme.mit.delta.mdd.MddHandle; ");
    _builder.newLine();
    _builder.append("import java reliability.intreface.D;");
    _builder.newLine();
    _builder.append("//import java reliability.mdd.MddHandleCollection;");
    _builder.newLine();
    _builder.append("import java reliability.events.Event;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Imports for weight functions");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    CharSequence _generateWeightImports = this.generateWeightImports(model);
    _builder.append(_generateWeightImports);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Imports from the original model");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    CharSequence _generateImports = this.g.generateImports(model);
    _builder.append(_generateImports);
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public CharSequence generateWeightImports(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Pattern> _patterns = model.getPatterns();
      for(final Pattern pattern : _patterns) {
        {
          EList<PatternBody> _bodies = pattern.getBodies();
          for(final PatternBody body : _bodies) {
            {
              EList<Constraint> _constraints = body.getConstraints();
              for(final Constraint constraint : _constraints) {
                {
                  boolean _ksKGateConstraint = this.ksKGateConstraint(constraint);
                  if (_ksKGateConstraint) {
                    _builder.append("import java ");
                    String _kGateWeightClass = this.getKGateWeightClass(constraint);
                    _builder.append(_kGateWeightClass);
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }

  public String getKGateWeightClass(final Constraint constraint) {
    EObject _eContainer = constraint.eContainer();
    final PatternBody body = ((PatternBody) _eContainer);
    EObject _eContainer_1 = body.eContainer();
    final Pattern pattern = ((Pattern) _eContainer_1);
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf(this.isWeightAnnotation(annotation, this.g.value(this.getLeftHandSide(constraint)).toString()));
    };
    final Annotation annotation = IterableExtensions.<Annotation>findFirst(pattern.getAnnotations(), _function);
    StringConcatenation _builder = new StringConcatenation();
    final Function1<AnnotationParameter, Boolean> _function_1 = (AnnotationParameter p) -> {
      return Boolean.valueOf("class".equals(p.getName()));
    };
    ValueReference _value = IterableExtensions.<AnnotationParameter>findFirst(annotation.getParameters(), _function_1).getValue();
    String _value_1 = ((StringValue) _value).getValue();
    _builder.append(_value_1);
    return _builder.toString();
  }

  public CharSequence generateInterfaceQueries() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Interface queries for event management");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("pattern unaryBETrace(element: EObject, name: EString, index: EInt, trace: UnaryTrace){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("UnaryTrace.source(trace, element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.generator(trace, name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.index(trace, index);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern unaryInsertion(element: EObject, name: java String, from: java Integer, to: java Integer, probability: java Double){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBERequiredName(element, name, to, probability);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("neg find unaryBETrace(element, name, _, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("from == 0;");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBERequiredName(element, name, to, probability);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBETrace(element, name, _, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("from == max find unaryBETrace(element, name, #_, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("check(to > from);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern unaryRemoval(trace: UnaryTrace){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("UnaryTrace.source(trace, element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.generator(trace, name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("neg find unaryBERequiredName(element, name, _, _);");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBERequiredName(element, name, multiplicity, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("UnaryTrace.source(trace, element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.generator(trace, name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.index(trace, idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("check(idx > multiplicity);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern unaryHandleOf(element: EObject, name: EString, handle: Handle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBETrace(element, name, _, trace);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.handle(trace, handle);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }

  public CharSequence generateUserQueries(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Stochastic patterns");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    CharSequence _createStochasticPatterns = this.createStochasticPatterns();
    _builder.append(_createStochasticPatterns);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Deterministic patterns");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _createDeterministicPatterns = this.createDeterministicPatterns(model);
    _builder.append(_createDeterministicPatterns);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Event require pattern");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    CharSequence _createRequire = this.createRequire(model);
    _builder.append(_createRequire);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }

  private int bodykgateidx = 1;

  public String createDeterministicPatterns(final PatternModel model) {
    final HashSet<Pattern> patterns = CollectionLiterals.<Pattern>newHashSet();
    patterns.addAll(model.getPatterns());
    patterns.removeAll(this.stochasticPatterns);
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pattern pattern : patterns) {
        {
          Integer _countKGateConstraints = this.countKGateConstraints(pattern);
          boolean _greaterThan = ((_countKGateConstraints).intValue() > 0);
          if (_greaterThan) {
            _builder.append("pattern ");
            CharSequence _patternModifiers = this.g.patternModifiers(pattern);
            _builder.append(_patternModifiers);
            _builder.append(" ");
            CharSequence _patternName = this.g.patternName(pattern);
            _builder.append(_patternName);
            _builder.append("(");
            CharSequence _patternParameters = this.g.patternParameters(pattern);
            _builder.append(_patternParameters);
            _builder.append("){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find wc_");
            CharSequence _patternName_1 = this.g.patternName(pattern);
            _builder.append(_patternName_1, "\t");
            _builder.append("(");
            CharSequence _patternParameterNames = this.g.patternParameterNames(pattern);
            _builder.append(_patternParameterNames, "\t");
            _builder.append(",");
            {
              Integer _countKGateConstraints_1 = this.countKGateConstraints(pattern);
              IntegerRange _upTo = new IntegerRange(1, (_countKGateConstraints_1).intValue());
              boolean _hasElements = false;
              for(final Integer i : _upTo) {
                if (!_hasElements) {
                  _hasElements = true;
                } else {
                  _builder.appendImmediate(",", "\t");
                }
                _builder.append("_");
              }
            }
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("pattern wc_");
            CharSequence _patternName_2 = this.g.patternName(pattern);
            _builder.append(_patternName_2);
            _builder.append("(");
            CharSequence _patternParameters_1 = this.g.patternParameters(pattern);
            _builder.append(_patternParameters_1);
            _builder.append(",");
            {
              Integer _countKGateConstraints_2 = this.countKGateConstraints(pattern);
              IntegerRange _upTo_1 = new IntegerRange(1, (_countKGateConstraints_2).intValue());
              boolean _hasElements_1 = false;
              for(final Integer i_1 : _upTo_1) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                } else {
                  _builder.appendImmediate(",", "");
                }
                _builder.append("c");
                _builder.append(i_1);
                _builder.append(" : java MddHandleCollection");
              }
            }
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            {
              EList<PatternBody> _bodies = pattern.getBodies();
              boolean _hasElements_2 = false;
              for(final PatternBody body : _bodies) {
                if (!_hasElements_2) {
                  _hasElements_2 = true;
                } else {
                  _builder.appendImmediate(" or ", "");
                }
                _builder.append("{");
                _builder.newLine();
                _builder.append("\t");
                Object _xblockexpression = null;
                {
                  this.bodykgateidx = 1;
                  _xblockexpression = null;
                }
                _builder.append(_xblockexpression, "\t");
                _builder.newLineIfNotEmpty();
                {
                  EList<Constraint> _constraints = body.getConstraints();
                  for(final Constraint constraint : _constraints) {
                    {
                      boolean _ksKGateConstraint = this.ksKGateConstraint(constraint);
                      if (_ksKGateConstraint) {
                        _builder.append("\t");
                        int _plusPlus = this.bodykgateidx++;
                        CharSequence _stochasticKGateConstraint = this.stochasticKGateConstraint(constraint, _plusPlus);
                        _builder.append(_stochasticKGateConstraint, "\t");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        CharSequence _constraint = this.g.constraint(constraint);
                        _builder.append(_constraint, "\t");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                {
                  Integer _countKGateConstraints_3 = this.countKGateConstraints(body);
                  int _plus = ((_countKGateConstraints_3).intValue() + 1);
                  Integer _countKGateConstraints_4 = this.countKGateConstraints(pattern);
                  int _plus_1 = ((_countKGateConstraints_4).intValue() + 1);
                  ExclusiveRange _doubleDotLessThan = new ExclusiveRange(_plus, _plus_1, true);
                  for(final Integer i_2 : _doubleDotLessThan) {
                    _builder.append("\t");
                    _builder.append("c");
                    _builder.append(i_2, "\t");
                    _builder.append(" == eval(D.EmptyCollection);");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("}");
                _builder.newLine();
              }
            }
          } else {
            CharSequence _generateQuery = this.g.generateQuery(pattern);
            _builder.append(_generateQuery);
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }

  public CharSequence createStochasticPatterns() {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Pattern pattern : this.stochasticPatterns) {
        CharSequence _generateStochasticPattern = this.generateStochasticPattern(pattern);
        _builder.append(_generateStochasticPattern);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  public CharSequence generateStochasticPattern(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pattern ");
    CharSequence _patternName = this.g.patternName(pattern);
    _builder.append(_patternName);
    _builder.append("(");
    CharSequence _patternParameters = this.g.patternParameters(pattern);
    _builder.append(_patternParameters);
    _builder.append(",event: java MddHandle){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("event == OR find ua_");
    CharSequence _patternName_1 = this.g.patternName(pattern);
    _builder.append(_patternName_1, "\t");
    _builder.append("(");
    CharSequence _patternParameterNames = this.g.patternParameterNames(pattern);
    _builder.append(_patternParameterNames, "\t");
    _builder.append(",#_);");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("private pattern ua_");
    CharSequence _patternName_2 = this.g.patternName(pattern);
    _builder.append(_patternName_2);
    _builder.append("(");
    CharSequence _patternParameters_1 = this.g.patternParameters(pattern);
    _builder.append(_patternParameters_1);
    _builder.append(",event: java MddHandle)");
    _builder.newLineIfNotEmpty();
    {
      EList<PatternBody> _bodies = pattern.getBodies();
      boolean _hasElements = false;
      for(final PatternBody body : _bodies) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(" or ", "");
        }
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _generateStochasticBody = this.generateStochasticBody(body);
        _builder.append(_generateStochasticBody, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }

  public CharSequence generateNotPattern(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private pattern not_");
    CharSequence _patternName = this.g.patternName(pattern);
    _builder.append(_patternName);
    _builder.append("(");
    CharSequence _patternParameters = this.g.patternParameters(pattern);
    _builder.append(_patternParameters);
    _builder.append(", event: java MddHandle){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("neg find ");
    CharSequence _patternName_1 = this.g.patternName(pattern);
    _builder.append(_patternName_1, "\t");
    _builder.append("(");
    CharSequence _patternParameterNames = this.g.patternParameterNames(pattern);
    _builder.append(_patternParameterNames, "\t");
    _builder.append(",_);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("TraceModel.mddTrue(_,event);");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find ");
    CharSequence _patternName_2 = this.g.patternName(pattern);
    _builder.append(_patternName_2, "\t");
    _builder.append("(");
    CharSequence _patternParameterNames_1 = this.g.patternParameterNames(pattern);
    _builder.append(_patternParameterNames_1, "\t");
    _builder.append(",event1);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("event == eval(D.NOT(event1));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }

  protected CharSequence _stochasticVersion(final Constraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _constraint = this.g.constraint(constraint);
    _builder.append(_constraint);
    return _builder;
  }

  protected CharSequence _stochasticVersion(final PatternCompositionConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    {
      CallableRelation _call = constraint.getCall();
      boolean _contains = this.stochasticPatterns.contains(((PatternCall) _call).getPatternRef());
      if (_contains) {
        _builder.append("find ");
        {
          if ((constraint.isNegative() && this.notQueryNeeded(constraint))) {
            _builder.append("not_");
          }
        }
        CallableRelation _call_1 = constraint.getCall();
        CharSequence _patternName = this.g.patternName(((PatternCall) _call_1).getPatternRef());
        _builder.append(_patternName);
        _builder.append("(");
        CallableRelation _call_2 = constraint.getCall();
        CharSequence _patternCallParameters = this.g.patternCallParameters(((PatternCall) _call_2));
        _builder.append(_patternCallParameters);
        _builder.append(",event");
        int _plusPlus = this.eventid++;
        _builder.append(_plusPlus);
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      } else {
        CallableRelation _call_3 = constraint.getCall();
        boolean _isBasicEventDefinition = this.isBasicEventDefinition(((PatternCall) _call_3).getPatternRef());
        if (_isBasicEventDefinition) {
          {
            boolean _isNegative = constraint.isNegative();
            if (_isNegative) {
              _builder.append("find unaryHandleOf(");
              CallableRelation _call_4 = constraint.getCall();
              CharSequence _patternCallParameters_1 = this.g.patternCallParameters(((PatternCall) _call_4));
              _builder.append(_patternCallParameters_1);
              _builder.append(",\"");
              CallableRelation _call_5 = constraint.getCall();
              CharSequence _patternName_1 = this.g.patternName(((PatternCall) _call_5).getPatternRef());
              _builder.append(_patternName_1);
              _builder.append("\",event");
              int _plusPlus_1 = this.eventid++;
              _builder.append(_plusPlus_1);
              _builder.append("_p);");
              _builder.newLineIfNotEmpty();
              _builder.append("event_i_ == eval(D.NOT(");
              int _plusPlus_2 = this.eventid++;
              _builder.append(_plusPlus_2);
              _builder.append("_p));");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("find unaryHandleOf(");
              CallableRelation _call_6 = constraint.getCall();
              CharSequence _patternCallParameters_2 = this.g.patternCallParameters(((PatternCall) _call_6));
              _builder.append(_patternCallParameters_2);
              _builder.append(",\"");
              CallableRelation _call_7 = constraint.getCall();
              CharSequence _patternName_2 = this.g.patternName(((PatternCall) _call_7).getPatternRef());
              _builder.append(_patternName_2);
              _builder.append("\",event");
              int _plusPlus_3 = this.eventid++;
              _builder.append(_plusPlus_3);
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            }
          }
        } else {
          CharSequence _constraint = this.g.constraint(constraint);
          _builder.append(_constraint);
          _builder.newLineIfNotEmpty();
        }
      }
    }
    return _builder;
  }

  protected CharSequence _stochasticVersion(final CompareConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _constraint = this.g.constraint(constraint);
    _builder.append(_constraint);
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  /**
   * Generate KGate equivalent
   */
  private CharSequence stochasticKGateConstraint(final Constraint constraint, final int idx) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("collectionOf_");
    CharSequence _value = this.g.value(this.getLeftHandSide(constraint));
    _builder.append(_value);
    _builder.append(" == COLLECT find ");
    CharSequence _patternName = this.g.patternName(this.getKGatePatternCall(constraint).getPatternRef());
    _builder.append(_patternName);
    _builder.append("(");
    CharSequence _patternCallParameters = this.g.patternCallParameters(this.getKGatePatternCall(constraint));
    _builder.append(_patternCallParameters);
    _builder.append(",#_);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//Cast: to avoid a false error indication in eclipse");
    _builder.newLine();
    CharSequence _value_1 = this.g.value(this.getLeftHandSide(constraint));
    _builder.append(_value_1);
    _builder.append(" == eval(D.WeightCollection(collectionOf_");
    CharSequence _value_2 = this.g.value(this.getLeftHandSide(constraint));
    _builder.append(_value_2);
    _builder.append(" as MddHandleCollection, ");
    String _weightPredicateCall = this.weightPredicateCall(constraint);
    _builder.append(_weightPredicateCall);
    _builder.append("));");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  private String weightPredicateCall(final Constraint constraint) {
    EObject _eContainer = constraint.eContainer();
    final PatternBody body = ((PatternBody) _eContainer);
    EObject _eContainer_1 = body.eContainer();
    final Pattern pattern = ((Pattern) _eContainer_1);
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf(this.isWeightAnnotation(annotation, this.g.value(this.getLeftHandSide(constraint)).toString()));
    };
    final Annotation annotation = IterableExtensions.<Annotation>findFirst(pattern.getAnnotations(), _function);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[cnt | ");
    final Function1<AnnotationParameter, Boolean> _function_1 = (AnnotationParameter p) -> {
      return Boolean.valueOf("class".equals(p.getName()));
    };
    ValueReference _value = IterableExtensions.<AnnotationParameter>findFirst(annotation.getParameters(), _function_1).getValue();
    String _value_1 = ((StringValue) _value).getValue();
    _builder.append(_value_1);
    _builder.append(".");
    final Function1<AnnotationParameter, Boolean> _function_2 = (AnnotationParameter p) -> {
      return Boolean.valueOf("function".equals(p.getName()));
    };
    ValueReference _value_2 = IterableExtensions.<AnnotationParameter>findFirst(annotation.getParameters(), _function_2).getValue();
    String _value_3 = ((StringValue) _value_2).getValue();
    _builder.append(_value_3);
    _builder.append("(cnt)]");
    return _builder.toString();
  }

  public boolean notQueryNeeded(final Constraint constraint) {
    return true;
  }

  /**
   * Generate body of stochastic query
   */
  private int eventid = 0;

  public void eventidReset() {
    this.eventid = 0;
  }

  public CharSequence generateStochasticBody(final PatternBody body) {
    StringConcatenation _builder = new StringConcatenation();
    this.eventidReset();
    _builder.newLineIfNotEmpty();
    {
      EList<Constraint> _constraints = body.getConstraints();
      for(final Constraint constraint : _constraints) {
        CharSequence _stochasticVersion = this.stochasticVersion(constraint);
        _builder.append(_stochasticVersion);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      int _stochasticConstraintCount = this.stochasticConstraintCount(body);
      boolean _greaterThan = (_stochasticConstraintCount > 1);
      if (_greaterThan) {
        _builder.append("event==eval(D.AND(");
        {
          int _stochasticConstraintCount_1 = this.stochasticConstraintCount(body);
          int _minus = (_stochasticConstraintCount_1 - 1);
          IntegerRange _upTo = new IntegerRange(0, _minus);
          boolean _hasElements = false;
          for(final Integer i : _upTo) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append("event");
            _builder.append(i);
          }
        }
        _builder.append("));");
        _builder.newLineIfNotEmpty();
      } else {
        {
          int _stochasticConstraintCount_2 = this.stochasticConstraintCount(body);
          boolean _lessThan = (_stochasticConstraintCount_2 < 1);
          if (_lessThan) {
            _builder.append("TraceModel.mddTrue(_, event);");
            _builder.newLine();
          } else {
            _builder.append("event==event0;");
            _builder.newLine();
          }
        }
      }
    }
    return _builder;
  }

  public CharSequence stochasticPatternCall(final PatternCall call, final int id) {
    CharSequence _xblockexpression = null;
    {
      EObject _eContainer = call.eContainer();
      final PatternCompositionConstraint constraint = ((PatternCompositionConstraint) _eContainer);
      CharSequence _xifexpression = null;
      boolean _isNegative = constraint.isNegative();
      if (_isNegative) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[unimplemented Constraint Transformation]");
        _xifexpression = _builder;
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("find ");
        CharSequence _patternName = this.g.patternName(call.getPatternRef());
        _builder_1.append(_patternName);
        _builder_1.append("(");
        CharSequence _patternCallParameters = this.g.patternCallParameters(call);
        _builder_1.append(_patternCallParameters);
        _builder_1.append(",event");
        _builder_1.append(id);
        _builder_1.append(");");
        _xifexpression = _builder_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public CharSequence createRequire(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pattern unaryBERequiredName(arg1:EObject, name: java String, index: java Integer, probability: java Double)");
    _builder.newLine();
    {
      final Function1<Pattern, Boolean> _function = (Pattern pattern) -> {
        return Boolean.valueOf(this.isBasicEventDefinition(pattern));
      };
      Iterable<Pattern> _filter = IterableExtensions.<Pattern>filter(model.getPatterns(), _function);
      boolean _hasElements = false;
      for(final Pattern required : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(" or ", "");
        }
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("find ");
        CharSequence _patternName = this.g.patternName(required);
        _builder.append(_patternName, "\t");
        _builder.append("(");
        {
          int _length = ((Object[])Conversions.unwrapArray(required.getParameters(), Object.class)).length;
          IntegerRange _upTo = new IntegerRange(1, _length);
          boolean _hasElements_1 = false;
          for(final Integer i : _upTo) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(",", "\t");
            }
            _builder.append("arg");
            _builder.append(i, "\t");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("name == \"");
        CharSequence _patternName_1 = this.g.patternName(required);
        _builder.append(_patternName_1, "\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("probability == ");
        double _probability = this.getProbability(required);
        _builder.append(_probability, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("index == 1;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }

  /**
   * KGate parameter list helper
   */
  public Integer countKGateConstraints(final Pattern pattern) {
    final Function2<Integer, PatternBody, Integer> _function = (Integer max, PatternBody body) -> {
      return Integer.valueOf(Math.max((max).intValue(), (this.countKGateConstraints(body)).intValue()));
    };
    return IterableExtensions.<PatternBody, Integer>fold(pattern.getBodies(), Integer.valueOf(0), _function);
  }

  public Integer countKGateConstraints(final PatternBody body) {
    final Function2<Integer, Constraint, Integer> _function = (Integer sum, Constraint constraint) -> {
      int _xifexpression = (int) 0;
      boolean _ksKGateConstraint = this.ksKGateConstraint(constraint);
      if (_ksKGateConstraint) {
        _xifexpression = 1;
      } else {
        _xifexpression = 0;
      }
      return Integer.valueOf(((sum).intValue() + _xifexpression));
    };
    return IterableExtensions.<Constraint, Integer>fold(body.getConstraints(), Integer.valueOf(0), _function);
  }

  /**
   * Constraint data access helpers
   */
  public PatternCall getKGatePatternCall(final Constraint constraint) {
    final CompareConstraint assertion = ((CompareConstraint) constraint);
    ValueReference _rightOperand = assertion.getRightOperand();
    final AggregatedValue aggregator = ((AggregatedValue) _rightOperand);
    CallableRelation _call = aggregator.getCall();
    return ((PatternCall) _call);
  }

  public ValueReference getLeftHandSide(final Constraint constraint) {
    final CompareConstraint assertion = ((CompareConstraint) constraint);
    return assertion.getLeftOperand();
  }

  /**
   * Annotation data access helpers
   */
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

  /**
   * Pattern classification methods
   */
  public Pair<HashSet<Pattern>, HashSet<Pattern>> getStochasticPatterns(final PatternModel model) {
    final HashSet<Pattern> patterns = new HashSet<Pattern>();
    final HashSet<Pattern> events = new HashSet<Pattern>();
    boolean changed = true;
    while (changed) {
      {
        changed = false;
        EList<Pattern> _patterns = model.getPatterns();
        for (final Pattern pattern : _patterns) {
          {
            if ((this.isBasicEventDefinition(pattern) && (!events.contains(pattern)))) {
              events.add(pattern);
              changed = true;
            }
            if (((this.refersFrom(pattern, patterns) && (!patterns.contains(pattern))) || (this.refersFrom(pattern, events) && (!patterns.contains(pattern))))) {
              patterns.add(pattern);
              changed = true;
            }
          }
        }
      }
    }
    return Pair.<HashSet<Pattern>, HashSet<Pattern>>of(patterns, events);
  }

  public boolean refersFrom(final Pattern pattern, final Set<Pattern> patterns) {
    boolean _xblockexpression = false;
    {
      EList<PatternBody> _bodies = pattern.getBodies();
      for (final PatternBody body : _bodies) {
        EList<Constraint> _constraints = body.getConstraints();
        for (final Constraint constraint : _constraints) {
          if ((constraint instanceof PatternCompositionConstraint)) {
            CallableRelation _call = ((PatternCompositionConstraint)constraint).getCall();
            boolean _contains = patterns.contains(((PatternCall) _call).getPatternRef());
            if (_contains) {
              return true;
            }
          }
        }
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }

  /**
   * Element identification methods
   */
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

  public boolean ksKGateConstraint(final Constraint constraint) {
    if ((constraint instanceof CompareConstraint)) {
      return this.isKGateConstraint(((CompareConstraint)constraint));
    }
    return false;
  }

  public boolean isKGateConstraint(final CompareConstraint constraint) {
    final ValueReference aggregator = constraint.getRightOperand();
    if ((aggregator instanceof AggregatedValue)) {
      return "KGate".equals(((AggregatedValue)aggregator).getAggregator().getSimpleName());
    }
    return false;
  }

  public boolean isKGateBody(final PatternBody body) {
    EList<Constraint> _constraints = body.getConstraints();
    for (final Constraint constraint : _constraints) {
      if ((constraint instanceof CompareConstraint)) {
        boolean _isKGateConstraint = this.isKGateConstraint(((CompareConstraint)constraint));
        if (_isKGateConstraint) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isDeterministic(final Constraint constraint) {
    if ((constraint instanceof PatternCompositionConstraint)) {
      if ((this.stochasticPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()) || 
        this.basicEventPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()))) {
        return false;
      }
    }
    return true;
  }

  public boolean isBasicEventDefinition(final Pattern pattern) {
    final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
      return Boolean.valueOf("BasicEvent".equals(annotation.getName()));
    };
    return IterableExtensions.<Annotation>exists(pattern.getAnnotations(), _function);
  }

  public int stochasticConstraintCount(final PatternBody body) {
    int count = 0;
    EList<Constraint> _constraints = body.getConstraints();
    for (final Constraint constraint : _constraints) {
      if ((constraint instanceof PatternCompositionConstraint)) {
        if ((this.stochasticPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()) || 
          this.basicEventPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()))) {
          count++;
        }
      }
    }
    return count;
  }

  public CharSequence stochasticVersion(final Constraint constraint) {
    if (constraint instanceof CompareConstraint) {
      return _stochasticVersion((CompareConstraint)constraint);
    } else if (constraint instanceof PatternCompositionConstraint) {
      return _stochasticVersion((PatternCompositionConstraint)constraint);
    } else if (constraint != null) {
      return _stochasticVersion(constraint);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(constraint).toString());
    }
  }
}
