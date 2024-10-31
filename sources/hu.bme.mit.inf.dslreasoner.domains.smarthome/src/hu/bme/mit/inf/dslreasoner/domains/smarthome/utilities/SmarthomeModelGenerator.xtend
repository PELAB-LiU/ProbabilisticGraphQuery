package hu.bme.mit.inf.dslreasoner.domains.smarthome.utilities

import smarthome.SmarthomeFactory
import uncertaindatatypes.UReal
import uncertaindatatypes.UBoolean
import smarthome.Smarthome
import java.util.Collections
import java.util.List
import smarthome.Home
import smarthome.Measurement
import org.eclipse.emf.ecore.EObject
import java.util.Map

class SmarthomeModelGenerator {
	def make(int homes, int persons, int measurements){
		val factory = SmarthomeFactory.eINSTANCE
		
		val container = new SmarthomeModel
		container.model = factory.createSmarthome
		
		for(i : 1..homes){
			val location = new Location(new UReal(1000*container.counter, 10),
										new UReal(1000*container.counter, 10));
			container.locations.add(location)
			container.counter++
		}
		
		for(i : 1..homes){
			val home = factory.createHome
			home.location = container.locations.get(i % container.locations.size);
			container.model.homes.add(home)
			container.idmap.put(home, "home"+i)
		}
		
		for(i : 1..persons){
			val person = factory.createPerson
			person.confidence = 0.88
			container.model.persons.add(person)
			container.idmap.put(person, "person"+i)
		}
		
		for(i : 1..measurements){
			val measurement = factory.createMeasurement
			measurement.temp = new UReal(30+container.counter%7, 0.5)
			measurement.co = new UReal(4920+container.counter%100, 0.5)
			measurement.dopen = new UBoolean(true, 0.8)
			measurement.time = new UReal(4*container.counter,1)
			container.counter++
			container.counter++
			container.idmap.put(measurement, "measurement"+i)
						
			if(i%7==0){
				measurement.athome.add(container.model.persons.get(i%persons))
			}
			container.model.homes.get(i%homes).measurements.add(measurement)
			container.measurements.add(measurement)
		}
		return container
	}
	
	def iterate(SmarthomeModel container, int change){
		val factory = SmarthomeFactory.eINSTANCE
		val off = container.measurements.size
		
		for(int i : 1..change){
			//remove old measurement
			val rm = container.measurements.remove(0)
			container.model.homes.forEach([home | 
				home.measurements.remove(rm)
			])
			
			// add new measurement
			val measurement = factory.createMeasurement
			measurement.temp = new UReal(30+container.counter%7, 0.5)
			measurement.co = new UReal(4920+container.counter%100, 0.5)
			measurement.dopen = new UBoolean(true, 0.8)
			measurement.time = new UReal(4*container.counter,1)
			container.counter++
			container.counter++
			container.idmap.put(measurement, "measurement"+i)
						
			if(i%7==0){
				measurement.athome.add(container.model.persons.get(i%container.model.persons.size))
			}
			container.model.homes.get(i%container.model.homes.size).measurements.add(measurement)
		}
	}
	
}


class SmarthomeModel{
	public var Smarthome model
	public val List<Location> locations = newArrayList
	public val List<Measurement> measurements = newArrayList
	public val Map<EObject, String> idmap = newHashMap
	public var int counter = 1
	
	def int size(){
		return model.homes.fold(
			model.persons.size + model.homes.size, 
			[size, home | (size + home.measurements.size)]
		)
	}
	def ofHashCode(long code){
		measurements.findFirst[it.hashCode === code]
	}
}






