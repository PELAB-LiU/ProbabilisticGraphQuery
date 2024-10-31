package prism;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.lang.ClassCastException;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;

import hu.bme.mit.inf.dslreasoner.domains.satellite1.performability.Performability;

@Deprecated
@SuppressWarnings("all")
public class ModelTrafo {
  public void generate(final String path, final String name, final List<?> sats, final List<?> comms, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback, final List<?> gsc, final Map<Object, Object> values, final List<Object> wpayload) {
    this.generate(path, name, sats, comms, owner, target, fallback, gsc, values, wpayload, CollectionLiterals.<String, Object>newHashMap());
  }
  
  /**
   * Generate PRISM model file from partial model
   */
  public void generate(final String path, final String name, final List<?> sats, final List<?> comms, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback, final List<?> gsc, final Map<Object, Object> values, final List<Object> wpayload, final Map<String, Object> modifiers) {
    try {
      final File out_model = new File(path, (name + ".prism"));
      out_model.getParentFile().mkdirs();
      out_model.createNewFile();
      String _path = out_model.getPath();
      final FileWriter output_model = new FileWriter(_path);
      final CharSequence genmodel = this.prism(sats, comms, owner, target, fallback, gsc, values, wpayload, modifiers);
      output_model.write(genmodel.toString());
      output_model.flush();
      output_model.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Create PRISM model
   */
  public CharSequence prism(final List<?> sats, final List<?> comms, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback, final List<?> gsc, final Map<Object, Object> values, final List<Object> wpayload, final Map<String, Object> modifiers) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ctmc");
    _builder.newLine();
    _builder.newLine();
    _builder.append("module _ ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("_ : bool init true; ");
    _builder.newLine();
    _builder.append("endmodule");
    _builder.newLine();
    {
      List<Object> _reverse = new TopologicalOrder().reverse(sats, comms, owner, target, fallback, gsc);
      for(final Object sat : _reverse) {
        CharSequence _module = this.module(sat, owner, values);
        _builder.append(_module);
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _formula = this.formula(gsc);
    _builder.append(_formula);
    _builder.newLineIfNotEmpty();
    {
      List<Object> _reverse_1 = new TopologicalOrder().reverse(sats, comms, owner, target, fallback, gsc);
      for(final Object sat_1 : _reverse_1) {
        CharSequence _formula_1 = this.formula(sat_1, owner, target, fallback, modifiers);
        _builder.append(_formula_1);
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _activepayloads = this.activepayloads(wpayload, modifiers);
    _builder.append(_activepayloads);
    _builder.newLineIfNotEmpty();
    CharSequence _utilityreward = this.utilityreward(wpayload, modifiers);
    _builder.append(_utilityreward);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * Counter for active payloads
   */
  public CharSequence activepayloads(final List<Object> wpayload, final Map<String, Object> modifiers) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//Active payload counter");
    _builder.newLine();
    _builder.append("formula working = ");
    {
      boolean _isEmpty = wpayload.isEmpty();
      if (_isEmpty) {
        _builder.append("0;");
      } else {
        {
          Object _get = modifiers.get("OPTIMAL_IP_SATS_COUNT");
          boolean _tripleNotEquals = (_get != null);
          if (_tripleNotEquals) {
            Object _get_1 = modifiers.get("OPTIMAL_IP_SATS_COUNT");
            _builder.append(_get_1);
            _builder.append(" +");
          }
        }
        _builder.newLineIfNotEmpty();
        {
          boolean _hasElements = false;
          for(final Object spc : wpayload) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(" + ", "");
            }
            _builder.append("(");
            String _nameOf = this.nameOf(spc);
            _builder.append(_nameOf);
            _builder.append("_online?1:0)");
            _builder.newLineIfNotEmpty();
          }
          if (_hasElements) {
            _builder.append(";");
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence utilityreward(final List<Object> wpayload, final Map<String, Object> modifiers) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("rewards \"utility\"");
    _builder.newLine();
    {
      Integer _offset = this.offset(modifiers);
      Integer _offset_1 = this.offset(modifiers);
      int _size = wpayload.size();
      int _plus = ((_offset_1).intValue() + _size);
      IntegerRange _upTo = new IntegerRange((_offset).intValue(), _plus);
      for(final Integer i : _upTo) {
        _builder.append("working = ");
        _builder.append(i);
        _builder.append(" : ");
        double _calculate = new Performability().calculate2((i).intValue());
        _builder.append(_calculate);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("endrewards");
    _builder.newLine();
    return _builder;
  }
  
  public Integer offset(final Map<String, Object> modifiers) {
    Object _get = modifiers.get("OPTIMAL_IP_SATS_COUNT");
    boolean _tripleNotEquals = (_get != null);
    if (_tripleNotEquals) {
      Object _get_1 = modifiers.get("OPTIMAL_IP_SATS_COUNT");
      return ((Integer) _get_1);
    } else {
      return Integer.valueOf(0);
    }
  }
  
  /**
   * Ground station availability
   */
  public CharSequence formula(final List<?> gsc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("//Ground stations");
    _builder.newLine();
    {
      for(final Object css : gsc) {
        _builder.append("formula ");
        String _nameOf = this.nameOf(css);
        _builder.append(_nameOf);
        _builder.append("_ready = true;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  /**
   * Satellite connection formulas
   */
  public CharSequence formula(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback, final Map<String, Object> modifiers) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//Link formulas");
    _builder.newLine();
    {
      boolean _idealTransmission = this.idealTransmission(sat, owner, target, fallback, modifiers);
      if (_idealTransmission) {
        CharSequence _idealTransmitting = this.idealTransmitting(sat, owner, target, fallback);
        _builder.append(_idealTransmitting);
        _builder.newLineIfNotEmpty();
      } else {
        CharSequence _transmitting = this.transmitting(sat, owner, target, fallback);
        _builder.append(_transmitting);
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _silent = this.silent(sat, owner, target, fallback);
    _builder.append(_silent);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence idealTransmitting(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("formula ");
    String _nameOf = this.nameOf(sat);
    _builder.append(_nameOf);
    _builder.append("_online = ");
    String _nameOf_1 = this.nameOf(sat);
    _builder.append(_nameOf_1);
    _builder.append("_sys=1;");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * Formulas for transmitting communication subsystems
   */
  public CharSequence transmitting(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//Transmitting formulas");
    _builder.newLine();
    {
      boolean _hasTransmitting = this.hasTransmitting(sat, owner, target, fallback);
      if (_hasTransmitting) {
        {
          List<?> _get = owner.get(sat);
          boolean _tripleNotEquals = (_get != null);
          if (_tripleNotEquals) {
            {
              List<?> _get_1 = owner.get(sat);
              for(final Object css : _get_1) {
                {
                  if (((target.get(css) != null) || (fallback.get(css) != null))) {
                    _builder.append("formula ");
                    String _nameOf = this.nameOf(sat);
                    _builder.append(_nameOf);
                    _builder.append("_main = ");
                    String _nameOf_1 = this.nameOf(css);
                    _builder.append(_nameOf_1);
                    _builder.append("=1 & ");
                    {
                      Object _get_2 = target.get(css);
                      boolean _tripleNotEquals_1 = (_get_2 != null);
                      if (_tripleNotEquals_1) {
                        String _nameOf_2 = this.nameOf(target.get(css));
                        _builder.append(_nameOf_2);
                        _builder.append("_ready");
                      } else {
                        _builder.append("false");
                      }
                    }
                    _builder.append(";");
                    _builder.newLineIfNotEmpty();
                    _builder.append("formula ");
                    String _nameOf_3 = this.nameOf(sat);
                    _builder.append(_nameOf_3);
                    _builder.append("_fallback = (!");
                    String _nameOf_4 = this.nameOf(sat);
                    _builder.append(_nameOf_4);
                    _builder.append("_main) & ");
                    {
                      if ((css != null)) {
                        String _nameOf_5 = this.nameOf(css);
                        _builder.append(_nameOf_5);
                        _builder.append("=1 ");
                      } else {
                        _builder.append(" false ");
                      }
                    }
                    _builder.append(" & ");
                    {
                      Object _get_3 = fallback.get(css);
                      boolean _tripleNotEquals_2 = (_get_3 != null);
                      if (_tripleNotEquals_2) {
                        String _nameOf_6 = this.nameOf(fallback.get(css));
                        _builder.append(_nameOf_6);
                        _builder.append("_ready ");
                      } else {
                        _builder.append(" false ");
                      }
                    }
                    _builder.append(";");
                    _builder.newLineIfNotEmpty();
                    _builder.append("formula ");
                    String _nameOf_7 = this.nameOf(sat);
                    _builder.append(_nameOf_7);
                    _builder.append("_online = ");
                    String _nameOf_8 = this.nameOf(sat);
                    _builder.append(_nameOf_8);
                    _builder.append("_sys=1 & ");
                    String _nameOf_9 = this.nameOf(css);
                    _builder.append(_nameOf_9);
                    _builder.append("=1 & (");
                    String _nameOf_10 = this.nameOf(sat);
                    _builder.append(_nameOf_10);
                    _builder.append("_main | ");
                    String _nameOf_11 = this.nameOf(sat);
                    _builder.append(_nameOf_11);
                    _builder.append("_fallback);");
                    _builder.newLineIfNotEmpty();
                    _builder.append("formula ");
                    String _nameOf_12 = this.nameOf(css);
                    _builder.append(_nameOf_12);
                    _builder.append("_ready = ");
                    String _nameOf_13 = this.nameOf(sat);
                    _builder.append(_nameOf_13);
                    _builder.append("_online;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      } else {
        _builder.append("formula ");
        String _nameOf_14 = this.nameOf(sat);
        _builder.append(_nameOf_14);
        _builder.append("_main = false;");
        _builder.newLineIfNotEmpty();
        _builder.append("formula ");
        String _nameOf_15 = this.nameOf(sat);
        _builder.append(_nameOf_15);
        _builder.append("_fallback = false;");
        _builder.newLineIfNotEmpty();
        _builder.append("formula ");
        String _nameOf_16 = this.nameOf(sat);
        _builder.append(_nameOf_16);
        _builder.append("_online = false;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  /**
   * Formulas for non transmitting communication subsystems
   */
  public CharSequence silent(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//Silent formulas");
    _builder.newLine();
    {
      List<?> _get = owner.get(sat);
      boolean _tripleNotEquals = (_get != null);
      if (_tripleNotEquals) {
        {
          List<?> _get_1 = owner.get(sat);
          for(final Object css : _get_1) {
            {
              boolean _not = (!((target.get(css) != null) || (fallback.get(css) != null)));
              if (_not) {
                _builder.append("formula ");
                String _nameOf = this.nameOf(css);
                _builder.append(_nameOf);
                _builder.append("_ready = ");
                String _nameOf_1 = this.nameOf(css);
                _builder.append(_nameOf_1);
                _builder.append("=1 & ");
                String _nameOf_2 = this.nameOf(sat);
                _builder.append(_nameOf_2);
                _builder.append("_online;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  /**
   * Module for satellite
   */
  public CharSequence module(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> values) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("module ");
    String _nameOf = this.nameOf(sat);
    _builder.append(_nameOf);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _nameOf_1 = this.nameOf(sat);
    _builder.append(_nameOf_1, "\t");
    _builder.append("_sys : [0..1] init 1;");
    _builder.newLineIfNotEmpty();
    {
      List<?> _get = owner.get(sat);
      boolean _tripleNotEquals = (_get != null);
      if (_tripleNotEquals) {
        {
          List<?> _get_1 = owner.get(sat);
          for(final Object css : _get_1) {
            _builder.append("\t");
            String _nameOf_2 = this.nameOf(css);
            _builder.append(_nameOf_2, "\t");
            _builder.append(" : [0..1] init 1;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("[] ");
    String _nameOf_3 = this.nameOf(sat);
    _builder.append(_nameOf_3, "\t");
    _builder.append("_sys=1 -> ");
    Object _get_2 = values.get(sat);
    _builder.append(_get_2, "\t");
    _builder.append(":(");
    String _nameOf_4 = this.nameOf(sat);
    _builder.append(_nameOf_4, "\t");
    _builder.append("_sys\'=0);");
    _builder.newLineIfNotEmpty();
    {
      List<?> _get_3 = owner.get(sat);
      boolean _tripleNotEquals_1 = (_get_3 != null);
      if (_tripleNotEquals_1) {
        {
          List<?> _get_4 = owner.get(sat);
          for(final Object css_1 : _get_4) {
            _builder.append("\t");
            _builder.append("[] ");
            String _nameOf_5 = this.nameOf(css_1);
            _builder.append(_nameOf_5, "\t");
            _builder.append("=1 -> ");
            Object _get_5 = values.get(css_1);
            _builder.append(_get_5, "\t");
            _builder.append(":(");
            String _nameOf_6 = this.nameOf(css_1);
            _builder.append(_nameOf_6, "\t");
            _builder.append("\'=0);");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("endmodule");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * Utility function for determining whether a sat has transmitting link or not
   */
  public boolean hasTransmitting(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback) {
    final List<?> coms = owner.get(sat);
    if ((coms == null)) {
      return false;
    }
    for (final Object com : coms) {
      if (((target.get(com) != null) || (fallback.get(com) != null))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean idealTransmission(final Object sat, final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target, final Map<Object, Object> fallback, final Map<String, Object> modifiers) {
    boolean _hasTransmitting = this.hasTransmitting(sat, owner, target, fallback);
    if (_hasTransmitting) {
      return false;
    }
    final Object ideals = modifiers.get("IDEAL_TRANSMITTING");
    if ((ideals == null)) {
      return false;
    }
    try {
      return ((List<Object>) ideals).contains(sat);
    } catch (final Throwable _t) {
      if (_t instanceof ClassCastException) {
        return false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  /**
   * Name converter for PRISM compatibility
   */
  public String nameOf(final Object obj) {
    return obj.toString().replace(".", "_").replace("@", "_").replace(":", "_").replace(" ", "_").replace("(", "_").replace(")", "_");
  }
}
