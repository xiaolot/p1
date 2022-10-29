import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

/**
 * Student name: WeiFeng Huang
 * Student number: 300185296
 * Density-Based Spatial Clustering of Application with Noise
 * which is DBScan. This is a class to implement DBScan algorithm
 * with particular attribute eps and minPts.
 * eps: trick a particular point as center of circle, trick eps as radius,
 * others points on this circle will be trick as the neighborhood of that point.
 * minPts: if the amount of neighborhood on that circle is bigger or equal to
 * minPts, then such areas would be called dense region. Otherwise, the point
 * that trick as center of circle will be denoted as noise.
 * Others attribute:
 * NUmberOfClusters is counting for the total amounts of cluster.
 * l is a list of all point from class Point3D.
 * Method: getter and setter for these attributes,toString findClusters,
 * read, save and DBScanAlgorithm.
 */
public class DBScan {
    private List<Point3D> l;
    private double eps;
    private double minPts;

    private int NumberOfClusters;

    /**
     * The constructor for class DBScan with one input parameter
     * @param l The list of class Point3D
     * it initializes the NumberOfClusters.
     * if Number of Clusters is -1, it means that
     * DBScanAlgorithm did not execute.
     */
    public DBScan(List<Point3D> l) {
        this.l = l;
        NumberOfClusters=-1;
    }


    public double setEps(double eps) {
        this.eps = eps;
        return eps;
    }

    public double setMinPts(double minPts) {
        this.minPts = minPts;
        return minPts;
    }

    /**
     * The method that executes the DBScanAlgorithm
     */
    public void findClusters(){
        if(eps!=0&&minPts!=0){
            DBScanAlgorithm(eps,minPts);
        }else{
            System.out.println("eps and minPts have not set completely");
        }
    }
    public int getNumberOfClusters(){
        return NumberOfClusters;
    }
    public List<Point3D> getPoints(){
        return l;
    }

    /**
     * The method that label the cluster for each
     * Point3D in list l, according to particular eps and minPts.
     * @param eps
     * @param minPts
     * keep tracking the non-noise Point3D that have not got label,
     * and selecting all their neighbors to a stack. Popping the stack
     * and label the cluster until the stack is empty. repeating this step
     * to all neighbors that are generated.(adding new neighbors of neighbors
     * to stack)
     * determine neighbors: when the Euclidean distance between two point are less
     * or equal to eps, they could be neighbors.
     * Once a Point3D is labeled as noise, it only could be the boundary of the
     * non-noise point.
     * The algorithm also set rbg while setting the cluster label.
     */
    public void DBScanAlgorithm(double eps,double minPts){
        int clusterCounter=0;
        for(Point3D p:l){
            if(p.getNoiseLabel()!=0||p.getClusterLabel()!=0){
                continue;
            }
            if(p.getNoiseLabel()==0){
                List<Point3D> ln=new NearestNeighbors(l).rangeQuery(eps,p);
                //you are noise
                if(ln.size()<minPts){
                    p.setNoiseLabel(1);
                    continue;
                }
                clusterCounter++;
                Double[] rgb=rgbUpdate();
                p.setClusterLabel(clusterCounter);
                p.setRgb(rgb);
                Stack<Point3D> s=new Stack<>();
                //np--neighbors points
                for(Point3D np:ln){
                    s.push(np);
                }
                while(!s.empty()){
                    Point3D Q=s.pop();
                    if (Q.getNoiseLabel()==1){
                        Q.setClusterLabel(clusterCounter);
                        Q.setRgb(rgb);
                    }
                    if (Q.getClusterLabel()!=0||Q.getClusterLabel()!=0){
                        continue;
                    }
                    Q.setClusterLabel(clusterCounter);
                    Q.setRgb(rgb);
                    List<Point3D> Qn=new NearestNeighbors(l).rangeQuery(eps,Q);
                    if(Qn.size()>=minPts){
                        for(Point3D Qnp:Qn){
                            s.push(Qnp);
                        }
                    }
                }
            }
        }
        NumberOfClusters=clusterCounter;
    }

    /**
     * The method that read a csvFile and return the list of class Point3D.
     * @param filename a String for filename
     * @return List<Point3D>
     */
    public static List<Point3D> read(String filename){
        List<Point3D> ans=new ArrayList<>();
        String csvFile=filename;
        try{
            BufferedReader br=new BufferedReader(new FileReader(csvFile));
            br.readLine();//read x,y,z
            String s="";
            while((s=br.readLine())!=null){
                String[] xyz=s.split(",");
                Double tempX=Double.valueOf(xyz[0]);
                Double tempY=Double.valueOf(xyz[1]);
                Double tempZ=Double.valueOf(xyz[2]);
                Point3D p=new Point3D(tempX,tempY,tempZ);
                ans.add(p);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * A method help to generate the string name of the output file.
     * @param filename a String for filename
     * @return String c
     */
    public String csvName(String filename){
        String s="_clusters_"+eps+"_"+minPts+"_"+getNumberOfClusters();
        StringBuffer csvFile=new StringBuffer(filename);
        int index=csvFile.indexOf(".csv");
        csvFile.insert(index,s);
        String c=csvFile.toString();
        return c;
    }

    /**
     * the method save all the points with its attribute to a csv file
     * @param filename a String for filename
     */

    public void save(String filename){
        String output=csvName(filename);
        File outPutFile=new File(output);
        try{
            outPutFile.createNewFile();//In my pc it could not directly create file, so I use createNewFile method
            BufferedWriter bw=new BufferedWriter(new FileWriter(outPutFile));
            bw.newLine();
            bw.write("x,y,z,C,R,G,B");
            for(Point3D p:l){
                bw.newLine();
                String s=p.getX()+","+p.getY()+","+p.getZ()+","+p.getClusterLabel()+","+p.getR()+","+p.getG()+","+p.getB();
                bw.write(s);
            }
       }
        catch(IOException e){e.printStackTrace();}
    }

    /**
     * updating the rgb, store on a Double array.
     * rgb[0] is red, rgb[1] is blue, and rgb[2] is green.
     */
    public Double[] rgbUpdate(){
       Double[] rgb=new Double[3];
       Random r=new Random();
       rgb[0]=r.nextDouble();
       rgb[1]=r.nextDouble();
       rgb[2]=r.nextDouble();
       return rgb;
    }
    /**
     * method toString is implemented for testing.
     */
    @Override
    public String toString() {
        String s="";
        for(Point3D p:l){
            s=s+p.toString()+"\n";
        }
        return s;
    }

    /**
     * Main
     * @param args
     * args[0] should be the input filename
     * for example: F:\\Java\\p1\\src\\Point_Cloud_1.csv in my pc.
     * arg[1] should be eps
     * arg[2] should be minPts
     * There would be an output csv file in the same file src
     * Example--input: java DBScan F:\Java\p1\src\Point_Cloud_1.csv 1.2 10
     * the name of output file would be Point_Cloud_1_clusters_1.2_10.0_29
     */
    public static void main(String[] args) {
        Double eps;
        Double minPts;
        String inputFile;
        if(args.length==3) {
            eps=Double.valueOf(args[1]);
            minPts=Double.valueOf(args[2]);
            inputFile=args[0];
            List<Point3D> l=DBScan.read(inputFile);
            DBScan db=new DBScan(l);
            db.setEps(eps);
            db.setMinPts(minPts);
            db.findClusters();
            db.save(inputFile);
        }else{
            System.out.println("Please enter the input filename, the parameters eps and minPts");
            System.out.println("For example: java DBScan data.csv 1.2 10");
        }
    }
}
