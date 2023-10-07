package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

import md2html.markup.*;

public class Md2Html {
    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))
        ) {
            while (true) {
                StringBuilder paragraph = new StringBuilder();
                String s;
                while (true) {
                    s = in.readLine();
                    if (s == null || s.isEmpty()) break;
                    paragraph.append(s).append("\n");
                }
                if (!paragraph.isEmpty()) {
                    paragraph.deleteCharAt(paragraph.length() - 1);
                    out.write(toHtml(paragraph.toString()));
                    out.write('\n');
                }
                if (s == null) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toHtml(String paragraph) {
        Paragraph p = new Paragraph(paragraph);
        StringBuilder result = new StringBuilder();
        p.toHTML(result);
        return result.toString();
    }
}
