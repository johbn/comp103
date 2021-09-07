public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }    

    public boolean equals(Object o) {
        if(o instanceof Point) {
            Point c = (Point) o;
            if(c.x == this.x && c.y == this.y) {
                return true;
            } 
        }
        return false;
    }    
}

