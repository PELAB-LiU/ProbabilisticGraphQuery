package hu.bme.mit.inf.dslreasoner.domains.surveillance.viatra

import hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities.Coordinate
import uncertaindatatypes.UReal

class SurveillanceIncrementalChanges {
	val SurveillanceWrapper parent
	new(SurveillanceWrapper wrapper){
		parent = wrapper
	}
	def getNewCoordinate(int i, int j){
		val off_x = #[0,500,700,1000,1000].get(j%5)
		val off_y = #[0,700,700,900,900].get(j%5)
		val coord = new Coordinate(
			new UReal(off_x+(i*1001), 0.1),
			new UReal(off_y+(i*1001), 0.1)
		)
		return coord
	}
	def addDrone(){
		val drove = parent.factory.createDrone
		
	}
}