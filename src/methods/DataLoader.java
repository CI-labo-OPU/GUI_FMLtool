package methods;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//データ読み込み用クラス
public class DataLoader {
	public static void inputFile(DataSetInfo data, String fileName) {
	    List<String[]> lines = new ArrayList<String[]>();
	    try{
	      BufferedReader in = new BufferedReader(new FileReader(fileName));
	      String line;
	      String[] x = null;
	      String y;

	      while( (line = in.readLine()) != null ){
	        lines.add(line.split(","));
	      }
	      data.setDataSize( Integer.parseInt(lines.get(0)[0]) );
	      data.setNdim( Integer.parseInt(lines.get(0)[1]) );
	      data.setCnum( Integer.parseInt(lines.get(0)[2]) );
	      lines.remove(0);
	      for(int i = 0; i < lines.size(); i++){
	        data.addPattern(lines.get(i));
	      }
	    } catch(IOException e){
	      e.printStackTrace();
	    }
	}
}
