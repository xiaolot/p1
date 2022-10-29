import java.util.ArrayList;
import java.util.List;
/**
 * Student name: WeiFeng Huang
 * Student number: 300185296
 * NearestNeighbors is a class that help to find neighbors.
 * method: rangeQuery
 */
public class NearestNeighbors {
    private List<Point3D> l;
    public NearestNeighbors(List<Point3D> l){
        this.l=l;
    }

    /**
     * The method that find the neighbors and
     * generate a List of the neighbors.
     * @param eps the radius
     * @param P the center point
     * @return List<Point3D>
     */
    public List<Point3D> rangeQuery(double eps, Point3D P){
        List<Point3D> l2=new ArrayList<>();
        for(Point3D p:l){
            if(P.distance(p)<=eps){
                l2.add(p);
            }
        }
        return l2;
    }
}
