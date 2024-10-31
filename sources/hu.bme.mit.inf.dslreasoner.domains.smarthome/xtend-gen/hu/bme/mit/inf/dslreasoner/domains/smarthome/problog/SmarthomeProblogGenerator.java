package hu.bme.mit.inf.dslreasoner.domains.smarthome.problog;

import hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities.SmarthomeHelper;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import se.liu.ida.sas.pelab.problog.transformation.ProbLogGeneration;
import smarthome.Home;
import smarthome.Measurement;
import smarthome.Person;
import smarthome.Smarthome;

@SuppressWarnings("all")
public class SmarthomeProblogGenerator {
  private final String incbe = "incrementBE";

  private final String inc = "increment";

  private final String incb = "incrementbase";

  private final String in5m = "within5m";

  private final String aft = "after";

  private final String in5s = "within5s";

  private final String prs = "person";

  private final String nby = "nododyhome";

  private final String co = "highco";

  public String generateFrom(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _incrementevent = this.incrementevent(model);
    _builder.append(_incrementevent);
    _builder.newLineIfNotEmpty();
    _builder.append("0.925::");
    _builder.append(this.incb);
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append(this.inc);
    _builder.append("(A,B) :- ");
    _builder.append(this.incbe);
    _builder.append("(A,B),");
    _builder.append(this.incb);
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    CharSequence _in5min = this.in5min(model);
    _builder.append(_in5min);
    _builder.newLineIfNotEmpty();
    CharSequence _after = this.after(model);
    _builder.append(_after);
    _builder.newLineIfNotEmpty();
    _builder.append("0.925::warningbase.");
    _builder.newLine();
    _builder.append("tempwarning(D) :- after4(_,_,_,D),warningbase.");
    _builder.newLine();
    _builder.append("after4(A,B,C,D) :- after3(A,B,C),after2(C,D),");
    _builder.append(this.in5m);
    _builder.append("(A,D).");
    _builder.newLineIfNotEmpty();
    _builder.append("after3(A,B,C) :- after2(A,B),after2(B,C),");
    _builder.append(this.in5m);
    _builder.append("(A,C).");
    _builder.newLineIfNotEmpty();
    _builder.append("after2(A,B) :- ");
    _builder.append(this.inc);
    _builder.append("(_,A),");
    _builder.append(this.inc);
    _builder.append("(_,B),A\\=B,");
    _builder.append(this.in5m);
    _builder.append("(A,B),");
    _builder.append(this.aft);
    _builder.append("(A,B).");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    CharSequence _highco = this.highco(model);
    _builder.append(_highco);
    _builder.newLineIfNotEmpty();
    CharSequence _in5sec = this.in5sec(model);
    _builder.append(_in5sec);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("firewarning(M) :- ");
    _builder.append(this.co);
    _builder.append("(M),tempwarning(D),");
    _builder.append(this.in5s);
    _builder.append("(M,D).");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    CharSequence _noperson = this.noperson(model);
    _builder.append(_noperson);
    _builder.newLineIfNotEmpty();
    CharSequence _nododyhome = this.nododyhome(model);
    _builder.append(_nododyhome);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("callevent(M) :- firewarning(M),");
    _builder.append(this.nby);
    _builder.append("(M).");
    _builder.newLineIfNotEmpty();
    _builder.append("callprobability(M,P) :- subquery(callevent(M),P).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("query(callprobability(M,P)).");
    _builder.newLine();
    return _builder.toString();
  }

  public CharSequence incrementevent(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          EList<Measurement> _measurements = home.getMeasurements();
          for(final Measurement early : _measurements) {
            {
              EList<Measurement> _measurements_1 = home.getMeasurements();
              for(final Measurement late : _measurements_1) {
                {
                  Boolean _incrementable = this.incrementable(early, late);
                  if ((_incrementable).booleanValue()) {
                    Double _incrementProbability = this.incrementProbability(early, late);
                    _builder.append(_incrementProbability);
                    _builder.append("::");
                    CharSequence _between = this.between(this.incbe, early, late);
                    _builder.append(_between);
                    _builder.append(".");
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

  public CharSequence in5min(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          EList<Measurement> _measurements = home.getMeasurements();
          for(final Measurement early : _measurements) {
            {
              final Function1<Measurement, Boolean> _function = (Measurement m) -> {
                return this.within5m(early, m);
              };
              Iterable<Measurement> _filter = IterableExtensions.<Measurement>filter(home.getMeasurements(), _function);
              for(final Measurement late : _filter) {
                CharSequence _between = this.between(this.in5m, early, late);
                _builder.append(_between);
                _builder.append(".");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }

  public CharSequence after(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          EList<Measurement> _measurements = home.getMeasurements();
          for(final Measurement early : _measurements) {
            {
              final Function1<Measurement, Boolean> _function = (Measurement m) -> {
                return this.after(early, m);
              };
              Iterable<Measurement> _filter = IterableExtensions.<Measurement>filter(home.getMeasurements(), _function);
              for(final Measurement late : _filter) {
                CharSequence _between = this.between(this.aft, early, late);
                _builder.append(_between);
                _builder.append(".");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }

  public CharSequence in5sec(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          EList<Measurement> _measurements = home.getMeasurements();
          for(final Measurement early : _measurements) {
            {
              final Function1<Measurement, Boolean> _function = (Measurement m) -> {
                return this.within5s(early, m);
              };
              Iterable<Measurement> _filter = IterableExtensions.<Measurement>filter(home.getMeasurements(), _function);
              for(final Measurement late : _filter) {
                CharSequence _between = this.between(this.in5s, early, late);
                _builder.append(_between);
                _builder.append(".");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }

  public CharSequence noperson(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Person> _persons = model.getPersons();
      for(final Person person : _persons) {
        double _confidence = person.getConfidence();
        double _minus = (1 - _confidence);
        _builder.append(_minus);
        _builder.append("::");
        CharSequence _between = this.between(this.prs, person);
        _builder.append(_between);
        _builder.append(".");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  public CharSequence nododyhome(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          EList<Measurement> _measurements = home.getMeasurements();
          for(final Measurement m1 : _measurements) {
            {
              boolean _isEmpty = m1.getAthome().isEmpty();
              if (_isEmpty) {
                _builder.append("1::");
                CharSequence _between = this.between(this.nby, m1);
                _builder.append(_between);
                _builder.append(".");
                _builder.newLineIfNotEmpty();
              } else {
                CharSequence _between_1 = this.between(this.nby, m1);
                _builder.append(_between_1);
                _builder.append(" :- ");
                {
                  EList<Person> _athome = m1.getAthome();
                  boolean _hasElements = false;
                  for(final Person p : _athome) {
                    if (!_hasElements) {
                      _hasElements = true;
                    } else {
                      _builder.appendImmediate(",", "");
                    }
                    CharSequence _between_2 = this.between(this.prs, p);
                    _builder.append(_between_2);
                  }
                }
                _builder.append(".");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }

  public CharSequence highco(final Smarthome model) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Home> _homes = model.getHomes();
      for(final Home home : _homes) {
        {
          final Function1<Measurement, Boolean> _function = (Measurement m) -> {
            return this.co5k(m);
          };
          Iterable<Measurement> _filter = IterableExtensions.<Measurement>filter(home.getMeasurements(), _function);
          for(final Measurement m1 : _filter) {
            Double _coProbability = this.coProbability(m1);
            _builder.append(_coProbability);
            _builder.append("::");
            CharSequence _between = this.between(this.co, m1);
            _builder.append(_between);
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }

  public Boolean incrementable(final Measurement m1, final Measurement m2) {
    return Boolean.valueOf(SmarthomeHelper.incrementable(m1.getTemp(), m1.getTime(), m2.getTemp(), m2.getTime()));
  }

  public Double incrementProbability(final Measurement m1, final Measurement m2) {
    return Double.valueOf(SmarthomeHelper.incrementConfidence(m1.getTemp(), m1.getTime(), m2.getTemp(), m2.getTime()));
  }

  public Boolean within5m(final Measurement m1, final Measurement m2) {
    return Boolean.valueOf(SmarthomeHelper.within5m(m1.getTime(), m2.getTime()));
  }

  public Boolean after(final Measurement m1, final Measurement m2) {
    return Boolean.valueOf(SmarthomeHelper.after(m1.getTime(), m2.getTime()));
  }

  public Boolean within5s(final Measurement m1, final Measurement m2) {
    return Boolean.valueOf(SmarthomeHelper.within5s(m1.getTime(), m2.getTime()));
  }

  public CharSequence between(final String type, final EObject... args) {
    final Function1<EObject, String> _function = (EObject arg) -> {
      return this.getName(arg);
    };
    return ProbLogGeneration.dfact(type, ((String[])Conversions.unwrapArray(ListExtensions.<EObject, String>map(((List<EObject>)Conversions.doWrapArray(args)), _function), String.class)));
  }

  public Double coProbability(final Measurement m1) {
    return Double.valueOf(SmarthomeHelper.gt5000Confidence(m1.getCo()));
  }

  public Boolean co5k(final Measurement m1) {
    return Boolean.valueOf(SmarthomeHelper.gt5000(m1.getCo()));
  }

  public String getName(final EObject object) {
    int _hashCode = object.hashCode();
    return ("" + Integer.valueOf(_hashCode));
  }
}
