package kenthacks.eric.composr;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;

import android.content.Context;

public class RecordingTask {

    // set in the constructor
    int tempo;
    int beat = 0;
    boolean start = false;
    final int INTERVAL = 4;
    Metronome metronome;
    Context ctx;
    FrequencyRecorder fr;
    RecordedFrequencies rf;

    // set elsewhere
    Timer timer;
    TimerTask task;
    boolean recording = false;
    public String pattern = "";
    public String displayPattern = "";

    public RecordingTask(int tempo, Context ctx){
        this.tempo = tempo;
        this.metronome = new Metronome(tempo, ctx);
        this.ctx = ctx;
        this.fr = new FrequencyRecorder(ctx);
        rf = new RecordedFrequencies();
    }

    TimerTask createTimerTask(){
        task = new TimerTask(){
            @Override
            public void run() {
                RecordedFrequencies previous_frequencies = rf;
                float median = previous_frequencies.getMedian();
                String note = fr.getNoteFromFrequency(median);

                if(start) {
                   pattern += note + " ";
                    beat++;
                    if (note == "R")
                        displayPattern += "- ";
                    else {
                        displayPattern += note + " ";
                    }
                    if (beat == INTERVAL) {
                        displayPattern += "| ";
                        pattern += "| ";
                        beat = 0;
                    }

                    if (displayPattern.length() > 20)
                        displayPattern = displayPattern.substring(displayPattern.length() - 20, displayPattern.length());

                    rf.reset();
                    Log.i("MEDIAN FREQ", "------------" + pattern + "--------------");
                }
                else {
                    beat++;
                    int countDown = 4 - beat;
                    displayPattern = ""+ countDown;
                    if(beat == INTERVAL) {
                        displayPattern = "";
                        start = true;
                        beat = 0;
                    }
                }
                metronome.playTick();
            }
        };
        return task;
    }

    void toggle(){
        this.recording = !this.recording;
        Log.i("rt_state", String.valueOf(this.recording));
        if (this.recording){
            pattern = "";
            displayPattern = "";
            this.timer = new Timer();
            this.task = createTimerTask();
            this.timer.schedule(this.task, new Date(), 60000/tempo);
        }
        else {
            while(beat < INTERVAL) {
                pattern += "R ";
                beat++;
            }
            start = false;
//            pattern += "|";
            timer.cancel();
            timer.purge();
            beat = 0;
        }
    }

}
