package kenthacks.eric.composr;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import nu.xom.Serializer;

/**
 * Created by Vanya on 10/25/2014.
 */
public class PatternToMUSICXML {
    Context context;
    //private String givenName = "";
    public PatternToMUSICXML(Context con) {
       context = con;
    }

    public void write(String pat, String givenName) throws IOException {
        try {

           File sdCard = Environment.getExternalStorageDirectory();
           FileOutputStream file = new FileOutputStream(sdCard.getAbsolutePath() +  "/composr_files" + "/" + givenName + ".musicxml");
           MusicXmlRenderer renderer = new MusicXmlRenderer();
           MusicStringParser parser = new MusicStringParser();
           parser.addParserListener(renderer);

           Toast.makeText(context, "Written to " + Environment.getExternalStorageDirectory() + "/composr_files" + "/" + givenName + ".musicxml", Toast.LENGTH_LONG).show();

           Pattern pattern = new Pattern(pat);

           parser.parse(pattern);

           Serializer serializer = new Serializer(file, "UTF-8");
           serializer.setIndent(4);
           serializer.write(renderer.getMusicXMLDoc());

           file.flush();
           file.close();
       }
       catch(UnsupportedEncodingException e) {
           e.printStackTrace();
       }
       catch(IOException e) {
           e.printStackTrace();
       }

    }
/*
        String musicXMLTitle = givenName; //"givenName" from text field where it prompts user for name of output file. must be filled.

        FileOutputStream file = c.openFileOutput(musicXMLTitle + ".xml", Context.MODE_PRIVATE);

        MusicXmlRenderer renderer = new MusicXmlRenderer();
        MusicStringParser parser = new MusicStringParser();
        parser.addParserListener(renderer);

        parser.parse(pat);

        Serializer serializer = new Serializer(file, "UTF-8"); //I guess.
        serializer.setIndent(4);
        serializer.write(renderer.getMusicXMLDoc());

        file.flush();
        file.close();
    }

   */
}
