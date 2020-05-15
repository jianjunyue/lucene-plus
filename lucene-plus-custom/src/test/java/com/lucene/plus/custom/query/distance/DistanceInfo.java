package com.lucene.plus.custom.query.distance;

public class DistanceInfo {

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRadiusMeters() {
		return radiusMeters;
	}

	public void setRadiusMeters(double radiusMeters) {
		this.radiusMeters = radiusMeters;
	}

	public double getSortKey() {
		return sortKey;
	}

	public void setSortKey(double sortKey) {
		this.sortKey = sortKey;
	}

	public byte[] getMinLat() {
		return minLat;
	}

	public void setMinLat(byte[] minLat) {
		this.minLat = minLat;
	}

	public byte[] getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(byte[] maxLat) {
		this.maxLat = maxLat;
	}

	public byte[] getMinLon() {
		return minLon;
	}

	public void setMinLon(byte[] minLon) {
		this.minLon = minLon;
	}

	public byte[] getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(byte[] maxLon) {
		this.maxLon = maxLon;
	}

	public byte[] getMinLon2() {
		return minLon2;
	}

	public void setMinLon2(byte[] minLon2) {
		this.minLon2 = minLon2;
	}

	public double getAxisLat() {
		return axisLat;
	}

	public void setAxisLat(double axisLat) {
		this.axisLat = axisLat;
	}

	public DistanceInfo(String field, double latitude, double longitude, double radiusMeters, double sortKey,
			double axisLat, byte[] minLat, byte[] maxLat, byte[] minLon, byte[] maxLon, byte[] minLon2) {
		this.field = field;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radiusMeters = radiusMeters;
		this.sortKey = sortKey;
		this.axisLat = axisLat;
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLon = minLon;
		this.maxLon = maxLon;
		this.minLon2 = minLon2;
	}

	private String field;
	private double latitude;
	private double longitude;
	private double radiusMeters;
	private double sortKey;
	private double axisLat;

	private byte[] minLat = new byte[4];
	private byte[] maxLat = new byte[4];
	private byte[] minLon = new byte[4];
	private byte[] maxLon = new byte[4];
	private byte[] minLon2 = new byte[4];
}
