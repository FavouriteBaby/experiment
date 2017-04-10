package pers.vinson.datastruct;

public enum ProtocolType{
	OTHER("0"),
	TCP("6"),
	IP("8"),
	UDP("17");

	private String type;

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	private ProtocolType(String type){
		this.type = type;
	}
}