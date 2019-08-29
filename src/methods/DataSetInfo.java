package methods;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataSetInfo {
	//Fields ****************************************************
	int Ndim;
	int Cnum;
	int DataSize;

	ArrayList<Pattern> patterns = new ArrayList<Pattern>();

	//For Histogram
	int histogramDivide = 20;
	int[][] patternNumAllClass;
	float[][][] patternRate;
	float[][] patternRateAllClass;

	// **********************************************************

	//Constructor ***********************************************
	public DataSetInfo() {}

	public DataSetInfo(int _DataSize, int _Ndim, int _Cnum, int _setting, InetSocketAddress[] _serverList) {
		this.DataSize = _DataSize;
		this.Ndim = _Ndim;
		this.Cnum = _Cnum;
	}
	// **********************************************************


	//Methods ***************************************************
	public void calcHistogram() {
		int[][] patternNum = new int[Cnum][histogramDivide];
		patternNumAllClass = new int[Ndim][histogramDivide];
		patternRate = new float[Ndim][Cnum][histogramDivide];
		patternRateAllClass = new float[Ndim][histogramDivide];

		for(int i = 0; i < Ndim; i++) {
			for(int j = 0; j < histogramDivide; j++) {
				int currentAttribute = i;
				double min = ((double)j) / (double)histogramDivide;
				double max = ((double)j + 1.0) / (double)histogramDivide;

				int count = 0;
				for(int p = 0; p < DataSize; p++) {
					if( patterns.get(p).getDimValue(currentAttribute) <= max &&
						patterns.get(p).getDimValue(currentAttribute) > min) {
						count++;
					}
				}
				patternNumAllClass[i][j] = count;
			}
			int maxNum = patternNumAllClass[i][0];
			for(int j = 1; j < histogramDivide; j++) {
				if(maxNum < patternNumAllClass[i][j]) {
					maxNum = patternNumAllClass[i][j];
				}
			}
			for(int j = 0; j < histogramDivide; j++) {
				patternRateAllClass[i][j] = (float)patternNumAllClass[i][j] / (float)maxNum;
			}

			for(int j = 0; j < histogramDivide; j++) {
				int currentAttribute = i;
				double min = ((double)j) / (double)histogramDivide;
				double max = ((double)j + 1.0) / (double)histogramDivide;
				for(int c = 0; c < Cnum; c++) {
					int nowClass = c;
					int count = 0;
					for(int p = 0; p < DataSize; p++) {
						if( patterns.get(p).getDimValue(currentAttribute) <= max &&
							patterns.get(p).getDimValue(currentAttribute) > min &&
							patterns.get(p).getConClass() == nowClass) {
							count++;
						}
					}
					patternNum[nowClass][j] = count;
				}
			}
			for(int c = 0; c < Cnum; c++) {
				for(int j = 0; j < histogramDivide; j++) {
					patternRate[i][c][j] = (float)patternNum[c][j] / (float)maxNum;
				}
			}
		}
	}


	//クラス数でソート
	public void sortPattern() {
		Collections.sort( this.patterns, new patternComparator() );
	}

	public int getNdim() {
		return this.Ndim;
	}

	public int getCnum() {
		return this.Cnum;
	}

	public int getDataSize() {
		return this.DataSize;
	}

	public Pattern getPattern(int _index) {
		return this.patterns.get(_index);
	}

	public ArrayList<Pattern> getPattern() {
		return this.patterns;
	}

	public void setDataSize(int _num) {
		this.DataSize = _num;
	}

	public void setNdim(int _num) {
		this.Ndim = _num;
	}

	public void setCnum(int _num) {
		this.Cnum = _num;
	}

	//ファイルから読み込み用 (_line[] には教師信号が含まれる)
	public void addPattern(double[] _line) {
		this.patterns.add(new Pattern(_line));
	}

	//Divider用 (シャローコピー)
	public void addPattern(Pattern _pattern) {
		this.patterns.add(_pattern);
	}

	public void setHistogramDivide(int divide) {
		this.histogramDivide = divide;
	}

	public int getHistogramDivide() {
		return this.histogramDivide;
	}

	public int[][] getPatternNumAllClass(){
		return this.patternNumAllClass;
	}

	public float[][][] getPatternRate(){
		return this.patternRate;
	}

	public float[][] getPatternRateAllClass(){
		return this.patternRateAllClass;
	}

	public void addPattern(String[] _line){
		double[] pattern = new double[_line.length];
		for(int i = 0; i < _line.length; i++){
			pattern[i] = Double.parseDouble(_line[i]);
		}
		this.addPattern(pattern);
	}

	// **********************************************************

	//comparator
	public class patternComparator implements Comparator<Pattern>{
		public int compare(Pattern a, Pattern b) {
			double no1 = a.getConClass();
			double no2 = b.getConClass();

			//昇順でソート
			if(no1 > no2) {
				return 1;
			} else if(no1 == no2) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
