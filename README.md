# Guardian News App, Stage 2
News feed app for the Android Basics Nanodegree Program. It uses the Guardian API to fetch news articles from their site. The API can be found here:
http://open-platform.theguardian.com/documentation/

## Lessons of this app:
- Connecting to an API
- Parsing the JSON response
- Handling error cases gracefully
- Updating information regularly
- Using an AsyncTask
- Doing network operations independent of the Activity lifecycle

## Note:
Since this app requires use ofthe Guardian API, you will need to supply your own API KEY. Once you have that, you can app it to the app one of two ways:
1) Add it to your Gradle build configuration, so that it's not hard-coded in the source code. CHeck out this link to learn more:
https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906

2) If you must hard-code the key directly into source code, go into the MainActivity.java file, search for the GUARDIAN_API_KEY_VALUE, and change its String value to your key.