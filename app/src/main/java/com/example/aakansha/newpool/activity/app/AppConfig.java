package com.example.aakansha.newpool.activity.app;

/**
 * Created by aakansha on 6/12/2017.
 */

public class AppConfig {

    public static final String ip= "192.168.28.28";

    //login and register api
    public static String URL_LOGIN = "http://"+ip+"/commute/login.php";
    public static String URL_REGISTER = "http://"+ip+"/commute/register.php";
    public static String URL_REQUEST = "http://"+ip+"/bandroid_api/justUsers.php";

    //requests api
    public static String URL_REQUEST_SENT = "http://"+ip+"/commute/ViewSentRequests.php?employee_id=";
    public static String URL_REQUEST_RECEIVED = "http://"+ip+"/commute/ViewReceivedRequests.php?employee_id=";
    public static String URL_REQUEST_ACCEPTED = "http://"+ip+"/commute/ViewAcceptedRequests.php?employee_id=";
    public static String URL_SAME_ZONE_PEOPLE = "http://"+ip+"/commute/sameZonePeople.php?employee_id=";
    public static String URL_SAME_ZONE_DRIVERS = "http://"+ip+"/commute/sameZoneDrivers.php?employee_id=";
    public static String URL_RESPONSE_UNDO = "http://"+ip+"/commute/ResponseUndo.php?employee_id=";
    public static String URL_UPDATE_ACCESS = "http://"+ip+"/commute/updateUserAccess.php?employee_id=";
    public static String URL_RESPONSE_ACCEPT = "http://"+ip+"/commute/AcceptingRequest.php?acceptor_id=";
    public static String URL_RESPONSE_CANCEL = "http://"+ip+"/commute/CancelingRequest.php?acceptor_id=";
    public static String URL_SEND_REQUEST = "http://"+ip+"/commute/SendingRequest.php?requester_id=";

    //maps api
    public static String URL_MAP = "http://"+ip+"/commute/nextPassengerDetails.php"; //fetching a passenger one by one
    public static String URL_START_JOURNEY = "http://"+ip+"/commute/StartJourneyAPI.php"; //waypoints order optimization,route calculation
    public static String URL_UPDATE_LOCATION = "http://"+ip+"/commute/updateLocation.php"; //driver's current location updated in db
    public static String URL_RIDE_COMPLETE = "http://"+ip+"/commute/RideComplete.php"; //setting status- ride completed against each passenger
    public static String URL_PASSENGER = "http://"+ip+"/commute/passengerMapUpdate.php"; //plotting passengers's map
}