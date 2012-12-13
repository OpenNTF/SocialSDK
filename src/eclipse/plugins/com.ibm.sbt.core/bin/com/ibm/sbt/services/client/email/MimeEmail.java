package com.ibm.sbt.services.client.email;

import java.util.List;

/**
 * Represents a MIME email.
 *
 */
public interface MimeEmail {

    /**
     * To property of the JSON Object.
     */
    public static final String TO = "to";
    /**
     * CC property of the JSON Object.
     */
    public static final String CC = "cc";
    /**
     * BCC property of the JSON Object.
     */
    public static final String BCC = "bcc";
    /**
     * Subject property of the JSON Object.
     */
    public static final String SUBJECT = "subject";
    /**
     * MIME Part property of the JSON Object.
     */
    public static final String MIME_PARTS = "mimeParts";

    /**
     * Gets the subject.
     * @return The email's subject.
     */
    public String getSubject();

    /**
     * Sets the subject.
     * @param subject The subject to set.
     */
    public void setSubject(String subject);

    /**
     * Gets the to field.
     * @return The email's to field.
     */
    public List<String> getTo();

    /**
     * Gets the list of to addresses separated by a comma.
     * @return The list of to addresses separated by a comma.
     */
    public String getCommaSeparatedTo();

    /**
     * Sets the to addresses.
     * @param to The to to set.
     */
    public void setTo(List<String> to);

    /**
     * Adds an email address to the to field.
     * The email address will not be added if it's value is null.
     * @param email The email address to add.
     */
    public void addToAddress(String email);

    /**
     * Removes an email address from the to field.
     * @param email The email address to remove.
     */
    public void removeToAddress(String email);

    /**
     * Gets the list of CC addresses.
     * @return The list of CC addresses.
     */
    public List<String> getCC();

    /**
     * Gets the list of CC addresses separated by a comma.
     * @return The list of CC addresses separated by a comma.
     */
    public String getCommaSeparatedCC();

    /**
     * Sets the list of CC addresses.
     * @param cc The list of CC addresses to set.
     */
    public void setCC(List<String> cc);

    /**
     * Adds an email address to the CC field.
     * The email address will not be added if its value is null.
     * @param email The email address to add.
     */
    public void addCCAddress(String email);

    /**
     * Removes an email address from the CC field.
     * @param email The email address to remove.
     */
    public void removeCCAddress(String email);

    /**
     * Gets the list of BCC addresses.
     * @return The list of BCC addresses.
     */
    public List<String> getBCC();

    /**
     * Gets the list of BCC addresses separated by a comma.
     * @return The list of BCC addresses separated by a comma.
     */
    public String getCommaSeparatedBCC();

    /**
     * Sets the list of BCC addresses.
     * @param bcc The list of BCC addresses to set.
     */
    public void setBCC(List<String> bcc);

    /**
     * Adds an email address to the BCC field.
     * The email address will not be added if its value is null.
     * @param email  The email address to add.
     */
    public void addBCCAddress(String email);

    /**
     * Removes an email address from the BCC field.
     * @param email The email address to remove.
     */
    public void removeBCCAddress(String email);

    /**
     * Gets the MIME parts for this email.
     * @return The MIME parts for this email.
     */
    public List<MimePart> getMimeParts();

    /**
     * Sets the MIME parts for this email.
     * @param mimeParts The MIME parts to set for this email.
     */
    public void setMimeParts(List<MimePart> mimeParts);

    /**
     * Adds a MIME part to the email.
     * The MIME part will not be added if its value is null.
     * @param mimePart The MIME part to add.
     */
    public void addMimePart(MimePart mimePart);

    /**
     * Removes a MIME part from the email.
     * @param mimePart The MIME part to remove.
     */
    public void removeMimePart(MimePart mimePart);

    /**
     * Sends the email.
     * @throws MimeEmailException Thrown when there is an error sending the email.
     */
    public void send() throws MimeEmailException;

}