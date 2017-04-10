package pers.vinson.datastruct;

import pers.vinson.Utils.*;

public class PcapFileHeader{
	private int magic;				//pacp标识，4B
	private short version_major;	//当前文件的主要标识号，2B
	private short version_minor;	//当前文件的次要标识号，2B
	private int timezone;			//当地的标准时间，4B
	private int sigflags;			//时间戳的精度，4B
	private int snaplen;			//最大存储长度，4B
	private int linktype;			//链路类型，4B

	public PcapFileHeader(){}		

	public PcapFileHeader(int magic, short version_major, short version_minor, int timezone, int sigflags, int snaplen, int linktype){
		this.magic = magic;
		this.version_major = version_major;
		this.version_minor = version_minor;
		this.timezone	   = timezone;
		this.sigflags	   = sigflags;
		this.snaplen	   = snaplen;
		this.linktype	   = linktype;
	}

	public String toString(){
		return "PcapFileHeader [magic=" + DataUtils.intToHexString(magic)
		+ ", version_major=" + DataUtils.shortToHexString(version_major)
		+ ", version_minor=" + DataUtils.shortToHexString(version_minor)
		+ ", timezone=" + DataUtils.intToHexString(timezone)
		+ ", sigflags=" + DataUtils.intToHexString(sigflags)
		+ ", snaplen=" + DataUtils.intToHexString(snaplen)
		+ ", linktype=" + DataUtils.intToHexString(linktype)
		+ "]";
	}

	public int getMagic(){
		return magic;
	}
	public void setMagic(int magic){
		this.magic = magic;
	}

	public short getVersionMajor(){
		return version_major;
	}
	public void setVersionMajor(short version_major){
		this.version_major = version_major;
	}

	public short getVersionMinor(){
		return version_minor;
	}
	public void setVersionMinor(short version_minor){
		this.version_minor = version_minor;
	}

	public int getTimezone(){
		return timezone;
	}
	public void setTimezone(int timezone){
		this.timezone = timezone;
	}

	public int getSigflags(){
		return sigflags;
	}
	public void setSigflags(int sigflags){
		this.sigflags = sigflags;
	}

	public int getSnaplen(){
		return snaplen;
	}
	public void setSnaplen(int snaplen){
		this.snaplen = snaplen;
	}

	public int getLinktype(){
		return linktype;
	}
	public void setLinktype(int linktype){
		this.linktype = linktype;
	}
}