public class Planet {
  public static double gravity = 6.67e-11;

  public double xxPos;
  public double yyPos;
  public double xxVel;
  public double yyVel;
  public double mass;
  public String imgFileName;

  public Planet(double xP, double yP, double xV, double yV, double m, String img) {
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
    double dx = xxPos - p.xxPos;
    double dy = yyPos - p.yyPos;
    return Math.sqrt(dx*dx + dy*dy);
  }

  public double calcForceExertedBy(Planet p) {
    double distance = calcDistance(p);
    return mass * p.mass * gravity / (distance * distance);
  }

  public double calcForceExertedByX(Planet p) {
    return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
  }

  public double calcForceExertedByY(Planet p) {
    return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
  }

  public double calcNetForceExertedByX(Planet[] planets) {
    double sum = 0;
    for (Planet p: planets) {
      if (p != this) {
        sum += calcForceExertedByX(p);
      }
    }
    return sum;
  }

  public double calcNetForceExertedByY(Planet[] planets) {
    double sum = 0;
    for (Planet p: planets) {
      if (p != this) {
        sum += calcForceExertedByY(p);
      }
    }
    return sum;
  }

  public void update(double dt, double fX, double fY) {
    double xxAcc = fX / mass;
    double yyAcc = fY / mass;
    xxVel += dt * xxAcc;
    yyVel += dt * yyAcc;
    xxPos += dt * xxVel;
    yyPos += dt * yyVel;
  }

  public void draw() {
    StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
  }
}
