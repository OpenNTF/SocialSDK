package com.ibm.sbt.automation.core.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

public class BaseWidgetTest extends BaseTest{

	/*
	 * The Result Page 
	 */
	private ResultPage resultPage;
	
	/*
	 * The Body Element
	 */
	private WebElement element;
	
	/*
	 * The first link element on the page
	 */
	private String ibmLinkId = "IbmLink";
	private WebElement ibmLink;
	
	/*
	 * Span used by the attach point function
	 */
	private String attachPointSpanId = "attach";
	private WebElement attachPointSpan;
	
	/*
	 * span used by the hitch function
	 */
	private String hitchSpanId = "hitch";
	private WebElement hitchSpan;
	
	/*
	 * link element used by the connect function
	 */
	private String connectLinkId = "connectLink";
	private WebElement connectLink;
	
	/*
	 * span element used by the substitute function
	 */
	private String substituteSpanId = "substituteSpan";
	private WebElement substituteSpan;	
	
	/*
	 * Checks the Widget Base class sample to see if all the functions
	 * in _TemplatedWidget are working correctly
	 * @param snippetId The Id of the snippet to be run
	 * @return boolean of wether the functions all work or not 
	 */
	public boolean checkWidgetBaseClass(String snippetId){
		resultPage = this.launchSnippet(snippetId, this.authType.AUTO_DETECT);
		element = resultPage.getWebElement();
		
		boolean attachAndStop = this.checkAttachAndStopEvent();
		boolean hitch = this.checkHitch();
		boolean connect = this.checkConnect();
		boolean attachPoints = this.checkAttachPoint();
		boolean substitute = this.checkSubstitute();
		
		return attachAndStop && hitch && connect && attachPoints && substitute;
	}
	
	/*
	 * Checks the doAttachEvent and stopEvent functions in _templatedWidget
	 * @return boolean, true if a success message is displayed, else false
	 */
	private boolean checkAttachAndStopEvent(){
		ibmLink = element.findElement(By.id(this.ibmLinkId));
		ibmLink.click();
		
		boolean functionIsOkay = false;
		if(ibmLink.getText() == "StopEvent function is working and doAttachEvents is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	/*
	 * Checks the hitch function in _TemplatedWidget
	 * @return boolean, true if a success message is displayed, else false
	 */
	private boolean checkHitch(){
		this.attachPointSpan = element.findElement(By.id(this.attachPointSpanId));
		boolean functionIsOkay = false;
		if(this.attachPointSpan.getText() == "doAttachPoints function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	/*
	 * Checks the doAttachPoints Function in _TemplatedWidget
	 * @return boolean, true if a success message is displayed, else false
	 */
	private boolean checkAttachPoint(){
		this.hitchSpan = element.findElement(By.id(this.hitchSpanId));
		boolean functionIsOkay = false;
		if(this.hitchSpan.getText() == "Hitch Function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	/*
	 * Checks The Connect function in _TemplatedWidget
	 * @return boolean, true if a success message is displayed, else false
	 */
	private boolean checkConnect(){
		this.connectLink = element.findElement(By.id(this.connectLinkId));
		this.connectLink.click();
		
		boolean functionIsOkay = false;
		if(this.connectLink.getText() == "Connect() function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
	
	/*
	 * Checks the Substitute function in _TemplatedWidget
	 * @return boolean, true if a success message is displayed, else false
	 */
	private boolean checkSubstitute(){
		this.substituteSpan = element.findElement(By.id(this.substituteSpanId));
		boolean functionIsOkay = false;
		if(this.substituteSpan.getText() == "Substitute function is working"){
			functionIsOkay = true;	
		}
		return functionIsOkay;
	}
}
