package uncertaindatatypes;

public class UInteger implements Cloneable,Comparable<UInteger> {
	
	protected int x = 0; 
	protected double u = 0.0;

    /**
     * Constructors 
     */
    public UInteger () {
        this.x = 0; this.u = 0.0;
    }

	public UInteger(int x){ //"promotes" a real x to (x,0) 
		this.x = x; this.u = 0.0;
	}
  
    public UInteger (int x, double u) {
        this.x = x; this.u = u;
    }
	
    public UInteger(String x) { //creates an UReal from a string representing a real, with u=0.
    	this.x = Integer.parseInt(x);
    	this.u = 0.0;
    }
    
    public UInteger(String x, String u) { //creates an UReal from two strings representing (x,u).
    	this.x = Integer.parseInt(x);
    	this.u = Double.parseDouble(u);
    }
   
    /**
     * Setters and getters 
     */
    public int getX() {
		return x; 
	}
    public void setX(int x) {
		this.x = x; 
	}
    public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}

   /*********
     * 
     * Type Operations
     */

	
	public UInteger add(UInteger r) {
		UInteger result = new UInteger();
		result.setX(this.getX() + r.getX());
		result.setU( Math.sqrt((this.getU() * this.getU()) + (r.getU() * r.getU()) ));
		return result;
	}
	

	public UInteger minus(UInteger r) {
		UInteger result = new UInteger();
			result.setX(this.getX() - r.getX());
			result.setU(Math.sqrt((this.getU()*this.getU()) + (r.getU()*r.getU())));
			return result;
	}

	
	public UInteger mult(UInteger r) {
		UInteger result = new UInteger();
		
		result.setX(this.getX() * r.getX());
		
		double a = r.getX()*r.getX()*this.getU()*this.getU();
		double b = this.getX()*this.getX()*r.getU()*r.getU();
		result.setU(Math.sqrt(a + b));
	
		return result;
	}
	
	
	public UInteger divideBy(UInteger r) {
		UInteger result = new UInteger();
	
		double a = this.getX() / r.getX();
		double b = (this.getX()*r.getU()*r.getU())/(Math.pow(r.getX(), 3));
		result.setX((int)Math.floor(a + b));
		
		double c = ((u*u)/r.getX());
		double d = (this.getX()*this.getX()*r.getU()*r.getU()) / Math.pow(r.getX(), 4);
		result.setU(Math.sqrt(c + d));
		
		return result;
	}
	
	/** this operation returns a UReal
	 */
	public UReal divideByR(UInteger r) {
		UReal result = new UReal();
	
		double a = this.getX() / r.getX();
		double b = (this.getX()*r.getU()*r.getU())/(Math.pow(r.getX(), 3));
		result.setX((int)Math.floor(a + b));
		
		double c = ((u*u)/r.getX());
		double d = (this.getX()*this.getX()*r.getU()*r.getU()) / Math.pow(r.getX(), 4);
		result.setU(Math.sqrt(c + d));
		
		return result;
	}
		
	
	public UInteger abs() {
		UInteger result = new UInteger();
	
		result.setX(Math.abs(this.getX()));
		result.setU(this.getU());
	
		return result;
	}
	
	
	public UInteger neg() {
		UInteger result = new UInteger();
		
		result.setX(-this.getX());
		result.setU(this.getU());
	
		return result;
	}

	
	public UInteger power(float s) {
		return this.toUReal().power(s).toUInteger();
 	}

	
	public UInteger sqrt() {
		return this.toUReal().sqrt().toUInteger();
	}

	public UInteger inverse() { //inverse (reciprocal)
		return new UInteger(1,0.0).divideBy(this);
	}
	
	/***
	 * comparison operations
	 * These operations, that return a boolean, have been superseded by the
	 * corresponding UBoolean-returning operations.
	 * 
	public boolean lt(UInteger r) {
			return this.toUReal().lt(r.toUReal());	
	}

	public boolean le(UInteger r) {
		return (this.lt(r) || this.equals(r));
	}

	public boolean gt(UInteger r) {
		return r.lt(this);
	}

	public boolean ge(UInteger r) {
		return  (this.gt(r) || this.equals(r)); 
	}
	

	public boolean equals(UInteger r) {
		return this.toUReal().equals(r.toUReal());
	}

	public boolean distinct(UInteger r) {
		return ( !(this.equals(r)) );
	}
	 */

	/***
	 * comparison operations WITH ZERO = UInteger(0.0)
	 * 
	 * These operations have been superseded too.

	public boolean ltZero() {
		return this.lt(new UInteger());
	}
	
	
	public boolean leZero() {
		return this.le(new UInteger());
	}

	
	public boolean gtZero() {
		return this.gt(new UInteger());
	}
	
	
	public boolean geZero() {
		return this.ge(new UInteger());
	}
	

	public boolean equalsZero() {
		return this.equals(new UInteger());
	}

	public boolean distinctZero() {
		return this.distinct(new UInteger());
	}
	 */

	/*** 
	 *   FUZZY COMPARISON OPERATIONS
	 *   Assume UReal values (x,u) represent standard uncertainty values, i.e., they follow a Normal distribution
	 *   of mean x and standard deviation \sigma = u
	 */
	
	public boolean equals(UInteger number) {
		return this.toUReal().equals(number.toUReal());
	}
	
	public UBoolean uEquals(UInteger number) {
		return this.toUReal().uEquals(number.toUReal());
	}

	public UBoolean distinct(UInteger r) {
		return this.uEquals(r).not();
	}

	public UBoolean lt(UInteger number) {
		return this.toUReal().lt(number.toUReal());
	}
	
	public UBoolean le(UInteger number) {
		return this.toUReal().le(number.toUReal());
	}

	public UBoolean gt(UInteger number) {
		return this.toUReal().gt(number.toUReal());
	}

	
	public UBoolean ge(UInteger number) {
		return this.toUReal().ge(number.toUReal());
	}
   
	/*** 
	 *   END OF FUZZY COMPARISON OPERATIONS
	 */


	/*** 
	 *   FUZZY COMPARISON OPERATIONS WITH ZERO=UReal(0.0,0.0)
	 *   Assume UReal values (x,u) represent standard uncertainty values, i.e., they follow a Normal distribution
	 *   of mean x and standard deviation \sigma = u
	 */
	

	public UBoolean equalsZero() {
		return this.uEquals(new UInteger());
	}

	public UBoolean distinctZero() {
		return this.distinct(new UInteger());
	}

	public UBoolean ltZero() {
		return this.lt(new UInteger());
	}
	
	public UBoolean leZero() {
		return this.le(new UInteger());
	}

	public UBoolean gtZero() {
		return this.gt(new UInteger());
	}

	public UBoolean geZero() {
		return this.ge(new UInteger());
	}
    
	/*** 
	 *   END OF FUZZY COMPARISON OPERATIONS WITH ZERO
	 */

	@Override
	public int compareTo(UInteger other) {
		if (this.uEquals(other).toBoolean()) return 0;
		if (this.lt(other).toBoolean()) return -1;
		return 1;
	}

	public UInteger min(UInteger r) {
		if (r.lt(this).toBoolean()) return new UInteger(r.getX(),r.getU()); 
		return new UInteger(this.getX(),this.getU());
	}
	public UInteger max(UInteger r) {
		//if (r>this) r; else this;
		if (r.gt(this).toBoolean()) return new UInteger(r.getX(),r.getU());
		return new UInteger(this.getX(),this.getU());
	}

	/******
	 * Conversions
	 */
	
	public String toString() {
		return "(" + x + "," + u + ")";
	}
	
	
	public int toInteger(){ //
		return this.getX();
	}
	
	public double toReal()  { 
		return this.getX();
	}
	
	public UReal toUReal() {
		return new UReal(this.getX(),this.getU());
	}
	
	/**
	 * Other Methods 
	 */

 	public int hashcode(){ //required for equals()
		return Math.round((float)x);
	}

 	public UInteger clone() {
		return new UInteger(this.getX(),this.getU());
	}



}
