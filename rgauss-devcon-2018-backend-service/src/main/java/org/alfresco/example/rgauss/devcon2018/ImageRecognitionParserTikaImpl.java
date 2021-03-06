/*
 * Copyright 2018 Alfresco Software, Ltd.
 *
 * This example is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This example is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this example. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.example.rgauss.devcon2018;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.recognition.ObjectRecognitionParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Calls Tika to parse a given content input stream and return the recognition tag results
 */
public class ImageRecognitionParserTikaImpl implements ImageRecognitionParser
{
    @Autowired
    private Tika tika;

    public List<RecognitionTagResult> parse(String contentId, InputStream inputStream) throws Exception
    {
        try
        {
            List<RecognitionTagResult> recognitionTagResults = new ArrayList<RecognitionTagResult>();
            Metadata metadata = new Metadata();
            
            // Call Tika to parse (which passes to TensorFlow)
            tika.parse(inputStream, metadata);
            
            // Convert the Tika results into our POJO 
            String[] results = metadata.getValues(ObjectRecognitionParser.MD_KEY_OBJ_REC);
            for (int i = 0; i < results.length; i++)
            {
                String[] resultParts = results[i].split("\\(");
                String[] tags = resultParts[0].split(", ");
                Float confidence = Float.valueOf(resultParts[1].substring(0, resultParts[1].length() - 1));
                for (int j = 0; j < tags.length; j++)
                {
                    recognitionTagResults.add(
                            new RecognitionTagResult(contentId, tags[j].trim(), confidence, new Date()));
                }
            }
            return recognitionTagResults;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }
}
