package hu.bme.mit.inf.querytransformation.query;

import java.util.Arrays;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParsingResults;
import org.eclipse.viatra.query.patternlanguage.emf.vql.AggregatedValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CallableRelation;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CheckConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.ClassType;
import org.eclipse.viatra.query.patternlanguage.emf.vql.CompareConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Constraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.EClassifierConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.EntityType;
import org.eclipse.viatra.query.patternlanguage.emf.vql.FunctionEvaluationValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.JavaType;
import org.eclipse.viatra.query.patternlanguage.emf.vql.NumberValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PackageImport;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PathExpressionConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Pattern;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternBody;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCall;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternCompositionConstraint;
import org.eclipse.viatra.query.patternlanguage.emf.vql.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.vql.StringValue;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Type;
import org.eclipse.viatra.query.patternlanguage.emf.vql.ValueReference;
import org.eclipse.viatra.query.patternlanguage.emf.vql.Variable;
import org.eclipse.viatra.query.patternlanguage.emf.vql.VariableReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XCastedExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XNumberLiteral;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xtype.XImportDeclaration;
import tracemodel.TracemodelPackage;

@SuppressWarnings("all")
public class VQLParser {
  public void test() {
    EPackage.Registry.INSTANCE.put(TracemodelPackage.eNS_URI, TracemodelPackage.eINSTANCE);
    EMFPatternLanguageStandaloneSetup.doSetup();
    final PatternParsingResults parsed = PatternParserBuilder.instance().parse(this.query().toString());
    if ((parsed.hasError() || IterableExtensions.isEmpty(parsed.getPatterns()))) {
      InputOutput.<String>println("Input contains errors or empty.");
      final Consumer<Issue> _function = (Issue err) -> {
        InputOutput.<Issue>println(err);
      };
      parsed.getErrors().forEach(_function);
    } else {
      EObject _eContainer = (((Pattern[])Conversions.unwrapArray(parsed.getPatterns(), Pattern.class))[0]).eContainer();
      final PatternModel patternmodel = ((PatternModel) _eContainer);
      InputOutput.<CharSequence>println(this.generateImports(patternmodel));
      Iterable<Pattern> _patterns = parsed.getPatterns();
      for (final Pattern pattern : _patterns) {
        InputOutput.<CharSequence>println(this.generateQuery(pattern));
      }
    }
  }

  public CharSequence generateImports(final PatternModel model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<PackageImport> _packageImport = model.getImportPackages().getPackageImport();
      for(final PackageImport packageImport : _packageImport) {
        _builder.append("import \"");
        String _nsURI = packageImport.getEPackage().getNsURI();
        _builder.append(_nsURI);
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      EList<XImportDeclaration> _importDeclarations = model.getImportPackages().getImportDeclarations();
      for(final XImportDeclaration javaImport : _importDeclarations) {
        _builder.append("import java ");
        String _packageName = javaImport.getImportedType().getPackageName();
        _builder.append(_packageName);
        _builder.append(".");
        String _simpleName = javaImport.getImportedType().getSimpleName();
        _builder.append(_simpleName);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  /**
   * Pattern definition generation
   */
  public CharSequence generateQuery(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _patternModifiers = this.patternModifiers(pattern);
    _builder.append(_patternModifiers);
    _builder.append(" pattern ");
    CharSequence _patternName = this.patternName(pattern);
    _builder.append(_patternName);
    _builder.append("(");
    CharSequence _patternParameters = this.patternParameters(pattern);
    _builder.append(_patternParameters);
    _builder.append(")");
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
        CharSequence _patternBody = this.patternBody(body);
        _builder.append(_patternBody, "\t");
        _builder.append("\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }

  public CharSequence patternModifiers(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isPrivate = pattern.getModifiers().isPrivate();
      if (_isPrivate) {
        _builder.append("private ");
      }
    }
    return _builder;
  }

  public CharSequence patternName(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = pattern.getName();
    _builder.append(_name);
    return _builder;
  }

  public CharSequence patternParameters(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Variable> _parameters = pattern.getParameters();
      boolean _hasElements = false;
      for(final Variable param : _parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = param.getName();
        _builder.append(_name);
        _builder.append(":");
        Type _type = param.getType();
        CharSequence _paramtype = this.paramtype(((EntityType) _type));
        _builder.append(_paramtype);
      }
    }
    return _builder;
  }

  public CharSequence patternParameterNames(final Pattern pattern) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Variable> _parameters = pattern.getParameters();
      boolean _hasElements = false;
      for(final Variable param : _parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = param.getName();
        _builder.append(_name);
      }
    }
    return _builder;
  }

  public CharSequence patternBody(final PatternBody body) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Constraint> _constraints = body.getConstraints();
      for(final Constraint constraint : _constraints) {
        CharSequence _constraint = this.constraint(constraint);
        _builder.append(_constraint);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  /**
   * Parameter types
   * (patternParameters uses it)
   */
  protected CharSequence _paramtype(final ClassType param) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = param.getClassname().getName();
    _builder.append(_name);
    return _builder;
  }

  protected CharSequence _paramtype(final JavaType param) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("java ");
    {
      boolean _startsWith = param.getClassRef().getPackageName().startsWith("java.");
      if (_startsWith) {
        _builder.append("^");
      }
    }
    String _packageName = param.getClassRef().getPackageName();
    _builder.append(_packageName);
    _builder.append(".");
    String _simpleName = param.getClassRef().getSimpleName();
    _builder.append(_simpleName);
    return _builder;
  }

  /**
   * Pattern Call
   */
  public CharSequence patternCall(final PatternCall call) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("find ");
    CharSequence _patternName = this.patternName(call.getPatternRef());
    _builder.append(_patternName);
    _builder.append("(");
    CharSequence _patternCallParameters = this.patternCallParameters(call);
    _builder.append(_patternCallParameters);
    _builder.append(")");
    return _builder;
  }

  public CharSequence patternCallParameters(final PatternCall call) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ValueReference> _parameters = call.getParameters();
      boolean _hasElements = false;
      for(final ValueReference param : _parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        Object _value = this.value(param);
        _builder.append(_value);
      }
    }
    return _builder;
  }

  /**
   * Constraint generation
   */
  public CharSequence defaultConstraint(final Constraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[unresolved Constraint]");
    return _builder;
  }

  protected CharSequence _constraint(final Constraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defaultConstraint = this.defaultConstraint(constraint);
    _builder.append(_defaultConstraint);
    return _builder;
  }

  protected CharSequence _constraint(final PathExpressionConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = constraint.getSourceType().getClassname().getName();
    _builder.append(_name);
    _builder.append(".");
    String _name_1 = constraint.getEdgeTypes().get(0).getRefname().getName();
    _builder.append(_name_1);
    _builder.append("(");
    CharSequence _value = this.value(constraint.getSrc());
    _builder.append(_value);
    _builder.append(",");
    CharSequence _value_1 = this.value(constraint.getDst());
    _builder.append(_value_1);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  protected CharSequence _constraint(final PatternCompositionConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isNegative = constraint.isNegative();
      if (_isNegative) {
        _builder.append("neg ");
      }
    }
    CallableRelation _call = constraint.getCall();
    CharSequence _patternCall = this.patternCall(((PatternCall) _call));
    _builder.append(_patternCall);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  protected CharSequence _constraint(final CompareConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _value = this.value(constraint.getLeftOperand());
    _builder.append(_value);
    String _literal = constraint.getFeature().getLiteral();
    _builder.append(_literal);
    CharSequence _value_1 = this.value(constraint.getRightOperand());
    _builder.append(_value_1);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  protected CharSequence _constraint(final EClassifierConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    EntityType _type = constraint.getType();
    String _name = ((ClassType) _type).getClassname().getName();
    _builder.append(_name);
    _builder.append("(");
    String _var = constraint.getVar().getVar();
    _builder.append(_var);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  protected CharSequence _constraint(final CheckConstraint constraint) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("check(");
    CharSequence _resolve = this.resolve(constraint.getExpression());
    _builder.append(_resolve);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  /**
   * Value generation
   */
  protected CharSequence _value(final VariableReference reference) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isAggregator = reference.isAggregator();
      if (_isAggregator) {
        _builder.append("#");
      }
    }
    String _var = reference.getVar();
    _builder.append(_var);
    return _builder;
  }

  protected CharSequence _value(final AggregatedValue reference) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = reference.getAggregator().getSimpleName();
    _builder.append(_simpleName);
    _builder.append(" ");
    CallableRelation _call = reference.getCall();
    CharSequence _patternCall = this.patternCall(((PatternCall) _call));
    _builder.append(_patternCall);
    return _builder;
  }

  protected CharSequence _value(final FunctionEvaluationValue reference) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("eval(");
    CharSequence _resolve = this.resolve(reference.getExpression());
    _builder.append(_resolve);
    _builder.append(")");
    return _builder;
  }

  protected CharSequence _value(final ValueReference reference) {
    StringConcatenation _builder = new StringConcatenation();
    String _string = reference.toString();
    _builder.append(_string);
    return _builder;
  }

  protected CharSequence _value(final StringValue reference) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _value = reference.getValue();
    _builder.append(_value);
    _builder.append("\"");
    return _builder;
  }

  protected CharSequence _value(final NumberValue reference) {
    StringConcatenation _builder = new StringConcatenation();
    String _value = reference.getValue().getValue();
    _builder.append(_value);
    return _builder;
  }

  /**
   * Expression generation
   * (body of check and eval constraints)
   */
  public CharSequence defaultResolve(final XExpression expression) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[unresolved XExpression]");
    return _builder;
  }

  public CharSequence defaultResolve(final XMemberFeatureCall expression) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[unresolved XExpression]");
    return _builder;
  }

  protected CharSequence _resolve(final XExpression expression) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defaultResolve = this.defaultResolve(expression);
    _builder.append(_defaultResolve);
    return _builder;
  }

  protected CharSequence _resolve(final XBinaryOperation expression) {
    StringConcatenation _builder = new StringConcatenation();
    Object _resolve = this.resolve(expression.getLeftOperand());
    _builder.append(_resolve);
    _builder.append(" ");
    CharSequence _jvmresolve = this.jvmresolve(expression.getFeature());
    _builder.append(_jvmresolve);
    _builder.append(" ");
    Object _resolve_1 = this.resolve(expression.getRightOperand());
    _builder.append(_resolve_1);
    return _builder;
  }

  protected CharSequence _resolve(final XFeatureCall expresstion) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _jvmresolve = this.jvmresolve(expresstion.getFeature());
    _builder.append(_jvmresolve);
    return _builder;
  }

  protected CharSequence _resolve(final XNumberLiteral expression) {
    StringConcatenation _builder = new StringConcatenation();
    String _value = expression.getValue();
    _builder.append(_value);
    return _builder;
  }

  protected CharSequence _resolve(final XCastedExpression expression) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    Object _resolve = this.resolve(expression.getTarget());
    _builder.append(_resolve);
    _builder.append(" as ");
    String _replaceAll = expression.getType().getType().getIdentifier().replaceAll("^java", "^java");
    _builder.append(_replaceAll);
    _builder.append(")");
    return _builder;
  }

  protected CharSequence _resolve(final XMemberFeatureCall expression) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isExplicitOperationCall = expression.isExplicitOperationCall();
      if (_isExplicitOperationCall) {
        CharSequence _operationCall = this.operationCall(expression);
        _builder.append(_operationCall);
        _builder.newLineIfNotEmpty();
      } else {
        boolean _isTypeLiteral = expression.isTypeLiteral();
        if (_isTypeLiteral) {
          String _identifier = expression.getFeature().getIdentifier();
          _builder.append(_identifier);
          _builder.newLineIfNotEmpty();
        } else {
          CharSequence _defaultResolve = this.defaultResolve(expression);
          _builder.append(_defaultResolve);
        }
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }

  public CharSequence defaultOperationCall(final XMemberFeatureCall expression) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[unresolved OperationCall]");
    return _builder;
  }

  public CharSequence operationCall(final XMemberFeatureCall expression) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isStatic = expression.isStatic();
      if (_isStatic) {
        Object _resolve = this.resolve(expression.getMemberCallTarget());
        _builder.append(_resolve);
        _builder.append(".");
        CharSequence _jvmresolve = this.jvmresolve(expression.getFeature());
        _builder.append(_jvmresolve);
        _builder.append("(");
        {
          EList<XExpression> _memberCallArguments = expression.getMemberCallArguments();
          boolean _hasElements = false;
          for(final XExpression arg : _memberCallArguments) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            Object _resolve_1 = this.resolve(arg);
            _builder.append(_resolve_1);
          }
        }
        _builder.append(")");
        _builder.newLineIfNotEmpty();
      } else {
        CharSequence _defaultOperationCall = this.defaultOperationCall(expression);
        _builder.append(_defaultOperationCall);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  /**
   * JVM resolve
   */
  public CharSequence defaultJVMResolve(final JvmIdentifiableElement element) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[unresolved JVMResolve]");
    return _builder;
  }

  protected CharSequence _jvmresolve(final JvmIdentifiableElement expression) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defaultJVMResolve = this.defaultJVMResolve(expression);
    _builder.append(_defaultJVMResolve);
    return _builder;
  }

  protected CharSequence _jvmresolve(final JvmFormalParameter expression) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = expression.getName();
    _builder.append(_name);
    return _builder;
  }

  protected CharSequence _jvmresolve(final JvmOperation expression) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _equals = "operator_lessEqualsThan".equals(expression.getSimpleName());
      if (_equals) {
        _builder.append("<=");
        _builder.newLineIfNotEmpty();
      } else {
        boolean _equals_1 = "operator_lessThan".equals(expression.getSimpleName());
        if (_equals_1) {
          _builder.append("<");
          _builder.newLineIfNotEmpty();
        } else {
          boolean _equals_2 = "operator_greaterThan".equals(expression.getSimpleName());
          if (_equals_2) {
            _builder.append(">");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _equals_3 = "operator_greaterEqualsThan".equals(expression.getSimpleName());
            if (_equals_3) {
              _builder.append(">=");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _equals_4 = "operator_multiply".equals(expression.getSimpleName());
              if (_equals_4) {
                _builder.append("*");
                _builder.newLineIfNotEmpty();
              } else {
                boolean _equals_5 = "operator_plus".equals(expression.getSimpleName());
                if (_equals_5) {
                  _builder.append("+");
                  _builder.newLineIfNotEmpty();
                } else {
                  boolean _equals_6 = "operator_minus".equals(expression.getSimpleName());
                  if (_equals_6) {
                    _builder.append("-");
                    _builder.newLineIfNotEmpty();
                  } else {
                    String _simpleName = expression.getSimpleName();
                    _builder.append(_simpleName);
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

  protected CharSequence _jvmresolve(final JvmGenericType expression) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = expression.getSimpleName();
    _builder.append(_simpleName);
    return _builder;
  }

  public CharSequence query() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java hu.bme.mit.delta.mdd.MddHandle; ");
    _builder.newLine();
    _builder.append("import \"http://www.example.org/satellite1\"");
    _builder.newLine();
    _builder.append("import \"http://www.eclipse.org/emf/2002/Ecore\"");
    _builder.newLine();
    _builder.append("import \"http://www.example.org/tracemodel\"");
    _builder.newLine();
    _builder.append("//import \"http://www.eclipse.org/emf/2002/Ecore\"");
    _builder.newLine();
    _builder.append("import java hu.bme.mit.delta.mdd.MddHandle; ");
    _builder.newLine();
    _builder.append("import java reliability.intreface.D;");
    _builder.newLine();
    _builder.append("import java hu.bme.mit.delta.mdd.MddHandle; ");
    _builder.newLine();
    _builder.append("import java reliability.mdd.OR;");
    _builder.newLine();
    _builder.append("import java reliability.mdd.COLLECT;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern canTransmit(css: CommSubsystem, handle: java MddHandle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find link(css, _, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("handle == OR find link(css, _, #_);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern unaryUpdate(trace: UnaryTrace, probability: java Double){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBERequiredName(element, name, multiplicity, probability);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBETrace(element, name, index, trace);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.probability(trace, old);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("old != probability;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("check(multiplicity <= index);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("pattern sat_online(sat: Spacecraft, handle: java MddHandle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Spacecraft.commSubsystem(sat, css);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find canTransmit(css, lnk_handle);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryHandleOf(css, \"component\", _, css_handle);//Sat can only have one transmitting css thus no aggregation is needed");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryHandleOf(sat, \"component\", _, sat_handle);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("handle == eval(D.AND(sat_handle, css_handle, lnk_handle));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern coverage(coverage: java Double){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("InterferometryMission(mission);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("collection == COLLECT find ip_sat(_, #_);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("coverage == eval(domain.Coverage.calculate(collection));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("pattern ip_sat(sat: Spacecraft, handle: java MddHandle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Spacecraft.payload(sat, _);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find sat_online(sat, handle);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private pattern groundcom(css: CommSubsystem){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("GroundStationNetwork(gsn);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("CommunicatingElement.commSubsystem(gsn, css);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern cssReady(css: CommSubsystem, handle: java MddHandle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find groundcom(css);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("TraceModel.mddTrue(_, handle);");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Spacecraft.commSubsystem(sat, css);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find sat_online(sat, sat_handle);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryHandleOf(css, \"component\", _, css_handle);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("handle == eval(D.AND(sat_handle, css_handle));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern link(from: CommSubsystem, to: CommSubsystem, handle: java MddHandle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("CommSubsystem.target(from, to);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find cssReady(to, handle);");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("CommSubsystem.fallback(from, to);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find cssReady(to, handle);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("pattern unaryBERequiredName(element: EObject, name: java String, multiplicity: java Integer, probability: java Double){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("CubeSat3U(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.98400034407713;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("CubeSat6U(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.98496269152523;");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SmallSat(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.98581584235241;");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("XCommSubsystem(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.92596107864232;");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("KaCommSubsystem(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.90483741803596; ");
    _builder.newLine();
    _builder.append("} or {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("UHFCommSubsystem(element);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("name == \"component\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiplicity == 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("probability == 0.92004441462932;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
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
    _builder.append("pattern unaryHandleOf(element: EObject, name: EString, index: EInt, handle: Handle){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("find unaryBETrace(element, name, index, trace);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Trace.handle(trace, handle);");
    _builder.newLine();
    _builder.append("} ");
    _builder.newLine();
    return _builder;
  }

  public CharSequence paramtype(final EntityType param) {
    if (param instanceof ClassType) {
      return _paramtype((ClassType)param);
    } else if (param instanceof JavaType) {
      return _paramtype((JavaType)param);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(param).toString());
    }
  }

  public CharSequence constraint(final Constraint constraint) {
    if (constraint instanceof EClassifierConstraint) {
      return _constraint((EClassifierConstraint)constraint);
    } else if (constraint instanceof CheckConstraint) {
      return _constraint((CheckConstraint)constraint);
    } else if (constraint instanceof CompareConstraint) {
      return _constraint((CompareConstraint)constraint);
    } else if (constraint instanceof PathExpressionConstraint) {
      return _constraint((PathExpressionConstraint)constraint);
    } else if (constraint instanceof PatternCompositionConstraint) {
      return _constraint((PatternCompositionConstraint)constraint);
    } else if (constraint != null) {
      return _constraint(constraint);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(constraint).toString());
    }
  }

  public CharSequence value(final ValueReference reference) {
    if (reference instanceof AggregatedValue) {
      return _value((AggregatedValue)reference);
    } else if (reference instanceof FunctionEvaluationValue) {
      return _value((FunctionEvaluationValue)reference);
    } else if (reference instanceof NumberValue) {
      return _value((NumberValue)reference);
    } else if (reference instanceof StringValue) {
      return _value((StringValue)reference);
    } else if (reference instanceof VariableReference) {
      return _value((VariableReference)reference);
    } else if (reference != null) {
      return _value(reference);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(reference).toString());
    }
  }

  public CharSequence resolve(final XExpression expression) {
    if (expression instanceof XBinaryOperation) {
      return _resolve((XBinaryOperation)expression);
    } else if (expression instanceof XFeatureCall) {
      return _resolve((XFeatureCall)expression);
    } else if (expression instanceof XMemberFeatureCall) {
      return _resolve((XMemberFeatureCall)expression);
    } else if (expression instanceof XCastedExpression) {
      return _resolve((XCastedExpression)expression);
    } else if (expression instanceof XNumberLiteral) {
      return _resolve((XNumberLiteral)expression);
    } else if (expression != null) {
      return _resolve(expression);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expression).toString());
    }
  }

  public CharSequence jvmresolve(final JvmIdentifiableElement expression) {
    if (expression instanceof JvmOperation) {
      return _jvmresolve((JvmOperation)expression);
    } else if (expression instanceof JvmGenericType) {
      return _jvmresolve((JvmGenericType)expression);
    } else if (expression instanceof JvmFormalParameter) {
      return _jvmresolve((JvmFormalParameter)expression);
    } else if (expression != null) {
      return _jvmresolve(expression);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expression).toString());
    }
  }
}
