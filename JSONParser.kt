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

@Suppress("DEPRECATION")
class JSONParser(private var c: Context, private var jsonData: String, private var myGridView: GridView) :
    AsyncTask<Void, Void, Boolean>() {


    private lateinit var pd: ProgressDialog
    private var topStoriesSciences = ArrayList<TopStoriesScience>()


    class TopStoriesScience(
        private var m_title: String,
        private var m_section: String,
        private var m_subsection: String,
        private var m_updated_date: String,
        private var m_url: String,
        private var m_byline: String,
        private var m_url_multimedia: String
    ) {

        fun getTitle(): String? {
            return m_title
        }

        fun getSection(): String? {
            return m_section
        }

        fun getSubsection(): String? {
            return m_subsection
        }

        fun getUpdated_date(): String? {
            return m_updated_date
        }

        fun getUrl(): String? {
            return m_url
        }

        fun getByline(): String? {
            return m_byline
        }

        fun getUrlMultimedia(): String {
            return m_url_multimedia
        }
    }

    class MrAdapter(private var c: Context, private var topStoriesSciences: ArrayList<TopStoriesScience>) :
        BaseAdapter() {


        override fun getCount(): Int {
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
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.row_model, viewGroup, false)
            }

            val titleTxt = convertView!!.findViewById<TextView>(R.id.titleTxt) as TextView
            val sectionTxt = convertView.findViewById<TextView>(R.id.sectionTxt) as TextView
            val subsectionTxt = convertView.findViewById<TextView>(R.id.subsectionTxt) as TextView
            val updatedDateTxt = convertView.findViewById<TextView>(R.id.updatedDateTxt) as TextView
            val urlTxt = convertView.findViewById<TextView>(R.id.urlTxt) as TextView
            val bylineTxt = convertView.findViewById<TextView>(R.id.bylineTxt) as TextView
            val urlMultimediaTxt = convertView.findViewById<TextView>(R.id.urlMultimediaTxt) as TextView


            val topStoriesSciences = this.getItem(i) as TopStoriesScience

            titleTxt.text = topStoriesSciences.getTitle()
            sectionTxt.text = topStoriesSciences.getSection()
            subsectionTxt.text = topStoriesSciences.getSubsection()
            updatedDateTxt.text = topStoriesSciences.getUpdated_date()
            urlTxt.text = topStoriesSciences.getUrl()
            bylineTxt.text = topStoriesSciences.getByline()
            urlMultimediaTxt.text = topStoriesSciences.getUrlMultimedia()


            convertView.setOnClickListener {
                Toast.makeText(c, topStoriesSciences.getTitle(), Toast.LENGTH_SHORT).show()
            }

            return convertView
        }
    }

    // PARSE JSON DATA

    private fun parse(): Boolean {



        try {


            var jo: JSONObject


            topStoriesSciences.clear()
            var topStoriesScience: TopStoriesScience


            jo = JSONObject(jsonData)
            val ja = jo.getJSONArray("results")



            for (i in 0 until ja.length()) {
                jo = ja.getJSONObject(i)


                val title = jo.getString("title")
                val section = jo.getString("section")
                val subsection = jo.getString("subsection")
                val updated_date = jo.getString("updated_date")
                val url = jo.getString("url")
                val byline = jo.getString("byline")


                val jam = jo.getJSONArray("multimedia")

                for (i in 0 until jam.length()) {

                        var jom = jam.getJSONObject(i)
                        val url_multimedia = jom.getString("url")


                        topStoriesScience =
                            TopStoriesScience(title, section, subsection, updated_date, url, byline, url_multimedia)
                        topStoriesSciences.add(topStoriesScience)

                    }
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
        if (isParsed!!) {
            //BIND
            myGridView.adapter = MrAdapter(c, topStoriesSciences)
        } else {
            Toast.makeText(
                c,
                "Unable to Parse that Data. Are you sure it is a valid Jsondata? Json Exception was raised ",
                Toast.LENGTH_LONG
            ).show()
            Toast.makeText(c, "This the data we are trying to Parse: " + jsonData, Toast.LENGTH_LONG).show()

        }
    }
}
