package com.example.practice;

// Sealed class
sealed abstract class Shape permits Circle, Rectangle, Triangle {
    public abstract double area();
}

// Fixed class name
final class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

final class Rectangle extends Shape {
    double length;
    double breadth;

    Rectangle(double length, double breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    @Override
    public double area() {
        return length * breadth;
    }
}

final class Triangle extends Shape {
    double base;
    double height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Shape s1 = new Circle(5);
        Shape s2 = new Rectangle(4, 2);
        Shape s3 = new Triangle(2, 4);

        System.out.println(s1.area());
        System.out.println(s2.area());
        System.out.println(s3.area());
    }
}