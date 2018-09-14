public class SynBISdata {

	private String DisplayID = "", nativeFrom = "", elements = "";
	private double rpu3 = Double.NaN;

	public void setDisplayID(String s){
		if (DisplayID.equals("")){
			DisplayID=s;
		}
	}
	
	public void setNativeFrom(String s){
		if (nativeFrom.equals("")){
			nativeFrom=s;
		}
	}
	
	public void setElements(String s){
		if (elements.equals("")){
			elements=s;
		}
	}
	
	public void setRPU(double d) {
		if (Double.isNaN(rpu3 )) {
			rpu3 = d;
		}
	}
		
	public String getDisplayID(){
		return DisplayID;
	}
	
	public String getNativeFrom(){
		return nativeFrom;
	}
	
	public String getElements(){
		return elements;
	}
	
	public double getRPU(){
		return rpu3;
	}

	public String toString() {
		return DisplayID + "," + nativeFrom + "," + elements + ","
				+ (Double.isNaN(rpu3) ? "" : rpu3);
	}

}
