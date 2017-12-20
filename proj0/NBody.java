public class NBody {

	public static double readRadius(String file) {
		In in = new In(file);
		in.readDouble();
		return in.readDouble();
	}

	public static Planet[] readPlanets(String file) {
		In in = new In(file);
		int num = in.readInt();
		in.readDouble();
		Planet[] planets = new Planet[num];
		int i;
		for (i = 0; i < num; i++){
			planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());

		}
	return planets;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for(int i=0; i<planets.length; i++) {
			planets[i].draw(); 
		}
		double time = 0;

		while (time < T) {
			double[] xforces = new double[planets.length];
			double[] yforces = new double[planets.length];
			for(int i = 0; i<planets.length; i++) {
				xforces[i] = (planets[i].calcNetForceExertedByX(planets));
				yforces[i] = (planets[i].calcNetForceExertedByY(planets));
			}
			for (int i = 0; i < planets.length; i++){
				planets[i].update(dt, xforces[i], yforces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for(int i=0; i<planets.length; i++) {
				planets[i].draw();
			}
			StdDraw.show(10);
			time = time + dt;
		}
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		   		planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);	
		}		
	}
}
