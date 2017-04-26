package com.nvision.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Channels")
public class Channel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description", length = 2048)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genre_id")
	private Genre genre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private Type type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ts_id")
	private Ts ts;

	@Column(name = "on_id")
	private int on_id;

	@Column(name = "sid")
	private int sid;

	@Column(name = "lcn")
	private int lcn;

	@Column(name = "epg")
	private String epg;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessCriteria_id")
	private AccessCriteria accessCriteria;

	@Column(name = "siteLink")
	private String siteLink;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id")
	private Language language;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broadcastTime_id")
	private BroadcastTime broadcastTime;

	@Column(name = "aLaCarte")
	private boolean aLaCarte;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelPackage_id")
	private ChannelPackage channelPackage;

	@Column(name = "catchUp")
	private boolean catchUp;

	@Column(name = "timeShift")
	private boolean timeShift;

	@Column(name = "pvr")
	private boolean pvr;

	@Column(name = "pin")
	private boolean pin;

	public Channel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Ts getTs() {
		return ts;
	}

	public void setTs(Ts ts) {
		this.ts = ts;
	}

	public int getOn_id() {
		return on_id;
	}

	public void setOn_id(int on_id) {
		this.on_id = on_id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getLcn() {
		return lcn;
	}

	public void setLcn(int lcn) {
		this.lcn = lcn;
	}

	public String getEpg() {
		return epg;
	}

	public void setEpg(String epg) {
		this.epg = epg;
	}

	public AccessCriteria getAccessCriteria() {
		return accessCriteria;
	}

	public void setAccessCriteria(AccessCriteria accessCriteria) {
		this.accessCriteria = accessCriteria;
	}

	public String getSiteLink() {
		return siteLink;
	}

	public void setSiteLink(String siteLink) {
		this.siteLink = siteLink;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public BroadcastTime getBroadcastTime() {
		return broadcastTime;
	}

	public void setBroadcastTime(BroadcastTime broadcastTime) {
		this.broadcastTime = broadcastTime;
	}

	public boolean getALaCarte() {
		return aLaCarte;
	}

	public void setALaCarte(boolean aLaCarte) {
		this.aLaCarte = aLaCarte;
	}

	public ChannelPackage getChannelPackage() {
		return channelPackage;
	}

	public void setChannelPackage(ChannelPackage channelPackage) {
		this.channelPackage = channelPackage;
	}

	public boolean getCatchUp() {
		return catchUp;
	}

	public void setCatchUp(boolean catchUp) {
		this.catchUp = catchUp;
	}

	public boolean getTimeShift() {
		return timeShift;
	}

	public void setTimeShift(boolean timeShift) {
		this.timeShift = timeShift;
	}

	public boolean getPvr() {
		return pvr;
	}

	public void setPvr(boolean pvr) {
		this.pvr = pvr;
	}

	public boolean getPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

}
