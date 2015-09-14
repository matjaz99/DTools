/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.web;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

@ManagedBean
@ApplicationScoped
public class SnmpSessionBean {
	
	private boolean showNumber = true;
	private boolean showUid = false;
	private boolean showTrapReceiverName = false;
	private boolean showPeerAddress = false;
	private boolean showPeerIp = true;
	private boolean showPeerHostname = false;
	private boolean showSnmpVersion = true;
	private boolean showCommunity = true;
	private boolean showGenericTrap = false;
	private boolean showSpecificTrap = false;
	private boolean showEnterpriseOid = false;
	private boolean showSnmpTrapOid = false;
	private boolean showSysUpTime = false;
	private boolean showNodeName = true;
	private boolean showTrapName = true;
	private boolean showName = false;
	private boolean showSeverity = false;
	private boolean showSourceInfo = false;
	private boolean showGroup = true;
	private boolean showLocation = true;
	private boolean showCustomText = true;
	private boolean showTimestamp = true;
	private boolean showExtDat1 = false;
	private boolean showExtDat2 = false;
	private boolean showExtDat3 = false;
	private boolean showExtDat4 = false;
	private boolean showExtDat5 = false;
	private boolean showExtDat6 = false;
	private boolean showExtDat7 = false;
	private boolean showExtDat8 = false;
	private boolean showExtDat9 = false;
	
	
	public void configureColumns() {
//		System.out.println("no=" + showNumber);
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("success", true);
	}
	
	
	public boolean isShowNumber() {
		return showNumber;
	}
	public void setShowNumber(boolean showNumber) {
		this.showNumber = showNumber;
		System.out.println("setShowNumber: " + showNumber);
	}
	public boolean isShowUid() {
		return showUid;
	}
	public void setShowUid(boolean showUid) {
		this.showUid = showUid;
	}
	public boolean isShowTrapReceiverName() {
		return showTrapReceiverName;
	}
	public void setShowTrapReceiverName(boolean showTrapReceiverName) {
		this.showTrapReceiverName = showTrapReceiverName;
	}
	public boolean isShowPeerAddress() {
		return showPeerAddress;
	}
	public void setShowPeerAddress(boolean showPeerAddress) {
		this.showPeerAddress = showPeerAddress;
	}
	public boolean isShowPeerIp() {
		return showPeerIp;
	}
	public void setShowPeerIp(boolean showPeerIp) {
		this.showPeerIp = showPeerIp;
	}
	public boolean isShowPeerHostname() {
		return showPeerHostname;
	}
	public void setShowPeerHostname(boolean showPeerHostname) {
		this.showPeerHostname = showPeerHostname;
	}
	public boolean isShowSnmpVersion() {
		return showSnmpVersion;
	}
	public void setShowSnmpVersion(boolean showSnmpVersion) {
		this.showSnmpVersion = showSnmpVersion;
	}
	public boolean isShowCommunity() {
		return showCommunity;
	}
	public void setShowCommunity(boolean showCommunity) {
		this.showCommunity = showCommunity;
	}
	public boolean isShowGenericTrap() {
		return showGenericTrap;
	}
	public void setShowGenericTrap(boolean showGenericTrap) {
		this.showGenericTrap = showGenericTrap;
	}
	public boolean isShowSpecificTrap() {
		return showSpecificTrap;
	}
	public void setShowSpecificTrap(boolean showSpecificTrap) {
		this.showSpecificTrap = showSpecificTrap;
	}
	public boolean isShowEnterpriseOid() {
		return showEnterpriseOid;
	}
	public void setShowEnterpriseOid(boolean showEnterpriseOid) {
		this.showEnterpriseOid = showEnterpriseOid;
	}
	public boolean isShowSnmpTrapOid() {
		return showSnmpTrapOid;
	}
	public void setShowSnmpTrapOid(boolean showSnmpTrapOid) {
		this.showSnmpTrapOid = showSnmpTrapOid;
	}
	public boolean isShowSysUpTime() {
		return showSysUpTime;
	}
	public void setShowSysUpTime(boolean showSysUpTime) {
		this.showSysUpTime = showSysUpTime;
	}
	public boolean isShowNodeName() {
		return showNodeName;
	}
	public void setShowNodeName(boolean showNodeName) {
		this.showNodeName = showNodeName;
	}
	public boolean isShowTrapName() {
		return showTrapName;
	}
	public void setShowTrapName(boolean showTrapName) {
		this.showTrapName = showTrapName;
	}
	public boolean isShowName() {
		return showName;
	}
	public void setShowName(boolean showName) {
		this.showName = showName;
	}
	public boolean isShowSeverity() {
		return showSeverity;
	}
	public void setShowSeverity(boolean showSeverity) {
		this.showSeverity = showSeverity;
	}
	public boolean isShowSourceInfo() {
		return showSourceInfo;
	}
	public void setShowSourceInfo(boolean showSourceInfo) {
		this.showSourceInfo = showSourceInfo;
	}
	public boolean isShowGroup() {
		return showGroup;
	}
	public void setShowGroup(boolean showGroup) {
		this.showGroup = showGroup;
	}
	public boolean isShowLocation() {
		return showLocation;
	}
	public void setShowLocation(boolean showLocation) {
		this.showLocation = showLocation;
	}
	public boolean isShowCustomText() {
		return showCustomText;
	}


	public void setShowCustomText(boolean showCustomText) {
		this.showCustomText = showCustomText;
	}


	public boolean isShowTimestamp() {
		return showTimestamp;
	}
	public void setShowTimestamp(boolean showTimestamp) {
		this.showTimestamp = showTimestamp;
	}
	public boolean isShowExtDat1() {
		return showExtDat1;
	}
	public void setShowExtDat1(boolean showExtDat1) {
		this.showExtDat1 = showExtDat1;
	}
	public boolean isShowExtDat2() {
		return showExtDat2;
	}
	public void setShowExtDat2(boolean showExtDat2) {
		this.showExtDat2 = showExtDat2;
	}
	public boolean isShowExtDat3() {
		return showExtDat3;
	}
	public void setShowExtDat3(boolean showExtDat3) {
		this.showExtDat3 = showExtDat3;
	}
	public boolean isShowExtDat4() {
		return showExtDat4;
	}
	public void setShowExtDat4(boolean showExtDat4) {
		this.showExtDat4 = showExtDat4;
	}
	public boolean isShowExtDat5() {
		return showExtDat5;
	}
	public void setShowExtDat5(boolean showExtDat5) {
		this.showExtDat5 = showExtDat5;
	}
	public boolean isShowExtDat6() {
		return showExtDat6;
	}
	public void setShowExtDat6(boolean showExtDat6) {
		this.showExtDat6 = showExtDat6;
	}
	public boolean isShowExtDat7() {
		return showExtDat7;
	}
	public void setShowExtDat7(boolean showExtDat7) {
		this.showExtDat7 = showExtDat7;
	}
	public boolean isShowExtDat8() {
		return showExtDat8;
	}
	public void setShowExtDat8(boolean showExtDat8) {
		this.showExtDat8 = showExtDat8;
	}
	public boolean isShowExtDat9() {
		return showExtDat9;
	}
	public void setShowExtDat9(boolean showExtDat9) {
		this.showExtDat9 = showExtDat9;
	}
	
	
	
}
