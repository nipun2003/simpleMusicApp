# simpleMusicApp

This is an simple Music tutorial using java and xml

Creating step -->

(i) First you need to create a new project name Nmusic(you can choose different name )

(ii)In you activity_main.xml file
      (a) You have to add a list view or recycler view for displaying the song 
(iii)In you ActivityMain.java file 
      (a) 1st you need to take user permisssion for reading the itnernal storage 
      
          Go to your Manifest.xml and add this line above the application tag
          
              <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
              
          For legacy storgae support you have to add the this line inside the application tag
          
                  android:requestLegacyExternalStorage="true"
                  
      (b) Now you have to show the permission token in user device 
            
            For our project we use Dexter class for taking permission for this you have to add this line in your build.gradle file(Module)
            
                implementation 'com.karumi:dexter:6.2.2'
                
                Or you can go to the official page of Karumi for more info https://github.com/Karumi/Dexter
                
       (c) We created a runtimepermisssion() method to get permission of storage in MainActivity.java class
       
       (d) After getting the permission we have to display our all song in list View
