/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unidue.tagrecognition;

/**
 *
 * @author czyrux
 */
public enum Message {
	ACK,
	START_SEARCH,
	STOP_SEARCH,
	CALIBRATE,
	SEND_VIEW,
	CALIBRATION_OK,
	CALIBRATION_FAIL,
        ERROR_SENDING,
        ERROR_CONNECTING,
        ERROR_CREATING_SERVER,
	QUIT
}
