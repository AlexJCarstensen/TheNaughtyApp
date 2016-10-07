package com.alexcarstensen.thebrandapp.Helpers;

/**
 * Created by Peter Ring on 07/10/2016.
 */

public class EmailNameHelper {

    public static String ConvertEmail(String emailName)
    {
        String dot = ".";
        String convertedDot = ";_dot_;";


        if(emailName.contains(dot))
        {
            emailName = emailName.replace(dot, convertedDot);
        }

        return emailName;
    }

    public static String  BackConvertEmail(String convertedEmail)
    {
        String dot = ".";
        String convertedDot = ";_dot_;";

        if(convertedEmail.contains(convertedDot))
        {
            convertedEmail = convertedEmail.replace(convertedDot, dot);
        }

        return convertedEmail;
    }
}
