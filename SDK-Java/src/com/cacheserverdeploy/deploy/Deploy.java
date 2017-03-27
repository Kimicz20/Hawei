package com.cacheserverdeploy.deploy;

import com.lab603.module.Net;
import com.lab603.util.TransStringToModule;
import com.lab603.util.getResult;


public class Deploy
{
  
    public static String[] deployServer(String[] graphContent)
    {
    	Net net = TransStringToModule.fromStrings(graphContent);
//        return getResult.formNet(net);
    	return getResult.formNetWithGA(net);
    }

}
