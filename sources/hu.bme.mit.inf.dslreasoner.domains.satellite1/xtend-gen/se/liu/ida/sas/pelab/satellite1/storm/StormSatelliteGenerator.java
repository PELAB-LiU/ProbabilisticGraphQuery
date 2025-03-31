package se.liu.ida.sas.pelab.satellite1.storm;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class StormSatelliteGenerator {
  public Pair<String, List<String>> generateFrom(final /* Satellite */Object model) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field calls is undefined"
      + "\nThe method or field callevent is undefined"
      + "\nkey cannot be resolved"
      + "\nmap cannot be resolved"
      + "\ntoList cannot be resolved");
  }

  public String key(final String name, final EObject... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(name);
    _builder.append("_");
    {
      boolean _hasElements = false;
      for(final EObject arg : args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("_", "");
        }
        String _name = this.getName(arg);
        _builder.append(_name);
      }
    }
    return _builder.toString();
  }

  public String getName(final EObject object) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = object.getClass().getSimpleName();
    _builder.append(_simpleName);
    int _hashCode = object.hashCode();
    _builder.append(_hashCode);
    return _builder.toString();
  }
}
