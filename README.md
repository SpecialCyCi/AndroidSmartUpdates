AndroidSmartUpdates
===================
Android Smart Updates is an Open Source library that make patch way update in android easily.<br>
And server is base on Ruby on Rails.<br>
Link to server source code [AndroidSmartUpdatesServer][1]

Demo
----------
Link to [AndroidSmartUpdatesDemo][2]

How to Install
----------
Using IntelliJ:<br>
1. import the AndroidSmartUpdates as a module into your project.<br>
2. add AndroidSmartUpdates into module dependency for your main module.<br>

Usage
----------
1. create an application in AndroidSmartUpdatesServer,and mark down the App ID.
2. copy the files in the folder ***libs*** your main module directory.<br>
3. add below permission in your AndroidManifest.xml
>     <uses-permission android:name="android.permission.INTERNET"/>
>     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

4、add these codes to your activity's entrance.

>         smartUpdates = new SmartUpdates(this);
>         smartUpdates.setServerAddress("http://www.xxx.com/");
>         smartUpdates.setApplicationId(YOUR_APP_ID);
>         smartUpdates.checkForUpdate();

**fill YOUR_APP_ID with App Id in step 1**

Listen the Update Status
----------
You can use the ***UpdateListener***

>     smartUpdates.setShowUpdateDialog(false);
>     smartUpdates.setUpdateListener(updateListener);
>     smartUpdates.checkForUpdate();
>     private UpdateListener updateListener = new UpdateListener() {
> 
>         @Override
>         public void hasUpdate(PatchInformation information) {
>             Toast.makeText(context, "has update, version:" + information.getVersionName(),
>                     Toast.LENGTH_LONG).show();
>             // start to update.
>             smartUpdates.startUpdate();
>         }
> 
>         @Override
>         public void hasNoUpdate() {
>             Toast.makeText(context, "no update", Toast.LENGTH_LONG).show();
>         }
>     };

About Author
----------
A student from SCAU China.<br>
Email: specialcyci#gmail.com


  [1]: https://github.com/SpecialCyCi/AndroidSmartUpdatesServer
  [2]: https://github.com/SpecialCyCi/AndroidSmartUpdatesDemo
