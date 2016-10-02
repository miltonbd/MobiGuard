package com.callsms.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.mobiguard.MyApp;

import java.util.ArrayList;

/**
 * Created by milton on 21/02/16.
 */
public class ContactHelper {
    public static ArrayList<ContactPojo> getAllContacts() {
        ArrayList<ContactPojo> allContacts = new ArrayList<ContactPojo>();
        Context mContext= MyApp.getContext();
        ContentResolver cr = mContext.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME));
                        ContactPojo contactPojo=new ContactPojo();
                        contactPojo.setNumber(contactNumber);
                        contactPojo.setName(contactName);
                        allContacts.add(contactPojo);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }
    return allContacts;

    }
}
