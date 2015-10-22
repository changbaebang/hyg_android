/*
 * Mobile Communication Company, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright(c) 2011 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a
 * retrieval system, or transmitted by any means without prior written
 * Permission of LG Electronics Inc.
 */
package com.lge.launcher2.smartbulletin.lib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;

public class Notification {
    public static final String SBNOTI_ADD_INTENT = "com.lge.launcher2.smartbulletin.ADD_NOTIFICATION_ICON";
    public static final String SBNOTI_REMOVE_INTENT = "com.lge.launcher2.smartbulletin.REMOVE_NOTIFICATION_ICON";
    public static final String SBNOTI_TYPE = "noti_type";
    public static final String SBNOTI_RESOURCE_URI = "resource_uri";
    public static final String SBNOTI_COMPONENT_NAME = "component_name";
    public static final String SBNOTI_TYPE_ONGOING = "ongoing";
    public static final String SBNOTI_TYPE_ONCE = "once";

    public static void sendAddOnceIntent(Context context, ComponentName componentName,
            int resourceId) {
        Intent intent = generateNotiIntent(context, SBNOTI_ADD_INTENT, SBNOTI_TYPE_ONCE,
                resourceId, componentName);
        context.sendBroadcast(intent);
    }

    public static void sendRemoveOnceIntent(Context context, ComponentName componentName,
            int resourceId) {
        Intent intent = generateNotiIntent(context, SBNOTI_REMOVE_INTENT, SBNOTI_TYPE_ONCE,
                resourceId, componentName);
        context.sendBroadcast(intent);
    }

    public static void sendAddOngoingIntent(Context context, ComponentName componentName,
            int resourceId) {
        Intent intent = generateNotiIntent(context, SBNOTI_ADD_INTENT, SBNOTI_TYPE_ONGOING,
                resourceId, componentName);
        context.sendBroadcast(intent);
    }

    public static void sendRemoveOngoingIntent(Context context, ComponentName componentName,
            int resourceId) {
        Intent intent = generateNotiIntent(context, SBNOTI_REMOVE_INTENT, SBNOTI_TYPE_ONGOING,
                resourceId, componentName);
        context.sendBroadcast(intent);
    }

    private static Intent generateNotiIntent(Context context, String action, String notiType,
            int resourceId, ComponentName componentName) {
        Intent intent = new Intent(action);
        intent.putExtra(SBNOTI_TYPE, notiType);
        String resourceUri = getResourceUri(context, resourceId);
        intent.putExtra(SBNOTI_RESOURCE_URI, resourceUri);
        intent.putExtra(SBNOTI_COMPONENT_NAME, componentName.flattenToString());
        return intent;
    }

    private static String getResourceUri(Context context, int resourceId) {

        try {
            String packageName = context.getResources().getResourcePackageName(resourceId);
            String resourceTypeName = context.getResources().getResourceTypeName(resourceId);
            String resourceName = context.getResources().getResourceEntryName(resourceId);

            String resourceUri = "android.resource://" + packageName + "/" + resourceTypeName + "/"
                    + resourceName;
            return resourceUri;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
