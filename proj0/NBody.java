public class NBody {
  public static double readRadius(String fileName) {
    In in = new In(fileName);
    in.readInt();
    double radius = in.readDouble();
    return radius;
  }

  public static Planet[] readPlanets(String fileName) {
    Planet[] planets;
    In in = new In(fileName);
    int size = in.readInt();
    in.readDouble();
    planets = new Planet[size];
    for (int i = 0; i < size; i++) {
      double xP = in.readDouble();
      double yP = in.readDouble();
      double xV = in.readDouble();
      double yV = in.readDouble();
      double mass = in.readDouble();
      String image = in.readString();
      planets[i] = new Planet(xP, yP, xV, yV, mass, image);
    }
    return planets;
  }

  public static void main(String[] args) {
    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];
    double radius = NBody.readRadius(filename);
    Planet[] planets = NBody.readPlanets(filename);

    StdDraw.setScale(-radius, radius);
    StdDraw.clear();
    StdDraw.picture(0, 0, "images/starfield.jpg");
    for (Planet p: planets) {
      p.draw();
    }
    StdDraw.show();

    StdDraw.enableDoubleBuffering();

    double time = 0;
    while (time < T) {
      double[] xForces = new double[planets.length];
      double[] yForces = new double[planets.length];
      for (int i = 0; i < planets.length; i++) {
        xForces[i] = planets[i].calcNetForceExertedByX(planets);
        yForces[i] = planets[i].calcNetForceExertedByY(planets);
      }
      for (int i = 0; i < planets.length; i++) {
        planets[i].update(dt, xForces[i], yForces[i]);
      }
      StdDraw.clear();
      StdDraw.picture(0, 0, "images/starfield.jpg");
      for (Planet p: planets) {
        p.draw();
      }
      StdDraw.show();
      StdDraw.pause(10);
      time += dt;
    }

    StdOut.printf("%d\n", planets.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < planets.length; i++) {
      StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
      planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
      planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
    }
  }
}
