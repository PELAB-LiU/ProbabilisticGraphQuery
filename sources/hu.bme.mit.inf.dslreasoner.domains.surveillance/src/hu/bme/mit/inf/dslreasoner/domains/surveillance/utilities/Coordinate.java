package hu.bme.mit.inf.dslreasoner.domains.surveillance.utilities;

import uncertaindatatypes.UReal;

public class Coordinate {
	public final UReal x;
	public final  UReal y;
	
	public Coordinate(UReal x, UReal y) {
		this.x = x;
		this.y = y;
	}
	public UReal distance(Coordinate other) {
		UReal a = x.minus(other.x).power(2);
		UReal b = y.minus(other.y).power(2);
		return (a.add(b).sqrt());
	}
	@Override
	public boolean equals(Object o) {
		// Force reference equality
		return o == this;
	}
}
