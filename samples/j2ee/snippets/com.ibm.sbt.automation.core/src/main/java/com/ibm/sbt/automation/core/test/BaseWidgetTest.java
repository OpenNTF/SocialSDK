package com.ibm.sbt.automation.core.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

public class BaseWidgetTest extends BaseTest{

	private ResultPage resultPage;
	private WebElement element;
	
	private String ibmLinkId = "IbmLink";
	private WebElement ibmLink;
	
	private String attachPointSpanId = "attach";
	private WebElement attachPointSpan;
	
	private String hitchSpanId = "hitch";
	private WebElement hitchSpan;
	
	private String connectLinkId = "connectLink";
	private WebElement connectLink;
	
	private String substituteSpanId = "substituteSpan";
	private WebElement substituteSpan;	
	
	public boolean checkWidgetBaseClass(String snippetId){
		resultPage = this.launchSnippet(snippetId, this.authType.AUTO_DETECT);
		element = resultPage.getWebElement();
		
		boolean attachAndStop = this.checkAttachandStopEvent();
		boolean hitch = this.checkHitch();
		boolean connect = this.checkConnect();
		boolean attachPoints = this.checkAttachPoint();
		boolean substitute = this.checkSubstitute();
		
		return attachAndStop && hitch && connect && attachPoints && substitute;
	}
	
	private boolean checkAttachandStopEvent(){
		ibmLink = element.findElement(By.id(this.ibmLinkId));
		ibmLink.click();
		
		boolean functionIsOkay = false;
		if(ibmLink.getText() == "StopEvent function is working and doAttachEvents is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	private boolean checkHitch(){
		this.attachPointSpan = element.findElement(By.id(this.attachPointSpanId));
		boolean functionIsOkay = false;
		if(this.attachPointSpan.getText() == "doAttachPoints function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	private boolean checkAttachPoint(){
		this.hitchSpan = element.findElement(By.id(this.hitchSpanId));
		boolean functionIsOkay = false;
		if(this.hitchSpan.getText() == "Hitch Function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	private boolean checkConnect(){
		this.connectLink = element.findElement(By.id(this.connectLinkId));
		this.connectLink.click();
		
		boolean functionIsOkay = false;
		if(this.connectLink.getText() == "Connect() function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	private boolean checkSubstitute(){
		this.substituteSpan = element.findElement(By.id(this.substituteSpanId));
		boolean functionIsOkay = false;
		if(this.substituteSpan.getText() == "Substitute function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
}
