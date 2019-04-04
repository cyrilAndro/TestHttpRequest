@file:Suppress("DEPRECATION")

package com.example.testhttprequest

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.GridView
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.HttpURLConnection
import java.net.MalformedURLException


@Suppress ("DEPRECATION")
class JSONDownloader(private var c: Context, private var jsonURL: String, private var myGridView : GridView) : AsyncTask<Void, Void, String>() {


    private lateinit var pd : ProgressDialog

    // Connect to NetWork via HTTPURLConnection
    // ***

    // ***
    private fun connect (jsonURL: String) : Any {

        try{
            val url = URL(jsonURL)
            val con = url.openConnection() as HttpURLConnection

            // Con Props

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            return con
        } catch (e:MalformedURLException){
            e.printStackTrace()
            return "URL ERROR" + e.message

        } catch (e: IOException){
            e.printStackTrace()
            return "CONNECT ERROR" + e.message

        }

    }

    // ***
    // Download JsonData
    // ***
    private fun download() : String {

        val connection = connect(jsonURL)
        if(connection.toString().startsWith("Error")){
            return connection.toString()
        }
        // DOWNLOAD
        try{
            val con = connection as HttpURLConnection
            // if response is HTTP OK
            if (con.responseCode == 200) {
                // GET INPUT FROM STREAM
            val `is` = BufferedInputStream(con.inputStream)
                val br = BufferedReader(InputStreamReader(`is`))

                val jsonData = StringBuffer()
                var line: String?

                do{
                    line = br.readLine()

                    if (line == null) {break}
                    jsonData.append(line + "\n");

                } while(true)

                // CLOSING RESOURCES
                br.close()
                `is`.close()

                // RETURN JSON
                return jsonData.toString()


            } else{
                return "Error" + con.responseMessage
            }
        } catch (e:IOException){
            e.printStackTrace()
            return "Error" + e.message
        }
    }

    // SHOW DIALOG WHILE DOWNLOADING DATAS
    override fun onPreExecute() {
        super.onPreExecute()

        pd = ProgressDialog(c)
        pd.setTitle("Download Json")
        pd.setMessage("Downloading... Please wait...")
        pd.show()
    }

    // DOWNLOADING IN BACKGROUND
    override fun doInBackground(vararg Voids: Void): String {
        return download()
    }

    // When BACKGROUNDWORK is finished, Dismiss Dialog and Pass Datas to JSONParser
    override fun onPostExecute(jsonData: String) {
        super.onPostExecute(jsonData)

        pd.dismiss()
        if(jsonData.startsWith("URL ERROR")){
            val error = jsonData
            Toast.makeText(c, error, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "MOST PROBABLY THE APP CANNOT CONNECT DUE TO WRONG URL SINCE MALFORMEDURLEXCEPTION WAS RAISED", Toast.LENGTH_LONG).show()

        } else if(jsonData.startsWith("CONNECT ERROR")) {
            val error = jsonData
            Toast.makeText(c, error, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "MOST PROBABLY THE APP CANNOT CONNECT TO ANY NETWORK SINCE IOEXCEPTION WAS RAISED", Toast.LENGTH_LONG).show()

        }
        else{
            // PARSE
            Toast.makeText(c, "Network Connection and Download Succesfull. Now Attempting to Parse", Toast.LENGTH_LONG).show()
            JSONParser(c, jsonData, myGridView).execute()
        }

    }


}