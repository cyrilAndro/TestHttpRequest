package com.example.testhttprequest

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Suppress ("DEPRECATION")
class JSONParser(private var c: Context, private var jsonData: String, private var myGridView: GridView) : AsyncTask<Void, Void, Boolean>() {


    private lateinit var pd : ProgressDialog
    private var topStoriesSciences = ArrayList<TopStoriesScience>()

    class TopStoriesScience (private var m_status : String,
                             private var m_copyright : String, private var m_section : String, private var m_last_updated : String, private var m_num_results : Int, private var m_results : ArrayList<String>) {

        fun getStatus() : String? {
            return m_status
        }

        fun getCopyright() : String? {
            return m_copyright
        }

        fun getSection() : String? {
            return m_section
        }

        fun getLastUpdated() : String? {
            return m_last_updated
        }
        fun getNumResults() : Int? {
            return m_num_results
        }
        fun getResults() : ArrayList<String>? {
            return m_results
        }
    }

    class MrAdapter (private var c: Context, private var topStoriesSciences: ArrayList<TopStoriesScience>) : BaseAdapter() {



        override fun getCount() : Int {
            return topStoriesSciences.size
        }

        override fun getItem(pos: Int): Any {
            return topStoriesSciences[pos]
        }

        override fun getItemId(pos: Int): Long {
           return pos.toLong()
        }
        //Inflate row_moadel and return it
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            var convertView = view
            if(convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false)
            }

            val statusTxt = convertView!!.findViewById<TextView>(R.id.titleTxt) as TextView
            val copyrightTxt = convertView.findViewById<TextView>(R.id.sectionTxt) as TextView
            val sectionTxt = convertView.findViewById<TextView>(R.id.subsectionTxt) as TextView
            val lastUpdatedTxt = convertView.findViewById<TextView>(R.id.urlArticleTxt) as TextView
            val numResultsTxt = convertView.findViewById<TextView>(R.id.dateTxt) as TextView
            val resultsTxt = convertView.findViewById<TextView>(R.id.resultsTxt) as TextView


            val topStoriesSciences = this.getItem(i) as TopStoriesScience

            statusTxt.text = topStoriesSciences.getStatus()
            copyrightTxt.text = topStoriesSciences.getCopyright()
            sectionTxt.text = topStoriesSciences.getSection()
            lastUpdatedTxt.text = topStoriesSciences.getLastUpdated()
            numResultsTxt.text = topStoriesSciences.getNumResults().toString()
            resultsTxt.text = topStoriesSciences.getResults().toString()


            convertView.setOnClickListener {Toast.makeText(c, topStoriesSciences.getStatus(), Toast.LENGTH_SHORT).show() }

            return convertView
        }
    }

    // PARSE JSON DATA

    private fun parse(): Boolean {



        try {

                var jo: JSONObject
                val ja = JSONArray(jsonData)

                topStoriesSciences.clear()
                var topStoriesScience: TopStoriesScience



                for (i in 0 until ja.length()) {
                    jo = ja.getJSONObject(i)

                    val status = jo.getString("status")
                    val copyright = jo.getString("copyright")
                    val section = jo.getString("section")
                    val last_updated = jo.getString("last_updated")
                    val num_results = jo.getInt("num_results")
                    val results :  ArrayList<String> = jo.getJSONArray("results") as ArrayList<String>


                    topStoriesScience = TopStoriesScience(status, copyright, section, last_updated, num_results, results)
                    topStoriesSciences.add(topStoriesScience)

                }

                return true

            } catch (e: JSONException) {
                e.printStackTrace()
                return false

            }
        }



    override fun onPreExecute() {
        super.onPreExecute()

        pd = ProgressDialog(c)
        pd.setTitle("Parse Json")
        pd.setMessage("Parsing... Please Wait")
        pd.show()
    }

    override fun doInBackground(vararg voids: Void): Boolean? {
       return parse()
    }

    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)

        pd.dismiss()
        if(isParsed!!){
            //BIND
            myGridView.adapter = MrAdapter(c, topStoriesSciences  )
        } else {
            Toast.makeText(c, "Unable to Parse that Data. Are you sure it is a valid Jsondata? Json Exception was raised ", Toast.LENGTH_LONG).show()
            Toast.makeText(c, "This the data we are trying to Parse: " + jsonData, Toast.LENGTH_LONG).show()

        }
     }
}
