package it.unibo.mvc.utilities;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class ParametersReader implements Iterable<Integer>, Closeable {
    private BufferedReader reader;

    public ParametersReader() {
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/config.yml"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new ParametersIterator();
    } 

    class ParametersIterator implements Iterator<Integer>{
        private String current;

        @Override
        public boolean hasNext() {
            String riga = null;
            try {
                riga = reader.readLine();
            } catch (IOException e) { e.printStackTrace(); }
            if (riga == null){
                    return false;
            }
            current = riga;
            return true;
        }

        @Override
        public Integer next() {
            return getParameter(current);
        }

        private int getParameter(String line){
        String[] result = line.split(": ");
        return Integer.parseInt(result[result.length - 1]);
    }

    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    
    


}
