package isil.edu.pe.integracion.sesion1.demo1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request

/*
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.TwitterV2
import twitter4j.TwitterV2Impl
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
*/

class XActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x)
        findViewById<Button>(R.id.btnPostTweet).setOnClickListener {
            val tweetText = findViewById<EditText>(R.id.etTweet).text.toString()
            postToTwitter(tweetText)
        }
    }

    fun postToTwitter(text: String) {
        val API_KEY = "RYtrkJUgWGRBfNLj43zFVkeeV"
        val API_SECRET = "W9CU8RzIGZQ1wDjVTgcV66TY9ei3Ooiu09aTx04oqhS3UnczaB"
        val ACCESS_TOKEN = "1900423461077737472-R7ybZFArGtGdndBJfDDAwC8mhbpdgt"
        val ACCESS_SECRET = "8iEd3qNmhDLstaJGCKfGTmKyQz3P8bl8A6kb8P8hhWEaA"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //-- Obteniendo el API de X mediante OAuth 1.0
                val service: OAuth10aService = ServiceBuilder(API_KEY)
                                                        .apiSecret(API_SECRET)
                                                        .build(TwitterApi.instance())
                //-- Obteniendo el Request
                val request = OAuthRequest(Verb.POST, "https://api.twitter.com/2/tweets")
                request.addHeader("Content-Type", "application/json")
                request.setPayload("""{"text": "$text"}""")
                service.signRequest(
                    com.github.scribejava.core.model.OAuth1AccessToken(ACCESS_TOKEN, ACCESS_SECRET),
                    request
                )
                //-- Obteniendo el Response
                val response: Response = service.execute(request)
                println(response.body)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*
    private fun postToTwitter(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //val bearerToken = "AAAAAAAAAAAAAAAAAAAAAC21zwEAAAAAn7rvnDRGQie9onF9DqE44DJ1cak%3Dyj8BI7Vq1703UP1QDeANfSBkoxFL0NFXNUBZqd3cdYdUMAHc8y"
                val accessToken = "1900423461077737472-R7ybZFArGtGdndBJfDDAwC8mhbpdgt"
                val client = OkHttpClient()
                val mediaType = "application/json".toMediaTypeOrNull()
                val requestBody = """{"text": $text}""".toRequestBody(mediaType)
                val request = Request.Builder()
                    .url("https://api.twitter.com/2/tweets")
                    .post(requestBody)
                    //.addHeader("Authorization", "Bearer $bearerToken")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .addHeader("Content-Type", "application/json")
                    .build()
                client.newCall(request).execute().use { response ->
                    println(response.body?.string())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    */

    /*
    private fun postToTwitter(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val twitterV2 = getTwitterInstanceV2()
                val response = twitterV2.createTweet(text)
                println("Tweet publicado con éxito. ID: ${response.id}")
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
        }
    }

    private fun getTwitterInstanceV11(): Twitter {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey("vg7NTxf4MAHSMKeevOzApLfMU")
            .setOAuthConsumerSecret("w6qN7SH5Mx4fMlRAwhnsx0KpPOUTA0FVB4DnIfhukyHY3QglZl")
            .setOAuthAccessToken("1900423461077737472-mc9TzPCTvQ2UrDdphO30c4e2SrLK0Z")
            .setOAuthAccessTokenSecret("laWc9QV9CuymjBfb6OWTYkLP4NsacxJBSdKqCZ6eIowjJ")
        val tf = TwitterFactory(cb.build())
        val twitter = tf.instance
        return twitter
    }

    private fun getTwitterInstanceV2(): TwitterV2 {
        val twitter = TwitterFactory.getSingleton().apply {
            setOAuthConsumer("RYtrkJUgWGRBfNLj43zFVkeeV",
                             "W9CU8RzIGZQ1wDjVTgcV66TY9ei3Ooiu09aTx04oqhS3UnczaB")
            oAuthAccessToken = AccessToken("1900423461077737472-R7ybZFArGtGdndBJfDDAwC8mhbpdgt",
                                           "8iEd3qNmhDLstaJGCKfGTmKyQz3P8bl8A6kb8P8hhWEaA")
        }
        //Bearer: AAAAAAAAAAAAAAAAAAAAAC21zwEAAAAAn7rvnDRGQie9onF9DqE44DJ1cak%3Dyj8BI7Vq1703UP1QDeANfSBkoxFL0NFXNUBZqd3cdYdUMAHc8y
        val twitterV2 = TwitterV2Impl(twitter)
        return twitterV2
    }
    */

}
