public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	private static double G = 6.67e-11;

	public Planet(double xP, double yP, double xV,
		double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}


	public double calcDistance(Planet p) {
		return Math.sqrt(Math.pow((this.xxPos - p.xxPos), 2) + Math.pow((this.yyPos - p.yyPos), 2));
	}

	public double calcForceExertedBy(Planet p) {
		return (G * this.mass * p.mass)/(this.calcDistance(p) * this.calcDistance(p));
	}

	public double calcForceExertedByX(Planet p) {
		return (this.calcForceExertedBy(p) * (p.xxPos - this.xxPos))/(this.calcDistance(p)); 
	}
    
	public double calcForceExertedByY(Planet p) {
		return (this.calcForceExertedBy(p) * (p.yyPos - this.yyPos))/(this.calcDistance(p));
	}

	public double calcNetForceExertedByX(Planet[] allPlanets) {
		int i;
		double netx = 0;
		for(i=0; i<allPlanets.length; i++) {
			if (allPlanets[i] != this) {
				netx = netx + this.calcForceExertedByX(allPlanets[i]);

			}
		}
		return netx;
	}

	public double calcNetForceExertedByY(Planet[] allPlanets) {
		int j;
		double nety = 0;
		for(j=0; j<allPlanets.length; j++) {
			if (allPlanets[j] != this) {
				nety = nety + this.calcForceExertedByY(allPlanets[j]);

			}
		}
		return nety;
	}

	public void update(double time, double xforce, double yforce) {
		double xaccel = xforce/this.mass;
		double yaccel = yforce/this.mass;
		this.xxVel = this.xxVel + (time * xaccel);
		this.yyVel = this.yyVel + (time * yaccel);
		this.xxPos = this.xxPos + (time * this.xxVel);
		this.yyPos = this.yyPos + (time * this.yyVel);

	} 

	public void draw() {
		StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
	}



}
