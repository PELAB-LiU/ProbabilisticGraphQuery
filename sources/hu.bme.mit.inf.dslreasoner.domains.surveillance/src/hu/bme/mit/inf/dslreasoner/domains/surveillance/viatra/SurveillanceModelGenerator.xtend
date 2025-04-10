package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import surveillance.SurveillanceModel
import surveillance.SurveillanceFactory
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
import uncertaindatatypes.UReal
import org.eclipse.viatra.query.runtime.api.IPatternMatch
import java.util.Comparator
import org.eclipse.emf.ecore.EObject
import java.util.Map
import java.util.Random
import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.SurveillanceHelper

class SurveillanceModelGenerator {
	def makeRadomDrone(int size, Random rnd){
		val drone = SurveillanceFactory.eINSTANCE.createDrone
		drone.angle = new UReal(rnd.nextDouble(2*Math.PI, 0.1))
		drone.speed = new UReal(20, 0.1)
		drone.position = new Coordinate(
			new UReal(rnd.nextDouble(1001*(size/5.0)), 0.1),
			new UReal(rnd.nextDouble(1001*(size/5.0)), 0.1)
		)
	}
	def makeRadomTarget(int size, Random rnd){
		val drone = SurveillanceFactory.eINSTANCE.createDrone
		drone.angle = new UReal(rnd.nextDouble(2*Math.PI, 0.1))
		drone.speed = new UReal(50+rnd.nextDouble(10), 0.2)
		drone.position = new Coordinate(
			new UReal(rnd.nextDouble(1001*(size/5.0)), 0.1),
			new UReal(rnd.nextDouble(1001*(size/5.0)), 0.1)
		)
	}
	def SurveillanceWrapper make(int size, int seed){
		val wrapper = new SurveillanceWrapper(seed)
		val factory = wrapper.factory
		
		for(int i : 1..(size/5)){
			val d1 = factory.createDrone
			d1.position = new Coordinate(new UReal( 0 + 1001*i, 0.1), new UReal(0 + 1001*i, 0.1));
			d1.speed = new UReal(20,0.1)
			d1.angle = new UReal(0.78, 0.02)
			wrapper.model.objects.add(d1)
			wrapper.ordering.put(d1, wrapper.newId)
			
			val d2 = factory.createDrone
			d2.position = new Coordinate(new UReal(500 + 1001*i, 0.1), new UReal(700 + 1001*i, 0.1));
			d2.speed = new UReal(20,0.1)
			d2.angle = new UReal(1.5, 0.02)
			wrapper.model.objects.add(d2)
			wrapper.ordering.put(d2, wrapper.newId)
			
			val o1 = factory.createUnidentifiedObject();
			o1.speed = new UReal(50, 0.2);
			o1.position = new Coordinate(new UReal(700 + 1001 * i, 0.1), new UReal(700 + 1001 * i, 0.1));
			o1.setConfidence(0.98);
			o1.angle = new UReal(3.92, 0.07)
			wrapper.model.objects.add(o1);
			wrapper.ordering.put(o1, wrapper.newId)
			
			val o2 = factory.createUnidentifiedObject();
			o2.setSpeed(new UReal(60, 0.2));
			o2.position = new Coordinate(new UReal(1000 + 1001 * i, 0.1), new UReal(900 + 1001 * i, 0.1))
			o2.setConfidence(0.98);
			o2.angle = new UReal(3.14, 0.07)
			wrapper.model.objects.add(o2);
 			wrapper.ordering.put(o2, wrapper.newId)
 			
			val o3 = factory.createUnidentifiedObject();
			o3.speed = new UReal(60, 0.2);
			o3.position = new Coordinate(new UReal(1000 + 1001 * i, 0.1), new UReal(900 + 1001 * i, 0.1));
			o3.setConfidence(0.98);
			o3.angle = new UReal(3.14, 0.07)
			wrapper.model.objects.add(o3);
			wrapper.ordering.put(o3, wrapper.newId)
		}
		return wrapper
	}
	def iterate(SurveillanceWrapper wrapper, double threshold){
		wrapper.model.objects.removeIf([obj |
			return wrapper.rnd.nextDouble < threshold
		])
		wrapper.model.objects.forEach[obj |
			val old = obj.position
			val neww = SurveillanceHelper.move(old, obj.speed, obj.angle, 1)
			obj.position = neww 
		]
	}
}

class SurveillanceWrapper{
	public val SurveillanceFactory factory
	public val SurveillanceModel model
	public val Map<EObject, Integer> ordering = newHashMap
	public val Random rnd;
	var int id = 1;
	def int newId(){return id++}
	
	new(int seed){
		factory = SurveillanceFactory.eINSTANCE
		model = factory.createSurveillanceModel
		rnd = new Random(seed)
	}
	def getComparator(){
		return new SurveillanceComparator(ordering)
	}
	def size(){
		model.objects.size
	}
	def ofHashCode(int hashcode){
		model.objects.findFirst[it.hashCode === hashcode]
	}
}

class SurveillanceComparator implements Comparator<IPatternMatch>{
	val Map<EObject, Integer> ordering;
	new(Map<EObject, Integer> ordering){
		this.ordering = ordering
	}
	override compare(IPatternMatch o1, IPatternMatch o2) {
		val argc = Math.max(o1.parameterNames.size, o2.parameterNames.size)
		for(int i : 0..(argc-1)){
			val diff = ordering.get(o2.get(i))-ordering.get(o1.get(i))
			if(diff != 0){
				return diff
			}
		}
		return 0
	}
	
}


