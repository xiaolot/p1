import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class test {
    public static void main(String[] args) {
      String csvFile="F:\\Java\\p1\\src\\Point_Cloud_3.csv";
      List<Point3D> l=DBScan.read(csvFile);
      DBScan db=new DBScan(l);
      db.setEps(1.2);
      db.setMinPts(10);
      db.DBScanAlgorithm(1.2,10);
      db.save(csvFile);
    }
}
