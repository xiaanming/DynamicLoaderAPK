package com.dynamic.framework;

import java.io.Serializable;
import java.util.List;

public class PluginInfo implements Serializable{
	private List<String> activities;
	private String applicationName;
	private String pkgName;
	private List<String> services;
	private List<String> receivers;
	private List<String> contentProviders;
	public List<String> getActivities() {
		return activities;
	}
	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	public List<String> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	public List<String> getContentProviders() {
		return contentProviders;
	}
	public void setContentProviders(List<String> contentProviders) {
		this.contentProviders = contentProviders;
	}
	@Override
	public String toString() {
		return "PluginInfo [activities=" + activities + ", applicationName="
				+ applicationName + ", pkgName=" + pkgName + ", services="
				+ services + ", receivers=" + receivers + ", contentProviders="
				+ contentProviders + "]";
	}
	
	
	

}
