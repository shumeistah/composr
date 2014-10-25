package kenthacks.eric.composr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import java.util.Hashtable;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FrequencyRecorder FREQ = new FrequencyRecorder(this);

        FREQ.initializeMidiValues();

        final Metronome m = new Metronome(70, this);      // start metronome

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Button metronomeButton = (Button) findViewById(R.id.Toggle);
        metronomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m.toggleMetronome();
            }
        });

        final Button recordButton = (Button) findViewById(R.id.frequencies);
        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //FREQ.writeFrequencies(); not working yet
            }
        });

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                final float pitchInHz = pitchDetectionResult.getPitch();
                final String note = FREQ.getNoteFromFrequency(pitchInHz);

                FREQ.addToFrequencyArray(pitchInHz);

                runOnUiThread((Runnable) new Runnable() {
                    @Override
                    public void run() {
                        TextView text = (TextView) findViewById(R.id.textView1);
                        text.setText("" + pitchInHz + "\n" + note);
                    }
                });
            }
        }));
        new Thread(dispatcher, "Audio Dispatcher").start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
