![alt tag](https://github.com/NielsMasdorp/Speculum-Android/blob/master/app/src/main/assets/Speculum_promo.jpg)

This is an Android application for a magic mirror.
I had an old Nexus 7 (2012) lying around collecting dust and got the idea from [HomeMirror by Hannah Mitt](https://github.com/HannahMitt/HomeMirror).

I started this project because I wanted to explore the MVP pattern for Android and Hannah's app did not have all the functionality I needed. Feel free to make this application look good on your own device (it was designed for the Nexus 7). Have any great ideas to make this more configurable or usable on more devices? 
Submit a pull request!

Setup            |  Running
:-------------------------:|:-------------------------:
![alt tag](https://github.com/NielsMasdorp/Speculum-Android/blob/master/app/src/main/assets/Screenshot_2016-01-20-23-24-13_framed.png)  |  ![alt tag](https://github.com/NielsMasdorp/Speculum-Android/blob/master/app/src/main/assets/Screenshot_2016-01-19-17-21-46_framed.png)

Features
====
* Date and time
* Weather powered by Yahoo Weather
  * Metric or Imperial
  * Wind information
    * Temperature
    * Speed
    * Direction
  * Humidity
  * Pressure
  * Visibility
  * Four day forecast
  * Sunrise and sunset time
* Your upcoming Google Calendar event
* One of the top posts of your favorite subreddit
* Experimental voice command support with Pocketsphinx

Voice commands
====

By default there is only one language dictionary and voice command available.
The current dictionary is an *US-English dictionary*. In order to wake the device say *'hello mirror'*. This phrase can be changed in `Constants.java`, change 

```java
public static final String KEYPHRASE = "hello mirror";
```
to anything you want. When you speak this phrase the device will respond and you have 5 seconds speak an command. As of now the only command is *'fetch'*. This will update the mirror data (weather, Reddit etc..). After the device has successfully received a command the action you've assigned to it (more below) will be excecuted and the device will go back to sleep.

If you want to add commands to the list of available commands and assign actions to it you must first add the commands to `/assets/sync/commands.gram` and use this format:

```
do this /1.0/
do that /1.0/
fetch /1.0/
turn on lights /1.0/
turn off lights /1e-1/
```
The number beween the `//` is the threshold for detecting, more about this below. Also, the words in the commands you choose **must** exist in the dictionary file, which can be found in `/assets/sync/cmudict-en-us.dict`.

After you add your commands you must individually add them to `Constants.java` as well.

```java
public static final String KEYPHRASE = "hello mirror";
public static final String DO_THIS_PHRASE = "do this";
public static final String DO_THAT_PHRASE = "do that";
public static final String UPDATE_PHRASE = "fetch";
public static final String LIGHT_ON_PHRASE = "turn on lights";
public static final String LIGHT_OFF_PHRASE = "turn off lights";
```
Now you only have to assign your custom actions to the commands. You can assign your own actions to the commands in the `processCommand()` method in the `MainPresenterImpl.java`.

```java
@Override
public void processCommand(String command) {
        
    //I've added only the magic keyword and one other command here
    //but you get the point. you can do anything you want here

    if (mMainView.get() != null) {
        if (command.equals(Constants.KEYPHRASE)) {
            // go and listen for commands
            mMainView.get().startListening(true, true);
        } else if (command.equals(Constants.UPDATE_PHRASE)) {
            //update all data
            mMainView.get().updateData();
            //go back to sleep and wait for the magic keyword again
            mMainView.get().startListening(false, true);
        }
    }
}
```
As of now since I only have 2 commands I pass an `boolean isSleeping` to the `startListening()` method to determine what the next step would be (either go back to sleep and wait for the *'hello mirror'* phrase again or to listen for legit commands.
When you have more legit commands this logic must ofcourse be changed. There is a `talk()` method in `MainActivity.java` that you can use to have the device say different things depending on the command received.

After changing **reinstall the application** and say the magic wake up command, when you get the response from the device say your custom command and that's pretty much it!

###Thresholds
If you are having trouble with commands being recognized like I have, you can edit the individual thresholds per keyphrase in the grammar file, I am still trying to find the best thresholds for my commands. More info can be found in the [Pocketsphinx  tutorial](http://cmusphinx.sourceforge.net/wiki/tutoriallm).

###Other languages and dictionaries
I have not experimented with this, however it is possible to create your own dictionary file. Please refer to the [CMU Sphinx tutorial](http://cmusphinx.sourceforge.net/wiki/tutorial).

How do I use this
====

1. Clone project in Android Studio
2. Make nescessary adjustments for your device
3. Run on device or generate .APK
4. Turn on "Stay Awake" in Developer Options on your device

How I made my mirror
====
//TODO

Thanks to
====

#####[gijsdewit](https://github.com/gijsdewit) for **awesome** designs

###Used libraries
* [Android Design Library](http://developer.android.com/tools/support-library/index.html)
* [Retrofit](https://github.com/square/retrofit)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [Assent](https://github.com/afollestad/assent)

License
====
```
The MIT License (MIT)

Copyright (c) 2016 Niels Masdorp

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
