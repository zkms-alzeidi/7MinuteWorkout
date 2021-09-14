package com.zkmsz.a7minuteworkout


import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncorti.slidetoact.SlideToActView
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import org.intellij.lang.annotations.Language
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {


    private var restTimer:CountDownTimer?= null //var to Count Down Timer.
    private var restProgress= 0 //var to Count Up.
    private var restTimeDuration : Long = 10

    private var exerciseTimer:CountDownTimer?= null
    private var exerciseProgress= 0
    private var exerciseTimerDuration: Long= 30

    private var exerciseList:ArrayList<ExerciseModel>?= null
    private var currentExercisePosition= -1

    private var tts:TextToSpeech?= null

    private var player:MediaPlayer?= null

    private var exerciseAdapter: ExerciseStatuAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        //To set the action bat (ToolBar)
        setSupportActionBar(toolbar_exercise_activity)


        //all the buttons on ActionBar
        theButtonsInActionBar()

        tts= TextToSpeech(this,this)

        exerciseList= Constants.defaulteExerciseList()

        //To start directly
        setupRestView()

        setupExerciseStatusRecyclerView()
    }

    /**
     * the buttons in the action bar
     * and what they have to do.
     */
    private fun theButtonsInActionBar()
    {
        val actionbar= supportActionBar

        //to make a button to return to home page
        if(actionbar != null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        //when i click on the button
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }

    }

    private fun setRestProgressBar()
    {
        progressBar.progress= restProgress
        restTimer= object : CountDownTimer(restTimeDuration*1000,1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress= 10 - restProgress

                tvTimer.text= (10- restProgress).toString()

            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }

        }.start()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }


    private fun setupRestView()
    {

        try {
            player= MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping= false
            player!!.start()
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }


        llRestView.visibility= View.VISIBLE
        llExerciseView.visibility= View.GONE

        if(restTimer!= null)
        {
            restTimer!!.cancel()
            restProgress= 0
        }
        tvUpExerciseComing.text= exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()


    }


    //if close the activity when...
    override fun onDestroy() {

        if(restTimer!= null)
        {
            restTimer!!.cancel()
            restProgress= 0
        }

        if(exerciseTimer!= null)
        {
            exerciseTimer!!.cancel()
            exerciseProgress= 0
        }

        if(tts!= null)
        {
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!= null)
        {
            player!!.stop()
        }

        super.onDestroy()
    }


    private fun setExerciseProgress()
    {
        progressBarExercise.progress= exerciseProgress

        exerciseTimer= object:CountDownTimer(exerciseTimerDuration*1000, 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress= exerciseTimerDuration.toInt() - exerciseProgress
                tvTimerExercise.text= (exerciseTimerDuration.toInt() - exerciseProgress).toString()

            }

            override fun onFinish() {
                if(currentExercisePosition< exerciseList?.size!!-1)
                {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }
                else{
                    finish()
                    val intent= Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()

    }


    private fun setupExerciseView()
    {
        llRestView.visibility= View.GONE
        llExerciseView.visibility= View.VISIBLE

        if(exerciseTimer != null)
        {
            exerciseTimer!!.cancel()
            exerciseProgress= 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgress()

        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseView.text= exerciseList!![currentExercisePosition].getName()


    }


    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS)
        {
            val result= tts!!.setLanguage(Locale.ENGLISH)
            if(result== TextToSpeech.LANG_MISSING_DATA || result== TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","The language specified is not supported")
            }

        }
        else{
            Log.e("TTS","Initialization Failed")
        }

    }

    private fun speakOut(text:String)
    {
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView()
    {
        rvExerciseStatus.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter= ExerciseStatuAdapter(this,exerciseList!!)
        rvExerciseStatus.adapter= exerciseAdapter
    }

    private fun customDialogForBackButton()
    {

        val customDailog= Dialog(this)
        customDailog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDailog.tvYes.setOnClickListener {
            finish()
            customDailog.dismiss()
        }

        customDailog.tvNo.setOnClickListener {
            customDailog.dismiss()
        }
        customDailog.show()
    }
}
