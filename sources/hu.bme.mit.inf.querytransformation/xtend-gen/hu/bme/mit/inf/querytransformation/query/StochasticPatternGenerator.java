package hu.bme.mit.inf.querytransformation.query;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.ValueReference;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Variable;
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import tracemodel.TracemodelPackage;

/**
 * Supported features
 * @BasicEvent Defines a basic event with the specified probability. Only EXACTLY one parameter queries are supported.
 * @Weight Specifies the weight function for a KGate aggregator
 * @NoEvent Forces the query to deterministic domain. This overrides implied stochastic nature and the effect of the @BasicEvent annotation.
 */
@SuppressWarnings("all")
public class StochasticPatternGenerator extends TransformationUtilities {
  private enum ConstraintType {
    DEFAULT,

    KGATE,

    STAGGREGATE,

    CALL,

    NOT;
  }

  private final Set<Pattern> stochasticPatterns = CollectionLiterals.<Pattern>newHashSet();

  private final Set<Pattern> basicEventPatterns = CollectionLiterals.<Pattern>newHashSet();

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
        CharSequence _generateInterfaceQueries = this.generateInterfaceQueries(patternmodel);
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
          InputOutput.<String>println(("Source error:" + err));
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
        CharSequence _generateInterfaceQueries = this.generateInterfaceQueries(patternmodel);
        _builder.append(_generateInterfaceQueries);
        _builder.newLineIfNotEmpty();
        final String vqlcontent = _builder.toString();
        InputOutput.<String>println(vqlcontent);
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
    _builder.append("import java reliability.mdd.NOT;");
    _builder.newLine();
    _builder.append("import java reliability.intreface.D;");
    _builder.newLine();
    _builder.append("import java hu.bme.mit.delta.mdd.MddHandle; ");
    _builder.newLine();
    _builder.append("import java reliability.mdd.MddHandleCollection;");
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
                  boolean _isKGateConstraint = this.isKGateConstraint(constraint);
                  if (_isKGateConstraint) {
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
      boolean _xblockexpression = false;
      {
        final CompareConstraint assertion = ((CompareConstraint) constraint);
        _xblockexpression = this.isWeightAnnotation(annotation, this.g.value(assertion.getLeftOperand()).toString());
      }
      return Boolean.valueOf(_xblockexpression);
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

  public CharSequence generateInterfaceQueries(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Interface queries for event management");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    {
      int _requiredPatternMaxArgCount = this.requiredPatternMaxArgCount(model);
      IntegerRange _upTo = new IntegerRange(1, _requiredPatternMaxArgCount);
      for(final Integer argc : _upTo) {
        {
          int _countRequiredArg = this.countRequiredArg(model, (argc).intValue());
          boolean _greaterThan = (_countRequiredArg > 0);
          if (_greaterThan) {
            _builder.append("pattern BETrace");
            _builder.append(argc);
            _builder.append("(");
            {
              IntegerRange _upTo_1 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements = false;
              for(final Integer i : _upTo_1) {
                if (!_hasElements) {
                  _hasElements = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                _builder.append("arg");
                _builder.append(i);
                _builder.append(":EObject");
              }
            }
            _builder.append(", name: EString, index: EInt, trace: Trace");
            _builder.append(argc);
            _builder.append("){");
            _builder.newLineIfNotEmpty();
            {
              IntegerRange _upTo_2 = new IntegerRange(1, (argc).intValue());
              for(final Integer i_1 : _upTo_2) {
                _builder.append("\t");
                _builder.append("Trace");
                _builder.append(argc, "\t");
                _builder.append(".arg");
                _builder.append(i_1, "\t");
                _builder.append("(trace, arg");
                _builder.append(i_1, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("Trace.generator(trace, name);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Trace.index(trace, index);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("pattern Insertion");
            _builder.append(argc);
            _builder.append("(");
            {
              IntegerRange _upTo_3 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_1 = false;
              for(final Integer i_2 : _upTo_3) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                _builder.append("arg");
                _builder.append(i_2);
                _builder.append(":EObject");
              }
            }
            _builder.append(", name: java String, from: java Integer, to: java Integer, probability: java Double){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_4 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_2 = false;
              for(final Integer i_3 : _upTo_4) {
                if (!_hasElements_2) {
                  _hasElements_2 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_3, "\t");
              }
            }
            _builder.append(", name, to, probability);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("neg find BETrace");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_5 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_3 = false;
              for(final Integer i_4 : _upTo_5) {
                if (!_hasElements_3) {
                  _hasElements_3 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_4, "\t");
              }
            }
            _builder.append(", name, _, _);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("from == 0;");
            _builder.newLine();
            _builder.append("} or {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_6 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_4 = false;
              for(final Integer i_5 : _upTo_6) {
                if (!_hasElements_4) {
                  _hasElements_4 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_5, "\t");
              }
            }
            _builder.append(", name, to, probability);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find BETrace");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_7 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_5 = false;
              for(final Integer i_6 : _upTo_7) {
                if (!_hasElements_5) {
                  _hasElements_5 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_6, "\t");
              }
            }
            _builder.append(", name, _, _);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("from == max find BETrace");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_8 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_6 = false;
              for(final Integer i_7 : _upTo_8) {
                if (!_hasElements_6) {
                  _hasElements_6 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_7, "\t");
              }
            }
            _builder.append(", name, #_, _);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("check(to > from);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("pattern Removal");
            _builder.append(argc);
            _builder.append("(trace: Trace");
            _builder.append(argc);
            _builder.append("){");
            _builder.newLineIfNotEmpty();
            {
              IntegerRange _upTo_9 = new IntegerRange(1, (argc).intValue());
              for(final Integer i_8 : _upTo_9) {
                _builder.append("\t");
                _builder.append("Trace");
                _builder.append(argc, "\t");
                _builder.append(".arg");
                _builder.append(i_8, "\t");
                _builder.append("(trace, arg");
                _builder.append(i_8, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("Trace.generator(trace, name);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("neg find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_10 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_7 = false;
              for(final Integer i_9 : _upTo_10) {
                if (!_hasElements_7) {
                  _hasElements_7 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_9, "\t");
              }
            }
            _builder.append(", name, _, _);");
            _builder.newLineIfNotEmpty();
            _builder.append("} or {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_11 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_8 = false;
              for(final Integer i_10 : _upTo_11) {
                if (!_hasElements_8) {
                  _hasElements_8 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_10, "\t");
              }
            }
            _builder.append(", name, multiplicity, _);");
            _builder.newLineIfNotEmpty();
            {
              IntegerRange _upTo_12 = new IntegerRange(1, (argc).intValue());
              for(final Integer i_11 : _upTo_12) {
                _builder.append("\t");
                _builder.append("Trace");
                _builder.append(argc, "\t");
                _builder.append(".arg");
                _builder.append(i_11, "\t");
                _builder.append("(trace, arg");
                _builder.append(i_11, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
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
            _builder.append("pattern Update");
            _builder.append(argc);
            _builder.append("(trace: Trace");
            _builder.append(argc);
            _builder.append(", probability: java Double){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_13 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_9 = false;
              for(final Integer i_12 : _upTo_13) {
                if (!_hasElements_9) {
                  _hasElements_9 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_12, "\t");
              }
            }
            _builder.append(", name, multiplicity, probability);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find BETrace");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_14 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_10 = false;
              for(final Integer i_13 : _upTo_14) {
                if (!_hasElements_10) {
                  _hasElements_10 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_13, "\t");
              }
            }
            _builder.append(", name, index, trace);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Trace.probability(trace, old);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("old != probability;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("check(index <= multiplicity);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("pattern HandleOf");
            _builder.append(argc);
            _builder.append("(");
            {
              IntegerRange _upTo_15 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_11 = false;
              for(final Integer i_14 : _upTo_15) {
                if (!_hasElements_11) {
                  _hasElements_11 = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                _builder.append("arg");
                _builder.append(i_14);
                _builder.append(":EObject");
              }
            }
            _builder.append(", name: EString, event: Handle){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("find BETrace");
            _builder.append(argc, "\t");
            _builder.append("(");
            {
              IntegerRange _upTo_16 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_12 = false;
              for(final Integer i_15 : _upTo_16) {
                if (!_hasElements_12) {
                  _hasElements_12 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_15, "\t");
              }
            }
            _builder.append(", name, _, trace);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Trace.event(trace, event);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }

  public CharSequence generateUserQueries(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* User defined patterns");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    {
      EList<Pattern> _patterns = model.getPatterns();
      for(final Pattern pattern : _patterns) {
        String _createPattern = this.createPattern(pattern);
        _builder.append(_createPattern);
        _builder.newLineIfNotEmpty();
      }
    }
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

  public String createPattern(final Pattern pattern) {
    final boolean stochastic = this.stochasticPatterns.contains(pattern);
    final boolean noevent = this.isEventBlocked(pattern);
    final CharSequence mods = this.g.patternModifiers(pattern);
    final CharSequence name = this.g.patternName(pattern);
    final CharSequence dparams = this.g.patternParameters(pattern);
    final CharSequence dparamname = this.g.patternParameterNames(pattern);
    StringConcatenation _builder = new StringConcatenation();
    {
      if (stochastic) {
        _builder.append(mods);
        _builder.append(" pattern ");
        _builder.append(name);
        _builder.append("(");
        _builder.append(dparams);
        _builder.append(",event: java Event){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("find ua_");
        _builder.append(name, "\t");
        _builder.append("(");
        _builder.append(dparamname, "\t");
        _builder.append(",_);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("event == OR find ua_");
        _builder.append(name, "\t");
        _builder.append("(");
        _builder.append(dparamname, "\t");
        _builder.append(",#_);");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("private pattern ua_");
        _builder.append(name);
        _builder.append("(");
        _builder.append(dparams);
        _builder.append(",event: java Event)");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append(mods);
        _builder.append(" pattern ");
        _builder.append(name);
        _builder.append("(");
        _builder.append(dparams);
        _builder.append(")");
        _builder.newLineIfNotEmpty();
      }
    }
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
        String _createBody = this.createBody(body, stochastic, noevent);
        _builder.append(_createBody, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder.toString();
  }

  private int eventidx = 0;

  public String createBody(final PatternBody body, final boolean stochastic, final boolean noevent) {
    int _xifexpression = (int) 0;
    if (stochastic) {
      _xifexpression = this.stochasticConstraintCount(body);
    } else {
      _xifexpression = 0;
    }
    final int eventcount = _xifexpression;
    this.eventidx = 0;
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Constraint> _constraints = body.getConstraints();
      for(final Constraint constraint : _constraints) {
        CharSequence _createConstraint = this.createConstraint(constraint, (eventcount <= 1), noevent);
        _builder.append(_createConstraint);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (stochastic) {
        {
          if ((eventcount == 0)) {
            _builder.append("TraceModel.mddTrue(_, event);");
            _builder.newLine();
          } else {
            if ((eventcount >= 2)) {
              _builder.append("event==eval(D.AND(");
              {
                IntegerRange _upTo = new IntegerRange(0, (eventcount - 1));
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
            }
          }
        }
      }
    }
    return _builder.toString();
  }

  public CharSequence createConstraint(final Constraint constraint, final boolean singleevent, final boolean noevent) {
    final Pair<StochasticPatternGenerator.ConstraintType, Boolean> type = this.type(constraint);
    StochasticPatternGenerator.ConstraintType _key = type.getKey();
    if (_key != null) {
      switch (_key) {
        case DEFAULT:
          if ((constraint instanceof PatternCompositionConstraint)) {
            CallableRelation _call = ((PatternCompositionConstraint)constraint).getCall();
            final PatternCall pc = ((PatternCall) _call);
            boolean _contains = this.stochasticPatterns.contains(pc.getPatternRef());
            if (_contains) {
            }
          }
          return this.g.constraint(constraint);
        case KGATE:
          final CompareConstraint cc = ((CompareConstraint) constraint);
          ValueReference _rightOperand = cc.getRightOperand();
          final AggregatedValue av = ((AggregatedValue) _rightOperand);
          CallableRelation _call_1 = av.getCall();
          final PatternCall pc_1 = ((PatternCall) _call_1);
          final CharSequence variable = this.g.value(cc.getLeftOperand());
          final CharSequence name = this.g.patternName(pc_1.getPatternRef());
          final CharSequence params = this.g.patternCallParameters(pc_1);
          EObject _eContainer = constraint.eContainer();
          final PatternBody body = ((PatternBody) _eContainer);
          EObject _eContainer_1 = body.eContainer();
          final Pattern pattern = ((Pattern) _eContainer_1);
          final Function1<Annotation, Boolean> _function = (Annotation annotation) -> {
            return Boolean.valueOf(this.isWeightAnnotation(annotation, variable.toString()));
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
          final String predicate = _builder.toString();
          StringConcatenation _builder_1 = new StringConcatenation();
          {
            Boolean _value_4 = type.getValue();
            if ((_value_4).booleanValue()) {
              _builder_1.append("collectionOf_");
              _builder_1.append(variable);
              _builder_1.append(" == COLLECT find use_");
              _builder_1.append(name);
              _builder_1.append("(");
              _builder_1.append(params);
              _builder_1.append(",#_);");
              _builder_1.newLineIfNotEmpty();
            } else {
              _builder_1.append("collectionOf_");
              _builder_1.append(variable);
              _builder_1.append(" == COLLECT find ");
              _builder_1.append(name);
              _builder_1.append("(");
              _builder_1.append(params);
              _builder_1.append(",#_);");
              _builder_1.newLineIfNotEmpty();
            }
          }
          _builder_1.append("TraceModel.probabilities(_,internal_probabilities);");
          _builder_1.newLine();
          _builder_1.append("//Cast: to avoid a false error indication in eclipse");
          _builder_1.newLine();
          _builder_1.append(variable);
          _builder_1.append(" == eval(D.WeightCollection(collectionOf_");
          _builder_1.append(variable);
          _builder_1.append(" as MddHandleCollection, ");
          _builder_1.append(predicate);
          _builder_1.append(", internal_probabilities));");
          _builder_1.newLineIfNotEmpty();
          return _builder_1.toString();
        case CALL:
          final PatternCompositionConstraint pcc = ((PatternCompositionConstraint) constraint);
          CallableRelation _call_2 = pcc.getCall();
          final Pattern pattern_1 = ((PatternCall) _call_2).getPatternRef();
          final CharSequence name_1 = this.g.patternName(pattern_1);
          CallableRelation _call_3 = pcc.getCall();
          final CharSequence params_1 = this.g.patternCallParameters(((PatternCall) _call_3));
          StringConcatenation _builder_2 = new StringConcatenation();
          {
            Boolean _value_5 = type.getValue();
            if ((_value_5).booleanValue()) {
              _builder_2.append("find use_");
              _builder_2.append(name_1);
              _builder_2.append("(");
              _builder_2.append(params_1);
              _builder_2.append(",");
              {
                if (noevent) {
                  _builder_2.append("_");
                } else {
                  _builder_2.append("event");
                  {
                    if ((!singleevent)) {
                      int _plusPlus = this.eventidx++;
                      _builder_2.append(_plusPlus);
                    }
                  }
                }
              }
              _builder_2.append(");");
              _builder_2.newLineIfNotEmpty();
            } else {
              _builder_2.append("find ");
              _builder_2.append(name_1);
              _builder_2.append("(");
              _builder_2.append(params_1);
              _builder_2.append(",");
              {
                if (noevent) {
                  _builder_2.append("_");
                } else {
                  _builder_2.append("event");
                  {
                    if ((!singleevent)) {
                      int _plusPlus_1 = this.eventidx++;
                      _builder_2.append(_plusPlus_1);
                    }
                  }
                }
              }
              _builder_2.append(");");
              _builder_2.newLineIfNotEmpty();
            }
          }
          return _builder_2.toString();
        case NOT:
          final PatternCompositionConstraint pcc_1 = ((PatternCompositionConstraint) constraint);
          CallableRelation _call_4 = pcc_1.getCall();
          final Pattern pattern_2 = ((PatternCall) _call_4).getPatternRef();
          final CharSequence name_2 = this.g.patternName(pattern_2);
          CallableRelation _call_5 = pcc_1.getCall();
          final CharSequence params_2 = this.g.patternCallParameters(((PatternCall) _call_5));
          if (noevent) {
            StringConcatenation _builder_3 = new StringConcatenation();
            {
              Boolean _value_6 = type.getValue();
              if ((_value_6).booleanValue()) {
                _builder_3.append("//Stochastic constraint is ignored (always match): ");
                _builder_3.newLine();
                _builder_3.append("//event_idx == NOT find use_");
                _builder_3.append(name_2);
                _builder_3.append("(");
                _builder_3.append(params_2);
                _builder_3.append(",#_);");
                _builder_3.newLineIfNotEmpty();
              } else {
                _builder_3.append("//Stochastic constraint is ignored (always match):");
                _builder_3.newLine();
                _builder_3.append("//event_idx == NOT find ");
                _builder_3.append(name_2);
                _builder_3.append("(");
                _builder_3.append(params_2);
                _builder_3.append(",#_);");
                _builder_3.newLineIfNotEmpty();
              }
            }
            return _builder_3.toString();
          }
          StringConcatenation _builder_4 = new StringConcatenation();
          {
            Boolean _value_7 = type.getValue();
            if ((_value_7).booleanValue()) {
              _builder_4.append("event");
              {
                if ((!singleevent)) {
                  int _plusPlus_2 = this.eventidx++;
                  _builder_4.append(_plusPlus_2);
                }
              }
              _builder_4.append(" == NOT find use_");
              _builder_4.append(name_2);
              _builder_4.append("(");
              _builder_4.append(params_2);
              _builder_4.append(",#_);");
              _builder_4.newLineIfNotEmpty();
            } else {
              _builder_4.append("event");
              {
                if ((!singleevent)) {
                  int _plusPlus_3 = this.eventidx++;
                  _builder_4.append(_plusPlus_3);
                }
              }
              _builder_4.append(" == NOT find ");
              _builder_4.append(name_2);
              _builder_4.append("(");
              _builder_4.append(params_2);
              _builder_4.append(",#_);");
              _builder_4.newLineIfNotEmpty();
            }
          }
          return _builder_4.toString();
        case STAGGREGATE:
          final CompareConstraint cc_1 = ((CompareConstraint) constraint);
          ValueReference _rightOperand_1 = cc_1.getRightOperand();
          final AggregatedValue av_1 = ((AggregatedValue) _rightOperand_1);
          final String aggregator = av_1.getAggregator().getSimpleName();
          CallableRelation _call_6 = av_1.getCall();
          final PatternCall pc_2 = ((PatternCall) _call_6);
          final CharSequence name_3 = this.g.patternName(pc_2.getPatternRef());
          final CharSequence params_3 = this.g.patternCallParameters(pc_2);
          StringConcatenation _builder_5 = new StringConcatenation();
          {
            Boolean _value_8 = type.getValue();
            if ((_value_8).booleanValue()) {
              CharSequence _value_9 = this.g.value(cc_1.getLeftOperand());
              _builder_5.append(_value_9);
              _builder_5.append("==");
              _builder_5.append(aggregator);
              _builder_5.append(" find ");
              _builder_5.append(name_3);
              _builder_5.append("(");
              _builder_5.append(params_3);
              _builder_5.append(");");
              _builder_5.newLineIfNotEmpty();
            } else {
              CharSequence _value_10 = this.g.value(cc_1.getLeftOperand());
              _builder_5.append(_value_10);
              _builder_5.append("==");
              _builder_5.append(aggregator);
              _builder_5.append(" find ");
              _builder_5.append(name_3);
              _builder_5.append("(");
              _builder_5.append(params_3);
              _builder_5.append(",_);");
              _builder_5.newLineIfNotEmpty();
            }
          }
          return _builder_5.toString();
        default:
          break;
      }
    }
    return null;
  }

  public CharSequence createRequire(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      int _requiredPatternMaxArgCount = this.requiredPatternMaxArgCount(model);
      IntegerRange _upTo = new IntegerRange(1, _requiredPatternMaxArgCount);
      for(final Integer argc : _upTo) {
        {
          int _countRequiredArg = this.countRequiredArg(model, (argc).intValue());
          boolean _greaterThan = (_countRequiredArg > 0);
          if (_greaterThan) {
            _builder.append("pattern BERequiredName");
            _builder.append(argc);
            _builder.append("(");
            {
              IntegerRange _upTo_1 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements = false;
              for(final Integer i : _upTo_1) {
                if (!_hasElements) {
                  _hasElements = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                _builder.append("arg");
                _builder.append(i);
                _builder.append(":EObject");
              }
            }
            _builder.append(",name: java String, index: java Integer, probability: java Double)");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("_many(");
            {
              IntegerRange _upTo_2 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_1 = false;
              for(final Integer i_1 : _upTo_2) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_1, "\t");
              }
            }
            _builder.append(",name, index, _);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("probability == max find BERequiredName");
            _builder.append(argc, "\t");
            _builder.append("_many(");
            {
              IntegerRange _upTo_3 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_2 = false;
              for(final Integer i_2 : _upTo_3) {
                if (!_hasElements_2) {
                  _hasElements_2 = true;
                } else {
                  _builder.appendImmediate(", ", "\t");
                }
                _builder.append("arg");
                _builder.append(i_2, "\t");
              }
            }
            _builder.append(",name, index, #_);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("pattern BERequiredName");
            _builder.append(argc);
            _builder.append("_many(");
            {
              IntegerRange _upTo_4 = new IntegerRange(1, (argc).intValue());
              boolean _hasElements_3 = false;
              for(final Integer i_3 : _upTo_4) {
                if (!_hasElements_3) {
                  _hasElements_3 = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                _builder.append("arg");
                _builder.append(i_3);
                _builder.append(":EObject");
              }
            }
            _builder.append(",name: java String, index: java Integer, probability: java Double)");
            _builder.newLineIfNotEmpty();
            {
              Iterable<Pattern> _filterRequiredArg = this.filterRequiredArg(model, (argc).intValue());
              boolean _hasElements_4 = false;
              for(final Pattern required : _filterRequiredArg) {
                if (!_hasElements_4) {
                  _hasElements_4 = true;
                } else {
                  _builder.appendImmediate(" or ", "");
                }
                _builder.append("{");
                _builder.newLine();
                {
                  TransformationUtilities.PSource _probabilitySourceType = this.getProbabilitySourceType(required);
                  boolean _tripleEquals = (_probabilitySourceType == TransformationUtilities.PSource.ANNOTATION);
                  if (_tripleEquals) {
                    _builder.append("\t");
                    _builder.append("find ");
                    CharSequence _patternName = this.g.patternName(required);
                    _builder.append(_patternName, "\t");
                    _builder.append("(");
                    {
                      int _length = ((Object[])Conversions.unwrapArray(required.getParameters(), Object.class)).length;
                      IntegerRange _upTo_5 = new IntegerRange(1, _length);
                      boolean _hasElements_5 = false;
                      for(final Integer i_4 : _upTo_5) {
                        if (!_hasElements_5) {
                          _hasElements_5 = true;
                        } else {
                          _builder.appendImmediate(",", "\t");
                        }
                        _builder.append("arg");
                        _builder.append(i_4, "\t");
                      }
                    }
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("probability == ");
                    double _probability = this.getProbability(required);
                    _builder.append(_probability, "\t");
                    _builder.append(";");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("find ");
                    CharSequence _patternName_1 = this.g.patternName(required);
                    _builder.append(_patternName_1, "\t");
                    _builder.append("(");
                    String _patternParemeterOverwrite = this.patternParemeterOverwrite(required, this.getProbabilityVariable(required), "probability");
                    _builder.append(_patternParemeterOverwrite, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("\t");
                _builder.append("name == \"");
                CharSequence _patternName_2 = this.g.patternName(required);
                _builder.append(_patternName_2, "\t");
                _builder.append("\";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("index == 1;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    {
      final Function1<Pattern, Boolean> _function = (Pattern pattern) -> {
        return Boolean.valueOf(this.isBasicEventDefinition(pattern));
      };
      Iterable<Pattern> _filter = IterableExtensions.<Pattern>filter(model.getPatterns(), _function);
      for(final Pattern required_1 : _filter) {
        _builder.append("private pattern use_");
        CharSequence _patternName_3 = this.g.patternName(required_1);
        _builder.append(_patternName_3);
        _builder.append("(");
        CharSequence _patternParameters = this.g.patternParameters(required_1);
        _builder.append(_patternParameters);
        _builder.append(", event: java Event){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//find ");
        CharSequence _patternName_4 = this.g.patternName(required_1);
        _builder.append(_patternName_4, "\t");
        _builder.append("(");
        CharSequence _patternParameterNames = this.g.patternParameterNames(required_1);
        _builder.append(_patternParameterNames, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("find HandleOf");
        int _reducedParameterCount = this.reducedParameterCount(required_1);
        _builder.append(_reducedParameterCount, "\t");
        _builder.append("(");
        String _patternReducedParameterNames = this.patternReducedParameterNames(required_1);
        _builder.append(_patternReducedParameterNames, "\t");
        _builder.append(",\"");
        CharSequence _patternName_5 = this.g.patternName(required_1);
        _builder.append(_patternName_5, "\t");
        _builder.append("\",event);");
        _builder.newLineIfNotEmpty();
        {
          TransformationUtilities.PSource _probabilitySourceType_1 = this.getProbabilitySourceType(required_1);
          boolean _tripleEquals_1 = (_probabilitySourceType_1 == TransformationUtilities.PSource.PARAMETER);
          if (_tripleEquals_1) {
            _builder.append("\t");
            _builder.append("find BETrace");
            int _reducedParameterCount_1 = this.reducedParameterCount(required_1);
            _builder.append(_reducedParameterCount_1, "\t");
            _builder.append("(");
            String _patternReducedParameterNames_1 = this.patternReducedParameterNames(required_1);
            _builder.append(_patternReducedParameterNames_1, "\t");
            _builder.append(",\"");
            CharSequence _patternName_6 = this.g.patternName(required_1);
            _builder.append(_patternName_6, "\t");
            _builder.append("\",1,trace);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Trace.probability(trace,");
            String _name = this.getProbabilityVariable(required_1).getVariable().getName();
            _builder.append(_name, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t ");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }

  public String patternParemeterOverwrite(final Pattern call, final VariableReference parameter, final String replacement) {
    int idx = 1;
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Variable> _parameters = call.getParameters();
      boolean _hasElements = false;
      for(final Variable param : _parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        {
          boolean _equals = param.equals(parameter.getVariable());
          if (_equals) {
            _builder.append(replacement);
          } else {
            _builder.append("arg");
            int _plusPlus = idx++;
            _builder.append(_plusPlus);
          }
        }
      }
    }
    return _builder.toString();
  }

  public String patternReducedParameterNames(final Pattern pattern) {
    Variable _xifexpression = null;
    TransformationUtilities.PSource _probabilitySourceType = this.getProbabilitySourceType(pattern);
    boolean _tripleEquals = (_probabilitySourceType == TransformationUtilities.PSource.PARAMETER);
    if (_tripleEquals) {
      _xifexpression = this.getProbabilityVariable(pattern).getVariable();
    } else {
      _xifexpression = null;
    }
    final Variable parameter = _xifexpression;
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<Variable, Boolean> _function = (Variable p) -> {
        boolean _equals = p.equals(parameter);
        return Boolean.valueOf((!_equals));
      };
      Iterable<Variable> _filter = IterableExtensions.<Variable>filter(pattern.getParameters(), _function);
      boolean _hasElements = false;
      for(final Variable param : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = param.getName();
        _builder.append(_name);
      }
    }
    return _builder.toString();
  }

  public Pair<StochasticPatternGenerator.ConstraintType, Boolean> type(final Constraint constraint) {
    boolean _isDeterministic = this.isDeterministic(constraint);
    if (_isDeterministic) {
      boolean _isKGateConstraint = this.isKGateConstraint(constraint);
      if (_isKGateConstraint) {
        final CompareConstraint pcc = ((CompareConstraint) constraint);
        ValueReference _rightOperand = pcc.getRightOperand();
        final AggregatedValue av = ((AggregatedValue) _rightOperand);
        CallableRelation _call = av.getCall();
        final Pattern pattern = ((PatternCall) _call).getPatternRef();
        boolean _contains = this.basicEventPatterns.contains(pattern);
        return Pair.<StochasticPatternGenerator.ConstraintType, Boolean>of(StochasticPatternGenerator.ConstraintType.KGATE, Boolean.valueOf(_contains));
      } else {
        if ((constraint instanceof CompareConstraint)) {
          final ValueReference rho = ((CompareConstraint)constraint).getRightOperand();
          if ((rho instanceof AggregatedValue)) {
            CallableRelation _call_1 = ((AggregatedValue)rho).getCall();
            final Pattern pattern_1 = ((PatternCall) _call_1).getPatternRef();
            boolean _contains_1 = this.stochasticPatterns.contains(pattern_1);
            if (_contains_1) {
              boolean _contains_2 = this.basicEventPatterns.contains(pattern_1);
              return Pair.<StochasticPatternGenerator.ConstraintType, Boolean>of(StochasticPatternGenerator.ConstraintType.STAGGREGATE, Boolean.valueOf(_contains_2));
            }
          }
        }
        return Pair.<StochasticPatternGenerator.ConstraintType, Boolean>of(StochasticPatternGenerator.ConstraintType.DEFAULT, Boolean.valueOf(false));
      }
    } else {
      final PatternCompositionConstraint pcc_1 = ((PatternCompositionConstraint) constraint);
      CallableRelation _call_2 = pcc_1.getCall();
      final Pattern pattern_2 = ((PatternCall) _call_2).getPatternRef();
      boolean _isNegative = pcc_1.isNegative();
      if (_isNegative) {
        boolean _contains_3 = this.basicEventPatterns.contains(pattern_2);
        return Pair.<StochasticPatternGenerator.ConstraintType, Boolean>of(StochasticPatternGenerator.ConstraintType.NOT, Boolean.valueOf(_contains_3));
      } else {
        boolean _contains_4 = this.basicEventPatterns.contains(pattern_2);
        return Pair.<StochasticPatternGenerator.ConstraintType, Boolean>of(StochasticPatternGenerator.ConstraintType.CALL, Boolean.valueOf(_contains_4));
      }
    }
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
          boolean _isEventBlocked = this.isEventBlocked(pattern);
          boolean _not = (!_isEventBlocked);
          if (_not) {
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

  public boolean isDeterministic(final Constraint constraint) {
    if ((constraint instanceof PatternCompositionConstraint)) {
      if ((this.stochasticPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()) || 
        this.basicEventPatterns.contains(((PatternCall) ((PatternCompositionConstraint)constraint).getCall()).getPatternRef()))) {
        return false;
      }
    }
    return true;
  }
}
