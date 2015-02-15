package com.movie.watch;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyCurrentLocation {
	private LocationManager lm;
    private LocationResult locationResult;
    private Criteria criteria;
    private String provider;
    private boolean network_enabled = false;
    public static boolean isEnableLocationSelected = true;
    
    /*public MyCurrentLocation(Context c)
    {
    	context = c;
    }*/

    public boolean getLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(lm == null)
        {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        
        try
        {
        	network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}
        
        //just use network updates as we only need approx location
        if(network_enabled)
        {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListenerNetwork);
            Log.d("log_tag", "Network Listener Activated");
        }
        
        return true;
    }

    LocationListener locationListenerNetwork = new LocationListener() 
   	{
        public void onLocationChanged(Location location) 
        {
            locationResult.gotLocation(location);
            Log.d("log_tag", "Network Location Changed " + location.toString());
        }
        public void onProviderDisabled(String provider) {
        	Log.d("log_tag", "Network Provider Disabled " + provider.toString());
        }
        public void onProviderEnabled(String provider) {
        	Log.d("log_tag", "Network Provider Enabled " + provider.toString());
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    
    public void removeListenerUpdates()
    {
    	lm.removeUpdates(locationListenerNetwork);
    }
    
    public boolean getEnableLocationButtonState()
    {
    	return isEnableLocationSelected;
    }

    public void updateWithLastLocation() {

         Location net_loc=null;
             
         if(network_enabled)
         {
             net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
         }
         
         //if there are both values use the latest one
         if(net_loc!=null)
         {
             locationResult.gotLocation(net_loc);
             return;
         }
         
         locationResult.gotLocation(null);
    
    }
    
    public boolean getNetworkLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(lm == null)
        {
        	Log.d("log_tag", "Setting Location Manager Criteria");
        	
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            
            criteria = new Criteria();
            criteria.setAccuracy( Criteria.ACCURACY_COARSE );
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW); 
            
        }
        
        provider = lm.getBestProvider(criteria, true );
        try {
        	network_enabled=lm.isProviderEnabled(provider);
        }catch(Exception ex){}

        if (provider == null) 
        {
        	//if(isEnableLocationSelected == true)
        		//showGPSDisabledAlertToUser();
        	Log.e("log_tag", "No location provider found!" );
        	return false;
        }

       //don't start listeners if no provider is enabled
        if(!network_enabled)
        {
        	//if(isEnableLocationSelected == true)
        		//showGPSDisabledAlertToUser();
        	return false;
        }
        else
        {
        	lm.requestLocationUpdates(provider, 1000, 0, locationListenerNetwork);  
        	Log.i(provider.toString() + " PROVIDER", "ENABLED");
        	Log.d("log_tag", "Requesting Location Updates from " + provider.toString());
        }

        return true;
    }

    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }
}
