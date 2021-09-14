package com.zkmsz.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_historyactivity.*

class Historyactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historyactivity)

        setSupportActionBar(toolbar_history_activity)

        val actionbar= supportActionBar

        if(actionbar != null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "History"
        }

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()

    }

    private fun getAllCompletedDates()
    {
        val dbHandler= SqliteOpenHelper(this,null)
        val allCompletedDatesList= dbHandler.getAllComlpletedDateList()

        if (allCompletedDatesList.size > 0 )
        {
            tvHistory.visibility=View.VISIBLE
            rvHistory.visibility= View.VISIBLE
            tvNoDataAvailable.visibility= View.GONE

            rvHistory.layoutManager= LinearLayoutManager(this)
            val historyAdapter= HistoryAdapter(this, allCompletedDatesList)
            rvHistory.adapter= historyAdapter
        }
        else
        {
            tvHistory.visibility=View.GONE
            rvHistory.visibility= View.GONE
            tvNoDataAvailable.visibility= View.VISIBLE
        }
    }

}
