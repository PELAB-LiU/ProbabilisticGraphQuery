package prism;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class TopologicalOrder {
   public int hexNameOf(final Object obj) {
      final int name = obj.hashCode();
      return name;
   }
   @Deprecated
   public List<Object> reverse(final List<?> satellites, final List<?> comms,
         final Map<Object, ? extends List<?>> owner, final Map<Object, Object> target,
         final Map<Object, Object> fallback, final List<?> gsc) {
      final ArrayList<Object> reverse = CollectionLiterals.<Object> newArrayList();
      final ArrayList<Object> source = CollectionLiterals.<Object> newArrayList();
      for(final Object spacecraft : satellites) {
         source.add(spacecraft);
      }
      for(int i = 0; i < source.size();) {
         {
            boolean cssOK = true;
            final List<?> subsys = owner.get(source.get(i));
            if(subsys != null) {
               for(final Object css : subsys) {
                  {
                     final Object trg = target.get(css);
                     boolean targetOk = trg == null || gsc.contains(trg);
                     if(!targetOk) {
                        for(final Object sel : reverse) {
                           {
                              final List<?> own = owner.get(sel);
                              if(own != null) {
                                 targetOk = targetOk || own.contains(trg);
                              }
                           }
                        }
                     }
                     final Object flb = fallback.get(css);
                     boolean fallbackOk = flb == null || gsc.contains(flb);
                     if(!fallbackOk) {
                        for(final Object sel_1 : reverse) {
                           {
                              final List<?> own = owner.get(sel_1);
                              if(own != null) {
                                 fallbackOk = fallbackOk || own.contains(flb);
                              }
                           }
                        }
                     }
                     cssOK = cssOK && targetOk && fallbackOk;
                  }
               }
            }
            if(cssOK) {
               final Object spc = source.get(i);
               source.remove(spc);
               reverse.add(spc);
               i = 0;
            } else {
               i++;
            }
         }
      }
      final int _size = reverse.size();
      final int _size_1 = satellites.size();
      final boolean _tripleNotEquals = _size != _size_1;
      if(_tripleNotEquals) {
         throw new RuntimeException("Topological order failed");
      }
      return reverse;
   }
}
