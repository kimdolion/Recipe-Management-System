import java.awt.*;

class Counter {
    public static int count2DShapes(Shape[] shapes) {
        int twoDCount = 0;
        for (Shape shape : shapes) {
            if (shape instanceof Shape2D && !(shape.getClass().equals(Shape2D.class))) {
                twoDCount++;
            }
        }
        return twoDCount;
    }
}

// Don't change the code below

class Shape {
}

class Shape2D extends Shape {
}

class Shape3D extends Shape {
}


class Circle extends Shape2D {
}

class Shape2DSub1 extends Shape2D {
}

class Shape2DSub2 extends Shape2D {
}


class Cube extends Shape3D {
}

class Shape3DSub1 extends Shape3D {
}

class Shape3DSub2 extends Shape3D {
}