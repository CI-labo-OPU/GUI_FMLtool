import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Sample {
	public static void main(String[] args) {
		String xmlFile = "sample.xml";
		MembershipFunction sample = new MembershipFunction(xmlFile);

		int h = 10;
		float[] x = new float[h+1];
		for(int i = 0; i < h; i++) {
			x[i] = (float)i/(float)h;
		}
		x[h] = 1;

		float[][][] y = new float[sample.getAttributeNum()][][];

		for(int i = 0; i < sample.getAttributeNum(); i++) {
			y[i] = new float[sample.getTermNum(i)][x.length];

			for(int j = 0; j < y[i].length; j++) {
				for(int k = 0; k < x.length; k++) {
					y[i][j][k] = sample.getMembershipValue(i, j, x[k]);
				}
			}
		}

		//output
		String sep = File.separator;
		String fileName = "sample.csv";
		ArrayList<String> strs = new ArrayList<String>();
		String str;

		str = "x";
		for(int i = 0; i < sample.getAttributeNum(); i ++) {
			for(int j = 0; j < sample.getTermNum(i); j++) {
				str += "," + sample.getVariableName().get(i) + "_" + sample.getTermName().get(i).get(j);
			}
		}
		strs.add(str);

		for(int k = 0; k < x.length; k++) {
			str = String.valueOf(x[k]);
			for(int i = 0; i < sample.getAttributeNum(); i++) {
				for(int j = 0; j < sample.getTermNum(i); j++) {
					str += "," + String.valueOf(y[i][j][k]);
				}
			}
			strs.add(str);
		}

		String[] array = (String[]) strs.toArray(new String[0]);

		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter( new BufferedWriter(fw) );
			for(int i=0; i<array.length; i++){
				 pw.println(array[i]);
			}
			pw.close();
	    }
		catch (IOException ex){
			ex.printStackTrace();
	    }
	}
}
